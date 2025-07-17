package com.github.leodan11.customview.core.helper;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import androidx.annotation.NonNull;

public class Converters {

    public static float dipToPixels(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return dipValue * scale + 0.5f;
    }

    public static int dpToPx(@NonNull Context context, float dp) {
        return dpToPx(context.getResources(), dp);
    }

    public static int dpToPx(@NonNull Resources resources, float dp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    public static int pxToDp(float px) {
        return Math.round(px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static float spToPixels(float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return spValue * fontScale + 0.5f;
    }

    public static float spToPixels(@NonNull Context context, float sp) {
        return spToPixels(context.getResources(), sp);
    }

    public static float spToPixels(@NonNull Resources resources, float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.getDisplayMetrics());
    }

    public static float convertPixelsToDp(@NonNull Context context, float px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertDpToPixels(@NonNull Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

}
