package com.github.leodan11.customview.widget.pin.model

data class PinParams(
    var pinCount: Int = 4,
    var isPassword: Boolean = true,
    var pinPadding: Float = 2f,
    var pinBackground: Int?,
    var pinMask: String = "",
    var textSize: Int? = null,
    var textColor: Int? = null,
    var textStyle: Int? = null,
    var fontFamilyID: Int? = null,
    var isEnabled: Boolean = false,
    var pinColor: Int? = null
)
