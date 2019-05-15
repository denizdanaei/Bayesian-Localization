package com.example.k2.d2.test;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {


    EditText et_name, et_content;
    Button b_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }

        et_name = findViewById(R.id.file_name);
        et_content = findViewById(R.id.content);
        b_save = findViewById(R.id.button_save);


        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = et_name.getText().toString();
                String content = et_content.getText().toString();

                if (!filename.equals("") && !content.equals("")) {
                    saveTextAsFile(filename, content);
                }
            }
        });
    }



    private void saveTextAsFile(String filename, String content)
    {
        String fileName= filename + ".txt";

        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.close();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {

            e.printStackTrace();
            Toast.makeText(this, "File not found!", Toast.LENGTH_SHORT).show();

        }catch (IOException e){

            e.printStackTrace();
            Toast.makeText(this, "Error saving!", Toast.LENGTH_SHORT).show();

        }
    }


    public void onRequestPermissionResult(int requestCode, @NonNull String permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case 1000:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }




    }




}

