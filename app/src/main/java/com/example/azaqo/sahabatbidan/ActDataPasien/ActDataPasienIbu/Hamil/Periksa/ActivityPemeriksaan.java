package com.example.azaqo.sahabatbidan.ActDataPasien.ActDataPasienIbu.Hamil.Periksa;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.azaqo.sahabatbidan.MainActivity;
import com.example.azaqo.sahabatbidan.R;
import com.example.azaqo.sahabatbidan.RequestDatabase;

import java.util.Date;
import java.util.HashMap;

public class ActivityPemeriksaan extends AppCompatActivity implements PemeriksaanListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     *
     * acrivity ini berisi fragment untuk melihat data ibu dan pemeriksaan
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static HashMap<String,String> datapemeriksaanAll = new HashMap<>();

    public static HashMap<String, String> getDatapemeriksaanAll() {
        return datapemeriksaanAll;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemeriksaan);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Intent ten = getIntent();
        if(ten.hasExtra("tanggalperiksa")) datapemeriksaanAll.put("tanggalperiksa",ten.getStringExtra("tanggalperiksa"));
        else datapemeriksaanAll.put("tanggalperiksa",new Date().toString());

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

    @Override
    public void kumpulinData(HashMap<String, String> data) {
        datapemeriksaanAll.putAll(data);
    }

    @Override
    public void uploadData() {
        Log.d("PHP", "uploadData: "+datapemeriksaanAll);
        new Request(datapemeriksaanAll,0).execute("http://sahabatbundaku.org/request_android/update_pemeriksaan.php");
    }

    @Override
    public void uploadDataBaru() {
        Log.d("PHP", "uploadDataBaru: "+datapemeriksaanAll);
        new Request(datapemeriksaanAll,1).execute("http://sahabatbundaku.org/request_android/insert_pemeriksaan.php");
    }

    public void lihatResume(){
        Intent ten = new Intent(this,ResumeActivity.class);
        ten.putExtra("datapemeriksaanAll",datapemeriksaanAll);
        startActivity(ten);
    }

    public void backToRekamPeriksa(){
        Intent retIntent = new Intent();
        setResult(Activity.RESULT_OK,retIntent);
        finish();
    }

    @Override
    public void geser() { mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);}


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Intent ten  = getIntent();
            return PemeriksaanAmnesa.newInstance(position,ten.getStringExtra("idpemeriksaan"),ten.getStringExtra("idkehamilan"),ten.getStringExtra("idpemeriksaanterbaru"));

        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return "Riwayat kehamilan";
                case 1: return "Riwayat penyakit";
                case 2: return "Keluhan";
                case 4: return "Tindakan";
                case 3: return "Pemeriksaan";
            }
            return null;
        }
    }
    public class Request extends RequestDatabase{
        int flag;
        public Request(HashMap<String, String> data,int flag) {
            super(data);
            this.flag = flag;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            switch (flag){
                case 0:
                    Toast.makeText(getBaseContext(),s,Toast.LENGTH_SHORT).show();
                    if (s.equals("Data Berhasil Dimasukkan")) lihatResume();
                    break;
                case 1:
                    Toast.makeText(getBaseContext(),s,Toast.LENGTH_SHORT).show();
                    if (s.equals("Data Berhasil Dimasukkan")) backToRekamPeriksa();

            }
        }
    }
}
