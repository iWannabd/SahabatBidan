package com.example.azaqo.sahabatbidan.ActDataPasien.ActDataPasienIbu.Hamil.Periksa;

import java.util.HashMap;


public interface PemeriksaanListener {
    public void kumpulinData(HashMap<String,String> data);
    public void uploadData();
    public void uploadDataBaru();
    public void geser();
}
