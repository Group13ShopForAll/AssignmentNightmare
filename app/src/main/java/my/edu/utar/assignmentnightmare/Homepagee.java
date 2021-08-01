package my.edu.utar.assignmentnightmare;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Homepagee extends AppCompatActivity {

    ImageSlider imageSlider;

    private RecyclerView rcvItem;
    private ItemAdapter mItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_homepagee);

        rcvItem = findViewById(R.id.rvSoldItem);
        mItemAdapter = new ItemAdapter(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rcvItem.setLayoutManager(gridLayoutManager);

        mItemAdapter.setData(getListItem());
        rcvItem.setAdapter(mItemAdapter);


        imageSlider = findViewById(R.id.carousel);

        ArrayList<SlideModel> images = new ArrayList<>();
        images.add(new SlideModel(R.drawable.banner1,ScaleTypes.CENTER_CROP));
        images.add(new SlideModel(R.drawable.banner2,ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(images, ScaleTypes.CENTER_CROP);
        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {
                Toast.makeText(Homepagee.this,"Redirecting to ..." + i , Toast.LENGTH_SHORT).show();
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.botnav);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.home:
                    item.setChecked(true);
                    break;
                case R.id.category:
                    item.setChecked(true);
                    Intent intent1 = new Intent(Homepagee.this, Profile.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                case R.id.message:
                    item.setChecked(true);
                    Intent intent2 = new Intent(Homepagee.this, Profile.class);
                    startActivity(intent2);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                case R.id.cart:
                    item.setChecked(true);
                    Intent intent3 = new Intent(Homepagee.this, Profile.class);
                    startActivity(intent3);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                case R.id.profile:
                    item.setChecked(true);
                    Intent intent4 = new Intent(Homepagee.this, Profile.class);
                    startActivity(intent4);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    break;
            }
            return false;
        });

    }

    private List<Item> getListItem(){
        List<Item> list = new ArrayList<>();
        list.add(new Item(R.drawable.item1, "Iphone 12 mini", 3500, "2"));
        list.add(new Item(R.drawable.item2, "PowerBank",30, "32"));
        list.add(new Item(R.drawable.item3, "Ultra non-stick fry pannananananannanananananannaa",55, "4"));
        list.add(new Item(R.drawable.item4, "Printer",210, "12"));
        list.add(new Item(R.drawable.item5, "Red Dress",78.50, "52"));
        list.add(new Item(R.drawable.item6, "Spalding Basketball",120, "22"));
        list.add(new Item(R.drawable.item7, "Boltless Rack",150, "22"));
        list.add(new Item(R.drawable.item8, "Marker Pens",10.55, "22"));

        return list;
    }

}