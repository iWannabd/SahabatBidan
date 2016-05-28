package com.example.azaqo.sahabatbidan;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by azaqo on 01/05/2016.
 * kelas diturunkan untuk mengirim request http OKhttpclient
 */
public class Okdeh {
    OkHttpClient client = new OkHttpClient();
    // code request code here
    String doPostRequestData(String url, String[] keys, String[] values) throws IOException {
        MultipartBody.Builder feb = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        for (int i = 0; i < values.length; i++) {
            feb.addFormDataPart(keys[i], values[i]);
        }

        RequestBody febs = feb.build();

        Request request = new Request.Builder()
                .url(url)
                .post(febs)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String doPostRequestData(String url, HashMap<String, String> data) throws IOException {
        MultipartBody.Builder feb = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);


        for (Map.Entry<String, String> entry : data.entrySet())
        {
            feb.addFormDataPart(entry.getKey(), entry.getValue());
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
