package com.example.appbangiayonline.zalo_pay;

import android.content.DialogInterface;
import android.widget.Toast;

import com.example.appbangiayonline.activity.Activity_GioHang;
import com.example.appbangiayonline.activity.ManHinh_CTSanPham;
import com.example.appbangiayonline.zalo_pay.Api.CreateOrder;

import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;

import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class ZaloPay {
    public static String createHoadon(String giatien) {
        CreateOrder orderApi = new CreateOrder();
        String code_values = "";
        try {
            JSONObject data = orderApi.createOrder(giatien);
            String code = data.getString("return_code");

            if (code.equals("1")) {
                code_values = data.getString("zp_trans_token");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code_values;
    }
}
