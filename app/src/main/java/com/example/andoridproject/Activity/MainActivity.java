package com.example.andoridproject.Activity;

import android.Manifest;
import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TabHost;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.andoridproject.Adapter.CommentAdapter;
import com.example.andoridproject.Etc.DBHelper2;
import com.example.andoridproject.Item.Comment;
import com.example.andoridproject.R;
import com.example.andoridproject.Tab.Tab1_Activity;
import com.example.andoridproject.Tab.Tab2_Activity;
import com.example.andoridproject.Tab.Tab3_Activity;
import com.example.andoridproject.Tab.Tab4_Activity;
import com.example.andoridproject.Tab.Tab5_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends TabActivity {
    public static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 123;
    public static TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            startLoginActivity();
        setStarDB();
        tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        ImageView tabwidget1 = new ImageView(this);
        tabwidget1.setImageResource(R.drawable.tab1);

        ImageView tabwidget2 = new ImageView(this);
        tabwidget2.setImageResource(R.drawable.tab2);

        ImageView tabwidget3 = new ImageView(this);
        tabwidget3.setImageResource(R.drawable.tab3);

        ImageView tabwidget4 = new ImageView(this);
        tabwidget4.setImageResource(R.drawable.tab4);

        ImageView tabwidget5 = new ImageView(this);
        tabwidget5.setImageResource(R.drawable.tab5);

        intent = new Intent().setClass(this, Tab1_Activity.class);
        spec = tabHost.newTabSpec("Tab Spec 1").setIndicator(tabwidget1).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Tab2_Activity.class);
        spec = tabHost.newTabSpec("Tab Spec 2").setIndicator(tabwidget2).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Tab3_Activity.class);
        spec = tabHost.newTabSpec("Tab Spec 3").setIndicator(tabwidget3).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Tab4_Activity.class);
        spec = tabHost.newTabSpec("Tab Spec 4").setIndicator(tabwidget4).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Tab5_Activity.class);
        spec = tabHost.newTabSpec("Tab Spec 5").setIndicator(tabwidget5).setContent(intent);
        tabHost.addTab(spec);

        for (int tab = 0; tab < tabHost.getTabWidget().getChildCount(); tab++)
            tabHost.getTabWidget().getChildAt(tab).getLayoutParams().height = (int) (50 * (getResources().getDisplayMetrics().density));

        tabHost.setCurrentTab(0);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void setStarDB() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference("stars").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                        String key = messageData.getKey();
                        DBHelper2 helper = new DBHelper2(getApplicationContext());
                        SQLiteDatabase db = helper.getWritableDatabase();
                        db.execSQL("INSERT INTO STARPOST VALUES (NULL,?)", new String[]{key});
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
        }
    }
}