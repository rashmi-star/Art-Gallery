package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splash_activity extends AppCompatActivity {


    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser user=mAuth.getCurrentUser();

    private int sleep=3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_activity);
        getSupportActionBar().hide();
        logolauncher logo =new logolauncher();
        logo.start();
    }

    private class logolauncher extends Thread {
        public void run() {
            try {
                sleep(1000*sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent intent;
            if(user!=null) {
                intent = new Intent(splash_activity.this, SecondActivity.class);
            }
            else
            {
                intent = new Intent(splash_activity.this, MainActivity.class);
            }
            startActivity(intent);
            splash_activity.this.finish();
        }
    }

}