package com.example.azaqo.sahabatbidan.SigninSignup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.azaqo.sahabatbidan.R;
import com.example.azaqo.sahabatbidan.UploadDataRegistrasi;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragmentLogin extends Fragment {
    public static SharedPreferences shapref;
    public static SharedPreferences.Editor editor;
    static final String[] tokeys = {"nama","instansi","alamatrs","alamatbidan","username","passwd","email","faspelkes"};

    ChangePage changePage;
    public interface ChangePage{
        public void geser(int pos);
    }
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragmentLogin newInstance(int sectionNumber) {
        PlaceholderFragmentLogin fragment = new PlaceholderFragmentLogin();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragmentLogin() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        shapref = getActivity().getSharedPreferences("Data Dasar", Context.MODE_PRIVATE);
        editor = shapref.edit();
        switch (getArguments().getInt(ARG_SECTION_NUMBER)){
            case 1:
                rootView = inflater.inflate(R.layout.fragment_login, container, false);
                return LoginSectionBinding(rootView);
            case 2:
                rootView = inflater.inflate(R.layout.fragment_registrasi, container, false);
                return RegisSectionBinding(rootView);
            default:
                return null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            changePage = (ChangePage) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()+" harus di implement om");
        }
    }

    public View LoginSectionBinding(View view){
        final EditText uname = (EditText) view.findViewById(R.id.input_username);
        final EditText passwd = (EditText) view.findViewById(R.id.input_password);
        final TextView signup = (TextView) view.findViewById(R.id.link_signup);
        final String[] key = {"username","passwd"};
        final String[] val = new String[2];

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePage.geser(1);
            }
        });
        Button login = (Button) view.findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                val[0] = uname.getText().toString();
                val[1] = passwd.getText().toString();
                if (val[0].trim().length()!=0 && val[1].trim().length()!=0){
                    new UploadDataRegistrasi(getContext(), (LoginActivity) getActivity(),"http://" +
                            "sahabatbundaku.org/request_android/login_bidan.php",false).execute(key,val);
                } else {
                    Toast.makeText(getContext(),"Data belum lengkap",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    public View RegisSectionBinding(View view){
        Button btn_daftar = (Button) view.findViewById(R.id.btn_daftar);
        final EditText[] todata = {
                (EditText) view.findViewById(R.id.nama),
                (EditText) view.findViewById(R.id.instansi),
                (EditText) view.findViewById(R.id.alamatrs),
                (EditText) view.findViewById(R.id.alamatbidan),
                (EditText) view.findViewById(R.id.username),
                (EditText) view.findViewById(R.id.passwd),
                (EditText) view.findViewById(R.id.email),
        };
        final Spinner faspelkes = (Spinner) view.findViewById(R.id.faspelkes);

        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean lengkap=true;
                for (int i = 0; i < todata.length; i++) {
                    if (todata[i].getText().toString().trim().length() != 0) {
                        lengkap = true;
                    }
                    else {
                        lengkap = false;
                        break;
                    }
                }
                if (lengkap) {
                    for (int i = 0; i < todata.length; i++) {
                        editor.putString(tokeys[i], todata[i].getText().toString());
                    }
                    editor.putString("faspelkes",""+faspelkes.getSelectedItemPosition());
                    editor.commit();
                    //menampilkan syarat dan ketentuan
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Syarat dan ketentuan");
                    alertDialog.setMessage("Apakah anda menyetujua persyaratan dan ketentuan yang berlaku?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ya",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    UploadData();
                                    dialog.dismiss();
                                }
                            }
                    );
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Tidak",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }
                            );
                    alertDialog.show();
                } else
                    Toast.makeText(getContext(),"Data belum lengkap",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    public void UploadData(){
        String[] values = new String[tokeys.length];
        for (int i = 0; i < tokeys.length ; i++) {
            values[i] = shapref.getString(tokeys[i],"");
        }
        new UploadDataRegistrasi(getContext(), (LoginActivity) getActivity(),"http://sahabatbundaku.org/request_android/" +
                "insert_bidan.php",true).execute(tokeys,values);
    }
}
