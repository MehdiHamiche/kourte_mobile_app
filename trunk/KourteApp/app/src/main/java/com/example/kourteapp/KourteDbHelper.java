package com.example.kourteapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

/**
 * Cette classe correspond à la base de données SQLite de l'application
 *
 * @author Thierno Bah
 * @version 1.0
 */
public class KourteDbHelper extends SQLiteOpenHelper {
    // les attributs de la classe
    static String DATABASE_NAME = "Kourte_Trajets.db";
    static int SCHEMA_VERSION = 1;

    /**
     * Constructeur de la classe
     *
     * @param context
     */
    public KourteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
        getWritableDatabase();
        Log.d("bdd", "calling constructor of open helper");
    }


    /**
     * Cette méthode permet de créer les tables
     *
     * @param db
     */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Trajet(_id integer PRIMARY KEY AUTOINCREMENT, mode_de_transport TEXT,date_debut TEXT , date_fin TEXT, nbPointsDb integer, fileName TEXT, favorie integer  );");
        db.execSQL("CREATE TABLE Trace(_id integer PRIMARY KEY AUTOINCREMENT, numero_point integer,x integer , y integer,latitude real, longitude real,trajet_id integer  );");
        Log.d("bdd", "oncreate");
    }


    /**
     * Cette méthode est appelée seulement si la version de la base de données
     * a changée
     *
     * @param db
     * @param v1
     * @param v2
     */
    public void onUpgrade(SQLiteDatabase db, int v1, int v2) {
    }


    /**
     * Permet d'ajouter un tracé dans l'historique
     *
     * @param trajet
     * @return
     */
    // ajout trajet
    public long ajouterTrajet(Trajet trajet){
        long inserted_trajet_id;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("mode_de_transport",trajet.mode_de_transport);
        cv.put("date_debut",trajet.date_debut);
        cv.put("date_fin",trajet.date_fin);
        cv.put("nbPointsDb",trajet.longeurTracet());
        cv.put("fileName",trajet.fileName);
        cv.put("favorie",trajet.favorie?1:0);
        inserted_trajet_id=db.insert("Trajet","mode_de_transport",cv);

        Log.d("bdd","ecriture 1 trajet de "+trajet.longeurTracet()+" points");

        return inserted_trajet_id;
    }



    /**
     * Permet de lire les informations d'un tracé
     *
     * @param trajets
     */
    public void lireTrajets(List<Trajet> trajets) {
        String query ="SELECT * from Trajet";
        Cursor cursor= getReadableDatabase().rawQuery(query,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            long id = cursor.getLong(0);
            String mode= cursor.getString(1);
            String dateDebut = cursor.getString(2);
            String dateFin = cursor.getString(3);
            Trajet trajet = new Trajet(mode,dateDebut, dateFin);
            trajet._id=id;
            trajet.nbPointsDb = cursor.getInt(4);
            trajet.fileName = cursor.getString(5);
            trajet.favorie = cursor.getInt(6)==1?true:false;
            trajets.add(trajet);
            cursor.moveToNext();
        }
    }


    /**
     * Permet de charger un trace avec tous ses informations
     *
     * @param trajet
     */
    public void chargerTracetTrajet(Trajet trajet){
        long trajet_id = trajet._id;
        String query ="SELECT * from Trace where trajet_id="+trajet_id;
        Cursor cursor= getReadableDatabase().rawQuery(query,null);
        cursor.moveToFirst();
        Log.d("bdd","lecture tracet");
        while(!cursor.isAfterLast()) {
            Log.d("bdd","lecture capture");
            long id = cursor.getLong(0);
            int x= cursor.getInt(2);
            int y= cursor.getInt(3);
            double latitude = cursor.getDouble(4);
            double longitude = cursor.getDouble(5);
            trajet.ChargerPoint(x,y,latitude,longitude);
            cursor.moveToNext();
        }
        trajet.nbPointsDb=trajet.longeurTracet();
    }


    /**
     * Permet de supprimer un trajet de l'historique
     *
     * @param id
     */
    public void supprimerTrajet(long id) {
        this.getWritableDatabase().delete("Trajet", "_id =" + id, null);
        this.getWritableDatabase().delete("Trace", "trajet_id =" + id, null);
        Log.d("bdd", "suppression du trajet de id " + id + " de la BDD");
    }


    /**
     * Permet de mettre un élément dans les favoris
     *
     * @param id
     * @param favorie
     */
    public void marquerFavorie(long id, boolean favorie ){
        ContentValues cv = new ContentValues();
        int updated_record_id;
        cv.put("favorie", favorie?1:0);
        updated_record_id=this.getWritableDatabase().update("Trajet",cv, "_id ="+id, null);
        Log.d("bdd","marquage de favorie a "+favorie+"  pour record id= "+id);
    }

}