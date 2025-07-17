package com.github.leodan11.customview.textfield.model

import com.github.leodan11.customview.textfield.base.FormattedString
import com.github.leodan11.customview.textfield.base.MaskCharacter
import com.github.leodan11.customview.textfield.content.IFormattedString

class Masked() {

    lateinit var formatString: String
    private lateinit var mMask: List<MaskCharacter>
    private val mFabric: MaskCharacterFabric = MaskCharacterFabric()
    private var mPrepopulateCharacter: MutableList<MaskCharacter>? = null

    constructor(fmtString: String) : this() {
        formatString = fmtString
        mMask = buildMask(formatString)
    }

    fun size(): Int {
        return mMask.size
    }

    operator fun get(index: Int): MaskCharacter {
        return mMask[index]
    }


    fun isValidPrepopulateCharacter(ch: Char, at: Int): Boolean {
        return try {
            val character = mMask[at]
            character.isPrepopulate && character.isValidCharacter(ch)
        } catch (e: IndexOutOfBoundsException) {
            false
        }

    }

    fun isValidPrepopulateCharacter(ch: Char): Boolean {
        for (maskCharacter in mPrepopulateCharacter!!) {
            if (maskCharacter.isValidCharacter(ch)) {
                return true
            }
        }
        return false
    }


    private fun buildMask(fmtString: String): List<MaskCharacter> {
        val result = ArrayList<MaskCharacter>()
        mPrepopulateCharacter = ArrayList()
        for (ch in fmtString.toCharArray()) {
            val maskCharacter = mFabric.buildCharacter(ch)
            if (maskCharacter.isPrepopulate) {
                mPrepopulateCharacter?.add(maskCharacter)
            }
            maskCharacter.let { result.add(it) }
        }
        return result
    }


    fun getFormattedString(value: String): IFormattedString {
        return FormattedString(this, value)
    }

}