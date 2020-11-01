package com.example.ea2soa.data.WeatherModels;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName(value="id")
    public int id;
    @SerializedName(value="main")
    public String main;
    @SerializedName(value="description")
    public String description;
    @SerializedName(value="icon")
    public String icon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
