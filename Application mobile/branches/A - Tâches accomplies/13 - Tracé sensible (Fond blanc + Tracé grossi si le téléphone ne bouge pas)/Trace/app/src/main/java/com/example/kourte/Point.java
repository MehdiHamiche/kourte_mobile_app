package com.example.kourte;


public class Point {
    private int x, y, gx, dx, hy, by;
    private float xf, yf;
    private float epaisseur;

    public static int FOCUS_BACKWARD = 1;


    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Point(float xf, float yf) {
        this.xf = xf;
        this.yf = yf;
    }

    public int getFocusBackward() {
        this.FOCUS_BACKWARD = FOCUS_BACKWARD-1;
        return FOCUS_BACKWARD;
    }

    public int getX()  {
        return x;
    }

    public int getY() {
        return y;
    }

    public float setEpaisseur(){
        return epaisseur = 5;
    }

    public float epaisseur(){
        return epaisseur = (float)(epaisseur + 0.1);
    }

}
