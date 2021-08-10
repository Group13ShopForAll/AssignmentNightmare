package my.edu.utar.assignmentnightmare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductActivity extends AppCompatActivity {

    private DatabaseReference productRef, sellerRef;

    private ImageView ivItemSoldImage;
    private TextView tvItemSoldName, tvItemSoldPrice, tvItemSoldSeller, tvItemSoldDesc, tvItemSoldQuantity, btnBuyNow;
    private ImageButton imgBtnBackToHomepage;

    private Intent intentFromProductAdapter;

    private String productKey;
    private String productName, productPrice, productStock, productDesc, sellerId, sellerUsername;
    private Uri productImgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        intentFromProductAdapter = getIntent();
        productKey = intentFromProductAdapter.getStringExtra("productKey");

        productRef = FirebaseDatabase.getInstance().getReference().child("homepage").child("products").child(productKey);

        imgBtnBackToHomepage = (ImageButton) findViewById(R.id.imgBtnBackToHomepage);
        btnBuyNow = (TextView) findViewById(R.id.btnBuyNow);

        ivItemSoldImage = (ImageView) findViewById(R.id.ivItemSoldImage);
        tvItemSoldName = (TextView) findViewById(R.id.tvItemSoldName);
        tvItemSoldPrice = (TextView) findViewById(R.id.tvItemSoldPrice);
        tvItemSoldSeller = (TextView) findViewById(R.id.tvItemSoldSeller);
        tvItemSoldDesc = (TextView) findViewById(R.id.tvItemSoldDesc);
        tvItemSoldQuantity = (TextView) findViewById(R.id.tvItemSoldQuantity);

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // retrieve the product related info from firebase database
                productName = snapshot.child("productName").getValue().toString();
                productPrice = snapshot.child("productPrice").getValue().toString();
                productStock = snapshot.child("productStock").getValue().toString();
                productDesc = snapshot.child("productDesc").getValue().toString();
                sellerId = snapshot.child("sellerId").getValue().toString();
                productImgUri = Uri.parse(snapshot.child("productImgUri").getValue().toString());

                sellerRef = FirebaseDatabase.getInstance().getReference().child("users").child(sellerId);
                sellerRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        sellerUsername = snapshot.child("user profile").child("username").getValue().toString();
                        tvItemSoldSeller.setText(sellerUsername);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ProductActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

                // assign all the variable to the respective field on product description page
                Picasso.get().load(productImgUri).into(ivItemSoldImage);
                tvItemSoldName.setText(productName);
                tvItemSoldPrice.setText(productPrice);
                tvItemSoldDesc.setText(productDesc);
                tvItemSoldQuantity.setText(productStock);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProductActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        // when click on the back button return back to homepage
        imgBtnBackToHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                sendToHomepage();
            }
        });

    }

    private void sendToHomepage() {
        startActivity(new Intent(ProductActivity.this, Homepagee.class));
    }
}