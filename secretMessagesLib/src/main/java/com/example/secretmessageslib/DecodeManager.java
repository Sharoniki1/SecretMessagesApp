package com.example.secretmessageslib;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

public class DecodeManager {

    private SecretMessage messageToDecode;
    private CallBack_decodedFinishedProtocol callBack_decodedFinishedProtocol;


    public DecodeManager(){
        messageToDecode = new SecretMessage();
    }

    public void setCallBack_decodedFinishedProtocol(CallBack_decodedFinishedProtocol callBack_decodedFinishedProtocol){
        this.callBack_decodedFinishedProtocol = callBack_decodedFinishedProtocol;
    }

    public void decryptData() {

        Bitmap encoded_bitMap = messageToDecode.getEncoded_image();
        int pixels[] = new int[encoded_bitMap.getWidth()*encoded_bitMap.getHeight()];
        encoded_bitMap.getPixels(pixels, 0, encoded_bitMap.getWidth(), 0,0,encoded_bitMap.getWidth(), encoded_bitMap.getHeight());

        String encrypted_message = SteganographicsManager.decode(pixels);

        SteganographicsManager.setMsgDecodedFromImage(false);
        callBack_decodedFinishedProtocol.handleDecryptedData(encrypted_message);
    }

    public void setMessageToDecode(SecretMessage secretMessage) {
        this.messageToDecode = secretMessage;
    }

    public boolean isKeyValid(String secret_key) {
        return secret_key.equals(messageToDecode.getSecretKey());

    }
}
