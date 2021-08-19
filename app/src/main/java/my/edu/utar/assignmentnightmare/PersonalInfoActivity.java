package my.edu.utar.assignmentnightmare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
// Done by Jiun Lin
public class PersonalInfoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference userProfileRef;
    private StorageReference userProfileImgRef;

    private ImageButton btnAddProfileImage;
    private EditText edtUsername, edtContactNum, edtAddress;
    private Uri profileImgUri;
    private TextView btnAddPersonalInfo, tvAlertProfileImg;
    private ProgressBar pbAddPersonalInfo;

    private String username, contactNum, address;
    private static int Gallery_Pick = 1;
    private String saveCurrentDate, saveCurrentTime, profileImgName, currentUserId, downloadUri;

    private Intent intentFromRegisterPage;
    private String emailAcc, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        btnAddProfileImage = (ImageButton) findViewById(R.id.btnAddProfileImage);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtContactNum = (EditText) findViewById(R.id.edtContactNum);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        btnAddPersonalInfo = (TextView) findViewById(R.id.btnAddPersonalInfo);
        pbAddPersonalInfo = (ProgressBar) findViewById(R.id.pbAddPersonalInfo);
        tvAlertProfileImg = (TextView) findViewById(R.id.tvAlertProfileImg);

        mAuth = FirebaseAuth.getInstance();

        intentFromRegisterPage = getIntent();
        emailAcc = intentFromRegisterPage.getStringExtra("emailAcc");
        password = intentFromRegisterPage.getStringExtra("password");

        btnAddProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnAddPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePersonalInfo();
            }
        });

    }

    private void validatePersonalInfo() {
        username = edtUsername.getText().toString();
        contactNum = edtContactNum.getText().toString();
        address = edtAddress.getText().toString();

        if (TextUtils.isEmpty(username)){
            edtUsername.setError("Please type in your username");
            return;
        }
        if (TextUtils.isEmpty(contactNum)){
            edtContactNum.setError("Please type in your contact number");
            return;
        }
        if (TextUtils.isEmpty(address)){
            edtAddress.setError("Please type in your address");
            return;
        }
        if (btnAddProfileImage.getDrawable() == null){
            tvAlertProfileImg.setVisibility(View.VISIBLE);
            return;
        }
        if (btnAddProfileImage.getDrawable() != null){
            tvAlertProfileImg.setVisibility(View.INVISIBLE);
        }
        registerAccount();
    }

    private void registerAccount() {
        mAuth.createUserWithEmailAndPassword(emailAcc,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // once account registration success, further define the path for storing profile image and info
                currentUserId = mAuth.getUid();
                userProfileRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("user profile");
                userProfileImgRef = FirebaseStorage.getInstance().getReference().child(currentUserId).child("profile images");

                Toast.makeText(PersonalInfoActivity.this, "Successfully created new account", Toast.LENGTH_SHORT).show();
                insertProfileImgToFirebaseStorage();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PersonalInfoActivity.this, "Failed created account, "+e.getMessage(), Toast.LENGTH_SHORT).show();
                pbAddPersonalInfo.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void insertProfileImgToFirebaseStorage() {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-YYYY");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForTime.getTime());

        // generate random img file name using current date and time
        profileImgName = saveCurrentDate + " " + saveCurrentTime;
        StorageReference imgPath = userProfileImgRef.child(profileImgUri.getLastPathSegment() + profileImgName);

        // start the progress bar which indicates starting of storing process
        pbAddPersonalInfo.setVisibility(View.VISIBLE);
        imgPath.putFile(profileImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        downloadUri = uri.toString();
                        Toast.makeText(PersonalInfoActivity.this, "Image uploaded to firebase storage successfully", Toast.LENGTH_SHORT).show();
                        savePersonalInfoToFirebase();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PersonalInfoActivity.this, "Error occurred "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PersonalInfoActivity.this, "Error occurred "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void savePersonalInfoToFirebase() {
        HashMap personalInfoMap = new HashMap();
        personalInfoMap.put("username", username);
        personalInfoMap.put("contactNum",contactNum);
        personalInfoMap.put("address",address);
        personalInfoMap.put("profile image",downloadUri);

        userProfileRef.updateChildren(personalInfoMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                pbAddPersonalInfo.setVisibility(View.INVISIBLE);
                Toast.makeText(PersonalInfoActivity.this, "Personal Info has been uploaded", Toast.LENGTH_SHORT).show();
                sendToLoginPage();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PersonalInfoActivity.this, "Error occurred "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendToLoginPage() {
        startActivity(new Intent(PersonalInfoActivity.this, LoginPage.class));
    }

    // open device gallery
    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Gallery_Pick && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            profileImgUri = data.getData();
            btnAddProfileImage.setImageURI(profileImgUri);
        }else{
            Toast.makeText(PersonalInfoActivity.this, "Error occurred while picking image from local storage.", Toast.LENGTH_SHORT).show();
        }
    }

}