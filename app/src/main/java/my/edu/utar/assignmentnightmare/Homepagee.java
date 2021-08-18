package my.edu.utar.assignmentnightmare;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
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
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.banner.BannerView;
import com.huawei.hms.ads.identifier.AdvertisingIdClient;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.push.HmsMessageService;

import java.io.IOException;
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
        rvSoldProduct.setLayoutManager(new GridLayoutManager(this, 2));
        // set up recycler view options at home page for sold product
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("homepage").child("products"), Product.class).build();
        // apply the options at the product adapter and allocate the adapter to the recycler view at homepage
        productAdapter = new ProductAdapter(options);
        rvSoldProduct.setAdapter(productAdapter);

        // Image slider & Bottom Navigation(Felix)
        imageSlider = findViewById(R.id.carousel);

        ArrayList<SlideModel> images = new ArrayList<>();
        images.add(new SlideModel(R.drawable.banner1, ScaleTypes.CENTER_CROP));
        images.add(new SlideModel(R.drawable.banner2, ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(images, ScaleTypes.CENTER_CROP);
        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {
                Toast.makeText(Homepagee.this, "Redirecting to ..." + i, Toast.LENGTH_SHORT).show();
            }
        });

        NestedScrollView scrolls = (NestedScrollView) findViewById(R.id.scroll);
        BottomNavigationView bottomNavigationView = findViewById(R.id.botnav);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    item.setChecked(true);
                    scrolls.smoothScrollTo(0, 0);
                    break;
                case R.id.cart:
                    item.setChecked(true);
                    startActivity(new Intent(Homepagee.this, ShoppingCart.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    break;
                case R.id.profile:
                    item.setChecked(true);
                    startActivity(new Intent(Homepagee.this, Profile.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
                svSearchBar.onActionViewExpanded();
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
                                .startAt(str).endAt(str + "~"), Product.class)
                        .build();
        productAdapter = new ProductAdapter(options);
        productAdapter.startListening();
        rvSoldProduct.setAdapter(productAdapter);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
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