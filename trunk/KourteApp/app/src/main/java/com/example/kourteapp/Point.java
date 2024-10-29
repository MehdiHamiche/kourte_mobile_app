package com.example.kourteapp;


/**
 * Cette classe permet de définir chaque points du tracé
 *
 * @author Youstina Abdel-Massih
 * @version 1.0
 */
public class Point {
    /**
     * Les attributs de la classe
     */
    private int x, y;
    private double latitude, longitude;
    private float epaisseur;



    /**
     * Constructeur de la classe
     *
     * @param x
     * @param y
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.latitude = 0;
        this.longitude = 0;
    }

    /**
     * Constructeur surchargée
     * @param x
     * @param y
     * @param latitude
     * @param longitude
     */
    public Point(int x, int y, double latitude, double longitude) {
        this.x = x;
        this.y = y;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Cette méthode permet de récupérer la valeur de l'abscisse d'un point
     *
     * @return x
     */
    public int getX() {
        return x;
    }


    /**
     * Cette méthode permet de récupérer la valeur de l'ordonnée d'un point
     *
     * @return x
     */
    public int getY() {
        return y;
    }


    /**
     * Cette méthode permet de récupérer la latitude du point
     *
     * @return latitude
     */
    public double getLatitude() {
        return latitude;
    }


    /**
     * Cette méthode permet de récupérer la longitude du point
     *
     * @return longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Cette méthode permet de récupérer la valeur de l'épaisseur d'un point
     *
     * @return x
     */
    public float getEpaisseur() {
        return epaisseur = 5;
    }


    /**
     * Cette méthode permet de décaler le canvas vers la droite
     *
     * @return x
     */
    public int getDroite() {
        x += 700;
        return x;
    }


    /**
     * Cette méthode permet de décaler le canvas vers la gauche
     *
     * @return x
     */
    public int getGauche() {
        x -= 700;
        return x;
    }


    /**
     * Cette méthode permet de décaler le canvas vers le haut
     *
     * @return y
     */
    public int getHaut() {
        y += 700;
        return y;
    }


    /**
     * Cette méthode permet de décaler le canvas vers le bas
     *
     * @return y
     */
    public int getBas() {
        y -= 700;
        return y;
    }



    public boolean equals(Object o){
        if (! (o instanceof Point)) return false;
        if (this==o) return true;

        Point p=(Point) o;
        return (getX()==p.getX()&&getY()==p.getY()&&getLatitude()==p.getLatitude()&&getLongitude()==p.getLongitude());
    }



    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}