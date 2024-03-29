package my.edu.utar.assignmentnightmare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

// Done by Felix
public class Checkout extends AppCompatActivity {

    TextView adress;
    private DatabaseReference userProfileRef;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private String productKey, currentDatee, currentTimee, sub, substring;
    private Intent intentFromProductAdapter;
    int i = 0, j = 0;
    private Double coupondiscount = 0.00;
    private Double shipdiscount = 0.00;
    private Double amountTotal = 0.00;


    TextView directvoucher;
    TextView tvmerchantprice, tvshippingprice, tvshippingdiscount, tvdiscount, tvfinaltotal, tvshippingvoucherused, tvcouponvoucherused;
    Button paybutt;
    ImageButton backbutt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_checkout);


        backbutt = (ImageButton) findViewById(R.id.backprofile);
        paybutt = (Button) findViewById(R.id.pay);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getUid();
        adress = (TextView) findViewById(R.id.adress);
        userProfileRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("user profile");
        intentFromProductAdapter = getIntent();
        productKey = intentFromProductAdapter.getStringExtra("productKey");

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-YYYY");
        currentDatee = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        currentTimee = currentTime.format(calForTime.getTime());

        //get value from SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String voucherused = prefs.getString("VoucherUsed", "no value");
        String shippingvoucherused = prefs.getString("ShippingVoucherUsed", "no value");
        String merchantprice = prefs.getString("merchanttotal", "no value");
        String shipname = prefs.getString("shipname", "no value");
        String shipprice = prefs.getString("shipprice", "0.00");

        tvmerchantprice = (TextView) findViewById(R.id.merchantprice);
        tvshippingprice = (TextView) findViewById(R.id.shippingprice);
        tvshippingdiscount = (TextView) findViewById(R.id.shippingdiscount);
        tvdiscount = (TextView) findViewById(R.id.discount);
        tvfinaltotal = (TextView) findViewById(R.id.finaltotal);
        tvshippingvoucherused = (TextView) findViewById(R.id.ShippingVoucherUsed);
        tvcouponvoucherused = (TextView) findViewById(R.id.CouponVoucherUsed);
        TextView displayshipprice = (TextView) findViewById(R.id.displayshipprice);
        TextView displayshipname = (TextView) findViewById(R.id.displayshipname);
        displayshipprice.setText(shipprice);
        displayshipname.setText(shipname);

        tvmerchantprice.setText(merchantprice.toString());
        tvshippingprice.setText(shipprice.toString());
        amountTotal = Double.valueOf(merchantprice) + Double.valueOf(shipprice);


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
                Intent intent = new Intent(Checkout.this, Voucher.class);
                startActivity(intent);
                finish();
            }
        });

        backbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        paybutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Checkout.this, RazorpayPayment.class);
                String sAmount = amountTotal.toString();
                intent.putExtra("sAmount", sAmount);
                startActivity(intent);
            }
        });

        switch (voucherused){
            case "❌ Coupon":
                    coupondiscount = 0.00;
                    tvcouponvoucherused.setText("No coupon voucher used");
                break;
            case "RM 5 OFF Code SHOP5OFF":
                if(amountTotal >= 10.00){
                    coupondiscount = 5.00;
                    tvcouponvoucherused.setText("RM 5 OFF Code SHOP5OFF");
                }
                break;
            case "10 % OFF Sitewide" :
                if(amountTotal >= 100.00){
                    coupondiscount = Double.valueOf(merchantprice) * 10 / 100;
                    tvcouponvoucherused.setText("10 % OFF Sitewide");
                }
                break;
            default:
                break;
        }

        switch (shippingvoucherused){
            case "❌ Shipping":
                shipdiscount = 0.00;
                tvshippingvoucherused.setText("No shipping voucher used");
                break;
            case "Free Shipping":
                if(amountTotal >= 15.00){
                    shipdiscount = Double.valueOf(shipprice);
                    tvshippingvoucherused.setText("Free Shipping");
                }
                else{
                    Toast.makeText(Checkout.this, "min spend RM15", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

        tvdiscount.setText(coupondiscount.toString());
        tvshippingdiscount.setText(shipdiscount.toString());

        amountTotal = amountTotal - coupondiscount - shipdiscount;
        tvfinaltotal.setText(amountTotal.toString());


    }

}