package com.example.appbangiayonline.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appbangiayonline.database.DBHelper;
import com.example.appbangiayonline.model.CTSanPham;
import com.example.appbangiayonline.model.GioHang;
import com.example.appbangiayonline.model.KhachHang;

import java.util.ArrayList;

public class Giohang_Dao {
    Context context;
    DBHelper helper;
    NhanVien_KhachHang_Dao dao;

    public Giohang_Dao(Context context) {
        this.context = context;
        helper = new DBHelper(context);
        dao = new NhanVien_KhachHang_Dao(context);
    }


    public ArrayList<GioHang> getList() {
        ArrayList<GioHang> list = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " +
                "giohang.magiohang,sanpham.masanpham,khachhang.makh," +
                "sanpham.hinhanh, " +
                "sanpham.tensanpham, " +
                "giohang.mausac,giohang.kichco,giohang.gia,giohang.soluong,giohang.slmua " +
                "from sanpham " +
                "join giohang " +
                "on sanpham.masanpham=giohang.masanpham " +
                "join khachhang " +
                "on khachhang.makh=giohang.makhachhang ", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add(
                        new GioHang(
                                cursor.getInt(0),
                                cursor.getInt(1),
                                cursor.getInt(2),
                                cursor.getBlob(3),
                                cursor.getString(4),
                                cursor.getString(5),
                                cursor.getInt(6),
                                cursor.getInt(7),
                                cursor.getInt(8),
                                cursor.getInt(9)));
            } while (cursor.moveToNext());
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("admin", Context.MODE_PRIVATE);
        if (sharedPreferences.getInt("setting", -1) == 2) {
            String taikhoan = sharedPreferences.getString("taikhoan", "");
            ArrayList<GioHang> gioHangs = new ArrayList<>();
            KhachHang khachHang = dao.getThongTinKhachHang(taikhoan);
            list.forEach(e -> {
                if (e.getMakhachhang() == khachHang.getMakh()) {
                    gioHangs.add(e);
                }
            });
            return gioHangs;
        }
        return list;
    }


    public int getMaCTSP(int magiohang) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select masanpham from giohang where magiohang = ?", new String[]{String.valueOf(magiohang)});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        return -1;

    }

    public void remove_data(int magiohang) {
        SQLiteDatabase sql = helper.getWritableDatabase();
        sql.delete("giohang", "magiohang=?", new String[]{String.valueOf(magiohang)});
        sql.close();
    }

    public void themGioHang(CTSanPham ctSanPham, int makh) {
        SQLiteDatabase sql = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("masanpham", ctSanPham.getMasanpham());
        contentValues.put("makhachhang", makh);
        contentValues.put("hinhanh", ctSanPham.getHinhanh());
        contentValues.put("mausac", ctSanPham.getTenmausac());
        contentValues.put("kichco", ctSanPham.getKichco());
        contentValues.put("gia", ctSanPham.getGia());
        contentValues.put("soluong", ctSanPham.getSoluong());
        contentValues.put("slmua", ctSanPham.getSl_mua());
        sql.insert("giohang", null, contentValues);
        sql.close();
    }
}
