package com.example.courseworkthird.model;

import java.util.Objects;

public class Sock {

    private final Color color;
    private final Size size;
    private final int cottonPerсentage;

    public Sock(Color color, Size size, int cottonPerсentage) {
        this.color = color;
        this.size = size;
        this.cottonPerсentage = cottonPerсentage;
    }

    public Color getColor() {
        return color;
    }

    public Size getSize() {
        return size;
    }

    public int getCottonPerсentage() {
        return cottonPerсentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sock sock = (Sock) o;
        return cottonPerсentage == sock.cottonPerсentage && color.equals(sock.color) && size.equals(sock.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, size, cottonPerсentage);
    }
}
