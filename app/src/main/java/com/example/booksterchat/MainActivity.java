package com.example.booksterchat;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference myDatabase;
    private static int SIGN_IN_REQUEST_CODE =1;
    private FirebaseListAdapter<ChatMessage> adapter;

    RelativeLayout activity_main;



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out)
        {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(activity_main, "You have been signed out. ", Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //createProfile(user);
                Snackbar.make(activity_main, "Successfully signed in. Welcome! ", Snackbar.LENGTH_SHORT).show();
                //displayChatMessage();
            }
            else
            {
                Snackbar.make(activity_main, "Sorry, we couldn't sign you in. Please try again later. ", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity_main = (RelativeLayout)findViewById(R.id.activity_main);


        //Check if not sign-in then navigate to signin page
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .build(), SIGN_IN_REQUEST_CODE);
        }
        else
        {
            Snackbar.make(activity_main, "Welcome "+FirebaseAuth.getInstance().getCurrentUser().getEmail(), Snackbar.LENGTH_SHORT).show();
            //displayChatMessage();
        }





        myDatabase = FirebaseDatabase.getInstance().getReference("Chats");

        //final TextView textView = findViewById(R.id.textView);
        final ListView mListView = findViewById(R.id.listView);
        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //textView.setText(dataSnapshot.getValue().toString());

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
            public void onCancelled(DatabaseError databaseError) {
                //textView.setText("CANCELLED");
            }
        });
    }

    public void sendMessage(View view)
    {
        EditText editText = findViewById(R.id.editText);
        myDatabase.push().setValue(new ChatMessage(
                editText.getText().toString(),
                FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                new Date().getTime()
                ));
        editText.setText("");
    }
}
