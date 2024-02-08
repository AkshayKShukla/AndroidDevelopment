package com.qwojdoqjwd.secure_image_xminiproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;



public class PhotoCrypto extends AppCompatActivity
{
    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 100;
    private static final int REQUEST_SELECT_PHOTO = 200;

    private EditText encryptedImageText;
    private ImageView imageView;
    private SecretKey AESkey=null;
    private ClipboardManager clipboardManager;

    //key GENeRATION



        // Generate a random key

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_crypto);

        Button encryptButton = findViewById(R.id.enc_btn);
        Button decryptButton = findViewById(R.id.dec_btn);
        encryptedImageText = findViewById(R.id.enc_text);
        imageView = findViewById(R.id.imgView);
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        encryptedImageText.setEnabled(false);

        encryptButton.setOnClickListener(v -> requestReadExternalStoragePermission());

        decryptButton.setOnClickListener(v -> {
            decryptImage();
        });
    }

    private void requestReadExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(PhotoCrypto.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PhotoCrypto.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
        } else {
            selectPhoto();
        }
    }

    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "CHOOSE IMAGE"), REQUEST_SELECT_PHOTO);
    }


    //public static String AESdecrypt(String encryptedText, String key) {
        /*try {
            byte[] encryptedBytes = Base64.decode(encryptedText, Base64.DEFAULT);

            SecretKey secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle the exception appropriately in your application
        }*/
        private static String AESdecrypt(String encryptedText, SecretKey secretKey) throws Exception {
            //byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);

            byte[] encryptedBytes = Base64.decode(encryptedText, Base64.DEFAULT);

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        }

    private void decryptImage() {
        String encryptedImageString = encryptedImageText.getText().toString().trim();
        if (!encryptedImageString.isEmpty()) {
            try {
                encryptedImageString=AESdecrypt(encryptedImageString,AESkey);
            } catch (Exception e) {
                throw new RuntimeException(e);

            }
            byte[] decodedBytes = Base64.decode(encryptedImageString, Base64.DEFAULT);
            Bitmap decryptedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

            imageView.setImageBitmap(decryptedBitmap);
        }
    }

    public void copyCode(View view) {
        String code = encryptedImageText.getText().toString().trim();
        if (!code.isEmpty()) {
            ClipData clipData = ClipData.newPlainText("text", code);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(this, "Copied to clipboard!!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectPhoto();
        } else {
            Toast.makeText(this, "Permission Denied!!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_PHOTO && resultCode == RESULT_OK && data != null) {
            handleSelectedPhoto(data.getData());
        }
    }

    /*public static String encrypt(String plainText) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128); // You can use 128, 192, or 256
            SecretKey secretKey = keyGenerator.generateKey();

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle the exception appropriately in your application
        }
    }*/

    public String AESencrypt(String plainText) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128); // You can use 128, 192, or 256
            SecretKey secretKey = keyGenerator.generateKey();
            AESkey=secretKey;
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle the exception appropriately in your application
        }
    }


    public void handleSelectedPhoto(Uri selectedPhotoUri) {
        if (selectedPhotoUri != null) {
            try {
                Bitmap bitmap;
                ImageDecoder.Source source;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    source = ImageDecoder.createSource(this.getContentResolver(), selectedPhotoUri);
                    bitmap = ImageDecoder.decodeBitmap(source);
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedPhotoUri);
                }

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                byte[] imageBytes = outputStream.toByteArray();
                String encodedImageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                encodedImageString=AESencrypt(encodedImageString);
                encryptedImageText.setText(encodedImageString);
                Toast.makeText(this, "Image encrypted successfully!!!", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error occurred while encrypting image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
