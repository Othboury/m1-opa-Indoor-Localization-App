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
        StringBuilder sb = new StringBuilder();
        ;
        String key = null;
        String header = "Salle - Pourcentage" + "\n";

        if (intent.getStringExtra("salle").equals("No Data")) {
            txtPrediction.setText(intent.getStringExtra("salle"));

        }else{


            try {
                sallePrediction = new JSONObject(intent.getStringExtra("salle"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Iterator<String> iter = sallePrediction.keys();
            while (iter.hasNext()) {
                key = iter.next();
                try {
                    sb.append(key).append("      -      ").append(new DecimalFormat("##.##").format(sallePrediction.get(key))).append("%").append("\n");

                    txtPrediction.setText(header + sb);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}