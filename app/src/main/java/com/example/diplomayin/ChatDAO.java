package com.example.diplomayin;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ChatDAO {
    @Query("SELECT * FROM messages WHERE contactId = :cId ORDER BY timestamp ASC")
    List<Message> getMessagesForContact(int cId);

    @Query("SELECT * FROM messages WHERE contactId = :cId ORDER BY timestamp ASC")
    LiveData<List<Message>> getMessagesLiveData(int cId);

    @Insert
    void insertMessage(Message message);

    @Query("SELECT * FROM contacts ORDER BY id DESC")
    List<Contact> getAllContacts();

    @Query("SELECT * FROM contacts ORDER BY id DESC")
    LiveData<List<Contact>> getAllContactsLiveData();

    @Insert
    void insertContact(Contact contact);

    @Query("UPDATE contacts SET lastMessage = :message, time = :time WHERE id = :contactId")
    void updateLastMessage(int contactId, String message, String time);






    @Query("DELETE FROM contacts")
    void deleteAllContacts();
}
