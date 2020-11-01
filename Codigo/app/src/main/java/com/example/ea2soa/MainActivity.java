package com.example.ea2soa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.se.omapi.Session;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ea2soa.data.BatteryStatus;
import com.example.ea2soa.data.SessionManager;
import com.example.ea2soa.data.TokenRefresher;
import com.example.ea2soa.ui.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.w3c.dom.Text;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private SessionManager sessionManager;
    private TokenRefresher tokenRefresher;
    private TextView txtEmailNavBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.sessionManager = new SessionManager(getApplicationContext());

        //Inicio el thread que verifica la vigencia del token
        this.tokenRefresher = new TokenRefresher(getApplicationContext());
        this.tokenRefresher.start();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_weather)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //Muestro estado de bateria
        BatteryStatus batStatus = new BatteryStatus(getApplicationContext());
        Toast.makeText(getApplicationContext(), "Su porcentaje de batería es: " + batStatus.getBatteryPercentage() + "%", Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        //Pongo el email en el navBar
        TextView txtEmailNavBar = findViewById(R.id.txtEmailNavBar);
        txtEmailNavBar.setText(this.sessionManager.getEmail());

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_sensors:
                Intent intent = new Intent(getApplicationContext(), SensorActivity.class); // Voy al sensor activity
                startActivity(intent);
                return true;

            case R.id.action_logout:
                this.tokenRefresher.interrupt(); //Interrumpo el hilo que checkea el token
                this.sessionManager.endSession(); //Pone en blanco los tokens
                finish(); //Finalizo el main activity
                Intent intentLogout = new Intent(getApplicationContext(), LoginActivity.class); // Voy al login
                startActivity(intentLogout);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void redirectToLogin(){
        finish(); //finalizo esta activity
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(this.sessionManager.isTokenExpired()){
            redirectToLogin();
            return;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(this.sessionManager.isTokenExpired()){
            redirectToLogin();
            return;
        }
    }
}