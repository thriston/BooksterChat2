package com.example.booksterchat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConversationChatActivity extends AppCompatActivity {

    private String myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversations_layout);


        DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(myUID).child("Conversations");


        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ListView mListView = findViewById(R.id.chatListView);
                ArrayList<Conversation> conversationsModel= new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Conversation conversation = new Conversation(ds.getKey(),(Long) ds.getValue());
                    conversationsModel.add(conversation);
                }
                ConversationListAdapter adapter = new ConversationListAdapter(ConversationChatActivity.this, R.layout.conversations_view_layout, conversationsModel);
                mListView.setAdapter(adapter);
                //System.out.println("CONVERSATIONS: "+conversationsModel.get(0).getLastActivityTime());
                //System.out.println("TIMES: "+lastActivityTimes);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
