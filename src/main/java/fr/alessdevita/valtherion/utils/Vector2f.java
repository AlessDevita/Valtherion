package fr.alessdevita.valtherion.utils;

public class Vector2f {
    public float x, y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void add(Vector2f other) {
        this.x += other.x;
        this.y += other.y;
    }
}