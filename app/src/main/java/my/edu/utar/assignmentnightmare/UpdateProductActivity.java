package my.edu.utar.assignmentnightmare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

// Done By Jiun Lin
public class UpdateProductActivity extends AppCompatActivity {

    private Intent intentFromMyProductAdapter;
    private String productKey, currentUserId;

    private DatabaseReference homepageProductRef, userProductRef;

    private ImageView ivUpdateProductImg;
    private TextView tvUpdateProductName;
    private EditText edtUpdateProductPrice, edtUpdateProductStock, edtUpdateProductDesc;
    private Button btnUpdateProductInfo;
    private ProgressBar pbUpdateProduct;

    private String productName, productDesc;
    private Double productPrice;
    private int productStock;
    private Uri productImgUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        intentFromMyProductAdapter = getIntent();
        productKey = intentFromMyProductAdapter.getStringExtra("productKey");
        currentUserId = intentFromMyProductAdapter.getStringExtra("currentUserId");

        homepageProductRef = FirebaseDatabase.getInstance().getReference().child("homepage").child("products");
        userProductRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("products");

        ivUpdateProductImg = (ImageView) findViewById(R.id.ivUpdateProductImg);
        tvUpdateProductName = (TextView) findViewById(R.id.tvUpdateProductName);
        edtUpdateProductPrice = (EditText) findViewById(R.id.edtUpdateProductPrice);
        edtUpdateProductStock = (EditText) findViewById(R.id.edtUpdateProductStock);
        edtUpdateProductDesc = (EditText) findViewById(R.id.edtUpdateProductDesc);
        pbUpdateProduct = (ProgressBar) findViewById(R.id.pbUpdateProduct);

        userProductRef.child(productKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // retrieve data and display on the respective field
                productImgUri = Uri.parse(snapshot.child("productImgUri").getValue().toString());
                Picasso.get().load(productImgUri).into(ivUpdateProductImg);
                productName = snapshot.child("productName").getValue().toString();
                tvUpdateProductName.setText(productName);
                String displayProductPrice = snapshot.child("productPrice").getValue().toString();
                edtUpdateProductPrice.setText(displayProductPrice);
                String displayRemainingStock = snapshot.child("productStock").getValue().toString();
                edtUpdateProductStock.setText(displayRemainingStock);
                productDesc = snapshot.child("productDesc").getValue().toString();
                edtUpdateProductDesc.setText(productDesc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateProductActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnUpdateProductInfo = (Button) findViewById(R.id.btnUpdateProductInfo);
        // while click on update button perform series of data validation and updating action
        btnUpdateProductInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbUpdateProduct.setVisibility(View.VISIBLE);
                validateProductUpdateInfo();
            }
        });

    }

    private void validateProductUpdateInfo() {
        productPrice = Double.parseDouble(edtUpdateProductPrice.getText().toString());
        productStock = Integer.parseInt(edtUpdateProductStock.getText().toString());
        productDesc = edtUpdateProductDesc.getText().toString();

        if (productPrice == 0){
            edtUpdateProductPrice.setError("Please type in latest product price");
            return;
        }
        if (productStock == 0){
            edtUpdateProductStock.setError("Please type in latest product stock (>0)");
            return;
        }
        if (TextUtils.isEmpty(productDesc)){
            edtUpdateProductDesc.setError("Please type in product description");
            return;
        }
        updateProductIntoFirebase();
    }

    private void updateProductIntoFirebase() {
        HashMap updateProductMap = new HashMap();
        updateProductMap.put("productName", productName);
        updateProductMap.put("productPrice",productPrice);
        updateProductMap.put("productStock",productStock);
        updateProductMap.put("productDesc",productDesc);
        updateProductMap.put("sellerId",currentUserId);
        updateProductMap.put("productImgUri",productImgUri.toString());

        // update info at homepage
        homepageProductRef.child(productKey).updateChildren(updateProductMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(UpdateProductActivity.this, "Successfully updated product at homepage", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pbUpdateProduct.setVisibility(View.INVISIBLE);
                Toast.makeText(UpdateProductActivity.this, "Error occurred: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //update info at my product page
        userProductRef.child(productKey).updateChildren(updateProductMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                pbUpdateProduct.setVisibility(View.INVISIBLE);
                Toast.makeText(UpdateProductActivity.this, "Successfully updated product at my product page", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pbUpdateProduct.setVisibility(View.INVISIBLE);
                Toast.makeText(UpdateProductActivity.this, "Error occurred: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}