package com.example.azaqo.sahabatbidan;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by azaqo on 27/05/2016.
 */
public class RequestDatabase extends AsyncTask<String,Void,String> {
    HashMap<String,String> data;

    public RequestDatabase(HashMap<String, String> data) {
        this.data = data;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Log.d("PHP", "doInBackground: "+data);
            Okdeh ok  = new Okdeh();
            return ok.doPostRequestData(params[0],data);
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }
}
