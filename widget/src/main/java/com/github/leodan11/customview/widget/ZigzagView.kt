package com.github.leodan11.customview.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.createBitmap


class ZigzagView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var zigzagHeight = 0f
    private var zigzagElevation = 0f
    private var zigzagPaddingContent = 0f
    var zigzagBackgroundColor = Color.WHITE
        set(@ColorInt value) {
            field = value
            paintZigzag.color = value
            invalidate()
        }
    private var zigzagPadding = 0f
    private var zigzagPaddingLeft = 0f
    private var zigzagPaddingRight = 0f
    private var zigzagPaddingTop = 0f
    private var zigzagPaddingBottom = 0f
    private var zigzagSides = 0
    private var zigzagShadowAlpha = 0f
    private val pathZigzag = Path()
    private val paintZigzag by lazy {
        Paint().apply {
            color = Color.BLACK
            style = Paint.Style.FILL
        }
    }
    private val paintShadow by lazy {
        Paint().apply {
            isAntiAlias = true
            colorFilter = PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN)
        }
    }
    private var shadow: Bitmap? = null
    private var rectMain = Rect()
    private var rectZigzag = RectF()
    private var rectContent = RectF()

    init {
        context.withStyledAttributes(attrs, R.styleable.ZigzagView) {
            zigzagElevation = getDimension(R.styleable.ZigzagView_zigzagElevation, 0.0f)
            zigzagHeight = getDimension(R.styleable.ZigzagView_zigzagHeight, 0.0f)
            zigzagPaddingContent = getDimension(R.styleable.ZigzagView_zigzagPaddingContent, 0.0f)
            zigzagBackgroundColor =
                getColor(R.styleable.ZigzagView_zigzagBackgroundColor, zigzagBackgroundColor)
            zigzagPadding = getDimension(R.styleable.ZigzagView_zigzagPadding, zigzagElevation)
            zigzagPaddingLeft =
                getDimension(R.styleable.ZigzagView_zigzagPaddingLeft, zigzagPadding)
            zigzagPaddingRight =
                getDimension(R.styleable.ZigzagView_zigzagPaddingRight, zigzagPadding)
            zigzagPaddingTop = getDimension(R.styleable.ZigzagView_zigzagPaddingTop, zigzagPadding)
            zigzagPaddingBottom =
                getDimension(R.styleable.ZigzagView_zigzagPaddingBottom, zigzagPadding)
            zigzagSides = getInt(R.styleable.ZigzagView_zigzagSides, ZIGZAG_BOTTOM)
            zigzagShadowAlpha = getFloat(R.styleable.ZigzagView_zigzagShadowAlpha, 0.5f)
        }

        zigzagElevation = zigzagElevation.coerceIn(0f, 25f)
        zigzagShadowAlpha = zigzagShadowAlpha.coerceIn(0f, 1f)
        paintShadow.alpha = (zigzagShadowAlpha * 100).toInt()

        setLayerType(LAYER_TYPE_SOFTWARE, null)
        setWillNotDraw(false)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        rectMain.set(0, 0, measuredWidth, measuredHeight)
        rectZigzag.set(
            rectMain.left + zigzagPaddingLeft,
            rectMain.top + zigzagPaddingTop,
            rectMain.right - zigzagPaddingRight,
            rectMain.bottom - zigzagPaddingBottom
        )
        rectContent.set(
            rectZigzag.left + zigzagPaddingContent + (if (containsSide(
                    zigzagSides,
                    ZIGZAG_LEFT
                )
            ) zigzagHeight else 0f),
            rectZigzag.top + zigzagPaddingContent + (if (containsSide(
                    zigzagSides,
                    ZIGZAG_TOP
                )
            ) zigzagHeight else 0f),
            rectZigzag.right - zigzagPaddingContent - if (containsSide(
                    zigzagSides,
                    ZIGZAG_RIGHT
                )
            ) zigzagHeight else 0f,
            rectZigzag.bottom - zigzagPaddingContent - if (containsSide(
                    zigzagSides,
                    ZIGZAG_BOTTOM
                )
            ) zigzagHeight else 0f
        )
        super.setPadding(
            rectContent.left.toInt(),
            rectContent.top.toInt(),
            (rectMain.right - rectContent.right).toInt(),
            (rectMain.bottom - rectContent.bottom).toInt()
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawZigzag()
        if (zigzagElevation > 0 && !isInEditMode) {
            drawShadow()
            canvas.drawBitmap(shadow!!, 0f, 0f, null)
        }
        canvas.drawPath(pathZigzag, paintZigzag)
    }

    private fun drawZigzag() {
        val left = rectZigzag.left
        val right = rectZigzag.right
        val top = rectZigzag.top
        val bottom = rectZigzag.bottom
        pathZigzag.moveTo(right, bottom)
        if (containsSide(zigzagSides, ZIGZAG_RIGHT) && zigzagHeight > 0)
            drawVerticalSide(pathZigzag, top, right, bottom, isLeft = false)
        else
            pathZigzag.lineTo(right, top)
        if (containsSide(zigzagSides, ZIGZAG_TOP) && zigzagHeight > 0)
            drawHorizontalSide(pathZigzag, left, top, right, isTop = true)
        else
            pathZigzag.lineTo(left, top)
        if (containsSide(zigzagSides, ZIGZAG_LEFT) && zigzagHeight > 0)
            drawVerticalSide(pathZigzag, top, left, bottom, isLeft = true)
        else
            pathZigzag.lineTo(left, bottom)
        if (containsSide(zigzagSides, ZIGZAG_BOTTOM) && zigzagHeight > 0)
            drawHorizontalSide(pathZigzag, left, bottom, right, isTop = false)
        else
            pathZigzag.lineTo(right, bottom)
    }

    private fun drawShadow() {
        shadow = createBitmap(width, height)
        shadow!!.eraseColor(Color.TRANSPARENT)

        val canvas = Canvas(shadow!!)
        canvas.drawPath(pathZigzag, paintShadow)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val blurEffect = RenderEffect.createBlurEffect(
                zigzagElevation, zigzagElevation,
                Shader.TileMode.CLAMP
            )

            val imageView = ImageView(context)
            imageView.setImageBitmap(shadow)
            imageView.setRenderEffect(blurEffect)
            imageView.layout(0, 0, width, height)

            val blurredBitmap = createBitmap(width, height)
            val outputCanvas = Canvas(blurredBitmap)
            imageView.draw(outputCanvas)

            shadow = blurredBitmap
        }
    }

    private fun drawHorizontalSide(
        path: Path,
        left: Float,
        y: Float,
        right: Float,
        isTop: Boolean
    ) {
        val h = zigzagHeight
        val seed = 2 * h
        val width = right - left
        val count: Int = (width / seed).toInt()
        val diff = width - seed * count
        val sideDiff = diff / 2
        val halfSeed = seed / 2
        val innerHeight = if (isTop) y + h else y - h
        if (isTop) {
            for (i in count downTo 1) {
                val startSeed = i * seed + sideDiff + left.toInt()
                var endSeed = startSeed - seed
                if (i == 1) {
                    endSeed -= sideDiff
                }
                path.lineTo(startSeed - halfSeed, innerHeight)
                path.lineTo(endSeed, y)
            }
        } else {
            for (i in 0 until count) {
                var startSeed = i * seed + sideDiff + left.toInt()
                var endSeed = startSeed + seed
                if (i == 0) {
                    startSeed = left.toInt() + sideDiff
                } else if (i == count - 1) {
                    endSeed += sideDiff
                }
                path.lineTo(startSeed + halfSeed, innerHeight)
                path.lineTo(endSeed, y)
            }
        }
    }

    private fun drawVerticalSide(path: Path, top: Float, x: Float, bottom: Float, isLeft: Boolean) {
        val h = zigzagHeight
        val seed = 2 * h
        val width = bottom - top
        val count: Int = (width / seed).toInt()
        val diff = width - seed * count
        val sideDiff = diff / 2
        val halfSeed = seed / 2
        val innerHeight = if (isLeft) x + h else x - h
        if (!isLeft) {
            for (i in count downTo 1) {
                val startSeed = i * seed + sideDiff + top.toInt()
                var endSeed = startSeed - seed
                if (i == 1) {
                    endSeed -= sideDiff
                }
                path.lineTo(innerHeight, startSeed - halfSeed)
                path.lineTo(x, endSeed)
            }
        } else {
            for (i in 0 until count) {
                var startSeed = i * seed + sideDiff + top.toInt()
                var endSeed = startSeed + seed
                if (i == 0) {
                    startSeed = top.toInt() + sideDiff
                } else if (i == count - 1) {
                    endSeed += sideDiff
                }
                path.lineTo(innerHeight, startSeed + halfSeed)
                path.lineTo(x, endSeed)
            }
        }
    }

    private fun containsSide(flagSet: Int, flag: Int): Boolean {
        return flagSet or flag == flagSet
    }

    companion object {
        private const val ZIGZAG_TOP = 1
        private const val ZIGZAG_BOTTOM = 2 // default to be backward compatible.Like google ;)
        private const val ZIGZAG_RIGHT = 4
        private const val ZIGZAG_LEFT = 8
    }

}
