package com.leodan11.myapplication

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.github.leodan11.customview.core.ReadMoreOption
import com.github.leodan11.customview.core.utils.Converters.dipToPixels
import com.github.leodan11.customview.drawable.MaterialBadgeDrawable
import com.github.leodan11.customview.drawable.TextDrawable
import com.leodan11.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        val more = ReadMoreOption.Builder(this@MainActivity)
            .textLength(3)
            .labelUnderLine(true)
            .textLengthType(ReadMoreOption.TYPE_LINE)
            .expandAnimation(true)
            .build()

        with(binding) {

            buttonViewExample.setOnClickListener {
                viewExample.root.isVisible = true
                viewExampleSnowfall.root.isVisible = false
                viewExampleSignature.root.isVisible = false
            }

            buttonViewExampleSignature.setOnClickListener {
                viewExample.root.isVisible = false
                viewExampleSnowfall.root.isVisible = false
                viewExampleSignature.root.isVisible = true
            }

            buttonViewExampleAndroidSnowfall.setOnClickListener {
                viewExample.root.isVisible = false
                viewExampleSnowfall.root.isVisible = true
                viewExampleSignature.root.isVisible = false
                more.addReadMoreTo(binding.viewExampleSnowfall.textviewFirst, R.string.text_value_temp)
            }

            speedDial.inflate(R.menu.menu_speed_dial)
            speedDial.setOnActionSelectedListener {
                Toast.makeText(this@MainActivity, "Selected", Toast.LENGTH_SHORT).show()
                true
            }

        }

        with(binding.viewExample) {
            badgeTextView.text = list.first().toSpannable()
            badgeTextView.setOnClickListener {
                badgeTextView.text = list.random().toSpannable()
            }
        }
        with(binding.viewExampleSignature) {

            action.setOnClickListener {
                if (value.isBitmapEmpty) {
                    Toast.makeText(applicationContext, "Empty", Toast.LENGTH_LONG).show()
                } else {
                    view.setImageBitmap(value.signatureBitmap)
                }
            }

            actionTwo.setOnClickListener {
                value.clearCanvas()
                view.setImageResource(R.drawable.ic_launcher_background)
            }

        }
        with(binding.viewExampleSnowfall) {
            var temp = true
            textviewFirst.setOnClickListener {
                if (temp) example.stopFalling() else example.restartFalling()
                temp = !temp
            }
            expandableCardview.innerView?.let {
                val drawable = TextDrawable
                    .builder()
                    .buildRound("EX", ContextCompat.getColor(this@MainActivity, R.color.purple_500))
                it.findViewById<ImageView>(R.id.shapeableImageView).setImageDrawable(drawable)
            }
        }

    }
}