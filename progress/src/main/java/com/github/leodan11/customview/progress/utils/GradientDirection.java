package com.github.leodan11.customview.progress.utils;

public enum GradientDirection {
    LEFT_TO_RIGHT(1), RIGHT_TO_LEFT(2),
    TOP_TO_BOTTOM(3), BOTTOM_TO_END(4);

    private final int value;
    GradientDirection(int v) { this.value = v; }
    public int getValue() { return value; }
    public static GradientDirection fromInt(int v) {
        if (v == 1) {
            return LEFT_TO_RIGHT;
        } else if (v == 2) {
            return RIGHT_TO_LEFT;
        } else if (v == 3) {
            return TOP_TO_BOTTOM;
        } else if (v == 4) {
            return BOTTOM_TO_END;
        }
        throw new IllegalArgumentException("Invalid GradientDirection " + v);
    }
}