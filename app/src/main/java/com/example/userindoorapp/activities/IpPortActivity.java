package com.example.userindoorapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userindoorapp.R;

import java.util.concurrent.ExecutionException;

public class IpPortActivity extends AppCompatActivity {

    EditText textPort;
    Button buttonNext;
    EditText textLink;
    String roomNumber;
    String link;
    String port;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_port);
        buttonNext = findViewById(R.id.NextBtn);
        textLink = findViewById(R.id.linkText);
        textPort = findViewById(R.id.txtPort);
        textLink.setEnabled(true);
        textPort.setEnabled(false);
        buttonNext.setEnabled(false);

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
            }
        });

        Next();
    }

    public void enablePort(){
        textPort.setEnabled(true);
    }
    public void enableBtnStartScan(){
        buttonNext.setEnabled(true);
    }

    public void Next() {
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try {
                    NextConfirm();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean validateNext(String link, String port){
        if(port == null || port.trim().length() == 0){
            Toast.makeText(this, "Port manquant!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(link == null || link.trim().length() == 0){
            Toast.makeText(this, "Adresse IP manquante!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private String NextConfirm() throws ExecutionException, InterruptedException {
        String port = textPort.getText().toString();
        String link = textLink.getText().toString();
        if(validateNext(link, port)) {
            String url = link +":"+port;
            Intent myIntent =
                    new Intent(IpPortActivity.this, LoginActivity.class);
            myIntent.putExtra("Url", url); //Optional parameters
           IpPortActivity.this.startActivity(myIntent);
        } else {
            Toast.makeText(this, "Veuillez rensigner les informations!", Toast.LENGTH_SHORT)
                    .show();
            return null;
        }


        return null;
}
}