package com.example.userindoorapp.model;

public class Wifi {
    private int idWifi;
    private String bssid;
    private String centerFreq0;
    private String frequency;
    private String level;
    private String ssid;
    private String date;
    private String idsalle;

    public Wifi(){}

    public int getIdWifi() {
        return idWifi;
    }

    public void setIdWifi(int idWifi) {
        this.idWifi = idWifi;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getCenterFreq0() {
        return centerFreq0;
    }

    public void setCenterFreq0(String centerFreq0) {
        this.centerFreq0 = centerFreq0;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIdsalle() {
        return idsalle;
    }

    public void setIdsalle(String idsalle) {
        this.idsalle = idsalle;
    }
}
