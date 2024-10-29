package com.example.kourteapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;


/**
 * Cette classe permet de dessiner le tracé à l'aide de la classe Point
 *
 * @author Youstina Abdel Massih
 * @version 1.0
 */
public class Trace extends View implements SensorEventListener {
    // Attributs
    private final Paint paint;

    private int x;
    private int y;

    final ArrayList<Point> traceSensible;


    /**
     * Constructeur de la classe
     *
     * @param context
     * @param attrSet
     */
    public Trace(Context context, AttributeSet attrSet) {
        super(context, attrSet);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        traceSensible = new ArrayList<>();
    }



    /**
     * Méthode permettant d'ajouter un point dans le tracé
     * @param point
     */
    private void ajouterPoint(Point point) {
        traceSensible.add(point);
    }


    /**
     * Méthode qui est appelé lorsqu'il y a un nouvel événement de capteur
     * @param sensorEvent
     */
    public void onSensorChanged(SensorEvent sensorEvent) {
        float xValue = sensorEvent.values[0]; // correspond à l'axe horizontal de l'écran
        float yValue = sensorEvent.values[1]; // correspond à l'axe vertical de l'écran
        float zValue = sensorEvent.values[2]; // correspond à un axe qui pointe vers le ciel

        // Calcul de l'accélération tiré de la documentation
        int accelerationSquareRoot = (int) ((xValue * xValue + yValue * yValue + zValue * zValue) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH));
        int acceleration = (int) Math.sqrt(accelerationSquareRoot);
        x -= (int) xValue;
        y += (int) yValue;

        Point pt = new Point(x, y);
        ajouterPoint(pt);
    }



    /**
     * Méthode qui est appelé lorsque la précision du capteur enregistré a changé
     * @param sensor
     * @param i
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }


    /**
     * Méthode qui permet de position le premier point du tracé vers le centre de
     * l'écran
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        x = w/2;
        y = h/2;
        Point p = new Point(x, y);
        ajouterPoint(p);

    }



    /**
     * Méthode qui permet d'effectuer le tracé et de décaler le canvas pour suivre
     * le dessin du tracé
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float epaisseur;

        canvas.drawColor(Color.WHITE);
        for (int i = 0; i < traceSensible.size(); i++) {
            epaisseur = traceSensible.get(i).getEpaisseur();
            canvas.drawCircle(traceSensible.get(i).getX(), traceSensible.get(i).getY(), epaisseur, paint);


        }if (x < 0) {
            x += 700;
            for (int k = 0; k < traceSensible.size(); k++) {
                traceSensible.get(k).getDroite();
            }

        } else if (x > getWidth()) {
            x -= 700;
            for (int k = 0; k < traceSensible.size(); k++) {
                traceSensible.get(k).getGauche();
            }

        } else if (y < 0) {
            y += 700;
            for (int k = 0; k < traceSensible.size(); k++) {
                traceSensible.get(k).getHaut();
            }
        } else if (y > getHeight()) {
            y -= 700;
            for (int k = 0; k < traceSensible.size(); k++) {
                traceSensible.get(k).getBas();
            }
        }
        this.invalidate();

    }
}