package com.boss.routedirectionsapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent MainIntent = new Intent(getApplicationContext(),PinActivity.class);
                startActivity(MainIntent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
