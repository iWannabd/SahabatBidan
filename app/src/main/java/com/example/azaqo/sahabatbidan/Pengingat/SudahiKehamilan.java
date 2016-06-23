package com.example.azaqo.sahabatbidan.Pengingat;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.example.azaqo.sahabatbidan.R;
import com.example.azaqo.sahabatbidan.RequestDatabase;

import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SudahiKehamilan extends AppCompatActivity {

    @BindView(R.id.tanggalbersalin)
    EditText tanggal;

    @BindView(R.id.carapersalinan)
    Spinner cara;

    @BindView(R.id.status)
    Spinner status;

    @BindView(R.id.rujuk)
    Spinner rujuk;

    @BindView(R.id.keaibu)
    Spinner keaibu;

    @BindView(R.id.keabayi)
    Spinner keabayi;

    @BindView(R.id.catatan)
    EditText catatan;

    HashMap<String,String> postdata = new HashMap<>();
    ReminderCard item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudahi_kehamilan);

        ButterKnife.bind(this);

        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                                int bulan = monthOfYear +1;
                                tanggal.setText(year+"-"+bulan+"-"+dayOfMonth);
                            }
                        })
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setDoneText("Ok");
                cdp.show(getSupportFragmentManager(),"Tanggal");
            }
        });
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        item = (ReminderCard) bundle.getSerializable("ReminderCards");

        tanggal.setText(item.tanggalpersalinan);
        catatan.setText(item.catatan);
        cara.setSelection(Integer.parseInt(item.carapersalinan));
        status.setSelection(Integer.parseInt(item.status));
        rujuk.setSelection(Integer.parseInt(item.rujuk));
        keaibu.setSelection(Integer.parseInt(item.keadaanibu));
        keabayi.setSelection(Integer.parseInt(item.keadaanbayi));

    }

    String id;

    @OnClick(R.id.btnOK)
    public void submit(){
        id = item.idkehamilan;
        Log.d("PHP", "submit: terpilih"+id);
        postdata.put("idkehamilan",id);
        postdata.put("carapersalinan",""+cara.getSelectedItemPosition());
        postdata.put("status",""+status.getSelectedItemPosition());
        postdata.put("rujuk",""+rujuk.getSelectedItemPosition());
        postdata.put("keadaanibu",""+keaibu.getSelectedItemPosition());
        postdata.put("keadaanbayi",""+keabayi.getSelectedItemPosition());
        postdata.put("catatan",""+catatan.getText().toString());
        postdata.put("tanggalpersalinan",""+tanggal.getText().toString());
        Log.d("PHP", "submit: "+postdata);
        new Request(postdata).execute("http://sahabatbundaku.org/request_android/sudahi_kehamilan.php");
    }
    public class Request extends RequestDatabase {

        public Request(HashMap<String, String> data) {
            super(data);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("updated")) {
                Toast.makeText(getBaseContext(),s,Toast.LENGTH_SHORT).show();
                Intent retintent = new Intent();
                retintent.putExtra("Idkehamilan", id);
                setResult(Activity.RESULT_OK,retintent);
                finish();
            } else {
                Toast.makeText(getBaseContext(),s,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
