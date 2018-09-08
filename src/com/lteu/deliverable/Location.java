package com.lteu.deliverable;



public class Location {
    private int x;
    private int y;

    public Location(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double Distance(Location l){
        return Math.sqrt(Math.pow(this.x - l.getX(), 2) + Math.pow(this.y - l.getY(), 2));
    }
}
