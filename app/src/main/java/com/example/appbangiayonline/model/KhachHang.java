package com.example.appbangiayonline.model;

public class KhachHang {
    private int makh;
    private String hoten, taikhoan, matkhau, sdt, email, diachi;

    public KhachHang(int makh, String hoten, String taikhoan, String matkhau, String sdt, String email, String diachi) {
        this.makh = makh;
        this.hoten = hoten;
        this.taikhoan = taikhoan;
        this.matkhau = matkhau;
        this.sdt = sdt;
        this.email = email;
        this.diachi = diachi;
    }


    public KhachHang(int makh) {
        this.makh = makh;
    }

    public KhachHang() {
    }

    public KhachHang(int makh, String hoten, String sdt, String email, String diachi) {
        this.makh = makh;
        this.hoten = hoten;
        this.sdt = sdt;
        this.email = email;
        this.diachi = diachi;
    }

    public int getMakh() {
        return makh;
    }

    public void setMakh(int makh) {
        this.makh = makh;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getTaikhoan() {
        return taikhoan;
    }

    public void setTaikhoan(String taikhoan) {
        this.taikhoan = taikhoan;
    }

    public String getMatkhau() {
        return matkhau;
    }

    public void setMatkhau(String matkhau) {
        this.matkhau = matkhau;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }
}
