package com.example.ea2soa.ui.weather;

import android.os.Bundle;
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
import com.example.ea2soa.data.Utils;
import com.example.ea2soa.data.WeatherRequest;
import com.example.ea2soa.data.model.City;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WeatherFragment extends Fragment {

    private WeatherViewModel weatherViewModel;
    private Spinner select_city;
    private ArrayList<String> cities_name_list;
    private ArrayList<Integer> cities_id_list;
    private WeatherRequest weatherRequest;
    private TextView txtTempMin;
    private TextView txtTempMax;
    private TextView txtHumedad;
    private TextView txtSensacionTermica;
    private TextView txtTemperatura;
    private TextView txtDescripcion;
    private TextView titleTempMin;
    private TextView titleTempMax;
    private TextView titleHumedad;
    private TextView titleSensacionTermica;
    private TextView titleTemperatura;


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

        //Obtengo los txt
        this.txtTempMin =  root.findViewById(R.id.txtTempMin);
        this.txtTempMax =  root.findViewById(R.id.textTempMax);
        this.txtHumedad =  root.findViewById(R.id.txtHumedad);
        this.txtTemperatura =  root.findViewById(R.id.txtTemperatura);
        this.txtSensacionTermica =  root.findViewById(R.id.txtSensacionTermica);
        this.txtDescripcion = root.findViewById(R.id.txtDescripcion);

        //Obtengo los title
        this.titleTempMin = root.findViewById(R.id.titleTempMin);
        this.titleTempMax = root.findViewById(R.id.titleTempMax);
        this.titleHumedad = root.findViewById(R.id.titleHumedad);
        this.titleSensacionTermica = root.findViewById(R.id.titleSensacionTermica);
        this.titleTemperatura = root.findViewById(R.id.titleTemperatura);
        return root;
    }

    public void inicializarSelectCiudades(View root) {
        //Dropdown ciudades

        select_city = root.findViewById(R.id.select_city);
        //Array para llenar el select
        this.cities_name_list = new ArrayList<>();
        this.cities_name_list.add("Seleccione una ciudad");
        //Array para obtener el id
        this.cities_id_list = new ArrayList<>();

        this.cities_id_list.add(-1);

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


                if(position != 0){
                    int city_id = cities_id_list.get(position);
                    //Le mando todos los textView para que pueda modificarlos cuando responde la API ya que la request es asincrona
                    ArrayList<TextView> outputs = new ArrayList<>();

                    outputs.add(txtTempMin);
                    outputs.add(txtTempMax);
                    outputs.add(txtHumedad);
                    outputs.add(txtSensacionTermica);
                    outputs.add(txtTemperatura);
                    outputs.add(txtDescripcion);

                    outputs.add(titleTempMin);
                    outputs.add(titleTempMax);
                    outputs.add(titleHumedad);
                    outputs.add(titleSensacionTermica);
                    outputs.add(titleTemperatura);


                    weatherRequest.getDataWeather(city_id, outputs);
                }
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