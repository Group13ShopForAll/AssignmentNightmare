package my.edu.utar.assignmentnightmare;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Homepagee extends AppCompatActivity {

    ImageSlider imageSlider;

    //private RecyclerView rcvItem;
    //private ItemAdapter mItemAdapter;

    private RecyclerView rvSoldProduct;
    private ProductAdapter productAdapter;

    private SearchView svSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_homepagee);

        // Done by Jiun Lin
        // get recycler view at home page
        rvSoldProduct = (RecyclerView) findViewById(R.id.rvSoldProduct);
        rvSoldProduct.setLayoutManager(new LinearLayoutManager(this));
        // set up recycler view options at home page for sold product
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("homepage").child("products"),Product.class).build();
        // apply the options at the product adapter and allocate the adapter to the recycler view at homepage
        productAdapter = new ProductAdapter(options);
        rvSoldProduct.setAdapter(productAdapter);

        // Done by Felix
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
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    break;
                case R.id.message:
                    item.setChecked(true);
                    //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    //finish();
                    break;
                case R.id.cart:
                    item.setChecked(true);
                    startActivity(new Intent(Homepagee.this, ShoppingCart.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    break;
                case R.id.profile:
                    item.setChecked(true);
                    startActivity(new Intent(Homepagee.this, Profile.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    break;
            }
            return false;
        });

        // search view action
        svSearchBar = (SearchView) findViewById(R.id.svSeachBar);
        svSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                txtSearch(newText);
                return false;
            }
        });

    }

    private void txtSearch(String str) {
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(FirebaseDatabase.getInstance().getReference()
                        .child("homepage").child("products").orderByChild("productName")
                        .startAt(str).endAt(str+"~"),Product.class )
                        .build();
        productAdapter = new ProductAdapter(options);
        productAdapter.startListening();
        rvSoldProduct.setAdapter(productAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        productAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        productAdapter.stopListening();
    }
}