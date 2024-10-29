package com.example.kourte4;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity {

    // Les attributs pour les capteurs
    private SensorManager mSensorManager;
    private Sensor mAccelerometer; // va permettre d'accéder aux capcités de l'accéléromètre

    private double accelerationPrecedante = 0;
    private double newAcceleration;

    private int ptsGraph = 5;

    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
            new DataPoint(0, 0),
            new DataPoint(0, 0),
            new DataPoint(0, 0),
            new DataPoint(0, 0),
            new DataPoint(0, 0)
    });

    private Viewport viewport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialise sensor objects
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // permet d'acceder à l'accéléromètre


        GraphView graph = (GraphView) findViewById(R.id.graph);

        viewport = graph.getViewport();
        viewport.setXAxisBoundsManual(true); //permet de dessiner le graphique en temps réel
        graph.addSeries(series);
    }


    private SensorEventListener sensorEventListener = new SensorEventListener() {
        // Méthode qui permet d'indiquer les changements de valeurs
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0]; // correspond à l'axe horizontal de l'écran
            float y = sensorEvent.values[1]; // correspond à l'axe vertical de l'écran
            float z = sensorEvent.values[2]; // correspond à un axe qui pointe vers le ciel

            //calculs de l'accélération tiré de la documentation
            double accelerationSquareRoot = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
            double acceleration = Math.sqrt(accelerationSquareRoot);


            newAcceleration = acceleration - accelerationPrecedante;
            accelerationPrecedante = acceleration;

            ptsGraph++;
            series.appendData(new DataPoint(ptsGraph, newAcceleration), true, ptsGraph); //ajout d'un nouveau point sur le graphe
            viewport.setMaxX(ptsGraph);
            viewport.setMinX(ptsGraph-150);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };

    // Ces méthodes permettent d'erengistrer les activités de l'application
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(sensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(sensorEventListener);
    }
}