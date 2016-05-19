package com.example.azaqo.sahabatbidan;

import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.Map;

public class ActivityPemeriksaan extends AppCompatActivity implements PemeriksaanListener{

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
    String bidan;
    HashMap<String,String> datapemeriksaanAll = new HashMap<>();

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

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        SharedPreferences sp = getSharedPreferences("Data Dasar",MODE_PRIVATE);
        bidan = sp.getString("SESSION_LOGIN","Bidan");

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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void kumpulinData(HashMap<String, String> data) {
        datapemeriksaanAll.putAll(data);
    }

    @Override
    public void uploadData() {
        //upload semua data pemeriksaan pakai kelas HubunganAtas
        //get dan set pemeriksaan dilakukan oleh masing masing fragment
        new HubunganAtas(this,"http://sahabatbundaku.org/request_android/riwayat_hamil.php",datapemeriksaanAll,"riwayat")
                .execute();

    }

    @Override
    public void geser() {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
    }


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
            switch (position){
                case 0:
                    return FragmentDataLengkapIbu.newInstance(getIntent().getStringExtra("unameibu"));
                case 5:
                    return PemeriksaanAmnesa.newInstancedataperiksa(position,datapemeriksaanAll);
                default:
                    return PemeriksaanAmnesa.newInstance(position,getIntent().getStringExtra("unameibu"),bidan);
            }
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return "Data Lengkap";
                case 1: return "Kehamilan";
                case 2: return "Penyakit";
                case 3: return "Keluhan";
                case 4: return "Pemeriksaan";
                case 5: return "Hasil";
            }
            return null;
        }
    }
}
