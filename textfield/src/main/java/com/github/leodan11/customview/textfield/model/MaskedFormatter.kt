package com.github.leodan11.customview.textfield.model

import com.github.leodan11.customview.textfield.content.IFormattedString

class MaskedFormatter internal constructor() {

    internal var mMask: Masked? = null


    val maskString: String?
        get() = mMask?.formatString

    val maskLength: Int?
        get() = mMask?.size()

    init {
        mMask = null
    }

    constructor(fmtString: String) : this() {
        this.setMask(fmtString)
    }

    fun setMask(fmtString: String) {
        mMask = Masked(fmtString)
    }


    fun formatString(value: String): IFormattedString? {
        return mMask?.getFormattedString(value)
    }

}