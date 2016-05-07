package com.example.azaqo.sahabatbidan;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by azaqo on 30/04/2016.
 */
public class HubunganAtas extends AsyncTask<String[],Void,Void> {
    DataPasien activity;
    ActivityPemeriksaan activityPemeriksaan;
    FragmentDataLengkapIbu detil;
    Context context;
    HashMap<String,String> data = null;
    String result = "{}";
    String url;

    public HubunganAtas(DataPasien activity, Context context, String url) {
        this.activity = activity;
        this.context = context;
        this.url = url;
        detil = null;
    }

    public HubunganAtas(FragmentDataLengkapIbu detil, Context context, String url) {
        this.detil = detil;
        this.context = context;
        this.url = url;
        activity = null;
    }

    public HubunganAtas(Context context, String url,HashMap<String,String> data) {
        this.context = context;
        this.url = url;
        this.data = data;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String[]... params) {
        Okdeh ok = new Okdeh();
        try {
            if (data!=null) result = ok.doPostRequestData(url,data);
            else result = ok.doPostRequestData(url, params[0],params[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        try {
            Log.d("PHP", "onPostExecute:"+result);
            if (activity!=null) activity.setDatapasien(result);
            if (detil!=null) detil.setDatapasien(result);
            if (activityPemeriksaan!=null) Toast.makeText(context,result,Toast.LENGTH_LONG).show();
            Log.d("PHP", "onPostExecute:"+result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
