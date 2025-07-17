package com.github.leodan11.customview.layout

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.github.leodan11.customview.core.helper.GradientBackgroundHelper

class GradientLinearLayout : LinearLayout {

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.GradientLinearLayout,
            defStyleAttr,
            0
        )
        try {
            val animate =
                a.getBoolean(R.styleable.GradientLinearLayout_gradient_ui_animate, false)
            val orientation =
                a.getInt(R.styleable.GradientLinearLayout_gradient_ui_orientation, 6)

            val animEnter = a.getInt(
                R.styleable.GradientLinearLayout_gradient_ui_animationEnterDuration,
                10
            )
            val animExit = a.getInt(
                R.styleable.GradientLinearLayout_gradient_ui_animationExitDuration,
                5000
            )

            val startColor =
                a.getColor(R.styleable.GradientLinearLayout_gradient_ui_startColor, -1)
            val centerColor =
                a.getColor(R.styleable.GradientLinearLayout_gradient_ui_centerColor, -1)
            val endColor =
                a.getColor(R.styleable.GradientLinearLayout_gradient_ui_endColor, -1)

            val colorsId =
                a.getResourceId(R.styleable.GradientLinearLayout_gradient_ui_colors, -1)

            val cornerRadius =
                a.getDimension(R.styleable.GradientLinearLayout_gradient_ui_cornerRadius, 0f)

            val colors = if (startColor != -1 && endColor != -1) {
                if (centerColor == -1) {
                    intArrayOf(startColor, endColor)
                } else {
                    intArrayOf(startColor, centerColor, endColor)
                }
            } else if (colorsId != -1) {
                resources.getIntArray(colorsId).takeIf { it.isNotEmpty() }
            } else {
                null
            }
            if (colors != null) {
                val gradientHelper = GradientBackgroundHelper(
                    colors,
                    cornerRadius,
                    orientation,
                    animate,
                    animEnter,
                    animExit
                )
                gradientHelper.applyBackground { background = it }
            }
        } finally {
            a.recycle()
        }
    }

}