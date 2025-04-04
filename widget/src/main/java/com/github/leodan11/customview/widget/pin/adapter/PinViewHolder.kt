package com.github.leodan11.customview.widget.pin.adapter

import android.graphics.Typeface
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.TypedValue
import android.view.KeyEvent
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.leodan11.customview.widget.databinding.PinItemBinding
import com.github.leodan11.customview.widget.pin.model.PinModel
import com.github.leodan11.customview.widget.pin.setColorFilter
import com.github.leodan11.customview.widget.pin.state.PinState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import java.util.regex.Pattern

class PinViewHolder(
    private val itemBinding: PinItemBinding,
    private val pinStateFlow: MutableSharedFlow<PinState>
) : RecyclerView.ViewHolder(itemBinding.root) {

    private var pinModel: PinModel? = null
    private var emitOnTextChanged = AtomicBoolean(true)

    fun bind(pinModel: PinModel) {
        this.pinModel = pinModel

        CoroutineScope(Dispatchers.Main).launch {
            pinStateFlow.collectLatest {
                when (it) {
                    is PinState.RequestFocus -> onFocusRequested(it.index)
                    is PinState.Clear -> clearText()
                    is PinState.SetText -> setText(it.text)
                    else -> {}
                }
            }
        }

        itemBinding.editPin.setText(pinModel.text)

        setupDelKeyListener()
        setupOnTextChanged()
    }

    private fun setText(text: String?) {
        emitOnTextChanged.set(false)
        itemBinding.editPin.setText(
            text?.getOrNull(adapterPosition)
                ?.toString() ?: ""
        )
        emitOnTextChanged.set(true)
    }

    fun clearText() {
        emitOnTextChanged.set(false)
        itemBinding.editPin.text.clear()
        emitOnTextChanged.set(true)
    }

    private fun setupOnTextChanged() {
        itemBinding.editPin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                pinModel?.text = s.toString()

                if (emitOnTextChanged.get())
                    pinStateFlow.tryEmit(
                        PinState.TextUnitChanged(
                            index = adapterPosition,
                            text = pinModel?.text
                        )
                    )
            }

            override fun afterTextChanged(s: Editable?) = Unit
        })
    }

    private fun setupDelKeyListener() {
        itemBinding.editPin.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN)
                pinStateFlow.tryEmit(PinState.RequestFocus(index = adapterPosition - 1))
            return@setOnKeyListener false
        }
    }

    private fun onFocusRequested(indexToFocus: Int) {
        if (indexToFocus == adapterPosition)
            with(itemBinding.editPin) {
                requestFocus()
                setSelection(length())
            }
    }

    fun setIsPassword(password: Boolean) {
        with(itemBinding.editPin) {
            emitOnTextChanged.set(false)
            val typeface = typeface

            inputType = if (password)
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            else
                InputType.TYPE_CLASS_TEXT

            setTypeface(typeface)
            setSelection(length())
            emitOnTextChanged.set(true)
        }
    }

    fun setTextStyle(fontFamilyId: Int?, textStyle: Int?) {
        runCatching {
            val font = if (fontFamilyId != null && fontFamilyId != 0) ResourcesCompat.getFont(
                itemBinding.root.context,
                fontFamilyId
            )
            else null

            itemBinding.editPin.setTypeface(
                font, textStyle ?: Typeface.NORMAL
            )
        }
    }

    fun setTextSize(textSize: Int?) {
        if (textSize != null && textSize > 0)
            itemBinding.editPin.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
    }

    fun setTextColor(textColor: Int?) {
        if (textColor != null && textColor != 0) itemBinding.editPin.setTextColor(textColor)
    }

    fun setBackground(pinBackground: Int?) {
        if (pinBackground != null) itemBinding.editPin.setBackgroundResource(pinBackground)
    }

    fun setIsEnabled(isEnabled: Boolean) {
        itemBinding.editPin.isEnabled = isEnabled
    }

    fun setPinColor(@ColorRes pinColor: Int?) {
        if (pinColor != null) itemBinding.editPin.setColorFilter(pinColor)
    }

    fun setTextMask(pattern: String) {
        val filter = InputFilter { source, start, end, _, _, _ ->
            for (i in start until end) {
                if (!Pattern.compile(pattern).matcher(source[i].toString()).matches()) {
                    return@InputFilter ""
                }
            }
            null
        }
        itemBinding.editPin.filters = arrayOf(filter, InputFilter.LengthFilter(1))
    }

    fun clearPinColor() {
        itemBinding.editPin.background?.clearColorFilter()
    }
}