package com.example.userindoorapp;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class HTTPReqTaskP extends AsyncTask<Object, Void, JSONObject> {
    @Override
    protected JSONObject doInBackground(Object... params) {
        HttpURLConnection urlConnection = null;
        JsonObject js = (JsonObject) params[0];
        JSONObject objJ = null;

        try {
            String para = (String) params[1];
            String newUrl = para.substring(0, 13) + "6000";
            URL url = new URL("http://"+newUrl+"/predict");

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    out, "UTF-8"));
            writer.write(js.toString());
            writer.flush();

            int code = urlConnection.getResponseCode();
            if (code !=  200) {
                throw new IOException("Invalid response from server: " + code);
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            String lineSalle;

            while ((lineSalle = rd.readLine()) != null) {
                objJ= new JSONObject(lineSalle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return objJ;
    }
}
