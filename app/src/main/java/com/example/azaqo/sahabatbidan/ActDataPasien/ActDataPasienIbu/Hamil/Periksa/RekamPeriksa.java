package com.example.azaqo.sahabatbidan.ActDataPasien.ActDataPasienIbu.Hamil.Periksa;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.azaqo.sahabatbidan.Okdeh;
import com.example.azaqo.sahabatbidan.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RekamPeriksa extends AppCompatActivity {
    ListView rekam;
    String idkehamilan;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekam_periksa);
        rekam = (ListView) findViewById(R.id.rekameriksa);
        Intent intent = getIntent();
        idkehamilan = intent.getStringExtra("idkehamilan");
        HashMap<String,String> send = new HashMap<String,String>();
        send.put("idkehamilan",idkehamilan);
        new Request(send).execute("http://sahabatbundaku.org/request_android/get_record_pemeriksaan.php");
    }

    public void setData(final JSONArray jsonArray) throws JSONException {
        List<String> tanggaltanggal = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tanggaltanggal.add(jsonArray.getJSONObject(i).getString("tanggalperiksa"));
        }
        ArrayAdapter<String> adapta = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,tanggaltanggal);
        rekam.setAdapter(adapta);
        rekam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Intent ten = new Intent(getBaseContext(),ActivityPemeriksaan.class);
                    String selectedid = jsonArray.getJSONObject(position).getString("idpemeriksaan");
                    ten.putExtra("idpemeriksaan",selectedid);
                    startActivity(ten);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class Request extends AsyncTask<String,Void,String>{
        HashMap<String,String> data;

        public Request(HashMap<String, String> data) {
            this.data = data;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Okdeh ok  = new Okdeh();
                return ok.doPostRequestData(params[0],data);
            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                setData(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
