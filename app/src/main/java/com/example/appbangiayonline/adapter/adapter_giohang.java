package com.example.appbangiayonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbangiayonline.R;
import com.example.appbangiayonline.activity.Activity_GioHang;
import com.example.appbangiayonline.convert.ConvertImage;
import com.example.appbangiayonline.dao.HoaDonCT_Dao;

import com.example.appbangiayonline.model.GioHang;

import java.util.ArrayList;
import java.util.List;

public class adapter_giohang extends RecyclerView.Adapter<adapter_giohang.rcv_holder> {
    List<GioHang> list;
    Activity_GioHang context;
    ArrayList<GioHang> list2;
    private gioHangInterface gioHangInterface;

    int sl = 1;

    public adapter_giohang(List<GioHang> list, Activity_GioHang context) {
        this.list = list;
        this.context = context;
    }

    public class rcv_holder extends RecyclerView.ViewHolder {
        CheckBox chck_item;
        ImageView img_item;
        TextView tensp_item, mausac_item, giasp_item, sl_sp_item, kichco_item;

        ImageButton tang_item, giam_item;

        public rcv_holder(@NonNull View itemView) {
            super(itemView);
            chck_item = itemView.findViewById(R.id.chck_item_giohang);
            img_item = itemView.findViewById(R.id.img_item_giohang);
            giasp_item = itemView.findViewById(R.id.txt_giasp_giohang);
            tensp_item = itemView.findViewById(R.id.txt_tensp_giohang);
            mausac_item = itemView.findViewById(R.id.txt_mausac_item_giohang);
            giam_item = itemView.findViewById(R.id.btn_giam_giohang);
            tang_item = itemView.findViewById(R.id.btn_tang_giohang);
            sl_sp_item = itemView.findViewById(R.id.sl_sp_item_giohang);
            kichco_item = itemView.findViewById(R.id.txt_kichco_giohang);
        }
    }

    @NonNull
    @Override
    public rcv_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new rcv_holder(LayoutInflater.from(context).inflate(R.layout.item_giohang, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull rcv_holder holder, int position) {
        GioHang gioHang = list.get(position);

        holder.tensp_item.setText(gioHang.getTensp());
        holder.mausac_item.setText("Màu: " + gioHang.getMausac());
        holder.giasp_item.setText(gioHang.getGiasp() + "");
        holder.sl_sp_item.setText(gioHang.getSl_mua() + "");
        holder.img_item.setImageBitmap(ConvertImage.ByteToBitmap(gioHang.getHinhanh()));
        holder.kichco_item.setText("Size: " + gioHang.getKichco());

        holder.giam_item.setOnClickListener(view -> {
            if (gioHang.getSl_mua() > 1) {
                gioHang.setSl_mua(gioHang.getSl_mua() - 1);
                holder.sl_sp_item.setText(gioHang.getSl_mua() + "");
                context.reload_tongtien();
            }
        });

        holder.tang_item.setOnClickListener(view -> {
            gioHang.setSl_mua(gioHang.getSl_mua() + 1);
            holder.sl_sp_item.setText(gioHang.getSl_mua() + "");
            context.reload_tongtien();
        });

        holder.chck_item.setOnClickListener(view -> {
            if (holder.chck_item.isChecked()) {
                context.add_chck(gioHang.getMagiohang());
            }
            if (!holder.chck_item.isChecked()) {
                context.rm_chck(gioHang.getMagiohang());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
