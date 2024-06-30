package com.example.appbangiayonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbangiayonline.R;
import com.example.appbangiayonline.model.CTSanPham;

import java.util.ArrayList;

public class HoaDonCT_Adapter extends RecyclerView.Adapter<HoaDonCT_Adapter.ViewHolder>{
    private Context context;
    private final ArrayList<CTSanPham> list;

    public HoaDonCT_Adapter(Context context, ArrayList<CTSanPham> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HoaDonCT_Adapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp_ct_hoadon, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtTenSP.setText("Tên SP: "+list.get(position).getTensanpham());
        holder.txtMauSac.setText("Màu sắc: "+ list.get(position).getTenmausac());
        holder.txtKichCo.setText("Kích cỡ: "+list.get(position).getKichco());
        holder.txtGiaBan.setText("Giá: "+ list.get(position).getGia());
        holder.txtSoLuongMua.setText("Số lượng mua: "+list.get(position).getSoluong());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenSP,txtMauSac,txtKichCo,txtSoLuongMua,txtGiaBan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenSP = itemView.findViewById(R.id.txtTenSP_item_HDCT);
            txtMauSac = itemView.findViewById(R.id.txtMauSac_item_HDCT);
            txtKichCo = itemView.findViewById(R.id.txtKichCo_item_HDCT);
            txtGiaBan = itemView.findViewById(R.id.txtGiaSP_item_HDCT);
            txtSoLuongMua = itemView.findViewById(R.id.txtSoLuongMua_item_HDCT);
        }
    }
}
