package com.example.userindoorapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.userindoorapp.R;

import java.util.concurrent.ExecutionException;

public class PredictionActivity extends AppCompatActivity {
    TextView txtPrediction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predicition);
        txtPrediction = findViewById(R.id.txtSalle);
        Intent intent = getIntent();
        String sallePrediction = intent.getStringExtra("salle");
        txtPrediction.setText(sallePrediction);

    }
}