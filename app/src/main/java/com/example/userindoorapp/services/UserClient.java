package com.example.userindoorapp.services;

import com.example.userindoorapp.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserClient {

    @GET("auth")
    Call<User> getUser(@Header("Authorization") String authToken);
}
