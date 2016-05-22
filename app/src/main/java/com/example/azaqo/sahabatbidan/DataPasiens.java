package com.example.azaqo.sahabatbidan;

import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataPasiens extends AppCompatActivity implements PairingDialog.NoticeDialogFragment,TanyaDialog.ApaYangTerjadi {

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
    public ViewPager mViewPager;
    String bidan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pasiens);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        SharedPreferences sp = getSharedPreferences("Data Dasar",MODE_PRIVATE);
        bidan = sp.getString("SESSION_LOGIN","Bidan");

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }

    public void pairing(View v){
        DialogFragment dialogFragment = new PairingDialog();
        dialogFragment.show(getFragmentManager(),"PairingDialog");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        SearchManager sm = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView sv = (SearchView) menu.findItem(R.id.search).getActionView();
        sv.setSearchableInfo(sm.getSearchableInfo(getComponentName()));
        final DataPasiens act = this;


        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new HubunganAtas("http://sahabatbundaku.org/request_android/search_ibu.php",act).execute(new String[]{"cari","uname"},new String[]{query,bidan});
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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

    @Override
    public void onSimpanPressed(DialogFragment dialog, String uname, String passwd) {
        new HubunganAtas("http://sahabatbundaku.org/request_android/pairing_ibubidan.php",this).execute(new String[]{"usernameibu","passwd","usernamebidan"},new String[]{uname,passwd,bidan});
        new HubunganAtas("http://sahabatbundaku.org/request_android/search_ibu.php",this).execute(new String[]{"uname"},new String[]{bidan});
    }

    @Override
    public void hapuspressed(String uname) {
        new HubunganAtas("http://sahabatbundaku.org/request_android/unpairing_ibubidan.php",this).execute(new String[]{"usernameibu","usernamebidan"},new String[]{uname,bidan});
        new HubunganAtas("http://sahabatbundaku.org/request_android/search_ibu.php",this).execute(new String[]{"uname"},new String[]{bidan});
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        ListView datapasien;
        String bidan;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView;
            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_data_pasiens, container, false);
                    datapasien = (ListView) rootView.findViewById(R.id.listPasien);

                    SharedPreferences sp = getActivity().getSharedPreferences("Data Dasar",MODE_PRIVATE);
                    bidan = sp.getString("SESSION_LOGIN","Bidan");

                    new HubunganAtas(this,"http://sahabatbundaku.org/request_android/search_ibu.php").execute(new String[]{"uname"},new String[]{bidan});
                    break;
                default:
                    rootView = inflater.inflate(R.layout.belum_tersedia, container, false);

            }
            return rootView;
        }

        public void setDatapasien(String jsonpasien) throws JSONException {
            Log.d("PHP", "setDatapasien: "+jsonpasien);
            JSONArray pasienss = new JSONArray(jsonpasien);
            JSONObject pasien;
            List<Map<String,String>> data = new ArrayList<>();
            Map<String,String> datum;
            for (int i = 0; i < pasienss.length(); i++) {
                pasien = pasienss.getJSONObject(i);
                datum = new HashMap<>();
                datum.put("line1",pasien.getString("nama")+" ("+pasien.getString("username")+")");
                datum.put("line2",pasien.getString("umur")+" tahun");
                data.add(datum);
            }

            final SimpleAdapter adapter = new SimpleAdapter(
                    getActivity(),
                    data,
                    android.R.layout.simple_list_item_2,
                    new String[]{"line1","line2"},
                    new int[]{android.R.id.text1,android.R.id.text2});

            datapasien.setAdapter(adapter);
            datapasien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //menampilakn data kehamilan-kehamilan
                    HashMap<String,String> o =  (HashMap<String, String>) parent.getItemAtPosition(position);
                    //mendapatkan username di dalam kurung
                    Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(o.get("line1"));
                    String uname = "";
                    while(m.find()) {
                        uname = m.group(1);
                    }
                    Log.d("PHP", "onItemClick: "+uname);
                    Intent ten = new Intent(getContext(),DataLengkapIbu.class);
                    ten.putExtra("unameibu",uname);
                    startActivity(ten);
                }
            });
            datapasien.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    //menampilakn data kehamilan-kehamilan
                    HashMap<String,String> o =  (HashMap<String, String>) parent.getItemAtPosition(position);
                    //mendapatkan username di dalam kurung
                    Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(o.get("line1"));
                    String uname = "";
                    while(m.find()) {
                        uname = m.group(1);
                    }
                    DialogFragment df = TanyaDialog.newInstance(uname);
                    df.show(getActivity().getFragmentManager(),"hapusga");
                    return true;
                }
            });
        }
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
                    return PlaceholderFragment.newInstance(1);
                case 1:
                    return PlaceholderFragment.newInstance(2);
                case 2:
                    return PlaceholderFragment.newInstance(3);
            }
            return new PlaceholderFragment();

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Ibu Hamil";
                case 1:
                    return "Ibu Nifas";
                case 2:
                    return "Keluarga Berencana";
            }
            return null;
        }
    }
}
