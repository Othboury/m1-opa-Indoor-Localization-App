package com.example.userindoorapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.userindoorapp.R;
import com.example.userindoorapp.ScanTaskP;
import com.example.userindoorapp.WifiReceiver;
import com.example.userindoorapp.model.Wifi;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class ScanActivity extends AppCompatActivity {
    EditText textRoom;
    Button buttonScan;
    EditText textLink;
    EditText textPort;
    private WifiManager wifiManager;
    private ListView wifiList;
    private final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;
    WifiReceiver receiverWifi;
    String roomNumber;
    String link;
    String port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scn);
        textRoom = findViewById(R.id.roomText);
        buttonScan = findViewById(R.id.scanNewBtn);
        textLink = findViewById(R.id.linkText);
        textPort = findViewById(R.id.txtPort);
        textLink.setEnabled(false);
        textPort.setEnabled(false);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(getApplicationContext(), "Turning WiFi ON...",
                    Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        textRoom.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                roomNumber = textRoom.getText().toString();
                enableTxtLink();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

        textLink.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                link = textLink.getText().toString();
                enablePort();

            }
        });

        textPort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                port = textPort.getText().toString();
                enableBtnStartScan();
                launchScan();
            }
        });
    }

    public void enableTxtLink(){
        textLink.setEnabled(true);
    }
    public void enablePort(){
        textPort.setEnabled(true);
    }
    public void enableBtnStartScan(){
        buttonScan.setEnabled(true);
    }

    public boolean scanReq() throws ExecutionException, InterruptedException {
        String roomNumber = textRoom.getText().toString();
        launchScan();
        JsonObject roomJson = new JsonObject();
        boolean done = false;
        List<Wifi> listWifi = receiverWifi.newScanList();
        if(!roomNumber.equals("") && !link.equals("") && !port.equals("")) {
            while (!done) {
                for (Wifi wifi : listWifi) {
                    wifi.setIdsalle(roomNumber);
                    roomJson.addProperty("bssid", wifi.getBssid());
                    roomJson.addProperty("centrefrequence0", wifi.getCenterFreq0());
                    roomJson.addProperty("frequency", wifi.getFrequency());
                    roomJson.addProperty("level", wifi.getLevel());
                    roomJson.addProperty("ssid", wifi.getSsid());
                    roomJson.addProperty("date", wifi.getDate());
                    roomJson.addProperty("salle", wifi.getIdsalle());

                    ScanTaskP ScanTaskP = new ScanTaskP();
                    if (ScanTaskP.execute(roomJson, link, port).get()) {
                        ScanTaskP.cancel(true);
                    }
                }
                done = true;
            }
        }else{
            Toast.makeText(ScanActivity.this, "Remplissez tous les champs!",
                    Toast.LENGTH_SHORT).show();
        }
        if(done){
            return true;
        }else{
            return false;
        }
    }

    public void launchScan(){
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ScanActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            ScanActivity.this, new String[]{Manifest.permission
                                    .ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
                } else {
                    wifiManager.startScan();

                    if(receiverWifi.ShowString() == null){
                        Toast.makeText(ScanActivity.this, "No Wifi data found.",
                                Toast.LENGTH_SHORT).show();
                    }else{
                        try {
                            if(scanReq()) {
                                Toast.makeText(ScanActivity.this,
                                        "Wifi data collected and stored...",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        receiverWifi = new WifiReceiver(wifiManager, wifiList);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(receiverWifi, intentFilter);
        getWifi();
    }

    private void getWifi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(ScanActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ScanActivity.this, new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
            } else {
                wifiManager.startScan();
            }
        } else {
            Toast.makeText(ScanActivity.this, "scanning", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiverWifi);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(ScanActivity.this, "permission granted",
                            Toast.LENGTH_SHORT).show();
                    //launchScan();
                } else {
                    Toast.makeText(ScanActivity.this, "permission not granted",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
        }
    }
}