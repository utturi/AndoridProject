package com.example.andoridproject.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.andoridproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class ReadMessageActivity extends AppCompatActivity {
    String name;
    String text;
    String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_read_message);
        TextView username = findViewById(R.id.message_read_name);
        TextView textView = findViewById(R.id.message_read_text);
        FloatingActionButton button = findViewById(R.id.message_read_send);
        name = intent.getStringExtra("name");
        text = intent.getStringExtra("text");
        UID = intent.getStringExtra("UID");
        if(intent.getStringExtra("Identifier").equals("sender"))
            username.setText("Sender : " + name);
        else
            username.setText("Receiver : " + name);
        textView.setText("Text :  " +text);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getApplicationContext(),ReadToSendActivity.class);
                newIntent.putExtra("name",name);
                newIntent.putExtra("text",text);
                newIntent.putExtra("UID",UID);
                startActivity(newIntent);
            }
        });


    }
}
