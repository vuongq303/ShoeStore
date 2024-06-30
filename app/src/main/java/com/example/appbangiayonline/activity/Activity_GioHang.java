package com.example.appbangiayonline.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbangiayonline.R;
import com.example.appbangiayonline.adapter.adapter_giohang;
import com.example.appbangiayonline.dao.CTSanPhamDao;
import com.example.appbangiayonline.dao.Giohang_Dao;
import com.example.appbangiayonline.dao.HoaDonCT_Dao;
import com.example.appbangiayonline.dao.HoaDonDao;
import com.example.appbangiayonline.dao.NhanVien_KhachHang_Dao;
import com.example.appbangiayonline.model.CTSanPham;
import com.example.appbangiayonline.model.GioHang;
import com.example.appbangiayonline.model.HoaDon;
import com.example.appbangiayonline.model.KhachHang;
import com.example.appbangiayonline.zalo_pay.ZaloPay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class Activity_GioHang extends AppCompatActivity {
    HoaDonDao dao2;
    CTSanPhamDao daoSP;
    Giohang_Dao dao;
    RecyclerView recyclerView;
    adapter_giohang adap;
    ArrayList<Integer> listchk = new ArrayList<>();

    ArrayList<GioHang> list;
    TextView tongtien;
    ArrayList<HoaDon> listhd;
    public int s;
    int id_kh, makh, sl, kichco;
    String mausac;
    HoaDonCT_Dao daoHDCT;
    boolean checksl = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment__gio_hang);

        daoHDCT = new HoaDonCT_Dao(this);
        dao = new Giohang_Dao(this);
        recyclerView = findViewById(R.id.rcv_giohang);
        tongtien = findViewById(R.id.tongTien_giohang);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reload();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        ImageButton back = findViewById(R.id.img_btn_back_giohang);
        back.setOnClickListener(view -> {
            finish();
        });

        //----------------
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("themgiohang")) {
            CTSanPham sanPham = (CTSanPham) intent.getSerializableExtra("themgiohang");

            SharedPreferences sharedPreferences = getSharedPreferences("khachhang", MODE_PRIVATE);
            id_kh = sharedPreferences.getInt("id_kh", -1);
            if (id_kh != -1) {
                dao.themGioHang(sanPham, id_kh);
                Toast.makeText(this, "Thêm sản phẩm " + sanPham.getTensanpham() + " thành công!", Toast.LENGTH_SHORT).show();
                reload();
            }
        }
        //------------------

        TextView edit = findViewById(R.id.edit_sp_giohang);
        edit.setOnClickListener(view -> {
            AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
            if (listchk.size() != 0) {
                alBuilder.setTitle("Xóa sản phẩm trong giỏ hàng!").setIcon(R.drawable.baseline_error_outline_24).setMessage("Bạn có chắc chắn muốn xóa sản phẩm").setPositiveButton("Có", ((dialogInterface, i) -> {
                    listchk.forEach(e -> {
                        dao.remove_data(e);
                    });
                    listchk.clear();
                    tongtien.setText("0 VNĐ");
                    reload();
                })).setNegativeButton("Không", ((dialogInterface, i) -> {
                }));
            }
            alBuilder.show();
        });

        NhanVien_KhachHang_Dao dao_nv_kh = new NhanVien_KhachHang_Dao(this);
        SharedPreferences sharedPreferences = getSharedPreferences("admin", MODE_PRIVATE);
        String username = sharedPreferences.getString("taikhoan", "");

        if (!TextUtils.isEmpty(username)) {
            KhachHang khachHang = dao_nv_kh.getThongTinKhachHang(username);
            if (khachHang != null) {
                makh = khachHang.getMakh();
            } else {
                makh = 1;
            }
        }

        TextView button_thanhToan = findViewById(R.id.btn_thanhtoan_giohang);
        button_thanhToan.setOnClickListener(view -> {
            //
                            AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
                        alBuilder
                                .setTitle("Thanh toán sản phẩm trong giỏ hàng!")
                                .setIcon(R.drawable.baseline_error_outline_24)
                                .setMessage("Bạn có chắc chắn muốn Thanh toán sản phẩm")
                                .setPositiveButton("Có", ((dialogInterface, i) -> {
                                    thanhToan(String.valueOf(s));
                                })).setNegativeButton("Không", ((dialogInterface, i) -> {
                                }));

                        alBuilder.show();

            //


        });
    }

    void reload() {
        list = dao.getList();
        adap = new adapter_giohang(list, this);
        recyclerView.setAdapter(adap);
    }
    public void add_chck(int i) {
        listchk.add(i);
        reload_tongtien();
    }
    public void rm_chck(int i) {
        listchk.remove(listchk.indexOf(i));
        reload_tongtien();
    }
    public void reload_tongtien() {
        s = 0;
        listchk.forEach(e -> {
            list.forEach(e1 -> {
                if (e1.getMagiohang() == e) {
                    s += (e1.getGiasp() * e1.getSl_mua());
                }
            });
        });
        tongtien.setText(s + " VNĐ");
    }

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
        dao2 = new HoaDonDao(this);
//        boolean check = dao2.addHoaDon(makh, s, ngay, gio);
        daoSP = new CTSanPhamDao(this);
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
                    //lay ngay thang
                    Date ngayDate = Calendar.getInstance().getTime();
                    SimpleDateFormat ngDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String ngay = ngDateFormat.format(ngayDate);
                    //lay gio
                    Date gioDate = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                    String gio = simpleDateFormat.format(gioDate);
                    if (listchk.size() != 0) {
                        boolean check = dao2.addHoaDon(makh, s, ngay, gio);
                        if (check) {

                            listhd = dao2.getDSHoaDon();
                            int mahd = dao2.mahd();

//                            AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
                            if (listchk.size() != 0) {
//                        alBuilder
//                                .setTitle("Thanh toán sản phẩm trong giỏ hàng!")
//                                .setIcon(R.drawable.baseline_error_outline_24)
//                                .setMessage("Bạn có chắc chắn muốn Thanh toán sản phẩm")
//                                .setPositiveButton("Có", ((dialogInterface, i) -> {


                                for(int i=0;i<listchk.size();i++){
                                    for(GioHang g:list){
                                        if(g.getSl_mua()<=0) checksl = true;
                                    }
                                }
                                if(checksl){
                                    Toast.makeText(this, "số lượng phải lớn hơn 0 ", Toast.LENGTH_SHORT).show();
                                } else {
                                    listchk.forEach(e -> {
                                        list.forEach(e1 -> {
                                            if (e1.getMagiohang() == e) {
                                                sl = e1.getSl_mua();
                                                if (sl<=0){
                                                    Toast.makeText(this, "số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                                                }
                                                kichco = e1.getKichco();
                                                mausac = e1.getMausac();
                                            }
                                        });
                                        int masp = dao.getMaCTSP(e);
                                        daoHDCT.themCTHD(mahd, daoSP.getMaCTSP(masp, mausac, kichco), sl);
                                        dao.remove_data(e);
                                    });
                                }




//                                })).setNegativeButton("Không", ((dialogInterface, i) -> {
//                                }));
                            }
//                        alBuilder.show();
                        }
                    } else {
                        Toast.makeText(this, "Bạn chưa chọn sản phẩm nào :(", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                    Toast.makeText(this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                }
                if (rdbtn_ttzalo.isChecked()) {
                    ZaloPaySDK.getInstance().payOrder(Activity_GioHang.this, code_donhang.getText().toString().trim(), "giohang://app", new PayOrderListener() {
                        @Override
                        public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                            //
                            //lay ngay thang
                            Date ngayDate = Calendar.getInstance().getTime();
                            SimpleDateFormat ngDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            String ngay = ngDateFormat.format(ngayDate);
                            //lay gio
                            Date gioDate = new Date();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                            String gio = simpleDateFormat.format(gioDate);
                            if (listchk.size() != 0) {
//                                HoaDonDao dao2 = new HoaDonDao(this);
                                boolean check = dao2.addHoaDon(makh, s, ngay, gio);
//                                CTSanPhamDao daoSP = new CTSanPhamDao(this);

                                if (check) {

                                    listhd = dao2.getDSHoaDon();
                                    int mahd = dao2.mahd();

//                                    AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
                                    if (listchk.size() != 0) {
//                        alBuilder
//                                .setTitle("Thanh toán sản phẩm trong giỏ hàng!")
//                                .setIcon(R.drawable.baseline_error_outline_24)
//                                .setMessage("Bạn có chắc chắn muốn Thanh toán sản phẩm")
//                                .setPositiveButton("Có", ((dialogInterface, i) -> {
                                        boolean checksl = false;
                                        for(int i=0;i<listchk.size();i++){
                                            for(GioHang g:list){
                                                if(g.getSl_mua()<=0) checksl = true;
                                            }
                                        }

                                        listchk.forEach(e -> {
                                            list.forEach(e1 -> {
                                                if (e1.getMagiohang() == e) {
                                                    sl = e1.getSl_mua();
                                                    kichco = e1.getKichco();
                                                    mausac = e1.getMausac();
                                                }
                                            });
                                            int masp = dao.getMaCTSP(e);
                                            daoHDCT.themCTHD(mahd, daoSP.getMaCTSP(masp, mausac, kichco), sl);
                                            dao.remove_data(e);
                                        });


//                                })).setNegativeButton("Không", ((dialogInterface, i) -> {
//                                }));
                                    }
//                        alBuilder.show();
                                }
                            }
                            //
                            listchk.clear();
                            tongtien.setText("0 VNĐ");
                            reload();

                            Intent intent1 = new Intent(Activity_GioHang.this, MainActivity.class);
                            intent1.putExtra("gethoadon", ":D");
                            Toast.makeText(Activity_GioHang.this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();

                            dialog.dismiss();
                            startActivity(intent1);
                        }

                        @Override
                        public void onPaymentCanceled(String zpTransToken, String appTransID) {
                            Toast.makeText(Activity_GioHang.this, "Bạn đã hủy thanh toán!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                            Toast.makeText(Activity_GioHang.this, "Lỗi thanh toán!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
        dialog.show();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}
