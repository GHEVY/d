package com.example.diplomayin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private final List<Contact> chatList;

    public ChatAdapter(List<Contact> chatList) {
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Contact chat = chatList.get(position);
        holder.textName.setText(chat.getName());
        holder.textLastMessage.setText(chat.getLastMessage());
        holder.textTime.setText(chat.getTime());
        holder.icon.setImageResource(chat.getAvatarID());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatFragment chatFragment = new ChatFragment();
                
                Bundle args = new Bundle();
                args.putInt("contact_id", chat.getId());
                args.putString("contact_name", chat.getName());
                chatFragment.setArguments(args);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main, chatFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList == null ? 0 : chatList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textLastMessage, textTime;
        ImageButton icon;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.name);
            textLastMessage = itemView.findViewById(R.id.last_message);
            textTime = itemView.findViewById(R.id.time);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}
