package com.example.diplomayin;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class Contact {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public UserStatus status;

    public String lastMessage;
    public String time;
    public int avatarID;

    public Contact(String name, UserStatus status, String lastMessage, String time, int avatarID) {
        this.name = name;
        this.status = status;
        this.lastMessage = lastMessage;
        this.time = time;
        this.avatarID = avatarID;
    }

    public int getAvatarID() {
        return avatarID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
