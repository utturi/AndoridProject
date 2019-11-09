package com.example.andoridproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView mainbut; //음식이름 뜨는 큰 화면
    ImageButton directbut; //직접등록버튼
    DatePickerDialog mDialog; //달력사용
    String TAG = "FoodName"; //직접등록 다이얼로그
    String name;           //음식이름
    ImageButton soundbut; //사운드 버튼
    SQLiteDatabase database; //데이터베이스

    private DrawerLayout drawerLayout;
    private View drawerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.testButton);
        mainbut = findViewById(R.id.mainbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectData("Food");
            }
        });

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
        // mainbut = findViewById(R.id.mainbutton);
        mainbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mDialog.show();
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

    //달력출력함수
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            openDatabase("Food.db");
            createTable("Food");
            insertData(name, year+"-"+monthOfYear+"-"+dayOfMonth,"Food");
            mainbut.setText(name+"\n"+year+"-"+monthOfYear+"-"+dayOfMonth);

            //((SubmainActivity)SubmainActivity.mContext).createTextView(name,year+"-"+monthOfYear+"-"+dayOfMonth);
        }
    };
    public void openDatabase(String databaseName)
    {
        database = openOrCreateDatabase(databaseName,MODE_PRIVATE,null);
    }
    public void createTable(String tableName)
    {

        if(database != null)
        {
            String sql = "Create Table IF NOT EXISTS " + tableName+ " (_id integer primary key autoincrement, name text, date text)";
            database.execSQL(sql);
        }
    }
    public void insertData(String name, String date,String tablename)
    {
        if(database!=null)
        {
            String sql = "insert into " + tablename+ "(name, date) values(?, ?)";
            Object[] params = {name, date};
            database.execSQL(sql,params);
        }
    }
    public void selectData(String tableName)
    {

        if(database!=null)
        {
            String str ="";
            String sql = "select name, date from " + tableName;
            Cursor cursor = database.rawQuery(sql,null);
            for(int i = 0; i <cursor.getCount();i++)
            {
                cursor.moveToNext();
                String name = cursor.getString(0);
                String date = cursor.getString(1);

                str = str + "#"+i+" -> "+name+", "+date+"/n";
            }
            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
        }
    }
}