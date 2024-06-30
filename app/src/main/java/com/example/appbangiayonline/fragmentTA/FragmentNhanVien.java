package com.example.appbangiayonline.fragmentTA;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbangiayonline.R;
import com.example.appbangiayonline.activity.DangKi;
import com.example.appbangiayonline.activity.DangNhap;
import com.example.appbangiayonline.adapter.KhachHangAdapter;
import com.example.appbangiayonline.adapter.NhanVienAdapter;
import com.example.appbangiayonline.dao.NhanVien_KhachHang_Dao;
import com.example.appbangiayonline.model.KhachHang;
import com.example.appbangiayonline.model.NhanVien;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FragmentNhanVien extends Fragment {
    RecyclerView recyclerView;
    NhanVien_KhachHang_Dao dao;
    ArrayList<NhanVien> list;

    FloatingActionButton floadAdd;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nhan_vien_ta,container,false);
        recyclerView = view.findViewById(R.id.recylerV_NhanVien);
        floadAdd = view.findViewById(R.id.fload_btn_Add_NhanVien);
        setAdapter();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("admin", MODE_PRIVATE);
        String username = sharedPreferences.getString("taikhoan", "");
        NhanVien nv2 = dao.getThongTinNhanVien(username);
        if(nv2.getChucvu()==0){
            floadAdd.setVisibility(View.GONE);
        }

        floadAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogThem();
            }
        });
        return view;
    }
    public void setAdapter(){
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        dao= new NhanVien_KhachHang_Dao(getContext());
        list = dao.getList_NV();
        NhanVienAdapter adapter = new NhanVienAdapter(getContext(),list);
        recyclerView.setAdapter(adapter);
    }

    public void showDialogThem(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_them_nhan_vien, null);
        builder.setView(view);
        TextInputEditText input_diachi = view.findViewById(R.id.input_diachi_Them_NV);
        TextInputLayout inputLayout_diachi = view.findViewById(R.id.layout_diachi_Them_NV);
//        input_diachi.setVisibility(View.VISIBLE);
        inputLayout_diachi.setVisibility(View.GONE);
//        EditText edtTen,edtUserName,edtPass,edtSDT,edtEmail;
//        edtTen = view.findViewById(R.id.edtTenNhanVien_new);
//        edtUserName =view.findViewById(R.id.edtTen_User_NhanVien_new);
//        edtPass = view.findViewById(R.id.edtPassNhanVien_new);
//        edtSDT = view.findViewById(R.id.edtSDT_NV_new);
//        edtEmail = view.findViewById(R.id.edtEmail_NV_new);
        TextInputEditText input_hoten = view.findViewById(R.id.input_hoten_Them_NV);
        TextInputEditText input_taikhoan = view.findViewById(R.id.input_taikhoan_Them_NV);
        TextInputEditText input_matkhau = view.findViewById(R.id.input_matkhau_Them_NV);
        TextInputEditText input_email = view.findViewById(R.id.input_email_Them_NV);
        TextInputEditText input_sdt = view.findViewById(R.id.input_sdt__Them_NV);
        Button btnHuy,btnThem;
        btnThem = view.findViewById(R.id.btnThemNV);
        btnHuy = view.findViewById(R.id.btnHuyThemNV);
        TextView txtTitel = view.findViewById(R.id.txtChuDeDialogThem);
        txtTitel.setText("Thêm nhân viên");
        AlertDialog dialog = builder.create();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hoten,taikhoan,matkhau,email,sdt;
                hoten = input_hoten.getText().toString();
                taikhoan = input_taikhoan.getText().toString();
                matkhau = input_matkhau.getText().toString();
                email = input_email.getText().toString();
                sdt = input_sdt.getText().toString();
                if (isValid(hoten,taikhoan,matkhau,sdt,email)){
                    dialog.dismiss();
                }
            }
        });
       btnHuy.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.dismiss();
           }
       });

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT; // Width có thể thiết lập theo nhu cầu của bạn
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT; // Height có thể thiết lập theo nhu cầu của bạn
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();
    }


    public boolean isValid(String hoten,String taikhoan, String matkhau,String sdt,String email) {
        if(hoten.equals("")||sdt.equals("")||
                taikhoan.equals("")||email.equals("")
                || matkhau.equals("")){

            Toast.makeText(getContext(), "Không để trống dữ liệu", Toast.LENGTH_SHORT).show();
            return false;
        }else {
//                    String hoten, String taikhoan, String matkhau, String email, String sdt
            int check = dao.newNhanVien(hoten, taikhoan, matkhau, sdt, email);
            if (check == 0) {
                Toast.makeText(getContext(), "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                return false;
            } else if (check == 1) {
                Toast.makeText(getContext(), "Thêm mới thành công", Toast.LENGTH_SHORT).show();
                list.clear();
                setAdapter();
                return true;
            }
        }
        return false;

    }

}
