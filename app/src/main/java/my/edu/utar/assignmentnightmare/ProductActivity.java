package my.edu.utar.assignmentnightmare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductActivity extends AppCompatActivity {

    private DatabaseReference productRef, sellerRef, reff;
    private ImageView ivItemSoldImage;
    private TextView tvItemSoldName, tvItemSoldPrice, tvItemSoldSeller, tvItemSoldDesc, tvItemSoldQuantity, btnBuyNow;
    private ImageButton imgBtnBackToHomepage;
    private CircleImageView civProfileImg;
    private Intent intentFromProductAdapter;
    private TextView inc, dec, tv;
    private Integer count;
    private String productKey;
    private String productName, productPrice, productStock, productDesc, sellerId, sellerUsername;
    private Uri productImgUri;
    private TextView edtquan;
    private AlertDialog alertDialog;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product);

        intentFromProductAdapter = getIntent();
        productKey = intentFromProductAdapter.getStringExtra("productKey");

        productRef = FirebaseDatabase.getInstance().getReference().child("homepage").child("products").child(productKey);

        imgBtnBackToHomepage = (ImageButton) findViewById(R.id.imgBtnBackToHomepage);
        btnBuyNow = (TextView) findViewById(R.id.btnAddToCart);

        ivItemSoldImage = (ImageView) findViewById(R.id.ivItemSoldImage);
        tvItemSoldName = (TextView) findViewById(R.id.tvItemSoldName);
        tvItemSoldPrice = (TextView) findViewById(R.id.tvItemSoldPrice);
        tvItemSoldSeller = (TextView) findViewById(R.id.tvItemSoldSeller);
        tvItemSoldDesc = (TextView) findViewById(R.id.tvItemSoldDesc);
        tvItemSoldQuantity = (TextView) findViewById(R.id.tvItemSoldQuantity);
        civProfileImg = (CircleImageView) findViewById(R.id.profilepicpro);

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

                        Uri profileImg = Uri.parse(snapshot.child("user profile").child("profile image").getValue().toString());
                        //display profile image and username on respective field
                        Picasso.get().load(profileImg).into(civProfileImg);
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
                tvItemSoldDesc.setMovementMethod(new ScrollingMovementMethod());

                inc = findViewById(R.id.inc);
                dec = findViewById(R.id.dec);
                tv = findViewById(R.id.edtquan);

                count = 1;
                inc.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        count++;
                        tv.setText(count+"");
                    }
                });
                dec.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        if(count == 1){
                            tv.setText(count+"");
                        }
                        else{
                            count--;
                            tv.setText(count+"");
                        }
                    }
                });

                edtquan = (TextView) findViewById(R.id.edtquan);
                alertDialog = new AlertDialog.Builder(ProductActivity.this).create();
                editText = new EditText(ProductActivity.this);

                alertDialog.setTitle("Edit Quantity");
                alertDialog.setView(editText);

                edtquan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editText.setText(edtquan.getText().toString());


                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                edtquan.setText(editText.getText().toString());
                                count = Integer.parseInt(editText.getText().toString());
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();

                    }
                });


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
            }
        });


            btnBuyNow.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(count > Integer.parseInt(productStock)){
                        Toast.makeText(ProductActivity.this, "Stock Insufficient", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        HashMap productMap = new HashMap();
                        productMap.put("productName", productName);
                        productMap.put("productDesc", productDesc);
                        productMap.put("productStock", Integer.parseInt(productStock));
                        productMap.put("productImgUri", productImgUri.toString());
                        productMap.put("productQuantity", count);
                        Double totalsingle = Double.valueOf(count) * Double.valueOf(productPrice);
                        productMap.put("productPrice", Double.valueOf(totalsingle));

                        reff = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("cart");
                        reff.child(productKey).updateChildren(productMap);

                        finish();
                    }
                }
            });

    }
}