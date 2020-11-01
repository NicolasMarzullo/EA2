package com.example.ea2soa.services;

import com.example.ea2soa.data.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface OpenWeatherMapService {

    @Headers("Content-Type: application/json")
    @GET("weather")
    Call<WeatherResponse> getWeather(@Query("id") int city_id, @Query("appid") String appid, @Query("lang") String lang);
}
