package com.github.leodan11.customview.widget.pin.state

sealed class PinState {
    class TextUnitChanged(val index: Int, val text: String?) : PinState()
    class SetText(val text: String?) : PinState()
    class RequestFocus(val index: Int) : PinState()
    data object Clear : PinState()
}