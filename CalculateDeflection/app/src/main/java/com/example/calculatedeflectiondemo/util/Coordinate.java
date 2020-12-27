package com.example.calculatedeflectiondemo.util;

public class Coordinate {
    private final double x;
    private final double y;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Coordinate(double latitude, double longitude) {
        this.x = latitude;
        this.y = longitude;
    }
}