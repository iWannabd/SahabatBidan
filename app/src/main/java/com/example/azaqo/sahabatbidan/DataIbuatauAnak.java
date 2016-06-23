package com.example.azaqo.sahabatbidan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.azaqo.sahabatbidan.ActDataPasien.DataPasiens;
import com.example.azaqo.sahabatbidan.Pengingat.ReminderActivity;

public class DataIbuatauAnak extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_ibu_atau_anak);
        int pilihan = getIntent().getIntExtra("pilihan",0);
        Log.d("PHP", "onCreate: "+pilihan);
        CardView ibu = (CardView) findViewById(R.id.btnDataIbu);
        CardView anak = (CardView) findViewById(R.id.card_view2);
        anak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"Masih dalam tahap pengembangna",Toast.LENGTH_SHORT).show();
            }
        });
        assert ibu != null;
        switch (pilihan){
            case 0: //lihat data ibu
                ibu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getBaseContext(),DataPasiens.class));
                    }
                });
                break;
            case 1: //lihat reminder
                ibu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getBaseContext(), ReminderActivity.class));
                    }
                });
                //ganti tulisan
                TextView tulisan_ibu = (TextView) findViewById(R.id.textView);
                TextView tulisan_anak = (TextView) findViewById(R.id.textView2);
                tulisan_ibu.setText("Reminder ibu");
                tulisan_anak.setText("Reminder anak");
                break;
        }
    }
}
