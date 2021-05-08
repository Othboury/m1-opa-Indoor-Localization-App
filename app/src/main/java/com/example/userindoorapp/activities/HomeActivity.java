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
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.userindoorapp.HTTPReqTaskP;
import com.example.userindoorapp.R;
import com.example.userindoorapp.WifiReceiver;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Home Activity
 * @author Othmane
 *
 * **/

public class HomeActivity extends AppCompatActivity {

    private ListView wifiList;
    private WifiManager wifiManager;
    private final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;
    WifiReceiver receiverWifi;
    Button btnLocate;
    Button btnScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnLocate= findViewById(R.id.btnHttp);
        btnScan = findViewById(R.id.btnScanUser);

        /* Get the user's logged in Token passed from the Login activity*/
        Intent intent = getIntent();
        String Token = intent.getStringExtra("Token");

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(getApplicationContext(), "Turning WiFi ON...", Toast.LENGTH_LONG)
                    .show();
            wifiManager.setWifiEnabled(true);
        }

        /**
         *
         * Locate button Listener, allows to Locate the user by collecting the Wifi Network data
         * and sending POST it to the prediction model's server "httpReq()" before sending the
         * result in an intent into the prediction activity
         *
         * @author Othmane
         *
         * **/
        btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject salle = null;
                try {
                    salle = httpReq();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(salle != null){
                    Intent myIntent =
                            new Intent(HomeActivity.this, PredictionActivity.class);
                    myIntent.putExtra("salle", salle.toString()); //Optional parameters
                    HomeActivity.this.startActivity(myIntent);
                }else{
                    Intent myIntent =
                            new Intent(HomeActivity.this, PredictionActivity.class);
                    myIntent.putExtra("salle", "No Data"); //Optional parameters
                    HomeActivity.this.startActivity(myIntent);                }
            }
        });

        /**
         *
         * Scan button Listener, allows to redirect the user to the scan activity in order to scan
         * new Wifi Networks and save them into the DB via Api
         *
         * @author Othmane
         *
         * **/
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(HomeActivity.this, ScanActivity.class);
                HomeActivity.this.startActivity(myIntent);
            }
        });
    }

    /**
     *
     * httpReq() function: it launches the POST Task by calling an instance from HTTPReqTaskP
     * and executing it
     *
     * returns String
     * @author Othmane
     *
     * **/
    public JSONObject httpReq() throws ExecutionException, InterruptedException {
        launchScan();
        HTTPReqTaskP httpReqTaskP = new HTTPReqTaskP();
        JSONObject statSalle = httpReqTaskP.execute(receiverWifi.jsList()).get();
        return statSalle;
    }

    /**
     *
     * LaunchScan() function : it launches a WIFI Network scan in order to locate the user
     *
     * returns void
     * @author Othmane
     *
     * **/
    public void launchScan(){
                if (ActivityCompat.checkSelfPermission(HomeActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            HomeActivity.this, new String[]{
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
                } else {
                    wifiManager.startScan();
                    if(receiverWifi.ShowString() == null){
                        Toast.makeText(HomeActivity.this, "EMPTY", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
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

            Toast.makeText(HomeActivity.this, "version> = marshmallow",
                    Toast.LENGTH_SHORT).show();
            if (ContextCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(HomeActivity.this, "location turned off",
                        Toast.LENGTH_SHORT)
                        .show();
                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
            } else {
                Toast.makeText(HomeActivity.this, "location turned on",
                        Toast.LENGTH_SHORT)
                        .show();
                wifiManager.startScan();
            }
        } else {
            Toast.makeText(HomeActivity.this, "scanning", Toast.LENGTH_SHORT).show();
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
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(HomeActivity.this, "permission granted",
                            Toast.LENGTH_SHORT)
                            .show();
                    launchScan();
                } else {
                    Toast.makeText(HomeActivity.this, "permission not granted",
                            Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                break;
        }
    }


}