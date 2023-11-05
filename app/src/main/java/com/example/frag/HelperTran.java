package com.example.frag;

public class HelperTran {
    String nume, descriere,sold,data;



    public String getNume() {
        return nume;
    }

    public String getDescriere() {
        return descriere;
    }

    public String getSold() {
        return sold;
    }

    public String getData() {
        return data;
    }

    public HelperTran(String nume, String descriere, String sold, String data) {
        this.nume = nume;
        this.descriere = descriere;
        this.sold = sold;
        this.data = data;
    }
}
