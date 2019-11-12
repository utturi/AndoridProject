package com.example.andoridproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SubmainActivity extends AppCompatActivity {
    ArrayList<ListViewItem> items = new ArrayList<ListViewItem>();
    boolean mclick = false;
    Button button;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_main);
        TextView textView = findViewById(R.id.textview);
        listView = findViewById(R.id.listview);
        button = (Button)findViewById(R.id.execute_delete);
        AnimationSet set = new AnimationSet(true);

        Animation rtl = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0);
        rtl.setDuration(400);
        set.addAnimation(rtl);

        Animation alpha = new AlphaAnimation(0,1);
        alpha.setDuration(400);
        set.addAnimation(alpha);

        LayoutAnimationController controller = new LayoutAnimationController(set,0.5f);
        listView.setLayoutAnimation(controller);

        //DB에서 데이터 추출
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT name, date FROM FOOD ORDER BY date ASC";
        Cursor cursor = db.rawQuery(sql, null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            String name = cursor.getString(0);
            String date = cursor.getString(1);
            ListViewItem item = new ListViewItem(name,date);
            items.add(item);
        }
        textView.setText("저장된 목록  "+ cursor.getCount()+"개");
        db.close();
        final ListViewAdapter listViewAdapter = new ListViewAdapter(items);
        listView.setAdapter(listViewAdapter);
        ImageButton imagebutton = findViewById(R.id.deleteButton);
        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mclick==false) {
                    mclick =true;
                    button.setVisibility(View.VISIBLE);
                    listViewAdapter.toggleCheckBox(mclick);
                }
                else
                {
                    mclick= false;
                    button.setVisibility(View.INVISIBLE);
                    listViewAdapter.toggleCheckBox(mclick);
                }
            }
        });

        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                int count = listViewAdapter.getCount();
                int delete_count = 0;
                ListViewItem item = new ListViewItem();
                DBHelper helper = new DBHelper(getApplicationContext());
                SQLiteDatabase db = helper.getWritableDatabase();
                for (int i = 0; i < count; i++) {
                        item = (ListViewItem) listViewAdapter.getItem(i);
                        if(item.getSelected())
                        {
                            String sql = "DELETE FROM FOOD WHERE name = '" + item.getName() + "' AND date = '" + item.getDate() + "';";
                            db.execSQL(sql);
                            delete_count++;
                        }
                }
                db.close();
                if(delete_count == 0)
                    Toast.makeText(getApplicationContext(),"삭제할 목록이 없어요..",Toast.LENGTH_SHORT).show();
                else {
                    ((MainActivity) MainActivity.CONTEXT).onResume();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        }) ;

    }

}

