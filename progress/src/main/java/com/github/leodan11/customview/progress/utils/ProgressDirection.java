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
        return switch (v) {
            case 1 -> TO_RIGHT;
            case 2 -> TO_LEFT;
            default -> throw new IllegalArgumentException("Invalid ProgressDirection " + v);
        };
    }
}
