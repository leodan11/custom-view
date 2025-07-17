package com.github.leodan11.customview.textfield.content

import android.text.Editable
import android.text.TextWatcher
import com.github.leodan11.customview.textfield.InputMaskedEditText


/**
 * Adds an action that will be invoked **before** the text is changed in this [InputMaskedEditText].
 *
 * This is a convenient way to listen for text changes without manually managing a [TextWatcher].
 *
 * @param action A lambda to invoke before the text is changed. Parameters provide the current text,
 * the start index, the number of characters that will be replaced, and the number of characters that will replace them.
 * @return The [MaskedWatcher] instance attached to this [InputMaskedEditText], or `null` if none is available.
 *
 *
 * @see InputMaskedEditText.doOnMaskedTextChanged
 * @see InputMaskedEditText.doAfterMaskedTextChanged
 */
fun InputMaskedEditText.doBeforeMaskedTextChanged(action: (text: CharSequence?, start: Int, count: Int, after: Int) -> Unit) =
    maskedWatcher?.apply {
        addTextChangedListener(beforeTextChanged = action)
    }

/**
 * Adds an action that will be invoked **as** the text is being changed in this [InputMaskedEditText].
 *
 * Useful for reacting to text modifications in real-time.
 *
 * @param action A lambda to invoke during text change. Parameters provide the current text,
 * the start index, the number of characters before the change, and the number of characters after the change.
 * @return The [MaskedWatcher] instance attached to this [InputMaskedEditText], or `null` if none is available.
 *
 *
 * @see InputMaskedEditText.doBeforeMaskedTextChanged
 * @see InputMaskedEditText.doAfterMaskedTextChanged
 */
fun InputMaskedEditText.doOnMaskedTextChanged(action: (text: CharSequence?, start: Int, before: Int, count: Int) -> Unit) =
    maskedWatcher?.apply {
        addTextChangedListener(onTextChanged = action)
    }

/**
 * Adds an action that will be invoked **after** the text has been changed in this [InputMaskedEditText].
 *
 * This is typically where validation or formatting logic is applied.
 *
 * @param action A lambda to invoke after the text has been changed. The [Editable] represents the new state of the text.
 * @return The [MaskedWatcher] instance attached to this [InputMaskedEditText], or `null` if none is available.
 *
 *
 * @see InputMaskedEditText.doBeforeMaskedTextChanged
 * @see InputMaskedEditText.doOnMaskedTextChanged
 */
fun InputMaskedEditText.doAfterMaskedTextChanged(action: (text: Editable?) -> Unit) =
    maskedWatcher?.apply {
        addTextChangedListener(afterTextChanged = action)
    }

/**
 * Adds a [TextWatcher] to this [InputMaskedEditText] with optional actions for each text change phase.
 *
 * This is a flexible method for listening to all phases of text change in one call.
 *
 * @param beforeMaskedTextChanged A lambda to be called **before** text is changed.
 * @param onMaskedTextChanged A lambda to be called **as** text is being changed.
 * @param afterMaskedTextChanged A lambda to be called **after** text has changed.
 * @return The [MaskedWatcher] instance attached to this [InputMaskedEditText], or `null` if none is available.
 *
 *
 * @see InputMaskedEditText.doBeforeMaskedTextChanged
 * @see InputMaskedEditText.doOnMaskedTextChanged
 * @see InputMaskedEditText.doAfterMaskedTextChanged
 */
fun InputMaskedEditText.addMaskedTextChangedListener(
    beforeMaskedTextChanged: ((text: CharSequence?, start: Int, count: Int, after: Int) -> Unit)? = null,
    onMaskedTextChanged: ((text: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? = null,
    afterMaskedTextChanged: ((text: Editable?) -> Unit)? = null
) = maskedWatcher?.apply {
    addTextChangedListener(beforeMaskedTextChanged, onMaskedTextChanged, afterMaskedTextChanged)
}