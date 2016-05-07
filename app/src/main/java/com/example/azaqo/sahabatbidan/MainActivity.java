package com.example.azaqo.sahabatbidan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton dataibu = (ImageButton) findViewById(R.id.imageButton);
        TextView sambut = (TextView) findViewById(R.id.sambut);
        SharedPreferences sp = getSharedPreferences("Data Dasar",MODE_PRIVATE);
        sambut.setText("Selamat datang "+ sp.getString("SESSION_LOGIN","Bidan"));
        dataibu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),DataIbuatauAnak.class));
            }
        });
    }
}
