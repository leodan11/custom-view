package com.github.leodan11.customview.progress.utils;

public enum GradientDirection {
    LEFT_TO_RIGHT(1), RIGHT_TO_LEFT(2),
    TOP_TO_BOTTOM(3), BOTTOM_TO_END(4);

    private final int value;
    GradientDirection(int v) { this.value = v; }
    public int getValue() { return value; }
    public static GradientDirection fromInt(int v) {
        return switch (v) {
            case 1 -> LEFT_TO_RIGHT;
            case 2 -> RIGHT_TO_LEFT;
            case 3 -> TOP_TO_BOTTOM;
            case 4 -> BOTTOM_TO_END;
            default -> throw new IllegalArgumentException("Invalid GradientDirection " + v);
        };
    }
}