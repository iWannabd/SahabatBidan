package com.example.azaqo.sahabatbidan.Pengingat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.azaqo.sahabatbidan.R;
import com.example.azaqo.sahabatbidan.RequestDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReminderActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    ProgressDialog dagoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dagoan = new ProgressDialog(ReminderActivity.this);
        dagoan.setMessage("Tunggu...");

        String unamebidan = getSharedPreferences("Data Dasar", Context.MODE_PRIVATE).getString("SESSION_LOGIN","Bidan");
        HashMap<String,String> a = new HashMap<String,String>();
        a.put("unamebidan",unamebidan);
        new Request(a).execute("http://sahabatbundaku.org/request_android/get_kesimpulan.php");


        bornclicked = new RecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ReminderCard item) {
                Log.d("PHP", "onItemClick: " + item.idkehamilan);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ReminderCards", item);
                Intent inteng = new Intent(getBaseContext(), SudahiKehamilan.class);
                inteng.putExtras(bundle);
                startActivityForResult(inteng, REQUESTCODE);
            }
        };

        dropclicked = new RecycleViewAdapter.OnDropOutClickListener() {
            @Override
            public void onDeleteClick(ReminderCard item) {
                HashMap<String,String> post  = new HashMap<>();
                post.put("idkehamilan",item.idkehamilan);
                new Request2(post,item).execute("http://sahabatbundaku.org/request_android/droping_out_kehamilan.php");
            }
        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    int REQUESTCODE = 1;
    public RecycleViewAdapter.OnItemClickListener bornclicked;
    public RecycleViewAdapter.OnDropOutClickListener dropclicked;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUESTCODE){
            if (resultCode == Activity.RESULT_OK){
                String birthid  = data.getStringExtra("Idkehamilan")+"";
                Log.d("PHP", "onActivityResult: "+reminderCards);
                Log.d("PHP", "onActivityResult: "+birthid);
                ReminderCard item = null;
                for (int i = 0; i < reminderCards.size(); i++) {
                    if (reminderCards.get(i).idkehamilan.equals(birthid)) {
                        item = reminderCards.get(i);
                        item.selesai = "1";
                    }
                }
                RecycleViewAdapter rva = new RecycleViewAdapter(reminderCards,bornclicked,dropclicked);
                recyclerView.setAdapter(rva);
            }
        }
    }

    List<ReminderCard> reminderCards = new ArrayList<>();

    protected void setRecyclerViewData(String jsonarray) throws JSONException {
        recyclerView = (RecyclerView) findViewById(R.id.recyclePiew);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        JSONArray jsonreminders = new JSONArray(jsonarray);

        for (int i = 0; i < jsonreminders.length(); i++) {
            JSONObject jobj = jsonreminders.getJSONObject(i);
            int usiahamil = Integer.parseInt(jobj.getString("usiakehamilan"));
            int jumkunjung = Integer.parseInt(jobj.getString("jumlahkunjungan"));
            int selesai = Integer.parseInt(jobj.getString("selesai"));
            int dropout = Integer.parseInt(jobj.getString("dropout"));
            String resiko = jobj.getString("resiko");
            String ket = "";
            Boolean masuk = false;
            if (usiahamil < 14 && jumkunjung < 1)
                masuk = true;
            if (usiahamil>=14 && usiahamil<28 && jumkunjung < 2)
                masuk = true;
            if (usiahamil>=28 && usiahamil<=36 && jumkunjung < 2)
                masuk = true;
            if (usiahamil==36){
                masuk = true;
                ket = "(Deadline)";
            }
            if (usiahamil > 42){
                masuk = true;
                ket = "(Post term)";
            }
            if (resiko.equals("Resiko tinggi"))
                masuk = true;

            if (masuk)
                reminderCards.add(new ReminderCard(
                        jobj.getString("nama"),
                        resiko,
                        "usia kehamilan "+usiahamil+" minggu "+ket,
                        "jumlah kunjungan "+jumkunjung,
                        usiahamil,
                        jobj.getString("idkehamilan"),
                        jobj.getString("selesai"),
                        jobj.getString("dropout"),
                        jobj.getString("carapersalinan"),
                        jobj.getString("tanggalpersalinan"),
                        jobj.getString("status"),
                        jobj.getString("rujuk"),
                        jobj.getString("keadaanibu"),
                        jobj.getString("keadaanbayi"),
                        jobj.getString("catatan"),
                        jobj.getString("jumlahkunjungan")
                ));

        }
        RecycleViewAdapter rva = new RecycleViewAdapter(reminderCards, bornclicked,dropclicked);
        recyclerView.setAdapter(rva);

    }

    public static class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ReminderCardViewHolder>{
        List<ReminderCard> reminderCards;
        OnItemClickListener listener;
        OnDropOutClickListener listener2;

        public interface OnItemClickListener{
            void onItemClick(ReminderCard item);
        }

        public interface OnDropOutClickListener{
            void onDeleteClick(ReminderCard item);
        }

        public RecycleViewAdapter(List<ReminderCard> reminderCards, OnItemClickListener listener, OnDropOutClickListener listener2) {
            this.reminderCards = reminderCards;
            this.listener = listener;
            this.listener2 = listener2;
        }

        @Override
        public ReminderCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_card,parent, false);
            ReminderCardViewHolder sdklfj = new ReminderCardViewHolder(view);
            return sdklfj;
        }

        @Override
        public void onBindViewHolder(ReminderCardViewHolder holder, int position) {
            holder.nama.setText(reminderCards.get(position).nama);
            holder.risk.setText(reminderCards.get(position).resiko);
            holder.kunjungan.setText(reminderCards.get(position).term);
            holder.term.setText(reminderCards.get(position).kunjungan);
            if (reminderCards.get(position).dropout.equals("1"))
                holder.doordone.setText("Droped Out");
            if (reminderCards.get(position).selesai.equals("1")) {
                holder.doordone.setText("Sudah Bersalin");
                holder.born.setText("EDIT DATA");
            }
            if (reminderCards.get(position).dropout.equals("1"))
                holder.drop.setText("UNDROP OUT");

            holder.bind(reminderCards.get(position),listener,listener2);
        }

        @Override
        public void onViewAttachedToWindow(ReminderCardViewHolder holder) {
            super.onViewAttachedToWindow(holder);
        }

        @Override
        public int getItemCount() {
            return reminderCards.size();
        }

        public class ReminderCardViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView risk;
            TextView term;
            TextView kunjungan;
            TextView nama;
            TextView doordone;
            TextView drop;
            TextView born;


            public ReminderCardViewHolder(View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.kartu);
                risk = (TextView) itemView.findViewById(R.id.risk);
                term = (TextView) itemView.findViewById(R.id.term);
                kunjungan = (TextView) itemView.findViewById(R.id.kunjungan);
                nama = (TextView) itemView.findViewById(R.id.nama);
                doordone = (TextView) itemView.findViewById(R.id.doordone);

                drop = (TextView) itemView.findViewById(R.id.dropout);
                born = (TextView) itemView.findViewById(R.id.melahirkan);
            }

            public void bind(final ReminderCard item,final OnItemClickListener listener, final OnDropOutClickListener listener2){
                born.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(item);
                    }
                });
                drop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener2.onDeleteClick(item);
                    }
                });
            }
        }
    }

    private class Request extends RequestDatabase{

        public Request(HashMap<String, String> data) {
            super(data);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dagoan.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("PHP", "onPostExecute: \n "+s);
            try {
                setRecyclerViewData(s);
            } catch (JSONException e) {
                Toast.makeText(getBaseContext(),s,Toast.LENGTH_LONG).show();
            }
            dagoan.dismiss();
        }
    }

    public void deleteCard(ReminderCard deleted){
        ReminderCard item = null;
        for (int i = 0; i < reminderCards.size(); i++) {
            if (reminderCards.get(i).idkehamilan.equals(deleted.idkehamilan)) {
                item = reminderCards.get(i);
                if (item.dropout.equals("1"))
                    item.dropout = "0";
                else
                    item.dropout = "1";
            }
        }

        RecycleViewAdapter rva = new RecycleViewAdapter(reminderCards,bornclicked,dropclicked);
        recyclerView.setAdapter(rva);
    }

    private  class Request2 extends RequestDatabase{

        ReminderCard deleted;

        public Request2(HashMap<String, String> data,ReminderCard deleted) {
            super(data);
            this.deleted = deleted;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dagoan.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getBaseContext(),s,Toast.LENGTH_LONG).show();
            if (s.equals("Droped Out"))
                deleteCard(deleted);
            dagoan.dismiss();

        }
    }

}
