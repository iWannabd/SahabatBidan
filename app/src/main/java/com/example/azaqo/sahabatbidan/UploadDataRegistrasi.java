package com.example.azaqo.sahabatbidan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.azaqo.sahabatbidan.SigninSignup.LoginActivity;

import java.io.IOException;

public class UploadDataRegistrasi extends AsyncTask<String[],Void,String> {

    Context context;
    String url;
    LoginActivity activity;
    Boolean flag; // true if login false if sign up // menandai apakah data yang dikirim untuk login atau signup
    SharedPreferences sp;
    SharedPreferences.Editor edt;

    public UploadDataRegistrasi(Context context,LoginActivity activity, String url, Boolean flag) {
        this.context = context;
        this.url = url;
        this.flag = flag;
        this.activity = activity;
        sp = context.getSharedPreferences("Data Dasar",Context.MODE_PRIVATE);
        edt = sp.edit();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(context,"Harap Tunggu...",Toast.LENGTH_LONG).show();
    }

    @Override
    protected String doInBackground(String[]... params) {
        Okdeh okdeh = new Okdeh();
        try {
            return okdeh.doPostRequestData(url, params[0], params[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "2";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("PHP", "onPostExecute: "+s);
        if (flag) {
            Toast.makeText(context, s, Toast.LENGTH_LONG).show();
            if (s.equals("Data berhasil dimasukkan, silahkan login")) {
                activity.geser(0);
            }
        } else {
            if (s.equals("0")) {
                Toast.makeText(context, "Terjadi kesalahan pada server", Toast.LENGTH_LONG).show();
            }
            if (s.equals("2")) Toast.makeText(context,"Sepertinya perangkat tidak terhubung ke internet",Toast.LENGTH_SHORT).show();
            if (s.equals("1")) {
                Toast.makeText(context, "Username atau Password salah", Toast.LENGTH_LONG).show();
            }
            else {
                edt.putString("SESSION_LOGIN",s);
                edt.putBoolean("LOGGED",true);
                edt.commit();
                Intent utama = new Intent(activity,MainActivity.class);
                utama.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(utama);
            }
        }
    }

}
