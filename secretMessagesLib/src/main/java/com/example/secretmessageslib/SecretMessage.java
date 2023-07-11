package com.example.secretmessageslib;

import android.graphics.Bitmap;
import android.net.Uri;

public class SecretMessage {

    private String secretMessage;
    private String secretKey;
    private Uri imagePath;
    private Bitmap basic_image;
    private Bitmap encoded_image;
    private byte[] iv;
    private boolean isImageSelected = false;

    public SecretMessage(){}

    public String getSecretMessage() {
        return secretMessage;
    }

    public SecretMessage setSecretMessage(String secretMessage) {
        this.secretMessage = secretMessage;
        return this;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public SecretMessage setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public Uri getImagePath() {
        return imagePath;
    }

    public SecretMessage setImagePath(Uri imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public boolean getIsImageSelected() {
        return isImageSelected;
    }

    public SecretMessage setIsImageSelected(boolean imageSelected) {
        isImageSelected = imageSelected;
        return this;
    }

    public Bitmap getBasic_image() {
        return basic_image;
    }

    public SecretMessage setBasic_image(Bitmap basic_image) {
        this.basic_image = basic_image;
        return this;
    }

    public Bitmap getEncoded_image() {
        return encoded_image;
    }

    public byte[] getIv() {
        return iv;
    }

    public SecretMessage setIv(byte[] iv) {
        this.iv = iv;
        return this;
    }

    public SecretMessage setEncoded_image(Bitmap encoded_image) {
        this.encoded_image = encoded_image;
        return this;
    }
}
