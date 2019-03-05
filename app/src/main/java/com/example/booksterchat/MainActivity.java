package com.example.booksterchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference myDatabase;

    private FirebaseListAdapter<ChatMessage> adapter;
    private String receiverUID;
    private String globalKey = null;
    private String key;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        receiverUID = intent.getStringExtra("receiverUID");

        myDatabase = FirebaseDatabase.getInstance().getReference().child("Chats");


        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ListView mListView = findViewById(R.id.listView);
                ArrayList<String> chatIDs = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    ChatMessage chatMessage = ds.getValue(ChatMessage.class);
                    chatIDs.add(ds.getKey());
                }

                String v1, v2, myUID;
                myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                v1 = receiverUID+"_"+myUID;
                v2 = myUID+"_"+receiverUID;


                if(chatIDs.contains(v1) || chatIDs.contains(v2))
                {
                    if(chatIDs.contains(v1))
                    {
                        myDatabase.child(v1).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ListView mListView = findViewById(R.id.listView);
                                ArrayList<ChatMessage> chatModelList = new ArrayList<>();
                                for(DataSnapshot ds : dataSnapshot.getChildren())
                                {
                                    ChatMessage chatMessage = ds.getValue(ChatMessage.class);
                                    chatModelList.add(chatMessage);
                                }

                                ChatMessageListAdapter adapter = new ChatMessageListAdapter(MainActivity.this, R.layout.adapter_view_layout, chatModelList);
                                mListView.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        key = v1;
                    }


                    if(chatIDs.contains(v2))
                    {
                        myDatabase.child(v2).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ListView mListView = findViewById(R.id.listView);
                                ArrayList<ChatMessage> chatModelList = new ArrayList<>();
                                for(DataSnapshot ds : dataSnapshot.getChildren())
                                {
                                    ChatMessage chatMessage = ds.getValue(ChatMessage.class);
                                    chatModelList.add(chatMessage);
                                }
                                ChatMessageListAdapter adapter = new ChatMessageListAdapter(MainActivity.this, R.layout.adapter_view_layout, chatModelList);
                                mListView.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        key = v2;
                    }

                }

                if(!chatIDs.contains(v1) && !chatIDs.contains(v2))
                {
                    key = v1;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void sendMessage(View view)
    {
        EditText editText = findViewById(R.id.editText);
        ChatMessage chatMessage = new ChatMessage(
                editText.getText().toString(),
                FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                new Date().getTime()
        );
        myDatabase.child(key).push().setValue(chatMessage);
        editText.setText("");
    }

}
