package com.example.kourteapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Classe qui permet d'entrer les informations liées à un tracé
 *
 * @author Youstina Abdel-Massih
 * @author Mehdi Hamiche
 *
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    // Attributs pour les boutons
    Button buttonStart;
    Button buttonHistorique;

    // va permettre la géolocalisation
    private LocationManager mLocationManager;
    private final static int REQUEST_CODE_UPDATE_LOCATION = 1;

    static KourteDbHelper kourteDbHelper;


    // va permettre de connaître la position réelle actuelle du système
    private LocationListener mLocationListener;


    /**
     * Cette méthode permet de démarrer les activités liées au menu principal
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStart = findViewById(R.id.button_start);
        buttonStart.setOnClickListener(view -> showAlertDialog(view));

        buttonHistorique = findViewById(R.id.button_history);
        buttonHistorique.setOnClickListener(view -> showAlertDialogH(view));
        kourteDbHelper = new KourteDbHelper(this);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if ((ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) &&
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_UPDATE_LOCATION);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, (LocationListener) this);

        }

        if ((ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1024);
        }
    }



    /**
     * Cette méthode permet d'afficher un pop-up pour avoir accès au tracé
     *
     * @param v
     */
    public void showAlertDialog(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        Toast.makeText(this, "Le tracé commence...", Toast.LENGTH_SHORT).show();

        openTraceActivity();
    }


    /**
     * Cette méthode permet l'ouverture du bouton "Démmarrer un tracé"
     */
    public void openTraceActivity() {
        Intent intent = new Intent(this, TraceActivity.class);
        startActivity(intent);
    }


    /**
     * Cette méthode permet d'appeler la méthode openHistoriqueActivity
     *
     * @param vH
     */
    public void showAlertDialogH(View vH) {
        openHistoriqueActivity();
    }


    /**
     * Cette méthode permet l'ouverture de l'historique à l'aide du bouton "Historique"
     * dans le menu principal
     */
    public void openHistoriqueActivity() {
        Intent intentH = new Intent(this, HistoriqueActivity.class);
        startActivity(intentH);
    }

}