package com.example.userindoorapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.userindoorapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class PredictionActivity extends AppCompatActivity {
    TextView txtPrediction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predicition);
        txtPrediction = findViewById(R.id.txtSalle);
        Intent intent = getIntent();
        JSONObject sallePrediction = null;
        StringBuilder sb = new StringBuilder();;
        String key = null;
        String header = "Salle - Pourcentage" + "\n";
        try {
            sallePrediction = new JSONObject(intent.getStringExtra("salle"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //System.out.println("[PREDICTION]: " +sallePrediction);
        Iterator<String> iter = sallePrediction.keys();
        while(iter.hasNext()){
            key = iter.next();
            //System.out.println("[KEYS]: "+ key);
            try {
                sb.append(key).append(" - ").append(new DecimalFormat("##.##").format(sallePrediction.get(key))).append("%").append("\n") ;
                //System.out.println("[VALUES]: "+ sallePrediction.get(key));
                //System.out.println(header + sb);
                txtPrediction.setText(header + sb);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}