package com.lteu.deliverable;



public class LocationLTE {
    private double x;
    private double y;

    public LocationLTE(double x, double y) {
        super();
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double Distance(LocationLTE l){
        return Math.sqrt(Math.pow(this.x - l.getX(), 2) + Math.pow(this.y - l.getY(), 2));
    }
}
