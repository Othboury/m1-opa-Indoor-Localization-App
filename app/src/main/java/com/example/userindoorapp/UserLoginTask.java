package com.example.userindoorapp;

import android.media.session.MediaSession;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.JsonReader;
import android.widget.Toast;

import com.example.userindoorapp.activities.LoginActivity;
import com.example.userindoorapp.model.User;
import com.example.userindoorapp.services.UserClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserLoginTask extends AsyncTask<String, Void, String> {
    public static final String BASE_URL = "http://10.21.46.224:8091/android/";

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson));

    Retrofit retrofit = builder.build();

    @Override
    protected String doInBackground(String... params) {
        String username = params[0];
        String password = params[1];

        UserClient userClient = retrofit.create(UserClient.class);

        String base = username + ":" + password;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        Call<User> call = userClient.getUser(authHeader);
        try {
            Response<User> response = call.execute();
            if(response.isSuccessful()) {
                return response.body().getToken();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
