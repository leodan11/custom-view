package com.github.leodan11.customview.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.leodan11.customview.widget.helpers.SwipeCallback
import com.github.leodan11.customview.widget.helpers.SwipeListener
import com.github.leodan11.customview.widget.helpers.SwipeView

class RecyclerViewSwipe(
    context: Context,
    attrs: AttributeSet?,
    private val recyclerView: RecyclerView
) : SwipeListener {

    private var mListener: SwipeListener? = null
    private lateinit var mSwipedView: SwipeView

    init {
        createSwipedView(context, attrs)
        recyclerView.setWillNotDraw(false)
    }

    fun setRightBg(bg: Int): RecyclerViewSwipe {
        mSwipedView.rightBg = bg
        return this
    }

    fun setLeftBg(bg: Int): RecyclerViewSwipe {
        mSwipedView.leftBg = bg
        return this
    }

    fun setRightText(text: String): RecyclerViewSwipe {
        mSwipedView.rightText = text
        return this
    }

    fun setLeftText(text: String): RecyclerViewSwipe {
        mSwipedView.leftText = text
        return this
    }

    fun setRightImage(imgRef: Int): RecyclerViewSwipe {
        mSwipedView.rightIcon = imgRef
        return this
    }

    fun setLeftImage(imgRef: Int): RecyclerViewSwipe {
        mSwipedView.leftIcon = imgRef
        return this
    }

    fun setTextColor(color: Int): RecyclerViewSwipe {
        mSwipedView.textColor = color
        return this
    }

    fun setTextSize(size: Int): RecyclerViewSwipe {
        mSwipedView.textSize = size
        return this
    }

    fun setListener(listener: SwipeListener): RecyclerViewSwipe {
        mListener = listener
        return this
    }

    override fun onSwipedLeft(position: Int) {
        mListener?.onSwipedLeft(position)
    }

    override fun onSwipedRight(position: Int) {
        mListener?.onSwipedRight(position)
    }

    private fun createSwipedView(context: Context, attrs: AttributeSet?) {
        //obtain attributes
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.RecyclerViewSwipe, 0, 0)
        mSwipedView = SwipeView(IntArray(0), IntArray(0), emptyArray())
        mSwipedView.textColor =
            typedArray.getColor(R.styleable.RecyclerViewSwipe_textColorSwipe, Color.BLACK)
        mSwipedView.textSize =
            typedArray.getDimension(R.styleable.RecyclerViewSwipe_textSizeSwipe, 15f).toInt()
    }

    fun createSwipeAble() {
        val mSwipeCallBack = SwipeCallback(this, mSwipedView)
        ItemTouchHelper(mSwipeCallBack).attachToRecyclerView(recyclerView)
    }

}