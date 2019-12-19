package com.example.andoridproject.Etc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper3 extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public DBHelper3(Context context)
    {
        super(context,"Dday.db",null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE D_DAY (" + "_id integer primary key autoincrement, " + "name, " + "date)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == DATABASE_VERSION){
            db.execSQL("DROP TABLE D_DAY ");
        }
    }
}