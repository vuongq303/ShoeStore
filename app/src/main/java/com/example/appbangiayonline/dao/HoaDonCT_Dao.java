package com.example.appbangiayonline.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appbangiayonline.database.DBHelper;
import com.example.appbangiayonline.model.CTSanPham;
import com.example.appbangiayonline.model.HoaDon;
import com.example.appbangiayonline.model.HoaDonCT;

import java.util.ArrayList;

public class HoaDonCT_Dao {
    private DBHelper helper;


    public HoaDonCT_Dao(Context context) {
        helper = new DBHelper(context);
    }
    public ArrayList<HoaDonCT> getListHDCT(int mahd){
        ArrayList<HoaDonCT> list= new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM cthoadon WHERE mahd = ?",new String[]{String.valueOf(mahd)});
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            do {
                list.add(new HoaDonCT(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2)));
            }while (cursor.moveToNext());
        }
        return list;
    }

    public ArrayList<CTSanPham> getListSP_CTHD(int maHoaDon){
        ArrayList<CTSanPham> list = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT s.tensanpham, sp.mausac, sp.kichco, sp.gia,cthd.soluongmua FROM cthoadon cthd, ctsanpham sp , sanpham s WHERE cthd.mahd = ? AND cthd.mactsanpham = sp.mactsanpham and s.masanpham = sp.masanpham  ",new String[]{String.valueOf(maHoaDon)});
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            do {
                list.add(new CTSanPham(cursor.getString(0),cursor.getString(1),cursor.getInt(2),cursor.getInt(3),cursor.getInt(4)));
            }while (cursor.moveToNext());
        }
        return list;
    }
    public boolean themCTHD(int maHD,int maCTSP,int soLuongMua){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mahd",maHD);
        values.put("mactsanpham",maCTSP);
        values.put("soluongmua",soLuongMua);
        long kt = db.insert("cthoadon", null, values);
        return kt>0;
    }

}
