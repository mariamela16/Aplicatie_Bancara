package com.example.frag;

public class HelperDepo {
    String nume;
    int sold;
    int perioada;
    int ratad;
    String data;
    String id;
    String num;

    public HelperDepo(String nume, int perioada, int ratad, int sold, String data, String id, String num) {
        this.nume = nume;
        this.sold = sold;
        this.perioada = perioada;
        this.ratad = ratad;
        this.data = data;
        this.id = id;
        this.num = num;

    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public int getPerioada() {
        return perioada;
    }

    public void setPerioada(int perioada) {
        this.perioada = perioada;
    }

    public int getRatad() {
        return ratad;
    }

    public void setRatad(int ratad) {
        this.ratad = ratad;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
