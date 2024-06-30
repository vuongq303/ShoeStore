package com.example.appbangiayonline.dao;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.appbangiayonline.database.DBHelper;
import com.example.appbangiayonline.model.KhachHang;
import com.example.appbangiayonline.model.NhanVien;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NhanVien_KhachHang_Dao {
    private DBHelper helper;
    private Context context;

    public NhanVien_KhachHang_Dao(Context context) {
        this.context = context;
        helper = new DBHelper(context);
    }

    public ArrayList<NhanVien> getList_NV() {
        ArrayList<NhanVien> list = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM nhanvien ", null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                list.add(new NhanVien(cursor.getInt(0), cursor.getInt(6), cursor.getString(1), cursor.getString(4), cursor.getString(5),cursor.getInt(7),cursor.getString(2)));
            } while (cursor.moveToNext());
        }
        return list;
    }

    public int newKhachHang(String hoTen, String userName, String pass, String sdt, String email, String diachi) {
        SQLiteDatabase sql = helper.getWritableDatabase();
        Cursor cursor = sql.rawQuery("SELECT * FROM khachhang WHERE khachhang.taikhoan='" + userName + "'", null);
        if (cursor.getCount() > 0) {
            return 0;
        } else if (!isEmailValid(email)) {
            Toast.makeText(context, "Email sai định dạng!", Toast.LENGTH_SHORT).show();
            return -10;
        } else if (!isNumberValid(sdt)) {
            Toast.makeText(context, "Số điện thoại sai định dạng!", Toast.LENGTH_SHORT).show();
            return -10;
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("hoten", hoTen);
            contentValues.put("taikhoan", userName);
            contentValues.put("matkhau", pass);
            contentValues.put("sdt", sdt);
            contentValues.put("email", email);
            contentValues.put("diachi", diachi);
            sql.insert("khachhang", null, contentValues);
            sql.close();
            return 1;
        }

    }

    public int newNhanVien(String hoTen, String userName, String pass, String sdt, String email) {
        SQLiteDatabase sql = helper.getWritableDatabase();
        Cursor cursor = sql.rawQuery("SELECT * FROM nhanvien WHERE nhanvien.taikhoan='" + userName + "'", null);
        if (cursor.getCount() > 0) {
            return 0;
        } else if (!isEmailValid(email)) {
            Toast.makeText(context, "Email sai định dạng!", Toast.LENGTH_SHORT).show();
            return -10;
        } else if (!isNumberValid(sdt)) {
            Toast.makeText(context, "Số điện thoại sai định dạng!", Toast.LENGTH_SHORT).show();
            return -10;
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("hoten", hoTen);
            contentValues.put("taikhoan", userName);
            contentValues.put("matkhau", pass);
            contentValues.put("sdt", sdt);
            contentValues.put("email", email);
            contentValues.put("chucvu", 0);
            contentValues.put("trangthai",0);
            sql.insert("nhanvien", null, contentValues);
            sql.close();
            return 1;
        }

    }

    public boolean isEmailValid(String email) {
        String emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isNumberValid(String number) {
        if (number.charAt(0) == '0' && number.length() == 10) {
            return true;
        }
        return false;
    }

    public ArrayList<KhachHang> getList_KH() {
        ArrayList<KhachHang> list = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM khachhang", null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                list.add(new KhachHang(cursor.getInt(0), cursor.getString(1), cursor.getString(4), cursor.getString(5), cursor.getString(6)));
            } while (cursor.moveToNext());
        }
        return list;
    }

    public KhachHang getThongTinKhachHang(String taikhoan) {
        KhachHang khachHang = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT makh,hoten, taikhoan, matkhau, sdt,email, diachi FROM khachhang WHERE taikhoan = ?", new String[]{taikhoan});
            // while (cursor.moveToNext()) {
            //1 tai khoan khong can movetonext
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                khachHang = new KhachHang(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
            }
            //  }
            cursor.close();
        } catch (Exception e) {
            Log.i(TAG, "loi", e);
        }
        return khachHang;
    }

    public NhanVien getThongTinNhanVien(String taikhoan) {
        NhanVien nhanVien = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("select manv, hoten, taikhoan, matkhau, sdt, email, chucvu,trangthai from nhanvien where taikhoan = ?", new String[]{taikhoan});
            while (cursor.moveToNext()) {
                nhanVien = new NhanVien(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6),cursor.getInt(7));
            }
            cursor.close();
        } catch (Exception e) {
            Log.i(TAG, "loi", e);
        }
        return nhanVien;
    }

    public void capNhatThongTin(int id, String hoten, String sdt, String email, String diachi) {
        SQLiteDatabase sql = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("hoten", hoten);
        contentValues.put("sdt", sdt);
        contentValues.put("email", email);
        contentValues.put("diachi", diachi);
        sql.update("khachhang", contentValues, "makh=?", new String[]{String.valueOf(id)});
        sql.close();
    }

    public void xoaTaiKhoan(int id) {
        SQLiteDatabase sql = helper.getWritableDatabase();
        sql.delete("khachhang", "makh=?", new String[]{String.valueOf(id)});
        sql.close();
    }

    public void ChangePassWord_KH(String taiKhoan,String newPass){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("matkhau",newPass);
        db.update("khachhang",values,"taikhoan = ? ",new String[]{taiKhoan});
    }
    public void ChangePassWord_NV(String taiKhoan,String newPass){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("matkhau",newPass);
        db.update("nhanvien",values,"taikhoan = ? ",new String[]{taiKhoan});
    }
    public boolean xoaNhanVien(int id,int trangthai){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues  values = new ContentValues();
        values.put("trangthai",trangthai);
        long check = db.update("nhanvien",values,"manv=?", new String[]{String.valueOf(id)});
        return check>0;
    }
    public boolean updateNV(int id, String hoten, String sdt, String email, int chucvu){
        if (!isEmailValid(email)) {
            Toast.makeText(context, "Email sai định dạng!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isNumberValid(sdt)) {
            Toast.makeText(context, "Số điện thoại sai định dạng!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("hoten", hoten);
            values.put("sdt", sdt);
            values.put("email", email);
            values.put("chucvu", chucvu);
            db.update("nhanvien", values, "manv=?", new String[]{String.valueOf(id)});
            db.close();
            return true;
        }
    }
}

