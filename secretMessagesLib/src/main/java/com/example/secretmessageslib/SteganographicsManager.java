package com.example.secretmessageslib;

import android.util.Log;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Vector;

public class SteganographicsManager {

    private static boolean msgEncodeToImage = false;
    private static boolean msgDecodedFromImage = false;
    private static final String END_MESSAGE_CONSTANT = "#!@";
    private static final String START_MESSAGE_CONSTANT = "@!#";
    private static final int[] binary = {16, 8, 0};
    private static final byte[] andByte = {(byte) 0xC0, 0x30, 0x0C, 0x03};
    private static final int[] toShift = {6, 4, 2, 0};

    public static void setMsgEncodeToImage(boolean status){
        msgEncodeToImage = status;
    }
    public static void setMsgDecodedFromImage(boolean status){
        msgDecodedFromImage = status;
    }

    public static byte[] encode(int[] pixelsArray, String encrypted_message, int img_width, int img_height) {

        StringBuilder builder = new StringBuilder();
        builder.append(START_MESSAGE_CONSTANT)
                .append(encrypted_message)
                .append(END_MESSAGE_CONSTANT);

        encrypted_message = builder.toString();
        byte[] encrpted_msg = encrypted_message.getBytes();

        int channels = 3;  //RGB color channels
        int shiftIndex = 4;

        byte[] encoded_img = new byte[img_width * img_height * channels];
        int encoded_imgIndex =0;
        int encrypted_msgIndex=0;

        for(int i=0; i<img_height; i++){
            for(int j=0; j<img_width; j++){

                int element = i*img_width + j;
                byte tmpByte;

                for(int c= 0; c<channels; c++){
                    if(!msgEncodeToImage){
                        tmpByte = (byte) ((((pixelsArray[element] >> binary[c]) & 0xFF ) & 0xFC) | ((encrpted_msg[encrypted_msgIndex] >> toShift[(shiftIndex++)% toShift.length]) & 0x3));
                        if(shiftIndex % toShift.length == 0)
                            encrypted_msgIndex++;

                        if(encrypted_msgIndex == encrpted_msg.length)
                            msgEncodeToImage = true;
                    }
                    else{
                        //Message was fully encoded to img so copy the pixel as it is
                        tmpByte = (byte) ((((pixelsArray[element] >> binary[c]) & 0xFF)));
                    }
                    encoded_img[encoded_imgIndex++] = tmpByte;
                }
            }
        }
        return encoded_img;
    }

    public static String decode(int[] pixels){

        Vector<Byte> encrypted_message = new Vector<Byte>();
        int shiftIndex = 4;
        byte currentByte = 0x00;
        String encrypted_data_checker ="";

        for(int pixel : pixels){
            currentByte = (byte)(currentByte | ((pixel << toShift[shiftIndex % toShift.length]) & andByte[shiftIndex++ % toShift.length]));

            if(shiftIndex % toShift.length == 0){
                encrypted_message.addElement(new Byte(currentByte));

                byte[] current = { encrypted_message.elementAt((encrypted_message.size()-1)).byteValue()};
                String byteToChar = new String(current);

                if(encrypted_data_checker.endsWith(END_MESSAGE_CONSTANT))
                    break;

                else{
                    encrypted_data_checker = encrypted_data_checker + byteToChar;
                    if(encrypted_data_checker.length() == START_MESSAGE_CONSTANT.length() && !START_MESSAGE_CONSTANT.equals(encrypted_data_checker)){
                        encrypted_data_checker = null;
                        break;
                    }
                }
                currentByte = 0x00;
            }
        }
        if(encrypted_data_checker != null)
            encrypted_data_checker = encrypted_data_checker.substring(START_MESSAGE_CONSTANT.length(), encrypted_data_checker.length() - END_MESSAGE_CONSTANT.length());

        return encrypted_data_checker;
    }
}
