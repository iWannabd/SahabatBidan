package com.example.azaqo.sahabatbidan;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatadataKehamilan extends AppCompatActivity implements TambahKehamilanDialog.NoticeDialogFragment, TanyaDialog.ApaYangTerjadi {

    ListView datakehamilan;
    String usernameibu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datadata_kehamilan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment df = new TambahKehamilanDialog();
                df.show(getFragmentManager(),"Tambah kehamilan");
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        usernameibu = getIntent().getStringExtra("unameibu");
        datakehamilan = (ListView) findViewById(R.id.datadatakehamilan);
        HashMap<String,String> send = new HashMap<>();
        send.put("unameibu",usernameibu);
        new HubunganAtas(this,"http://sahabatbundaku.org/request_android/get_kehamilan.php",send).execute();
    }

    public void setDatadatakehamilan(String result){
        try {
            JSONArray data_mentah = new JSONArray(result);
            List<String> dataset = new ArrayList<>();
            for (int i = 0; i < data_mentah.length(); i++) {
                dataset.add("Kehamilan ke-"+data_mentah.getJSONObject(i).getString("ke"));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,dataset);
            datakehamilan.setAdapter(adapter);
            datakehamilan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String item =(String) parent.getItemAtPosition(position);
                    String ke = item.replaceAll("[^0-9]", "");
                    Intent ten = new Intent(getBaseContext(),ActivityPemeriksaan.class);
                    ten.putExtra("ke",ke);
                    ten.putExtra("unameibu",usernameibu);
                    startActivity(ten);

                }
            });
            datakehamilan.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    String item =(String) parent.getItemAtPosition(position);
                    String ke = item.replaceAll("[^0-9]", "");
                    DialogFragment df = TanyaDialog.newInstance(ke);
                    df.show(getFragmentManager(),"hapusga");
                    return true;
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSimpanPressed(int selected) {
        HashMap<String,String> send = new  HashMap<>();
        send.put("ke",selected+"");
        send.put("unameibu",usernameibu);
        new HubunganAtas(this,"http://sahabatbundaku.org/request_android/tambah_kehamilan.php","tambah",send).execute();
        //refresh setelah tambah
        HashMap<String,String> send2 = new HashMap<>();
        send2.put("unameibu",usernameibu);
        new HubunganAtas(this,"http://sahabatbundaku.org/request_android/get_kehamilan.php",send2).execute();
    }

    @Override
    public void hapuspressed(String uname) {
        HashMap<String,String> send = new  HashMap<>();
        send.put("ke",uname+"");
        send.put("unameibu",usernameibu);
        new HubunganAtas(this,"http://sahabatbundaku.org/request_android/hapus_kehamilan.php","tambah",send).execute();
        //refresh setelah hapus
        HashMap<String,String> send2 = new HashMap<>();
        send2.put("unameibu",usernameibu);
        new HubunganAtas(this,"http://sahabatbundaku.org/request_android/get_kehamilan.php",send2).execute();
    }
}
