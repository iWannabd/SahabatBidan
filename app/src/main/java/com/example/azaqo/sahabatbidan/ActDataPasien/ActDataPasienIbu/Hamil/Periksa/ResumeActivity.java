package com.example.azaqo.sahabatbidan.ActDataPasien.ActDataPasienIbu.Hamil.Periksa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ResumeActivity extends AppCompatActivity {
    HashMap<String,String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);

        TextView keadaan_ibu = (TextView) findViewById(R.id.ibu);

        JSONObject dataibu = new JSONObject();
        try {
            SharedPreferences sp = getSharedPreferences("datadata",Context.MODE_PRIVATE);
            String json = sp.getString("dataibu","{}");
            dataibu = new JSONObject(json);
            Log.d("PHP", "onCreate: dataibu "+json);
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

        DateTime taksir = new DateTime();
        TextView usia = (TextView) findViewById(R.id.usiakehamilan);
        TextView taksiran = (TextView) findViewById(R.id.taksir);
        try {
            //taksiran
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
            DateTime hpmt = formatter.parseDateTime(data.get("hpmt"));
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
            Boolean low_risk = false;
            Boolean high_risk = false;
            Boolean very_high_risk = false;

            int umur = Integer.parseInt(dataibu.getString("umur"));
            int hamilke = Integer.parseInt(data.get("hamilke"));
            int jarakhamil = Integer.parseInt(data.get("jarakhamil"));
            int jumlahlahir = Integer.parseInt(data.get("jumlahir"));
            int tinggibadan = Integer.parseInt(data.get("tinggibadan"));

            String raw = data.get("riwayatpenyakit");
            raw = raw.substring(1, raw.length() - 1);
            List<String> riwayatpenyakit = Arrays.asList(raw.split("\\s*,\\s*"));

            raw = data.get("penyakitturun");
            raw = raw.substring(1, raw.length() - 1);
            List<String> riwayatpenyakitx = Arrays.asList(raw.split("\\s*,\\s*"));

            raw = data.get("tris3");
            raw = raw.substring(1, raw.length() - 1);
            List<String> tris3 = Arrays.asList(raw.split("\\s*,\\s*"));

            //resiko rendah
            if (umur<16) low_risk = true;
            if (umur>35 && hamilke==1) low_risk = true;
            if (jarakhamil<2 || jarakhamil<35) low_risk = true;
            if (jumlahlahir>=4) low_risk = true;
            if (umur>35) low_risk = true;
            if (tinggibadan<=145) low_risk = true;
            if (an>=1) low_risk = true;
            if (!data.get("vaccum").equals("-1")) low_risk = true;
            if (!data.get("sesar").equals("-1")) low_risk = true;
            //resiko sangat tinggi
            if (riwayatpenyakit.contains("6")) high_risk = true;
            if (riwayatpenyakit.contains("5")) high_risk = true;
            if (riwayatpenyakit.contains("7")) high_risk = true;
            if (riwayatpenyakit.contains("3")) high_risk = true;
            if (riwayatpenyakit.contains("0")) high_risk = true;

            if (tris3.contains("11")) high_risk = true;
            if (tris3.contains("12")) high_risk = true;
            if (tris3.contains("13")) high_risk = true;
            if (tris3.contains("14")) high_risk = true;
            if (taksir.isBeforeNow()) high_risk = true;
            Log.d("PHP", "onCreate: taksirafternow "+ taksir.isAfterNow());
            if (data.get("presentasijanin").equals("2")) high_risk = true;
            if (data.get("presentasijanin").equals("3")) high_risk = true;

            if (tris3.contains("15")) very_high_risk = true;
            if (tris3.contains("10")) very_high_risk = true;

            if (low_risk) keadaan_ibu.setText("Resiko rendah");
            if (high_risk) keadaan_ibu.setText("Resiko tinggi");
            if (very_high_risk) keadaan_ibu.setText("Resiko sangat tinggi");

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
