package com.github.leodan11.customview.progress.utils;

import android.graphics.LinearGradient;
import android.graphics.Shader;

public class DirectionUtils {

    public static LinearGradient createLinearGradient(GradientDirection dir, int w, int h, int startColor, int endColor) {
        float x0=0, y0=0, x1=0, y1=0;
        switch (dir) {
            case LEFT_TO_RIGHT: x1 = w; break;
            case RIGHT_TO_LEFT: x0 = w; break;
            case TOP_TO_BOTTOM: y1 = h; break;
            case BOTTOM_TO_END: y0 = h; break;
        }
        return new LinearGradient(x0, y0, x1, y1, startColor, endColor, Shader.TileMode.CLAMP);
    }

}
