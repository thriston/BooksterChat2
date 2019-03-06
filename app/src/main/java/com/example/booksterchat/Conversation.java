package com.example.booksterchat;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Conversation {
    private String conversationID;
    private long lastActivityTime;
    private String myUID;
    private String receiverUID;
    private String receiverFullName;

    public Conversation(String conversationID, long lastActivityTime) {
        this.conversationID = conversationID;
        this.lastActivityTime = lastActivityTime;
        //this.receiverUID = "S6bAklJU4HdYxR0wxP8xvGzB72x1";
    }

    public String getConversationID() {
        return conversationID;
    }

    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }

    public long getLastActivityTime() {
        return lastActivityTime;
    }

    public void setLastActivityTime(long lastActivityTime) {
        this.lastActivityTime = lastActivityTime;
    }

    public String getMyUID() {
        return myUID;
    }

    public void setMyUID(String senderUID) {
        this.myUID = senderUID;
    }

    public String getReceiverUID() {
        return receiverUID;
    }

    public void setReceiverUID(String receiverUID) {
        this.receiverUID = receiverUID;
    }

    public String getReceiverFullName() {
        return receiverFullName;
    }

    public void setReceiverFullName(String receiverFullName) {
        this.receiverFullName = receiverFullName;
    }
}
