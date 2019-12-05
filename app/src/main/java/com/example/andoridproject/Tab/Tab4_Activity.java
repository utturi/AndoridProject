package com.example.andoridproject.Tab;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.andoridproject.Activity.MainActivity;
import com.example.andoridproject.R;

public class Tab4_Activity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab4);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        MainActivity.tabHost.setCurrentTab(0);
    }
}