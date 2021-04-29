package com.example.userindoorapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.ListView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class WifiReceiver extends BroadcastReceiver {
    WifiManager wifiManager;
    StringBuilder sb;
    ListView wifiDeviceList;
    ArrayList<String> deviceList = new ArrayList<>();
    String batimentId = "NoBat";
    String salleId = "N/A";
    String floorId= "Empty";
    int positionId = 0;
    JsonObject combined= new JsonObject();

    public WifiReceiver(WifiManager wifiManager, ListView wifiDeviceList) {
        this.wifiManager = wifiManager;
        this.wifiDeviceList = wifiDeviceList;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
            sb = new StringBuilder();
            List<ScanResult> wifiList = wifiManager.getScanResults();
            for (ScanResult scanResult : wifiList) {
                sb.append("\n").append(batimentId).append(",").append(salleId).append(",").append(floorId).append(",").append(positionId)
                        .append(",").append(scanResult.SSID).append(",").append(scanResult.BSSID).append(",").append(scanResult.level)
                        .append(",").append(scanResult.centerFreq0).append(",").append(scanResult.frequency);
                JsonObject postData= new JsonObject();
                postData.addProperty("level",scanResult.level);
                combined.add(scanResult.BSSID, postData);
            }
        }
    }

    public String ShowString(){
        if(sb == null){
            return "EMPTY";
        }else {
            return sb.toString();
        }
    }

    public JsonObject jsList(){
        return combined;
    }

}
