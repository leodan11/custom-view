package com.github.leodan11.customview.core

import android.content.Context
import com.github.leodan11.customview.core.base.ReadMoreOptionBase

class ReadMoreOption(
    context: Context,
    textLength: Int,
    textLengthType: Int,
    moreLabel: String?,
    lessLabel: String?,
    moreLabelColor: Int,
    lessLabelColor: Int,
    labelUnderLine: Boolean,
    expandAnimation: Boolean
) :
    ReadMoreOptionBase(
        context = context,
        textLength = textLength,
        textLengthType = textLengthType,
        moreLabel = moreLabel,
        lessLabel = lessLabel,
        moreLabelColor = moreLabelColor,
        lessLabelColor = lessLabelColor,
        labelUnderLine = labelUnderLine,
        expandAnimation = expandAnimation
    ) {


    /**
     * Creates an [ReadMoreOption] with the arguments supplied to this builder.
     *
     * @property context The parent context
     * @constructor Create empty Builder.
     */
    class Builder(context: Context) : ReadMoreOptionBase.Builder<ReadMoreOption>(context) {
        override fun build(): ReadMoreOption {
            return ReadMoreOption(
                context = context,
                textLength = textLength,
                textLengthType = textLengthType,
                moreLabel = moreLabel,
                lessLabel = lessLabel,
                moreLabelColor = moreLabelColor,
                lessLabelColor = lessLabelColor,
                labelUnderLine = labelUnderLine,
                expandAnimation = expandAnimation
            )
        }
    }

    companion object {
        const val TYPE_LINE: Int = 1
        const val TYPE_CHARACTER: Int = 2
    }

}