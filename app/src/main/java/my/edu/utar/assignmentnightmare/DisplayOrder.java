package my.edu.utar.assignmentnightmare;
//Done by Low Wei Han
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class DisplayOrder extends AppCompatActivity {

    RecyclerView recyclerView;
    DisplayOrderAdapter displayOrderAdapter;
    private FirebaseAuth mAuth;
    private String currentUserId;
    String orderdate, ordertime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_order);

        recyclerView = (RecyclerView)findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getUid();

        orderdate = getIntent().getStringExtra("orderdate");
        ordertime = getIntent().getStringExtra("ordertime");

        FirebaseRecyclerOptions<OrderModel> options =
                new FirebaseRecyclerOptions.Builder<OrderModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("order").child(orderdate + ordertime).orderByChild("sellername"), OrderModel.class)
                        .build();

        DatabaseReference reff;
        TextView feeorder;
        feeorder = (TextView) findViewById(R.id.orderfee);
        reff = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("order").child(orderdate + ordertime);
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double count = 0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    double price = Double.valueOf(snapshot.child("productPrice").getValue(Long.class));
                    count = count + price;
                    feeorder.setText(String.format("%.2f", count));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });


        TextView ordermodule = (TextView) findViewById(R.id.ordermodule);
        TextView ordertimemodule = (TextView) findViewById(R.id.ordertimemodule);
        ImageButton backorder = (ImageButton) findViewById(R.id.backorder);
        ordermodule.setText(orderdate);
        ordertimemodule.setText(ordertime);

        backorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        displayOrderAdapter = new DisplayOrderAdapter(options);
        recyclerView.setAdapter(displayOrderAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayOrderAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        displayOrderAdapter.stopListening();
    }
}