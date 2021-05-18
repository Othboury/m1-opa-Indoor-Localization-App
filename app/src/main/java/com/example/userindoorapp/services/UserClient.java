package com.example.userindoorapp.services;

import com.example.userindoorapp.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserClient {
    @POST("auth")
    Call<User> getUser(@Header("Authorization") String authToken);
}
