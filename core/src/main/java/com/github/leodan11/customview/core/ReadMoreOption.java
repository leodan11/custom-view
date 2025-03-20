package com.github.leodan11.customview.core;

import android.content.Context;

import androidx.annotation.Nullable;

import com.github.leodan11.customview.core.base.ReadMoreOptionBase;

public class ReadMoreOption extends ReadMoreOptionBase {

    public ReadMoreOption(Context context, int textLength, int textLengthType, String moreLabel, @Nullable ReadOptionListener.More onClickMoreListener, String lessLabel, @Nullable ReadOptionListener.Less onClickLessListener, int moreLabelColor, int lessLabelColor, boolean labelUnderLine, boolean expandAnimation) {
        super(context, textLength, textLengthType, moreLabel, onClickMoreListener, lessLabel, onClickLessListener, moreLabelColor, lessLabelColor, labelUnderLine, expandAnimation);
    }

    public static class Builder extends ReadMoreOptionBase.Builder<ReadMoreOption> {
        public Builder(Context context) {
            super(context);
        }

        @Override
        public ReadMoreOption build() {
            return new ReadMoreOption(
                    context,
                    textLength,
                    textLengthType,
                    moreLabel,
                    onClickMoreListener,
                    lessLabel,
                    onClickLessListener,
                    moreLabelColor,
                    lessLabelColor,
                    labelUnderLine,
                    expandAnimation
            );
        }
    }

    public static final int TYPE_LINE = 1;
    public static final int TYPE_CHARACTER = 2;
}
