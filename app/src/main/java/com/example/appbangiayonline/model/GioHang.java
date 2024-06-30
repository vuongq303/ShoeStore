package com.example.appbangiayonline.model;

public class GioHang {
    private int magiohang, masanpham, makhachhang;
    private byte[] hinhanh;
    private String tensp, mausac;
    private int kichco, giasp, soluong;
    //-----------
    private int sl_mua;

    public GioHang(int magiohang, int masanpham, int makhachhang, byte[] hinhanh, String tensp, String mausac, int kichco, int giasp, int soluong, int sl_mua) {
        this.magiohang = magiohang;
        this.masanpham = masanpham;
        this.makhachhang = makhachhang;
        this.hinhanh = hinhanh;
        this.tensp = tensp;
        this.mausac = mausac;
        this.kichco = kichco;
        this.giasp = giasp;
        this.soluong = soluong;
        this.sl_mua = sl_mua;
    }

    public GioHang(int magiohang, int masanpham, int makhachhang, byte[] hinhanh, String tensp, String mausac, int kichco, int giasp, int soluong) {
        this.magiohang = magiohang;
        this.masanpham = masanpham;
        this.makhachhang = makhachhang;
        this.hinhanh = hinhanh;
        this.tensp = tensp;
        this.mausac = mausac;
        this.kichco = kichco;
        this.giasp = giasp;
        this.soluong = soluong;
    }

    public byte[] getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(byte[] hinhanh) {
        this.hinhanh = hinhanh;
    }
    public GioHang() {

    }

    public int getSl_mua() {
        return sl_mua;
    }

    public void setSl_mua(int sl_mua) {
        this.sl_mua = sl_mua;
    }

    public GioHang(int magiohang, int giasp, int sl_mua) {
        this.magiohang = magiohang;
        this.giasp = giasp;
        this.sl_mua = sl_mua;
    }


    //--------------------
    public GioHang(int magiohang, int masanpham, int makhachhang, String tensp, String mausac, int kichco, int giasp, int soluong) {
        this.magiohang = magiohang;
        this.masanpham = masanpham;
        this.makhachhang = makhachhang;
        this.tensp = tensp;
        this.mausac = mausac;
        this.kichco = kichco;
        this.giasp = giasp;
        this.soluong = soluong;
    }

    public int getMagiohang() {
        return magiohang;
    }

    public void setMagiohang(int magiohang) {
        this.magiohang = magiohang;
    }

    public int getMasanpham() {
        return masanpham;
    }

    public void setMasanpham(int masanpham) {
        this.masanpham = masanpham;
    }

    public int getMakhachhang() {
        return makhachhang;
    }

    public void setMakhachhang(int makhachhang) {
        this.makhachhang = makhachhang;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public String getMausac() {
        return mausac;
    }

    public void setMausac(String mausac) {
        this.mausac = mausac;
    }

    public int getKichco() {
        return kichco;
    }

    public void setKichco(int kichco) {
        this.kichco = kichco;
    }

    public int getGiasp() {
        return giasp;
    }

    public void setGiasp(int giasp) {
        this.giasp = giasp;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }
}
