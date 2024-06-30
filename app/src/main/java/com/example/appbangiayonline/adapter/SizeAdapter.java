package com.example.appbangiayonline.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbangiayonline.R;
import com.example.appbangiayonline.activity.OnItemClickMauSize;
import com.example.appbangiayonline.model.CTSanPham;

import java.util.ArrayList;
import java.util.HashMap;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.Viewholder> {
    private Context context;
    private final ArrayList<CTSanPham> list;
    private OnItemClickMauSize onItemClickMauSize;

    private HashMap<Integer, Integer> sizeCountMap = new HashMap<>();

    public SizeAdapter(Context context, ArrayList<CTSanPham> list, OnItemClickMauSize onItemClickMauSize) {
        this.context = context;
        this.list = list;
        this.onItemClickMauSize = onItemClickMauSize;
    }

    @NonNull
    @Override
    public SizeAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kichco, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SizeAdapter.Viewholder holder, int position) {
        CTSanPham ctSanPham = list.get(position);
        int kichco = list.get(position).getKichco();
        int dem = sizeCountMap.getOrDefault(kichco, 0);

        if (dem == 0){
            holder.kichco.setText(String.valueOf(kichco));
        } else {
            holder.kichco.setVisibility(View.GONE);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(0, 0);
            holder.itemView.setLayoutParams(params);
        }

        sizeCountMap.put(kichco, dem + 1);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickMauSize.onItemClick(ctSanPham);
            }
        });
    }

    private int getCountForSize(int kichco) {
        return sizeCountMap.getOrDefault(kichco, 0);
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView kichco;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            kichco = itemView.findViewById(R.id.kiccosanpham_ctsanpham);
        }
    }
}
