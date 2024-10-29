package com.example.kourteapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Cette classe permet le dessin du tracé sensible
 *
 * @author Youstina Abdel-Massih
 * @author Mehdi Hamiche
 *
 * @version 1.0
 */
public class TraceActivity extends AppCompatActivity implements LocationListener {
    //Les attributs liés au dessin du tracé
    static Trace nouveauTrace;
    SensorManager mSensorManager;
    Location location;

    //Les attributs qui permettent d'utiliser les boutons
    Button telechargerTrace;
    Button enregistrerTrace;


    /**
     * Cette méthode permet de démarrer les activités liées au téléchargement
     * du tracé et elle permet aussi de passer à la classe FormulaireActivity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace);

        // Permet d'avoir un retour au menu principal
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = new Intent(TraceActivity.this, FormulaireActivity.class);

        mSensorManager = null;
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        nouveauTrace = findViewById(R.id.espace_trace);

        telechargerTrace = findViewById(R.id.button_download);
        telechargerTrace.setOnClickListener(view -> {
            saveTrace();
        });

        enregistrerTrace = (Button)findViewById(R.id.button_save);
        enregistrerTrace.setOnClickListener(view -> {
            Toast.makeText(this, "Ouverture du formulaire", Toast.LENGTH_SHORT).show();
            String fileName= saveTrace();
            intent.putExtra("fileName",fileName);
            startActivity(intent);
            this.finish();
        });
    }



    /**
     * Cette méthode permet de convertir un élément view en bitmap
     * afin de pouvoir télécharger une image du tracé
     *
     * ressource : https://stackoverflow.com/questions/5536066/convert-view-to-bitmap-on-android
     * @return
     */
    public static Bitmap getBitmapFromView() {
        //Defini un bitmap qui la même taille que le view du tracé
        Bitmap returnedBitmap = Bitmap.createBitmap(nouveauTrace.getWidth(), nouveauTrace.getHeight(),Bitmap.Config.ARGB_8888);
        //Le bitmap est liée à un canvas
        Canvas canvas = new Canvas(returnedBitmap);
        //On recupère le dessin
        Drawable bgDrawable = nouveauTrace.getBackground();
        if (bgDrawable!=null)
            //on dessine sur le canvas
            bgDrawable.draw(canvas);
        else
            //si il n'y a pas d'arrière plan, on dessine sur un arrière plan blanc
            canvas.drawColor(Color.WHITE);
        // dessin du tracé sur le canvas
        nouveauTrace.draw(canvas);
        return returnedBitmap;
    }


    /**
     * Cette méthode permet d'enregistrer une image du tracé dans la
     * gallery de l'utilisateur
     *
     * @param bitmap
     */
    private String save(Bitmap bitmap) {
        nouveauTrace.setDrawingCacheEnabled(true);

        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String fileName = UUID.randomUUID().toString()+".jpg";
        File myfile = new File(file, fileName);


        try{
            if (ActivityCompat.checkSelfPermission(TraceActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
                    (ActivityCompat.checkSelfPermission(TraceActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Vous n'avez pas autorisé l'application à accéder à la galerie du téléphone", Toast.LENGTH_SHORT).show();
            }
            else{

                FileOutputStream fileOutputStream = new FileOutputStream(myfile);
                getBitmapFromView().compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                Toast.makeText(this, "Tracé téléchargé", Toast.LENGTH_SHORT).show();
                nouveauTrace.setDrawingCacheEnabled(false);
            }

        } catch (Exception e){
            Toast.makeText(this, "Erreur : " + e.toString(), Toast.LENGTH_LONG).show();
        }
        return myfile.toString();

    }


    /**
     * Cette méthode permet de finaliser le téléchargement de l'image du tracé
     *
     */
    private String saveTrace() {
        nouveauTrace.setDrawingCacheEnabled(true);
        nouveauTrace.buildDrawingCache();
        nouveauTrace.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = nouveauTrace.getDrawingCache();
        return save(bitmap);
    }


    /**
     * Cette méthode est appelée lorsque on met l'application en arrière
     * plan avant d'y revenir
     */
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(nouveauTrace, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                mSensorManager.SENSOR_DELAY_GAME);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
        }
    }


    /**
     * Le méthode est appelée quand la localisation du téléphone change
     * @param location
     */
    @Override
    public void onLocationChanged(@NonNull Location location) {
    }

    public void onStatusChanged(String provider, int status, Bundle extra){
    }



    /**
     * Cette méthode permet de revenir au menu principal
     * @param menuItem
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}