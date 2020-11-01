package com.example.ea2soa.ui.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.ea2soa.R;
import com.example.ea2soa.data.Utils;
import com.example.ea2soa.data.WeatherRequest;
import com.example.ea2soa.data.model.City;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WeatherFragment extends Fragment {

    private WeatherViewModel weatherViewModel;
    private Spinner select_city;
    private ArrayList<String> cities_name_list;
    private ArrayList<Integer> cities_id_list;
    private WeatherRequest weatherRequest;

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

        this.weatherRequest = new WeatherRequest(getContext());
        inicializarSelectCiudades(root);

        return root;
    }

    public void inicializarSelectCiudades(View root) {
        //Dropdown ciudades

        select_city = root.findViewById(R.id.select_city);
        //Array para llenar el select
        this.cities_name_list = new ArrayList<>();
        //Array para obtener el id
        this.cities_id_list = new ArrayList<>();

        List<City> cities = getCitiesFromJsonFile();
        for (int i = 0; i < cities.size(); i++) {
            cities_name_list.add(cities.get(i).getName());
            cities_id_list.add(cities.get(i).getId());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, cities_name_list);
        select_city.setAdapter(adapter);

        select_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String sNumber = parent.getItemAtPosition(position).toString();
                System.out.println(sNumber);
                int city_id = cities_id_list.get(position);
                System.out.println(city_id);
                weatherRequest.getDataWeather(city_id);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public List<City> getCitiesFromJsonFile() {
        Gson gson = new Gson();

        String jsonFileString = Utils.getJsonFromAssets(getContext(), "ciudades_argentinas.json");
        Type listCityType = new TypeToken<List<City>>() {
        }.getType();
        List<City> cities = gson.fromJson(jsonFileString, listCityType);
        return cities;
    }
}