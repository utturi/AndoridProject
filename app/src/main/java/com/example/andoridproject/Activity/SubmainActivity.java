package com.example.andoridproject.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.andoridproject.Adapter.ListViewAdapter;
import com.example.andoridproject.Etc.DBHelper;
import com.example.andoridproject.Item.ListViewItem;
import com.example.andoridproject.R;

import java.util.ArrayList;

public class SubmainActivity extends AppCompatActivity {
    ArrayList<ListViewItem> items = new ArrayList<ListViewItem>();
    boolean mclick = false;
    boolean check_state = false; //전체선택 버튼 상태를 나타내는 변수
    Button execute_delete;
    Button all_check;
    SwipeMenuListView listView;
    int delete_point;           //position을 리스너에서 사용하기위한 전역변수
    Context context;
    DatePickerDialog dateDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_main);
        context = this;
        TextView textView = findViewById(R.id.textview);
        listView = findViewById(R.id.listview);
        execute_delete = (Button) findViewById(R.id.execute_delete);
        all_check = (Button) findViewById(R.id.execute_allcheck);
        AnimationSet set = new AnimationSet(true);

        Animation rtl = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0);
        rtl.setDuration(400);
        set.addAnimation(rtl);

        Animation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(400);
        set.addAnimation(alpha);

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
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
            ListViewItem item = new ListViewItem(name, date);
            items.add(item);
        }
        textView.setText("저장된 목록  " + cursor.getCount() + "개");
        db.close();
        final ListViewAdapter listViewAdapter = new ListViewAdapter(items);
        listView.setAdapter(listViewAdapter);
        listView.setMenuCreator(creator);
        listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
                listView.smoothOpenMenu(position);
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
                listView.smoothOpenMenu(position);
            }
        });

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                delete_point = position;
                if(index == 1)
                {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SubmainActivity.this);
                    dialogBuilder.setTitle("Delete");

                    dialogBuilder.setMessage("\n음식을 정말 삭제하시겠습니까??")
                            .setCancelable(false)
                            .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DBHelper helper = new DBHelper(getApplicationContext());
                                    SQLiteDatabase db = helper.getWritableDatabase();
                                    ListViewItem item = (ListViewItem) listViewAdapter.getItem(delete_point);
                                    String sql = "DELETE FROM FOOD WHERE name = '" + item.getName() + "' AND date = '" + item.getDate() + "';";
                                    db.execSQL(sql);
                                    //((Tab1_Activity) Tab1_Activity.CONTEXT).onResume();
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    dialogBuilder.show();
                }
                else
                {
                    dateDialog = new DatePickerDialog(SubmainActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            month += 1;               //월이 1작아서 1추가
                            String day = "" + dayOfMonth;
                            String temp_month = "" + month;
                            if (dayOfMonth < 10) {          //YYYY-MM-DD 형식으로 맞추기위해
                                day = "0" + dayOfMonth;
                            }
                            if (month < 10)
                                temp_month = "0" + month;
                            String date = year+"-"+temp_month+"-"+day;
                            DBHelper helper = new DBHelper(getApplicationContext());
                            SQLiteDatabase db = helper.getWritableDatabase();
                            ListViewItem item = (ListViewItem) listViewAdapter.getItem(delete_point);
                            String sql = "UPDATE FOOD SET date = '"+date+"' WHERE name = '" + item.getName() + "' AND date = '" + item.getDate() + "';";
                            db.execSQL(sql);
                            //((Tab1_Activity) Tab1_Activity.CONTEXT).onResume();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    },2019,11,8);
                    dateDialog.show();
                }
                return false;
            }
        });



        ImageButton imagebutton = findViewById(R.id.deleteButton);
        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mclick == false) {
                    mclick = true;
                    execute_delete.setVisibility(View.VISIBLE);
                    all_check.setVisibility(View.VISIBLE);
                    listViewAdapter.toggleCheckBox(mclick);
                } else {
                    mclick = false;
                    execute_delete.setVisibility(View.INVISIBLE);
                    all_check.setVisibility(View.INVISIBLE);
                    listViewAdapter.toggleCheckBox(mclick);
                }
            }
        });

        execute_delete.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                int count = listViewAdapter.getCount();
                int delete_count = 0;
                ListViewItem item = new ListViewItem();
                DBHelper helper = new DBHelper(getApplicationContext());
                SQLiteDatabase db = helper.getWritableDatabase();
                for (int i = 0; i < count; i++) {
                    item = (ListViewItem) listViewAdapter.getItem(i);
                    if (item.getSelected()) {
                        String sql = "DELETE FROM FOOD WHERE name = '" + item.getName() + "' AND date = '" + item.getDate() + "';";
                        db.execSQL(sql);
                        delete_count++;
                    }
                }
                db.close();
                if (delete_count == 0)
                    Toast.makeText(getApplicationContext(), "삭제할 목록이 없어요..", Toast.LENGTH_SHORT).show();
                else {
                    //((Tab1_Activity) Tab1_Activity.CONTEXT).onResume();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        });

        all_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(check_state))//전체체크가 안됐으면
                {
                    listViewAdapter.allCheckBox(false);
                    check_state = true;
                } else {
                    listViewAdapter.allCheckBox(true);
                    check_state = false;
                }
            }
        });

    }

    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "Close" item
            SwipeMenuItem openItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            openItem.setBackground(R.drawable.edit_button);
            // set item width
            openItem.setWidth(200);
            // set item title
            openItem.setTitle("Edit");
            // set item title fontsize
            openItem.setTitleSize(18);
            // set item title font color
            openItem.setTitleColor(Color.WHITE);
            // add to menu
            menu.addMenuItem(openItem);


            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            deleteItem.setBackground(R.drawable.red_button);
            // set item width
            deleteItem.setWidth(200);
            // set item title
            deleteItem.setTitle("Delete");
            // set item title fontsize
            deleteItem.setTitleSize(18);
            // set item title font color
            deleteItem.setTitleColor(Color.WHITE);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };


}


