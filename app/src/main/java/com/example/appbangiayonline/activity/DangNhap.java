package com.example.appbangiayonline.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbangiayonline.R;
import com.example.appbangiayonline.dao.DangNhap_DangKi_Dao;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

public class DangNhap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        Button btn_login = findViewById(R.id.btn_dangnhap_dangnhap);
        TextView txt_signup = findViewById(R.id.txt_dangki_dangnhap);
        DangNhap_DangKi_Dao dao = new DangNhap_DangKi_Dao(this);

        TextInputEditText input_taikhoan = findViewById(R.id.input_taikhoan_dangnhap);
        TextInputEditText input_matkhau = findViewById(R.id.input_matkhau_dangnhap);

        btn_login.setOnClickListener(view -> {

            String taikhoan = input_taikhoan.getText().toString().trim();
            String matkhau = input_matkhau.getText().toString().trim();
            int check = dao.dang_nhap(taikhoan, matkhau);

            if (taikhoan.equals("") || matkhau.equals("")) {
                Toast.makeText(this, "Thông tin tài khoản mật khẩu trống!", Toast.LENGTH_SHORT).show();
                //1 admin,2 khachhang,0 nhanvien
            }else if (check == -100) {
                Toast.makeText(this, "Tài khoản của bạn đã thôi việc vui lòng tìm admin để mở ", Toast.LENGTH_SHORT).show();
            }
            else if (check == 1 || check == 2 || check == 0) {
                SharedPreferences.Editor sharedPreferences = getSharedPreferences("admin", Context.MODE_PRIVATE).edit();
                Intent intent = new Intent(DangNhap.this, MainActivity.class);

                sharedPreferences.putInt("setting", check);
                sharedPreferences.putString("taikhoan", taikhoan);
                sharedPreferences.apply();
                startActivity(intent);

                sharedPreferences.putInt("setting", check);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.create();
                builder.setTitle("Thông tin tài khoản mật khẩu không đúng");
                builder.setMessage("Bạn có muốn đăng kí tài khoản");
                builder.setIcon(R.drawable.baseline_error_outline_24);
                builder.setPositiveButton("Có", (dialogInterface, i) -> {
                    Intent intent = new Intent(DangNhap.this, DangKi.class);
                    startActivity(intent);
                });
                builder.setNegativeButton("Không", (dialogInterface, i) -> {
                });
                builder.show();
            }
        });

        txt_signup.setOnClickListener(view -> {
            Intent intent = new Intent(DangNhap.this, DangKi.class);
            startActivity(intent);
        });

    }
}