package my.edu.utar.assignmentnightmare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
// Done by Felix
public class Voucher extends AppCompatActivity {

    private RecyclerView rcvvoucher, rcvvoucher2;
    private VoucherAdapter mItemAdapter, mItemAdapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);

        rcvvoucher = findViewById(R.id.itemsrcv);
        mItemAdapter = new VoucherAdapter(this);
        rcvvoucher2 = findViewById(R.id.itemsrcv2);
        mItemAdapter2 = new VoucherAdapter(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        rcvvoucher.setLayoutManager(gridLayoutManager);

        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 1);
        rcvvoucher2.setLayoutManager(gridLayoutManager2);

        mItemAdapter.setData(getListItem());
        rcvvoucher.setAdapter(mItemAdapter);
        mItemAdapter2.setData(getListItem2());
        rcvvoucher2.setAdapter(mItemAdapter2);

        ImageButton backvoucher = (ImageButton) findViewById(R.id.backvoucher);

        backvoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public List<Vou> getListItem(){
        List<Vou> list = new ArrayList<>();

        list.add(new Vou("❌ Coupon", "Clear Coupon Voucher Selection"));
        list.add(new Vou("RM 5 OFF Code SHOP5OFF", " Enjoy RM5 off Sitewide, Min spend RM 10"));
        list.add(new Vou("10 % OFF Sitewide", "Here you can get an extra 10% discount. Min spend RM 100"));
        mItemAdapter.notifyDataSetChanged();

        return list;
    }

    public List<Vou> getListItem2(){
        List<Vou> list = new ArrayList<>();

        list.add(new Vou("❌ Shipping", "Clear Shipping Voucher Selection"));
        list.add(new Vou("Free Shipping", "Enjoy Free Shipping, Min spend RM10"));
        mItemAdapter.notifyDataSetChanged();

        return list;
    }
}