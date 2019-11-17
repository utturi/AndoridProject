package com.example.andoridproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.relex.circleindicator.CircleIndicator;


public class MainActivity extends AppCompatActivity {
    ImageButton directbut;      //직접등록버튼
    DatePickerDialog mDialog;   //달력사용
    String TAG = "FoodName";    //직접등록 다이얼로그
    String name;                //음식이름
    ImageButton soundbut;       //사운드 버튼
    ImageButton layer[];        //gage배열

    private ViewPager viewPager ;           //뷰페이저
    private MainPagerAdapter pagerAdapter ; //어댑터
    private CircleIndicator indicator;      //인디케이터

    //대호
    public static Context CONTEXT;
    private DrawerLayout drawerLayout;
    private View drawerView;

    //의현
    private static final int REQUEST_CODE = 1234;
    ArrayList<String> matches_text;
    //의현

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CONTEXT = this;
        layer = new ImageButton[10];
        layer[0] = findViewById(R.id.layer1);
        layer[1] = findViewById(R.id.layer2);
        layer[2] = findViewById(R.id.layer3);
        layer[3] = findViewById(R.id.layer4);
        layer[4] = findViewById(R.id.layer5);
        layer[5] = findViewById(R.id.layer6);
        layer[6] = findViewById(R.id.layer7);
        layer[7] = findViewById(R.id.layer8);
        layer[8] = findViewById(R.id.layer9);
        layer[9] = findViewById(R.id.layer10);
        ImageButton eat_button = findViewById(R.id.eatBut);
        ImageButton youtube = findViewById(R.id.youtubebut);
        //ViewPager
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        viewPager = (ViewPager) findViewById(R.id.mainbutton) ;
        setMainbut();
        setGage();
        eat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper helper = new DBHelper(CONTEXT);
                SQLiteDatabase db = helper.getWritableDatabase();
                String sql = "SELECT name, date FROM FOOD ORDER BY date ASC";
                Cursor cursor = db.rawQuery(sql, null);
                if (cursor.getCount() != 0) {
                    cursor.moveToNext();
                    String name = cursor.getString(0);
                    String date = cursor.getString(1);
                    sql = "DELETE FROM FOOD WHERE name = '" + name + "' AND date = '" + date + "';";
                    db.execSQL(sql);
                    Toast.makeText(getApplicationContext(), "먹었어요~", Toast.LENGTH_SHORT).show();
                }
                db.close();
                onResume();
            }
        });
        youtube.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),""+viewPager.getCurrentItem(),Toast.LENGTH_SHORT).show();
                indicator.getDataSetObserver();
            }
        });
        //의현 - 음성등록
        soundbut = findViewById(R.id.soundbutton);

        soundbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    Toast.makeText(getApplicationContext(),"음식이름 기한날짜 순으로 말해주세요!\nex) 돼지갈비 2019년 11월 12일",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"음식이름 날짜 순으로 입력하세요~");
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    Toast.makeText(getApplicationContext(), "Plese Connect to Internet", Toast.LENGTH_LONG).show();
                }
            }

        });
        //의현

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

    //의현
    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        if (net != null && net.isAvailable() && net.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data1) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            matches_text = data1
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String text = matches_text.get(0).replace(" ", "");
            String name = "", data = "";
            String[] final_arr;
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) >= '0' && text.charAt(i) <= '9') {
                    data = text.substring(i, text.length() - 1);
                    i = text.length() - 1;
                } else { //음식이름
                    name += text.charAt(i);
                }
            }
            data = data.replace("년", "-");
            data = data.replace("월", "-");
            String[] temp_data = data.split("-");
            String date = ""; //최종 년-월-일

            if (name == "" || data == "")
                Toast.makeText(getApplicationContext(), "입력이 잘못되었습니다.", Toast.LENGTH_LONG).show();
            else {
                if ((Integer.parseInt(temp_data[0]) >= 2019) && (Integer.parseInt(temp_data[0]) < 2050)) {
                    if (Integer.parseInt(temp_data[1]) < 13) {
                        if (Integer.parseInt(temp_data[2]) < 32) {
                            if (Integer.parseInt(temp_data[1]) < 10)
                                temp_data[1] = "0" + temp_data[1]; //월
                            if (Integer.parseInt(temp_data[2]) < 10)
                                temp_data[2] = "0" + temp_data[2]; //일
                            date = temp_data[0] + "-" + temp_data[1] + "-" + temp_data[2];
                            final_arr = new String[]{name, date};
                            insertDB(final_arr);
                        } else {
                            Toast.makeText(getApplicationContext(), "요일이 되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "월이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "년도가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data1);
    }
    //의현

    //달력출력및 DB추가
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear += 1;               //월이 1작아서 1추가
            String day = "" + dayOfMonth;
            String month = "" + monthOfYear;
            if (dayOfMonth < 10) {          //YYYY-MM-DD 형식으로 맞추기위해
                day = "0" + dayOfMonth;
            }
            if (monthOfYear < 10)
                month = "0" + monthOfYear;
            String date = year + "-" + month + "-" + day;
            String[] arr = new String[]{name, date};
            insertDB(arr);
        }
    };

    public void insertDB(String[] arr) {
        DBHelper helper = new DBHelper(CONTEXT);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO FOOD (name, date) VALUES (?, ?)", new String[]{arr[0], arr[1]});
        String sql = "SELECT name, date FROM FOOD ORDER BY date ASC";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();
        Toast.makeText(getApplicationContext(), "음식이 등록되었습니다~", Toast.LENGTH_SHORT).show();
        db.close();
        setGage();
        setMainbut();
    }

    //화면 새로고침

    @Override
    public void onResume() {
        super.onResume();
        setMainbut();
        setGage();
    }

    //MainButton 구성
    public void setMainbut() {
        ListViewItem[] items;
        DBHelper helper = new DBHelper(CONTEXT);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "SELECT name, date FROM FOOD ORDER BY date ASC";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() != 0) {
            items = new ListViewItem[cursor.getCount()];
            for(int i = 0; i<cursor.getCount();i++) {
                cursor.moveToNext();
                String name = cursor.getString(0);
                String date = cursor.getString(1);
                items[i] = new ListViewItem();
                items[i].setName(name);
                items[i].setDate(date);
            }
        }
        else
        {
            items = new ListViewItem[1];
            items[0] = new ListViewItem();
            String name = "음식이 없어요!";
            items[0].setName(name);
            items[0].setDate("");
        }
        db.close();
        pagerAdapter = new MainPagerAdapter(this,items) ;
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageMargin(20);
        indicator.setViewPager(viewPager);
    }

    //Gage구성
    public void setGage() {
        int count = 0; //아이템 개수
        long now = System.currentTimeMillis();
        Date mdate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = simpleDate.format(mdate);
        getTime = getTime.replace("-", "");
        int currentTime = Integer.parseInt(getTime);
        DBHelper helper = new DBHelper(CONTEXT);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "SELECT name, date FROM FOOD ORDER BY date DESC";
        Cursor cursor = db.rawQuery(sql, null);
        //초기화
        for (int i = 0; i < 10; i++) {
            layer[i].setBackgroundColor(0XCCCCCCCC);
            if (i == 0)
                layer[i].setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.nomal_bottom));
            else if (i == 9)
                layer[i].setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.nomal_top));
        }
        //색지정
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            String date = cursor.getString(1);
            date = date.replace("-", "");
            int database = Integer.parseInt(date);
            int limit_time = database - currentTime;
            if (limit_time <= 0 && count < 10) {
                layer[i].setBackgroundColor(0XFFF44336);
                if (count == 0)
                    layer[i].setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.red_bottom));
                else if (count == 9)
                    layer[i].setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.red_top));
                count++;
            } else if (limit_time <= 4 && count < 10) {
                layer[i].setBackgroundColor(0xFFFFEB3B);
                if (count == 0)
                    layer[i].setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.yellow_bottom));
                else if (count == 9)
                    layer[i].setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.yellow_top));
                count++;
            } else if (count < 10) {
                layer[i].setBackgroundColor(0XFF4CAF50);
                if (count == 0)
                    layer[i].setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.green_bottom));
                else if (count == 9)
                    layer[i].setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.green_top));
                count++;
            }
        }
        db.close();
    }

}