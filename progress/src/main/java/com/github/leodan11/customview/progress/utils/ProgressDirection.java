package com.github.leodan11.customview.progress.utils;

public enum ProgressDirection {
    TO_RIGHT(1), TO_LEFT(2);

    private final int value;
    ProgressDirection(int v) { this.value = v; }
    public int getValue() { return value; }
    public boolean isToRight() { return this == TO_RIGHT; }
    public ProgressDirection reverse() {
        return this == TO_RIGHT ? TO_LEFT : TO_RIGHT;
    }
    public static ProgressDirection fromInt(int v) {
        if (v == 1) {
            return TO_RIGHT;
        } else if (v == 2) {
            return TO_LEFT;
        }
        throw new IllegalArgumentException("Invalid ProgressDirection " + v);
    }
}
