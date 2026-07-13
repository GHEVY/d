package com.example.diplomayin;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "messages")
public class Message {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int contactId;
    public String text;
    public long timestamp;
    public boolean isFromUser;
    public Message(){};

    public Message(int contactId, String text, long timestamp, boolean isFromMe) {
        this.contactId = contactId;
        this.text = text;
        this.timestamp = timestamp;
        this.isFromUser = isFromMe;
    }
}