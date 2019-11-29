package com.example.andoridproject;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TabHost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Tab2_Activity extends AppCompatActivity {
    ArrayList<String> recieve;
    ArrayList<String> send;
    MessageAdapter reciever;
    MessageAdapter sender;
    ListView recieve_list;
    ListView send_list;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab2);
        TabHost tabHost = findViewById(R.id.tabhost);
        recieve_list = findViewById(R.id.tab2_reciever);
        send_list = findViewById(R.id.tab2_sender);
        tabHost.setup();
        TabHost.TabSpec ts1 = tabHost.newTabSpec("Tab1").setIndicator("받은메시지");
        ts1.setContent(R.id.content1);
        tabHost.addTab(ts1);
        recieve = new ArrayList<String>();
        setList_reciever();

        TabHost.TabSpec ts2 = tabHost.newTabSpec("Tab2").setIndicator("보낸메시지");
        ts2.setContent(R.id.content2);
        tabHost.addTab(ts2);
        tabHost.setCurrentTab(0);
        send = new ArrayList<String>();
        setList_sender();
    }
    private void setList_reciever()
    {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("message").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("receiver");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot messageData : dataSnapshot.getChildren())
                {
                    String msg = messageData.getValue(String.class);
                    recieve.add(msg);
                }
                reciever= new MessageAdapter(recieve);
                recieve_list.setAdapter(reciever);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void setList_sender()
    {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("message").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("sender");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot messageData : dataSnapshot.getChildren())
                {
                    String msg = messageData.getValue(String.class);
                    send.add(msg);
                }
                sender= new MessageAdapter(send);
                send_list.setAdapter(sender);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}