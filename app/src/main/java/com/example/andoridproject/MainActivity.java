package com.example.andoridproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    ImageButton mainbut; //음식이름 뜨는 큰 화면
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SubActivity로 넘어가는 버튼
        mainbut = findViewById(R.id.mainbutton);

        mainbut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //인텐트 선언 -> 현재 엑티비티, 넘어갈 엑티비티
                Intent intent = new Intent(MainActivity.this,Submain_Activity.class);
                //인텐트 실행
                startActivity(intent);
            }
        });
    }
}
