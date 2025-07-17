package com.github.leodan11.customview.textfield.base

import com.github.leodan11.customview.textfield.content.IFormattedString
import com.github.leodan11.customview.textfield.model.Masked

internal abstract class AbstractFormattedString(val mMask: Masked, val inputString: String) : IFormattedString {

    private var mFormattedString: String? = null
    final override val unMaskedString: String


    init {
        unMaskedString = this.buildRawString(inputString)
    }


    internal abstract fun formatString(): String

    internal abstract fun buildRawString(str: String): String


    override val length: Int
        get() = toString().length

    override fun toString(): String {
        return mFormattedString ?: formatString()
    }

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
        return toString().subSequence(startIndex, endIndex)
    }

    override fun get(index: Int): Char {
        return toString()[index]
    }
}

internal class FormattedString(mask: Masked, rawString: String) : AbstractFormattedString(mask, rawString) {

    override fun buildRawString(str: String): String {
        val builder = StringBuilder()
        val inputLen = mMask.size().coerceAtMost(str.length)
        for (i in 0 ..< inputLen) {
            val ch = str[i]
            if (!mMask.isValidPrepopulateCharacter(ch, i))
                builder.append(ch)
        }
        return builder.toString()
    }

    override fun formatString(): String {
        val builder = StringBuilder()

        var strIndex = 0
        var maskCharIndex = 0
        var stringCharacter: Char

        while (strIndex < inputString.length && maskCharIndex < mMask.size()) {
            val maskChar = mMask[maskCharIndex]

            stringCharacter = inputString[strIndex]

            when {
                maskChar.isValidCharacter(stringCharacter) -> {
                    builder.append(maskChar.processCharacter(stringCharacter))
                    strIndex += 1
                    maskCharIndex += 1
                }
                maskChar.isPrepopulate -> {
                    builder.append(maskChar.processCharacter(stringCharacter))
                    maskCharIndex += 1
                }
                else -> strIndex += 1
            }
        }

        return builder.toString()
    }
}