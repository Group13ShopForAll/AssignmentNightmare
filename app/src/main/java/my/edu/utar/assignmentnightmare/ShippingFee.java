package my.edu.utar.assignmentnightmare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ShippingFee extends AppCompatActivity {

    ImageView ship1,ship2,ship3,ship4,ship5,ship6;
    Button backbutt;
    String shipname;
    Double shipprice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_fee);

        ship1 = (ImageView) findViewById(R.id.ship1);
        ship2 = (ImageView) findViewById(R.id.ship2);
        ship3 = (ImageView) findViewById(R.id.ship3);
        ship4 = (ImageView) findViewById(R.id.ship4);
        ship5 = (ImageView) findViewById(R.id.ship5);
        ship6 = (ImageView) findViewById(R.id.ship6);
        backbutt = (Button) findViewById(R.id.backbutt);

        Intent intent = new Intent(ShippingFee.this, ShoppingCart.class);

        ship1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shipname = "ABX EXPRESS";
                shipprice = 5.00;
                intent.putExtra("keyname", shipname);
                intent.putExtra("keyprice", shipprice);
                startActivity(intent);
                finish();
            }
        });
        ship2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shipname = "SKYNET";
                shipprice = 5.00;
                intent.putExtra("keyname", shipname);
                intent.putExtra("keyprice", shipprice);
                startActivity(intent);
                finish();
            }
        });
        ship3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shipname = "GDEX EXPRESS";
                shipprice = 4.00;
                intent.putExtra("keyname", shipname);
                intent.putExtra("keyprice", shipprice);
                startActivity(intent);
                finish();
            }
        });
        ship4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shipname = "CITY-LINK EXPRESS";
                shipprice = 4.00;
                intent.putExtra("keyname", shipname);
                intent.putExtra("keyprice", shipprice);
                startActivity(intent);
                finish();
            }
        });
        ship5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shipname = "POS-LAJU";
                shipprice = 3.00;
                intent.putExtra("keyname", shipname);
                intent.putExtra("keyprice", shipprice);
                startActivity(intent);
                finish();
            }
        });
        ship6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shipname = "J&T EXPRESS";
                shipprice = 3.00;
                intent.putExtra("keyname", shipname);
                intent.putExtra("keyprice", shipprice);
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



    }
}