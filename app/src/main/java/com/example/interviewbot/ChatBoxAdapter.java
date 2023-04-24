package com.example.interviewbot;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
// this class is for setting the adapter for both user and bot messages

public class ChatBoxAdapter extends RecyclerView.Adapter{

//    we'll be storing the data in this arraylist
    private ArrayList<ChatsModel> chatsModelArrayList;
    private Context context;

    public ChatBoxAdapter(ArrayList<ChatsModel> chatsModelArrayList, Context context) {
        this.chatsModelArrayList = chatsModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_bg,parent,false);
                return new UserViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_bg,parent,false);
                return new BotViewHolder(view);
        }
        return  null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//          check who is the sender
        ChatsModel chatsModel = chatsModelArrayList.get(position);
        switch(chatsModel.getSender()){
            case "user":
                ((UserViewHolder)holder).user.setText(chatsModel.getMessage());
                break;
            case "bot":
                ((BotViewHolder)holder).bot.setText(chatsModel.getMessage());
                break;
        }
    }

    public int getItemViewType(int position){
        switch(chatsModelArrayList.get(position).getSender()){
            case "user":
                return 0;
            case "bot":
                return 1;
            default:
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        return chatsModelArrayList.size();
    }


//    These class will hold the textviews
    public class UserViewHolder extends RecyclerView.ViewHolder{
       TextView user;
    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        user = itemView.findViewById(R.id.user_msg);
    }
}

    public class BotViewHolder extends RecyclerView.ViewHolder{
        TextView bot;
        public BotViewHolder(@NonNull View itemView) {
            super(itemView);
            bot = itemView.findViewById(R.id.bot_msg);
        }
    }
}
