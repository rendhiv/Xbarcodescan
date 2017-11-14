package com.example.king.xbarcodescan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ImageButton fab;
    private File root;
    private ArrayList<File> fileList = new ArrayList<File>();
    private LinearLayout view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        // pada method onCreate, panggil fab dari xml
        fab = (ImageButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BarcodeScanner.class);
                startActivity(intent);
            }
        });

        view = (LinearLayout) findViewById(R.id.view);

        //getting SDcard root path
        root = new File(Environment.getExternalStorageDirectory(), "Barcode Result");
        getfile(root);

        for (int i = 0; i < fileList.size(); i++) {
            TextView textView = new TextView(this);
            textView.setText((i + 1) + "). Filename: " + fileList.get(i).getName());
            textView.setPadding(8, 8, 8, 8);
            textView.setTextSize(18);

            System.out.println(fileList.get(i).getName());
            view.addView(textView);
            final int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File myExternalFile = new File(Environment.getExternalStorageDirectory() + "/Barcode Result", fileList.get(finalI).getName());

                    String myData = "";
                    try {
                        FileInputStream fis = new FileInputStream(myExternalFile);
                        DataInputStream in = new DataInputStream(fis);
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        String strLine;
                        while ((strLine = br.readLine()) != null) {
                            myData = myData + strLine;
                        }
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(getResources().getString(R.string.app_name))
                            .setCancelable(false)
                            .setMessage("Filename : " + fileList.get(finalI).getName() + "\nData File : " + myData)
                            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                    Toast.makeText(MainActivity.this, "Filename: " + fileList.get(finalI).getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    public ArrayList<File> getfile(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].getName().endsWith(".txt"))

                {
                    fileList.add(listFile[i]);
                }

            }
        }
        return fileList;
    }
}