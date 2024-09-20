package com.github.leodan11.customview.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewStub
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.animation.Transformation
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.github.leodan11.customview.core.utils.Converters.convertDpToPixels

class ExpandableCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var cardLayout: CardView
    private lateinit var cardHeader: RelativeLayout
    private lateinit var cardContainer: LinearLayout
    private lateinit var cardTitle: TextView
    private lateinit var cardIcon: ImageButton
    private lateinit var cardArrow: ImageButton
    private lateinit var cardStub: ViewStub

    private var title: String? = null
    private var innerView: View? = null
    private var typedArray: TypedArray? = null
    private var innerViewRes: Int = 0
    private var iconDrawable: Drawable? = null
    private var iconDrawableTint: Int = 0
    private var iconDrawableArrow: Drawable? = null
    private var iconDrawableArrowTint: Int = 0

    var animDuration = DEFAULT_ANIM_DURATION.toLong()

    var isExpanded = false
        private set
    private var isExpanding = false
    private var isCollapsing = false
    private var expandOnClick = false
    private var startExpanded = false

    private var previousHeight = 0

    private var listener: OnExpandedListener? = null

    private val defaultClickListener = OnClickListener {
        if (isExpanded)
            collapse()
        else
            expand()
    }

    private val isMoving: Boolean
        get() = isExpanding || isCollapsing


    init {
        initView(context)
        attrs?.let {
            initAttributes(context, attrs)
        }
    }

    private fun initView(context: Context) {
        //Inflating View
        val viewRoot: View =
            LayoutInflater.from(context).inflate(R.layout.expandable_cardview, this)
        cardLayout = viewRoot.findViewById(R.id.card_layout)
        cardHeader = viewRoot.findViewById(R.id.card_header)
        cardContainer = viewRoot.findViewById(R.id.card_container)
        cardTitle = viewRoot.findViewById(R.id.card_title)
        cardIcon = viewRoot.findViewById(R.id.card_icon)
        cardArrow = viewRoot.findViewById(R.id.card_arrow)
        cardStub = viewRoot.findViewById(R.id.card_stub)
    }

    private fun initAttributes(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableCardView)
        this@ExpandableCardView.typedArray = typedArray
        title = typedArray.getString(R.styleable.ExpandableCardView_expandTitle)
        iconDrawable = typedArray.getDrawable(R.styleable.ExpandableCardView_expandIcon)
        iconDrawableTint = typedArray.getColor(R.styleable.ExpandableCardView_expandIconTint, 0)
        iconDrawableArrow = typedArray.getDrawable(R.styleable.ExpandableCardView_expandArrowIcon)
            ?: ContextCompat.getDrawable(context, R.drawable.baseline_keyboard_arrow_down)
        iconDrawableArrowTint =
            typedArray.getColor(R.styleable.ExpandableCardView_expandArrowIconTint, 0)
        innerViewRes =
            typedArray.getResourceId(R.styleable.ExpandableCardView_innerViewExpand, View.NO_ID)
        expandOnClick = typedArray.getBoolean(R.styleable.ExpandableCardView_expandOnClick, false)
        animDuration = typedArray.getInteger(
            R.styleable.ExpandableCardView_animationExpandDuration,
            DEFAULT_ANIM_DURATION
        ).toLong()
        startExpanded = typedArray.getBoolean(R.styleable.ExpandableCardView_startExpanded, false)
        typedArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        //Setting attributes
        if (!TextUtils.isEmpty(title)) cardTitle.text = title

        iconDrawable?.let { drawable ->
            if (iconDrawableTint != 0) setIconTint(iconDrawableTint)
            cardIcon.background = drawable
        }

        iconDrawableArrow?.let { drawable ->
            if (iconDrawableArrowTint != 0) setIconArrowTint(iconDrawableArrowTint)
            cardArrow.background = drawable
        }

        cardIcon.isVisible = iconDrawable != null

        setInnerView(innerViewRes)

        elevation = convertDpToPixels(context, 4f)

        if (startExpanded) {
            animDuration = 0
            expand()
        }

        if (expandOnClick) {
            cardLayout.setOnClickListener(defaultClickListener)
            cardArrow.setOnClickListener(defaultClickListener)
        }

    }

    /**
     * Expands the card
     */
    fun expand() {
        val initialHeight = cardLayout.height
        if (!isMoving) {
            previousHeight = initialHeight
        }

        cardLayout.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val targetHeight = cardLayout.measuredHeight

        if (targetHeight - initialHeight != 0) {
            animateViews(
                initialHeight,
                targetHeight - initialHeight,
                EXPANDING
            )
        }
    }

    /**
     * Collapses the card
     */
    fun collapse() {
        val initialHeight = cardLayout.measuredHeight
        if (initialHeight - previousHeight != 0) {
            animateViews(
                initialHeight,
                initialHeight - previousHeight,
                COLLAPSING
            )
        }
    }

    private fun animateViews(initialHeight: Int, distance: Int, animationType: Int) {

        val expandAnimation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    //Setting isExpanding/isCollapsing to false
                    isExpanding = false
                    isCollapsing = false

                    listener?.let { listener ->
                        if (animationType == EXPANDING) {
                            listener.onExpandChanged(cardLayout, true)
                        } else {
                            listener.onExpandChanged(cardLayout, false)
                        }
                    }
                }

                cardLayout.layoutParams.height = if (animationType == EXPANDING)
                    (initialHeight + distance * interpolatedTime).toInt()
                else
                    (initialHeight - distance * interpolatedTime).toInt()
                cardContainer.requestLayout()

                cardContainer.layoutParams.height = if (animationType == EXPANDING)
                    (initialHeight + distance * interpolatedTime).toInt()
                else
                    (initialHeight - distance * interpolatedTime).toInt()

            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        val arrowAnimation = if (animationType == EXPANDING)
            RotateAnimation(
                0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f
            )
        else
            RotateAnimation(
                180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f
            )

        arrowAnimation.fillAfter = true


        arrowAnimation.duration = animDuration
        expandAnimation.duration = animDuration

        isExpanding = animationType == EXPANDING
        isCollapsing = animationType == COLLAPSING

        startAnimation(expandAnimation)
        Log.d(
            "SO",
            "Started animation: " + if (animationType == EXPANDING) "Expanding" else "Collapsing"
        )
        cardArrow.startAnimation(arrowAnimation)
        isExpanded = animationType == EXPANDING

    }

    /**
     * Sets the listener for the expanded state
     * @param listener the listener
     */
    fun setOnExpandedListener(listener: OnExpandedListener) {
        this.listener = listener
    }

    /**
     * Sets the listener for the expanded state
     * @param method the method to call when the expanded state changes
     */
    fun setOnExpandedListener(method: (v: View?, isExpanded: Boolean) -> Unit) {
        this.listener = object : OnExpandedListener {
            override fun onExpandChanged(v: View?, isExpanded: Boolean) {
                method(v, isExpanded)
            }
        }
    }

    /**
     * Removes the listener for the expanded state
     */
    fun removeOnExpandedListener() {
        this.listener = null
    }

    /**
     * Sets the title of the card
     * @param title the title
     */
    fun setTitle(@StringRes title: Int) {
        setTitle(titleRes = title)
    }

    /**
     * Sets the title of the card
     * @param title the title
     */
    fun setTitle(title: String) {
        setTitle(titleText = title)
    }


    private fun setTitle(@StringRes titleRes: Int = -1, titleText: String = "") {
        if (titleRes != -1)
            cardTitle.setText(titleRes)
        else
            cardTitle.text = titleText
    }

    /**
     * Sets the icon of the card
     * @param drawable the drawable
     */
    fun setIcon(@DrawableRes drawable: Int) {
        setIcon(drawableRes = drawable)
    }

    /**
     * Sets the icon of the card
     * @param drawable the drawable
     */
    fun setIcon(drawable: Drawable) {
        setIcon(drawableFile = drawable)
    }

    private fun setIcon(@DrawableRes drawableRes: Int = -1, drawableFile: Drawable? = null) {
        if (drawableRes != -1) {
            iconDrawable = ContextCompat.getDrawable(context, drawableRes)
            cardIcon.background = iconDrawable
        } else {
            cardIcon.background = drawableFile
            iconDrawable = drawableFile
        }

    }

    /**
     * Sets the icon tint of the card
     * @param tint the tint
     */
    fun setIconTintRes(@ColorRes tint: Int) {
        setIconTint(ContextCompat.getColor(context, tint))
    }

    /**
     * Sets the icon tint of the card
     * @param red the red value
     * @param green the green value
     * @param blue the blue value
     */
    fun setIconTint(
        @IntRange(from = 0, to = 255) red: Int,
        @IntRange(from = 0, to = 255) green: Int,
        @IntRange(from = 0, to = 255) blue: Int
    ) {
        setIconTint(Color.rgb(red, green, blue))
    }

    /**
     * Sets the icon tint of the card
     * @param tint the tint
     */
    fun setIconTint(tint: String) {
        setIconTint(Color.parseColor(tint))
    }

    /**
     * Sets the icon tint of the card
     * @param tint the tint
     */
    fun setIconTint(@ColorInt tint: Int) {
        try {
            iconDrawableTint = tint
            iconDrawable?.setTint(iconDrawableTint)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Sets the arrow icon of the card
     * @param drawable the drawable
     */
    fun setIconArrow(@DrawableRes drawable: Int) {
        setIcon(drawableRes = drawable)
    }

    /**
     * Sets the arrow icon of the card
     * @param drawable the drawable
     */
    fun setIconArrow(drawable: Drawable) {
        setIcon(drawableFile = drawable)
    }

    private fun setIconArrow(@DrawableRes drawableRes: Int = -1, drawableFile: Drawable? = null) {
        if (drawableRes != -1) {
            iconDrawableArrow = ContextCompat.getDrawable(context, drawableRes)
            cardArrow.background = iconDrawableArrow
        } else {
            cardArrow.background = drawableFile
            iconDrawableArrow = drawableFile
        }

    }

    /**
     * Sets the arrow icon tint of the card
     * @param tint the tint
     */
    fun setIconArrowTintRes(@ColorRes tint: Int) {
        setIconArrowTint(ContextCompat.getColor(context, tint))
    }

    /**
     * Sets the arrow icon tint of the card
     * @param red the red value
     * @param green the green value
     * @param blue the blue value
     */
    fun setIconArrowTint(
        @IntRange(from = 0, to = 255) red: Int,
        @IntRange(from = 0, to = 255) green: Int,
        @IntRange(from = 0, to = 255) blue: Int
    ) {
        setIconArrowTint(Color.rgb(red, green, blue))
    }

    /**
     * Sets the arrow icon tint of the card
     * @param tint the tint
     */
    fun setIconArrowTint(tint: String) {
        setIconArrowTint(Color.parseColor(tint))
    }

    /**
     * Sets the arrow icon tint of the card
     * @param tint the tint
     */
    fun setIconArrowTint(@ColorInt tint: Int) {
        try {
            iconDrawableArrowTint = tint
            iconDrawableArrow?.setTint(iconDrawableArrowTint)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Sets the inner view of the card
     * @param resId the resource id
     */
    private fun setInnerView(@LayoutRes resId: Int) {
        cardStub.layoutResource = resId
        innerView = cardStub.inflate()
    }


    override fun setOnClickListener(l: OnClickListener?) {
        cardArrow.setOnClickListener(l)
        super.setOnClickListener(l)
    }


    /**
     * Interfaces
     */

    interface OnExpandedListener {

        fun onExpandChanged(v: View?, isExpanded: Boolean)

    }

    companion object {

        private const val DEFAULT_ANIM_DURATION = 350

        private const val COLLAPSING = 0
        private const val EXPANDING = 1
    }

}