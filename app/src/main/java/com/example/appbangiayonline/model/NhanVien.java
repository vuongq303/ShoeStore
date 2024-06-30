package com.example.appbangiayonline.model;

public class NhanVien {
    private int manv, chucvu,trangthai;
    private String hoten, taikhoan, matkhau, sdt, email;

    public NhanVien(int manv, String hoten, String taikhoan, String matkhau, String sdt, String email, int chucvu,int trangthai) {
        this.manv = manv;
        this.hoten = hoten;
        this.taikhoan = taikhoan;
        this.matkhau = matkhau;
        this.sdt = sdt;
        this.email = email;
        this.chucvu = chucvu;
        this.trangthai = trangthai;
    }

    public NhanVien() {
    }

    public NhanVien(int manv, int chucvu, String hoten, String sdt, String email,int trangthai,String taikhoan) {
        this.manv = manv;
        this.chucvu = chucvu;
        this.hoten = hoten;
        this.sdt = sdt;
        this.email = email;
        this.trangthai = trangthai;
        this.taikhoan = taikhoan;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }

    public int getManv() {
        return manv;
    }

    public void setManv(int manv) {
        this.manv = manv;
    }

    public int getChucvu() {
        return chucvu;
    }

    public void setChucvu(int chucvu) {
        this.chucvu = chucvu;
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



}


