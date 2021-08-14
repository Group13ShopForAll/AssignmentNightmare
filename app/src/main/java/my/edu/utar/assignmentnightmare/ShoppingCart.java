package my.edu.utar.assignmentnightmare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShoppingCart extends AppCompatActivity {

    private RecyclerView rcvShopCart;
    private CartAdapter cartAdapter;

    private FirebaseAuth mAuth;
    private DatabaseReference SCReference;
    private String currentUserId;
    TextView arrow;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_shopping_cart);

        rcvShopCart = (RecyclerView) findViewById(R.id.rcvShopCart);
        rcvShopCart.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getUid();

        SCReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("cart");

        FirebaseRecyclerOptions<CartProduct> options =
                new FirebaseRecyclerOptions.Builder<CartProduct>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("cart"),
                                CartProduct.class).build();

        DatabaseReference reff;
        String productKey;
        Intent intentFromProductAdapter = getIntent();
        productKey = intentFromProductAdapter.getStringExtra("productKey");
        TextView totalAmount = (TextView) findViewById(R.id.amount);
        int totalquan;
        arrow = (TextView) findViewById(R.id.arrow);
        TextView fee = (TextView) findViewById(R.id.fee);
        String shipname = getIntent().getStringExtra("keyname");
        Double shipprice = getIntent().getDoubleExtra("keyprice",0.00);
        //Calculate subtotal
        reff = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("cart");
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double count = 0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        double price = Double.valueOf(snapshot.child("productPrice").getValue(Long.class));
                        count = count + price;

                        arrow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ShoppingCart.this, ShippingFee.class);
                                startActivity(intent);
                            }
                        });
                        fee.setText(shipprice.toString());
                        Double total = count + shipprice;
                        totalAmount.setText(String.format("%.2f", total));

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

        cartAdapter = new CartAdapter(options);
        rcvShopCart.setAdapter(cartAdapter);

        TextView btnCheckout = (TextView) findViewById(R.id.btnCheckout);
        Intent intent = new Intent(ShoppingCart.this, Checkout.class);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shipprice == 0.0){
                    Toast.makeText(ShoppingCart.this, "Please select shipping method", Toast.LENGTH_SHORT).show();
                }
                else{
                    intent.putExtra("total", Double.valueOf(totalAmount.getText().toString()));
                    intent.putExtra("shipname", shipname);
                    intent.putExtra("shipprice", shipprice);
                    startActivity(intent);
                }
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.botnav);
        bottomNavigationView.setSelectedItemId(R.id.cart);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.home:
                    item.setChecked(true);
                    startActivity(new Intent(ShoppingCart.this, Homepagee.class));
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    finish();
                    break;
                case R.id.category:
                    item.setChecked(true);
                    break;
                case R.id.message:
                    item.setChecked(true);
                    break;
                case R.id.cart:
                    item.setChecked(true);
                    break;
                case R.id.profile:
                    item.setChecked(true);
                    startActivity(new Intent(ShoppingCart.this, Profile.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    break;
            }
            return false;
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        cartAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cartAdapter.stopListening();
    }

}