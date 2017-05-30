package com.rohansarkar.helpex.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rohansarkar.helpex.R;

import java.util.Random;

public class WelcomeScreen extends Activity {

    private Handler delayHandler;
    private TextView welcomeMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        startAnimation();
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
        Random r = new Random();
        switch (r.nextInt(9)){
            case 0:
                YoYo.with(Techniques.Pulse).duration(1000).playOn(findViewById(R.id.tvWelcomeMessage));
                break;
            case 1:
                YoYo.with(Techniques.RubberBand).duration(1000).playOn(findViewById(R.id.tvWelcomeMessage));
                break;
            case 2:
                YoYo.with(Techniques.Shake).duration(1000).playOn(findViewById(R.id.tvWelcomeMessage));
                break;
            case 3:
                YoYo.with(Techniques.Swing).duration(1000).playOn(findViewById(R.id.tvWelcomeMessage));
                break;
            case 4:
                YoYo.with(Techniques.Wobble).duration(1000).playOn(findViewById(R.id.tvWelcomeMessage));
                break;
            case 5:
                YoYo.with(Techniques.Bounce).duration(1000).playOn(findViewById(R.id.tvWelcomeMessage));
                break;
            case 6:
                YoYo.with(Techniques.Tada).duration(1000).playOn(findViewById(R.id.tvWelcomeMessage));
                break;
            case 7:
                YoYo.with(Techniques.StandUp).duration(1000).playOn(findViewById(R.id.tvWelcomeMessage));
                break;
            case 8:
                YoYo.with(Techniques.Wave).duration(1000).playOn(findViewById(R.id.tvWelcomeMessage));
                break;
            case 9:
                YoYo.with(Techniques.Hinge).duration(1000).playOn(findViewById(R.id.tvWelcomeMessage));
                break;
        }
    }
}
