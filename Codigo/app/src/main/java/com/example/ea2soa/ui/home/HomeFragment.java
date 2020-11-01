package com.example.ea2soa.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.ea2soa.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView txtLastValueAccelerometer;
    private TextView txtLastValueGyroscope;
    private TextView txtViewAcelerometro;
    private TextView txtViewGiroscopo;
    private SharedPreferences sharedPrefs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        this.txtLastValueAccelerometer = root.findViewById(R.id.txtLastValueAccelerometer);
        this.txtLastValueGyroscope =  root.findViewById(R.id.txtLastValueGyroscope);
        this.txtViewAcelerometro =  root.findViewById(R.id.txtViewAcelerometro);
        this.txtViewGiroscopo =  root.findViewById(R.id.textViewGiroscopo);

        actualizarValorSensores();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        actualizarValorSensores();
    }

    @Override
    public void onStart() {
        super.onStart();

        actualizarValorSensores();

    }

    /**
     * Actualiza los valores de los sensores que guardó el usuario
     */
    public void actualizarValorSensores(){
        this.sharedPrefs = getContext().getSharedPreferences("events", Context.MODE_PRIVATE);
        String accelerometer = this.sharedPrefs.getString("accelerometer", "");
        String gyroscope = this.sharedPrefs.getString("gyroscope", "");

        //Si estan vacios es porque nunca guardó ningun reporte de los eventos entonces oculto los text view con sus descripciones
        if(accelerometer.isEmpty()){
            this.txtLastValueAccelerometer.setVisibility(View.INVISIBLE);
            this.txtViewAcelerometro.setVisibility(View.INVISIBLE);
        }else{
            this.txtViewAcelerometro.setVisibility(View.VISIBLE);
            this.txtLastValueAccelerometer.setVisibility(View.VISIBLE);
            this.txtLastValueAccelerometer.setText(accelerometer);
        }
        if(gyroscope.isEmpty()){
            this.txtViewGiroscopo.setVisibility(View.INVISIBLE);
            this.txtLastValueGyroscope.setVisibility(View.INVISIBLE);
        }else{
            this.txtViewGiroscopo.setVisibility(View.VISIBLE);
            this.txtLastValueGyroscope.setVisibility(View.VISIBLE);
            this.txtLastValueGyroscope.setText(gyroscope);
        }
    }
}