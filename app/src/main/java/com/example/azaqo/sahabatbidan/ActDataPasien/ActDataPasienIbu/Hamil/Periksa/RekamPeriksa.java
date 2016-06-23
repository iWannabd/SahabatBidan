package com.example.azaqo.sahabatbidan.ActDataPasien.ActDataPasienIbu.Hamil.Periksa;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.azaqo.sahabatbidan.ActDataPasien.HapusDialog;
import com.example.azaqo.sahabatbidan.MainActivity;
import com.example.azaqo.sahabatbidan.Okdeh;
import com.example.azaqo.sahabatbidan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RekamPeriksa extends AppCompatActivity implements HapusDialog.ApaYangTerjadi {
    ListView rekam;
    String idkehamilan;
    FloatingActionButton fab;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekam_periksa);
        rekam = (ListView) findViewById(R.id.rekameriksa);
        Intent intent = getIntent();
        idkehamilan = intent.getStringExtra("idkehamilan");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        HashMap<String,String> send = new HashMap<String,String>();
        send.put("idkehamilan",idkehamilan);
        new Request(send).execute("http://sahabatbundaku.org/request_android/get_record_pemeriksaan.php");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data_lengkap_ibu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.home){
            Intent ten = new Intent(this,MainActivity.class);
            ten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(ten);
        }

        return super.onOptionsItemSelected(item);
    }

    String selectedidpemeriksaan = "0";
    public void setData(String jsonstring) {
        List<String> tanggaltanggal = new ArrayList<>();
        try {
            final JSONArray jsonArray = new JSONArray(jsonstring);
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
                        JSONObject obj = jsonArray.getJSONObject(position);
                        ten.putExtra("idkehamilan",idkehamilan);
                        ten.putExtra("idpemeriksaan",obj.getString("idpemeriksaan"));
                        ten.putExtra("tanggalperiksa",obj.getString("tanggalperiksa"));
                        startActivity(ten);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            rekam.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    DialogFragment df = new HapusDialog();
                    df.show(getFragmentManager(),"hapusrekam");
                    try {
                        selectedidpemeriksaan = jsonArray.getJSONObject(position).getString("idpemeriksaan");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });
            fab = (FloatingActionButton) findViewById(R.id.fab);
            assert fab != null;
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ten = new Intent(getBaseContext(),ActivityPemeriksaan.class);
                    ten.putExtra("idkehamilan",idkehamilan);
                    ten.putExtra("idpemeriksaan","0"); //artinya isi form dari awal
                    try {
                        String idpemeriksaanterbaru = jsonArray.getJSONObject(0).getString("idpemeriksaan");
                        ten.putExtra("idpemeriksaanterbaru",idpemeriksaanterbaru);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivityForResult(ten,1);
//                    startActivity(ten);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            fab = (FloatingActionButton) findViewById(R.id.fab);
            assert fab != null;
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ten = new Intent(getBaseContext(),ActivityPemeriksaan.class);
                    ten.putExtra("idkehamilan",idkehamilan);
                    ten.putExtra("idpemeriksaan","0");
                    String idpemeriksaanterbaru = "0";
                    ten.putExtra("idpemeriksaanterbaru",idpemeriksaanterbaru);
                    startActivity(ten);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){
                HashMap<String,String> send = new HashMap<String,String>();
                send.put("idkehamilan",idkehamilan);
                new Request(send).execute("http://sahabatbundaku.org/request_android/get_record_pemeriksaan.php");
            }
        }
    }

    @Override
    public void hapuspressed(String uname) {
        HashMap<String,String> send = new HashMap<String,String>();
        send.put("idkehamilan",idkehamilan);
        send.put("idpemeriksaan",selectedidpemeriksaan);
        new Request(send).execute("http://sahabatbundaku.org/request_android/delete_record_periksa.php");
        new Request(send).execute("http://sahabatbundaku.org/request_android/get_record_pemeriksaan.php");
    }

    private class Request extends AsyncTask<String,Void,String>{
        HashMap<String,String> data;

        public Request(HashMap<String, String> data) {
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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setData(s);
        }


    }
}
