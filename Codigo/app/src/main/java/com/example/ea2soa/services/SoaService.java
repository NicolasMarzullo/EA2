package com.example.ea2soa.services;


import com.example.ea2soa.data.SoaResponse;
import com.example.ea2soa.data.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface SoaService {
    @Headers("Content-Type: application/json")
    @POST("api/register")
    Call<SoaResponse> register(@Body User user);

    @Headers("Content-Type: application/json")
    @POST("api/login")
    Call<SoaResponse> login(@Body User user);
}

