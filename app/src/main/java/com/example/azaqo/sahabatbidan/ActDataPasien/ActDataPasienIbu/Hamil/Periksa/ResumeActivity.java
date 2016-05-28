package com.example.azaqo.sahabatbidan.ActDataPasien.ActDataPasienIbu.Hamil.Periksa;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.example.azaqo.sahabatbidan.R;

import org.joda.time.DateTime;
import org.joda.time.Weeks;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ResumeActivity extends AppCompatActivity {
    HashMap<String,String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);

        JSONObject dataibu = new JSONObject();
        try {
            dataibu = new JSONObject(getPreferences(Context.MODE_PRIVATE).getString("dataibu","{}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent inten = getIntent();
        data = (HashMap<String,String>) inten.getSerializableExtra("datapemeriksaanAll");

        Log.d("PHP", "kesimpulan: "+data);
        //g and p
        TextView tentang  = (TextView) findViewById(R.id.tentang);
        String g = data.get("hamilke");
        String p = data.get("jumlahir");

        int gn = 0,pn = 0;
        if (g.equals("-1")) g = "-";
        else gn = Integer.parseInt(g);
        if (p.equals("-1")) p = "-";
        else pn = Integer.parseInt(p);
        int an = gn - pn - 1;
        String s ="G <sub>"+ g + "</sub> P <sub>"+ p+"</sub> A <sub>"+an+"</sub>";
        tentang.setText(Html.fromHtml(s));

        TextView usia = (TextView) findViewById(R.id.usiakehamilan);
        TextView taksiran = (TextView) findViewById(R.id.taksir);
        try {
            //taksiran
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
            DateTime hpmt = formatter.parseDateTime(data.get("hpmt"));
            DateTime taksir;
            taksir = hpmt.plusDays(7);
            taksir = taksir.minusMonths(3);
            taksir = taksir.plusYears(1);
            taksiran.setText(formatter.print(taksir));
            //usia hamil
            int usiakehamilan = Weeks.weeksBetween(hpmt,new DateTime()).getValue(0);
            usia.setText(usiakehamilan + " minggu");
            Log.d("PHP", "kesimpulan: "+new DateTime());
            Log.d("PHP", "kesimpulan: "+hpmt);
        } catch (IllegalArgumentException e) {
            usia.setText("-");
            usia.setText("-");
        }
        //saran
        TextView saran = (TextView) findViewById(R.id.sarann);
        assert saran != null;
        saran.setText(data.get("saran"));
        //keadaan ibu

        try {
            //resiko rendah
            Boolean low_risk = false;
            if (Integer.parseInt(dataibu.getString("umur"))<16) low_risk = true;
            if (Integer.parseInt(dataibu.getString("umur"))>35 && Integer.parseInt(data.get("hamilke"))==1) low_risk = true;
            if (Integer.parseInt(data.get("jarakhamil"))<2 || Integer.parseInt(data.get("jarakhamil"))<35) low_risk = true;
            if (Integer.parseInt(data.get("jumlahir"))>4) low_risk = true;


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
