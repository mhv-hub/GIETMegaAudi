package com.mno.gietmegaaudi;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        final CountDownTimer mTimer = new CountDownTimer(2000, 1000) {

            @Override
            public void onTick(final long millisUntilFinished){

            }

            @Override
            public void onFinish() {
                Intent i = new Intent(SplashActivity.this,MovieDetailsActivity.class);
                startActivity(i);
                finish();
            }
        };
        mTimer.start();
    }
}
