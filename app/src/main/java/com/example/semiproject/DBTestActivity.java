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

public class DBTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_test);

        final EditText _dbTest_timeEditText = findViewById(R.id._dbTest_timeEditText);
        Button _dbTestInsertBtn = findViewById(R.id._dbTestInsertBtn);
        Button _dbTestDeleteBtn = findViewById(R.id._dbTestDeleteBtn);
        final Button _dbTestSelectBtn = findViewById(R.id._dbTestSelectBtn);
        final TextView _dbTestTv = findViewById(R.id._dbTestTv);

        final DBHelper helper =
                new DBHelper(DBTestActivity.this, "alarm", 1);
        //helper를 통해서 db refeernce를 획득

        _dbTestInsertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = _dbTest_timeEditText.getText().toString();
                helper.insert(time);

            }
        });

        _dbTestDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.delete();
            }
        });

        _dbTestSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dbTestTv.setText(helper.getResult());
            }
        });
    }
}


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
