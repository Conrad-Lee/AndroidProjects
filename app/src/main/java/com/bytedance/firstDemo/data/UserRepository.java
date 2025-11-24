package com.bytedance.firstDemo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserRepository {

    private final UserDbHelper dbHelper;

    public UserRepository(Context context) {
        dbHelper = new UserDbHelper(context);
    }

    public void updateUserAvatar(String avatar, Context context) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String account = getCurrentAccount(context);

        ContentValues values = new ContentValues();
        values.put(UserDbHelper.COL_AVATAR, avatar);

        db.update(UserDbHelper.TABLE_NAME,
                values,
                UserDbHelper.COL_ACCOUNT + " = ?",
                new String[]{account});
    }

    public String getUserAvatar(Context context) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String account = getCurrentAccount(context);

        Cursor cursor = db.query(UserDbHelper.TABLE_NAME,
                new String[]{UserDbHelper.COL_AVATAR},
                UserDbHelper.COL_ACCOUNT + " = ?",
                new String[]{account},
                null, null, null);

        if (cursor.moveToFirst()) {
            String avatar = cursor.getString(0);
            cursor.close();
            return avatar == null ? "" : avatar;
        }

        cursor.close();
        return "";
    }


    public String getCurrentAccount(Context context) {
        return context.getSharedPreferences("login_state", Context.MODE_PRIVATE)
                .getString("account", "");
    }
    public String getCurrentUserInfo(Context context) {
        String account = getCurrentAccount(context);
        if (account.isEmpty()) return "未找到账号";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                UserDbHelper.TABLE_NAME,
                null,
                UserDbHelper.COL_ACCOUNT + " = ?",
                new String[]{account},
                null, null, null
        );

        if (!cursor.moveToFirst()) {
            cursor.close();
            return "用户不存在";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(cursor.getInt(cursor.getColumnIndexOrThrow(UserDbHelper.COL_ID))).append("\n");
        sb.append("Account: ").append(cursor.getString(cursor.getColumnIndexOrThrow(UserDbHelper.COL_ACCOUNT))).append("\n");
        sb.append("Password: ").append(cursor.getString(cursor.getColumnIndexOrThrow(UserDbHelper.COL_PASSWORD))).append("\n");
        sb.append("Nickname: ").append(cursor.getString(cursor.getColumnIndexOrThrow(UserDbHelper.COL_NICKNAME))).append("\n");
        sb.append("Avatar: ").append(cursor.getString(cursor.getColumnIndexOrThrow(UserDbHelper.COL_AVATAR))).append("\n");

        cursor.close();
        return sb.toString();
    }


    // 注册用户
    public boolean registerUser(String account, String password, String nickname) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserDbHelper.COL_ACCOUNT, account);
        values.put(UserDbHelper.COL_PASSWORD, password);
        values.put(UserDbHelper.COL_NICKNAME, nickname);

        long result = db.insert(UserDbHelper.TABLE_NAME, null, values);
        return result != -1;
    }

    // 查询用户是否存在
    public boolean userExists(String account) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(UserDbHelper.TABLE_NAME,
                null,
                UserDbHelper.COL_ACCOUNT + " = ?",
                new String[]{account},
                null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }
//测试用查询
    public String[] getLatestUser() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(UserDbHelper.TABLE_NAME,
                new String[]{UserDbHelper.COL_ACCOUNT, UserDbHelper.COL_NICKNAME},
                null, null, null, null,
                UserDbHelper.COL_ID + " DESC", "1");

        String[] result = null;
        if (cursor.moveToFirst()) {
            result = new String[]{
                    cursor.getString(0), // account
                    cursor.getString(1)  // nickname
            };
        }
        cursor.close();
        return result;
    }

    // 验证账号 + 密码
    public boolean validateLogin(String account, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(UserDbHelper.TABLE_NAME,
                null,
                UserDbHelper.COL_ACCOUNT + " = ? AND " + UserDbHelper.COL_PASSWORD + " = ?",
                new String[]{account, password},
                null, null, null);
        boolean valid = cursor.moveToFirst();
        cursor.close();
        return valid;
    }

    public String getNicknameByAccount(String account) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(UserDbHelper.TABLE_NAME,
                new String[]{UserDbHelper.COL_NICKNAME},
                UserDbHelper.COL_ACCOUNT + " = ?",
                new String[]{account}, null, null, null);

        String nickname = "";
        if (cursor.moveToFirst()) {
            nickname = cursor.getString(0);
        }
        cursor.close();
        return nickname;
    }

    public String getAllUsersInfo() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(UserDbHelper.TABLE_NAME,
                null, null, null, null, null, null);

        StringBuilder sb = new StringBuilder();

        while (cursor.moveToNext()) {
            sb.append("ID: ").append(cursor.getInt(cursor.getColumnIndexOrThrow(UserDbHelper.COL_ID))).append("\n");
            sb.append("Account: ").append(cursor.getString(cursor.getColumnIndexOrThrow(UserDbHelper.COL_ACCOUNT))).append("\n");
            sb.append("Password: ").append(cursor.getString(cursor.getColumnIndexOrThrow(UserDbHelper.COL_PASSWORD))).append("\n");
            sb.append("Nickname: ").append(cursor.getString(cursor.getColumnIndexOrThrow(UserDbHelper.COL_NICKNAME))).append("\n");
            sb.append("Avatar: ").append(cursor.getString(cursor.getColumnIndexOrThrow(UserDbHelper.COL_AVATAR))).append("\n");
            sb.append("------------------------\n");
        }
        cursor.close();
        return sb.length() > 0 ? sb.toString() : "数据库为空";
    }

}
