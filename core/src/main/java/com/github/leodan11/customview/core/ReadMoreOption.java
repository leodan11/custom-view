package com.github.leodan11.customview.core;

import android.content.Context;

import com.github.leodan11.customview.core.base.ReadMoreOptionBase;

public class ReadMoreOption extends ReadMoreOptionBase {

    public ReadMoreOption(Context context, int textLength, int textLengthType, String moreLabel, String lessLabel, int moreLabelColor, int lessLabelColor, boolean labelUnderLine, boolean expandAnimation) {
        super(context, textLength, textLengthType, moreLabel, lessLabel, moreLabelColor, lessLabelColor, labelUnderLine, expandAnimation);
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
                    lessLabel,
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
