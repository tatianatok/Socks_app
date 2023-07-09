package com.example.courseworkthird.model;

import java.time.LocalDateTime;

public class Operation {
    private final Type type;
    private final LocalDateTime dateTime;
    private final int quantity;
    private Sock sock;

    public Operation(Type type, LocalDateTime dateTime, int quantity, Sock sock) {
        this.type = type;
        this.dateTime = dateTime;
        this.quantity = quantity;
        this.sock = sock;
    }

    public Type getType() {
        return type;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getQuantity() {
        return quantity;
    }

    public Sock getSock() {
        return sock;
    }
}
