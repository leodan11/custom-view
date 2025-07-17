package com.github.leodan11.customview.textfield

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.github.leodan11.customview.textfield.content.MaskedWatcher
import com.github.leodan11.customview.textfield.model.MaskedFormatter
import androidx.core.content.withStyledAttributes

class InputMaskedEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var maskedFormatter: MaskedFormatter? = null
    var maskedWatcher: MaskedWatcher? = null

    val maskString: String?
        get() = maskedFormatter?.maskString

    val unMaskedText: String?
        get() {
            val currentText = text?.toString()
            val formattedString = currentText?.let { maskedFormatter?.formatString(it) }
            return formattedString?.unMaskedString
        }

    init {

        context.withStyledAttributes(attrs, R.styleable.InputMaskedEditText) {

            if (hasValue(R.styleable.InputMaskedEditText_masked)) {
                val maskStr = getString(R.styleable.InputMaskedEditText_masked)

                if (!maskStr.isNullOrEmpty()) {
                    setMask(maskStr)
                }
            }

        }
    }

    fun setMask(mMaskStr: String) {
        maskedFormatter = MaskedFormatter(mMaskStr)

        if (maskedWatcher != null) {
            removeTextChangedListener(maskedWatcher)
        }

        maskedFormatter?.let { maskedWatcher = MaskedWatcher(it, this) }
        addTextChangedListener(maskedWatcher)
    }
}