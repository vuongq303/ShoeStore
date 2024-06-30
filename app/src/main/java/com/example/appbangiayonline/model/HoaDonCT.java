package com.example.appbangiayonline.model;

public class HoaDonCT {
    private int mahd;
    private int mactsp;
    private int solm;

    public int getMactsp() {
        return mactsp;
    }

    public void setMactsp(int mactsp) {
        this.mactsp = mactsp;
    }

    public int getMahd() {
        return mahd;
    }

    public void setMahd(int mahd) {
        this.mahd = mahd;
    }

    public int getSolm() {
        return solm;
    }

    public void setSolm(int solm) {
        this.solm = solm;
    }

    public HoaDonCT(int mahd, int mactsp, int solm) {
        this.mahd = mahd;
        this.mactsp = mactsp;
        this.solm = solm;
    }
}
