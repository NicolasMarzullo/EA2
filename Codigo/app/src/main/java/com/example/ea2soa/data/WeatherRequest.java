package com.example.ea2soa.data;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ea2soa.R;
import com.example.ea2soa.data.model.Event;
import com.example.ea2soa.services.OpenWeatherMapService;
import com.example.ea2soa.services.SoaService;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherRequest {
    private static final String TAG = "WeatherRequest";
    private Retrofit retrofit;
    private OpenWeatherMapService openWeatherMapService;
    private Context context;

    public WeatherRequest(Context context) {

        this.retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(context.getResources().getString(R.string.server_weather))
                .build();

        this.openWeatherMapService = retrofit.create(OpenWeatherMapService.class);
        this.context = context;
    }


    /**
     * Realiza la request a la APi para obtener los datos del clima
     */
    public void getDataWeather(final int city_id, final ArrayList<TextView> outputs) {

        Call<WeatherResponse> call = this.openWeatherMapService.getWeather(city_id, this.context.getResources().getString(R.string.app_id_open_weather_map), "es");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Clima obtenido correctamente !" + response.body().toString());


                    //Pongo los datos en la vista

                    outputs.get(0).setText(String.format("%.1f", kelvinToCelsius(response.body().getMain().getTemp_min())) + " °C"); //Temperatura mínima
                    outputs.get(1).setText(String.format("%.1f", kelvinToCelsius(response.body().getMain().getTemp_max())) + " °C"); //Temperatura maxima
                    outputs.get(2).setText(String.valueOf(response.body().getMain().getHumidity()) + " %"); //Humedad
                    outputs.get(3).setText(String.format("%.1f", kelvinToCelsius(response.body().getMain().getFeels_like())) + " °C"); //Sensacion térmica
                    outputs.get(4).setText(String.format("%.1f", kelvinToCelsius(response.body().getMain().getTemp())) + " °C"); //Temperatura
                    outputs.get(5).setText("Estado del clima: " + response.body().getWeaters().get(0).getDescription()); //Descripcion del clima

                    showOutputs(outputs);
                } else {
                    Log.e(TAG, "Hubo un problema al intentar obtener el clima" + response.errorBody().toString());
                    hideOutputs(outputs);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(context, "Ups, el servidor del clima no está operativo :(", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Error en la respuesta del servidor del clima: " + t.getMessage().toString());
                hideOutputs(outputs);
            }
        });
    }

    public Double kelvinToCelsius(Double kelvin) {
        return kelvin - 273.15;
    }

    public void showOutputs(ArrayList<TextView> outputs){
        for (int i = 0; i < outputs.size(); i++) {
            outputs.get(i).setVisibility(View.VISIBLE);
        }
    }

    public void hideOutputs(ArrayList<TextView> outputs){
        for (int i = 0; i < outputs.size(); i++) {
            outputs.get(i).setVisibility(View.INVISIBLE);
        }
    }
}
