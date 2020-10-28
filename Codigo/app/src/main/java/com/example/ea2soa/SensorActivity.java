package com.example.ea2soa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.DecimalFormat;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private TextView txtAcelerometro;
    private TextView txtGiroscopo;
    private TextView txtOrientacion;
    private TextView txtMagnetico;
    private TextView txtProximidad;
    private TextView txtLuminosidad;
    private TextView txtTemperatura;
    private TextView txtGravedad;
    private TextView txtDetecta;
    private TextView txtGiro;
    private TextView txtPresion;

    DecimalFormat dosdecimales = new DecimalFormat("###.###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        txtAcelerometro = findViewById(R.id.txtAcelerometro);
        txtGiroscopo = findViewById(R.id.txtGiroscopo);
        txtOrientacion = findViewById(R.id.txtOrientacion);
        txtMagnetico = findViewById(R.id.txtMagnetico);
        txtProximidad = findViewById(R.id.txtProximidad);
        txtLuminosidad = findViewById(R.id.txtLuminosidad);
        txtTemperatura = findViewById(R.id.txtTemperatura);
        txtGravedad = findViewById(R.id.txtGravedad);
        txtDetecta = findViewById(R.id.txtDetecta);
        txtGiro = findViewById(R.id.txtGiro);
        txtPresion = findViewById(R.id.txtPresion);

        // Uso el servicio de sensores mediante SensorManager
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);


    }


    /**
     * Queda a la escucha de los sensores del dispositivo
     */
    protected void inicializar_sensores()
    {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),   SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),       SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),     SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),  SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),       SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),           SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE),     SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),         SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),        SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Detiene la escucha de los sensores (Ahorra batería)
     */
    private void detener_sensores()
    {

        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION));
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE));
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY));
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR));
        mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE));
    }

    /**
     * Handler que se ejecuta cuando un sensor cambia
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        String txt = "";

        // Cada sensor puede lanzar un thread y al cambiar su valor pasar por este método
        // Para asegurarnos ante los accesos simultáneos lo sincronizo con un Mutex

        synchronized (this)
        {
            Log.d("sensor", event.sensor.getName());

            switch(event.sensor.getType())
            {
                case Sensor.TYPE_ORIENTATION :
                    txt += "Orientacion:\n";
                    txt += "Brújula: " + getDireccion(event.values[0]) + "\n";
                    txt += "y: " + event.values[1] + "\n";
                    txt += "z: " + event.values[2] + "\n";
                    txtOrientacion.setText(txt);
                    break;

                case Sensor.TYPE_ACCELEROMETER :
                    txt += "Acelerometro:\n";
                    txt += "x: " + dosdecimales.format(event.values[0]) + " m/seg2 \n";
                    txt += "y: " + dosdecimales.format(event.values[1]) + " m/seg2 \n";
                    txt += "z: " + dosdecimales.format(event.values[2]) + " m/seg2 \n";
                    txtAcelerometro.setText(txt);

                    if ((event.values[0] > 25) || (event.values[1] > 25) || (event.values[2] > 25))
                    {
                        txtDetecta.setBackgroundColor(Color.parseColor("#cf091c"));
                        txtDetecta.setText("Vibracion Detectada");
                    }
                    break;

                case Sensor.TYPE_GYROSCOPE:
                    txt += "Giroscopo:\n";
                    txt += "x: " + dosdecimales.format(event.values[0]) + " deg/s \n";
                    txt += "y: " + dosdecimales.format(event.values[1]) + " deg/s \n";
                    txt += "z: " + dosdecimales.format(event.values[2]) + " deg/s \n";
                    txtGiroscopo.setText(txt);
                    break;

                case Sensor.TYPE_ROTATION_VECTOR :
                    txt += "Vector de rotaion:\n";
                    txt += "x: " + event.values[0] + "\n";
                    txt += "y: " + event.values[1] + "\n";
                    txt += "z: " + event.values[2] + "\n";

                    // Creo objeto para saber como esta la pantalla
                    Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                    int rotation = display.getRotation();

                    // El objeto devuelve 3 estados 0, 1 y 3
                    if( rotation == 0 )
                    {
                        txt += "Pos: Vertical \n";

                    }
                    else if( rotation == 1 )
                    {
                        txt += "Pos: Horizontal Izq. \n";

                    }
                    else if (rotation == 3)
                    {
                        txt += "Pos: Horizontal Der \n";
                    }

                    txt += "display: " + rotation + "\n";

                    txtGiro.setText(txt);

                    break;

                case Sensor.TYPE_GRAVITY :
                    txt += "Gravedad:\n";
                    txt += "x: " + event.values[0] + "\n";
                    txt += "y: " + event.values[1] + "\n";
                    txt += "z: " + event.values[2] + "\n";

                    txtGravedad.setText(txt);
                    break;

                case Sensor.TYPE_MAGNETIC_FIELD :
                    txt += "Campo Magnetico:\n";
                    txt += event.values[0] + " uT" + "\n";

                    txtMagnetico.setText(txt);
                    break;

                case Sensor.TYPE_PROXIMITY :
                    txt += "Proximidad:\n";
                    txt += event.values[0] + "\n";

                    txtProximidad.setText(txt);

                    // Si detecta 0 lo represento
                    if( event.values[0] == 0 )
                    {
                        txtDetecta.setBackgroundColor(Color.parseColor("#cf091c"));
                        txtDetecta.setText("Proximidad Detectada");
                    }
                    break;

                case Sensor.TYPE_LIGHT :
                    txt += "Luminosidad\n";
                    txt += event.values[0] + " Lux \n";

                    txtLuminosidad.setText(txt);
                    break;

                case Sensor.TYPE_PRESSURE :
                    txt += "Presion\n";
                    txt += event.values[0] + " mBar \n";

                    txtPresion.setText(txt);
                    break;

                case Sensor.TYPE_TEMPERATURE :
                    txt += "Temperatura\n";
                    txt += event.values[0] + " C \n";

                    txtTemperatura.setText(txt);
                    break;
            }
        }
    }

    @Override
    protected void onStop()
    {

        detener_sensores();
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        detener_sensores();

        super.onDestroy();
    }

    @Override
    protected void onPause()
    {
        detener_sensores();

        super.onPause();
    }

    @Override
    protected void onRestart()
    {
        inicializar_sensores();

        super.onRestart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        inicializar_sensores();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    /**
     * Obtiene la direccion cardinal según los valores de la brujula
     * @param values
     * @return
     */
    private String getDireccion(float values)
    {
        String txtDirection = "";
        if (values < 22)
            txtDirection = "N";
        else if (values >= 22 && values < 67)
            txtDirection = "NE";
        else if (values >= 67 && values < 112)
            txtDirection = "E";
        else if (values >= 112 && values < 157)
            txtDirection = "SE";
        else if (values >= 157 && values < 202)
            txtDirection = "S";
        else if (values >= 202 && values < 247)
            txtDirection = "SO";
        else if (values >= 247 && values < 292)
            txtDirection = "O";
        else if (values >= 292 && values < 337)
            txtDirection = "NO";
        else if (values >= 337)
            txtDirection = "N";

        return txtDirection;
    }
}