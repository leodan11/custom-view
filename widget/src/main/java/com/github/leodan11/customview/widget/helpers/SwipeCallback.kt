package com.github.leodan11.customview.widget.helpers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.leodan11.customview.core.utils.Converters

class SwipeCallback(private val listener: SwipeListener, private val swipeView: SwipeView) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val movementFlags: Int = returnMovementFlags()
        return if (movementFlags != -1) movementFlags else super.getMovementFlags(
            recyclerView,
            viewHolder
        )
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (direction == ItemTouchHelper.RIGHT) {
            listener.onSwipedRight(viewHolder.adapterPosition)
        } else {
            listener.onSwipedLeft(viewHolder.adapterPosition)
        }
    }

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(
            c, recyclerView, viewHolder, dX, dY,
            actionState, isCurrentlyActive
        )
        val v = viewHolder.itemView
        val context = v.context
        var toDraw: ChildToDraw? = null
        if (dX > 0) {
            toDraw = ChildToDraw(dX.toInt(), v, context, ChildToDraw.LEFT, swipeView).invoke()
        } else if (dX < 0) {
            toDraw = ChildToDraw(dX.toInt(), v, context, ChildToDraw.RIGHT, swipeView).invoke()
        }
        toDraw?.let { draw(c, context, it) }
    }

    private fun draw(c: Canvas, context: Context, toDraw: ChildToDraw) {
        val bg: ColorDrawable? = toDraw.bg
        val icon: Drawable? = toDraw.icon
        val paint: Paint? = toDraw.paintText
        paint?.color = swipeView.textColor
        bg?.draw(c)
        icon?.draw(c)
        c.drawText(
            toDraw.text ?: "",
            icon?.bounds?.centerX()?.toFloat() ?: 0f,
            (icon?.bounds?.centerY()?.toFloat() ?: 0f) + (icon?.bounds?.height()
                ?: 0) + Converters.convertDpToPixels(context, 5f),
            (paint!!)
        )
    }

    private fun returnMovementFlags(): Int {
        if (((swipeView.leftIcon == -1) && (
                    swipeView.leftBg == android.R.color.holo_red_light) &&
                    swipeView.leftText.isEmpty() && (
                    swipeView.rightIcon == -1) && (
                    swipeView.rightBg == android.R.color.holo_green_light) &&
                    swipeView.rightText.isEmpty())
        ) return makeMovementFlags(0, 0)
        if (((swipeView.leftIcon == -1) && (
                    swipeView.leftBg == android.R.color.holo_red_light) &&
                    swipeView.leftText.isEmpty())
        ) return makeMovementFlags(0, ItemTouchHelper.LEFT)
        return if (((swipeView.leftIcon == -1) && (
                    swipeView.rightBg == android.R.color.holo_green_light) &&
                    swipeView.rightText.isEmpty())
        ) makeMovementFlags(0, ItemTouchHelper.RIGHT) else -1
    }


}