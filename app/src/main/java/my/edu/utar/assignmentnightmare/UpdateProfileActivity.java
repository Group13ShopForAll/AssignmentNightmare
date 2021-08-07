package my.edu.utar.assignmentnightmare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

// Done by Jiun Lin
public class UpdateProfileActivity extends AppCompatActivity {

    private ImageButton btnAddProfileImage;
    private EditText edtUsername, edtContactNum, edtAddressLine1, edtAddressLine2;
    private TextView tvEmail, btnUpdateProfile, btnResetPassword;

    private FirebaseAuth mAuth;
    private DatabaseReference userProfileRef;
    private StorageReference userProfileImgRef;

    private String currentUserId;
    private String username, email, contactNum, addressLine1, addressLine2;
    private Uri profileImgUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getUid();
        email = mAuth.getCurrentUser().getEmail();

        userProfileRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("user profile");
        userProfileImgRef = FirebaseStorage.getInstance().getReference().child(currentUserId).child("profile image");

        btnAddProfileImage = (ImageButton) findViewById(R.id.btnAddProfileImage);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtContactNum = (EditText) findViewById(R.id.edtContactNum);
        edtAddressLine1 = (EditText) findViewById(R.id.edtAddressLine1);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        btnUpdateProfile = (TextView) findViewById(R.id.btnUpdateProfile);
        btnResetPassword = (TextView) findViewById(R.id.btnResetPassword);

        // everytime user had click the update profile,
        // system would first check if the user info exist in firebase or not
        // if the info is existed, then display all related info

        // display the user profile img
        userProfileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // get all the related info from firebase
                username = snapshot.child("username").getValue().toString();
                email = snapshot.child("email").getValue().toString();
                contactNum = snapshot.child("contactNum").getValue().toString();
                addressLine1 = snapshot.child("addressLine1").getValue().toString();
                addressLine2 = snapshot.child("addressLine2").getValue().toString();
                profileImgUri = Uri.parse(snapshot.child("profileImgUri").getValue().toString());
                
                if (profileImgUri!=null)
                    Picasso.get().load(profileImgUri).into(btnAddProfileImage);
                if (username!=null)
                    edtUsername.setText(username);
                if (email!=null)
                    tvEmail.setText(email);
                if (contactNum!=null)
                    edtContactNum.setText(contactNum);
                if (addressLine1!=null)
                    edtAddressLine1.setText(addressLine1);
                if (addressLine2!=null)
                    edtAddressLine2.setText(addressLine2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateProfileActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        });

    }
}