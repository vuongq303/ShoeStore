package com.example.appbangiayonline.dao;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.appbangiayonline.database.DBHelper;
import com.example.appbangiayonline.model.HoaDon;
import com.example.appbangiayonline.model.KhachHang;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HoaDonDao {
    private final DBHelper dbHelper;
    Context context;

    public HoaDonDao(Context context) {
        dbHelper = new DBHelper(context);
        this.context = context;
    }

    public ArrayList<HoaDon> getDSHoaDon() {
        ArrayList<HoaDon> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select " +
                    "hd.mahd, " +
                    "hd.makh, " +
                    "nv.hoten, kh.hoten, " +
                    "hd.tongsl, hd.tongtien, " +
                    "hd.ngay, hd.gio, " +
                    "hd.trangthai " +
                    "from hoadon hd, " +
                    "nhanvien nv, khachhang kh " +
                    "where hd.makh = kh.makh and " +
                    "hd.manv = nv.manv ", null);
// public HoaDon(int mahoadon, int makh, String tenkh, String tennv, int tongsl, int tongTien, int trangthai) {
            while (cursor.moveToNext()) {
                list.add(new HoaDon(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(3),
                        cursor.getString(2),
                        cursor.getInt(4),
                        cursor.getInt(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getInt(8)));
            }


            SharedPreferences sharedPreferences = context.getSharedPreferences("admin", Context.MODE_PRIVATE);
            if (sharedPreferences.getInt("setting", 0) == 2) {
                NhanVien_KhachHang_Dao dao = new NhanVien_KhachHang_Dao(context);
                KhachHang khachHang = dao.getThongTinKhachHang(sharedPreferences.getString("taikhoan", ""));
                ArrayList<HoaDon> list1 = new ArrayList<>();
                list.forEach(e -> {
                    if (e.getMakh() == khachHang.getMakh()) {
                        list1.add(e);
                    }
                });
                return list1;
            }
            cursor.close();
        } catch (Exception e) {
            Log.i(TAG, "loi", e);
        }
        return list;
    }

    public int mahd(){
        ArrayList<HoaDon> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select mahd from hoadon",null);
        if (cursor!=null&&cursor.moveToLast()){
            return cursor.getInt(0);
        }
        return -10;
    }

    public boolean addHoaDon(int makh, int tongtien, String ngay, String gio) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("manv", 1);
        values.put("makh", makh);
        values.put("tongtien", tongtien);
        values.put("ngay", ngay);
        values.put("gio", gio);
        values.put("trangthai", 0);
        long kt = db.insert("hoadon", null, values);
        return (kt > 0);
    }

    public boolean thayDoiTrangThaiHoaDon(int mahd, int manv) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("trangthai", 1);
        values.put("manv", manv);
        long row = db.update("hoadon", values, "mahd = ?", new String[]{String.valueOf(mahd)});
        return (row > 0);
    }
}
