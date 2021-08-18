package my.edu.utar.assignmentnightmare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class orderlist extends AppCompatActivity {

    private RecyclerView rcvvoucher;
    private OrderListAdapter mItemAdapter;
    private FirebaseAuth mAuth;
    private String currentUserId, keydate, keytime;
    double price;
    double count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);

        rcvvoucher = findViewById(R.id.rvv);
        mItemAdapter = new OrderListAdapter(this);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getUid();


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        rcvvoucher.setLayoutManager(gridLayoutManager);

        mItemAdapter.setData(getListItem());
        rcvvoucher.setAdapter(mItemAdapter);

        ImageButton backorderlist = (ImageButton) findViewById(R.id.backorderlist);
        backorderlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public List<OrderListModel> getListItem(){
        List<OrderListModel> list = new ArrayList<>();
        DatabaseReference reff;

        reff = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("order");

        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for(DataSnapshot d : dataSnapshot.getChildren()) {
                         keydate = d.getKey().toString();
                         keytime = d.getKey().toString();
                        keydate = keydate.substring(0,14); //0-14 date 14-22 time
                        keytime = keytime.substring(14,22);


                        list.add(new OrderListModel(keydate, keytime));
                        mItemAdapter.notifyDataSetChanged();
                    }
                }
            }//onDataChange
            @Override
            public void onCancelled(DatabaseError error) {

            }//onCancelled
        });


        return list;
    }
}