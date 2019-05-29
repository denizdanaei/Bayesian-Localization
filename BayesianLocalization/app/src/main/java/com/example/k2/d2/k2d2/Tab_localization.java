package com.example.k2.d2.k2d2;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.k2.d2.k2d2.ui.main.SectionsPagerAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.k2.d2.k2d2.Tab_training.pmf_data;

import static com.example.k2.d2.k2d2.bayesianLocalization.localize;
import static com.example.k2.d2.k2d2.bayesianLocalization.training_Setup;

public class Tab_localization extends Fragment implements View.OnClickListener {

    private WifiManager wifiManager;

    ImageButton localize;

    TextView cellnumber;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_localization, container, false);

        cellnumber=(TextView) rootView.findViewById(R.id.cellnumber);
        // view to display the results. This is temp for debug purpose
        //cellnumber.setMovementMethod(new ScrollingMovementMethod());

        localize = (ImageButton) rootView.findViewById(R.id.locate_me); // button used for scanning in each cell.
        localize.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onClick(View v) {
        cellnumber.setText("Loading...");
        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();
        readFile(1); // for debug purpose i have set this to one to read pmf_data from pmf_json file locally.

        List<ScanResult> localize_scanResults = wifiManager.getScanResults();
        cellnumber.setText("Loading...");
        int location =localize(localize_scanResults); // for localization purposes.
        location = location +1; // adding one since this takes cell 1 as zero.
        cellnumber.setText("You are in cell "+ location);
    }
    /*
        Function to read JSON files.
        Input 0 reads the scan results
        Input !=0 reads the pmf data --> used for offline storage. This will be used for localization.
        So that we don't have to create/store tables every time for localization.
     */
    public gsonParser[] readFile(int i){
        gsonParser[] items = new gsonParser[0];
        try {
            FileInputStream fos1;
            int size;
            if (i == 0) {
                fos1 = getContext().openFileInput("test.json"); // scan results.
            }
            else{
                fos1 = getContext().openFileInput("pmf_data.json"); // offline tables
            }
            size = fos1.available();
            byte[] buffer = new byte[size];
            fos1.read(buffer);
            String text = new String(buffer);
            if(i ==0){
                items = new Gson().fromJson(text, gsonParser[].class);
//                textRssi.setText(text);
                return items;
            }
            else{
                Type type = new TypeToken<HashMap<String, Float[][]>>(){}.getType();
                pmf_data = new Gson().fromJson(text,type);
                return items;
            }


        }catch (IOException e){

        }
        return items;
    }


}
