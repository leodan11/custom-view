package com.github.leodan11.customview.core.helper

import android.text.Editable
import android.text.SpannableStringBuilder
import com.github.leodan11.customview.core.OmniSpanBuilder

/**
 *  Gives a `OmniSpanBuilder` context to attach spans.
 *  How to use:
 *  ```
 *  // Making text
 *  val span = createOmniSpan {
 *      +"This is an example using unary plus operator"
 *
 *      "This is an example using a string invoke" {
 *          ...
 *      }
 *
 *      text("This is an example using text function") {
 *          ...
 *      }
 *  }
 *  binding.tvSpannable.apply {
 *      movementMethod = LinkMovementMethod.getInstance()
 *      text = span
 *  }
 *  ```
 */
fun createOmniSpan(
    spannableStringBuilder: SpannableStringBuilder = SpannableStringBuilder(),
    builder: OmniSpanBuilder.() -> Unit
): SpannableStringBuilder {
    return OmniSpanBuilder(spannableStringBuilder).apply(builder).build()
}


/**
 * Casts this [Editable] instance to a [SpannableStringBuilder].
 *
 * This extension function provides a convenient way to treat an [Editable] as a
 * [SpannableStringBuilder], enabling the use of spannable text manipulation APIs.
 *
 * ### Example:
 * ```kotlin
 * val editable: Editable = ...
 * val spannableBuilder: SpannableStringBuilder = editable.asSpannableStringBuilder()
 * spannableBuilder.append(" Additional styled text")
 * ```
 *
 * @receiver The [Editable] instance to be cast.
 * @return The same instance cast as a [SpannableStringBuilder].
 * @throws ClassCastException if the receiver is not a [SpannableStringBuilder].
 */
fun Editable.asSpannableStringBuilder() = this as SpannableStringBuilder