package com.example.sara.mygym.Modules.Welcome;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.sara.mygym.R;


public class StartSplashScreen extends Activity {
    public void onAttachedToWindow(){
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    Thread splashThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_splash_screen);
        startAnimations();
    }

    private void startAnimations() {
       /* Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        /*ConstraintLayout l = (ConstraintLayout) findViewById(R.id.splash_layout);
        l.clearAnimation();
        l.startAnimation(anim);*/

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView imageview = (ImageView) findViewById(R.id.imageViewSplash);
        imageview.clearAnimation();
        imageview.startAnimation(anim);

        splashThread = new Thread(){
            @Override
            public void run(){
                try {
                    int waited = 0;
                    while(waited <3500){
                        sleep(100);
                        waited +=100;
                    }
                    Intent intent = new Intent(StartSplashScreen.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    StartSplashScreen.this.finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        splashThread.start();
    }
}
