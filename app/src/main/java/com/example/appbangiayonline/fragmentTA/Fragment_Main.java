package com.example.appbangiayonline.fragmentTA;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.appbangiayonline.R;
import com.example.appbangiayonline.adapter.HoaDonAdapter;
import com.example.appbangiayonline.adapter.SanPhamAdapter;
import com.example.appbangiayonline.adapter.SliderAdapter;
import com.example.appbangiayonline.dao.SanPhamDao;
import com.example.appbangiayonline.model.SanPham;

import java.util.ArrayList;
import java.util.Arrays;

public class Fragment_Main extends Fragment {

    ViewPager viewPager;
    SliderAdapter sliderAdapter;
    int index = 0;
    int images[] = {R.drawable.n4, R.drawable.n1, R.drawable.n2, R.drawable.n3};
    ArrayList<SanPham> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__main, container, false);

        RecyclerView rc_main = view.findViewById(R.id.rc_main);
        rc_main.setLayoutManager(new LinearLayoutManager(getContext()));
        SanPhamDao dao = new SanPhamDao(getContext());
        list = new ArrayList<>();
        list = dao.getListSanPham();
        SanPhamAdapter adapter = new SanPhamAdapter(getContext(), list);
        rc_main.setAdapter(adapter);

        //Tìm kiếm
        SearchView searchView = view.findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<SanPham> newSP = new ArrayList<>();
                for (SanPham sn : list){
                    if(sn.getTensanpham().toLowerCase().contains(newText.toLowerCase())){
                        newSP.add(sn);
                    }
                }
                SanPhamAdapter adaptern = new SanPhamAdapter(getContext(), newSP);
                rc_main.setAdapter(adaptern);
                return false;
            }
        });


        viewPager = view.findViewById(R.id.viewPager);
        sliderAdapter = new SliderAdapter(getContext(), images);
        viewPager.setAdapter(sliderAdapter);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (index == images.length) {
                    index = 0;
                }
                viewPager.setCurrentItem(index++, true);
                handler.postDelayed(this, 1000);
            }
        }, 1000);
        return view;
    }
}