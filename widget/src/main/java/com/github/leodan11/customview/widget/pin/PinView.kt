package com.github.leodan11.customview.widget.pin

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.leodan11.customview.widget.R
import com.github.leodan11.customview.widget.databinding.PinViewLayoutBinding
import com.github.leodan11.customview.widget.pin.adapter.PinAdapter
import com.github.leodan11.customview.widget.pin.adapter.SpaceItemDecoration
import com.github.leodan11.customview.widget.pin.model.PinListener
import com.github.leodan11.customview.widget.pin.model.PinParams
import com.github.leodan11.customview.widget.pin.state.PinState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PinView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs), View.OnAttachStateChangeListener {

    private val binding = PinViewLayoutBinding.inflate(LayoutInflater.from(context), this, true)
    private var adapter: PinAdapter? = null

    private var pinCount: Int = 4
    private var isPassword: Boolean = true
    private var pinPadding: Float = 2f
    private var textSize: Int? = null
    private var textColor: Int? = null
    private lateinit var pinMask: String
    private var textStyle: Int? = null
    private var fontFamilyId: Int? = null

    private var pinBackground = R.drawable.background_pin_item
    private var viewScope: CoroutineContext = SupervisorJob() + Dispatchers.Main

    private val pinStateEmitter = MutableSharedFlow<PinState>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    var text: String?
        get() = adapter?.getText()
        set(value) {
            pinStateEmitter.tryEmit(PinState.SetText(value))
        }

    private var onTextChangedListener: PinListener? = null

    init {
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.PinView, R.attr.pinViewStyle, 0
        ).runCatching {
            pinCount = getInt(R.styleable.PinView_pinCount, pinCount)
            isPassword = getBoolean(R.styleable.PinView_isPassword, isPassword)
            pinPadding = getDimension(R.styleable.PinView_pinPadding, pinPadding)
            pinBackground = getResourceId(
                R.styleable.PinView_pinBackground,
                R.drawable.background_pin_item
            )
            pinMask = getString(R.styleable.PinView_pinMask) ?: DEFAULT_PATTERN

            fontFamilyId = getResourceId(R.styleable.PinView_android_fontFamily, 0)
            textSize = getDimensionPixelSize(R.styleable.PinView_android_textSize, 0)
            textColor = getColor(R.styleable.PinView_android_textColor, 0)
            textStyle = getInt(R.styleable.PinView_android_textStyle, 0)
            recycle()
        }

        val pinParams = PinParams(
            pinCount,
            isPassword,
            pinPadding,
            pinBackground,
            pinMask,
            textSize = textSize,
            textColor = textColor,
            textStyle = textStyle,
            fontFamilyID = fontFamilyId
        )

        addOnAttachStateChangeListener(this)
        addPinStateObserver()

        setupPinRecycler(pinParams, context)
    }

    fun clear() {
        adapter?.clearText()
    }

    fun requestFocusToPin(pinIndex: Int = 0) {
        pinStateEmitter.tryEmit(PinState.RequestFocus(index = pinIndex))
    }

    fun setPinBackground(pinBackground: Int) {
        adapter?.setPinBackground(pinBackground)
    }

    fun setIsEnabled(isEnabled: Boolean) {
        adapter?.setIsEnabled(isEnabled)
    }

    fun setPinColor(@ColorRes color: Int) {
        adapter?.setPinColor(color)
    }

    fun clearPinColor() {
        adapter?.clearPinColor()
    }

    fun isTextVisible() = adapter?.isTextVisible() == true


    fun setTextVisible(isVisible: Boolean) {
        adapter?.setTextVisible(isVisible)
    }

    fun setPinCount(pinCount: Int) {
        this.pinCount = pinCount
        onRecreatePinParams()
    }

    fun setPinMask(pinMask: String) {
        this.pinMask = pinMask
        onRecreatePinParams()
    }

    fun setOnTextChangedListener(onTextChangedListener: PinListener) {
        this.onTextChangedListener = onTextChangedListener
    }

    private fun addPinStateObserver() {
        CoroutineScope(viewScope).launch {
            pinStateEmitter.collectLatest {
                if (it is PinState.TextUnitChanged) {
                    onTextChangedListener?.onTextChangedListener(text)
                    if (text != null && text?.length == pinCount) {
                        onTextChangedListener?.onPinCompletedListener(text!!)
                    }
                    if (it.text?.isNotEmpty() == true) {
                        pinStateEmitter.tryEmit(PinState.RequestFocus(index = it.index + 1))
                    }
                }
            }
        }
    }

    private fun onRecreatePinParams() {
        val pinParams = PinParams(
            pinCount,
            isPassword,
            pinPadding,
            pinBackground,
            pinMask,
            textSize = textSize,
            textColor = textColor,
            textStyle = textStyle,
            fontFamilyID = fontFamilyId
        )
        setupPinRecycler(pinParams, context)
    }

    private fun setupPinRecycler(pinParams: PinParams, context: Context) {
        adapter = PinAdapter(pinParams, pinStateEmitter)

        binding.recyclerPin.apply {
            adapter = this@PinView.adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            addItemDecoration(SpaceItemDecoration(pinPadding.toInt()))
        }
    }

    override fun onViewAttachedToWindow(view: View) = Unit

    override fun onViewDetachedFromWindow(view: View) {
        viewScope.cancel()
    }

    companion object {
        private const val DEFAULT_PATTERN: String = "[A-Za-z]*"
    }

}