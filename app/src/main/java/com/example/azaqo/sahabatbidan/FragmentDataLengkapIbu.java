package com.example.azaqo.sahabatbidan;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PemeriksaanListener} interface
 * to handle interaction events.
 * Use the {@link FragmentDataLengkapIbu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDataLengkapIbu extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String usernameibu;

    private PemeriksaanListener mListener;

    public FragmentDataLengkapIbu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1. berguna untuk memasukkan json ibu
     * @return A new instance of fragment FragmentDataLengkapIbu.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentDataLengkapIbu newInstance(String param1) {
        FragmentDataLengkapIbu fragment = new FragmentDataLengkapIbu();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            usernameibu = getArguments().getString(ARG_PARAM1);
        }
    }

    ListView datalengkap;
    EditText[] handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data_lengkap_ibu, container, false);
//        datalengkap = (ListView) view.findViewById(R.id.listDataLengIbu);
        //satu parameter lagi
        new HubunganAtas(this,"http://sahabatbundaku.org/request_android/pemeriksaan_pidan_1.php")
                .execute(new String[]{"user"},new String[]{usernameibu});
        handler = new EditText[]{
                (EditText) view.findViewById(R.id.email),
                (EditText) view.findViewById(R.id.username),
                (EditText) view.findViewById(R.id.nama),
                (EditText) view.findViewById(R.id.umur),
                (EditText) view.findViewById(R.id.sukubangsa),
                (EditText) view.findViewById(R.id.agama),
                (EditText) view.findViewById(R.id.rtrw),
                (EditText) view.findViewById(R.id.kecamatan),
                (EditText) view.findViewById(R.id.pendidikan),
                (EditText) view.findViewById(R.id.pekerjaan),
                (EditText) view.findViewById(R.id.nomorhp),
                (EditText) view.findViewById(R.id.alamat),
                (EditText) view.findViewById(R.id.keludes),
                (EditText) view.findViewById(R.id.kota),
        };
        return view;
    }

    public void setDatapasien(String jsonpasien) throws JSONException {
        JSONObject pasien = new JSONObject(jsonpasien);
        List<Map<String,String>> data = new ArrayList<>();
        Map<String,String> datum;
        String[] keys = {"email","username","nama","umur","sukubangsa","agama","rt/rw","kecamatan","pendidikan","pekerjaan","nomorhp","alamat","keludes","kota"};
        String[] explanations = {"Email","Username","Nama","Umur","Sukubangsa","Agama","RT/RW","Kecamatan","Pendidikan","Pekerjaan","Kontak","Alammat","Keluarahan/Desa","Kota"};

        for (int i = 0; i < keys.length; i++) {
            handler[i].setText(pasien.getString(keys[i]));
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PemeriksaanListener) {
            mListener = (PemeriksaanListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PemeriksaanListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
