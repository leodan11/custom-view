package com.leodan11.myapplication

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.leodan11.customview.core.DisplayMetrics.dipToPixels
import com.github.leodan11.customview.drawable.MaterialBadgeDrawable

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView: TextView = findViewById(R.id.badgeTextView)

        val list = listOf(
            MaterialBadgeDrawable.Builder()
                .type(MaterialBadgeDrawable.TYPE_NUMBER)
                .number(56)
                .build(),
            MaterialBadgeDrawable.Builder()
                .type(MaterialBadgeDrawable.TYPE_ONLY_ONE_TEXT)
                .badgeColor(Color.LTGRAY)
                .textOne("One")
                .build(),
            MaterialBadgeDrawable.Builder()
                .type(MaterialBadgeDrawable.TYPE_WITH_TWO_TEXT)
                .badgeColor(Color.MAGENTA)
                .textOne("TEST")
                .textTwo("Pass")
                .build(),
            MaterialBadgeDrawable.Builder()
                .type(MaterialBadgeDrawable.TYPE_WITH_TWO_TEXT_COMPLEMENTARY)
                .textOne("LEVEL")
                .padding(dipToPixels(2f))
                .strokeWidth(dipToPixels(1f).toInt())
                .textTwo("10")
                .build(),
        )

        textView.text = list.first().toSpannable()

        textView.setOnClickListener {

            textView.text = list.random().toSpannable()

        }

    }
}