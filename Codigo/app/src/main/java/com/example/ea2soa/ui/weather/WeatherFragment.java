package com.example.ea2soa.ui.weather;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.ea2soa.R;

import java.util.ArrayList;

public class WeatherFragment extends Fragment {

    private WeatherViewModel weatherViewModel;
    private Spinner select_city;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        weatherViewModel =
                ViewModelProviders.of(this).get(WeatherViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        weatherViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });


        inicializarSelectCiudades();

        //Dropdown ciudades

        select_city = root.findViewById(R.id.select_city);
        ArrayList<String> numberList = new ArrayList<>();
        numberList.add("Select Number");
        numberList.add("One");
        numberList.add("Two");
        numberList.add("Three");
        numberList.add("Four");
        numberList.add("Five");
        numberList.add("Six");



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, numberList);
        select_city.setAdapter(adapter);

        select_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Toast.makeText(getContext(),"Seleccione un numero", Toast.LENGTH_LONG).show();;
                }else{
                    String sNumber =parent.getItemAtPosition(position).toString();
                    System.out.println(sNumber);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return root;
    }

    public void inicializarSelectCiudades(){
        
    }
}