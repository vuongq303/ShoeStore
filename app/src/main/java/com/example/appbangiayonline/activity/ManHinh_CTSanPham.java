package com.example.appbangiayonline.activity;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbangiayonline.R;
import com.example.appbangiayonline.adapter.CTSanPhamAdapter;
import com.example.appbangiayonline.adapter.HoaDonAdapter;
import com.example.appbangiayonline.adapter.MauSacAdapter;
import com.example.appbangiayonline.adapter.SizeAdapter;
import com.example.appbangiayonline.convert.ConvertImage;
import com.example.appbangiayonline.dao.CTSanPhamDao;

import com.example.appbangiayonline.dao.HoaDonCT_Dao;
import com.example.appbangiayonline.dao.HoaDonDao;
import com.example.appbangiayonline.dao.NhanVien_KhachHang_Dao;


import com.example.appbangiayonline.model.CTSanPham;
import com.example.appbangiayonline.model.HoaDon;
import com.example.appbangiayonline.model.KhachHang;

import com.example.appbangiayonline.model.SanPham;
import com.example.appbangiayonline.zalo_pay.ZaloPay;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;


public class ManHinh_CTSanPham extends AppCompatActivity implements OnItemClickMauSize {
    String tenchung;
    ImageButton giohang;
    Button muaNgay;

    CTSanPhamDao dao;
    ArrayList<CTSanPham> list;
    CTSanPhamAdapter adapter;
    MauSacAdapter mauSacAdapter;
    SizeAdapter kichCoAdapter;

    RecyclerView rcmau;
    RecyclerView rckichco;
    RecyclerView rcctsanpham;
    CTSanPham ctSanPham;

    //nhan vao item
    TextView nhantenct;
    TextView nhankichco;
    TextView nhanmausac;
    TextView nhantensanpham;
    TextView nhangia;
    TextView nhansiluong;
    //lưu dữ được nhiều lần chọn
   String selectedMauSac;
  int selectedKichCo;
    //cong tru
    int tongSoLuongSP = 1;
    int tongGiaSP;
    //cong tru soluong tongtien
    ImageView imgCong;
    ImageView imgTru;
    TextView muangay_soluong;
    TextView muangay_tongtien;
    ImageView quaylai_rc_sanpham, img_ctsp;

    //KhachHang
    NhanVien_KhachHang_Dao dao_nv_kh;
    //HoaDon
    HoaDonDao daohd;
    HoaDonAdapter adapterhd;
    ArrayList<HoaDon> listhd;
    //lay mactsp để cập nhật số lương mới nè
    int laymactsp;
    Bitmap bitmap;
    int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_hinh_ctsan_pham);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        SharedPreferences sharedPreferences = getSharedPreferences("admin", Context.MODE_PRIVATE);
        check = sharedPreferences.getInt("setting", 0);

        giohang = findViewById(R.id.giohang_sanpham);
        muaNgay = findViewById(R.id.muangay_sanpham);
        if (check != 2) {
            giohang.setVisibility(View.GONE);
            muaNgay.setText("Xem chi tiết");
        }

        quaylai_rc_sanpham = findViewById(R.id.quaylai_rc_sanpham);

        quaylai_rc_sanpham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();

        if (intent.hasExtra("obj_sanpham")) {

            SanPham sanPham = (SanPham) intent.getSerializableExtra("obj_sanpham");
            bitmap = ConvertImage.ByteToBitmap(sanPham.getImage());
            tenchung = sanPham.getTensanpham();
            img_ctsp = findViewById(R.id.anhSanpham_ctSanpham);
            img_ctsp.setImageBitmap(bitmap);
            muaNgay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    XacNhanMuaNgay(tongSoLuongSP, tongGiaSP);
                }
            });

        }
        //CHÚ Ý NÈ: Cong tru va sotien, soluong
        imgCong = findViewById(R.id.imgCong);
        imgTru = findViewById(R.id.imgTru);

        muangay_soluong = findViewById(R.id.tongsoluong_muangay);
        muangay_tongtien = findViewById(R.id.tongtien_muangay);

        rckichco = findViewById(R.id.kichco_detail);
        rcmau = findViewById(R.id.mausac_detail);
        rcctsanpham = findViewById(R.id.rcctsanpham_detail);
        dao = new CTSanPhamDao(this);
        ctSanPham = new CTSanPham();

        nhantenct = findViewById(R.id.tensanphamct);
        nhankichco = findViewById(R.id.kiccosanphamct);
        nhanmausac = findViewById(R.id.mausacsanphamct);
        nhantensanpham = findViewById(R.id.tensanphamct);
        nhansiluong = findViewById(R.id.soluongsanphamct);
        nhangia = findViewById(R.id.giasanphamct);
        load(tenchung);
        loadDSKichCo(tenchung);
        loadDSMau(tenchung);

        giohang.setOnClickListener(view -> {
            themGioHang();
        });
    }

    //zalo//
    public void thanhToan(String giatien) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_xacnhanmua, null);
        builder.setView(v);

        AlertDialog dialog = builder.create();
        RadioButton rdbtn_ttnhanhang = v.findViewById(R.id.rdbtn_ttkhinhan);
        RadioButton rdbtn_ttzalo = v.findViewById(R.id.rdbtn_ttzalo);
        LinearLayout layout = v.findViewById(R.id.layout_zalo);
        TextView code_donhang = v.findViewById(R.id.txt_code_donhang);
        TextView tongtien = v.findViewById(R.id.txt_tongtien_thanhtoan);
        Button xacnhan = v.findViewById(R.id.btn_xacnhan_thanhtoan);

        rdbtn_ttnhanhang.setOnClickListener(view -> {
            if (rdbtn_ttnhanhang.isChecked()) {
                layout.setVisibility(View.GONE);
            }
        });

        rdbtn_ttzalo.setOnClickListener(view -> {
            if (rdbtn_ttzalo.isChecked()) {
                layout.setVisibility(View.VISIBLE);
            }
        });
        String code = ZaloPay.createHoadon(giatien);
        code_donhang.setText(code);
        tongtien.setText(giatien + " VND");

        xacnhan.setOnClickListener(view -> {
            if (!rdbtn_ttnhanhang.isChecked() && !rdbtn_ttzalo.isChecked()) {
                Toast.makeText(this, "Bạn chưa chọn phương thức thanh toán!", Toast.LENGTH_SHORT).show();
            } else {
                if (rdbtn_ttnhanhang.isChecked()) {
                    SharedPreferences sharedPreferences = getSharedPreferences("admin", MODE_PRIVATE);
                    String username = sharedPreferences.getString("taikhoan", "");

                    if (!TextUtils.isEmpty(username)) {
                        KhachHang khachHang = dao_nv_kh.getThongTinKhachHang(username);
                        int makh = khachHang.getMakh();
                        if (makh != 0) {
                            //lay ngay,thangg, nam
                            Date ngayDate = Calendar.getInstance().getTime();
                            SimpleDateFormat ngDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            String ngay = ngDateFormat.format(ngayDate);
                            //lay gio
                            Date gioDate = new Date();
                            SimpleDateFormat gioFormat = new SimpleDateFormat("HH:mm");
                            String gio = gioFormat.format(gioDate);

                            boolean row = daohd.addHoaDon(makh, tongGiaSP, ngay, gio);
                            if (row) {
                                listhd.clear();
                                listhd.addAll(daohd.getDSHoaDon());
                                adapterhd.notifyDataSetChanged();
                                int mahd = daohd.mahd();

                                    list.clear();
                                    list.addAll(dao.getListCTSanPham(tenchung));
                                    adapter.notifyDataSetChanged();
                                    HoaDonCT_Dao daoCTHD = new HoaDonCT_Dao(ManHinh_CTSanPham.this);
                                    boolean addCTHD = daoCTHD.themCTHD(mahd, laymactsp, tongSoLuongSP);

                                    if (addCTHD) {
                                        list.clear();
                                        list.addAll(dao.getListCTSanPham(tenchung));
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(ManHinh_CTSanPham.this, "thành công", Toast.LENGTH_SHORT).show();
                                    }
                                    Intent intent1 = new Intent(ManHinh_CTSanPham.this, MainActivity.class);
                                    intent1.putExtra("gethoadon", ":D");
                                    Toast.makeText(ManHinh_CTSanPham.this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    startActivity(intent1);
                            }
                        } else {
                            Toast.makeText(ManHinh_CTSanPham.this, "Khong ton tai", Toast.LENGTH_SHORT).show();
                        }
                    }
                    dialog.dismiss();
                    Toast.makeText(this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                }
                if (rdbtn_ttzalo.isChecked()) {
                    ZaloPaySDK.getInstance().payOrder(ManHinh_CTSanPham.this, code_donhang.getText().toString().trim(), "ctsp://app", new PayOrderListener() {
                        @Override
                        public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                            SharedPreferences sharedPreferences = getSharedPreferences("admin", MODE_PRIVATE);
                            String username = sharedPreferences.getString("taikhoan", "");

                            if (!TextUtils.isEmpty(username)) {
                                KhachHang khachHang = dao_nv_kh.getThongTinKhachHang(username);
                                int makh = khachHang.getMakh();
                                if (makh != 0) {
                                    //lay ngay,thangg, nam
                                    Date ngayDate = Calendar.getInstance().getTime();
                                    SimpleDateFormat ngDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                    String ngay = ngDateFormat.format(ngayDate);
                                    //lay gio
                                    Date gioDate = new Date();
                                    SimpleDateFormat gioFormat = new SimpleDateFormat("HH:mm");
                                    String gio = gioFormat.format(gioDate);

                                    boolean row = daohd.addHoaDon(makh, tongGiaSP, ngay, gio);
                                    if (row) {
                                        listhd.clear();
                                        listhd.addAll(daohd.getDSHoaDon());
                                        adapterhd.notifyDataSetChanged();
                                        int mahd = daohd.mahd();

                                            list.clear();
                                            list.addAll(dao.getListCTSanPham(tenchung));
                                            adapter.notifyDataSetChanged();

                                            HoaDonCT_Dao daoCTHD = new HoaDonCT_Dao(ManHinh_CTSanPham.this);
                                            boolean addCTHD = daoCTHD.themCTHD(mahd, laymactsp, tongSoLuongSP);

                                            if (addCTHD) {
                                                list.clear();
                                                list.addAll(dao.getListCTSanPham(tenchung));
                                                adapter.notifyDataSetChanged();
                                                Toast.makeText(ManHinh_CTSanPham.this, "thành công", Toast.LENGTH_SHORT).show();
                                            }

                                            Intent intent1 = new Intent(ManHinh_CTSanPham.this, MainActivity.class);
                                            intent1.putExtra("gethoadon", ":D");
                                            Toast.makeText(ManHinh_CTSanPham.this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            startActivity(intent1);
                                    }
                                } else {
                                    Toast.makeText(ManHinh_CTSanPham.this, "Khong ton tai", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onPaymentCanceled(String zpTransToken, String appTransID) {
                            Toast.makeText(ManHinh_CTSanPham.this, "Bạn đã hủy thanh toán!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                            Toast.makeText(ManHinh_CTSanPham.this, "Lỗi thanh toán!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
        dialog.show();
    }

    private void load(String tenchung) {
//        rcctsanpham.setLayoutManager(new LinearLayoutManager(ManHinh_CTSanPham.this, RecyclerView.HORIZONTAL, false));
//        list = dao.getListCTSanPham(tenchung);
        adapter = new CTSanPhamAdapter(ManHinh_CTSanPham.this, list);
//        rcctsanpham.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
        // Khi dữ liệu đã được lấy xong, gọi phương thức onDataLoaded()
    }
    private void loadDSKichCo(String tansanpham) {
        rckichco.setLayoutManager(new LinearLayoutManager(ManHinh_CTSanPham.this, RecyclerView.HORIZONTAL, false));
        list = dao.getListDSMauSize(tansanpham);
        kichCoAdapter = new SizeAdapter(ManHinh_CTSanPham.this, list, new OnItemClickMauSize() {
            @Override
            public void onItemClick(CTSanPham ctSanPham) {
                nhankichco.setText(String.valueOf(ctSanPham.getKichco()));
                nhantenct.setText(ctSanPham.getTensanpham());
                //load lại số lượng, tổng giá về con số 0
                muangay_soluong.setText("1");
                muangay_tongtien.setText("0");
                selectedKichCo = ctSanPham.getKichco();
                mauSize(selectedMauSac, selectedKichCo);
            }
        });
        rckichco.setAdapter(kichCoAdapter);
        kichCoAdapter.notifyDataSetChanged();
    }

    private void loadDSMau(String tenchung) {
        rcmau.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        list = dao.getListDSMauSize(tenchung);
        mauSacAdapter = new MauSacAdapter(ManHinh_CTSanPham.this, list, new OnItemClickMauSize() {
            @Override
            public void onItemClick(CTSanPham ctSanPham) {
                nhanmausac.setText(ctSanPham.getTenmausac());
                nhantenct.setText(ctSanPham.getTensanpham());
                //load lại số lượng, tổng giá về con số 0
                muangay_soluong.setText("1");
                muangay_tongtien.setText("0");
                //o day minh dat cho no ten bien de de dang goi
                selectedMauSac = ctSanPham.getTenmausac();
                //thuc hiện truyền dữ liệu trước b1
                mauSize(selectedMauSac, selectedKichCo);
            }
        });
        rcmau.setAdapter(mauSacAdapter);
        mauSacAdapter.notifyDataSetChanged();
    }

    private void mauSize(String selectedMauSac, int selectedKichCo) {
        if (selectedMauSac != null && selectedKichCo != 0) {
            ctSanPham = new CTSanPham();
            ctSanPham = dao.getItemCTSanPham(selectedMauSac, selectedKichCo);
            //Hiện thị thông tin sản phẩm khi click cả 2
            if (ctSanPham != null) {
                laymactsp = ctSanPham.getMactsanpham();
                nhanmausac.setText("Màu: " + selectedMauSac);
                nhankichco.setText("Kích cỡ: " + Integer.toString(selectedKichCo));
                nhangia.setText("Giá: " + Integer.toString(ctSanPham.getGia()));
                nhansiluong.setText("Số lượng: " + Integer.toString(ctSanPham.getSoluong()));
                tongGiaSP = ctSanPham.getGia();
                //CHÚ Ý NÈ: Cong tru va sotien, soluong
                if (imgCong != null) {
                    tongSoLuongSP = 1;
                    muangay_tongtien.setText(Integer.toString(tongGiaSP));
                    imgCong.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (tongSoLuongSP >= 0 && ctSanPham != null) {
                                tongSoLuongSP++;
                                tongGiaSP = ctSanPham.getGia() * tongSoLuongSP;
                                muangay_soluong.setText(String.valueOf(tongSoLuongSP));
                                muangay_tongtien.setText("Tổng tiền: " + String.valueOf(tongGiaSP));
                            } else {
                                imgCong.setClickable(false);

                            }
                        }
                    });
                }

                if (imgTru != null) {
                    tongSoLuongSP = 1;
                    muangay_tongtien.setText(Integer.toString(tongGiaSP));
                    imgTru.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (tongSoLuongSP > 1 && ctSanPham != null) {
                                tongSoLuongSP--;
                                tongGiaSP = ctSanPham.getGia() * tongSoLuongSP;
                                muangay_soluong.setText(String.valueOf(tongSoLuongSP));
                                muangay_tongtien.setText("Tổng tiền: " + String.valueOf(tongGiaSP));
                            } else {

                            }
                        }
                    });
                }
            } else {
                nhangia.setText("Giá: " + "0");
                nhansiluong.setText("Số lượng: " + "0");
            }
        } else {
            nhangia.setText("Giá: " + "0");
            nhansiluong.setText("Số lượng: " + "0");
        }
    }

    //Thêm vào giỏ
    private void themGioHang(){
            Intent intent = new Intent(ManHinh_CTSanPham.this, Activity_GioHang.class);
            if (selectedMauSac != null && selectedKichCo != 0) {
                CTSanPham ctSanPham1 = dao.getItemCTSanPham_config(tenchung, selectedMauSac, selectedKichCo);

                if (ctSanPham1 == null) {
                    Toast.makeText(this, "Sản phẩm đã hết hàng :(", Toast.LENGTH_SHORT).show();
                } else {
                    ctSanPham1.setSl_mua(tongSoLuongSP);
                    intent.putExtra("themgiohang", ctSanPham1);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(this, "Bạn hãy chọn màu sắc và kích cỡ phù hợp!", Toast.LENGTH_SHORT).show();
            }
    }
    @Override
    public void onItemClick(CTSanPham ctSanPham) {
        if (selectedMauSac != null && selectedKichCo != 0) {
            mauSize(selectedMauSac, selectedKichCo);
        } else {

        }
    }
    private void XacNhanMuaNgay(int tongSoLuongSP, int tongGiaSP) {
        ctSanPham = new CTSanPham();
        ctSanPham = dao.getItemCTSanPham(selectedMauSac, selectedKichCo);
        //qua bat luc nen phai use flag :<
        if (ctSanPham != null) {
            daohd = new HoaDonDao(this);
            listhd = daohd.getDSHoaDon();
            adapterhd = new HoaDonAdapter(this, listhd);
            dao_nv_kh = new NhanVien_KhachHang_Dao(this);

            if (xemConSoLuong(tongSoLuongSP)) {
                thanhToan(String.valueOf(tongGiaSP));
            } else {
                Toast.makeText(this, "Sản phẩm này đã hết ạ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Không tồn tại sản phẩm này", Toast.LENGTH_SHORT).show();
        }

    }
    private boolean xemConSoLuong(int tongSoLuongSP) {
        if(tongSoLuongSP<=0){
            Toast.makeText(this, " Số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
            return false;
        }
        return ctSanPham.getSoluong() >= tongSoLuongSP;
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}