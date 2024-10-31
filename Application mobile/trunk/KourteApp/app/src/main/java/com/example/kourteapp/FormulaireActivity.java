package com.example.kourteapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

/**
 * Classe qui permet d'entrer les informations liées à un tracé
 *
 * @author Youstina Abdel-Massih
 * @author Mehdi Hamiche
 *
 * @version 1.0
 */
public class FormulaireActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    //Attributs pour la date
    EditText mDateFormat;
    DatePickerDialog.OnDateSetListener OnDateSetListener;
    String moyenTransport="";
    //Attributs mode de transport
    Spinner mSpinner;
    ArrayAdapter<CharSequence> contenu;


    /**
     * Cette méthode permet de démarrer les activités liées au formulaire
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire);

        // Permet d'avoir un retour au menu principal
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Obtenir le jour, le mois, l'année
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Permet l'affichage de la date complète
        mDateFormat = findViewById(R.id.dateFormat);
        mDateFormat.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    FormulaireActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    OnDateSetListener, year, month, day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        });

        OnDateSetListener = (view, year1, month1, dayOfMonth) -> {
            month1 = month1 +1;
            String date = dayOfMonth+"/"+ month1 +"/"+ year1;
            mDateFormat.setText(date);
        };

        // Permet de faire apparaître le menu déroulant pour le mode de transport
        mSpinner = findViewById(R.id.spinner);
        contenu = ArrayAdapter.createFromResource(FormulaireActivity.this, R.array.add_mode_of_transport, android.R.layout.simple_spinner_item);
        contenu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(contenu);
        moyenTransport=contenu.getItem(0).toString();
        mSpinner.setOnItemSelectedListener(this);
    }



    /**
     * Cette méthode permet d'indiquer un mode de transport ajouté par défaut
     * avant que l'utilisateur en choisisse un.
     * Ici, on n'utilise pas cette méthode
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
       moyenTransport=contenu.getItem(i).toString();
    }


    /**
     * Cette méthode est appelée chaque fois qu'un élément est actuellement
     * sélectionné et supprime cet élément de la liste des éléments disponibles.
     * Ici, on n'utilise pas cette méthode
     *
     * @param adapterView
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    /**
     * Cette méthode permet de retourner à l'écran d'accueil
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


    /**
     * Cette méthode permet d'enregistrer les informations entrées dans le formulaire dans
     * l'historique de l'application
     *
     * @param v
     */
    public void enregistrerTrajet(View v) {
        Trajet trajet=new Trajet(moyenTransport,mDateFormat.getText().toString(),mDateFormat.getText().toString());
        for(Point p:TraceActivity.nouveauTrace.traceSensible) {
            trajet.ChargerPoint(p.getX(),p.getY(),p.getLatitude(),p.getLongitude());
        }
        trajet.fileName=getIntent().getStringExtra("fileName");
        MainActivity.kourteDbHelper.ajouterTrajet(trajet);

        Toast.makeText(this,"Trajet ("+trajet.longeurTracet()+" points ) enregistré dans la Base de données",Toast.LENGTH_LONG).show();
        this.finish();
    }

}
