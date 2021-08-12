package my.edu.utar.assignmentnightmare;
//Done by Low Wei Han
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class DisplayOrder extends AppCompatActivity {

    RecyclerView recyclerView;
    DisplayOrderAdapter displayOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_order);

        recyclerView = (RecyclerView)findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<OrderModel> options =
                new FirebaseRecyclerOptions.Builder<OrderModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child(""), OrderModel.class) //child waiting video 28:05
                        .build();

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