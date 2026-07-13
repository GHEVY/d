package com.example.diplomayin;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private AppDatabase db;
    public List<Contact> contactList = new ArrayList<>();
    private ChatAdapter adapter;
    private ChatViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerViewChats);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ChatAdapter(contactList);
        recyclerView.setAdapter(adapter);

        db = AppDatabase.getInstance(this);
        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        viewModel.getAllContacts().observe(this, contacts -> {
            contactList.clear();
            contactList.addAll(contacts);
            adapter.notifyDataSetChanged();
        });
        
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // Keep initial data insertion but it will now trigger LiveData automatically
                List<Contact> currentContacts = db.chatDao().getAllContacts();
                if (currentContacts.isEmpty()) {
                    db.chatDao().insertContact(new Contact("Աննա", UserStatus.ONLINE, "Հաղորդագրություններ չկան", " ", R.color.white));
                    db.chatDao().insertContact(new Contact("Իվան", UserStatus.OFFLINE, "Հաղորդագրություններ չկան", " ", R.color.black));
                    db.chatDao().insertContact(new Contact("Մարիա", UserStatus.TYPING, "Հաղորդագրություններ չկան", " ", R.color.white));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadContacts() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                List<Contact> loadedFromDb = db.chatDao().getAllContacts();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        contactList.clear();
                        contactList.addAll(loadedFromDb);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }


}
