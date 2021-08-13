package my.edu.utar.assignmentnightmare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    private ImageView btnUploadProduct, btnUpdateProfile, btnMyProduct;
    private CircleImageView civProfileImg;
    private TextView tvProfileName;
    private FirebaseAuth mAuth;
    private DatabaseReference userProfileRef;
    private String currentUserId;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.botnav);
        //initialize check
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.home:
                    item.setChecked(true);
                    startActivity(new Intent(Profile.this, Homepagee.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    finish();
                    break;
                case R.id.category:
                    item.setChecked(true);
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    finish();
                    break;
                case R.id.message:
                    item.setChecked(true);
                    //overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    //finish();
                    break;
                case R.id.cart:
                    item.setChecked(true);
                    startActivity(new Intent(Profile.this, ShoppingCart.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    finish();
                    break;
                case R.id.profile:
                    item.setChecked(true);
                   break;
            }
            return false;
        });

        //Done by Jiun Lin
        btnUploadProduct = (ImageView) findViewById(R.id.buttonUploadProduct);
        btnUploadProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToUploadActivity();
            }
        });

        btnUpdateProfile = (ImageView) findViewById(R.id.buttonUpdateProfile);
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToUpdateProfileActivity();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getUid();
        userProfileRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("user profile");

        civProfileImg = (CircleImageView) findViewById(R.id.profilepic);
        tvProfileName = (TextView) findViewById(R.id.profilename);

        userProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //retrieve data from firebase
                String username = snapshot.child("username").getValue().toString();
                Uri profileImg = Uri.parse(snapshot.child("profile image").getValue().toString());
                //display profile image and username on respective field
                Picasso.get().load(profileImg).into(civProfileImg);
                tvProfileName.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        btnMyProduct = (ImageView) findViewById(R.id.btnMyProduct);
        btnMyProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToMyProductPage();
            }
        });

        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                sendToLoginPage();
            }
        });

    }

    private void sendToMyProductPage() {
        startActivity(new Intent(Profile.this, MyProductActivity.class));
    }

    private void sendToUpdateProfileActivity() {
        startActivity(new Intent(Profile.this, UpdateProfileActivity.class));
    }

    private void sendToUploadActivity() {
        startActivity(new Intent(Profile.this,UploadActivity.class));
    }
    private void sendToLoginPage() {
        startActivity(new Intent(Profile.this, LoginPage.class));
    }
}
