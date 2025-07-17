package com.github.leodan11.customview.core.base

import android.text.Spannable
import com.github.leodan11.customview.core.model.Span

/**
 * Class that represents a Decorator to be applied on the span.
 *
 * @property what The what parameter refers to the span to apply to the text
 * @property fillOnlyOnePosition Indicates if the span should fill only one position.
 * Used to replace an space " " with a `Span.image`.
 * @property start indicate the start portion of the text to which to apply the span.
 * if start == -1, means that the start position will be the first character of the text.
 * Otherwise, the start position will be applied with the current value.
 * @property end indicate the end portion of the text to which to apply the span.
 * if end == -1, means that the end position will be the last character of the text.
 * Otherwise, the end position will be applied with the current value.
 * @property flags determines whether the span should expand to include the inserted text.
 *
 * @see Span
 */
open class Decorator(
    val what: Any,
    val fillOnlyOnePosition: Boolean = false,
    var start: Int = -1,
    var end: Int = -1,
    var flags: Int = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
)