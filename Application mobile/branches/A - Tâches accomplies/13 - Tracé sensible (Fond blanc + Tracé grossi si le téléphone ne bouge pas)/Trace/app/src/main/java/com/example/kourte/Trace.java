package com.example.kourte;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class Trace extends View implements SensorEventListener {

    private Paint paint;

    private int x;
    private int y;

    private ArrayList<Point> traceSensible;

    private int newAcceleration;
    private int accelerationPrecedante;



    public Trace(Context context, AttributeSet attrSet) {
        super(context, attrSet);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        traceSensible = new ArrayList<Point>();
        setFocusable(true);
    }



    private void ajouterPoint(Point point) {
        traceSensible.add(point);
    }

    public void onSensorChanged(SensorEvent sensorEvent) {

        float xValue = sensorEvent.values[0]; // correspond à l'axe horizontal de l'écran
        float yValue = sensorEvent.values[1]; // correspond à l'axe vertical de l'écran
        float zValue = sensorEvent.values[2]; // correspond à un axe qui pointe vers le ciel

        //calculs de l'accélération tiré de la documentation
        int accelerationSquareRoot = (int) ((xValue * xValue + yValue * yValue + zValue * zValue) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH));
        int acceleration = (int) Math.sqrt(accelerationSquareRoot);
        x -= (int) xValue;
        y += (int) yValue;

        Point pt = new Point(x, y);
        ajouterPoint(pt);
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void setNextFocusDownId(int nextFocusDownId) {
        super.setNextFocusDownId(nextFocusDownId);
    }


    //public static final int FOCUS_BACKWARD(){}

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);
        for (int i = 0; i < traceSensible.size(); i++) {
            canvas.drawCircle(traceSensible.get(i).getX(), traceSensible.get(i).getY(),traceSensible.get(i).setEpaisseur(), paint);
            for (int j = 0; j < traceSensible.size(); j++){
                if (traceSensible.get(i).getX() == traceSensible.get(j).getX()){
                    if (traceSensible.get(i).getY() == traceSensible.get(j).getY()){
                        canvas.drawCircle(traceSensible.get(i).getX(), traceSensible.get(i).getY(),traceSensible.get(i).epaisseur(), paint);
                    }
                }
            }
            }this.invalidate();
        }



    public boolean decalerCanvas() {
        return (x < 0 || x > getWidth() || y < 0 || y > getHeight()) ;
    }

}
