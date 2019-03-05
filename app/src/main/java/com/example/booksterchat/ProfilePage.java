package com.example.booksterchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfilePage extends AppCompatActivity {

    String receiverUID = "UJ6Oeescpxc9nkvvNBMdwqinAFI3"; 
    String name = "John Doe";
    String email = "john.doe@gmail.com";
    Button messageBtn;
    TextView tvName, tvEmail, tvUID;
    RelativeLayout profile_activity;
    private static int SIGN_IN_REQUEST_CODE =1;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out)
        {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(profile_activity, "You have been signed out. ", Snackbar.LENGTH_SHORT).show();
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
                Snackbar.make(profile_activity, "Successfully signed in. Welcome! ", Snackbar.LENGTH_SHORT).show();
            }
            else
            {
                Snackbar.make(profile_activity, "Sorry, we couldn't sign you in. Please try again later. ", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        profile_activity = (RelativeLayout)findViewById(R.id.profile_activity);

        tvName = findViewById(R.id.name);
        tvEmail = findViewById(R.id.email);
        tvUID = findViewById(R.id.UID);

        tvName.setText(name);
        tvEmail.setText(email);
        tvUID.setText(receiverUID);

        //Check if not sign-in then navigate to signin page
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .build(), SIGN_IN_REQUEST_CODE);
        }
        else
        {
            Snackbar.make(profile_activity, "Welcome "+FirebaseAuth.getInstance().getCurrentUser().getEmail(), Snackbar.LENGTH_SHORT).show();
            //displayChatMessage();
        }

        messageBtn = findViewById(R.id.messageBtn);
        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for chat activity
                Intent myintent = new Intent(ProfilePage.this, MainActivity.class);
                myintent.putExtra("receiverUID", receiverUID);
                startActivity(myintent);
            }
        });
    }
}
