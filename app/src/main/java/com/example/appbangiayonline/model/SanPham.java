package com.example.appbangiayonline.model;

import java.io.Serializable;

public class SanPham implements Serializable {
    private int masanpham;
    private String tensanpham;
    private String hang;
    private String trangthai;

    private byte[] image;


    public SanPham(int masanpham, String tensanpham, String hang, String trangthai, byte[] hinhanh) {
        this.masanpham = masanpham;
        this.tensanpham = tensanpham;
        this.hang = hang;
        this.trangthai = trangthai;
        this.image = hinhanh;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public SanPham() {
    }

    public String getHang() {
        return hang;
    }

    public void setHang(String hang) {
        this.hang = hang;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }

    public int getMasanpham() {
        return masanpham;
    }

    public void setMasanpham(int masanpham) {
        this.masanpham = masanpham;
    }

    public String getTensanpham() {
        return tensanpham;
    }

    public void setTensanpham(String tensanpham) {
        this.tensanpham = tensanpham;
    }
}
