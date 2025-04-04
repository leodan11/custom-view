package com.github.leodan11.customview.widget.pin.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface PinListener {

    default void onTextChangedListener(@Nullable String text) {

    }

    void onPinCompletedListener(@NonNull String text);

}
