package com.example.secretmessageslib;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

public class EncodeManager {


    private SecretMessage messageToEncode;
    private boolean isMessageReady = false;
    private boolean isSecretKeyReady = false;
    private boolean isImageReady = false;
    private CallBack_encodedFinishedProtocol callBack_encodedFinishedProtocol;

    public EncodeManager(){
        messageToEncode = new SecretMessage();
    }

    public SecretMessage getMessageToEncode() {
        return messageToEncode;
    }

    public void setCallBack_encodedFinishedProtocol(CallBack_encodedFinishedProtocol callBack_encodedFinishedProtocol){
        this.callBack_encodedFinishedProtocol = callBack_encodedFinishedProtocol;
    }

    public void setMessageReady(boolean messageReady) {
        isMessageReady = messageReady;
    }

    public void setSecretKeyReady(boolean secretKeyReady) {
        isSecretKeyReady = secretKeyReady;
    }

    public void setImageReady(boolean imageReady) {
        isImageReady = imageReady;
    }

    public boolean ready() {
        return isImageReady && isMessageReady && isSecretKeyReady;
    }

    public void setImagePath(Uri imagePath) {
        messageToEncode.setImagePath(imagePath);
    }

    public void encryptData() throws Exception {
        String encrypted_message = CryptoUtil.encrypt(messageToEncode); // encrypts the secret message text
        messageToEncode.setSecretMessage("");
        encodeMsgToIMG(encrypted_message);
    }

    private void encodeMsgToIMG(String encrypted_message) {

        //size of image
        int img_width = messageToEncode.getBasic_image().getWidth();
        int img_height = messageToEncode.getBasic_image().getHeight();


        int pixelsArray[] = new int[img_width*img_height];
        messageToEncode.getBasic_image().getPixels(pixelsArray, 0, img_width, 0, 0, img_width, img_height);
        int density = messageToEncode.getBasic_image().getDensity();

        byte[] encodedIMG = SteganographicsManager.encode(pixelsArray, encrypted_message, img_width, img_height);

        int encoded_pixels[] = convertByteArrayToIntArray(encodedIMG);

        messageToEncode.setEncoded_image(covert(encoded_pixels, img_height, img_width, density));
        SteganographicsManager.setMsgEncodeToImage(false);
        callBack_encodedFinishedProtocol.handleEncryptedData();
    }

    private Bitmap covert(int[] encoded_pixels, int height, int width, int density) {
        Bitmap encoded_bitMap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        encoded_bitMap.setDensity(density);

        int masterIndex =0;

        for( int i=0; i<height; i++){
            for(int j=0; j< width; j++){
                encoded_bitMap.setPixel(j,i, Color.argb(0xFF,
                        encoded_pixels[masterIndex] >> 16 & 0xFF,
                        encoded_pixels[masterIndex] >> 8 & 0xFF,
                        encoded_pixels[masterIndex] & 0xFF));
               masterIndex++;
            }
        }
        return encoded_bitMap;
    }

    private int[] convertByteArrayToIntArray(byte[] byteArray) {
        int[] intArray = new int[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            intArray[i] = byteArray[i] & 0xFF;
        }
        return intArray;
    }
}
