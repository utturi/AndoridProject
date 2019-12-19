package com.example.andoridproject.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.andoridproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadToSendActivity extends AppCompatActivity {

    TextView message_receiver; //받는 사람
    EditText message;
    FloatingActionButton button;
    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_to_send);
        message_receiver = findViewById(R.id.message_send_name);
        message = findViewById(R.id.message_send_text);
        button = findViewById(R.id.message_send_button);
        final Intent intent = getIntent();
        message_receiver.setText("To. "+intent.getStringExtra("name")); //유저네임따옴
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("message");
                String recv_id = intent.getStringExtra("UID");
                String send_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String text = message.getText().toString();
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String dataset[] = email.split("@");
                String username = dataset[0];
                SimpleDateFormat format1 = new SimpleDateFormat ( "yy.MM.dd");
                Date time = new Date();
                String time1 = format1.format(time);
                if (message.length() > 0) {
                    database.child(recv_id).child("receiver").push().setValue(username + "#" + text + "#"+send_id+"#"+time1);
                    database.child(send_id).child("sender").push().setValue(intent.getStringExtra("name") + "#" + text + "#"+recv_id+"#"+time1);
                    Toast.makeText(getApplicationContext(), "전송 완료", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(getApplicationContext(), "입력 내용이 없습니다", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
