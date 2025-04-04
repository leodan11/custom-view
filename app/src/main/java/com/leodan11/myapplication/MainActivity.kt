package com.leodan11.myapplication

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.github.leodan11.customview.core.ReadMoreOption
import com.github.leodan11.customview.core.ToastFlasher
import com.github.leodan11.customview.core.utils.Converters.dipToPixels
import com.github.leodan11.customview.drawable.MaterialBadgeDrawable
import com.github.leodan11.customview.drawable.TextDrawable
import com.github.leodan11.customview.widget.helpers.SwipeListener
import com.github.leodan11.customview.widget.helpers.makeLeftRightSwipeAble
import com.github.leodan11.customview.widget.pin.model.PinListener
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
            .onClickMoreListener {
                Toast.makeText(this@MainActivity, "More", Toast.LENGTH_SHORT).show()
            }
            .textLengthType(ReadMoreOption.TYPE_LINE)
            .onClickLessListener {
                Toast.makeText(this@MainActivity, "Less", Toast.LENGTH_SHORT).show()
            }
            .expandAnimation(true)
            .build()

        with(binding) {

            buttonViewExample.setOnClickListener {
                viewExample.root.isVisible = true
                viewExampleSnowfall.root.isVisible = false
                viewExampleSignature.root.isVisible = false
                ToastFlasher.createToast(
                    this@MainActivity,
                    message = getString(R.string.app_name),
                    style = ToastFlasher.Style.INFO
                )
            }

            buttonViewExampleSignature.setOnClickListener {
                viewExample.root.isVisible = false
                viewExampleSnowfall.root.isVisible = false
                viewExampleSignature.root.isVisible = true
                ToastFlasher.createColorToast(
                    this@MainActivity,
                    message = getString(R.string.app_name),
                    style = ToastFlasher.Style.INFO
                )
            }

            buttonViewExampleAndroidSnowfall.setOnClickListener {
                viewExample.root.isVisible = false
                viewExampleSnowfall.root.isVisible = true
                viewExampleSignature.root.isVisible = false
                more.addReadMoreTo(
                    binding.viewExampleSnowfall.textviewFirst,
                    R.string.text_value_temp
                )
            }

            speedDial.inflate(R.menu.menu_speed_dial)
            speedDial.setOnActionSelectedListener {
                Toast.makeText(this@MainActivity, "Selected", Toast.LENGTH_SHORT).show()
                true
            }

        }

        with(binding.viewExample) {
            badgeTextView.text = list.first().toSpannable()
            otpView.setOnTextChangedListener(object : PinListener {

                override fun onTextChangedListener(text: String?) {
                    Toast.makeText(
                        this@MainActivity,
                        "OTP changed - $text",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPinCompletedListener(text: String) {
                    Toast.makeText(
                        this@MainActivity,
                        "OTP Completed - $text",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
            badgeTextView.setOnClickListener {
                Toast.makeText(this@MainActivity, "Badge Clicked - OTP: ${otpView.text}", Toast.LENGTH_SHORT)
                    .show()
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

            val adapter = CustomAdapter()
            containerList.adapter = adapter
            val listData = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
            adapter.updateFlowerCount(listData)
            containerList.makeLeftRightSwipeAble(this@MainActivity)
                .setListener(object : SwipeListener {

                    override fun onSwipedLeft(position: Int) {
                        Toast.makeText(this@MainActivity, "Archived $position", Toast.LENGTH_SHORT)
                            .show()
                        adapter.notifyItemRangeChanged(position, adapter.itemCount)
                    }

                    override fun onSwipedRight(position: Int) {
                        Toast.makeText(this@MainActivity, "Deleted $position", Toast.LENGTH_SHORT)
                            .show()
                        adapter.notifyItemRangeChanged(position, adapter.itemCount)
                    }

                })
                .createSwipeAble()

        }
        with(binding.viewExampleSnowfall) {
            var temp = true
            var animationView = true
            buttonFadeOutAction.setOnClickListener {
                if (animationView) {
                    fadeoutParticleLayout.startAnimation()
                    animationView = false
                } else {
                    fadeoutParticleLayout.reset()
                    animationView = true
                }
            }
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
            gradientTextView.apply {
                addGradientToFullText(
                    startColorHex = "#FF0000",
                    endColorHex = "#00FF00"
                )
            }
            gradientCheckBox.apply {
                addGradientToFullText(
                    startColorHex = "#FF0000",
                    endColorHex = "#00FF00"
                )
            }
        }

    }
}