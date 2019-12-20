package com.example.andoridproject.Tab;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.andoridproject.Activity.MainActivity;
import com.example.andoridproject.Adapter.AlarmAdapter;
import com.example.andoridproject.Etc.DBHelper;
import com.example.andoridproject.Etc.DBHelper3;
import com.example.andoridproject.Etc.Dday;
import com.example.andoridproject.Etc.RecyclerViewDecoration;
import com.example.andoridproject.Item.Alert;
import com.example.andoridproject.Item.ListViewItem;
import com.example.andoridproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Tab4_Activity extends AppCompatActivity {
    public Context CONTEXT = Tab1_Activity.CONTEXT;
    public static Context context;
    public String food;
    public ArrayList<Alert> data;
    public AlarmAdapter alarmAdapter;
    String[] arr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab4);
        context = this;
        //리사이클러뷰 스와이프 삭제 구현
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                DBHelper3 helper3 = new DBHelper3(CONTEXT);
                SQLiteDatabase db3 = helper3.getWritableDatabase();
                String sql = "DELETE FROM D_DAY WHERE name = '" + data.get(position).title + "' AND date = '" + data.get(position).date + "';";
                db3.execSQL(sql);
                data.remove(position);
                alarmAdapter.notifyItemRemoved(position);
            }
        };
        data = new ArrayList<>();
        alarmAdapter = new AlarmAdapter(this,data);
        RecyclerView recyclerView = findViewById(R.id.listview_array);
        recyclerView.setAdapter(alarmAdapter);
        recyclerView.addItemDecoration(new RecyclerViewDecoration(48));
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        ListViewItem[] item;
        DBHelper3 helper3 = new DBHelper3(CONTEXT);
        SQLiteDatabase db3 = helper3.getWritableDatabase();
        String sql3 = "SELECT name, date FROM D_DAY ORDER BY date DESC";
        Cursor cursor3 = db3.rawQuery(sql3, null);
        if (cursor3.getCount() != 0) {
            item = new ListViewItem[cursor3.getCount()];
            for (int i = 0; i < cursor3.getCount(); i++) {
                cursor3.moveToNext();
                String date = cursor3.getString(1);
                food = cursor3.getString(0);
                item[i] = new ListViewItem();
                item[i].setDate(date);

                Alert vo = new Alert();
                vo.content = ("D-1 ");
                vo.title = cursor3.getString(0);
                vo.date = cursor3.getString(1);
                data.add(vo);
                alarmAdapter.notifyDataSetChanged();
            }
        }
        db3.close();
    }

    public void cal_Dday() {
        ListViewItem[] items;
        DBHelper helper = new DBHelper(CONTEXT);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "SELECT name, date FROM FOOD ORDER BY date ASC";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() != 0) {
            items = new ListViewItem[cursor.getCount()];
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                String date = cursor.getString(1);
                food = cursor.getString(0);
                items[i] = new ListViewItem();
                items[i].setDate(date);
                String[] calcDate = items[i].getDate().split("-");
                int Dday_result = Dday.caldate(Integer.parseInt(calcDate[0]), Integer.parseInt(calcDate[1]), Integer.parseInt(calcDate[2]));
                if (Dday_result == -1) {
                    arr = new String[]{food, date};
                    insertDB(arr);
                    insertDB2(arr);
                }
            }
        }
    }

    public void insertDB(String[] arr) {
        DBHelper3 helper3 = new DBHelper3(CONTEXT);
        SQLiteDatabase db3 = helper3.getWritableDatabase();
        db3.execSQL("INSERT INTO D_DAY (name, date) VALUES (?, ?)", new String[]{arr[0], arr[1]});
        String sql3 = "SELECT name, date FROM D_DAY ORDER BY date ASC";
        Cursor cursor3 = db3.rawQuery(sql3, null);
        cursor3.moveToNext();
        db3.close();
    }

    @Override
    public void onBackPressed() {
        MainActivity.tabHost.setCurrentTab(0);
    }


    public void insertDB2(String[] arr) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("FoodLimits").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        database.push().setValue((arr[0]+"#"+arr[1]));
    }
}