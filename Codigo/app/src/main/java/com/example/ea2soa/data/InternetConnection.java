package com.example.ea2soa.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class InternetConnection {
    private ConnectivityManager connMaanager ;


    public InternetConnection(Context context){
        this.connMaanager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }


    public boolean isInternetAvailable(){
        NetworkInfo activeNetwork = this.connMaanager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
