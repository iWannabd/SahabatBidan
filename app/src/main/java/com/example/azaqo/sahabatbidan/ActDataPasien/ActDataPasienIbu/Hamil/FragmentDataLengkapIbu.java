package com.example.azaqo.sahabatbidan.ActDataPasien.ActDataPasienIbu.Hamil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.azaqo.sahabatbidan.ActDataPasien.ActDataPasienIbu.Hamil.Periksa.PemeriksaanListener;
import com.example.azaqo.sahabatbidan.HubunganAtas;
import com.example.azaqo.sahabatbidan.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


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

    private DataLengkapIbuListener mListener;

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

    EditText[] handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data_lengkap_ibu, container, false);
//        datalengkap = (ListView) view.findViewById(R.id.listDataLengIbu);
        //satu parameter lagi
        new HubunganAtas(this,"http://sahabatbundaku.org/request_android/pemeriksaan_pidan_1.php","get")
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
        Button but = (Button) view.findViewById(R.id.btn_update);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateData();
            }
        });
        return view;
    }

    String[] getkeys = {"email","username","nama","umur","sukubangsa","agama","rt/rw","kecamatan","pendidikan","pekerjaan","nomorhp","alamat","keludes","kota"};
    String[] updatekeys = {"email","username","nama","umur","sukubangsa","agama","rtrw","kecamatan","pendidikan","pekerjaan","nomorhp","alamat","keludes","kota"};

    public void setDatapasien(String jsonpasien) throws JSONException {
        JSONObject pasien = new JSONObject(jsonpasien);
        for (int i = 0; i < getkeys.length; i++) {
            handler[i].setText(pasien.getString(getkeys[i]));
        }
        //simpan username ibu ke shared preference
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor e =sp.edit();
        e.putString("dataibu",jsonpasien);
        e.commit();
    }

    public void UpdateData(){
        HashMap<String,String> send = new HashMap<>();
        for (int i = 0; i < handler.length; i++) {
            send.put(updatekeys[i],handler[i].getText().toString());
        }
        Log.d("PHP", "UpdateData: "+send);
        new HubunganAtas(this,"http://sahabatbundaku.org/request_android/update_ibu.php","update",send).execute();
        //simpan username ibu ke shared preference
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("dataibu",new JSONObject(send).toString());
        e.commit();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DataLengkapIbuListener) {
            mListener = (DataLengkapIbuListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DataLengkapIbuListener");
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
