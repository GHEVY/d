package com.example.diplomayin;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private ImageButton backBtn;
    private TextView contactNameTv;
    private TextView typingStatusTv;
    private EditText messageEt;
    private ImageButton sendBtn;
    
    private int contactId;
    private String contactName;
    private AppDatabase db;
    private MessageAdapter adapter;
    private ChatViewModel viewModel;
    private List<Message> messageList = new ArrayList<>();

    private final String[] botResponses = {
            "Բարև: Ինչպե՞ս ես:",
            "Հիմա մի քիչ զբաղված եմ, հետո կպատասխանեմ:",
            "Հետաքրքիր է:",
            "Այո, համաձայն եմ:",
            "Վա՜յ, չի կարող պատահել:",
            "Պարզ է:",
            "Հա-հա, շատ ծիծաղելի է:"
    };

    public ChatFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            contactId = getArguments().getInt("contact_id");
            contactName = getArguments().getString("contact_name");
        }
        db = AppDatabase.getInstance(requireContext());
        viewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewChats);
        backBtn = view.findViewById(R.id.back);
        contactNameTv = view.findViewById(R.id.name);
        typingStatusTv = view.findViewById(R.id.typingStatus);
        messageEt = view.findViewById(R.id.editTextMessage);
        sendBtn = view.findViewById(R.id.buttonSend);

        contactNameTv.setText(contactName);

        // Filter to allow only Armenian characters, numbers, and punctuation
        InputFilter armenianFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    // Armenian Unicode range: 0530-058F. Also allow basic symbols/numbers
                    if (!Character.isWhitespace(c) && (c < 0x0530 || c > 0x058F) && 
                        !Character.isDigit(c) && (c < 32 || c > 64)) {
                        return "";
                    }
                }
                return null;
            }
        };
        messageEt.setFilters(new InputFilter[]{armenianFilter});

        adapter = new MessageAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        loadMessages();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        return view;
    }

    private void loadMessages() {
        viewModel.getMessages(contactId).observe(getViewLifecycleOwner(), messages -> {
            messageList.clear();
            messageList.addAll(messages);
            adapter.notifyDataSetChanged();
            if (!messageList.isEmpty()) {
                recyclerView.scrollToPosition(messageList.size() - 1);
            }
        });
    }

    private void sendMessage() {
        String text = messageEt.getText().toString().trim();
        
        if (!text.isEmpty()) {
            long timestamp = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String timeStr = sdf.format(new Date(timestamp));

            Message message = new Message(contactId, text, timestamp, true);
            messageEt.setText("");
            
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    db.chatDao().insertMessage(message);
                    db.chatDao().updateLastMessage(contactId, text, timeStr);

                    // Show typing status
                    requireActivity().runOnUiThread(() -> {
                        if (typingStatusTv != null) typingStatusTv.setVisibility(View.VISIBLE);
                    });

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                    // Hide typing status
                    requireActivity().runOnUiThread(() -> {
                        if (typingStatusTv != null) typingStatusTv.setVisibility(View.GONE);
                    });

                    String response = botResponses[(int) (Math.random() * botResponses.length)];
                    long botTimestamp = System.currentTimeMillis();
                    String botTimeStr = sdf.format(new Date(botTimestamp));
                    
                    Message botMessage = new Message(contactId, response, botTimestamp, false);
                    db.chatDao().insertMessage(botMessage);
                    db.chatDao().updateLastMessage(contactId, response, botTimeStr);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
