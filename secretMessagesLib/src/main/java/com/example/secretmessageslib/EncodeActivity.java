package com.example.secretmessageslib;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

public class EncodeActivity extends AppCompatActivity {

    private AppCompatImageView encode_IMG_loadimg;
    private TextInputEditText encode_TXT_secretmessage;
    private TextInputEditText encode_TXT_secretkey;
    private MaterialButton encode_BTN_send;
    private static CallBack_encodedMessageReadyProtocol callBack_encodedMessageReadyProtocol;
    EncodeManager encodeManager;
    private int requestExternalStorage = 1;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    CallBack_encodedFinishedProtocol callBack_encodedFinishedProtocol = new CallBack_encodedFinishedProtocol() {
        @Override
        public void handleEncryptedData() {
            openChat();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode);

        findViews();
        initViews();

        encodeManager = new EncodeManager();
        encodeManager.setCallBack_encodedFinishedProtocol(callBack_encodedFinishedProtocol);
        pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        encodeManager.setImageReady(true);
                        encodeManager.setImagePath(uri);
                        try {
                            encodeManager.getMessageToEncode().setBasic_image( MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        loadImage(encodeManager.getMessageToEncode().getImagePath());
                        handleButton();
                    } else { // gallery was closed without selecting any image

                    }
                });
    }

    public static void setCallBack_encodedMessageReadyProtocol(CallBack_encodedMessageReadyProtocol callback){
        callBack_encodedMessageReadyProtocol = callback;
    }

    private void initViews() {
        encode_IMG_loadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleGalleryUsage();
            }
        });

        encode_BTN_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                encodeManager
                        .getMessageToEncode()
                                .setSecretMessage(encode_TXT_secretmessage.getText().toString())
                                        .setSecretKey(encode_TXT_secretkey.getText().toString());

                try {
                    encodeManager.encryptData();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        encode_TXT_secretmessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                encodeManager.setMessageReady(true);
                handleButton();
            }
        });

        encode_TXT_secretkey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                encodeManager.setSecretKeyReady(true);
                handleButton();
            }
        });
    }

    private void openChat() {
        callBack_encodedMessageReadyProtocol.handleMessage(encodeManager.getMessageToEncode());
        finish();
    }

    private void handleGalleryUsage() {
        String[] permissionsStorage = {Manifest.permission.READ_EXTERNAL_STORAGE};

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, permissionsStorage, requestExternalStorage);

        else
            pickImageFromGallery();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == requestExternalStorage) { // Request code should match the one used in requestPermissions()
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                pickImageFromGallery();
            } else {
                // Permission denied
            }
        }
    }

    private void pickImageFromGallery() {
        // Registers a photo picker activity launcher in single-select mode.


        // Launch the photo picker and let the user choose only images.
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()); // Note: this is not an error. This is a bug of android studio
    }

    private void handleButton() {
        if(encodeManager.ready())
            encode_BTN_send.setEnabled(true);
    }

    private void loadImage(Uri uri) {
        Glide
                .with(this)
                .load(uri)
                .into(encode_IMG_loadimg);
    }

    private void findViews() {

        encode_IMG_loadimg = findViewById(R.id. encode_IMG_loadimg);
        encode_TXT_secretmessage = findViewById(R.id.encode_TXT_secretmessage);
        encode_TXT_secretkey = findViewById(R.id.encode_TXT_secretkey);
        encode_BTN_send = findViewById(R.id.encode_BTN_send);
    }
}