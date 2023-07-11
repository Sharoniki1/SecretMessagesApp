package com.example.secretmessageslib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Adapter_Message extends RecyclerView.Adapter<Adapter_Message.MessageViewHolder> {

    private Context context;
    private ArrayList<SecretMessage> messages;
    private CallBack_imageReadyToDecodeProtocol callBack_imageReadyProtocol;

    public Adapter_Message(Context context, ArrayList<SecretMessage> messages){
        this.context = context;
        this.messages = messages;
    }

    public void setCallBack_imageReadyProtocol(CallBack_imageReadyToDecodeProtocol callBack_imageReadyProtocol){
        this.callBack_imageReadyProtocol = callBack_imageReadyProtocol;
    }


    @NonNull
    @Override
    public Adapter_Message.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list, parent, false);
        MessageViewHolder messageViewHolder = new MessageViewHolder(view);
        return messageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        SecretMessage message = messages.get(position);

        initViews(holder, position);
        Glide
                .with(context)
                .load(message.getImagePath())
                .into(holder.messagelist_IMG_encodedimg);

        if(message.getIsImageSelected())
            colorCheckMark(holder.messagelist_IMG_greencheckmark, holder.messagelist_IMG_checkmark);

        else
            colorCheckMark(holder.messagelist_IMG_checkmark, holder.messagelist_IMG_greencheckmark);
    }

    private void initViews(MessageViewHolder holder, int position) {
        holder.messagelist_IMG_encodedimg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callBack_imageReadyProtocol.imageClicked(position);
            }
        });
    }

    private void colorCheckMark(ImageView onCheckMark, ImageView offCheckMark ){
        offCheckMark.setVisibility(View.INVISIBLE);
        onCheckMark.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return messages == null? 0: messages.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView messagelist_IMG_encodedimg;
        private AppCompatImageView messagelist_IMG_checkmark;
        private AppCompatImageView messagelist_IMG_greencheckmark;


        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            findViews(itemView);
        }

        private void findViews(View itemView) {
            messagelist_IMG_encodedimg = itemView.findViewById(R.id.messagelist_IMG_encodedimg);
            messagelist_IMG_checkmark = itemView.findViewById(R.id.messagelist_IMG_checkmark);
            messagelist_IMG_greencheckmark= itemView.findViewById(R.id.messagelist_IMG_greencheckmark);
        }
    }

}
