package com.example.ea2soa.data;

import com.example.ea2soa.data.WeatherModels.Main;
import com.example.ea2soa.data.WeatherModels.Weather;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WeatherResponse {

    @SerializedName("timezone")
    private int timezone;
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("code")
    private int code;

    @SerializedName("weather")
    private List<Weather> weathers = new ArrayList<Weather>();

    @SerializedName("main")
    private Main main;

    public List<Weather> getWeaters() {
        return weathers;
    }

    public void setWeaters(List<Weather> wheaters) {
        this.weathers = wheaters;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public int getTimezone() {
        return timezone;
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
