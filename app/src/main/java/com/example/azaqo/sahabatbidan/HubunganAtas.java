package com.example.azaqo.sahabatbidan;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by azaqo on 30/04/2016.
 * kelas ini digunakan oleh beberapa kelas lain untuk melakukan http request
 */
public class HubunganAtas extends AsyncTask<String[],Void,Void> {
    DataPasien activity;
    DataPasiens activ;
    DataPasiens.PlaceholderFragment pasien;
    PemeriksaanAmnesa fragment;
    FragmentDataLengkapIbu detil;
    Context context;
    HashMap<String,String> data = null;
    String result = "{}";
    String url;
    String flag = ""; //sebagai penanda kelas digunakan oleh kelas mana
    View view = null;

    public HubunganAtas(DataPasien activity, Context context, String url) {
        this.activity = activity;
        this.context = context;
        this.url = url;
        detil = null;
    }

    public HubunganAtas(DataPasiens.PlaceholderFragment pasien, String url) {
        this.pasien = pasien;
        this.url = url;
    }

    public HubunganAtas(String url, DataPasiens activ) {
        this.url = url;
        this.activ = activ;
    }

    public HubunganAtas(DataPasien activity, String url, String flag) {
        this.activity = activity;
        this.url = url;
        this.flag = flag;
    }

    public HubunganAtas(FragmentDataLengkapIbu detil, Context context, String url) {
        this.detil = detil;
        this.context = context;
        this.url = url;
        activity = null;
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
            if (activity!=null && !flag.equals("pairing")) activity.setDatapasien(result);
            if (detil!=null) detil.setDatapasien(result);
            if (flag.equals("riwayat")) Toast.makeText(context,result,Toast.LENGTH_LONG).show();
            if (fragment!=null && flag.equals("loaddata")) fragment.setDataRiwayatHamil(view,result);
            if (fragment!=null && flag.equals("penyakit")) fragment.setRiwayatPenyakit(view,result);
            if (fragment!=null && flag.equals("keluhan")) fragment.setDataKeluahan(view,result);
            if (fragment!=null && flag.equals("umumperiksa")) fragment.setDataPemeriksaanUmum(view,result);
            if (flag.equals("pairing")) Toast.makeText(activity,result,Toast.LENGTH_SHORT).show();
            if (pasien!=null) pasien.setDatapasien(result);
            if (activ!=null) {
                Fragment page = activ.getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + activ.mViewPager.getCurrentItem());
                if (activ.mViewPager.getCurrentItem() == 0 && page != null) ((DataPasiens.PlaceholderFragment)page).setDatapasien(result);
            }
            Log.d("PHP", "onPostExecute:"+result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
