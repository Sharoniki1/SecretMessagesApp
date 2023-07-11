package com.example.secretmessageslib;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private Adapter_Message adapter_message;
    private RecyclerView chat_RCV_messageslist;
    private ExtendedFloatingActionButton chat_BTN_encode;
    private TextInputEditText chat_TXT_secretkey;
    private ExtendedFloatingActionButton chat_BTN_decode;
    private boolean isSecretKeyEntered = false;
    private int indexOfMessageToDecode = -1;
    private ArrayList<SecretMessage> messages = new ArrayList<>();
    DecodeManager decodeManager;


    CallBack_imageReadyToDecodeProtocol callBack_imageReadyProtocol = new CallBack_imageReadyToDecodeProtocol() {
        @Override
        public void imageClicked(int messageIndex) {
            //indexOfMessageToDecode = messageIndex;
            //isImageSelected = true;
            if( indexOfMessageToDecode == messageIndex){
                messages.get(messageIndex).setIsImageSelected(false);
                indexOfMessageToDecode = -1;
            }
            else {
                messages.get(messageIndex).setIsImageSelected(true);
                indexOfMessageToDecode = messageIndex;
            }

            adapter_message.notifyDataSetChanged();
            handleButton();
        }
    };

    CallBack_encodedMessageReadyProtocol callBack_encodedMessageReadyProtocol = new CallBack_encodedMessageReadyProtocol() {
        @Override
        public void handleMessage(SecretMessage message) {
            messages.add(message);
            adapter_message.notifyDataSetChanged();
        }
    };

    CallBack_decodedFinishedProtocol callBack_decodedFinishedProtocol = new CallBack_decodedFinishedProtocol() {
        @Override
        public void handleDecryptedData(String encrypted_message) {
            Log.d("mmm", "in callback decrypted finished");
            messages.get(indexOfMessageToDecode).setSecretMessage(encrypted_message);

            try {
                String decryptedMessage = CryptoUtil.decrypt(messages.get(indexOfMessageToDecode));// decryption for the encrypted text message
                if( decryptedMessage.equals(""))
                    decryptedMessage = "Empty Message";

                messages.get(indexOfMessageToDecode).setSecretMessage(decryptedMessage);
                showSecretMessageAlertDialog(decryptedMessage);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        findViews();
        initViews();

        EncodeActivity.setCallBack_encodedMessageReadyProtocol(callBack_encodedMessageReadyProtocol);
        decodeManager = new DecodeManager();
        decodeManager.setCallBack_decodedFinishedProtocol(callBack_decodedFinishedProtocol);
        adapter_message = new Adapter_Message(ChatActivity.this, messages);
        adapter_message.setCallBack_imageReadyProtocol(callBack_imageReadyProtocol);
        chat_RCV_messageslist.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        chat_RCV_messageslist.setAdapter(adapter_message);

    }

    private void initViews() {
        chat_BTN_encode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, EncodeActivity.class));
            }
        });

        chat_BTN_decode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decodeManager.setMessageToDecode(messages.get(indexOfMessageToDecode));
                if(decodeManager.isKeyValid(chat_TXT_secretkey.getText().toString()))
                    decodeManager.decryptData();

                else
                    Toast.makeText(ChatActivity.this,"Wrong key, try again", Toast.LENGTH_SHORT).show();
            }
        });

        chat_TXT_secretkey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                isSecretKeyEntered = true;
                handleButton();
            }
        });
    }

    private void showSecretMessageAlertDialog(String message) {
        AlertDialog.Builder secretMessageDialog = new AlertDialog.Builder(ChatActivity.this);
        secretMessageDialog.setTitle("Message");
        secretMessageDialog.setMessage(message);
        secretMessageDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                refreshChat();
            }
        });
        secretMessageDialog.create().show();
    }

    private void refreshChat(){
        isSecretKeyEntered = false;
        chat_TXT_secretkey.setText("");
        messages.remove(indexOfMessageToDecode);
        indexOfMessageToDecode = -1;
        adapter_message.notifyDataSetChanged();
        chat_BTN_decode.setEnabled(false);
    }

    private void handleButton() {
        if (indexOfMessageToDecode>=0 && isSecretKeyEntered )
            chat_BTN_decode.setEnabled(true);
    }

    private void findViews() {
        chat_RCV_messageslist = findViewById(R.id.chat_RCV_messageslist);
        chat_BTN_encode = findViewById(R.id.chat_BTN_encode);
        chat_TXT_secretkey = findViewById(R.id.chat_TXT_secretkey);
        chat_BTN_decode = findViewById(R.id.chat_BTN_decode);
    }
}