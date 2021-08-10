package my.edu.utar.assignmentnightmare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class UploadActivity extends AppCompatActivity {

    //Done by Jiun Lin
    private ImageView ivAddProductImage;
    private EditText edtProductName, edtProductDesc, edtProductPrice, edtProductStock;
    private FloatingActionButton btnUploadCancel, btnUploadConfirm, btnBackToProfile;
    private ProgressBar pbUploadProduct;
    private Uri productImgUri;

    private static int Gallery_Pick = 1;

    private FirebaseAuth mAuth;
    private DatabaseReference homepageProductRef, userProductRef;
    private StorageReference productImgRef;
    private String currentUserId, saveCurrentDate, saveCurrentTime, productImgName, downloadUri;

    private String productName, productDesc;
    private double productPrice;
    private int productStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        ivAddProductImage = (ImageView) findViewById(R.id.ivAddImage);
        edtProductName = (EditText) findViewById(R.id.edtProductName);
        edtProductDesc = (EditText) findViewById(R.id.edtProductDesc);
        edtProductPrice = (EditText) findViewById(R.id.edtProductPrice);
        edtProductStock = (EditText) findViewById(R.id.edtProductStock);

        btnUploadCancel = (FloatingActionButton) findViewById(R.id.btnUploadCancel);
        btnUploadConfirm = (FloatingActionButton) findViewById(R.id.btnUploadConfirm);

        pbUploadProduct = (ProgressBar) findViewById(R.id.pbUploadProduct);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getUid();

        productImgRef = FirebaseStorage.getInstance().getReference().child(currentUserId).child("product images");
        userProductRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("products");
        homepageProductRef = FirebaseDatabase.getInstance().getReference().child("homepage").child("products");

        // while clicking on the add product image icon, trigger function to choose image from device storage
        ivAddProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        // while user type in all the required information perform product info validation function
        btnUploadConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductInfo();
            }
        });

        // while user click on cross button clear all input data and return back to profile page
        btnUploadCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                sendToProfileActivity();
            }
        });

    }

    private void sendToProfileActivity() {
        startActivity(new Intent(UploadActivity.this, Profile.class));
    }

    private void validateProductInfo() {
        productName = edtProductName.getText().toString();
        productDesc = edtProductDesc.getText().toString();
        productPrice = Double.parseDouble(edtProductPrice.getText().toString());
        productStock = Integer.parseInt(edtProductStock.getText().toString());

        if (TextUtils.isEmpty(productName)){
            edtProductName.setError("Please type in your product name");
            return;
        }
        if (TextUtils.isEmpty(productDesc)){
            edtProductDesc.setError("Please fill in product description");
            return;
        }
        if (productPrice==0){
            edtProductPrice.setError("Please type in product selling price");
            return;
        }
        if (productStock==0){
            edtProductStock.setError("Product stock cannot be 0");
            return;
        }
        // if all the fields had been filled up properly, then insert the product image into the firebase storage;
        insertProductImgToFirebaseStorage();
    }

    private void insertProductImgToFirebaseStorage() {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-YYYY");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForTime.getTime());

        // generate random img file name using current date and time
        productImgName = saveCurrentDate + " " + saveCurrentTime;
        StorageReference imgPath = productImgRef.child(productImgUri.getLastPathSegment() + productImgName);

        // start the progress bar which indicates starting of storing process
        pbUploadProduct.setVisibility(View.VISIBLE);
        imgPath.putFile(productImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        downloadUri = uri.toString();
                        Toast.makeText(UploadActivity.this, "Image uplaoded to firebase storage successfully", Toast.LENGTH_SHORT).show();
                        //once img had been saved to firebase storage, next store the product info such as name, desc, price and stock into firebase
                        saveProductToFirebase();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadActivity.this, "Error occurred "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadActivity.this, "Error occurred "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveProductToFirebase() {
        //create hashmap to store the product info
        HashMap productMap = new HashMap();
        productMap.put("productName",productName);
        productMap.put("productDesc",productDesc);
        productMap.put("productPrice",productPrice);
        productMap.put("productStock",productStock);
        productMap.put("productImgUri",downloadUri);
        productMap.put("sellerId",currentUserId);

        //store product info in particular user reference
        userProductRef.child(productName+currentUserId+saveCurrentTime).updateChildren(productMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(UploadActivity.this, "New Product has been uploaded", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadActivity.this, "Error occurred "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // store product info in homepage ref for showing purpose
        homepageProductRef.child(productName+currentUserId+saveCurrentTime).updateChildren(productMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(UploadActivity.this, "New Product has been uploaded to homepage", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadActivity.this, "Error occurred "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
            productImgUri = data.getData();
            ivAddProductImage.setImageURI(productImgUri);
        }else{
            Toast.makeText(UploadActivity.this, "Error occurred while picking image from local storage.", Toast.LENGTH_SHORT).show();
        }
    }
}