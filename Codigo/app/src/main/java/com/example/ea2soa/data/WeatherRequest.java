package com.example.ea2soa.data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.ea2soa.R;
import com.example.ea2soa.data.model.Event;
import com.example.ea2soa.services.OpenWeatherMapService;
import com.example.ea2soa.services.SoaService;

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
    public void getDataWeather(int city_id) {

        Call<WeatherResponse> call = this.openWeatherMapService.getWeather(city_id, this.context.getResources().getString(R.string.app_id_open_weather_map), "es");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Evento registrado correctamente !" + response.body().toString());
                    System.out.println("");
                } else {
                    Log.e(TAG, "Hubo un problema al intentar registrar un evento" + response.errorBody().toString());
                }
                //responseBody.getW
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(context, "Ups, el servidor no está operativo :(", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Error en la respuesta del servidor al intentar registrar un evento " + t.getMessage().toString());
            }
        });

    }
}
