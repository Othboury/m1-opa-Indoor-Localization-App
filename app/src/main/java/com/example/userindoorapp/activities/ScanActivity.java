package com.example.userindoorapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.userindoorapp.R;
import com.example.userindoorapp.WifiReceiver;

public class ScanActivity extends AppCompatActivity {
    EditText textRoom;
    Button buttonScan;
    private WifiManager wifiManager;
    private ListView wifiList;
    private final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;
    WifiReceiver receiverWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scn);
        textRoom = findViewById(R.id.roomText);
        buttonScan = findViewById(R.id.scanBtn);
        buttonScan.setEnabled(false);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(getApplicationContext(), "Turning WiFi ON...", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        textRoom.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                String roomNumber = textRoom.getText().toString();
                enableBtnStartScan();
                launchScan();
                Toast.makeText(getApplicationContext(), "Salle: " + roomNumber, Toast.LENGTH_LONG).show();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

    }

    public void enableBtnStartScan(){
        buttonScan.setEnabled(true);
    }

    public void launchScan(){
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            ScanActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
                } else {
                    String roomNumber = textRoom.getText().toString();
                    //String floorNumber = textFloor.getText().toString();
                    //String buildingNumber = textBuilding.getText().toString();
                    wifiManager.startScan();

                    if(receiverWifi.ShowString() == null){
                        Toast.makeText(ScanActivity.this, "EMPTY", Toast.LENGTH_SHORT).show();
                    }else{
                        String sb =  receiverWifi.ShowString();
                        StringBuilder header = new StringBuilder();
                        header.append("batimentid").append(",").append("salleid").append(",").append("floorid").append(",")
                                .append("positionid").append(",").append("ssid").append(",")
                                .append("bssid").append(",").append("level").append(",").append("centrefrequence0")
                                .append(",").append("frequency");

                        /*String fullRoomNumber = "";
                        if(roomNumber.length() == 1){
                            fullRoomNumber = "00"+ roomNumber;
                        }else if(roomNumber.length() ==2){
                            fullRoomNumber = "0" + roomNumber;
                        }else{
                            fullRoomNumber = roomNumber;
                        }*/

                       /* filename = "salle-" + buildingNumber + fullRoomNumber + ".txt";
                        String dataToReplace = "N/A";
                        Pattern pattern = Pattern.compile(dataToReplace);
                        Matcher matcher = pattern.matcher(sb);
                        String result = matcher.replaceAll(roomNumber);

                        String ToReplace = "Empty";
                        Pattern patternFloor = Pattern.compile(ToReplace);
                        Matcher matcherFloor = patternFloor.matcher(result);
                        String resultFloor = matcherFloor.replaceAll(floorNumber);

                        String buildingToReplace = "NoBat";
                        Pattern patternBuilding = Pattern.compile(buildingToReplace);
                        Matcher matcherBuilding = patternBuilding.matcher(resultFloor);
                        resultBuilding = matcherBuilding.replaceAll(buildingNumber);*/
                        /*if(fileExists(MainActivity.this, filename)){
                            try {
                                FileOutputStream fOut = openFileOutput(filename,  MODE_APPEND);
                                OutputStreamWriter osw = new OutputStreamWriter(fOut);
                                osw.write(resultBuilding);
                                osw.flush();
                                osw.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else {
                            try {
                                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE));
                                outputStreamWriter.write(header+resultBuilding);
                                outputStreamWriter.close();
                            } catch (IOException e) {
                                Log.e("Exception", "File write failed: " + e.toString());
                            }
                        }*/
                    }
                    Toast.makeText(ScanActivity.this, "Wifi Data Collected and Stored...", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MainActivity.this, "File name:" + filename, Toast.LENGTH_SHORT).show();
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

            Toast.makeText(ScanActivity.this, "version> = marshmallow", Toast.LENGTH_SHORT).show();
            if (ContextCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(ScanActivity.this, "location turned off", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(ScanActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
            } else {
                Toast.makeText(ScanActivity.this, "location turned on", Toast.LENGTH_SHORT).show();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ScanActivity.this, "permission granted", Toast.LENGTH_SHORT).show();
                    launchScan();
                } else {
                    Toast.makeText(ScanActivity.this, "permission not granted", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
        }
    }
}