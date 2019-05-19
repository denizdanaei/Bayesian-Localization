package com.example.k2.d2.wifiscan;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.app.Activity;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Smart Phone Sensing Example 4. Wifi received signal strength.
 */
public class MainActivity extends Activity implements OnClickListener {

    /**
     * The wifi manager.
     */
    private WifiManager wifiManager;
    /**
     * The text view.
     */
    private TextView textRssi;
    /**
     * The button.
     */
    private Button buttonRssi, buttonSort;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create items.
        textRssi = (TextView) findViewById(R.id.textRSSI);
        textRssi.setMovementMethod(new ScrollingMovementMethod());

        buttonRssi = (Button) findViewById(R.id.buttonRSSI);
        buttonRssi.setOnClickListener(this);

        buttonSort = (Button) findViewById(R.id.sortwifi);
        buttonSort.setOnClickListener(this);

        // Set listener for the button.


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buttonRSSI:
                // Set text.
                textRssi.setText("\n\tScan all access points:");
                // Set wifi manager.
                wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                // Start a wifi scan.
                wifiManager.startScan();
                // Store results in a list.
                List<ScanResult> scanResults = wifiManager.getScanResults();
                // Write results to a label
                for (ScanResult scanResult : scanResults) {
                    textRssi.setText(textRssi.getText() + "\n\tBSSID = " + scanResult.BSSID +
                           "    level = " + scanResult.level);
                }

                break;
            case R.id.sortwifi:

//                // Set wifi manager.
//                wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//                // Start a wifi scan.
//                wifiManager.startScan();
//                // Store results in a list.
//                scanResults = wifiManager.getScanResults();
//                //sortArrayList(scanResults);
//                // Write results to a label
//                for (ScanResult scanResult : scanResults) {
//                    textRssi.setText(textRssi.getText() + "\n\tBSSID = " + scanResult.BSSID +
//                            "    level = " + scanResult.level);
//                }
                Gson gsonBuilder = new GsonBuilder().create();
                // Convert Java Array into JSON
                List languagesArrayList = new ArrayList();
                languagesArrayList.add("Russian");
                languagesArrayList.add("English");
                languagesArrayList.add("French");

                String jsonFromJavaArrayList = gsonBuilder.toJson(languagesArrayList);

                System.out.println(jsonFromJavaArrayList);
                break;


        }

    }

//    public void sortedList() {
//
//        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        // Start a wifi scan.
//        wifiManager.startScan();
//        // Store results in a list.
//        List<ScanResult> scanResults = wifiManager.getScanResults();
//
//        Collections.sort(scanResults, new Comparator<ScanResult>() {
//            @Override
//            public int compare(ScanResult o1, ScanResult o2) {
//                return o1.level.compareTo(o2.level);
//            }
//
//        });
//    }

}