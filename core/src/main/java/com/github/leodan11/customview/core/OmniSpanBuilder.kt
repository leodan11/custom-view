package com.github.leodan11.customview.core

import android.content.Context
import android.text.SpannableStringBuilder
import androidx.annotation.StringRes
import com.github.leodan11.customview.core.model.Span

/**
 * Builder DSL for creating richly formatted [SpannableStringBuilder] content.
 *
 * This class provides a domain-specific language (DSL) to construct a spannable string
 * with multiple styled spans. It supports adding text with styling, nested spans,
 * and special operators for a clean and expressive syntax.
 *
 * ### Usage example:
 * ```kotlin
 * val spannable = OmniSpanBuilder(SpannableStringBuilder()).apply {
 *     +"Hello, "
 *     "World!" {
 *         bold()
 *         foreground(Color.RED)
 *     }
 *     newLine()
 *     text("This is a new line with underline") {
 *         underline()
 *     }
 * }.build()
 * ```
 *
 * @property spannableStringBuilder The [SpannableStringBuilder] instance to which spans are applied.
 */
class OmniSpanBuilder(private val spannableStringBuilder: SpannableStringBuilder) {

    private val spans = mutableListOf<Span>()

    /**
     * Initializes a new [Span] with the given text and applies the provided styling block.
     *
     * @param text The text content for the span.
     * @param init A lambda to apply span styles.
     * @return The configured [Span] instance.
     */
    private fun initSpan(
        text: CharSequence,
        init: Span.() -> Unit
    ): Span {
        return Span(text).apply {
            spans += this
            init()
        }
    }

    /**
     * Appends the given string as a new span using the unary plus operator.
     *
     * Example:
     * ```kotlin
     * +"Sample text"
     * ```
     *
     * @receiver The string to append.
     */
    operator fun String.unaryPlus() = text(this)

    /**
     * Invokes a span block on the string, allowing nested styles to be applied.
     *
     * Example:
     * ```kotlin
     * "Styled text" {
     *     bold()
     *     italic()
     * }
     * ```
     *
     * @param init A lambda to apply span styles.
     * @return The configured [Span] instance.
     */
    operator fun String.invoke(
        init: Span.() -> Unit = {}
    ): Span = initSpan(this, init)

    /**
     * Adds a new span with the given text and applies styles from the [init] block.
     *
     * @param text The text content for the span.
     * @param init A lambda to apply span styles.
     * @return The configured [Span] instance.
     */
    fun text(
        text: CharSequence,
        init: Span.() -> Unit = {}
    ): Span = initSpan(text, init)

    /**
     * Adds a new span with the text retrieved from string resources and applies styles.
     *
     * @param context The [Context] used to access resources.
     * @param textResId The string resource ID.
     * @param init A lambda to apply span styles.
     * @return The configured [Span] instance.
     */
    fun text(
        context: Context,
        @StringRes textResId: Int,
        init: Span.() -> Unit = {}
    ): Span = initSpan(context.getText(textResId), init)

    /**
     * Inserts a newline character as a new span.
     */
    fun newLine() { +"\n" }

    /**
     * Builds and applies all configured spans to the internal [SpannableStringBuilder].
     *
     * @return The fully styled [SpannableStringBuilder].
     */
    fun build() : SpannableStringBuilder {
        spans.forEach { span ->
            span.build(spannableStringBuilder)
        }
        return spannableStringBuilder
    }

}
