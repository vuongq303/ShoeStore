package com.example.appbangiayonline.fragmentTA;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbangiayonline.R;
import com.example.appbangiayonline.activity.ManHinh_CTSanPham;
import com.example.appbangiayonline.adapter.SanPhamAdapter;
import com.example.appbangiayonline.convert.ConvertImage;
import com.example.appbangiayonline.dao.SanPhamDao;
import com.example.appbangiayonline.model.SanPham;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentSanPham extends Fragment {

    SanPhamDao dao;
    ArrayList<SanPham> list;
    SanPhamAdapter adapter;

    FloatingActionButton fl;
    RecyclerView rc_sanpham;
    ImageView imageView;
    TextView select_image;
    EditText edt_ten, edt_hang;
    Button btnThem, btnHuy;
    View dialogView;
    AlertDialog alertDialog;
    //bộ lọc
    List<String> DSHang;
    ArrayAdapter<String> hangadapter;
    Spinner boLoc;
    List<String> hangDN;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view1 = inflater.inflate(R.layout.fragment_san_pham, container, false);

        fl = view1.findViewById(R.id.fl_shoes_tab2);
        rc_sanpham = view1.findViewById(R.id.rc_shoes_tab2);
        rc_sanpham.setLayoutManager(new GridLayoutManager(getContext(), 1));
        dao = new SanPhamDao(getContext());
        reload();

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("admin", Context.MODE_PRIVATE);
        int check = sharedPreferences.getInt("setting", 2);
        if (check == 2) {
            fl.setVisibility(View.GONE);
        }

        //Bo loc
        DSHang = Arrays.asList("Nike", "New Balance");
        boLoc = view1.findViewById(R.id.boLoc);
        hangadapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, getHangDuyNhat());
        hangadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boLoc.setAdapter(hangadapter);

        boLoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //lấy hàng được chọn từ spinner
                String chonHang = hangadapter.getItem(i);
                List<SanPham> boLocHang = null;
                if (i != 0) {
                    //tạo 1 danh sach sanpham để lọc nè
                    boLocHang = new ArrayList<>();
                    for (SanPham sanPham : list) {
                        if (sanPham != null && chonHang.equals(sanPham.getHang())) {
                            boLocHang.add(sanPham);
                        }
                    }
                    adapter.setData((ArrayList<SanPham>) boLocHang);
                    adapter.notifyDataSetChanged();

                } else if (i == 0) {
                    adapter.setData(list);
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.setData(list);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapter.setData(list);
                adapter.notifyDataSetChanged();
            }
        });

        fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemSanPham();
                boLoc.setAdapter(hangadapter);
            }
        });

        return view1;
    }

    private List<String> getHangDuyNhat() {
        hangDN = new ArrayList<>();
        hangDN.add("Tất cả sản phẩm");
        for (SanPham sanPham : list) {
            if (sanPham != null && !hangDN.contains(sanPham.getHang())) {
                hangDN.add(sanPham.getHang());
            }
        }
        return hangDN;
    }

    private void ThemSanPham() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.them_sanpham, null);
        builder.setView(dialogView);
        alertDialog = builder.create();

        edt_ten = dialogView.findViewById(R.id.tensanpham_shoes_tab_them);
        edt_hang = dialogView.findViewById(R.id.hang_shoes_tab_them);
        select_image = dialogView.findViewById(R.id.txt_chooseImage_sanpham);
        btnThem = dialogView.findViewById(R.id.btn_them_dialogThemsp);
        btnHuy = dialogView.findViewById(R.id.btn_huy_dialogThemsp);
        imageView = dialogView.findViewById(R.id.setimg);

        select_image.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        });

        btnThem.setOnClickListener(view -> {
            String ten = edt_ten.getText().toString().trim();
            String hang = edt_hang.getText().toString().trim();
            SanPham sp = new SanPham();
            byte[] resultImage = ConvertImage.ImageDrawableToByte(imageView);

            if (resultImage == null || ten.equals("") || hang.equals("")) {
                Toast.makeText(requireActivity(), "Nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                sp.setImage(resultImage);
                sp.setTensanpham(ten);
                sp.setHang(hang);

                boolean kt = dao.ThemSanPham(sp);
                if (kt)
                    Toast.makeText(requireActivity(), "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
                reload();
                capNhatSpinner();
            }
        });
        btnHuy.setOnClickListener(view -> {
            alertDialog.dismiss();
        });
        alertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            Uri selectedImageUri = data.getData();
            byte[] imageByte = ConvertImage.FileImageToByte(requireActivity(), selectedImageUri);
            imageView.setImageBitmap(ConvertImage.ByteToBitmap(imageByte));
        }
    }

    private void capNhatSpinner() {
        hangadapter.clear();
        hangadapter.addAll(getHangDuyNhat());
        hangadapter.notifyDataSetChanged();
    }

    void reload() {
        list = dao.getListSanPham();
        adapter = new SanPhamAdapter(getContext(), list);
        rc_sanpham.setAdapter(adapter);
    }
}

