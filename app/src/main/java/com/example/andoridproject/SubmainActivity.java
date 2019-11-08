package com.example.andoridproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

public class SubmainActivity extends AppCompatActivity {
    public static Context mContext;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    int tableCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_main);
        textView1 = findViewById(R.id.textTable1);
        textView2 = findViewById(R.id.textTable2);
        textView3 = findViewById(R.id.textTable3);
        textView4 = findViewById(R.id.textTable4);
        textView5 = findViewById(R.id.textTable5);
        mContext = this;
    }
    public void createTextView(String name, String date)
    {
        switch(tableCount)
        {
            case 0 : textView1.setCursorVisible(true);textView1.setText(name+"/n"+date);tableCount++;break;
            case 1 : textView2.setCursorVisible(true);textView2.setText(name+"/n"+date);tableCount++;break;
            case 2 : textView3.setCursorVisible(true);textView3.setText(name+"/n"+date);tableCount++;break;
            case 3 : textView4.setCursorVisible(true);textView4.setText(name+"/n"+date);tableCount++;break;
            case 4 : textView5.setCursorVisible(true);textView5.setText(name+"/n"+date);tableCount++;break;

        }
    }
}
