package com.example.andoridproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
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
                ListViewItem item = new ListViewItem();
                for (int i = 0; i < count; i++) {
                        item = (ListViewItem) listViewAdapter.getItem(i);
                        if(item.getSelected())
                        {
                            DBHelper helper = new DBHelper(getApplicationContext());
                            SQLiteDatabase db = helper.getWritableDatabase();
                            String sql = "DELETE FROM FOOD WHERE name = '" + item.getName() + "';";
                            db.execSQL(sql);
                        }
                }
                ((MainActivity)MainActivity.CONTEXT).onResume();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }) ;

    }

}

