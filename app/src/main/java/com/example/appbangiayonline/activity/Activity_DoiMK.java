package com.example.appbangiayonline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appbangiayonline.R;
import com.example.appbangiayonline.dao.NhanVien_KhachHang_Dao;
import com.example.appbangiayonline.model.KhachHang;
import com.example.appbangiayonline.model.NhanVien;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Activity_DoiMK extends AppCompatActivity {
    private String passWord,taikhoan;
    private int check = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mk);

        TextInputEditText edtOldPass = findViewById(R.id.input_old_pass);
        TextInputEditText edtNewPass = findViewById(R.id.input_New_Pass);
        TextInputEditText edt_Re_typeNewPass = findViewById(R.id.input_retype_Pass);
        TextInputLayout ipLayoutOld = findViewById(R.id.layout_user);
        TextInputLayout ipLayoutnew = findViewById(R.id.layout_password);
        TextInputLayout ipLayoutnew2 = findViewById(R.id.layout_re_type_pass);
        Button btnChangePass = findViewById(R.id.btn_change_pass);
        ImageView btnBack = findViewById(R.id.img_btnBack_DMK);
        ImageView btnEye = findViewById(R.id.imgEye);

        NhanVien_KhachHang_Dao dao1 = new NhanVien_KhachHang_Dao(this);
        NhanVien_KhachHang_Dao dao = new NhanVien_KhachHang_Dao(this);
        SharedPreferences sharedPreferences = getSharedPreferences("admin", Context.MODE_PRIVATE);
        taikhoan = sharedPreferences.getString("taikhoan","");
        if(sharedPreferences.getInt("setting",0) == 2){
            KhachHang kh = dao.getThongTinKhachHang(taikhoan);
            passWord = kh.getMatkhau();
            check++;
        }else {
            NhanVien nv = dao.getThongTinNhanVien(taikhoan);
            passWord = nv.getMatkhau();

        }

        btnEye.setOnClickListener(new View.OnClickListener() {
            boolean eyePass = false;
            @Override
            public void onClick(View v) {
                if (eyePass){
                    ipLayoutOld.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
                    ipLayoutnew2.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
                    ipLayoutOld.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
                    edtOldPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edtNewPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edt_Re_typeNewPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eyePass = false;
                } else {
                    ipLayoutOld.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
                    ipLayoutnew2.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
                    ipLayoutOld.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
                    edtOldPass.setTransformationMethod(null);
                    edt_Re_typeNewPass.setTransformationMethod(null);
                    edtNewPass.setTransformationMethod(null);
                    eyePass = true;
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ollPass = edtOldPass.getText().toString().trim();
                String newPass = edtNewPass.getText().toString().trim();
                String re_typePass = edt_Re_typeNewPass.getText().toString().trim();

                if(newPass.length()<4){
                    Toast.makeText(Activity_DoiMK.this, "Mật khẩu phải tối thiểu 4 ký tự ", Toast.LENGTH_SHORT).show();
                }else if (ollPass.length()<1){
                    Toast.makeText(Activity_DoiMK.this, "Vui lòng nhập mật khẩu cũ", Toast.LENGTH_SHORT).show();
                }else if(!newPass.equals(re_typePass)){
                    Toast.makeText(Activity_DoiMK.this, "Mật khẩu không giống nhau ", Toast.LENGTH_SHORT).show();
                }else if(!ollPass.equals(passWord)){
                    Toast.makeText(Activity_DoiMK.this, "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
                }else {
                    if (check>0){
                        dao1.ChangePassWord_KH(taikhoan,newPass);
                        Toast.makeText(Activity_DoiMK.this, "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Activity_DoiMK.this, MainActivity.class);
                        startActivity(intent);
                    }else {
                        dao1.ChangePassWord_NV(taikhoan,newPass);
                        Toast.makeText(Activity_DoiMK.this, "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Activity_DoiMK.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}