package my.edu.utar.assignmentnightmare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class Checkout extends AppCompatActivity {

    TextView adress;
    private DatabaseReference userProfileRef;
    private FirebaseAuth mAuth;
    private String currentUserId;
    TextView directvoucher;
    Button backbutt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        backbutt = (Button) findViewById(R.id.backbutt);
        String shipname = getIntent().getStringExtra("shipname");
        Double total = getIntent().getDoubleExtra("total", 0.00);
        Double shipprice = getIntent().getDoubleExtra("shipprice",0.00);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getUid();
        adress = (TextView) findViewById(R.id.adress);
        userProfileRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("user profile");

        userProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String address = snapshot.child("address").getValue().toString();
                adress.setText(address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Checkout.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        directvoucher = (TextView) findViewById(R.id.directvoucher);

        directvoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Checkout.this, Voucher.class));
            }
        });

        backbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}