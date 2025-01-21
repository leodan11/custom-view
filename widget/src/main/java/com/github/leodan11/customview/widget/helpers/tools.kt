package com.github.leodan11.customview.widget.helpers

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.github.leodan11.customview.widget.RecyclerViewSwipe

/**
 *
 * Create a [RecyclerViewSwipe] object
 *
 * @receiver [RecyclerView]
 *
 * @param context [Context] current context
 * @param attrs [AttributeSet]? attributes. Default is `null`
 *
 * @return [RecyclerViewSwipe]
 *
 */
fun RecyclerView.makeLeftRightSwipeAble(context: Context, attrs: AttributeSet? = null): RecyclerViewSwipe {
    return RecyclerViewSwipe(context, attrs, this)
}