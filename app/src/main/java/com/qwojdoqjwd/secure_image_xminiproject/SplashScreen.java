package com.qwojdoqjwd.secure_image_xminiproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreen.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        },5000);
    }
}

/*


protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        // Using Handler to delay the intent for the next activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an Intent that will start the next activity
                Intent mainIntent = new Intent(splash_screen.this, MainActivity.class);
                startActivity(mainIntent);
                finish(); // Close the splash activity so it's not available on back press
            }
        }, SPLASH_DISPLAY_LENGTH);
    }



 */