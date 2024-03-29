package com.example.andoridproject.Etc;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.example.andoridproject.Activity.SplashActivity;
import com.example.andoridproject.R;
import com.example.andoridproject.Tab.Tab1_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import static android.content.Context.MODE_PRIVATE;

public class AlarmReceiver extends BroadcastReceiver {
    public Context CONTEXT = Tab1_Activity.CONTEXT;
    public String food;
    int count = 0;
    String arr[];

    @Override
    public void onReceive(final Context context, Intent intent) {
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, SplashActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingI = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");

        //OREO API 26 이상에서는 채널 필요
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            builder.setSmallIcon(R.mipmap.ic_launcher); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남\

            String channelName = "매일 알람 채널";
            String description = "매일 정해진 시간에 알람합니다.";
            int importance = NotificationManager.IMPORTANCE_HIGH; //소리와 알림메시지를 같이 보여줌

            NotificationChannel channel = new NotificationChannel("default", channelName, importance);
            channel.setDescription(description);

            if (notificationManager != null) {
                // 노티피케이션 채널을 시스템에 등록
                notificationManager.createNotificationChannel(channel);
            }
        } else
            builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("FoodLimits").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String data = messageData.getValue(String.class);
                    String[] result = data.split("#");
                    String date = result[1];
                    food = result[0];
                    String[] calcDate = date.split("-");
                    int Dday_result = Dday.caldate(Integer.parseInt(calcDate[0]), Integer.parseInt(calcDate[1]), Integer.parseInt(calcDate[2]));
                    if (Dday_result == -1)
                        count++;

                    if (count == 1) {
                        // 알림바 띄우는 거
                        builder.setAutoCancel(true)
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setTicker("유통기한 알림")
                                .setContentTitle("\'" + food + "\'" + " 유통기한 알림")
                                .setContentText("유통기한 만료까지 하루 남았습니다. 확인하러 갈까요?")
                                .setContentInfo("INFO")
                                .setContentIntent(pendingI);
                    } else if(count > 1) {
                        builder.setAutoCancel(true)
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setTicker("유통기한 알림")
                                .setContentTitle("\'" + food + "\'외 " + (count - 1) + "개 유통기한 알림")
                                .setContentText("유통기한 만료까지 하루 남았습니다. 확인하러 갈까요?")
                                .setContentInfo("INFO")
                                .setContentIntent(pendingI);
                    }

                    // DB에서 D-day를 찾아줌
                    if (notificationManager != null) {
                        // D-day 하루 남았을 때 알람 띄우기
                        if (Dday_result == -1) {
                            // 노티피케이션 동작시킴
                            notificationManager.notify(1234, builder.build());

                            Calendar nextNotifyTime = Calendar.getInstance();

                            // 내일 같은 시간으로 알람시간 결정
                            nextNotifyTime.add(Calendar.DATE, 1);

                            //  Preference에 설정한 값 저장
                            SharedPreferences.Editor editor = context.getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
                            editor.putLong("nextNotifyTime", nextNotifyTime.getTimeInMillis());
                            editor.apply();

                            Date currentDateTime = nextNotifyTime.getTime();
                            String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(currentDateTime);
                            Toast.makeText(context.getApplicationContext(), "다음 알람은 " + date_text + "으로 알람이 설정 되었습니다", Toast.LENGTH_SHORT).show();
                        }
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

}