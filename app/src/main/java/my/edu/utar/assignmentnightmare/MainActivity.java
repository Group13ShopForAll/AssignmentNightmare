package my.edu.utar.assignmentnightmare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;

    //variables
    Animation topAnimation, bottomAnimation;
    ImageView img;
    TextView desc1, desc2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Animations
        topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        img = findViewById(R.id.biglogo);
        desc1 = findViewById(R.id.desc);
        desc2 = findViewById(R.id.desc2);

        img.setAnimation(topAnimation);
        desc1.setAnimation(bottomAnimation);
        desc2.setAnimation(bottomAnimation);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent intent = new Intent( MainActivity.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);

    }
}