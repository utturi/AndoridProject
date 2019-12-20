package com.example.andoridproject.Activity;

import androidx.annotation.NonNull;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.andoridproject.R;
import com.example.andoridproject.Tab.Tab4_Activity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends Activity {
    private DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        database = FirebaseDatabase.getInstance().getReference("posts");      //데이터베이스에 값 가져오는딜레이잡기
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 1000); // 1초 후에 hd handler 실행  3000ms = 3초


    }
    private class splashhandler implements Runnable{
        public void run(){
            startActivity(new Intent(getApplication(), LoginActivity.class)); //로딩이 끝난 후, ChoiceFunction 이동
            SplashActivity.this.finish(); // 로딩페이지 Activity stack에서 제거
        }
    }
    @Override
    public void onBackPressed() {
        //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }
}
