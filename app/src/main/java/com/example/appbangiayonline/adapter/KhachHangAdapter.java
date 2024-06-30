package com.example.appbangiayonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbangiayonline.R;
import com.example.appbangiayonline.model.KhachHang;
import com.example.appbangiayonline.model.SanPham;

import java.util.ArrayList;

public class KhachHangAdapter extends RecyclerView.Adapter<KhachHangAdapter.ViewHolder>{
    private Context context;
    private  ArrayList<KhachHang> list;

    public KhachHangAdapter(Context context, ArrayList<KhachHang> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_khach_hang, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KhachHang kh = list.get(position);
        holder.txtTen.setText("Tên : "+kh.getHoten());
        holder.txtSDT.setText("SĐT : "+kh.getSdt());
        holder.txtEmail.setText("Email : "+kh.getEmail());
        holder.txtDiaChi.setText("Địa chỉ :"+kh.getDiachi());
        holder.btnSua.setVisibility(View.GONE);
        holder.btnXoa.setVisibility(View.GONE);
        holder.txtTrangthai.setVisibility(View.GONE);
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
}
