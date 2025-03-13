package com.github.leodan11.customview.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * An extension of LinearLayout that automatically switches to vertical
 * orientation when it can't fit its child views horizontally.
 */
@SuppressLint("RestrictedApi")
public class ButtonBarLayout extends androidx.appcompat.widget.ButtonBarLayout {

    public ButtonBarLayout(@NonNull Context context) {
        super(context, null);
    }

    public ButtonBarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

}
