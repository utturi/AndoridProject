package com.example.andoridproject.Tab;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.andoridproject.Activity.LoginActivity;
import com.example.andoridproject.Activity.MainActivity;
import com.example.andoridproject.Activity.PostDetailActivity;
import com.example.andoridproject.Etc.AlarmReceiver;
import com.example.andoridproject.Etc.DBHelper2;
import com.example.andoridproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Tab5_Activity extends AppCompatActivity {
    AlertDialog alram;
    AlertDialog developer_view;
    TextView alram_setting;
    TextView developer_but;
    int alarm_check; //알람 on,off판단변수
    Switch sw;
    Tab4_Activity tab4 = new Tab4_Activity();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab5);

        TextView button = findViewById(R.id.logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Tab5_Activity.this);
                dialogBuilder.setTitle("Logout");
                dialogBuilder.setMessage("\n로그아웃 하시겠습니까?")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBHelper2 helper = new DBHelper2(getApplicationContext());
                                SQLiteDatabase db = helper.getWritableDatabase();
                                String sql = "DELETE FROM STARPOST";
                                db.execSQL(sql);
                                FirebaseAuth.getInstance().signOut();
                                startLoginActivity();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                dialogBuilder.show();
            }
        });

        final TextView alramtime = findViewById(R.id.alarm_time);
        sw = findViewById(R.id.alarm_switch);
        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw.isChecked()) {
                    alarm_check = 1;
                    Toast.makeText(getApplicationContext(), "알람이 활성화 되었습니다", Toast.LENGTH_SHORT).show();
                } else {
                    alarm_check = 0;
                    Toast.makeText(getApplicationContext(), "알람이 비활성화 되었습니다", Toast.LENGTH_SHORT).show();
                    Calendar calendar = Calendar.getInstance();
                    diaryNotification(calendar, alarm_check);
                    alramtime.setText(" ");
                }
            }
        });


        developer_but = findViewById(R.id.developer);
        developer_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder developer_builder = new androidx.appcompat.app.AlertDialog.Builder(Tab5_Activity.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View customDialogView = inflater.inflate(R.layout.developer_dialog, null);
                developer_builder.setView(customDialogView);
                developer_builder.setCancelable(true).setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                developer_view = developer_builder.create();
                developer_view.show();
            }
        });

        alram_setting = findViewById(R.id.alram_button);
        alram_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alarm_check == 0) {
                    Toast.makeText(getApplicationContext(), "알람을 활성화 시켜주세요", Toast.LENGTH_SHORT).show();
                } else {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Tab5_Activity.this);
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View customDialogView = inflater.inflate(R.layout.alramtime, null);
                    final TimePicker picker = customDialogView.findViewById(R.id.timePicker);
                    picker.setIs24HourView(true);
                    builder.setTitle("시간 설정");
                    builder.setView(customDialogView);

                    builder.setCancelable(true).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int hour, hour_24, minute;
                            String am_pm;
                            Log.e("num", "2");
                            if (Build.VERSION.SDK_INT >= 23) {
                                hour_24 = picker.getHour();
                                minute = picker.getMinute();
                            } else {
                                hour_24 = picker.getCurrentHour();
                                minute = picker.getCurrentMinute();
                            }
                            if (hour_24 > 12) {
                                am_pm = "PM";
                                hour = hour_24 - 12;
                            } else {
                                hour = hour_24;
                                am_pm = "AM";
                            }

                            // 현재 지정된 시간으로 알람 시간 설정
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            calendar.set(Calendar.HOUR_OF_DAY, hour_24);
                            calendar.set(Calendar.MINUTE, minute);
                            calendar.set(Calendar.SECOND, 0);

                            // 이미 지난 시간을 지정했다면 다음날 같은 시간으로 설정
                            if (calendar.before(Calendar.getInstance())) {
                                Log.e("num", "1");
                                calendar.add(Calendar.DATE, 1);
                            }

                            Date currentDateTime = calendar.getTime();
                            alramtime.setText(new SimpleDateFormat("a h시 m분", Locale.getDefault()).format(currentDateTime));

                            //  Preference에 설정한 값 저장
                            SharedPreferences.Editor editor = getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
                            editor.putLong("nextNotifyTime", (long) calendar.getTimeInMillis());
                            editor.apply();
                            diaryNotification(calendar, alarm_check);
                        }
                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alram = builder.create();
                    alram.show();
                }
            }
        });
    }

    void diaryNotification(Calendar calendar, int alarm_check) {
        Boolean dailyNotify = true; // 무조건 알람을 사용

        PackageManager pm = this.getPackageManager();
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (alarm_check == 1) {
            // 사용자가 매일 알람을 허용했다면
            if (dailyNotify) {
                if (alarmManager != null) {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY, pendingIntent);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    }
                }
            }
            tab4.cal_Dday();
        }
        else {
            alarmManager.cancel(pendingIntent);
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        MainActivity.tabHost.setCurrentTab(0);
    }
}