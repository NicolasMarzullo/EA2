package com.example.ea2soa.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ea2soa.MainActivity;
import com.example.ea2soa.R;
import com.example.ea2soa.RegisterActivity;
import com.example.ea2soa.SensorActivity;
import com.example.ea2soa.data.InternetConnection;
import com.example.ea2soa.data.SoaErrorMessage;
import com.example.ea2soa.data.SoaResponse;
import com.example.ea2soa.data.model.User;
import com.example.ea2soa.services.SoaService;
import com.example.ea2soa.ui.login.LoginViewModel;
import com.example.ea2soa.ui.login.LoginViewModelFactory;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    private static final String TAG = "LoginActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText emailEditText = findViewById(R.id.email);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final Button registerButton = findViewById(R.id.registerButton);


        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    emailEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(emailEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingProgressBar.setVisibility(View.VISIBLE);
                //Dehabilito los botones botón
                loginButton.setEnabled(false);
                registerButton.setEnabled(false);


                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(getString(R.string.retrofit_server))
                        .build();

                SoaService soaService = retrofit.create(SoaService.class);


                User user = new User();
                user.setEmail(emailEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());

                Call<SoaResponse> call = soaService.login(user);

                call.enqueue(new Callback<SoaResponse>() {
                    @Override
                    public void onResponse(Call<SoaResponse> call, Response<SoaResponse> response) {
                        if (response.isSuccessful()) {
                            Log.i(TAG, response.body().toString());

                            //Voy al main activity
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                        } else {
                            //Parseo la respuesta para poder mostrarla en la app
                            Gson gson = new Gson();
                            SoaErrorMessage error = gson.fromJson(response.errorBody().charStream(), SoaErrorMessage.class);
                            Toast.makeText(getApplicationContext(), "Hubo un error: " + error.getMsg(), Toast.LENGTH_LONG).show();
                            Log.e(TAG, response.errorBody().toString());
                        }
                        //saco el loader
                        loadingProgressBar.setVisibility(View.INVISIBLE);
                        Log.i(TAG, "Mensaje finalizado");
                        //Habilito ols botones
                        loginButton.setEnabled(true);
                        registerButton.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<SoaResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Ups, el servidor no está operativo :(", Toast.LENGTH_LONG).show();
                        Log.e(TAG, t.getMessage().toString());
                        loadingProgressBar.setVisibility(View.INVISIBLE);
                        //Habilito el botón
                        loginButton.setEnabled(true);
                        registerButton.setEnabled(true);
                    }
                });



            }
        });

        //Verifico conexion a Internet
        InternetConnection iConnection = new InternetConnection(getApplicationContext());
        if(!iConnection.isInternetAvailable()){
            Toast.makeText(getApplicationContext(), "Ups, no tenés conexión a Internet :(", Toast.LENGTH_LONG).show();
        }


        // Listener para abrir el activity de register cuando tocan el botón
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

}