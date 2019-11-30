package com.example.andoridproject.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andoridproject.Item.Board;
import com.example.andoridproject.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class MessageActivity extends AppCompatActivity {
    TextView message_receiver; //받는 사람
    EditText message;
    FloatingActionButton button;
    Board post;
    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        message_receiver = findViewById(R.id.message_name);
        message = findViewById(R.id.message_text);
        button = findViewById(R.id.message_send);
        layout = findViewById(R.id.message_layout);
        //id랑 , 이름
        post = (Board)getIntent().getSerializableExtra("post"); //내가 만든 클래스 직렬화
        message_receiver.setText("To. "+post.getUserName()); //유저네임따옴
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("message");
                String recv_id = post.getUserID();
                String send_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String text = message.getText().toString();
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String dataset[] = email.split("@");
                String username = dataset[0];
                if (message.length() > 0) {
                    database.child(recv_id).child("receiver").push().setValue(username + "#" + text);
                    database.child(send_id).child("sender").push().setValue(post.getUserName() + "#" + text);
                    Toast.makeText(getApplicationContext(), "전송 완료", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(getApplicationContext(), "입력 내용이 없습니다", Toast.LENGTH_SHORT).show();
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
            }
        });
    }
}
