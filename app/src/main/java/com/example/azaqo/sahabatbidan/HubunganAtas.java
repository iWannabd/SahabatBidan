package com.example.azaqo.sahabatbidan;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.azaqo.sahabatbidan.ActDataPasien.ActDataPasienIbu.Hamil.DataLengkapIbu;
import com.example.azaqo.sahabatbidan.ActDataPasien.ActDataPasienIbu.Hamil.Periksa.PemeriksaanAmnesa;
import com.example.azaqo.sahabatbidan.ActDataPasien.ActDataPasienIbu.Hamil.DaftarKehamilanFragment;
import com.example.azaqo.sahabatbidan.ActDataPasien.ActDataPasienIbu.Hamil.FragmentDataLengkapIbu;
import com.example.azaqo.sahabatbidan.ActDataPasien.DataPasiens;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by azaqo on 30/04/2016.
 * kelas ini digunakan oleh beberapa kelas lain untuk melakukan http request
 */
public class HubunganAtas extends AsyncTask<String[],Void,Void> {
    Activity act;
    DataLengkapIbu dataLengkapIbu;
    DaftarKehamilanFragment datadataKehamilan;
    DataPasiens dataPasiens;
    DataPasiens.PlaceholderFragment pasien;
    PemeriksaanAmnesa fragment;
    FragmentDataLengkapIbu detil;
    Context context;
    HashMap<String,String> data = null;
    String result = "{}";
    String url;
    String flag = ""; //sebagai penanda kelas digunakan oleh kelas mana
    View view;

    public HubunganAtas(DataLengkapIbu dataLengkapIbu, String url, String flag, DataPasiens dataPasiens) {
        this.dataLengkapIbu = dataLengkapIbu;
        this.url = url;
        this.flag = flag;
        this.dataPasiens = dataPasiens;
    }

    //tambah kehamilan
    public HubunganAtas(DaftarKehamilanFragment datadataKehamilan, String url, String flag,HashMap<String,String> data) {
        this.datadataKehamilan = datadataKehamilan;
        this.url = url;
        this.flag = flag;
        this.data = data;
    }

    //FragmentDataLengkapIbu
    public HubunganAtas(FragmentDataLengkapIbu detil, String url,String flag) {
        this.detil = detil;
        this.url = url;
        this.flag = flag;
    }

    //FragmentDataLengkapIbu update
    public HubunganAtas(FragmentDataLengkapIbu detil, String url,String flag, HashMap<String,String> data) {
        this.detil = detil;
        this.url = url;
        this.flag = flag;
        this.data = data;
    }

    public HubunganAtas(DataPasiens.PlaceholderFragment pasien, String url) {
        this.pasien = pasien;
        this.url = url;
    }

    public HubunganAtas(DaftarKehamilanFragment datadataKehamilan, String url, HashMap<String, String> data) {
        this.datadataKehamilan = datadataKehamilan;
        this.url = url;
        this.data = data;
    }

    public HubunganAtas(String url, DataPasiens dataPasiens) {
        this.url = url;
        this.dataPasiens = dataPasiens;
    }
    //untuk upload data pemeriksaan
    public HubunganAtas(Context context, String url,HashMap<String,String> data,String flag) {
        this.context = context;
        this.url = url;
        this.data = data;
        this.flag = flag;
    }
    //untuk load data pemeriksaan
    public HubunganAtas(HashMap<String, String> data, String url, String flag, View view,PemeriksaanAmnesa fragment) {
        this.data = data;
        this.url = url;
        this.flag = flag;
        this.view = view;
        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String[]... params) {
        Okdeh ok = new Okdeh();
        try {
            if (data!=null) result = ok.doPostRequestData(url,data);
            else result = ok.doPostRequestData(url, params[0],params[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        try {
            if (detil!=null && flag.equals("get")) detil.setDatapasien(result);
            if (detil!=null && flag.equals("update")) Toast.makeText(detil.getActivity(),result,Toast.LENGTH_SHORT).show();
            if (flag.equals("riwayat")) Toast.makeText(context,result,Toast.LENGTH_LONG).show();
            if (fragment!=null && flag.equals("loaddata")) fragment.setDataRiwayatHamil(view,result);
            if (fragment!=null && flag.equals("penyakit")) fragment.setRiwayatPenyakit(view,result);
            if (fragment!=null && flag.equals("keluhan")) fragment.setDataKeluahan(view,result);
            if (fragment!=null && flag.equals("umumperiksa")) fragment.setDataPemeriksaanUmum(view,result);
            if (pasien!=null) pasien.setDatapasien(result);
            if (datadataKehamilan!=null) datadataKehamilan.setDatadatakehamilan(result);
            if (datadataKehamilan!=null && flag.equals("tambah")) Toast.makeText(datadataKehamilan.getActivity(),result,Toast.LENGTH_SHORT).show();
            if (dataPasiens !=null) {
                Fragment page = dataPasiens.getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + dataPasiens.mViewPager.getCurrentItem());
                if (dataPasiens.mViewPager.getCurrentItem() == 0 && page != null) ((DataPasiens.PlaceholderFragment)page).setDatapasien(result);
            }
            Log.d("PHP", "onPostExecute:"+result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
