package com.example.azaqo.sahabatbidan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by azaqo on 28/04/2016.
 */
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
            return okdeh.doPostRequestDataDasar(url, params[0], params[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ada yang salah";
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
            if (s.equals("1")) {
                Toast.makeText(context, "Username atau Password salah", Toast.LENGTH_LONG).show();
            }
            else {
                edt.putString("SESSION_LOGIN",s);
                edt.commit();
                Intent utama = new Intent(activity,MainActivity.class);
                utama.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(utama);
            }
        }
    }

    public class Okdeh {
        OkHttpClient client = new OkHttpClient();
        // code request code here
        String doPostRequestDataDasar(String url, String[] keys, String[] values) throws IOException {
            MultipartBody.Builder feb = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);

            for (int i = 0; i < values.length; i++) {
                feb.addFormDataPart(keys[i],values[i]);
            }

            RequestBody febs = feb.build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(febs)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }

    }
}
