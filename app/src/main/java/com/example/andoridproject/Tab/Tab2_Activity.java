package com.example.andoridproject.Tab;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;
import android.widget.TabHost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.andoridproject.Activity.MainActivity;
import com.example.andoridproject.Adapter.MessageAdapter;
import com.example.andoridproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//TAB2 BACK UP

public class Tab2_Activity extends AppCompatActivity {
    ArrayList<String> recieve;
    ArrayList<String> send;
    MessageAdapter reciever;
    MessageAdapter sender;
    ListView recieve_list;
    ListView send_list;
    private SwipeRefreshLayout refresh;
    private SwipeRefreshLayout refresh2;
    public Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_tab2);
        TabHost tabHost = findViewById(R.id.tabhost);
        recieve_list = findViewById(R.id.tab2_reciever);
        send_list = findViewById(R.id.tab2_sender);
        refresh = findViewById(R.id.tab2_refresh);
        refresh2 = findViewById(R.id.tab2_refresh2);

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
        refresh.setColorSchemeResources(R.color.MyGreen);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recieve.clear();
                        setList_reciever();
                        refresh.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        refresh2.setColorSchemeResources(R.color.MyGreen);
        refresh2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh2.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        send.clear();
                        setList_sender();
                        refresh2.setRefreshing(false);
                    }
                }, 1000);
            }
        });
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
                reciever= new MessageAdapter(recieve,context);
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
                sender= new MessageAdapter(send,context);
                send_list.setAdapter(sender);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        MainActivity.tabHost.setCurrentTab(0);
    }
}