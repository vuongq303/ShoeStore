package com.example.appbangiayonline.dao;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.appbangiayonline.database.DBHelper;
import com.example.appbangiayonline.model.SanPham;

import java.util.ArrayList;

public class SanPhamDao {
    private final DBHelper dbHelper;

    public SanPhamDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    public ArrayList<SanPham> getListSanPham() {
        ArrayList<SanPham> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select * from sanpham", null);
            while (cursor.moveToNext()) {
                list.add(new SanPham(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getBlob(4)));
            }
            cursor.close();
        } catch (Exception e) {
            Log.i(TAG, "loi", e);
        }
        return list;
    }

    public boolean ThemSanPham(SanPham sanPham) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tensanpham", sanPham.getTensanpham());
        values.put("hang", sanPham.getHang());
        values.put("trangthai", "Còn hàng");
         values.put("hinhanh", sanPham.getImage());
        long kt = db.insert("sanpham", null, values);
        return (kt > 0);
    }

    public boolean SuaSanPham(SanPham sanPham) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("masanpham", sanPham.getMasanpham());
        values.put("tensanpham", sanPham.getTensanpham());
        values.put("trangthai", sanPham.getTrangthai());
        long kt = db.update("sanpham", values, "masanpham = ?", new String[]{String.valueOf(sanPham.getMasanpham())});
        return (kt > 0);
    }

}
