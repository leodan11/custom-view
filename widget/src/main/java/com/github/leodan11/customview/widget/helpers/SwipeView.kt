package com.github.leodan11.customview.widget.helpers

import com.github.leodan11.customview.widget.R

class SwipeView(icons: IntArray, backgrounds: IntArray, texts: Array<String>) {

    private var mIcons: IntArray = icons
    private var mBackgrounds: IntArray
    private var mTexts: Array<String>
    var textColor = android.R.color.white
    var textSize = 15
    var leftText: String = ""
    var rightText: String = ""

    var leftIcon: Int = R.drawable.ic_keyboard_arrow_down_baseline
    var rightIcon: Int = R.drawable.ic_keyboard_arrow_down_baseline
    var leftBg: Int = android.R.color.holo_red_light
    var leftBgInt: Int? = null
    var rightBg: Int = android.R.color.holo_green_light
    var rightBgInt: Int? = null

    init {
        if (mIcons.size == 2) {
            leftIcon = mIcons[0]
            rightIcon = mIcons[1]
        }
        mBackgrounds = backgrounds
        if (backgrounds.size == 2) {
            leftBg = mBackgrounds[0]
            rightBg = mBackgrounds[1]
        }
        mTexts = texts
        if (mTexts.size == 2) {
            leftText = mTexts[0]
            rightText = mTexts[1]
        }
    }

    val isFull: Boolean
        get() = mBackgrounds.size == 2 && mIcons.size == 2 && mTexts.size == 2

}