package com.example.appbangiayonline.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appbangiayonline.database.DBHelper;

public class ThongKeDTDao {
    private final DBHelper dbHelper;

    public ThongKeDTDao(Context context) {
        dbHelper = new DBHelper(context);    }

    public int getTien(String Start, String End){
        Start = Start.replace("/","");
        End = End.replace("/", "");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select sum(tongtien) from hoadon where substr(ngay,7) || substr(ngay, 4, 2) || substr(ngay, 1, 2) between ? and ?", new String[]{Start, End});
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        return 0;
    }
}
