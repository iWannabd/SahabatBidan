package com.example.azaqo.sahabatbidan.Pengingat;

import java.io.Serializable;

/**
 * Created by azaqo on 6/23/2016.
 */
public class ReminderCard implements Serializable {
    String nama;
    String resiko;
    String term;
    String kunjungan;

    int usiakehamilan;
    String idkehamilan;

    String selesai;
    String dropout;
    String carapersalinan;
    String tanggalpersalinan;
    String status;
    String rujuk;
    String keadaanibu;
    String keadaanbayi;
    String catatan;
    String jumlahkunjungan;

    public ReminderCard(String nama, String resiko, String term, String kunjungan, int usiakehamilan, String idkehamilan, String selesai, String dropout, String carapersalinan, String tanggalpersalinan, String status, String rujuk, String keadaanibu, String keadaanbayi, String catatan, String jumlahkunjungan) {
        this.nama = nama;
        this.resiko = resiko;
        this.term = term;
        this.kunjungan = kunjungan;
        this.usiakehamilan = usiakehamilan;
        this.idkehamilan = idkehamilan;
        this.selesai = selesai;
        this.dropout = dropout;
        this.carapersalinan = carapersalinan;
        this.tanggalpersalinan = tanggalpersalinan;
        this.status = status;
        this.rujuk = rujuk;
        this.keadaanibu = keadaanibu;
        this.keadaanbayi = keadaanbayi;
        this.catatan = catatan;
        this.jumlahkunjungan = jumlahkunjungan;
    }
}
