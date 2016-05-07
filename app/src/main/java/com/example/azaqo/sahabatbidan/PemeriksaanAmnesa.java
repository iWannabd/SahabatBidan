package com.example.azaqo.sahabatbidan;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class  PemeriksaanAmnesa extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private int position;
    private String usernameibu;
    private String usernamebidan;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_PARAM1);
            usernameibu = getArguments().getString(ARG_PARAM2);
            usernamebidan = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        switch (position){
            case 1:
                View view = inflater.inflate(R.layout.riwayat_hamil, container, false);
                return periksariwayat(view);
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

    public View periksariwayat(View view){
        final EditText hpmt = (EditText) view.findViewById(R.id.hpmt);
        final Button submit = (Button) view.findViewById(R.id.btnSubmitRiwayat);
        hpmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp  = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                                hpmt.setText(""+year+"-"+monthOfYear+"-"+dayOfMonth);
                            }
                        })
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setDoneText("Sudah");
                cdp.show(getFragmentManager(),"HPMT");
            }
        });

        final CheckBox[] penolong = {
                (CheckBox) view.findViewById(R.id.p1),
                (CheckBox) view.findViewById(R.id.p2),
                (CheckBox) view.findViewById(R.id.p3),
                (CheckBox) view.findViewById(R.id.p4),
        };

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

        final String[] keys = {"hpmt","hamilke","jumlahir","jarakhamil","normal","sesar","vaccum","prematur","bb1","bb2","bb3"};

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> data = new HashMap<>();
                //getting all value from edittext
                for (int i = 0; i < handler.length; i++) {
                    data.put(keys[i],handler[i].getText().toString());
                }
                data.put("penolong","");
                for (int i = 0; i < penolong.length; i++) {
                    if (penolong[i].isChecked())
                        data.put("penolong",data.get("penolong")+penolong[i].getText().toString()+",");
                }

                data.put("usernamebidan",usernamebidan);
                data.put("usernameibu",usernameibu);

                for (Map.Entry<String, String> entry : data.entrySet())
                {
                    Log.d("PHP", "onClick: "+entry.getKey() +" : "+entry.getValue());
                }

                new HubunganAtas(getContext(),"http://sahabatbundaku.org/request_android/riwayat_hamil.php",data)
                .execute();

            }
        });
        return view;
    }
}
