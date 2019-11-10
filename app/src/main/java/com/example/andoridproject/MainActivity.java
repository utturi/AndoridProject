package com.example.andoridproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView mainbut;           //음식이름 뜨는 큰 화면
    ImageButton directbut;      //직접등록버튼
    DatePickerDialog mDialog;   //달력사용
    String TAG = "FoodName";    //직접등록 다이얼로그
    String name;                //음식이름
    ImageButton soundbut;       //사운드 버튼
    public static Context CONTEXT;
    private DrawerLayout drawerLayout;
    private View drawerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CONTEXT =this;
        mainbut = findViewById(R.id.mainbutton);
        DBHelper helper = new DBHelper(CONTEXT);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "SELECT name, date FROM FOOD ORDER BY date ASC";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.getCount()!=0) {
            cursor.moveToNext();
            mainbut.setText(cursor.getString(0) + "\n" + cursor.getString(1));
        }
        db.close();

        //직접등록버튼 입력 -> 달력(유통기한설정)
        directbut = findViewById(R.id.directbutton);
        directbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                ad.setTitle("음식입력"); //제목

                final EditText et = new EditText(MainActivity.this);
                ad.setView(et);

                //확인 버튼 설정
                ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v(TAG, "Yes Btn Click");

                        //Text값 받아서 로그 남기기
                        name = et.getText().toString(); //음식입력받은 값 저장
                        Log.v(TAG, name);

                        dialog.dismiss(); //닫기
                        mDialog.show(); //달력창 띄우기
                        //Event
                    }
                });
                //취소 버튼 설정
                ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v(TAG, "No Btn Click");
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });
                ad.show();
            }
        });

        // DatePickerDialog
        mDialog = new DatePickerDialog(this, listener, 2019, 11, 8);

        //SubActivity로 넘어가는 버튼
        mainbut = findViewById(R.id.mainbutton);
        mainbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SubmainActivity.class);
                startActivity(intent);
            }
        });

        //음성등록버튼 작업
        soundbut = findViewById(R.id.soundbutton);
        soundbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mDialog.show();
            }
        });

        // Navigation Drawer 버튼
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById((R.id.drawer));


        // menu버튼을 누르면 navigation Drawer가 나옴
        Button menu = (Button) findViewById((R.id.menu));
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        // 뒤로 가기 버튼 누를 때
        Button btn_close = findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });

        drawerLayout.setDrawerListener(listener_);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    //
    DrawerLayout.DrawerListener listener_ = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    //달력출력및 DB추가
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear+=1;               //월이 1작아서 1추가
            String day;
            if(dayOfMonth<10) {          //YYYY-MM-DD 형식으로 맞추기위해
                day = "0" + dayOfMonth;
            }
            else
                day = ""+dayOfMonth;
            String date = year+"-"+monthOfYear+"-"+day;
            DBHelper helper = new DBHelper(CONTEXT);
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("INSERT INTO FOOD (name, date) VALUES (?, ?)",new String[]{name,date});
            String sql = "SELECT name, date FROM FOOD ORDER BY date ASC";
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToNext();
            mainbut.setText(cursor.getString(0) + "\n" + cursor.getString(1));
            db.close();
        }
    };

    //화면 새로고침
    @Override
    public void onResume() {
        super.onResume();
        DBHelper helper = new DBHelper(CONTEXT);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "SELECT name, date FROM FOOD ORDER BY date ASC";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.getCount()!=0) {
            cursor.moveToNext();
            mainbut.setText(cursor.getString(0) + "\n" + cursor.getString(1));
        }
        else
            mainbut.setText("음식이 없어요!");
        db.close();
    }


}