package com.example.azaqo.sahabatbidan.ActDataPasien.ActDataPasienIbu.Hamil;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.azaqo.sahabatbidan.ActDataPasien.DataPasiens;
import com.example.azaqo.sahabatbidan.HubunganAtas;
import com.example.azaqo.sahabatbidan.Okdeh;
import com.example.azaqo.sahabatbidan.R;
import com.example.azaqo.sahabatbidan.ActDataPasien.HapusDialog;

import java.io.IOException;
import java.util.HashMap;

public class DataLengkapIbu extends AppCompatActivity
        implements DataLengkapIbuListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    String usernameibu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_lengkap_ibu);

        usernameibu = getIntent().getStringExtra("unameibu");

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

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

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void hapuspressed(String uname) {
//        HashMap<String,String> send = new  HashMap<>();
//        send.put("ke",uname+"");
//        send.put("unameibu",usernameibu);
//        new Request(send,"http://sahabatbundaku.org/request_android/hapus_kehamilan.php","tambah").execute();
//        //refresh setelah hapus
//        HashMap<String,String> send2 = new HashMap<>();
//        send2.put("unameibu",usernameibu);
//        new Request(send2,"http://sahabatbundaku.org/request_android/get_kehamilan.php","update").execute();
//
//    }

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
                case 0: return FragmentDataLengkapIbu.newInstance(usernameibu);
                case 1: return DaftarKehamilanFragment.newInstance(usernameibu);
                default: return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return "Data Lengkap Ibu";
                case 1: return "Data Kehamilan";
            }
            return null;
        }
    }


    public class Request extends AsyncTask<String,Void,Void>{
        HashMap<String,String> data;
        String url;
        String result;
        String flag;

        public Request(HashMap<String, String> data, String url,String flag) {
            this.data = data;
            this.url = url;
            this.flag = flag;
        }

        @Override
        protected Void doInBackground(String... params) {
            Okdeh ok = new Okdeh();
            try {
                if (data!=null) result = ok.doPostRequestData(url,data);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (flag.equals("update")){
                Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + mViewPager.getCurrentItem());
                if (mViewPager.getCurrentItem() == 0 && page != null) ((DaftarKehamilanFragment)page).setDatadatakehamilan(result);
            }
            if (flag.equals("tambah")) {
                Toast.makeText(getBaseContext(),result,Toast.LENGTH_SHORT);
            }
        }
    }
}
