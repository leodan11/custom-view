package com.github.leodan11.customview.widget.helpers

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.util.Base64
import androidx.recyclerview.widget.RecyclerView
import com.github.leodan11.customview.widget.RecyclerViewSwipe
import java.io.ByteArrayOutputStream


/**
 * Converts the [Bitmap] to a Base64-encoded string in PNG format.
 *
 * ```kotlin
 * val base64 = bitmap.toBase64()
 * ```
 *
 * @param quality Compression quality (0–100), where 100 is best quality.
 * @param flags Optional [Base64] flags (default: [Base64.NO_WRAP]).
 * @return A Base64 string representing the image.
 */
fun Bitmap.toBase64(quality: Int = 100, flags: Int = Base64.NO_WRAP): String {
    val outputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, quality, outputStream)
    val byteArray = outputStream.toByteArray()
    return Base64.encodeToString(byteArray, flags)
}


/**
 *
 * Create a [RecyclerViewSwipe] object
 *
 * @receiver [RecyclerView]
 *
 * @param context [Context] current context
 * @param attrs [AttributeSet]? attributes. Default is `null`
 *
 * @return [RecyclerViewSwipe]
 *
 */
fun RecyclerView.makeLeftRightSwipeAble(context: Context, attrs: AttributeSet? = null): RecyclerViewSwipe {
    return RecyclerViewSwipe(context, attrs, this)
}