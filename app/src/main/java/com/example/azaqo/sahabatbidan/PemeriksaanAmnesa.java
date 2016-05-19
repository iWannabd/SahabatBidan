package com.example.azaqo.sahabatbidan;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class  PemeriksaanAmnesa extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    // TODO: Rename and change types of parameters
    private int position;
    private String usernameibu;
    private String usernamebidan;
    private HashMap<String,String> data;

    private PemeriksaanListener mListener;

    public PemeriksaanAmnesa() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1. digunakan untuk posisi
     * @return A new instance of fragment PemeriksaanAmnesa.
     */
    // TODO: Rename and change types and number of parameters
    public static PemeriksaanAmnesa newInstance(int param1,String unameibu, String unamebidan) {
        PemeriksaanAmnesa fragment = new PemeriksaanAmnesa();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2,unameibu);
        args.putString(ARG_PARAM3,unamebidan);
        fragment.setArguments(args);
        return fragment;
    }

    public static PemeriksaanAmnesa newInstancedataperiksa(int param1,HashMap<String,String> data) {
        PemeriksaanAmnesa fragment = new PemeriksaanAmnesa();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM4, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_PARAM1);
            usernameibu = getArguments().getString(ARG_PARAM2);
            usernamebidan = getArguments().getString(ARG_PARAM3);
            data = (HashMap<String, String>) getArguments().getSerializable(ARG_PARAM4);
        }
    }
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        switch (position){
            case 1:
                view = inflater.inflate(R.layout.riwayat_hamil, container, false);
                return periksariwayat(view);
            case 2:
                view = inflater.inflate(R.layout.riwayat_penyakit,container,false);
                return periksapenyakit(view);
            case 3:
                view = inflater.inflate(R.layout.riwayat_keluhan,container,false);
                return periksakeluhan(view);
            case 4:
                view = inflater.inflate(R.layout.pemeriksaan_umum,container,false);
                return periksaumum(view);
            case 5:
                view = inflater.inflate(R.layout.resume_pemeriksaan,container,false);
                return kesimpulan(view);
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
                    case 1:
                        simpan1(view);
                        break;
                    case 2:
                        simpan2(view);
                        break;
                    case 3:
                        simpan3(view);
                        break;
                }
            }
        }
    }


    public View kesimpulan(View view){
        Log.d("PHP", "kesimpulan: "+data.get("hpmt"));
        return view;
    }

    public View periksariwayat(View view){
        //cek database dlu om
        HashMap<String,String> getdata = new HashMap<>();
        getdata.put("usernameibu",usernameibu);
        new HubunganAtas(getdata,"http://sahabatbundaku.org/request_android/get_pemeriksaan.php","loaddata",view,this).execute();


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

    public void simpan1(View view){
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
            data.put(keys[i],handler[i].getText().toString());
        }
        //getting value for penolong
        List<Integer> peno = new ArrayList<>();
        for (int i = 0; i < penolong.length; i++) {
            if (penolong[i].isChecked())
                peno.add(i);
        }
        data.put("penolong",peno.toString());
        //get username ibu and bidan
        data.put("usernamebidan",usernamebidan);
        data.put("usernameibu",usernameibu);

        mListener.kumpulinData(data);
    }

    public View periksapenyakit(View view ){
        //cek database dlu om
        HashMap<String,String> getdata = new HashMap<>();
        getdata.put("usernameibu",usernameibu);
        new HubunganAtas(getdata,"http://sahabatbundaku.org/request_android/get_penyakit.php","penyakit",view,this).execute();

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

    public void simpan2(View view){
        final CheckBox[] penyakit = {
                (CheckBox) view.findViewById(R.id.darahTinggi),
                (CheckBox) view.findViewById(R.id.gula),
                (CheckBox) view.findViewById(R.id.asma),
                (CheckBox) view.findViewById(R.id.jantung),
        };
        final CheckBox penyakitx[] = {
                (CheckBox) view.findViewById(R.id.darahTinggiturunan),
                (CheckBox) view.findViewById(R.id.gulaturunan),
                (CheckBox) view.findViewById(R.id.asmaturunan),
                (CheckBox) view.findViewById(R.id.jantungturunan)
        };
        final Spinner kontra = (Spinner) view.findViewById(R.id.kontra);
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
        getdata.put("usernameibu",usernameibu);
        new HubunganAtas(getdata,"http://sahabatbundaku.org/request_android/get_keluhan.php","keluhan",view,this).execute();


        Button submit = (Button) view.findViewById(R.id.btnSubmitKeluhan);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.geser();
            }
        });
        return view;
    }

    public void simpan3(View view){
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

    public View periksaumum(final View view){

        //cek database dlu om
        HashMap<String,String> getdata = new HashMap<>();
        getdata.put("usernameibu",usernameibu);
        new HubunganAtas(getdata,"http://sahabatbundaku.org/request_android/get_umum.php","umumperiksa",view,this).execute();
        Button but = (Button) view.findViewById(R.id.btnSubmitUmum);

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan4(view);
                mListener.uploadData();

            }
        });

        return view;
    }

    public void simpan4(View view){
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
        };

        final Spinner spiners[] = {
                (Spinner) view.findViewById(R.id.keadaanumum),
                (Spinner) view.findViewById(R.id.goldar),
                (Spinner) view.findViewById(R.id.prejanin),
                (Spinner) view.findViewById(R.id.gerakjanin2jam),
        };

        HashMap<String,String> data = new HashMap<>();

        String keyedt[] = {"suhutubuh","tekdarsistol","tekdardiastol","beratbadan","tinggibadan","lila","tfu","pemeriksaanhb","detakjantungjanin"};
        String kespin[] = {"keadaanumum","goldar","presentasijanin","gerakjanin"};

        for (int i = 0; i < keyedt.length; i++) {
            if (!teksu[i].getText().toString().matches(""))
                data.put(keyedt[i],teksu[i].getText().toString());
            else
                data.put(keyedt[i],"-1");
        }

        for (int i = 0; i < kespin.length; i++) {
            data.put(kespin[i],""+spiners[i].getSelectedItemPosition());
        }

        mListener.kumpulinData(data);
    }

    public void setDataRiwayatHamil(View v,String resultjson) throws JSONException {
        if (!resultjson.equals("Belum menjalain pemeriksaan")) {
            Log.d("PHP", "setDataRiwayatHamil: loadharusnya");
            Log.d("PHP", "setDataRiwayatHamil: "+resultjson);
            Toast.makeText(getActivity(),"Telah menjalani pemeriksaan",Toast.LENGTH_SHORT).show();
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
                };
                final CheckBox penyakitx[] = {
                        (CheckBox) view.findViewById(R.id.darahTinggiturunan),
                        (CheckBox) view.findViewById(R.id.gulaturunan),
                        (CheckBox) view.findViewById(R.id.asmaturunan),
                        (CheckBox) view.findViewById(R.id.jantungturunan)
                };
                final Spinner kontra = (Spinner) view.findViewById(R.id.kontra);
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
                String raw = dataperiksa.getString("riwayatpenyakit");
                raw = raw.substring(1, raw.length() - 1);
                List<String> raww = Arrays.asList(raw.split("\\s*,\\s*"));
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
        };

        final Spinner spiners[] = {
                (Spinner) view.findViewById(R.id.keadaanumum),
                (Spinner) view.findViewById(R.id.goldar),
                (Spinner) view.findViewById(R.id.prejanin),
                (Spinner) view.findViewById(R.id.gerakjanin2jam),
        };

        if (!json.equals("Belum menjalain pemeriksaan")){
            JSONObject dataperiksa = new JSONObject(json);

            String keyedt[] = {"suhutubuh","tekdarsistol","tekdardiastol","beratbadan","tinggibadan","lila","tfu","pemeriksaanhb","detakjantungjanin"};
            String kespin[] = {"keadaanumum","goldar","presentasijanin","gerakjanin"};

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
        }
    }
}
