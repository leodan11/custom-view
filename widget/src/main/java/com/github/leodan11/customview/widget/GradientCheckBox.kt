package com.github.leodan11.customview.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatCheckBox
import com.github.leodan11.customview.core.helper.GradientTextHelper
import com.github.leodan11.customview.core.utils.WrongColorException

class GradientCheckBox : AppCompatCheckBox {

    private var textHelper: GradientTextHelper = GradientTextHelper(this)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun addGradientToFullText(
        @ColorInt startColor: Int,
        @ColorInt endColor: Int
    ) {
        addGradientSection(0, length(), startColor, endColor)
    }

    fun addGradientToFullText(
        startColorHex: String,
        endColorHex: String
    ) {
        try {
            val startColor = Color.parseColor(startColorHex)
            val endColor = Color.parseColor(endColorHex)
            addGradientToFullText(startColor, endColor)
        } catch (e: Exception) {
            throw WrongColorException()
        }
    }

    fun addGradientSection(
        startIndex: Int,
        endIndex: Int,
        @ColorInt startColor: Int,
        @ColorInt endColor: Int
    ) {
        textHelper.addGradientSection(startIndex, endIndex, startColor, endColor)
    }

    fun addGradientSection(
        startIndex: Int,
        endIndex: Int,
        startColorHex: String,
        endColorHex: String
    ) {
        try {
            val startColor = Color.parseColor(startColorHex)
            val endColor = Color.parseColor(endColorHex)
            addGradientSection(startIndex, endIndex, startColor, endColor)
        } catch (e: Exception) {
            throw WrongColorException()
        }
    }

    fun addGradientSection(
        gradientText: String,
        @ColorInt startColor: Int,
        @ColorInt endColor: Int
    ) {
        val textString = text.toString()
        val startIndex = textString.indexOf(gradientText)
        if (startIndex != -1) {
            addGradientSection(startIndex, startIndex + gradientText.length, startColor, endColor)
        }
    }

    fun addGradientSection(
        gradientText: String,
        startColorHex: String,
        endColorHex: String
    ) {
        try {
            val startColor = Color.parseColor(startColorHex)
            val endColor = Color.parseColor(endColorHex)
            addGradientSection(gradientText, startColor, endColor)
        } catch (e: Exception) {
            throw WrongColorException()
        }
    }

    fun clearGradient() {
        textHelper.clear()
        text = text.toString()
    }
}