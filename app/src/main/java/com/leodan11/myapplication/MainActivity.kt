package com.leodan11.myapplication

import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.leodan11.customview.core.ReadMoreOption
import com.github.leodan11.customview.core.RecyclerViewSwipeDecorator
import com.github.leodan11.customview.core.Splashy
import com.github.leodan11.customview.core.ToastKit
import com.github.leodan11.customview.core.Toasty
import com.github.leodan11.customview.core.helper.Converters.dipToPixels
import com.github.leodan11.customview.drawable.MaterialBadgeDrawable
import com.github.leodan11.customview.drawable.TextDrawable
import com.github.leodan11.customview.textfield.MaterialSpinner
import com.github.leodan11.customview.widget.pin.model.PinListener
import com.github.leodan11.customview.widget.swipeablerv.SwipeLeftRightCallback
import com.google.android.material.snackbar.Snackbar
import com.leodan11.myapplication.databinding.ActivityMainBinding
import java.util.UUID
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onCreateSplashy()

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

        val adapter = CustomAdapter()

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

            containerList2.adapter = adapter
            containerList2.setListener(object : SwipeLeftRightCallback.Listener {
                override fun onSwipedLeft(position: Int) {
                    Snackbar.make(root, "Item: $position -> moved LEFT", Snackbar.LENGTH_LONG).show()
                    adapter.notifyItemChanged(position)
                }

                override fun onSwipedRight(position: Int) {
                    Snackbar.make(root, "Item: $position -> moved RIGHT", Snackbar.LENGTH_LONG).show()
                    adapter.notifyItemChanged(position)
                }

            })
        }
        with(binding.viewExampleSignature) {

            action.setOnClickListener {
                value.isBitmapEmpty {
                    if (it) {
                        Toast.makeText(applicationContext, "Empty", Toast.LENGTH_LONG).show()
                    } else {
                        view.setImageBitmap(value.signatureBitmap)
                    }
                }
            }

            numberPicker.setListener {
                Toast.makeText(this@MainActivity, "Number Picker [$it]", Toast.LENGTH_SHORT).show()
            }

            actionTwo.setOnClickListener {
                value.clearCanvas()
                view.setImageResource(R.drawable.ic_launcher_background)
            }


            containerList.adapter = adapter
            val listData = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
            adapter.updateFlowerCount(listData)
            val callback: ItemTouchHelper.Callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position: Int = viewHolder.bindingAdapterPosition
                    val moved: String = if (direction == ItemTouchHelper.RIGHT) "RIGHT" else "LEFT"
                    Snackbar.make(viewHolder.itemView, "Item: $position -> moved $moved", Snackbar.LENGTH_LONG).show()
                    adapter.notifyItemChanged(position)
                }

                override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                    RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(getColor(R.color.purple_200))
                        .addSwipeLeftLabel("Example 1")
                        .addSwipeLeftActionIcon(R.drawable.baseline_ac_unit)
                        .setSwipeLeftActionIconTint(getColor(R.color.white))
                        .addSwipeRightBackgroundColor(getColor(R.color.teal_200))
                        .addSwipeRightLabel("Example 2")
                        .addCornerRadius(TypedValue.COMPLEX_UNIT_DIP, 16)
                        .addSwipeRightActionIcon(R.drawable.baseline_add)
                        .setSwipeRightLabelColor(getColor(R.color.white))
                        .create()
                        .decorate()
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }

            }
            val itemTouchHelper = ItemTouchHelper(callback)
            itemTouchHelper.attachToRecyclerView(containerList)
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

    private fun onCreateSplashy() {
        Splashy(this)
            .setTitle("Splashy")
            .setSubTitle("Splash screen made easy")
            .setFullScreen(true)
            .show()
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