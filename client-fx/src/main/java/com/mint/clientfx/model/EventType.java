package com.mint.clientfx.model;

public enum EventType {

    SEMINAR("GREEN"),
    TIME_OFF("RED"),
    PROJECT("BLUE"),
    APPOINTMENT("ORANGE"),
    ;

    private final String color;

    EventType(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
