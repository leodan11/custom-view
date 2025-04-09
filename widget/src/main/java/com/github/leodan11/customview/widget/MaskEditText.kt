package com.github.leodan11.customview.widget

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.github.leodan11.customview.widget.content.MaskChangedListener
import com.github.leodan11.customview.widget.content.MaskStyle
import com.github.leodan11.customview.widget.content.model.Mask
import com.github.leodan11.customview.widget.content.model.mostOccurred

class MaskEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var maskChangedListener: MaskChangedListener? = null

    val masked: String
        get() = maskChangedListener?.masked.orEmpty()

    val unMasked: String
        get() = maskChangedListener?.unMasked.orEmpty()

    val isDone: Boolean
        get() = maskChangedListener?.isDone ?: false

    init {
        context.obtainStyledAttributes(attrs, R.styleable.MaskEditText).apply {
            val style = getInteger(R.styleable.MaskEditText_maskStyle, 0)
            val value = getString(R.styleable.MaskEditText_mask).orEmpty()
            val character = getString(R.styleable.MaskEditText_maskCharacter).orEmpty()

            if (value.isNotEmpty()) {
                val maskChar = if (character.isEmpty()) value.mostOccurred() else character.single()
                val mask = Mask(value, maskChar, MaskStyle.valueOf(style))
                maskChangedListener = MaskChangedListener(mask)
            }

            recycle()
        }
    }

    /**
     * Let only one [maskChangedListener] allowed at a time
     */
    override fun addTextChangedListener(watcher: TextWatcher?) {
        if (watcher is MaskChangedListener) {
            removeTextChangedListener(maskChangedListener)
            maskChangedListener = watcher
        }
        super.addTextChangedListener(watcher)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        addTextChangedListener(maskChangedListener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeTextChangedListener(maskChangedListener)
    }

}