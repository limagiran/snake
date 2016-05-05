package com.limagiran.snake.control;

import java.awt.geom.Point2D;

public final class Vector2D implements Cloneable {
    private double x;
    private double y;

    public Vector2D() {
        x = y = 0;
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Point2D point) {
        this.x = point.getX();
        this.y = point.getY();
    }

    public Vector2D set(double newX, double newY) {
        x = newX;
        y = newY;
        return this;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    
    public Vector2D addMe(Vector2D other) {
        x += other.x;
        y += other.y;
        return this;
    }

    public Vector2D add(Vector2D other) {
        return clone().addMe(other);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Vector2D)) {
            return false;
        }
        Vector2D other = (Vector2D) obj;

        return x == other.x && y == other.y;
    }

    @Override
    public Vector2D clone() {
        try {
            return (Vector2D) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private int doubleHash(double number) {
        long value = Double.doubleToLongBits(number);
        return (int) (value ^ (value >>> 32));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = prime + doubleHash(x);
        return prime * result + doubleHash(y);
    }

    @Override
    public String toString() {
        return toString("%.4f");
    }

    public String toString(String numberFormat) {
        return String.format("x: " + numberFormat + " y: " + numberFormat,
                x, y);
    }

}
