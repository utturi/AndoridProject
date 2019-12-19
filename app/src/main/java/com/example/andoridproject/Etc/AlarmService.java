package com.example.andoridproject.Etc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.andoridproject.Tab.Tab5_Activity;

import java.util.Calendar;

public class AlarmService extends Service {
    private int check_num = 0;
    private Calendar calendar = null;
    @Override
    public void onCreate() {
        super.onCreate();
        check_num = 1;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if(intent.getIntExtra("start",1) == 1) {            //시간을 설정했을때
            calendar = (Calendar) intent.getSerializableExtra("Calendar");
            ((Tab5_Activity) Tab5_Activity.context).diaryNotification(calendar, 1);
        }
        if(intent.getIntExtra("start",1)==3)
        {
            if(calendar!=null)
                ((Tab5_Activity) Tab5_Activity.context).setText(calendar);
        }
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        Log.i("superdroid", "onDestroy()");
        check_num = 0;
        ((Tab5_Activity)Tab5_Activity.context).diaryNotification(calendar,0);
        super.onDestroy();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
