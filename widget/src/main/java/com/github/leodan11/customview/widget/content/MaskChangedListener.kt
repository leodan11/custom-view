package com.github.leodan11.customview.widget.content

import android.text.Editable
import android.text.TextWatcher
import com.github.leodan11.customview.widget.content.model.Mask
import com.github.leodan11.customview.widget.content.model.MaskResult
import com.github.leodan11.customview.widget.content.model.apply
import com.github.leodan11.customview.widget.tools.Action

class MaskChangedListener(mask: Mask) : TextWatcher {

    private val maskara: Maskara = Maskara(mask)

    private var selfChange: Boolean = false
    private var result: MaskResult? = null

    val masked: String
        get() = result?.masked.orEmpty()

    val unMasked: String
        get() = result?.unMasked.orEmpty()

    val isDone: Boolean
        get() = result?.isDone ?: false

    override fun afterTextChanged(s: Editable?) {
        if (selfChange || s.isNullOrEmpty()) return

        selfChange = true
        result?.apply(s)
        selfChange = false
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (selfChange || s.isNullOrEmpty()) return

        val action = if (before > 0 && count == 0) Action.DELETE else Action.INSERT
        result = maskara.apply(s, action)
    }

}
