package com.example.ea2soa.data;


import android.content.Context;

import java.util.Date;

/**
 * Thread que consulta frecuentemente si el token está expirado, en caso de que esté expirado lo renueva
 */
public class TokenRefresher extends Thread{

    final long ms_to_wait = 500; //milisegundos de espera para volver a consultar si el token está expirado
    SessionManager sessionManager;
    Context context;
    AuthorizedRequest authorizedRequest;

    public TokenRefresher(Context context) {
        this.context = context;
        this.sessionManager = new SessionManager(this.context);
        this.authorizedRequest = new AuthorizedRequest(this.context);
    }

    /**
     * Metodo que queda ejecutando en loop inifinito por el while true
     */
    public void run(){
        Long time = new Date().getTime();
        //this.sessionManager = new SessionManager();


        while (true && !this.isInterrupted()){
            Long currentTime = new Date().getTime();

            if((currentTime - time) >= ms_to_wait){ // Verifico el token cada 500 ms (medio segudo)
                time = currentTime;

                if(this.sessionManager.isTokenExpired()){
                    this.authorizedRequest.refreshToken();
                }
            }
        }
    }

}
