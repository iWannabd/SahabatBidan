package com.example.azaqo.sahabatbidan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageButton;

import com.example.azaqo.sahabatbidan.ActDataPasien.DataPasiens;

public class DataIbuatauAnak extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_ibu_atau_anak);
        CardView ibu = (CardView) findViewById(R.id.btnDataIbu);
        assert ibu != null;
        ibu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),DataPasiens.class));
            }
        });
    }
}
