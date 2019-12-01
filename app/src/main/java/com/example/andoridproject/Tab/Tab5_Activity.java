package com.example.andoridproject.Tab;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.andoridproject.Activity.LoginActivity;
import com.example.andoridproject.Etc.DBHelper2;
import com.example.andoridproject.R;
import com.google.firebase.auth.FirebaseAuth;

public class Tab5_Activity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab5);
        Button button = findViewById(R.id.logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper2 helper = new DBHelper2(getApplicationContext());
                SQLiteDatabase db = helper.getWritableDatabase();
                String sql = "DELETE FROM STARPOST";
                db.execSQL(sql);
                FirebaseAuth.getInstance().signOut();
                startLoginActivity();
            }
        });
    }
    private void startLoginActivity()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}