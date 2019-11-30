package com.example.andoridproject.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.andoridproject.Item.Board;
import com.example.andoridproject.R;
import com.example.andoridproject.Tab.Tab1_Activity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PostActivity extends AppCompatActivity {
    private FloatingActionButton submit;
    private EditText title;
    private EditText text;
    private int button;
    private Board post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        submit = findViewById(R.id.submit_button);
        title = findViewById(R.id.post_title);
        text = findViewById(R.id.post_text);
        RelativeLayout layout = findViewById(R.id.post_layout);
        post = (Board)getIntent().getSerializableExtra("post");

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
            }
        });
        if(post!=null){                    //수정시에 작동
            title.setText(post.getTitle());
            text.setText(post.getContent());
            button=2;
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(button ==2)              //수정일때
                    writePost(2);
                else
                    writePost(1);
            }
        });

    }
    //파이어베이스에 put함
    private void writePost(int button)
    {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        String tit = title.getText().toString();
        String content = text.getText().toString();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String dataset[] = email.split("@");
        String username = dataset[0];
        SimpleDateFormat format1 = new SimpleDateFormat ( "yy.MM.dd");
        Date time = new Date();
        String time1 = format1.format(time);
        if(tit.length()>0 && content.length()>0) {
            if(button==2) {
                Board board = new Board(tit, content, userId, username,time1);
                database.child("posts").child(post.getKey()).setValue(board);
                Toast.makeText(getApplicationContext(), "등록중...", Toast.LENGTH_SHORT).show();
                ((Tab1_Activity) Tab1_Activity.CONTEXT).onResume();
                finish();
            }
            else {
                Board board = new Board(tit, content, userId, username,time1);
                database.child("posts").push().setValue(board);
                Toast.makeText(getApplicationContext(), "등록중...", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "제목, 내용을 모두 입력해주세요!", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
