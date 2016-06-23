package com.example.azaqo.sahabatbidan.ActDataPasien.ActDataPasienIbu.Hamil.Periksa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.example.azaqo.sahabatbidan.R;
import com.example.azaqo.sahabatbidan.RequestDatabase;

import org.joda.time.DateTime;
import org.joda.time.Weeks;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class  PemeriksaanAmnesa extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String POSITION = "param1";
    private static final String IDPERIKSA = "param2";
    private static final String IDHAMIL = "param3";
    private static final String IDPERIKSALAMA = "param4";

    private int position;
    private String idpemeriksaan;
    private String idkehamilan;
    private String idpemeriksaanlama;

    private PemeriksaanListener mListener;

    public PemeriksaanAmnesa() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param position Parameter 1. digunakan untuk posisi
     * @return A new instance of fragment PemeriksaanAmnesa.
     */
    public static PemeriksaanAmnesa newInstance(int position,String idpemeriksaan,String idHamil, String idperiksalma) {
        PemeriksaanAmnesa fragment = new PemeriksaanAmnesa();
        Bundle args = new Bundle();
        args.putInt(POSITION, position); //posisi fragment keberapa
        args.putString(IDPERIKSA,idpemeriksaan);
        args.putString(IDHAMIL,idHamil);
        args.putString(IDPERIKSALAMA,idperiksalma);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(POSITION);
            idpemeriksaan = getArguments().getString(IDPERIKSA);
            idkehamilan = getArguments().getString(IDHAMIL);
            idpemeriksaanlama = getArguments().getString(IDPERIKSALAMA);
        }
    }
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        switch (position){
            case 0:
                view = inflater.inflate(R.layout.riwayat_hamil, container, false);
                return periksariwayat(view);
            case 1:
                view = inflater.inflate(R.layout.riwayat_penyakit,container,false);
                return periksapenyakit(view);
            case 2:
                view = inflater.inflate(R.layout.riwayat_keluhan,container,false);
                return periksakeluhan(view);
            case 3:
                view = inflater.inflate(R.layout.pemeriksaan_umum,container,false);
                return periksaumum(view);
            case 4:
                view = inflater.inflate(R.layout.tindakan,container,false);
                return periksatindakan(view);
            default:
                return null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PemeriksaanListener) {
            mListener = (PemeriksaanListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()){
            if(!isVisibleToUser){
                switch (position){
                    case 0:
                        simpanriwayat(view);
                        break;
                    case 1:
                        simpanpenyakit(view);
                        break;
                    case 2:
                        simpankeluhan(view);
                        break;
                    case 3:
                        simpanperiksaumum(view);
                }
            }
        }
    }

    public View periksariwayat(View view){
        //cek database dlu om
        HashMap<String, String> getdata = new HashMap<>();
        if (!idpemeriksaan.equals("0")) {
            getdata.put("idpemeriksaan", idpemeriksaan);
            new Request(getdata,view,0).execute("http://sahabatbundaku.org/request_android/get_pemeriksaan.php");
        } else { //periksa baru maka untuk riwayat hamil menggunakan data lama
            getdata.put("idpemeriksaan", idpemeriksaanlama);
            new Request(getdata,view,0).execute("http://sahabatbundaku.org/request_android/get_pemeriksaan.php");
        }
        final EditText hpmt = (EditText) view.findViewById(R.id.hpmt);
        final Button submit = (Button) view.findViewById(R.id.btnSubmitRiwayat);
        hpmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp  = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                                int bulan = monthOfYear + 1;
                                hpmt.setText(""+year+"-"+bulan+"-"+dayOfMonth);
                            }
                        })
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setDoneText("Ok");
                cdp.show(getFragmentManager(),"HPMT");
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.geser();
            }
        });

        return view;
    }

    public void simpanriwayat(View view){
        final EditText[] handler = {
                ((EditText) view.findViewById(R.id.hpmt)),
                ((EditText) view.findViewById(R.id.hamilke)),
                ((EditText) view.findViewById(R.id.jumlahir)),
                ((EditText) view.findViewById(R.id.jarakhamil)),
                ((EditText) view.findViewById(R.id.normal)),
                ((EditText) view.findViewById(R.id.sesar)),
                ((EditText) view.findViewById(R.id.vaccum)),
                ((EditText) view.findViewById(R.id.prematur)),
                ((EditText) view.findViewById(R.id.bb1)),
                ((EditText) view.findViewById(R.id.bb2)),
                ((EditText) view.findViewById(R.id.bb3)),
        };

        final CheckBox[] penolong = {
                (CheckBox) view.findViewById(R.id.p1),
                (CheckBox) view.findViewById(R.id.p2),
                (CheckBox) view.findViewById(R.id.p3),
                (CheckBox) view.findViewById(R.id.p4),
        };

        final String[] keys = {"hpmt","hamilke","jumlahir","jarakhamil","normal","sesar","vaccum","prematur","bb1","bb2","bb3"};

        HashMap<String,String> data = new HashMap<>();
        //getting all value from edittext
        for (int i = 0; i < handler.length; i++) {
            if (!handler[i].getText().toString().matches(""))
                data.put(keys[i],handler[i].getText().toString());
            else
                data.put(keys[i],"-1");
        }
        for (int i = 8; i <= 10; i++) {
            if (handler[i].getText().toString().equals("")) handler[i].setText("0");
        }
        //getting value for penolong
        List<Integer> peno = new ArrayList<>();
        for (int i = 0; i < penolong.length; i++) {
            if (penolong[i].isChecked())
                peno.add(i);
        }
        data.put("penolong",peno.toString());
        //put idpemeriksaan
        data.put("idpemeriksaan", idpemeriksaan);
        data.put("idkehamilan",idkehamilan);
        SharedPreferences sp = getActivity().getSharedPreferences("Data Dasar",Context.MODE_PRIVATE);
        data.put("usernamebidan",sp.getString("SESSION_LOGIN","Bidan"));

        mListener.kumpulinData(data);
    }

    public View periksapenyakit(View view ){
        //cek database dlu om
        HashMap<String,String> getdata = new HashMap<>();
        if (!idpemeriksaan.equals("0")) {
            getdata.put("idpemeriksaan", idpemeriksaan);
            new Request(getdata,view,1).execute("http://sahabatbundaku.org/request_android/get_penyakit.php");
        } else { //untuk riwayat penyakit harus menggunakan data lama untuk membuat data baru
            getdata.put("idpemeriksaan",idpemeriksaanlama);
            new Request(getdata,view,1).execute("http://sahabatbundaku.org/request_android/get_penyakit.php");
        }
        Spinner kont = (Spinner) view.findViewById(R.id.kontra);
        kont.setSelection(3);

        Button lanjut = (Button) view.findViewById(R.id.btnSubmitPenyakit);
        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.geser();
            }
        });
        return view;
    }

    public void simpanpenyakit(View view){
        final CheckBox[] penyakit = {
                (CheckBox) view.findViewById(R.id.darahTinggi),
                (CheckBox) view.findViewById(R.id.gula),
                (CheckBox) view.findViewById(R.id.asma),
                (CheckBox) view.findViewById(R.id.jantung),
                (CheckBox) view.findViewById(R.id.pms),
                (CheckBox) view.findViewById(R.id.malaria),
                (CheckBox) view.findViewById(R.id.kurangdarah),
                (CheckBox) view.findViewById(R.id.tpcparu),
        };
        final CheckBox penyakitx[] = {
                (CheckBox) view.findViewById(R.id.darahTinggiturunan),
                (CheckBox) view.findViewById(R.id.gulaturunan),
                (CheckBox) view.findViewById(R.id.asmaturunan),
                (CheckBox) view.findViewById(R.id.jantungturunan)
        };
        final EditText[] imunisasi = {
                (EditText) view.findViewById(R.id.tt1),
                (EditText) view.findViewById(R.id.tt2),
                (EditText) view.findViewById(R.id.tt3),
                (EditText) view.findViewById(R.id.tt4),
                (EditText) view.findViewById(R.id.tt5),
        };
        HashMap<String,String> data = new HashMap<String, String>();
        //getting value for imunisasi
        String key[] = {"imunisasiTT1", "imunisasiTT2", "imunisasiTT3", "imunisasiTT4", "imunisasiTT5"};
        for (int i = 0; i < key.length; i++) {
            data.put(key[i],imunisasi[i].getText().toString());
        }
        final Spinner kontra = (Spinner) view.findViewById(R.id.kontra);

        //getting value of penyakit biasa
        ArrayList<Integer> penya = new ArrayList<>();
        for (int i = 0; i < penyakit.length; i++) {
            if (penyakit[i].isChecked()) penya.add(i);
        }
        data.put("riwayatpenyakit",penya.toString());
        //getting value of penyakir turunan
        ArrayList<Integer> penyax = new ArrayList<>();
        for (int i = 0; i < penyakitx.length; i++) {
            if (penyakitx[i].isChecked()) penyax.add(i);
        }
        data.put("penyakitturun",penyax.toString());
        //getting value for kontra
        data.put("riwayatkont",""+kontra.getSelectedItemPosition());

        mListener.kumpulinData(data);
    }

    public View periksakeluhan(View view){
        //cek database dlu om
        HashMap<String,String> getdata = new HashMap<>();
        getdata.put("idpemeriksaan", idpemeriksaan);
        new Request(getdata,view,4).execute("http://sahabatbundaku.org/request_android/get_keluhan.php");
        //highlight kehamilan sekarang
        String tanggal = ActivityPemeriksaan.getDatapemeriksaanAll().get("hpmt");
        if (!tanggal.equals("-1")) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
            DateTime hpmt = formatter.parseDateTime(tanggal);
            int usiakehamilan = Weeks.weeksBetween(hpmt, new DateTime()).getValue(0);

            TextView tris;
            if (usiakehamilan <= 14) {
                tris = (TextView) view.findViewById(R.id.tri1);
                tris.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                tris.setTextColor(Color.parseColor("#FFFFFF"));
            }
            if (usiakehamilan < 28 && usiakehamilan > 14) {
                tris = (TextView) view.findViewById(R.id.tri2);
                tris.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                tris.setTextColor(Color.parseColor("#FFFFFF"));
            }
            if (usiakehamilan >= 28) {
                tris = (TextView) view.findViewById(R.id.tri3);
                tris.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                tris.setTextColor(Color.parseColor("#FFFFFF"));
            }
        }

        Button submit = (Button) view.findViewById(R.id.btnSubmitKeluhan);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.geser();
            }
        });
        return view;
    }

    public void simpankeluhan(View view){
        final CheckBox tri1[] = {
                (CheckBox) view.findViewById(R.id.mualmuntah),
                (CheckBox) view.findViewById(R.id.susahbab),
                (CheckBox) view.findViewById(R.id.keputhihan),
                (CheckBox) view.findViewById(R.id.pusing),
                (CheckBox) view.findViewById(R.id.mudahlelah),
                (CheckBox) view.findViewById(R.id.bakar),
        };

        final CheckBox tri2[] = {
                (CheckBox) view.findViewById(R.id.pusing1),
                (CheckBox) view.findViewById(R.id.nyeriperut),
                (CheckBox) view.findViewById(R.id.nyeripunggung),
                (CheckBox) view.findViewById(R.id.keputhihan1),
                (CheckBox) view.findViewById(R.id.susahbab1),
                (CheckBox) view.findViewById(R.id.kemih),
                (CheckBox) view.findViewById(R.id.gerakjanin),
        };

        final CheckBox tri3[] = {
                (CheckBox) view.findViewById(R.id.susahbab2),
                (CheckBox) view.findViewById(R.id.kramkaki),
                (CheckBox) view.findViewById(R.id.nyeriperut1),
                (CheckBox) view.findViewById(R.id.gerakjanin2),
                (CheckBox) view.findViewById(R.id.kemih1),
                (CheckBox) view.findViewById(R.id.bengkakkaki),
                (CheckBox) view.findViewById(R.id.keputhihan2),
                (CheckBox) view.findViewById(R.id.insomnia),
                (CheckBox) view.findViewById(R.id.sesaknapas),
                (CheckBox) view.findViewById(R.id.nyeripinggang),
                (CheckBox) view.findViewById(R.id.darah),

        };

        HashMap<String,String> data = new HashMap<>();

        ArrayList<Integer> t1 = new ArrayList<>();
        for (int i = 0; i < tri1.length; i++) {
            if (tri1[i].isChecked()) t1.add(i);
        }
        data.put("tris1",t1.toString());

        ArrayList<Integer> t2 = new ArrayList<>();
        for (int i = 0; i < tri2.length; i++) {
            if (tri2[i].isChecked()) t2.add(i);
        }
        data.put("tris2",t2.toString());

        ArrayList<Integer> t3 = new ArrayList<>();
        for (int i = 0; i < tri3.length; i++) {
            if (tri3[i].isChecked()) t3.add(i);
        }
        data.put("tris3",t3.toString());

        mListener.kumpulinData(data);

    }

    public View periksatindakan(final View view){
        HashMap<String,String> req = new HashMap<>();
        req.put("idpemeriksaan",idpemeriksaan);
        new Request(req,view,3).execute("http://sahabatbundaku.org/request_android/get_tindakan.php");


        Button but = (Button) view.findViewById(R.id.btnSubmitUmum);
        Button baru = (Button) view.findViewById(R.id.btnSubmitBaru);

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpantindak(view);
                try {
                    simpankesimpulan();
                } catch (JSONException ignored) {
                }
                mListener.uploadData();
            }
        });

        baru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpantindak(view);
                mListener.uploadDataBaru();
            }
        });

        if (!idpemeriksaan.equals("0")){
            baru.setVisibility(View.GONE);
        } else {
            but.setVisibility(View.GONE);
        }

        Button resume = (Button) view.findViewById(R.id.btnResume);
        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpantindak(view);
                Intent ten = new Intent(getActivity(),ResumeActivity.class);
                ten.putExtra("datapemeriksaanAll",ActivityPemeriksaan.getDatapemeriksaanAll());
                startActivity(ten);
            }
        });

        return view;
    }

    private void simpankesimpulan() throws JSONException {
        //masukkan datakesimpulan

        HashMap<String,String> tampung = new HashMap<>();
        HashMap<String,String> data = ActivityPemeriksaan.getDatapemeriksaanAll();

        JSONObject dataibu = new JSONObject();
        try {
            SharedPreferences sp = getActivity().getSharedPreferences("datadata", Context.MODE_PRIVATE);
            String json = sp.getString("dataibu","{}");
            dataibu = new JSONObject(json);
            Log.d("PHP", "onCreate: dataibu "+json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //usiakehamilan
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime hpmt = formatter.parseDateTime(data.get("hpmt"));
        int usiahamil = Weeks.weeksBetween(hpmt,new DateTime()).getValue(0);
        tampung.put("usiakehamilan",""+usiahamil);

        //data data
        int umur = Integer.parseInt(dataibu.getString("umur"));
        int hamilke = Integer.parseInt(data.get("hamilke"));
        int jarakhamil = Integer.parseInt(data.get("jarakhamil"));
        int jumlahlahir = Integer.parseInt(data.get("jumlahir"));
        double tinggibadan = Double.parseDouble(data.get("tinggibadan"));
        String g = data.get("hamilke");
        String p = data.get("jumlahir");
        String raw = data.get("riwayatpenyakit");
        raw = raw.substring(1, raw.length() - 1);
        List<String> riwayatpenyakit = Arrays.asList(raw.split("\\s*,\\s*"));
        String pre = data.get("presentasijanin");
        pre = pre.substring(1, pre.length() - 1);
        List<String> presentasijanin = Arrays.asList(pre.split("\\s*,\\s*"));
        raw = data.get("tris3");
        raw = raw.substring(1, raw.length() - 1);
        List<String> tris3 = Arrays.asList(raw.split("\\s*,\\s*"));
        double sistol = 0;
        double diastol = 0;
        int prek = -1; //-1 tidak terdeteksi, 0 preklamsi ringan, 1 preklamsi berat, 2 eklamsi

        //preklamsi dan eklamsi
        if (!data.get("tekdardiastol").equals("-1"))
            sistol = Double.parseDouble(data.get("tekdardiastol"));
        if (!data.get("tekdardiastol").equals("-1"))
            diastol = Double.parseDouble(data.get("tekdardiastol"));
        if (sistol > 0 && diastol > 0) {
            //syarat untuk preklamsi ringan
            if (sistol / diastol >= 140 / 90 && usiahamil > 20)
                prek = 0;
            if (data.get("proteinuri").equals("+1"))
                prek = 0;
            //syarat untuk preklamsi berat
            if (sistol / diastol > 160 / 110 && usiahamil > 20)
                prek = 1;
            if (data.get("proteinuri").equals("4"))//lebih dari +2
                prek = 1;
            //kurang satu poin
            //syarat untuk eklamsi
            if (sistol >= 140 || diastol >= 90) {
                if (usiahamil > 20)
                    if (!data.get("proteinuri").equals("1")) //ada protein uri
                        if (data.get("keadaankhusus").equals("3")) //kejang kejang
                            prek = 2;
            }
        }


        //resiko
        Boolean low_risk = false;
        Boolean high_risk = false;
        Boolean very_high_risk = false;

        // penentuan g p dan a
        int gn = 0,pn = 0;
        if (g.equals("-1")) g = "-";
        else gn = Integer.parseInt(g);
        if (p.equals("-1")) p = "-";
        else pn = Integer.parseInt(p);
        int an = gn - pn - 1;

        if (umur<16) low_risk = true;
        if (umur>35 && hamilke==1) low_risk = true;
        if (jarakhamil<2 || jarakhamil<35) low_risk = true;
        if (jumlahlahir>=4) low_risk = true;
        if (umur>35) low_risk = true;
        if (tinggibadan<=145) low_risk = true;
        if (an>=1) low_risk = true; //pernah abortus
        if (!data.get("vaccum").equals("-1")) low_risk = true; //pernah melahirkan vakum
        if (!data.get("sesar").equals("-1")) low_risk = true; //pernah melahirkan sesar

        if (riwayatpenyakit.contains("6")) high_risk = true; //kurang darah
        if (riwayatpenyakit.contains("5")) high_risk = true; //malaria
        if (riwayatpenyakit.contains("7")) high_risk = true; //tpc paru
        if (riwayatpenyakit.contains("3")) high_risk = true; //jantung
        if (riwayatpenyakit.contains("0")) high_risk = true; //darah tinggi
        if (riwayatpenyakit.contains("1")) high_risk = true; //kencing manis atau diabetes
        if (riwayatpenyakit.contains("4")) high_risk = true; //psm
        if (usiahamil>42) high_risk = true; //lebih bulan
        if (data.get("keadaankhusus").equals("2")) high_risk = true; //kembar air
        if (presentasijanin.contains("3")) high_risk = true; //sungsang
        if (presentasijanin.contains("2")) high_risk = true; //lintang

        if (tris3.contains("15")) very_high_risk = true;
        if (tris3.contains("10")) very_high_risk = true;
        if (prek == 2) very_high_risk = true;

        if (low_risk) tampung.put("resiko","Resiko rendah");
        if (high_risk) tampung.put("resiko","Resiko tinggi");
        if (very_high_risk) tampung.put("resiko","Resiko sangat tinggi");

        mListener.kumpulinData(tampung);
    }

    public void simpantindak(View view){
        CheckBox[] imunTT = {
                (CheckBox) view.findViewById(R.id.imnuntt1),
                (CheckBox) view.findViewById(R.id.imnuntt2),
                (CheckBox) view.findViewById(R.id.imnuntt3),
                (CheckBox) view.findViewById(R.id.imnuntt4),
                (CheckBox) view.findViewById(R.id.imnuntt5),
        };
        EditText[] editTexts = {
                (EditText) view.findViewById(R.id.imunlain),
                (EditText) view.findViewById(R.id.tabletfe),
                (EditText) view.findViewById(R.id.tindaklain),
                (EditText) view.findViewById(R.id.saran),
        };

        HashMap<String,String> tampung = new HashMap<>();

        List<Integer> pilihan = new ArrayList<>();
        for (int i = 0; i < imunTT.length; i++) {
            if (imunTT[i].isChecked()) pilihan.add(i);
        }
        tampung.put("imunTT",pilihan.toString());
        tampung.put("imunLain",editTexts[0].getText().toString());
        tampung.put("tindaklain",editTexts[2].getText().toString());
        tampung.put("saran",editTexts[3].getText().toString());
        if (!editTexts[1].getText().toString().equals(""))
            tampung.put("tabletFE",editTexts[1].getText().toString());
        else
            tampung.put("tabletFE","-1");
        mListener.kumpulinData(tampung);
    }

    public View periksaumum(View view){

        //cek database dlu om

        HashMap<String,String> getdata = new HashMap<>();
        if (!idpemeriksaan.equals("0")) {
            getdata.put("idpemeriksaan", idpemeriksaan);
            new Request(getdata,view,5).execute("http://sahabatbundaku.org/request_android/get_umum.php");
        } else {
            getdata.put("idpemeriksaan",idpemeriksaanlama);
            new Request(getdata,view,2).execute("http://sahabatbundaku.org/request_android/get_umum.php");
        }

        Button next = (Button) view.findViewById(R.id.btnSubmitTindak);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.geser();
            }
        });

        return view;
    }

    public void simpanperiksaumum(View view){
        final EditText teksu[] = {
                (EditText) view.findViewById(R.id.suhutubuh),
                (EditText) view.findViewById(R.id.tekanandarahsistol),
                (EditText) view.findViewById(R.id.tekanandarahdiastol),
                (EditText) view.findViewById(R.id.beratbadan),
                (EditText) view.findViewById(R.id.tinggibadan),
                (EditText) view.findViewById(R.id.lila),
                (EditText) view.findViewById(R.id.tfu),
                (EditText) view.findViewById(R.id.hb),
                (EditText) view.findViewById(R.id.jantungjanin),
                (EditText) view.findViewById(R.id.kembar),
                (EditText) view.findViewById(R.id.lablain),
                (EditText) view.findViewById(R.id.beratbadanlama),
        };

        final Spinner spiners[] = {
                (Spinner) view.findViewById(R.id.keadaanumum),
                (Spinner) view.findViewById(R.id.goldar),
                (Spinner) view.findViewById(R.id.gerakjanin2jam),
                (Spinner) view.findViewById(R.id.keadaankhusus),
                (Spinner) view.findViewById(R.id.potenuri),
                (Spinner) view.findViewById(R.id.glukosa),
        };

        final CheckBox[] posisijanins = {
                (CheckBox) view.findViewById(R.id.belum),
                (CheckBox) view.findViewById(R.id.tidak),
                (CheckBox) view.findViewById(R.id.lintang),
                (CheckBox) view.findViewById(R.id.sungsang),
                (CheckBox) view.findViewById(R.id.punggungkiri),
                (CheckBox) view.findViewById(R.id.punggungkanan),
                (CheckBox) view.findViewById(R.id.kepalasudah),
                (CheckBox) view.findViewById(R.id.kepalabelum),
        };

        String keyedt[] = {"suhutubuh","tekdarsistol","tekdardiastol","beratbadan","tinggibadan","lila","tfu","pemeriksaanhb","detakjantungjanin","kembar","pemeriksaanlablain","beratbadanlama"};
        String kespin[] = {"keadaanumum","goldar","gerakjanin","keadaankhusus","proteinuri","glukosa"};

        HashMap<String,String> data = new HashMap<>();

        for (int i = 0; i < keyedt.length; i++) {
            if (!teksu[i].getText().toString().matches(""))
                data.put(keyedt[i],teksu[i].getText().toString());
            else
                data.put(keyedt[i],"-1");
        }

        for (int i = 0; i < kespin.length; i++) {
            data.put(kespin[i],""+spiners[i].getSelectedItemPosition());
        }

        //mendapatkan nilai untuk presentasi janin
        List<Integer> checked = new ArrayList<>();
        for (int i = 0; i < posisijanins.length; i++) {
            if (posisijanins[i].isChecked()) checked.add(i);
        }
        data.put("presentasijanin",checked.toString());


        mListener.kumpulinData(data);
    }

    public void setDataRiwayatHamil(View v,String resultjson) throws JSONException {
        if (!resultjson.equals("Belum menjalain pemeriksaan")) {
            Log.d("PHP", "setDataRiwayatHamil: loadharusnya");
            Log.d("PHP", "setDataRiwayatHamil: "+resultjson);
            JSONObject datariwayathamil = new JSONObject(resultjson);
            EditText[] handler = {
                    ((EditText) v.findViewById(R.id.hpmt)),
                    ((EditText) v.findViewById(R.id.hamilke)),
                    ((EditText) v.findViewById(R.id.jumlahir)),
                    ((EditText) v.findViewById(R.id.jarakhamil)),
                    ((EditText) v.findViewById(R.id.normal)),
                    ((EditText) v.findViewById(R.id.sesar)),
                    ((EditText) v.findViewById(R.id.vaccum)),
                    ((EditText) v.findViewById(R.id.prematur)),
                    ((EditText) v.findViewById(R.id.bb1)),
                    ((EditText) v.findViewById(R.id.bb2)),
                    ((EditText) v.findViewById(R.id.bb3)),
            };
            String[] konci = {"hpmt", "hamilke", "jumlahir", "jarakhamil", "normal", "sesar", "vaccum", "prematur", "bb1", "bb2", "bb3"};
            //set data untuk setiap edit text
            for (int i = 0; i < handler.length; i++) {
                if(datariwayathamil.getString(konci[i]).equals("-1"))
                handler[i].setText("");
                else
                handler[i].setText(datariwayathamil.getString(konci[i]));
            }
            //data untuk checkbox penolong
            final CheckBox[] penolong = {
                    (CheckBox) v.findViewById(R.id.p1),
                    (CheckBox) v.findViewById(R.id.p2),
                    (CheckBox) v.findViewById(R.id.p3),
                    (CheckBox) v.findViewById(R.id.p4),
            };
            String dafterpenolong_raw = datariwayathamil.getString("penolong");
            dafterpenolong_raw = dafterpenolong_raw.substring(1, dafterpenolong_raw.length() - 1);

            List<String> daftarpenolong = Arrays.asList(dafterpenolong_raw.split("\\s*,\\s*"));
            if (!dafterpenolong_raw.equals("")) {
                for (String dafpen : daftarpenolong) {
                    penolong[Integer.parseInt(dafpen)].setChecked(true);
                }
            }
        } else {
            Toast.makeText(getActivity(),resultjson,Toast.LENGTH_SHORT).show();
        }
    }

    public void setRiwayatPenyakit(View view,String json){
        if (!json.equals("Belum menjalani pemeriksaan")) {
            JSONObject dataperiksa;
            try {
                dataperiksa = new JSONObject(json);
                final CheckBox[] penyakit = {
                        (CheckBox) view.findViewById(R.id.darahTinggi),
                        (CheckBox) view.findViewById(R.id.gula),
                        (CheckBox) view.findViewById(R.id.asma),
                        (CheckBox) view.findViewById(R.id.jantung),
                        (CheckBox) view.findViewById(R.id.pms),
                        (CheckBox) view.findViewById(R.id.malaria),
                        (CheckBox) view.findViewById(R.id.kurangdarah),
                        (CheckBox) view.findViewById(R.id.tpcparu),
                };
                final CheckBox penyakitx[] = {
                        (CheckBox) view.findViewById(R.id.darahTinggiturunan),
                        (CheckBox) view.findViewById(R.id.gulaturunan),
                        (CheckBox) view.findViewById(R.id.asmaturunan),
                        (CheckBox) view.findViewById(R.id.jantungturunan)
                };
                final EditText[] imunisasi = {
                        (EditText) view.findViewById(R.id.tt1),
                        (EditText) view.findViewById(R.id.tt2),
                        (EditText) view.findViewById(R.id.tt3),
                        (EditText) view.findViewById(R.id.tt4),
                        (EditText) view.findViewById(R.id.tt5),
                };
                String key[] = {"imunisasiTT1", "imunisasiTT2", "imunisasiTT3", "imunisasiTT4", "imunisasiTT5"};

                for (int i = 0; i < imunisasi.length; i++) {
                    imunisasi[i].setText(dataperiksa.getString(key[i]));
                }

                final Spinner kontra = (Spinner) view.findViewById(R.id.kontra);
                String raw = dataperiksa.getString("riwayatpenyakit");
                raw = raw.substring(1, raw.length() - 1);
                List<String> raww = Arrays.asList(raw.split("\\s*,\\s*"));
                Log.d("PHP", "setRiwayatPenyakit: "+raww);
                if (!raw.equals("")) {
                    for (String i : raww) {
                        penyakit[Integer.parseInt(i)].setChecked(true);
                    }
                }
                raw = dataperiksa.getString("penyakitturun");
                raw = raw.substring(1, raw.length() - 1);

                raww = Arrays.asList(raw.split("\\s*,\\s*"));
                if (!raw.equals("")) {
                    for (String i : raww) {
                        penyakitx[Integer.parseInt(i)].setChecked(true);
                    }
                }
                kontra.setSelection(Integer.parseInt(dataperiksa.getString("riwayatkont")), true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void setDataKeluahan(View view,String json) throws JSONException {

        final CheckBox tri1[] = {
                (CheckBox) view.findViewById(R.id.mualmuntah),
                (CheckBox) view.findViewById(R.id.susahbab),
                (CheckBox) view.findViewById(R.id.keputhihan),
                (CheckBox) view.findViewById(R.id.pusing),
                (CheckBox) view.findViewById(R.id.mudahlelah),
                (CheckBox) view.findViewById(R.id.bakar),
        };

        final CheckBox tri2[] = {
                (CheckBox) view.findViewById(R.id.pusing1),
                (CheckBox) view.findViewById(R.id.nyeriperut),
                (CheckBox) view.findViewById(R.id.nyeripunggung),
                (CheckBox) view.findViewById(R.id.keputhihan1),
                (CheckBox) view.findViewById(R.id.susahbab1),
                (CheckBox) view.findViewById(R.id.kemih),
                (CheckBox) view.findViewById(R.id.gerakjanin),
        };
        Log.d("PHP", "setDataKeluahan: "+json);
        final CheckBox tri3[] = {
                (CheckBox) view.findViewById(R.id.susahbab2),
                (CheckBox) view.findViewById(R.id.kramkaki),
                (CheckBox) view.findViewById(R.id.nyeriperut1),
                (CheckBox) view.findViewById(R.id.gerakjanin2),
                (CheckBox) view.findViewById(R.id.kemih1),
                (CheckBox) view.findViewById(R.id.bengkakkaki),
                (CheckBox) view.findViewById(R.id.keputhihan2),
                (CheckBox) view.findViewById(R.id.insomnia),
                (CheckBox) view.findViewById(R.id.sesaknapas),
                (CheckBox) view.findViewById(R.id.nyeripinggang),
                (CheckBox) view.findViewById(R.id.darah),
        };

        if (!json.equals("Belum menjalani pemeriksaan")){

            JSONObject dataperiksa = new JSONObject(json);

            String raw = dataperiksa.getString("tris1");
            raw = raw.substring(1, raw.length() - 1);
            List<String> raww = Arrays.asList(raw.split("\\s*,\\s*"));
            if (!raw.equals("")){
                for (String i: raww) {
                    tri1[Integer.parseInt(i)].setChecked(true);
                }
            }

            raw = dataperiksa.getString("tris2");
            raw = raw.substring(1, raw.length() - 1);
            raww = Arrays.asList(raw.split("\\s*,\\s*"));
            if (!raw.equals("")){
                for (String i: raww) {
                    tri2[Integer.parseInt(i)].setChecked(true);
                }
            }

            raw = dataperiksa.getString("tris3");
            raw = raw.substring(1, raw.length() - 1);
            raww = Arrays.asList(raw.split("\\s*,\\s*"));
            if (!raw.equals("")){
                for (String i: raww) {
                    tri3[Integer.parseInt(i)].setChecked(true);
                }
            }
        }
    }

    public void setDataTindakan(View view,String json) throws JSONException {
        CheckBox[] imunTT = {
                (CheckBox) view.findViewById(R.id.imnuntt1),
                (CheckBox) view.findViewById(R.id.imnuntt2),
                (CheckBox) view.findViewById(R.id.imnuntt3),
                (CheckBox) view.findViewById(R.id.imnuntt4),
                (CheckBox) view.findViewById(R.id.imnuntt5),
        };
        EditText[] editTexts = {
                (EditText) view.findViewById(R.id.imunlain),
                (EditText) view.findViewById(R.id.tabletfe),
                (EditText) view.findViewById(R.id.tindaklain),
                (EditText) view.findViewById(R.id.saran),
        };

        List<String> imu;
        JSONObject data = new JSONObject(json);
        String im = data.getString("imunTT");
        im = im.substring(1,im.length()-1);
        imu = Arrays.asList(im.split("\\s*,\\s*"));
        if (!im.equals("")){
            for (String i:imu) {
                imunTT[Integer.parseInt(i)].setChecked(true);
            }
        }
        editTexts[0].setText(data.getString("imunLain"));
        editTexts[2].setText(data.getString("tindaklain"));
        if (!data.getString("tabletFE").equals("-1")) editTexts[1].setText(data.getString("tabletFE"));
        else editTexts[1].setText("");
        editTexts[3].setText(data.getString("saran"));
    }

    public void setDataPemeriksaanUmum(View view, String json) throws JSONException {

        final EditText teksu[] = {
                (EditText) view.findViewById(R.id.suhutubuh),
                (EditText) view.findViewById(R.id.tekanandarahsistol),
                (EditText) view.findViewById(R.id.tekanandarahdiastol),
                (EditText) view.findViewById(R.id.beratbadan),
                (EditText) view.findViewById(R.id.tinggibadan),
                (EditText) view.findViewById(R.id.lila),
                (EditText) view.findViewById(R.id.tfu),
                (EditText) view.findViewById(R.id.hb),
                (EditText) view.findViewById(R.id.jantungjanin),
                (EditText) view.findViewById(R.id.kembar),
                (EditText) view.findViewById(R.id.lablain),
                (EditText) view.findViewById(R.id.beratbadanlama),
        };

        final Spinner spiners[] = {
                (Spinner) view.findViewById(R.id.keadaanumum),
                (Spinner) view.findViewById(R.id.goldar),
                (Spinner) view.findViewById(R.id.gerakjanin2jam),
                (Spinner) view.findViewById(R.id.keadaankhusus),
                (Spinner) view.findViewById(R.id.potenuri),
                (Spinner) view.findViewById(R.id.glukosa),
        };

        final CheckBox[] posisijanins = {
                (CheckBox) view.findViewById(R.id.belum),
                (CheckBox) view.findViewById(R.id.tidak),
                (CheckBox) view.findViewById(R.id.lintang),
                (CheckBox) view.findViewById(R.id.sungsang),
                (CheckBox) view.findViewById(R.id.punggungkiri),
                (CheckBox) view.findViewById(R.id.punggungkanan),
                (CheckBox) view.findViewById(R.id.kepalasudah),
                (CheckBox) view.findViewById(R.id.kepalabelum),
        };


        if (!json.equals("Belum menjalain pemeriksaan")){
            JSONObject dataperiksa = new JSONObject(json);

            String keyedt[] = {"suhutubuh","tekdarsistol","tekdardiastol","beratbadan","tinggibadan","lila","tfu","pemeriksaanhb","detakjantungjanin","kembar","pemeriksaanlablain","beratbadanlama"};
            String kespin[] = {"keadaanumum","goldar","gerakjanin","keadaankhusus","proteinuri","glukosa"};

            int i = 0;
            for (String k: keyedt) {
                if (!dataperiksa.getString(k).equals("-1"))
                teksu[i].setText(dataperiksa.getString(k));
                i++;
            }
            i = 0;
            for (String k: kespin) {
                spiners[i].setSelection(Integer.parseInt(dataperiksa.getString(k)));
                i++;
            }
            //setting value for presentasi janin
            String pre = dataperiksa.getString("presentasijanin");
            pre = pre.substring(1, pre.length() - 1);
            List<String> raww = Arrays.asList(pre.split("\\s*,\\s*"));
            if (!pre.equals("")) {
                for (String ii : raww) {
                    posisijanins[Integer.parseInt(ii)].setChecked(true);
                }
            }

        }
    }
    public void setberatbadansebelumhami(View view,String json) throws JSONException {
        EditText lama = (EditText) view.findViewById(R.id.beratbadanlama);
        JSONObject data = new JSONObject(json);
        lama.setText(data.getString("beratbadanlama"));
    }
    public class Request extends RequestDatabase{
        View view;
        int flag;

        public Request (HashMap<String, String> data,View v,int f) {
            super(data);
            this.view = v;
            this.flag = f;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                switch (flag){
                    case 0: //riwayat hamil
                        setDataRiwayatHamil(view,s);
                        break;
                    case 1:
                        setRiwayatPenyakit(view,s);
                        break;
                    case 2:
                        setberatbadansebelumhami(view,s);
                        break;
                    case 3:
                        setDataTindakan(view,s);
                        break;
                    case 4:
                        setDataKeluahan(view,s);
                        break;
                    case 5:
                        setDataPemeriksaanUmum(view,s);
                }
            } catch (JSONException e) {
                Log.d("PHP", "onPostExecute request pemeriksaan: "+s);
                e.printStackTrace();
            }
        }
    }
}
