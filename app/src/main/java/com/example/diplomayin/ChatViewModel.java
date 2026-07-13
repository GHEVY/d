package com.example.diplomayin;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {
    private final ChatDAO chatDAO;
    private final LiveData<List<Contact>> allContacts;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        chatDAO = db.chatDao();
        allContacts = chatDAO.getAllContactsLiveData();
    }

    public LiveData<List<Contact>> getAllContacts() {
        return allContacts;
    }
    
    public LiveData<List<Message>> getMessages(int contactId) {
        return chatDAO.getMessagesLiveData(contactId);
    }
}
