package com.example.azaqo.sahabatbidan;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);
        SharedPreferences sp = getSharedPreferences("Data Dasar",MODE_PRIVATE);
        String s = sp.getString("SESSION_LOGIN","bidan");
        getActionBar().setTitle("Selamat Datang "+s);
    }
}
