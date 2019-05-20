package com.example.myapplication;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
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
    private Button scan_me, training_done, read_jsonfile;

    public String cellNumber;
    ArrayList<gsonParser> allItems = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textRssi = (TextView) findViewById(R.id.textView);


        Spinner dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"cell 1", "cell 2", "cell 3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
               cellNumber =  (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        textRssi.setMovementMethod(new ScrollingMovementMethod());
        scan_me = (Button) findViewById(R.id.button2);
        scan_me.setOnClickListener(this);


        training_done = (Button) findViewById(R.id.button);
        training_done.setOnClickListener(this);

        read_jsonfile = (Button) findViewById(R.id.button3);
        read_jsonfile.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button2:
                wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager.startScan();
                List<ScanResult> scanResults = wifiManager.getScanResults();
                for (ScanResult scanResult : scanResults) {
                    allItems.add(new gsonParser(scanResult.BSSID, scanResult.level, cellNumber));
                }
                break;
            case R.id.button:
                writeFile();
                break;
            case R.id.button3:
                readFile();
                break;
        }


    }

    public void writeFile(){
        try {
            FileOutputStream fos = openFileOutput("test.json", MODE_PRIVATE);
            String json = new Gson().toJson(allItems);
            fos.write(json.getBytes());
            fos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public gsonParser[] readFile(){
        gsonParser[] items = new gsonParser[0];
        try {
            Gson gson = new Gson();
            FileInputStream fos1 = openFileInput("test.json");
            int size = fos1.available();
            byte[] buffer = new byte[size];
            fos1.read(buffer);
            String text = new String(buffer);
            items = new Gson().fromJson(text, gsonParser[].class);
            textRssi.setText(text);
        }catch (IOException e){

        }
        return items;
    }
}
