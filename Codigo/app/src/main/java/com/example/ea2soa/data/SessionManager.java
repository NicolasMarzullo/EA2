package com.example.ea2soa.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SessionManager {


    private final Context context;
    private final SharedPreferences sharedPrefs;
    //Esto debería ser enviado por el server, pero como es un valor que desconozco quedara harcodeado hasta que el server envie el tiempo de expiración del token
    private static final long time_token_expired_in_miliseconds = 1800000; // 30 minutes in miliseconds

    public SessionManager(Context context) {
        this.context = context;
        this.sharedPrefs = context.getSharedPreferences("tokens_shared_preferences", Context.MODE_PRIVATE);
    }

    /**
     * Guarda el token y el token refresh en el shared prefrences
     *
     * @param token
     * @param refreshToken
     */
    public void storeTokens(String token, String refreshToken) {
        this.sharedPrefs.edit().putString("token", token).commit();
        this.sharedPrefs.edit().putString("refreshToken", refreshToken).commit();

        Date dateToken = new Date();

        //Guardo el date del token para luego verificar si está vencido
        this.sharedPrefs.edit().putLong("timeToken", dateToken.getTime()).commit();

    }

    public Map<String, String> getTokens() {
        Map<String, String> tokens = new HashMap<String, String>();
        tokens.put("token", this.sharedPrefs.getString("token", ""));
        tokens.put("refreshToken", this.sharedPrefs.getString("refreshToken", ""));

        return tokens;
    }

    public boolean isTokenExpired() {
        Long timeToken = this.sharedPrefs.getLong("timeToken", 0);
        String token = this.sharedPrefs.getString("token", "");
        String refreshToken = this.sharedPrefs.getString("refreshToken", "");

        //Si el usuario nunca se registró, nunca va a tener un token guardado en el sharedPreferences
        //Tambien me fijo que los token existan y no esten vacíos
        if(timeToken == 0 || token.isEmpty() || refreshToken.isEmpty()){
            return true;
        }

        Date now = new Date();
        Long diff_in_ms = now.getTime() - timeToken;
        return (diff_in_ms >= time_token_expired_in_miliseconds);
    }

    /**
     * Pone en blanco los tokens. Se utiliza para el logout
     */
    public void endSession(){
        this.sharedPrefs.edit().putString("token", "").commit();
        this.sharedPrefs.edit().putString("refreshToken", "").commit();
    }

    /**
     * Guarda el email en el SharedPreferences para luego mostrarlo en el nav bar
     */
    public void storeEmail(String email){
        this.sharedPrefs.edit().putString("email", email).commit();
    }

    public String getEmail(){
        return this.sharedPrefs.getString("email", "");
    }
}

