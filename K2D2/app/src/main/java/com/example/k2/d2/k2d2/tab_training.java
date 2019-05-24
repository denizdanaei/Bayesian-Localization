package com.example.k2.d2.k2d2;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import static com.example.k2.d2.k2d2.bayesianLocalization.training_Setup;

import  static com.example.k2.d2.k2d2.MainActivity.WIFI_SERVICE

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.k2.d2.k2d2.bayesianLocalization.training_Setup;


public class tab_training  extends Fragment  implements View.OnClickListener   {

    /**
     * The wifi manager.
     */
    private WifiManager wifiManager;
    /**
     * The text view.
     */
    private TextView scanResults;
    /**
     * The button.
     */
    private Button scan_me, training_done, read_jsonfile;

    private static final String TAG= "Training Data";

    public int cellNumber;
    ArrayList<gsonParser> allItems = new ArrayList<>();
    static HashMap<String,Float[][]> pmf_data = new HashMap<>();

    static Float[] prior = {(float)1/3,(float)1/3,(float)1/3};
    static Float[] posterior = {(float)1/3,(float)1/3,(float)1/3};

    static int rowsize = 3; //  row size for training data
    static int columnsize = 30; // column size for training data
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.tab_training, container, false);

        scanResults = (TextView) view.findViewById(R.id.scan_results); // view to display the results. This is temp for debug purpose
        scanResults.setMovementMethod(new ScrollingMovementMethod());

        scan_me = (Button) view.findViewById(R.id.cellscan); // button used for scanning in each cell.
        scan_me.setOnClickListener(this);

        training_done = (Button) view.findViewById(R.id.write_JSON); // button to indicate when the training is done and
        training_done.setOnClickListener(this);

        read_jsonfile = (Button) view.findViewById(R.id.read_JSON); // button to read the JSOn file and display.
        read_jsonfile.setOnClickListener(this);


        Spinner dropdown = view.findViewById(R.id.spinner1); // spinner to display the drop down box.
        String[] items = new String[]{"cell 1", "cell 2", "cell 3"}; // items that contain the name of each cell. Currently  set to three for now.
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
//               cellNumber =  (String) parent.getItemAtPosition(position);
                cellNumber = position; // setting the position to global variable cell number.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cellscan:
                wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager.startScan();
                List<ScanResult> scanResults = wifiManager.getScanResults();
                for (ScanResult scanResult : scanResults) {
                    allItems.add(new gsonParser(scanResult.BSSID, WifiManager.calculateSignalLevel(scanResult.level,30), cellNumber));
                }
                break;
            case R.id.write_JSON:
                writeFile(0); // refer function for the passing of the parameter. Writes to a JSON file based on the argument.
                break;
            case R.id.read_JSON:
                gsonParser[] items = readFile(0);  // refer function for passing the parameter. Reads a JSON file based on the argument.
                training_Setup(items); // Main function to update the tables.
                writeFile(1); // refer function for the passing of the parameter. Writes to a JSON file based on the argument.
                break;

        }


    }
    public void writeFile(int i){
        try {
            FileOutputStream fos ;
            String json;
            if(i == 0) { //for writing the training data
                fos = openFileOutput("test.json", MODE_PRIVATE);
                json = new Gson().toJson(allItems);
            }
            else { //for writing the pmf hashmap with key -> BSSi , value -> pmf_tables.
                fos = openFileOutput("pmf_data.json", MODE_PRIVATE);
                json = new Gson().toJson(pmf_data);
            }
            fos.write(json.getBytes());
            fos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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
                fos1 =openFileInput("test.json"); // scan results.
            }
            else{
                fos1 = openFileInput("pmf_data.json"); // offline tables
            }
            size = fos1.available();
            byte[] buffer = new byte[size];
            fos1.read(buffer);
            String text = new String(buffer);
            if(i ==0){
                items = new Gson().fromJson(text, gsonParser[].class);
                scanResults.setText(text);
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




