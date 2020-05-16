package com.example.semiproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String dbName, int version) {
        super(context, dbName, null, version);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.i("DBTEST", "onOpen 호출");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS alarm" +
                "(_id INTEGER PRIMARY KEY autoincrement, time String)";
        db.execSQL(sql);
        Log.i("DBTEST", "DB를 생성했습니다");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String time) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        String sql = "INSERT INTO alarm(time) VALUES " +
                "("+time+")";
        db.execSQL(sql);
        Log.i("DBTEST", "insert 호출");
        db.close();
    }

    public void update(String item, String price) {
        SQLiteDatabase db = getWritableDatabase();

        db.close();
    }

    public void delete() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM ALARM");
        Log.i("DBTEST", "delete 호출");
        db.close();
    }

    public String getResult() {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        String sql = "SELECT * FROM ALARM";

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String time = cursor.getString(1);
            result += "레코드 : " + id + ", " + time + "\n";
        }
        Log.i("DBTEST", "select 호출");
        return result;
    }


}
