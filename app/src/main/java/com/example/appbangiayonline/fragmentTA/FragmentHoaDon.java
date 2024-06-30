package com.example.appbangiayonline.fragmentTA;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbangiayonline.R;
import com.example.appbangiayonline.adapter.HoaDonAdapter;
import com.example.appbangiayonline.dao.HoaDonDao;
import com.example.appbangiayonline.model.HoaDon;

import java.util.ArrayList;

public class FragmentHoaDon extends Fragment {
    RecyclerView recyclerView;
    HoaDonAdapter adapter;
    HoaDonDao dao;
    ArrayList<HoaDon> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hoa_don, container, false);
        recyclerView = view.findViewById(R.id.rchoadon);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        dao = new HoaDonDao(getContext());
        reload();
        return view;
    }

    public void reload() {
        list = dao.getDSHoaDon();
        adapter = new HoaDonAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);
    }
}
