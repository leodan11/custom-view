package com.github.leodan11.customview.textview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import kotlin.math.min
import androidx.core.content.withStyledAttributes

class MaterialBadgeTextView : AppCompatTextView {

    private var badgeCount = -1

    /**
     * This is the maximum number to be displayed
     * Any number above this will be showed as **MAX_NUMBER+**
     */
    var maxNumber = 99

    /**
     * Should we draw circle or rounded rectangle shape?
     */
    private var isCircleShape = true

    /**
     * After how many digits should start drawing rectangle shape
     *
     * Setting a threshold to 2 will draw circle when the badge count is less or equal to 2
     */
    var circleShapeThreshold = 1

    /**
     * Top and bottom margin for rectangle or circle which is
     *  half the size between TextView's total width minus it's badge shape
     *
     * If shape is circle vertical margin is equal to horizontal margin
     *
     */
    var paddingVertical = 0
        get() {
            return if (field > borderWidth + badgeShadowYOffset) field
            else (field + borderWidth + badgeShadowYOffset).toInt()
        }

    /**
     * Horizontal margin for rectangle or circle which is
     *  half the size between TextView's total width minus it's badge shape
     *
     * If shape is circle vertical margin is equal to horizontal margin
     */
    var paddingHorizontal = 0
        get() {
            return if (field > borderWidth + badgeShadowXOffset) field
            else (field + borderWidth + badgeShadowXOffset).toInt()
        }

    /**
     * Radius for drawing a rounded rect background.
     *
     * It's 50% of the smaller dimension by default
     */
    var radiusRatio = .5f
        set(value) {
            if (value < 1) field = value
        }

    /**
     * Radius of rounded rect
     */
    private var badgeRoundedRectRadius = radiusRatio

    /**
     * Default background color
     */
    private var badgeBackgroundColor = Color.RED

    /**
     * Enable debugging rectangle behind this view
     */
    var isDebug = false

    /**
     * Border
     */
    var borderWidth = 0f
    var borderColor = Color.rgb(211, 47, 47)

    private val drawBorder: Boolean
        get() {
            return (borderWidth > 0)
        }

    /**
     * Shadow
     */
    var badgeShadowColor = 0x55000000
    var badgeShadowRadius = 0f

    var badgeShadowXOffset = 0f
        get() {
            return if (badgeShadowRadius == 0f) 0f else field + 0.5f
        }

    /**
     * Offset for shadow for x-axis. Setting positive number pushes shadow right.
     */
    var badgeShadowYOffset = 1.5f
        get() {
            return if (badgeShadowRadius == 0f) 0f else field + 0.5f
        }


    /**
     * Offset for shadow for x-axis. Setting positive number pushes shadow down.
     */
    private val drawShadow: Boolean
        get() {
            return (badgeShadowRadius > 0)
        }

    /**
     * Paint for drawing background color of the badge
     */
    private val paintBackground by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = badgeBackgroundColor
        }
    }

    /**
     * Paint for drawing border around badge
     */
    private val paintBorder by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            color = borderColor
        }
    }

    private val paintDebug by lazy {
        Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 5f
            color = Color.LTGRAY
        }
    }

    /**
     * Rectangle for measuring text dimensions
     */
    private val rect = Rect()

    /**
     * Pin mode is for drawing half radius smaller circle with no text
     */
    var pinMode = false
        set(value) {
            if (value) setBadgeCount(0)
            text = ""
            field = value
        }


    /**
     * Current width of the View.
     *
     * This can be used to have max width for same char size strings
     * not to change width when counter changes
     */
    private var currentWidth = 0

    private var textLength = 0

    private fun init() {

        // Center text
        gravity = Gravity.CENTER
        // Only draw one line
        maxLines = 1

        background = null

        if (drawShadow) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, paintBackground)
            paintBackground.setShadowLayer(
                badgeShadowRadius,
                badgeShadowXOffset,
                badgeShadowYOffset,
                badgeShadowColor
            )
        }

        textLength = text.length

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val length = text.length

        // Should draw circle or rectangle
        isCircleShape = (length <= circleShapeThreshold)

        // Get Text Width and Height; this is the smallest rectangle to be used
        val textHeight = textSize

        // Select one of these. paint.measureText adds some space to text when measuring
        val textWidth = (paint.measureText(text.toString())).toInt()

        paint.getTextBounds(text.toString(), 0, length, rect)

        // Space above and below text, this is drawing area + empty space
        val verticalSpaceAroundText = (textHeight * .12f + 6 + paddingVertical).toInt()

        // Space left and right of the text, this is a drawing area + empty space
        val horizontalSpaceAroundText = ((textHeight * .24f) + 8 + paddingHorizontal).toInt()

        // Measure dimensions
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)


        val desiredHeight = (textSize + 2 * verticalSpaceAroundText).toInt()
        val desiredWidth =
            if (isCircleShape) desiredHeight else (textWidth + 2 * horizontalSpaceAroundText)

        //Measure Width
        val width: Int = when (widthMode) {
            MeasureSpec.EXACTLY -> {
                //Must be this size
                widthSize
            }

            MeasureSpec.AT_MOST -> {
                desiredWidth
            }

            else -> {
                desiredWidth
            }
        }

        //Measure Height
        val height: Int = when (heightMode) {
            MeasureSpec.EXACTLY -> {
                heightSize
            }

            MeasureSpec.AT_MOST -> {
                desiredHeight
            }

            else -> {
                desiredHeight
            }
        }

        setMeasuredDimension(width, height)


        if (isDebug) {
            println(
                "🚀 onMeasure() " +
                        "minimumWidth: $minimumWidth, " +
                        "suggestedMinimumWidth: $suggestedMinimumWidth, " +
                        "minimumHeight: $minimumHeight, " +
                        "suggestedMinimumHeight: $suggestedMinimumHeight, " +
                        "verticalSpaceAroundText: $verticalSpaceAroundText, " +
                        "horizontalSpaceAroundText: $horizontalSpaceAroundText, " +
                        "textWidth: $textWidth, " +
                        "textHeight: $textHeight, " +
                        "rect width: ${rect.width()}, " +
                        "rect: height: ${rect.height()}" +
                        "padding: paddingStart: $paddingLeft, paddingTop: $paddingTop, " +
                        "desiredWidth: $desiredWidth, " +
                        "width: $width, " +
                        "desiredHeight: $desiredHeight, " +
                        "height: $height"
            )
        }
    }


    override fun onDraw(canvas: Canvas) {

        if (isDebug) {

            canvas.drawRect(
                (0).toFloat(),
                (0).toFloat(),
                (width).toFloat(),
                (height).toFloat(),
                paintDebug
            )
        }

        if (isCircleShape) {
            val radius = (height - paddingVertical * 2) / 2f
            canvas.drawCircle(width / 2f, height / 2f, radius, paintBackground)

            if (drawBorder) {
                canvas.drawCircle(width / 2f, height / 2f, radius, paintBorder)
            }

        } else {

            badgeRoundedRectRadius =
                min(width - paddingVertical * 2, height - paddingHorizontal * 2) * radiusRatio

            canvas.drawRoundRect(
                (paddingHorizontal).toFloat(),
                (paddingVertical).toFloat(),
                (width - paddingHorizontal).toFloat(),
                (height - paddingVertical).toFloat(),
                badgeRoundedRectRadius,
                badgeRoundedRectRadius,
                paintBackground
            )

            if (drawBorder) {
                canvas.drawRoundRect(
                    (paddingHorizontal).toFloat(),
                    (paddingVertical).toFloat(),
                    (width - paddingHorizontal).toFloat(),
                    (height - paddingVertical).toFloat(),
                    badgeRoundedRectRadius,
                    badgeRoundedRectRadius,
                    paintBorder
                )
            }
        }

        super.onDraw(canvas)
    }

    /**
     * Set badge background color in [MaterialBadgeTextView].
     *
     * @param color integer value
     */
    fun setBadgeBackgroundColor(@ColorInt color: Int) {
        badgeShadowColor = color
        paintBackground.color = color
        requestLayout()
    }


    /**
     * Set number to be displayed in [MaterialBadgeTextView]. If it cannot be
     * parsed to a number
     */
    fun setBadgeCount(count: String, showWhenZero: Boolean = false) {

        val badgeCount = count.toIntOrNull()

        badgeCount?.let {
            setBadgeCount(it, showWhenZero)
        }
    }

    /**
     * Set number to be displayed in [MaterialBadgeTextView]. If it cannot be
     * parsed to a number
     */
    @SuppressLint("SetTextI18n")
    fun setBadgeCount(count: Int, showWhenZero: Boolean = false) {

        this.badgeCount = count

        isCircleShape = (text.length <= circleShapeThreshold)

        when {
            count in 1..maxNumber -> {
                text = count.toString()
                visibility = VISIBLE
            }

            count > maxNumber -> {
                text = "$maxNumber+"
                visibility = VISIBLE
            }

            else -> {
                text = "0"
                visibility = if (showWhenZero) {
                    VISIBLE
                } else {
                    GONE
                }
            }
        }

    }


    /**
     * Convert dp to px for drawing xml attribute using pixel with canvas
     */
    fun dp2px(dpValue: Float): Int {
        return try {
            val scale = context.resources.displayMetrics.density
            (dpValue * scale + 0.5f).toInt()
        } catch (e: Exception) {
            (dpValue + 0.5f).toInt()
        }
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        context.withStyledAttributes(attrs, R.styleable.MaterialBadgeTextView) {

            circleShapeThreshold =
                getInteger(
                    R.styleable.MaterialBadgeTextView_badge_circle_threshold,
                    circleShapeThreshold
                )

            maxNumber =
                getInteger(R.styleable.MaterialBadgeTextView_badge_max_number, maxNumber)

            radiusRatio =
                getFloat(
                    R.styleable.MaterialBadgeTextView_badge_round_corner_ratio,
                    radiusRatio
                )


            badgeBackgroundColor =
                getColor(
                    R.styleable.MaterialBadgeTextView_android_background,
                    badgeBackgroundColor
                )

            paintBackground.color = badgeBackgroundColor

            // Padding
            paddingHorizontal =
                getDimensionPixelOffset(
                    R.styleable.MaterialBadgeTextView_badge_padding_horizontal,
                    dp2px(4f)
                )
            paddingVertical =
                getDimensionPixelOffset(
                    R.styleable.MaterialBadgeTextView_badge_padding_vertical,
                    dp2px(4f)
                )

            // Border
            borderWidth =
                getDimensionPixelOffset(
                    R.styleable.MaterialBadgeTextView_badge_border_width,
                    dp2px(borderWidth)
                )
                    .toFloat()
            borderColor =
                getInteger(R.styleable.MaterialBadgeTextView_badge_border_color, borderColor)

            paintBorder.strokeWidth = borderWidth
            paintBorder.color = borderColor

            // Shadow
            badgeShadowRadius = getDimensionPixelOffset(
                R.styleable.MaterialBadgeTextView_badge_shadow_radius, dp2px(badgeShadowRadius)
            ).toFloat()
            badgeShadowXOffset =
                getDimensionPixelOffset(
                    R.styleable.MaterialBadgeTextView_badge_shadow_offset_x,
                    dp2px(badgeShadowXOffset)
                ).toFloat()
            badgeShadowYOffset =
                getDimensionPixelOffset(
                    R.styleable.MaterialBadgeTextView_badge_shadow_offset_y,
                    dp2px(badgeShadowYOffset)
                ).toFloat()
            badgeShadowColor =
                getColor(
                    R.styleable.MaterialBadgeTextView_badge_shadow_color,
                    badgeShadowColor
                )


            // Debug
            isDebug = getBoolean(R.styleable.MaterialBadgeTextView_badge_is_debug, false)

        }

        init()
    }

    /**
     * Add horizontal and/or vertical margin between total space and drawing area
     */
    constructor(context: Context) : super(context) {
        paddingVertical = dp2px(4f)
        paddingHorizontal = dp2px(4f)
        init()
    }


    /**
     * Add Compose style Modifier and Scope to set attributes and chain them
     */
    interface Modifier {
        companion object : Modifier
    }

    interface BadgeScope {
        companion object : BadgeScope
    }

}