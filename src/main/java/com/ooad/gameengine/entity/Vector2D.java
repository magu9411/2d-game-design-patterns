package com.ooad.gameengine.entity;

/**
 * Lightweight immutable-ish vector to avoid external dependencies.
 */
public class Vector2D {
    private double x;
    private double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void add(Vector2D other) {
        this.x += other.x;
        this.y += other.y;
    }

    public void add(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public void scale(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    public Vector2D copy() {
        return new Vector2D(x, y);
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void clamp(double minX, double maxX, double minY, double maxY) {
        this.x = Math.max(minX, Math.min(maxX, this.x));
        this.y = Math.max(minY, Math.min(maxY, this.y));
    }

    @Override
    public String toString() {
        return "Vector2D{" + "x=" + x + ", y=" + y + '}';
    }
}
