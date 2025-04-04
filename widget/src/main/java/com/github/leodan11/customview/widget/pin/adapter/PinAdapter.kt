package com.github.leodan11.customview.widget.pin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.github.leodan11.customview.widget.databinding.PinItemBinding
import com.github.leodan11.customview.widget.pin.model.PinModel
import com.github.leodan11.customview.widget.pin.model.PinParams
import com.github.leodan11.customview.widget.pin.state.PinState
import kotlinx.coroutines.flow.MutableSharedFlow

class PinAdapter(
    private val pinParams: PinParams,
    private val pinEventEmitter: MutableSharedFlow<PinState>
) : ListAdapter<PinModel, PinViewHolder>(PIN_DIFF_UTIL) {

    init {
        submitList(List(pinParams.pinCount) { PinModel(index = it) })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinViewHolder {
        return PinViewHolder(
            PinItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), pinEventEmitter
        )
    }

    override fun onBindViewHolder(
        holder: PinViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            payloads.forEach {
                when (it) {
                    UPDATE_BACKGROUND_PAYLOAD -> holder.setBackground(pinParams.pinBackground)
                    UPDATE_IS_ENABLED_PAYLOAD -> holder.setIsEnabled(pinParams.isEnabled)
                    UPDATE_PIN_COLOR_PAYLOAD -> holder.setPinColor(pinParams.pinColor)
                    CLEAR_PIN_COLOR_PAYLOAD -> holder.clearPinColor()
                    CLEAR_TEXT_PAYLOAD -> holder.clearText()
                    UPDATE_TEXT_VISIBILITY_PAYLOAD -> holder.setIsPassword(pinParams.isPassword)
                }
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: PinViewHolder, position: Int) {
        holder.bind(getItem(position))

        holder.setBackground(pinParams.pinBackground)
        holder.setIsPassword(pinParams.isPassword)
        holder.setTextStyle(pinParams.fontFamilyID, pinParams.textStyle)
        holder.setTextSize(pinParams.textSize)
        holder.setTextColor(pinParams.textColor)
        holder.setTextMask(pinParams.pinMask)
    }

    fun setPinBackground(pinBackground: Int) {
        pinParams.pinBackground = pinBackground
        notifyItemRangeChanged(0, currentList.size, UPDATE_BACKGROUND_PAYLOAD)
    }

    fun setIsEnabled(isEnabled: Boolean) {
        pinParams.isEnabled = isEnabled
        notifyItemRangeChanged(0, currentList.size, UPDATE_IS_ENABLED_PAYLOAD)
    }

    fun getText(): String {
        val stringBuilder = StringBuilder()
        currentList.map {
            stringBuilder.append(it.text ?: "")
        }
        return stringBuilder.toString()
    }

    fun clearText() {
        notifyItemRangeChanged(0, currentList.size, CLEAR_TEXT_PAYLOAD)
    }

    fun setPinColor(@ColorRes color: Int) {
        pinParams.pinColor = color
        notifyItemRangeChanged(0, currentList.size, UPDATE_PIN_COLOR_PAYLOAD)
    }

    fun clearPinColor() {
        notifyItemRangeChanged(0, currentList.size, CLEAR_PIN_COLOR_PAYLOAD)
    }

    fun setTextVisible(isVisible: Boolean) {
        pinParams.isPassword = !isVisible
        notifyItemRangeChanged(0, currentList.size, UPDATE_TEXT_VISIBILITY_PAYLOAD)
    }

    fun isTextVisible() = !pinParams.isPassword

    companion object {

        private const val UPDATE_BACKGROUND_PAYLOAD = "UPDATE_BACKGROUND_PAYLOAD"
        private const val UPDATE_IS_ENABLED_PAYLOAD = "UPDATE_IS_ENABLED_PAYLOAD"
        private const val UPDATE_PIN_COLOR_PAYLOAD = "UPDATE_PIN_COLOR_PAYLOAD"
        private const val CLEAR_PIN_COLOR_PAYLOAD = "CLEAR_PIN_COLOR_PAYLOAD"
        private const val CLEAR_TEXT_PAYLOAD = "CLEAR_TEXT_PAYLOAD"
        private const val UPDATE_TEXT_VISIBILITY_PAYLOAD = "UPDATE_VISIBILITY_PAYLOAD"

        private val PIN_DIFF_UTIL = object : DiffUtil.ItemCallback<PinModel>() {
            override fun areItemsTheSame(oldItem: PinModel, newItem: PinModel): Boolean {
                return oldItem.index == newItem.index
            }

            override fun areContentsTheSame(oldItem: PinModel, newItem: PinModel): Boolean {
                return oldItem == newItem
            }

        }
    }
}