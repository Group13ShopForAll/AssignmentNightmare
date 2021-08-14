package my.edu.utar.assignmentnightmare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

// Done by Jiun Lin
public class UpdateProfileActivity extends AppCompatActivity {

    private CircleImageView civUpdateProfileImg;
    private EditText edtUpdateUsername, edtUpdateContactNum, edtUpdateAddress;
    private TextView tvUpdateEmail, btnUpdateProfileInfo, btnResetPassword;

    private ProgressBar pbUpdateProfile;
    private ImageButton imgBtnBackToHomepage;
    private FirebaseAuth mAuth;
    private DatabaseReference userProfileRef;
    private StorageReference userProfileImgRef;

    private static int Gallery_Pick = 1;
    private boolean profileImgChanged = false;

    private String currentUserId, saveCurrentDate, saveCurrentTime, profileImgName,downloadUri;
    private String username, email, contactNum, address;
    private Uri profileImgUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getUid();
        email = mAuth.getCurrentUser().getEmail();

        userProfileRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("user profile");
        userProfileImgRef = FirebaseStorage.getInstance().getReference().child(currentUserId).child("profile images");

        imgBtnBackToHomepage = (ImageButton) findViewById(R.id.backprofile);
        civUpdateProfileImg = (CircleImageView) findViewById(R.id.civUpdateProfileImg);
        edtUpdateUsername = (EditText) findViewById(R.id.edtUpdateUsername);
        edtUpdateContactNum = (EditText) findViewById(R.id.edtUpdateContactNum);
        edtUpdateAddress = (EditText) findViewById(R.id.edtUpdateAddress);
        tvUpdateEmail = (TextView) findViewById(R.id.tvUpdateEmail);
        btnUpdateProfileInfo = (TextView) findViewById(R.id.btnUpdateProfileInfo);
        btnResetPassword = (TextView) findViewById(R.id.btnResetPassword);

        pbUpdateProfile = (ProgressBar) findViewById(R.id.pbUpdateProfile);

        tvUpdateEmail.setText(email);

        // everytime user had click the update profile,
        // system would first check if the user info exist in firebase or not
        // if the info is existed, then display all related info

        // display the user profile img
        userProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username = snapshot.child("username").getValue().toString();
                edtUpdateUsername.setText(username);
                edtUpdateUsername.setTextColor(Color.WHITE);
                contactNum = snapshot.child("contactNum").getValue().toString();
                edtUpdateContactNum.setText(contactNum);
                edtUpdateContactNum.setTextColor(Color.WHITE);
                address = snapshot.child("address").getValue().toString();
                edtUpdateAddress.setText(address);
                edtUpdateAddress.setTextColor(Color.WHITE);
                profileImgUri = Uri.parse(snapshot.child("profile image").getValue().toString());
                Picasso.get().load(profileImgUri).into(civUpdateProfileImg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateProfileActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        civUpdateProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        // check if user had filled all the info
        btnUpdateProfileInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUpdateProfileInfo();
            }
        });
        // send
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResetPasswordEmail();
                AlertDialog resetPasswordDialog = new AlertDialog.Builder(v.getContext()).setTitle("Reset Password").
                        setMessage("Reset password link had been sent to your email")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).show();
            }
        });
        imgBtnBackToHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sendResetPasswordEmail() {
        mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(UpdateProfileActivity.this, "Reset Link sent to your email", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateUpdateProfileInfo() {
        username = edtUpdateUsername.getText().toString();
        email = tvUpdateEmail.getText().toString();
        contactNum = edtUpdateContactNum.getText().toString();
        address = edtUpdateAddress.getText().toString();

        if (TextUtils.isEmpty(username)){
            edtUpdateUsername.setError("Please type in username");
            return;
        }
        if (TextUtils.isEmpty(contactNum)){
            edtUpdateContactNum.setError("Please type in contact number");
            return;
        }
        if (TextUtils.isEmpty(address)){
            edtUpdateAddress.setError("Please type in address");
            return;
        }
        if (profileImgChanged == false){
            downloadUri = profileImgUri.toString();
            updatePersonalInfoToFirebase();
        }else
            updateProfileImgToFirebaseStorage();
    }

    private void updateProfileImgToFirebaseStorage() {

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-YYYY");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForTime.getTime());

        // generate random img file name using current date and time
        profileImgName = saveCurrentDate + " " + saveCurrentTime;
        StorageReference imgPath = userProfileImgRef.child(profileImgUri.getLastPathSegment() + profileImgName);

        // start update info
        pbUpdateProfile.setVisibility(View.VISIBLE);
        imgPath.putFile(profileImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        downloadUri = uri.toString();
                        Toast.makeText(UpdateProfileActivity.this, "Image uploaded to firebase storage successfully", Toast.LENGTH_SHORT).show();
                        updatePersonalInfoToFirebase();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateProfileActivity.this, "Error occurred "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateProfileActivity.this, "Error occurred "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePersonalInfoToFirebase() {
        pbUpdateProfile.setVisibility(View.VISIBLE);
        HashMap updateProfileMap = new HashMap();
        updateProfileMap.put("username", username);
        updateProfileMap.put("contactNum", contactNum);
        updateProfileMap.put("address",address);
        updateProfileMap.put("profile image", downloadUri);

        userProfileRef.updateChildren(updateProfileMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                pbUpdateProfile.setVisibility(View.INVISIBLE);
                Toast.makeText(UpdateProfileActivity.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                sendToProfileActivity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pbUpdateProfile.setVisibility(View.INVISIBLE);
                Toast.makeText(UpdateProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendToProfileActivity() {
        startActivity(new Intent(UpdateProfileActivity.this,Profile.class));
    }

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
            civUpdateProfileImg.setImageURI(profileImgUri);
            // set the boolean flag to true
            profileImgChanged = true;
        }else{
            Toast.makeText(UpdateProfileActivity.this, "Error occurred while picking image from local storage.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

}