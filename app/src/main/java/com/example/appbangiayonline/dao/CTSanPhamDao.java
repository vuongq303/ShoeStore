package com.example.appbangiayonline.dao;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.appbangiayonline.database.DBHelper;
import com.example.appbangiayonline.model.CTSanPham;

import java.util.ArrayList;

public class CTSanPhamDao {
    private final DBHelper dbHelper;

    public CTSanPhamDao(Context context) {
        dbHelper = new DBHelper(context);
    }
    public ArrayList<CTSanPham> getDSCTSP(String tensp){
        ArrayList<CTSanPham> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //int mactsanpham, String tensanpham, String tenmausac, int kichco, int gia, int soluong
        Cursor cursor = db.rawQuery("select ctsp.mactsanpham, sp.tensanpham, ctsp.mausac, ctsp.kichco, ctsp.gia, ctsp.soluong from sanpham sp, ctsanpham ctsp where ctsp.masanpham = sp.masanpham and sp.tensanpham = ?",new String[]{tensp});
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            do {
                list.add(new CTSanPham(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3),cursor.getInt(4),cursor.getInt(5)));
            }while (cursor.moveToNext());
        }
        return list;
    }

    public ArrayList<CTSanPham> getListCTSanPham(String tensanpham) {
        ArrayList<CTSanPham> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select ctsp.mactsanpham, sp.tensanpham, ctsp.mausac, ctsp.kichco, ctsp.gia, ctsp.soluong from sanpham sp, ctsanpham ctsp where ctsp.masanpham = sp.masanpham and sp.tensanpham = ?", new String[]{tensanpham});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (cursor.moveToNext()) {
                    list.add(new CTSanPham(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5)));
                }
            }
            cursor.close();
        } catch (Exception e) {
            Log.i(TAG, "loi", e);
        }
        return list;
    }

    public ArrayList<CTSanPham> getListDSMauSize(String tensanpham) {
        ArrayList<CTSanPham> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select sp.tensanpham, ctsp.mausac, ctsp.kichco, ctsp.gia, ctsp.soluong from sanpham sp, ctsanpham ctsp where sp.masanpham = ctsp.masanpham and sp.tensanpham = ?", new String[]{tensanpham});
            while (cursor.moveToNext()) {
                list.add(new CTSanPham(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4)));
            }
            cursor.close();
        } catch (Exception e) {
            Log.i(TAG, "loi", e);
        }
        return list;
    }
    public boolean addCTSP(int masp, String mausac,int size,int gia,int soluong){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor1 = db.rawQuery("select * from ctsanpham where masanpham = ? and mausac = ? and kichco = ?",new String[]{String.valueOf(masp),mausac,String.valueOf(size)});
        if(cursor1.getCount()>0){
            return false;
        }else {
            ContentValues values = new ContentValues();
            values.put("masanpham", masp);
            values.put("mausac", mausac);
            values.put("gia", gia);
            values.put("soluong", soluong);
            values.put("kichco", size);
            long kt = db.insert("ctsanpham", null, values);
            return kt>0;
        }
    }
    public CTSanPham getItemCTSanPham(String mausac, int kichco) {
        CTSanPham ctSanPham = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select ctsp.mactsanpham, sp.tensanpham, ctsp.mausac, ctsp.kichco, ctsp.gia, ctsp.soluong from sanpham sp, ctsanpham ctsp where sp.masanpham = ctsp.masanpham and mausac = ? AND kichco = ?", new String[]{mausac, String.valueOf(kichco)});
            while (cursor.moveToNext()) {
                ctSanPham = new CTSanPham(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5));
            }
        } catch (Exception e) {
            Log.i(TAG, "loi", e);
        }
        return ctSanPham;
    }
    public boolean updateGiaAndSL(int mactsp,int gia,int soluong){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("soluong", soluong);
        values.put("gia",gia);
        long kt = sqLiteDatabase.update("ctsanpham", values, "mactsanpham = ?", new String[]{String.valueOf(mactsp)});
        return (kt > 0);
    }

    //Đây là khi xuất hoadon thì load lại số lượng ctsanpham
    public boolean capNhatSoLuongMoi(int mactsanpham, int soluong) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("soluong", soluong);
        long kt = sqLiteDatabase.update("ctsanpham", values, "mactsanpham =?", new String[]{String.valueOf(mactsanpham)});
        return (kt > 0);
    }
    public int getSL2(int mactsp){
        int sl = 0;
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select soluong from ctsanpham where mactsanpham = ?", new String[]{String.valueOf(mactsp)});
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            sl = cursor.getInt(0);
        }
        return sl;
    }
    //-----------------------
    public CTSanPham getItemCTSanPham_config(String tensp, String mausac, int kichco) {
        CTSanPham ctSanPham = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT ctsanpham.mactsanpham, ctsanpham.masanpham, sanpham.hinhanh, sanpham.tensanpham, ctsanpham.mausac, ctsanpham.kichco, ctsanpham.gia, ctsanpham.soluong " +
                "FROM ctsanpham " +
                "JOIN sanpham ON ctsanpham.masanpham = sanpham.masanpham " +
                "WHERE sanpham.tensanpham = ? AND ctsanpham.mausac = ? AND ctsanpham.kichco = ?";
        String[] selectionArgs = {tensp, mausac, String.valueOf(kichco)};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            ctSanPham = new CTSanPham(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getBlob(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5),
                    cursor.getInt(6),
                    cursor.getInt(7)
            );
        }
        cursor.close();
        return ctSanPham;
    }
    public int getMaCTSP(int masp,String mausac,int kichco) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select mactsanpham from ctsanpham where masanpham = ? and mausac = ? and kichco = ?", new String[]{String.valueOf(masp),mausac, String.valueOf(kichco)});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        return -1;

    }
    //
    public ArrayList<CTSanPham> getList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<CTSanPham> list = new ArrayList<>();
        try{
            Cursor cursor = db.rawQuery("select ctsanpham", null);
            while (cursor.moveToNext()){
                //int mactsanpham, String tensanpham, String tenmausac, int kichco, int gia, int soluong
                list.add(new CTSanPham(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5)));
            }
        }catch (Exception e){
            Log.i(TAG, "loi", e);
        }
        return list;
    }
}
