package com.example.secretmessageslib;

import android.util.Base64;
import android.util.Log;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtil {

    private static final int PSWD_ITERATION = 10;
    private static final int KEY_SIZE = 128;
    private static final String AESSalt = "exampleSalt";

    public static String encrypt(SecretMessage messageToEncrypt)throws Exception{
        SecretKeySpec skeySpec = new SecretKeySpec(getRawKey(messageToEncrypt.getSecretKey(), AESSalt), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] iv = cipher.getIV();
        messageToEncrypt.setIv(iv);

        byte[] encrypted = cipher.doFinal(messageToEncrypt.getSecretMessage().getBytes());
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    public static String decrypt(SecretMessage messageToDecrypt) throws Exception {
        byte[] encryted_bytes = Base64.decode(messageToDecrypt.getSecretMessage(), Base64.DEFAULT);
        SecretKeySpec skeySpec = new SecretKeySpec(getRawKey(messageToDecrypt.getSecretKey(), AESSalt), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(messageToDecrypt.getIv()));
        byte[] decrypted = cipher.doFinal(encryted_bytes);
        return new String(decrypted, "UTF-8");
    }

    private static byte[] getRawKey(String plainText, String salt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(plainText.toCharArray(), salt.getBytes(), PSWD_ITERATION, KEY_SIZE);
            return factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
