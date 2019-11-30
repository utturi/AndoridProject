package com.example.andoridproject;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class Tab1_Activity extends AppCompatActivity {
    DatePickerDialog mDialog;   //달력사용
    String TAG = "FoodName";    //직접등록 다이얼로그
    String name;                //음식이름
    ImageButton youtube_but;    //유튜브 버튼
    ImageButton layer[];        //gage배열

    private ViewPager viewPager;           //뷰페이저
    private MainPagerAdapter pagerAdapter; //어댑터
    private CircleIndicator indicator;      //인디케이터
    private ArrayList<Board> items;
    private Tab1BoardAdapter adapter;
    private ListView listView;
    //대호
    public static Context CONTEXT;
    private BackPressCloseHandler backPressCloseHandler; // 2번 뒤로가기 종료

    //의현
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private SpeechRecognizer speechRecog;
    ImageButton customDialogBtn;
    AlertDialog customDialog;
    ImageView but;

    FloatingActionButton fab_plus,fab_sound,fab_direct;
    Animation FabOpen,FabClose,FabRClockwise,FabRanticlockwise;
    boolean isOpen = false;
    //의현

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab1);

        backPressCloseHandler = new BackPressCloseHandler(this);

        //의현
        fab_plus = (FloatingActionButton)findViewById(R.id.fab_plus);
        fab_sound = (FloatingActionButton)findViewById(R.id.fab_sound); //음성등록
        fab_direct = (FloatingActionButton)findViewById(R.id.fab_direct); //직접등록
        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabRClockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        FabRanticlockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);
        fab_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen)
                {
                    fab_direct.startAnimation(FabClose);
                    fab_sound.startAnimation(FabClose);
                    fab_plus.startAnimation(FabRanticlockwise);
                    fab_sound.setClickable(false);
                    fab_direct.setClickable(false);
                    isOpen = false;
                }
                else
                {
                    fab_direct.startAnimation(FabOpen);
                    fab_sound.startAnimation(FabOpen);
                    fab_plus.startAnimation(FabRClockwise);
                    fab_sound.setClickable(true);
                    fab_direct.setClickable(true);
                    isOpen = true;
                }
            }
        });
        //의현

        CONTEXT = this;
        listView=findViewById(R.id.tab1_listview);
        //setList();
        /*
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

         */

        //ViewPager
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        viewPager = (ViewPager) findViewById(R.id.mainbutton) ;
        ImageButton eat_button = findViewById(R.id.eatbut);
        setMainbut();
        //setGage();
        eat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper helper = new DBHelper(CONTEXT);
                SQLiteDatabase db = helper.getWritableDatabase();
                String sql = "SELECT name, date FROM FOOD ORDER BY date ASC";
                Cursor cursor = db.rawQuery(sql, null);
                if (cursor.getCount() != 0) {
                    cursor.moveToNext();
                    for(int i = 0; i < viewPager.getCurrentItem();i++) {
                        cursor.moveToNext();
                    }
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

        // Youtube 버튼
        youtube_but = findViewById(R.id.youtubebut);
        youtube_but.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ListViewItem[] items;
                DBHelper helper = new DBHelper(CONTEXT);
                SQLiteDatabase db = helper.getWritableDatabase();
                String sql = "SELECT name, date FROM FOOD ORDER BY date ASC";
                Cursor cursor = db.rawQuery(sql, null);
                if (cursor.getCount() != 0) {
                    cursor.moveToNext();
                    // 뷰페이저가 바뀔 때 마다 count
                    for(int i = 0; i < viewPager.getCurrentItem(); i++)
                        cursor.moveToNext();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=" +
                            cursor.getString(0) + " 레시피"));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/"));
                    startActivity(intent);
                }
                db.close();
            }
        });

        //의현
        //gv = findViewById(R.id.soundbutton);
        customDialogBtn = findViewById(R.id.fab_sound);
        customDialogBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Tab1_Activity.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View customDialogView = inflater.inflate(R.layout.dialog, null);
                but = customDialogView.findViewById(R.id.but);
                GlideDrawableImageViewTarget gif = new GlideDrawableImageViewTarget(but);
                Glide.with(Tab1_Activity.this).load(R.drawable.soundview).into(gif);
                builder.setView(customDialogView);

                customDialog = builder.create();
                customDialog.show();


                //음성인식
                if (ContextCompat.checkSelfPermission(Tab1_Activity.this,
                        Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(Tab1_Activity.this,
                            Manifest.permission.RECORD_AUDIO)) {
                    } else {
                        ActivityCompat.requestPermissions(Tab1_Activity.this,
                                new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
                    }
                } else {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
                    intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                    speechRecog.startListening(intent);
                }
            }
        });
        initializeSpeechRecognizer();
        //의현

        //직접등록버튼 입력 -> 달력(유통기한설정)
        fab_direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(Tab1_Activity.this);
                ad.setTitle("음식입력"); //제목

                final EditText et = new EditText(Tab1_Activity.this);
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
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar, menu) ;
        return true ;
    }*/
    public void setList()
    {
       DatabaseReference database = FirebaseDatabase.getInstance().getReference("posts");
       items = new ArrayList<>();
       items.clear();
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot messageData : dataSnapshot.getChildren())
                {
                    String key = messageData.getKey();
                    Board data = messageData.getValue(Board.class);
                    data.setKey(key);
                    items.add(data);
                }
                adapter = new Tab1BoardAdapter(items,getApplicationContext());
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //의현
    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecog = SpeechRecognizer.createSpeechRecognizer(this);
            speechRecog.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) { }

                @Override
                public void onBeginningOfSpeech() {
                    Toast.makeText(Tab1_Activity.this, "입력 중...", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRmsChanged(float rmsdB) {
                }

                @Override
                public void onBufferReceived(byte[] buffer) {
                    Toast.makeText(Tab1_Activity.this, "입력 완료", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onEndOfSpeech() {
                    customDialog.dismiss();
                }

                @Override
                public void onError(int error) {
                }

                @Override
                public void onResults(Bundle results) {
                    List<String> result_arr = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    String[] arry = result_arr.toArray(new String[result_arr.size()]);
                    //Toast.makeText(getApplicationContext(), arry[0], Toast.LENGTH_SHORT).show();

                    String text = arry[0].replaceAll(" ", "");
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
                    //Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(getApplicationContext(), "요일이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                                    customDialog.dismiss();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "월이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                                customDialog.dismiss();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "년도가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                            customDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onPartialResults(Bundle partialResults) { }

                @Override
                public void onEvent(int eventType, Bundle params) { }
            });
        }
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
        //setGage();
        setMainbut();
    }

    // 화면 새로고침
    @Override
    public void onResume() {
        super.onResume();
        setMainbut();
        setList();
        //setGage();
    }

    // MainButton 구성
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

    // Gage구성
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
}
