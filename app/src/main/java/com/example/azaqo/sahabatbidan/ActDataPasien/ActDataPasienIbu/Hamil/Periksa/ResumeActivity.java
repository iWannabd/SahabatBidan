package com.example.azaqo.sahabatbidan.ActDataPasien.ActDataPasienIbu.Hamil.Periksa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.azaqo.sahabatbidan.R;

import org.joda.time.DateTime;
import org.joda.time.Weeks;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ResumeActivity extends AppCompatActivity {
    HashMap<String,String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);
        String keseluruhan = "";
        //ibu
        TextView keadaan_ibu = (TextView) findViewById(R.id.ibu);
        TextView analisahb = (TextView) findViewById(R.id.hemo);
        TextView gizi = (TextView) findViewById(R.id.gizi);
        //janin
        TextView gerakjanin = (TextView) findViewById(R.id.gerak);
        TextView taksiranberatjanin = (TextView) findViewById(R.id.beratjanin);
        TextView posisijan = (TextView) findViewById(R.id.posisijanin);

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
        String g = data.get("hamilke");
        String p = data.get("jumlahir");
        // penentuan g p dan a
        int gn = 0,pn = 0;
        if (g.equals("-1")) g = "-";
        else gn = Integer.parseInt(g);
        if (p.equals("-1")) p = "-";
        else pn = Integer.parseInt(p);
        int an = gn - pn - 1;
        String s ="G <sub>"+ g + "</sub> P <sub>"+ p+"</sub> A <sub>"+an+"</sub>";

        DateTime taksir = new DateTime();

        int prek = -1; //-1 tidak terdeteksi, 0 preklamsi ringan, 1 preklamsi berat, 2 eklamsi
        double sistol = 0;
        double diastol = 0;
        double BB = 0;
        double TB = 1;

        String hasilanalisahb = "tidak diperiksa";

        try {
            //keterangan pemeriksa
            TextView pemeriksa = (TextView) findViewById(R.id.pemeriksa);
            pemeriksa.setText(dataibu.get("nama")+" diperiksa oleh "+data.get("usernamebidan"));
            keseluruhan += "pada "+data.get("tanggalperiksa")+"\n";
            keseluruhan += Html.fromHtml(s)+"\n";
            //daftar tindakan
            String daftartindakan = "";
            //daftar tindakan pemberian imunisasi TT1..5
            String im = data.get("imunTT");
            im = im.substring(1,im.length()-1);
            List<String> imu = Arrays.asList(im.split("\\s*,\\s*"));
            String pemberian = "Pemberian ";
            if (!im.equals("")){
                for (String i:imu) {
                    int ke = Integer.parseInt(i)+1;
                    pemberian += "Imunisasi TT" + ke +", ";
                }
                daftartindakan += pemberian;
            }
            //imuni lain
            if (!data.get("imunLain").equals("")) daftartindakan += "Pemberian "+data.get("imunLain")+"\n";
            //tindak lain
            if (!data.get("tindaklain").equals("")) daftartindakan += "Dilakukan "+data.get("tindaklain")+"\n";
            if (!daftartindakan.equals("")) {keseluruhan += daftartindakan;}
            //taksiran
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
            DateTime hpmt = formatter.parseDateTime(data.get("hpmt"));
            taksir = hpmt.plusDays(7);
            taksir = taksir.minusMonths(3);
            taksir = taksir.plusYears(1);

            //usia hamil
            final int usiakehamilan = Weeks.weeksBetween(hpmt,new DateTime()).getValue(0);
            keseluruhan += "Usia kehamilan "+usiakehamilan + " minggu\n";
            keseluruhan += "Taksiran tanggal persalinan "+formatter.print(taksir)+"\n";
            TextView all = (TextView) findViewById(R.id.keseluruhan);
            all.setText(keseluruhan);
            Log.d("PHP", "kesimpulan: "+new DateTime());
            Log.d("PHP", "kesimpulan: "+hpmt);
            //preklamsi dan eklamsi
            if (!data.get("tekdardiastol").equals("-1"))
                sistol = Double.parseDouble(data.get("tekdardiastol"));
            if (!data.get("tekdardiastol").equals("-1"))
                diastol = Double.parseDouble(data.get("tekdardiastol"));
            if (sistol > 0 && diastol > 0) {
                //syarat untuk preklamsi ringan
                if (sistol / diastol >= 140 / 90 && usiakehamilan > 20)
                    prek = 0;
                if (data.get("proteinuri").equals("+1"))
                    prek = 0;
                //syarat untuk preklamsi berat
                if (sistol / diastol > 160 / 110 && usiakehamilan > 20)
                    prek = 1;
                if (data.get("proteinuri").equals("4"))//lebih dari +2
                    prek = 1;
                //kurang satu poin
                //syarat untuk eklamsi
                if (sistol >= 140 || diastol >= 90) {
                    if (usiakehamilan > 20)
                        if (!data.get("proteinuri").equals("1")) //ada protein uri
                            if (data.get("keadaankhusus").equals("3")) //kejang kejang
                                prek = 2;
                }
            }

            Boolean low_risk = false;
            Boolean high_risk = false;
            Boolean very_high_risk = false;

            int umur = Integer.parseInt(dataibu.getString("umur"));
            int hamilke = Integer.parseInt(data.get("hamilke"));
            int jarakhamil = Integer.parseInt(data.get("jarakhamil"));
            int jumlahlahir = Integer.parseInt(data.get("jumlahir"));
            double tinggibadan = Double.parseDouble(data.get("tinggibadan"));
            String pre = data.get("presentasijanin");
            pre = pre.substring(1, pre.length() - 1);
            List<String> presentasijanin = Arrays.asList(pre.split("\\s*,\\s*"));

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
            if (an>=1) low_risk = true; //pernah abortus
            if (!data.get("vaccum").equals("-1")) low_risk = true; //pernah melahirkan vakum
            if (!data.get("sesar").equals("-1")) low_risk = true; //pernah melahirkan sesar
            //resiko tinggi
            if (riwayatpenyakit.contains("6")) high_risk = true; //kurang darah
            if (riwayatpenyakit.contains("5")) high_risk = true; //malaria
            if (riwayatpenyakit.contains("7")) high_risk = true; //tpc paru
            if (riwayatpenyakit.contains("3")) high_risk = true; //jantung
            if (riwayatpenyakit.contains("0")) high_risk = true; //darah tinggi
            if (riwayatpenyakit.contains("1")) high_risk = true; //kencing manis atau diabetes
            if (riwayatpenyakit.contains("4")) high_risk = true; //psm
            if (usiakehamilan>42) high_risk = true; //lebih bulan
            if (data.get("keadaankhusus").equals("2")) high_risk = true; //kembar air
            if (presentasijanin.contains("3")) high_risk = true; //sungsang
            if (presentasijanin.contains("2")) high_risk = true; //lintang
            // resiko sangat tinggi
            if (tris3.contains("15")) very_high_risk = true;
            if (tris3.contains("10")) very_high_risk = true;
            if (prek == 2) very_high_risk = true;
            //maka tulis kesimpulan
            if (low_risk) keadaan_ibu.setText("Resiko kehamilan: rendah");
            int hemoglobin = Integer.parseInt(data.get("pemeriksaanhb"));
            if (hemoglobin>=11) hasilanalisahb = "Normal";
            if (high_risk) keadaan_ibu.setText("Resiko kehamilan: tinggi");
            if (very_high_risk) keadaan_ibu.setText("Resiko kehamilan: tinggi");
            //hasilanalisahb
            if (hemoglobin>8 && hemoglobin<11) hasilanalisahb = "Anemia ringan";
            if (hemoglobin<=8) hasilanalisahb = "Anemia berat";
            analisahb.setText(analisahb.getText()+hasilanalisahb);
            //analisa gizi
            BB = Double.parseDouble(data.get("tinggibadan"));
            TB = Double.parseDouble(data.get("beratbadanlama"));
            DecimalFormat df = new DecimalFormat("#.##");
            gizi.setText("Indeks masa tubuh: "+df.format(BB/(TB*TB))+" meter");
            //keadaan janin
            //taksiran berat badan janin
            double tfu =0;
            if (usiakehamilan >24 && !data.get("tfu").equals("-1") && !presentasijanin.contains("0") && !presentasijanin.contains("1")) {
                tfu = Double.parseDouble(data.get("tfu"));
                double k = 0;
                if (presentasijanin.contains("6")) k = 11;
                if (presentasijanin.contains("7")) k = 12;
                taksiranberatjanin.setText("Taksiran berat janin: "+df.format(155*(tfu-k))+" gram");

            } else {
                taksiranberatjanin.setText("Taksiran berat janin: belum bisa dianalisa");
            }

            //gerak janin
            String[] pil = {"Belum Terdeteksi","Tidak Terdeteksi","kurang dari 4 kali","antara 4 hingga 10 kali","lebih dari 10 kali"};
            gerakjanin.setText("Gerak janin: "+pil[Integer.parseInt(data.get("gerakjanin"))]);
            //posisi janin
            String[] pilj = {"Belum Terdeteksi",
                    "Tidak Terdeteksi",
                    "Lintang",
                    "Sungsang",
                    "Punggung Kiri",
                    "Punggung Kanan",
                    "kepala belum masuk pintu atas panggul",
                    "kepala sudah masuk pintu atas panggu"};
            String poja = "";
            for (String posisi:presentasijanin) {
                poja += pilj[Integer.parseInt(posisi)]+", ";
            }
            posisijan.setText("Posisi janin: "+poja);

        } catch (IllegalArgumentException | JSONException e) {
            e.printStackTrace();
        }


        //saran
        TextView hasilsaran = (TextView) findViewById(R.id.sarann);
        assert hasilsaran != null;
        String saran = data.get("saran");
        if (!saran.equals("-1"))
        hasilsaran.setText(saran);


    }
}
