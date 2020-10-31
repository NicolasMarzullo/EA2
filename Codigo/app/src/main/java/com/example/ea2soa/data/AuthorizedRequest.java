package com.example.ea2soa.data;

import android.content.Context;
import android.content.res.Resources;
import android.se.omapi.Session;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ea2soa.R;
import com.example.ea2soa.data.model.Event;
import com.example.ea2soa.services.SoaService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Esta clase maneja las request al servidor que son autorizadas
 */
public class AuthorizedRequest {
    private static final String TAG = "AuthorizedRequest";
    private Retrofit retrofit;
    private SoaService soaService;
    private Context context;
    private SessionManager sessionManager;

    public AuthorizedRequest(Context context) {

        this.retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(context.getResources().getString(R.string.retrofit_server))
                .build();

        this.soaService = retrofit.create(SoaService.class);
        this.context = context;
        this.sessionManager = new SessionManager(this.context);
    }

    /**
     * Realiza la request al server para registrar un evento
     *
     * @param event
     */
    public void registerEvent(Event event) {

        if (!this.sessionManager.isTokenExpired()) {
            Map<String, String> tokens = this.sessionManager.getTokens();
            Call<SoaResponse> call = soaService.event(event, "Bearer " + tokens.get("token"));
            call.enqueue(new Callback<SoaResponse>() {
                @Override
                public void onResponse(Call<SoaResponse> call, Response<SoaResponse> response) {
                    if(response.isSuccessful()){
                        Log.i(TAG, "Evento registrado correctamente !" + response.errorBody().toString());
                    }else{
                        Log.e(TAG, "Hubo un problema al intentar registrar un evento" + response.errorBody().toString());
                    }
                }

                @Override
                public void onFailure(Call<SoaResponse> call, Throwable t) {
                    Toast.makeText(context, "Ups, el servidor no está operativo :(", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error en la respuesta del servidor al intentar registrar un evento " + t.getMessage().toString());
                }
            });
        }else{
            this.refreshToken();;
        }
    }

    public void refreshToken(){
        Map<String, String> tokens = this.sessionManager.getTokens();
        Call<SoaResponse> call = soaService.refresh_token(tokens.get("refreshToken"));

        call.enqueue(new Callback<SoaResponse>() {
            @Override
            public void onResponse(Call<SoaResponse> call, Response<SoaResponse> response) {

                if(response.isSuccessful()){
                    Log.i(TAG, "Token refrescado correctamente!!");
                    sessionManager.storeTokens(response.body().getToken(), response.body().getToken_refresh());
                }else{
                    Log.e(TAG, "Hubo un problema al intentar refrescar el token" + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<SoaResponse> call, Throwable t) {
                Toast.makeText(context, "Ups, el servidor no está operativo :(", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Error en la respuesta del servidor al intentar refrescar el token " + t.getMessage().toString());
            }
        });
    }
}
