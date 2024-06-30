package com.example.appbangiayonline.fragmentTA;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbangiayonline.R;
import com.example.appbangiayonline.activity.Activity_DoiMK;
import com.example.appbangiayonline.activity.Activity_GioHang;
import com.example.appbangiayonline.activity.DangNhap;
import com.example.appbangiayonline.activity.MainActivity;
import com.example.appbangiayonline.activity.SuaThongTinActivity;
import com.example.appbangiayonline.adapter.HoaDonAdapter;
import com.example.appbangiayonline.dao.HoaDonDao;
import com.example.appbangiayonline.dao.NhanVien_KhachHang_Dao;
import com.example.appbangiayonline.model.HoaDon;
import com.example.appbangiayonline.model.KhachHang;

import java.util.ArrayList;

public class FragmentThongTin extends Fragment {
    public FragmentThongTin() {

    }

    NhanVien_KhachHang_Dao dao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_thong_tin, container, false);

        dao = new NhanVien_KhachHang_Dao(requireActivity());
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("admin", Context.MODE_PRIVATE);
        KhachHang khachHang = dao.getThongTinKhachHang(sharedPreferences.getString("taikhoan", ""));
        TextView txt_hoten = view.findViewById(R.id.txt_hoten_nguoidung);
        txt_hoten.setText(khachHang.getHoten());

        TextView capNhatThongtin = view.findViewById(R.id.thongtin_nguoidung);
        capNhatThongtin.setOnClickListener(i -> {
            Intent intent = new Intent(requireActivity(), SuaThongTinActivity.class);
            startActivity(intent);
        });

        TextView  dsHoaDon = view.findViewById(R.id.ds_hoadon_nguoidung);
        dsHoaDon.setOnClickListener(i -> {
            ((MainActivity) getActivity()).change_Fragment(new FragmentHoaDon(), "Danh sách hóa đơn");
        });

        TextView  gioHang = view.findViewById(R.id.giohang_nguoidung);
        gioHang.setOnClickListener(i -> {
            Intent intent = new Intent(requireActivity(), Activity_GioHang.class);
            startActivity(intent);
        });
        TextView  doimk = view.findViewById(R.id.doimk_nguoidung);
        doimk.setOnClickListener(i -> {
            Intent intent = new Intent(requireActivity(), Activity_DoiMK.class);
            startActivity(intent);
        });
        TextView  dangxuat = view.findViewById(R.id.dangxuat_nguoidung);
        dangxuat.setOnClickListener(i -> {
            getActivity().finish();
        });


        return view;
    }
}