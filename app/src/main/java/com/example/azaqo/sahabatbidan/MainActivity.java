package com.example.azaqo.sahabatbidan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.azaqo.sahabatbidan.SigninSignup.LoginActivity;

import org.joda.time.DateTime;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView dataibu = (CardView) findViewById(R.id.card_view2);
        TextView sambut = (TextView) findViewById(R.id.sambut);
        SharedPreferences sp = getSharedPreferences("Data Dasar",MODE_PRIVATE);
        DateTime sekarang = new DateTime();
        int jasekarang = sekarang.getHourOfDay();
        String salam = "";
        if (jasekarang>0) salam = "pagi";
        if (jasekarang>10) salam = "siang";
        if (jasekarang>14) salam = "sore";
        if (jasekarang>18) salam = "malam";

        Button logout = (Button) findViewById(R.id.btnlogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("Data Dasar", Context.MODE_PRIVATE);
                SharedPreferences.Editor edt = sp.edit();
                edt.putString("SESSION_LOGIN","");
                edt.putBoolean("LOGGED",false);
                edt.commit();

                Intent ten = new Intent(getBaseContext(), LoginActivity.class);
                ten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(ten);
            }
        });

        sambut.setText("Selamat "+salam+" "+ sp.getString("SESSION_LOGIN","Bidan"));
        dataibu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),DataIbuatauAnak.class));
            }
        });
    }
}
