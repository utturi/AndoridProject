package com.example.andoridproject.Etc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper2 extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public DBHelper2(Context context)
    {
        super(context,"STAR.db",null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE STARPOST (" + "_id integer primary key autoincrement, " + "postID)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == DATABASE_VERSION){
            db.execSQL("DROP TABLE STARPOST");
        }
    }
}
