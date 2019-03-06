package com.example.booksterchat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ConversationListAdapter extends ArrayAdapter<Conversation> {
    private Context mContext;
    private int mResource;
    private String myUID;
    private String receiverUID;
    private String key;

    public ConversationListAdapter(Context context, int resource, ArrayList<Conversation> objects){
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.myUID = myUID;
        this.receiverUID = receiverUID;
    }



    @Override
    public View getView(int position,View convertView, ViewGroup parent) {

        //search for chatname and then find chat messages
        DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        String v1, v2;
        v1 = receiverUID+"_"+myUID;
        v2 = myUID+"_"+receiverUID;

        String fullName = getItem(position).getReceiverFullName();
        String conversationID = getItem(position).getConversationID();
        long lastActivityTime = getItem(position).getLastActivityTime();

        Conversation conversation = new Conversation(conversationID, lastActivityTime);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent,false);

        TextView tvDisplayName = (TextView) convertView.findViewById(R.id.chat_display_name);
        TextView tvChatActivity = (TextView) convertView.findViewById(R.id.chat_last_activity);

        tvDisplayName.setText(fullName);
        tvChatActivity.setText(DateFormat.format("h:mma dd-MM-yyyy", lastActivityTime));


        return convertView;

    }
}
