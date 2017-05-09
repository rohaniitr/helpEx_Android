package com.rohansarkar.helpex.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.rohansarkar.helpex.R;

public class WelcomeScreen extends Activity {

    private Handler delayHandler;
    private TextView welcomeMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

//        startAnimation();
        startTimer();
    }

    private void startTimer(){
        delayHandler = new Handler();

        Runnable startActivity = new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(WelcomeScreen.this, Home.class);
                startActivity(i);
                WelcomeScreen.this.finish();
            }
        };

        delayHandler.postDelayed(startActivity, 1500);
    }

    private void startAnimation(){
        welcomeMessage = (TextView) findViewById(R.id.tvWelcomeMessage);

        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        welcomeMessage.setAnimation(zoomIn);
    }
}
