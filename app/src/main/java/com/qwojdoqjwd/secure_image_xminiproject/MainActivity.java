package com.qwojdoqjwd.secure_image_xminiproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ImageView imgEncrypt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // Objects.requireNonNull(getSupportActionBar()).hide();
        imgEncrypt=findViewById(R.id.img_Encrypt);
        imgEncrypt.setOnClickListener(v -> {
            Intent m2=new Intent(MainActivity.this,PhotoCrypto.class);
            startActivity(m2);
        });

    }
}