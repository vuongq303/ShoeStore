package com.example.appbangiayonline.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbangiayonline.R;
import com.example.appbangiayonline.dao.CTSanPhamDao;
import com.example.appbangiayonline.model.CTSanPham;

import java.util.ArrayList;

public class CTSanPhamAdapter extends RecyclerView.Adapter<CTSanPhamAdapter.Viewholder> {
    private Context context;
    private final ArrayList<CTSanPham> list;

    public CTSanPhamAdapter(Context context, ArrayList<CTSanPham> list) {
        this.context = context;
        this.list = list;
    }
    CTSanPham ctsp;
    int gia,soluong,mactsp;
    CTSanPhamDao dao;

    @NonNull
    @Override
    public CTSanPhamAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ctsanpham, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CTSanPhamAdapter.Viewholder holder, @SuppressLint("RecyclerView") int position) {
        ctsp = list.get(position);
        holder.ma.setText("Mã chi tiết sản phẩm : " + Integer.toString(list.get(position).getMactsanpham()));
        holder.tensp.setText("Tên sản phẩm : " +list.get(position).getTensanpham());
        holder.tenmau.setText("Màu : " +list.get(position).getTenmausac());
        holder.kichco.setText("Kích cỡ : " +Integer.toString(list.get(position).getKichco()));
        holder.soluong.setText("Số lượng : " +Integer.toString(list.get(position).getSoluong()));
        holder.gia.setText("Giá sản phẩm : " +Integer.toString(list.get(position).getGia()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ctsp = list.get(position);
                gia = ctsp.getGia();
                soluong = ctsp.getSoluong();
                mactsp = ctsp.getMactsanpham();
                themSL(mactsp,gia,soluong);

            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView ma, tensp, tenmau, kichco, soluong, gia;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            ma = itemView.findViewById(R.id.mactsanpham_ctsanpham);
            tensp = itemView.findViewById(R.id.tensanpham_ctsanpham);
            tenmau = itemView.findViewById(R.id.tenmausac_ctsanpham);
            kichco = itemView.findViewById(R.id.sokicco_ctsanpham);
            soluong = itemView.findViewById(R.id.soluongsanpham_ctsanpham);
            gia = itemView.findViewById(R.id.giasanpham_ctsanpham);
        }
    }
    private void themSL(int mactsp,int gia,int soluong){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.them_sl_ctsanpham, null);
        builder.setView(view);

        TextView txtGia = view.findViewById(R.id.mactsanpham_them_sl);
        TextView txtSoluong = view.findViewById(R.id.soluongthem_sl);
        Button btnUpdate = view.findViewById(R.id.btnUpdate);
        Button btnHuy = view.findViewById(R.id.btnHuy);

        txtSoluong.setText(soluong+"");
        txtGia.setText(gia+"");
        dao = new CTSanPhamDao(context);
        Dialog dialog = builder.create();
        dialog.show();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            int check = 0,gia2 = 0, soluong2 = 0;
            @Override
            public void onClick(View v) {


                if(txtGia.getText().toString().trim().equals("")||txtSoluong.getText().toString().trim().equals("")){
                    check++;
                    Toast.makeText(context, "Khong de trong du lieu", Toast.LENGTH_SHORT).show();
                }
                if (check==0){
                    gia2 = Integer.parseInt(txtGia.getText().toString());
                    soluong2 = Integer.parseInt(txtSoluong.getText().toString());
                    if(gia2 < 1 || soluong2 < 1){
                        Toast.makeText(context, "khong duoc nho hon 1", Toast.LENGTH_SHORT).show();
                    }else {
                        boolean check = dao.updateGiaAndSL(mactsp,gia2,soluong2);
                        if(check){
                            Toast.makeText(context, "Cap nhat thanh cong", Toast.LENGTH_SHORT).show();
                            list.clear();
                            list.addAll(dao.getDSCTSP(ctsp.getTensanpham()));
                            notifyDataSetChanged();
                            dialog.dismiss();


                        }else {
                            Toast.makeText(context, "Cap nhat that bai", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
