package com.github.leodan11.customview.textfield.model

import com.github.leodan11.customview.textfield.base.AlphaNumericCharacter
import com.github.leodan11.customview.textfield.base.DigitCharacter
import com.github.leodan11.customview.textfield.base.HexCharacter
import com.github.leodan11.customview.textfield.base.LetterCharacter
import com.github.leodan11.customview.textfield.base.LiteralCharacter
import com.github.leodan11.customview.textfield.base.LowerCaseCharacter
import com.github.leodan11.customview.textfield.base.MaskCharacter
import com.github.leodan11.customview.textfield.base.UpperCaseCharacter

internal class MaskCharacterFabric {

    fun buildCharacter(ch: Char): MaskCharacter {
        return when (ch) {
            ANYTHING_KEY -> LiteralCharacter()
            DIGIT_KEY -> DigitCharacter()
            UPPERCASE_KEY -> UpperCaseCharacter()
            LOWERCASE_KEY -> LowerCaseCharacter()
            ALPHA_NUMERIC_KEY -> AlphaNumericCharacter()
            CHARACTER_KEY -> LetterCharacter()
            HEX_KEY -> HexCharacter()
            else -> LiteralCharacter(ch)
        }
    }

    companion object {
        private const val ANYTHING_KEY = '*'
        private const val DIGIT_KEY = '#'
        private const val UPPERCASE_KEY = 'U'
        private const val LOWERCASE_KEY = 'L'
        private const val ALPHA_NUMERIC_KEY = 'A'
        private const val CHARACTER_KEY = '?'
        private const val HEX_KEY = 'H'
    }

}
