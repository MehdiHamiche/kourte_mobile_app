package com.example.kourteapp;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Thierno Bah
 * @version 1.0
 */
public class Trajet {

    long _id;
    String mode_de_transport;
    String date_debut;
    String date_fin;
    public List<Point> tracet;
    String fileName;
    int nbPointsDb;
    boolean favorie;


    /**
     * Constructeur de la classe
     *
     * @param mode_de_transport
     * @param date_debut
     * @param date_fin
     */
    public Trajet(String mode_de_transport, String date_debut, String date_fin) {
        this._id = _id;
        this.mode_de_transport = mode_de_transport;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        nbPointsDb=0;
        fileName="";
        tracet=new ArrayList();
        favorie=false;
    }


    /**
     * Cette méthode ajoute un point au tracé
     *
     * @param x
     * @param y
     * @param latitude
     * @param longitude
     */
    public void ChargerPoint(int x, int y, double latitude, double longitude){
        tracet.add(new Point(x,y,latitude,longitude ));
    }


    /**
     * Cette méthode permet de récupéré la taille d'un tracé
     *
     * @return trace
     */
    public int longeurTracet(){
        return tracet.size();
    }


    @Override
    public String toString() {
        String str= "Trajet{" +
                "_id=" + _id +
                ", mode_de_transport='" + mode_de_transport + '\'' +
                ", date_debut=" + date_debut +
                ", date_fin=" + date_fin +
                '}';
        for(Point point: tracet){
            str=str+point.toString()+"\n";
        }
        return str;
    }
}
