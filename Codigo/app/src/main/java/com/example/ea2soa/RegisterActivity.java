package com.example.ea2soa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ea2soa.data.AuthorizedRequest;
import com.example.ea2soa.data.InternetConnection;
import com.example.ea2soa.data.SoaErrorMessage;
import com.example.ea2soa.data.SoaResponse;
import com.example.ea2soa.data.model.Event;
import com.example.ea2soa.data.model.User;
import com.example.ea2soa.services.SoaService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        final EditText editTextNombre = findViewById(R.id.editTextNombre);
        final EditText editTextApellido = findViewById(R.id.editTextApellido);
        final EditText editTextDNI = findViewById(R.id.editTextDNI);
        final EditText editTextCommission = findViewById(R.id.editTextCommission);
        final EditText editTextEmail = findViewById(R.id.editTextEmail);
        final EditText editTextPassword = findViewById(R.id.editTextPassword);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);


        final Button btnRegister = findViewById(R.id.registerButton);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User newUser = new User();
                newUser.setEnv(getString(R.string.server_enviroment));
                newUser.setName(editTextNombre.getText().toString());
                newUser.setLastname(editTextApellido.getText().toString());
                newUser.setDni(Long.parseLong(editTextDNI.getText().toString()));
                newUser.setCommission(Long.parseLong(editTextCommission.getText().toString()));
                newUser.setEmail(editTextEmail.getText().toString());
                newUser.setPassword(editTextPassword.getText().toString());

                if (newUser.validate()) {
                    //Pongo el loader
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    //Deshabilito el botón para que no toquen dos veces o mas
                    btnRegister.setEnabled(false);

                    Retrofit retrofit = new Retrofit.Builder()
                            .addConverterFactory(GsonConverterFactory.create())
                            .baseUrl(getString(R.string.retrofit_server))
                            .build();

                    SoaService soaService = retrofit.create(SoaService.class);

                    Call<SoaResponse> call = soaService.register(newUser);
                    call.enqueue(new Callback<SoaResponse>() {
                        @Override
                        public void onResponse(Call<SoaResponse> call, Response<SoaResponse> response) {
                            if (response.isSuccessful()) {

                                //Limpio los input
                                editTextNombre.setText("");
                                editTextApellido.setText("");
                                editTextDNI.setText("");
                                editTextCommission.setText("");
                                editTextEmail.setText("");
                                editTextPassword.setText("");

                                Toast.makeText(getApplicationContext(), "Usuario registrado exitosamente", Toast.LENGTH_LONG).show();
                                Log.i(TAG, response.body().toString());
                                //Cierro activity register
                                finish();
                                //Voy al principal activity
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);

                            } else {
                                //Parseo la respuesta para poder mostrarla en la app
                                Gson gson = new Gson();
                                SoaErrorMessage error = gson.fromJson(response.errorBody().charStream(), SoaErrorMessage.class);
                                Toast.makeText(getApplicationContext(), "Hubo un error: " + error.getMsg(), Toast.LENGTH_LONG).show();
                                Log.e(TAG, response.errorBody().toString());

                            }
                            Log.i(TAG, "Mensaje finalizado");
                            //Apago el loader
                            loadingProgressBar.setVisibility(View.INVISIBLE);
                            //Habilito el botón nuevamente
                            btnRegister.setEnabled(true);
                        }

                        @Override
                        public void onFailure(Call<SoaResponse> call, Throwable t) {
                            //Apago el loader
                            loadingProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Ups, el servidor no está operativo :(", Toast.LENGTH_LONG).show();
                            Log.e(TAG, t.getMessage().toString());

                            //Habilito el botón nuevamente
                            btnRegister.setEnabled(true);
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Por favor verifique sus datos", Toast.LENGTH_LONG).show();
                }

            }
        });

        //Verifico conexion a Internet
        InternetConnection iConnection = new InternetConnection(getApplicationContext());
        if (!iConnection.isInternetAvailable()) {
            Toast.makeText(getApplicationContext(), "Ups, no tenés conexión a Internet :(", Toast.LENGTH_LONG).show();
        }
    }


}