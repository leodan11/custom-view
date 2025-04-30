package com.leodan11.myapplication

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.github.leodan11.customview.core.ReadMoreOption
import com.github.leodan11.customview.core.ToastKit
import com.github.leodan11.customview.core.Toasty
import com.github.leodan11.customview.core.utils.Converters.dipToPixels
import com.github.leodan11.customview.drawable.MaterialBadgeDrawable
import com.github.leodan11.customview.drawable.TextDrawable
import com.github.leodan11.customview.widget.MaterialSpinner
import com.github.leodan11.customview.widget.helpers.SwipeListener
import com.github.leodan11.customview.widget.helpers.makeLeftRightSwipeAble
import com.github.leodan11.customview.widget.pin.model.PinListener
import com.leodan11.myapplication.databinding.ActivityMainBinding
import java.util.UUID
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val more = ReadMoreOption.Builder(this@MainActivity)
            .textLength(3)
            .labelUnderLine(true)
            .onClickMoreListener {
                Toasty.info(this@MainActivity, "More", Toast.LENGTH_SHORT).show()
            }
            .textLengthType(ReadMoreOption.TYPE_LINE)
            .onClickLessListener {
                Toasty.success(this@MainActivity, "Less", Toast.LENGTH_SHORT).show()
            }
            .expandAnimation(true)
            .build()

        with(binding) {

            buttonViewExample.setOnClickListener {
                viewExample.root.isVisible = true
                viewExampleSnowfall.root.isVisible = false
                viewExampleSignature.root.isVisible = false
                ToastKit.createToast(
                    this@MainActivity,
                    message = getString(R.string.app_name),
                    style = ToastKit.Style.INFO
                )
            }

            buttonViewExampleSignature.setOnClickListener {
                viewExample.root.isVisible = false
                viewExampleSnowfall.root.isVisible = false
                viewExampleSignature.root.isVisible = true
                ToastKit.createColorToast(
                    this@MainActivity,
                    message = getString(R.string.app_name),
                    style = ToastKit.Style.INFO
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
            badgeTextView.text = generateRandomBadgeDrawable().toSpannable()
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
                Toast.makeText(
                    this@MainActivity,
                    "Badge Clicked - OTP: ${otpView.text}",
                    Toast.LENGTH_SHORT
                )
                    .show()
                badgeTextView.text = generateRandomBadgeDrawable().toSpannable()
                switchIconView.switchState()
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

            numberPicker.setListener {
                Toast.makeText(this@MainActivity, "Number Picker [$it]", Toast.LENGTH_SHORT).show()
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
                        Toasty.normal(this@MainActivity, "Archived $position", Toast.LENGTH_SHORT)
                            .show()
                        adapter.notifyItemRangeChanged(position, adapter.itemCount)
                    }

                    override fun onSwipedRight(position: Int) {
                        Toasty.warning(this@MainActivity, "Deleted $position", Toast.LENGTH_SHORT)
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
                    .buildRound(
                        generateRandomString(2),
                        ContextCompat.getColor(this@MainActivity, R.color.purple_500)
                    )
                it.findViewById<ImageView>(R.id.shapeableImageView).setImageDrawable(drawable)
                val spinner = it.findViewById<MaterialSpinner>(R.id.material_spinner)
                val listItems = arrayOf("USA", "Japan", "India")
                spinner.onItemSelectedListener = object : MaterialSpinner.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: MaterialSpinner,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        parent.focusSearch(View.FOCUS_UP)?.requestFocus()
                    }

                    override fun onNothingSelected(parent: MaterialSpinner) {
                        Log.v("MaterialSpinner", "onNothingSelected parent=${parent.id}")
                    }


                }
                val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, listItems)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
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

    private fun generateRandomString(length: Int = 10): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

    private fun generateRandomBadgeDrawable(): MaterialBadgeDrawable {
        val index = (0..3).random()
        return when (index) {
            0 -> {
                MaterialBadgeDrawable.Builder()
                    .type(MaterialBadgeDrawable.TYPE_NUMBER)
                    .number((0..9999).random())
                    .build()
            }

            1 -> {
                MaterialBadgeDrawable.Builder()
                    .type(MaterialBadgeDrawable.TYPE_ONLY_ONE_TEXT)
                    .badgeColor(Color.LTGRAY)
                    .textOne(" ${generateRandomString()}")
                    .build()
            }

            2 -> {
                MaterialBadgeDrawable.Builder()
                    .type(MaterialBadgeDrawable.TYPE_WITH_TWO_TEXT)
                    .badgeColor(Color.MAGENTA)
                    .textOne(" UUID ")
                    .textTwo(" ${UUID.randomUUID()} ")
                    .build()
            }

            else -> {
                MaterialBadgeDrawable.Builder()
                    .type(MaterialBadgeDrawable.TYPE_WITH_TWO_TEXT_COMPLEMENTARY)
                    .textOne(" LEVEL ")
                    .padding(dipToPixels(2f))
                    .strokeWidth(dipToPixels(1f).toInt())
                    .textTwo(" ${Random.nextInt()} ")
                    .build()
            }
        }
    }

}