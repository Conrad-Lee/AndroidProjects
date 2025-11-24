package com.bytedance.firstDemo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "users.db";
    public static final int DB_VERSION = 2; // 升级版本

    public static final String TABLE_NAME = "users";
    public static final String COL_ID = "id";
    public static final String COL_ACCOUNT = "account";
    public static final String COL_PASSWORD = "password";
    public static final String COL_NICKNAME = "nickname";
    public static final String COL_AVATAR = "avatar";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_ACCOUNT + " TEXT UNIQUE, " +
                    COL_PASSWORD + " TEXT, " +
                    COL_NICKNAME + " TEXT, " +
                    COL_AVATAR + " TEXT)";

    public UserDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("startSQL", "start");
        Log.d("dbVersion", String.valueOf(DB_VERSION));
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("oldVersion", String.valueOf(oldVersion));
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COL_AVATAR + " TEXT;");
        }
    }
}

