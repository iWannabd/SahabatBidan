package com.example.azaqo.sahabatbidan.ActDataPasien.ActDataPasienIbu.Hamil;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.azaqo.sahabatbidan.ActDataPasien.ActDataPasienIbu.Hamil.Periksa.ActivityPemeriksaan;
import com.example.azaqo.sahabatbidan.ActDataPasien.ActDataPasienIbu.Hamil.Periksa.RekamPeriksa;
import com.example.azaqo.sahabatbidan.HubunganAtas;
import com.example.azaqo.sahabatbidan.R;
import com.example.azaqo.sahabatbidan.ActDataPasien.HapusDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DaftarKehamilanFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private String usernameibu;

    private DataLengkapIbuListener mListener;

    public DaftarKehamilanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment DaftarKehamilanFragment.
     */
    public static DaftarKehamilanFragment newInstance(String param1) {
        DaftarKehamilanFragment fragment = new DaftarKehamilanFragment();
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

    ListView daftarkheamilan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daftar_kehamilan, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment df = new TambahKehamilanDialog();
                df.show(getActivity().getFragmentManager(),"Tambah kehamilan");
            }
        });
        daftarkheamilan = (ListView) view.findViewById(R.id.daftarkehamilan);
        HashMap<String,String> send = new HashMap<>();
        send.put("unameibu",usernameibu);
        new HubunganAtas(this,"http://sahabatbundaku.org/request_android/get_kehamilan.php",send).execute();


        return view;
    }

    public void setDatadatakehamilan(String result){
        try {
            final JSONArray data_mentah = new JSONArray(result);
            List<String> dataset = new ArrayList<>();
            for (int i = 0; i < data_mentah.length(); i++) {
                dataset.add("Kehamilan ke-"+data_mentah.getJSONObject(i).getString("ke"));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,dataset);
            daftarkheamilan.setAdapter(adapter);
            daftarkheamilan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        Intent ten = new Intent(getContext(),RekamPeriksa.class);
                        ten.putExtra("idkehamilan",data_mentah.getJSONObject(position).getString("idHamil"));
                        startActivity(ten);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
            daftarkheamilan.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    String item =(String) parent.getItemAtPosition(position);
                    String ke = item.replaceAll("[^0-9]", "");
                    DialogFragment df = HapusDialog.newInstance(ke);
                    df.show(getActivity().getFragmentManager(),"hapusga");
                    return true;
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
