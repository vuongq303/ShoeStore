package com.example.appbangiayonline.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbangiayonline.R;
import com.example.appbangiayonline.activity.DangKi;
import com.example.appbangiayonline.activity.DangNhap;
import com.example.appbangiayonline.dao.NhanVien_KhachHang_Dao;
import com.example.appbangiayonline.model.KhachHang;
import com.example.appbangiayonline.model.NhanVien;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class NhanVienAdapter extends RecyclerView.Adapter<NhanVienAdapter.ViewHolder>{
    private Context context;
    private ArrayList<NhanVien> list;
    private NhanVien_KhachHang_Dao dao;
    private String ten,sdt,email;

    public NhanVienAdapter(Context context, ArrayList<NhanVien> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NhanVienAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_khach_hang, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        dao = new NhanVien_KhachHang_Dao(context);
        NhanVien kh = list.get(position);

        holder.txtTen.setText("Tên : "+kh.getHoten());
        holder.txtSDT.setText("SĐT : "+kh.getSdt());
        holder.txtEmail.setText("Email : "+kh.getEmail());
        String chucVu = kh.getChucvu()==0 ? "Nhân viên" : "Admin";
        holder.txtDiaChi.setText("Chức vụ : "+ chucVu);
        String trangThai = kh.getTrangthai() != 0 ? "Đã nghỉ":"Còn làm";


        holder.txtTrangthai.setText("Trạng thái: "+trangThai);
        SharedPreferences sharedPreferences = context.getSharedPreferences("admin", MODE_PRIVATE);
        String username = sharedPreferences.getString("taikhoan", "");
        NhanVien nv2 = dao.getThongTinNhanVien(username);
        if(nv2.getChucvu()==0){
            holder.btnSua.setVisibility(View.GONE);
            holder.btnXoa.setVisibility(View.GONE);
        }

        holder.btnXoa.setOnClickListener(new View.OnClickListener() {
            int trangthai = 0;
            @Override
            public void onClick(View v) {
                if(kh.getTrangthai()==0){
                    trangthai = 1;
                }else {
                    trangthai = 0;
                }
                if(dao.xoaNhanVien(kh.getManv(),trangthai)){
                    Toast.makeText(context, "Đã xóa nhân viên: "+kh.getHoten(), Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = context.getSharedPreferences("admin", MODE_PRIVATE);
                    String username = sharedPreferences.getString("taikhoan", "");
                    kh.getTaikhoan();

                    if(kh.getTaikhoan().equals(username)){
                        Intent intent = new Intent(context, DangNhap.class);
                        context.startActivity(intent);
                    }
                    list.clear();
                    list = dao.getList_NV();
                    notifyDataSetChanged();
                }

            }
        });
        holder.btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ten = list.get(position).getHoten(); sdt = list.get(position).getSdt(); email = list.get(position).getEmail();
                showDiaLogUpdateNV(kh.getManv(),ten,sdt,email);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTen,txtSDT,txtEmail,txtDiaChi,txtTrangthai;
        ImageView btnSua,btnXoa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTrangthai = itemView.findViewById(R.id.txtTrangthai_KH);
            txtEmail = itemView.findViewById(R.id.txtEmail_KH);
            txtSDT = itemView.findViewById(R.id.txtSDT_KH);
            txtTen = itemView.findViewById(R.id.txtTenKH);
            txtDiaChi = itemView.findViewById(R.id.txtDiaChi_KH);
            btnXoa = itemView.findViewById(R.id.img_btn_delete_KH);
            btnSua = itemView.findViewById(R.id.img_btn_update_KH);
        }
    }
    private void showDiaLogUpdateNV(int id,String ten,String sdt,String email){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_update_nv,null);
        builder.setView(view);
        Spinner spinner = view.findViewById(R.id.spnChucVu);
        ArrayList<String> listCV = new ArrayList<>();
        listCV.add("Nhân viên");listCV.add("Admin");
        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1,listCV);
        spinner.setAdapter(adapter);

        TextInputEditText edtTen = view.findViewById(R.id.input_hoten_sua_NV);
        TextInputEditText edtEmail = view.findViewById(R.id.input_email_sua_NV);
        TextInputEditText edtSdt = view.findViewById(R.id.input_sdt__sua_NV);
        Button btnHuy = view.findViewById(R.id.btnHuysuaNV);
        Button btnUpdate = view.findViewById(R.id.btnsuaNV);

        AlertDialog dialog = builder.create();
        edtTen.setText(ten); edtEmail.setText(email); edtSdt.setText(sdt);
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int chucVu = spinner.getSelectedItemPosition();
                if(isValid(id,edtTen.getText().toString(),edtSdt.getText().toString(),edtEmail.getText().toString(),chucVu)){
                    Toast.makeText(context, "Sửa thành công ", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });
        dialog.show();

    }
    public boolean isValid(int id,String hoten,String sdt,String email,int chucvu) {
        if(hoten.equals("")||sdt.equals("")||email.equals("")){

            Toast.makeText(context, "Không để trống dữ liệu", Toast.LENGTH_SHORT).show();
            return false;
        }else {
//                    String hoten, String taikhoan, String matkhau, String email, String sdt
                boolean check = dao.updateNV(id, hoten, sdt, email, chucvu);
                if (check){
                    list.clear();
                    list = dao.getList_NV();
                    notifyDataSetChanged();
                    return true;
                }
                return false;
        }


    }
}
