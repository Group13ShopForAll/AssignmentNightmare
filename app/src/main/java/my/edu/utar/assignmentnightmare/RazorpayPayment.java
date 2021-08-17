package my.edu.utar.assignmentnightmare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class RazorpayPayment extends AppCompatActivity implements PaymentResultListener {
    //Initialize variable
    Button btnPay;
    Intent intentFromCheckOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razorpay_payment);

        intentFromCheckOut = getIntent();
        String sAmount = intentFromCheckOut.getStringExtra("sAmount");

        //Assign variable
        btnPay = (Button) findViewById(R.id.btn_pay);

        //Convert
        double amount = (Double.parseDouble(sAmount)*100);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize razorpay checkout
                Checkout checkout = new Checkout();
                //Set key id
                checkout.setKeyID("rzp_test_PwZZPZy9gc3L0k");
                //Set image
                checkout.setImage(R.drawable.razorpay);
                //Initialize JSON object
                JSONObject object = new JSONObject();
                try {
                    //Put name
                    object.put("name", "Shop4All");
                    //Put description
                    object.put("description", "Test Payment");
                    //Put theme color
                    object.put("theme.color","#0093DD");
                    //Put currency unit
                    object.put("currency", "MYR");
                    //Put amount
                    object.put("amount", amount);
                    //Put mobile number
                    //object.put("prefill.contact", "60102279589");
                    //Put email
                    //object.put("prefill.email", "group13shop4all@gmail.com");
                    //Open Razorpay checkout activity
                    checkout.open(RazorpayPayment.this, object);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onPaymentSuccess(String s) {
        //Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Set title
        builder.setTitle("Payment ID");
        //Set message
        builder.setMessage(s);
        //Show alert dialog
        builder.show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        //Display toast
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}