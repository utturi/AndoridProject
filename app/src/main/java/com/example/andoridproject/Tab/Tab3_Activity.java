package com.example.andoridproject.Tab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;

import com.example.andoridproject.Activity.MainActivity;
import com.example.andoridproject.Adapter.BoardAdapter;
import com.example.andoridproject.Item.Board;
import com.example.andoridproject.Activity.PostActivity;
import com.example.andoridproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Tab3_Activity extends AppCompatActivity {
    private FloatingActionButton posting;
    private ListView listView; //게시판
    private SwipeRefreshLayout refresh; //게시판
    private ArrayList<Board> items;
    private BoardAdapter adapter;
    private DatabaseReference database;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab3);
        context = this;
        posting = findViewById(R.id.post_button);
        listView = findViewById(R.id.listview);
        refresh = findViewById(R.id.refrash);
        items = new ArrayList<Board>();
        refresh.setColorSchemeResources(R.color.MyGreen);
        setList();
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        items.clear();
                        setList();
                        refresh.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostActivity.class);
                startActivity(intent);
            }
        });

    }


    //여기도 그냥 똑같이 추가함
    @Override
    public void onRestart() {
        super.onRestart();
        items.clear();
        setList();
    }


    private void setList() {
        database = FirebaseDatabase.getInstance().getReference("posts");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String key = messageData.getKey();
                    Board data = messageData.getValue(Board.class);
                    data.setKey(key);
                    items.add(data);
                }
                adapter = new BoardAdapter(items, context);
                listView.setAdapter(adapter);
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