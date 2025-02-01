package com.github.leodan11.customview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import kotlin.math.roundToInt

class GradientView : View {

    // Properties
    private val paint: Paint = Paint()
    private val rect = Rect()

    //region Attributes
    var start: Int = Color.WHITE
        set(value) {
            field = value
            update()
        }
    var alphaStart: Float = 1f
        set(value) {
            field = value.coerceIn(0f, 1f)
            update()
        }
    var end: Int = Color.WHITE
        set(value) {
            field = value
            update()
        }
    var alphaEnd: Float = 1f
        set(value) {
            field = value.coerceIn(0f, 1f)
            update()
        }
    var direction: GradientDirection = GradientDirection.LEFT_TO_RIGHT
        set(value) {
            field = value
            update()
        }
    //endregion

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

        // Load the styled attributes and set their properties
        val attributes =
            context.obtainStyledAttributes(attrs, R.styleable.GradientView, defStyleAttr, 0)

        try {
            start = attributes.getColor(R.styleable.GradientView_gv_start, start)
            alphaStart = attributes.getFloat(R.styleable.GradientView_gv_alpha_start, alphaStart)
            end = attributes.getColor(R.styleable.GradientView_gv_end, end)
            alphaEnd = attributes.getFloat(R.styleable.GradientView_gv_alpha_end, alphaEnd)
            direction =
                attributes.getInteger(R.styleable.GradientView_gv_direction, direction.value)
                    .toGradientDirection()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            attributes.recycle()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        update()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(rect, paint)
    }

    private fun update() {
        // Update Size
        val usableWidth = width - (paddingLeft + paddingRight)
        val usableHeight = height - (paddingTop + paddingBottom)
        rect.right = usableWidth
        rect.bottom = usableHeight
        // Update Color
        paint.shader = linearGradient()
        // ReDraw
        invalidate()
    }

    private fun linearGradient(): LinearGradient {
        var x0 = 0f
        var y0 = 0f
        var x1 = 0f
        var y1 = 0f
        when (direction) {
            GradientDirection.LEFT_TO_RIGHT -> x1 = width.toFloat()
            GradientDirection.RIGHT_TO_LEFT -> x0 = width.toFloat()
            GradientDirection.TOP_TO_BOTTOM -> y1 = height.toFloat()
            GradientDirection.BOTTOM_TO_TOP -> y0 = height.toFloat()
        }
        return LinearGradient(
            x0, y0, x1, y1,
            start.adjustAlpha(alphaStart), end.adjustAlpha(alphaEnd),
            Shader.TileMode.CLAMP
        )
    }

    private fun Int.adjustAlpha(factor: Float): Int {
        val alpha = (Color.alpha(this) * factor).roundToInt()
        val red = Color.red(this)
        val green = Color.green(this)
        val blue = Color.blue(this)
        return Color.argb(alpha, red, green, blue)
    }

    private fun Int.toGradientDirection(): GradientDirection =
        when (this) {
            1 -> GradientDirection.LEFT_TO_RIGHT
            2 -> GradientDirection.RIGHT_TO_LEFT
            3 -> GradientDirection.TOP_TO_BOTTOM
            4 -> GradientDirection.BOTTOM_TO_TOP
            else -> throw IllegalArgumentException("This value is not supported for GradientDirection: $this")
        }

    /**
     * GradientDirection enum class to set the direction of the gradient color
     */
    enum class GradientDirection(val value: Int) {
        LEFT_TO_RIGHT(1),
        RIGHT_TO_LEFT(2),
        TOP_TO_BOTTOM(3),
        BOTTOM_TO_TOP(4)
    }

}