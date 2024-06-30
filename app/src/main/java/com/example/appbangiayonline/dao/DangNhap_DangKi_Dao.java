package com.example.appbangiayonline.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.appbangiayonline.check_information.CheckInformation;
import com.example.appbangiayonline.database.DBHelper;
import com.example.appbangiayonline.model.KhachHang;
import com.example.appbangiayonline.model.NhanVien;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kotlin.contracts.Returns;

public class DangNhap_DangKi_Dao {
    DBHelper dbHelper;
    Context context;

    public DangNhap_DangKi_Dao(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    public int dang_nhap(String taikhoan, String matkhau) {
        ArrayList<KhachHang> list_kh = new ArrayList<>();
        ArrayList<NhanVien> list_nv = new ArrayList<>();

        SQLiteDatabase sql = dbHelper.getReadableDatabase();
        Cursor cursor_nv = sql.rawQuery("SELECT * FROM nhanvien WHERE nhanvien.taikhoan='" + taikhoan + "' AND nhanvien.matkhau='" + matkhau + "'", null);
        Cursor cursor_kh = sql.rawQuery("SELECT * FROM khachhang WHERE khachhang.taikhoan='" + taikhoan + "' AND khachhang.matkhau='" + matkhau + "'", null);
        if (cursor_nv.getCount() > 0) {
            cursor_nv.moveToFirst();
            list_nv.add(new NhanVien(cursor_nv.getInt(0), cursor_nv.getString(1), cursor_nv.getString(2), cursor_nv.getString(3), cursor_nv.getString(4), cursor_nv.getString(5), cursor_nv.getInt(6),cursor_nv.getInt(7)));
            if(list_nv.get(0).getTrangthai()!=0){
                return -100;
            }
            //1 admin
            //0 nhan vien
            if (list_nv.get(0).getChucvu() == 1) {
                return 1;
            } else {
                return 0;
            }
        }
        if (cursor_kh.getCount() > 0) {
            cursor_kh.moveToFirst();
            list_kh.add(new KhachHang(cursor_kh.getInt(0), cursor_kh.getString(1), cursor_kh.getString(2), cursor_kh.getString(3), cursor_kh.getString(4), cursor_kh.getString(5), cursor_kh.getString(6)));

            SharedPreferences.Editor editor = context.getSharedPreferences("khachhang", Context.MODE_PRIVATE).edit();
            editor.putInt("id_kh", list_kh.get(0).getMakh());
            editor.putString("name_kh", list_kh.get(0).getHoten());
            editor.apply();
            return 2;
        }
        return -1;
    }

    public int dang_ki(String hoten, String taikhoan, String matkhau, String email, String sdt) {
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        Cursor cursor = sql.rawQuery("SELECT * FROM khachhang WHERE khachhang.taikhoan='" + taikhoan + "'", null);
        Cursor cursor1 = sql.rawQuery("SELECT * FROM nhanvien WHERE nhanvien.taikhoan='" + taikhoan + "'", null);
        if (cursor.getCount() > 0||cursor1.getCount()>0) {
            return 0;
        } else if (!CheckInformation.isEmailValid(email)) {
            Toast.makeText(context, "Email sai định dạng!", Toast.LENGTH_SHORT).show();
        } else if (!CheckInformation.isNumberValid(sdt)) {
            Toast.makeText(context, "Số điện thoại sai định dạng!", Toast.LENGTH_SHORT).show();
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("hoten", hoten);
            contentValues.put("taikhoan", taikhoan);
            contentValues.put("matkhau", matkhau);
            contentValues.put("sdt", sdt);
            contentValues.put("email", email);
            sql.insert("khachhang", null, contentValues);
            sql.close();
            return 1;
        }
        return -10;
    }

}
