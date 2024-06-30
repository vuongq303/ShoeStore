package com.example.appbangiayonline.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.appbangiayonline.R;
import com.example.appbangiayonline.check_information.CheckInformation;
import com.example.appbangiayonline.dao.NhanVien_KhachHang_Dao;
import com.example.appbangiayonline.model.KhachHang;

public class SuaThongTinActivity extends AppCompatActivity {
    NhanVien_KhachHang_Dao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_thong_tin);
        dao = new NhanVien_KhachHang_Dao(this);
        SharedPreferences sharedPreferences = getSharedPreferences("admin", MODE_PRIVATE);

        KhachHang khachHang = dao.getThongTinKhachHang(sharedPreferences.getString("taikhoan", ""));
        EditText edt_hoten = findViewById(R.id.edt_hoten_suathongtin);
        EditText edt_sdt = findViewById(R.id.edt_sdt_suathongtin);
        EditText edt_email = findViewById(R.id.edt_email_suathongtin);
        EditText edt_diachi = findViewById(R.id.edt_diachi_suathongtin);
        ImageButton btnBack = findViewById(R.id.img_btn_back_nguoidung);

        edt_hoten.setText(khachHang.getHoten());
        edt_sdt.setText(khachHang.getSdt());
        edt_email.setText(khachHang.getEmail());
        edt_diachi.setText(khachHang.getDiachi());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button updateIn4 = findViewById(R.id.btn_suatk_nguoidung);
        updateIn4.setOnClickListener(v -> {

            String hoten = edt_hoten.getText().toString().trim();
            String sdt = edt_sdt.getText().toString().trim();
            String email = edt_email.getText().toString().trim();
            String diachi = edt_diachi.getText().toString().trim();

            if (hoten.equals("") || sdt.equals("") || email.equals("") || diachi.equals("")) {
                Toast.makeText(this, "Thông tin không được để trống", Toast.LENGTH_SHORT).show();
            } else if (!CheckInformation.isNumberValid(sdt)) {
                Toast.makeText(this, "Số điện thoại sai định dạng", Toast.LENGTH_SHORT).show();
            } else if (!CheckInformation.isEmailValid(email)) {
                Toast.makeText(this, "Email sai định dạng", Toast.LENGTH_SHORT).show();
            } else {
                dao.capNhatThongTin(khachHang.getMakh(), hoten, sdt, email, diachi);
                Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}