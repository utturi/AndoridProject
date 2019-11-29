package com.example.andoridproject;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TabHost;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends TabActivity {
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
            startLoginActivity();

        backPressCloseHandler = new BackPressCloseHandler(this);
//        tabHost = (TabHost)findViewById(R.id.tabhost);
//        tabHost.setup(getLocalActivityManager());
        TabHost tabHost = getTabHost();
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

        for(int tab = 0; tab < tabHost.getTabWidget().getChildCount(); tab++)
            tabHost.getTabWidget().getChildAt(tab).getLayoutParams().height = (int) (50*(getResources( ).getDisplayMetrics( ).density));

        tabHost.setCurrentTab(0);
    }

    @Override public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
    private void startLoginActivity()
    {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
