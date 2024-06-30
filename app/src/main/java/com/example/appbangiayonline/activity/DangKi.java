package com.example.appbangiayonline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbangiayonline.R;
import com.example.appbangiayonline.dao.DangNhap_DangKi_Dao;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DangKi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);

        TextInputEditText input_hoten = findViewById(R.id.input_hoten_dangki);
        TextInputEditText input_taikhoan = findViewById(R.id.input_taikhoan_dangki);
        TextInputEditText input_matkhau = findViewById(R.id.input_matkhau_dangki);
        TextInputEditText input_email = findViewById(R.id.input_email_dangki);
        TextInputEditText input_sdt = findViewById(R.id.input_sdt_dangki);

        Button btn_dangki = findViewById(R.id.btn_dangki_dangki);
        TextView txt_dangnhap = findViewById(R.id.txt_dangnhap_dangki);
        DangNhap_DangKi_Dao dao = new DangNhap_DangKi_Dao(this);

        txt_dangnhap.setOnClickListener(view -> {
            Intent intent = new Intent(DangKi.this, DangNhap.class);
            startActivity(intent);
        });
        btn_dangki.setOnClickListener(v -> {
            String hoten = input_hoten.getText().toString().trim();
            String taikhoan = input_taikhoan.getText().toString().trim();
            String matkhau = input_matkhau.getText().toString().trim();
            String email = input_email.getText().toString().trim();
            String sdt = input_sdt.getText().toString().trim();

            if (hoten.equals("") || taikhoan.equals("") || matkhau.equals("") || email.equals("") || sdt.equals("")) {
                Toast.makeText(this, "Thông tin tài khoản mật khẩu trống!", Toast.LENGTH_SHORT).show();
            } else {
                int check = dao.dang_ki(hoten, taikhoan, matkhau, email, sdt);
                if (check == 0) {
                    Toast.makeText(this, "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                }
                if (check == 1) {
                    Toast.makeText(this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DangKi.this, DangNhap.class);
                    startActivity(intent);
                }
            }
        });
    }
}