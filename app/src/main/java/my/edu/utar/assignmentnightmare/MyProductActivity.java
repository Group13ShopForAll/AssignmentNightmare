package my.edu.utar.assignmentnightmare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyProductActivity extends AppCompatActivity {

    // Done by Jiun Lin
    private RecyclerView rvMyProduct;
    private MyProductAdapter myProductAdapter;

    private FirebaseAuth mAuth;
    private DatabaseReference myProductRef;

    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product);

        // find the recycler and set up the layout manager
        rvMyProduct = (RecyclerView) findViewById(R.id.rvMyProduct);
        rvMyProduct.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getUid();

        myProductRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("products");
        // set up recycler view options at my product page for current sold product
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("products"),
                                Product.class).build();
        // apply the options at the product adapter and allocate the adapter to the recycler view at my product page
        myProductAdapter = new MyProductAdapter(currentUserId,options);
        rvMyProduct.setAdapter(myProductAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        myProductAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myProductAdapter.stopListening();
    }
}