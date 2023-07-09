package com.example.courseworkthird.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Size {

    S (  36),
    M (  39),
    L (  42);
    private final int maxSize;

    Size(int maxSize) {
        this.maxSize = maxSize;
    }

    @JsonCreator
    public static Size convertSize(int value) {
        for (Size size : Size.values()) {
            if (value == size.maxSize) {
                return size;
            }
        }
        throw new RuntimeException(("Нет такого размера"));
    }
}
