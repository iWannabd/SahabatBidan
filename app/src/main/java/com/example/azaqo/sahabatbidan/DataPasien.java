package com.example.azaqo.sahabatbidan;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataPasien extends AppCompatActivity {

    ListView datapasien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pasien);
        Log.d("PHP", "onCreate: datapasien");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        datapasien = (ListView) findViewById(R.id.listPasien);
        new HubunganAtas(this,getBaseContext(),"http://sahabatbundaku.org/request_android/search_ibu.php").execute(new String[]{""},new String[]{""});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        SearchManager sm = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView sv = (SearchView) menu.findItem(R.id.search).getActionView();
        sv.setSearchableInfo(sm.getSearchableInfo(getComponentName()));
        final Context appc = getBaseContext();
        final DataPasien act = this;


        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new HubunganAtas(act,appc,"http://sahabatbundaku.org/request_android/search_ibu.php").execute(new String[]{"cari"},new String[]{query});
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }


        });

        return true;
    }

    public void setDatapasien(String jsonpasien) throws JSONException {
        Log.d("PHP", "setDatapasien: "+jsonpasien);
        JSONArray pasienss = new JSONArray(jsonpasien);
        JSONObject pasien;
        List<Map<String,String>> data = new ArrayList<>();
        Map<String,String> datum;
        for (int i = 0; i < pasienss.length(); i++) {
            pasien = pasienss.getJSONObject(i);
            datum = new HashMap<>();
            datum.put("line1",pasien.getString("nama")+" ("+pasien.getString("username")+")");
            datum.put("line2",pasien.getString("umur")+" tahun");
            data.add(datum);
        }

        final SimpleAdapter adapter = new SimpleAdapter(
                this,
                data,
                android.R.layout.simple_list_item_2,
                new String[]{"line1","line2"},
                new int[]{android.R.id.text1,android.R.id.text2});

        datapasien.setAdapter(adapter);
        datapasien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> o =  (HashMap<String, String>) parent.getItemAtPosition(position);
                //mendapatkan username di dalam kurung
                Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(o.get("line1"));
                String uname = "";
                while(m.find()) {
                    uname = m.group(1);
                }
                Log.d("PHP", "onItemClick: "+uname);
                Intent ten = new Intent(getBaseContext(),ActivityPemeriksaan.class);
                ten.putExtra("unameibu",uname);
                startActivity(ten);
            }
        });
    }


}
