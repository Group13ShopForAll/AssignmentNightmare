/*
 *  Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *  This subclass is created by HMS Core Toolkit
 *  and used to receive token information or messages returned by HMS server
 *
 */
package my.edu.utar.assignmentnightmare;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.banner.BannerView;

public class BannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);


        /**
         * The Id number here is constructed in step 1. It is recommended to complete the step 1 before using the sample code snippet.
         */
        /*BannerView bannerView = findViewById(R.id.hw_banner_view);
        //BannerView bannerView = new BannerView(this);
        // "testw6vs28auh3" is a dedicated test ad slot ID.
        bannerView.setAdId("testw6vs28auh3");
        bannerView.setBannerAdSize(BannerAdSize.BANNER_SIZE_360_57);
        FrameLayout adFrameLayout = findViewById(R.id.ad_frame);
        adFrameLayout.addView(bannerView);*/
        /**
         * The Id number here is constructed in step 1. It is recommended to complete the step 1 before using the sample code snippet.
         */
        BannerView bannerView = findViewById(R.id.hw_banner_view);
        // Set the ad slot ID and ad size,"testw6vs28auh3" is a dedicated test ad slot ID.
        bannerView.setAdId("testw6vs28auh3");
        bannerView.setBannerAdSize(BannerAdSize.BANNER_SIZE_360_57);
        // Create an ad request to load an ad.
        AdParam adParam = new AdParam.Builder().build();
        bannerView.loadAd(adParam);

    }
}
