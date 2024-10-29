package com.example.kourteapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Cette classe permet l'affichage des tracé dans l'historique
 *
 * @author Thierno Bah
 * @version 1.0
 */
public class HistoriqueActivity extends AppCompatActivity {

    //Les attributs de la classe
    HistoriqueViewAdapter historiqueViewAdapter;
    ArrayList<Trajet> trajets = new ArrayList<>();


    /**
     * Cette méthode permet d'effectuer les activités liées à l'historique
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);
        ListView list = (ListView) findViewById(R.id.listHistorique);
        MainActivity.kourteDbHelper.lireTrajets(trajets);
        historiqueViewAdapter = new HistoriqueViewAdapter(this, trajets);
        Log.d("bdd", "**************** Chargement de " + trajets.size()+" trajets de la BDD");
        list.setAdapter(historiqueViewAdapter);
    }


    /**
     * Cette méthode permet de créer le menu avec les favoris
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }


    /**
     * Cette méthode permet de rajouter un trajet dans les favoris
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId()==R.id.menu_tous){
            historiqueViewAdapter.getFilter().filter(null);
        }
        else if (item.getItemId()==R.id.menu_favoris){
            historiqueViewAdapter.getFilter().filter("favoris");
        }
        return true;
    }


    /**
     * Cette méthode permet de supprimer un tracé. Une alerte pour demander
     * à l'utilisateur s'il veut vraiment supprimer un tracé
     *
     * @param v
     */
    public void showDeleteDialogue(final View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Alerte!!");
        alert.setMessage("Voulez-vous vraiment supprimer ce trajet ?");
        alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                int position=(Integer)v.getTag();
                long id = historiqueViewAdapter.getItem(position)._id;
                historiqueViewAdapter.remove(position);
                MainActivity.kourteDbHelper.supprimerTrajet(id);
                dialog.dismiss();
            }});
        alert.setNegativeButton("Non", null);
        alert.show();
    }


    /**
     * Cette méthode permet d'afficher l'image du tracé
     *
     * @param v
     */
    public void afficherTrajetActivity(View v){
        int position=(Integer)v.getTag();
        Intent intent=new Intent(this,AfficherTrajetHistoriqueActivity.class);
        intent.putExtra("fileName",historiqueViewAdapter.getItem(position).fileName);
        startActivity(intent);
    }


    /**
     * Cette méthode permet d'afficher la liste des tracés en favoris
     *
     * @param v
     */
    public void processFavorite(View v){
        int position=(Integer)v.getTag();
        long id = historiqueViewAdapter.getItem(position)._id;
        historiqueViewAdapter.getItem(position).favorie=!historiqueViewAdapter.getItem(position).favorie;
        historiqueViewAdapter.notifyDataSetChanged();
        MainActivity.kourteDbHelper.marquerFavorie(id,historiqueViewAdapter.getItem(position).favorie);
    }

}
