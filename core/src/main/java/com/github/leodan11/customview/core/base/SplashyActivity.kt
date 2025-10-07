@file:Suppress("DEPRECATION")

package com.github.leodan11.customview.core.base

import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import com.github.leodan11.customview.core.R
import com.github.leodan11.customview.core.Splashy
import com.github.leodan11.customview.core.databinding.MActivitySplashyBinding

internal class SplashyActivity : AppCompatActivity() {

    private lateinit var binding: MActivitySplashyBinding
    private var isIntermediate: Boolean = false
    private var progressVisible = false
    private var time: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashyTheme)
        super.onCreate(savedInstanceState)

        binding = MActivitySplashyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this

        setLogo()

        setTitle()

        setSubTitle()

        setProgress()

        setSplashBackground()

        setFullScreen()

        setClickToHide()

        setAnimation()

        setTime()

        if (!isIntermediate) showSplashy()

    }

    private fun setLogo() {
        val applicationInfo: ApplicationInfo = applicationInfo

        if (intent.hasExtra(SHOW_LOGO)) {
            if (!intent.getBooleanExtra(SHOW_LOGO, true))
                binding.ivLogo.visibility = View.GONE
        }


        if (intent.hasExtra(LOGO)) {
            binding.ivLogo.setImageResource(intent.getIntExtra(LOGO, applicationInfo.icon))
        } else {
            binding.ivLogo.setImageResource(applicationInfo.icon)

        }

        if (intent.hasExtra(LOGO_WIDTH) || intent.hasExtra(LOGO_HEIGHT)) {
            val width = intent.getIntExtra(LOGO_WIDTH, 200)
            val height = intent.getIntExtra(LOGO_HEIGHT, 200)
            val widthInDp = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                width.toFloat(),
                resources.displayMetrics
            ).toInt()
            val heightInDp = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                height.toFloat(),
                resources.displayMetrics
            ).toInt()
            binding.ivLogo.layoutParams.width = widthInDp
            binding.ivLogo.layoutParams.height = heightInDp
            binding.ivLogo.requestLayout()


        }

        if (intent.hasExtra(LOGO_SCALE_TYPE)) {
            val scaleType: ImageView.ScaleType =
                intent.getSerializableExtra(LOGO_SCALE_TYPE) as ImageView.ScaleType
            binding.ivLogo.scaleType = scaleType
            binding.ivLogo.requestLayout()
        }
    }

    private fun setTitle() {
        val applicationInfo: ApplicationInfo = applicationInfo
        if (intent.hasExtra(SHOW_TITLE)) {
            if (!intent.getBooleanExtra(SHOW_TITLE, true))
                binding.tvTitle.visibility = View.GONE
        }
        when {
            intent.hasExtra(TITLE) -> {
                binding.tvTitle.text = intent.getStringExtra(TITLE)
            }

            intent.hasExtra(TITLE_RESOURCE) -> {
                binding.tvTitle.text = resources.getString(
                    intent.getIntExtra(
                        TITLE_RESOURCE,
                        applicationInfo.labelRes
                    )
                )
            }

            else -> {
                binding.tvTitle.text = resources.getString(applicationInfo.labelRes)
            }
        }
        if (intent.hasExtra(TITLE_SIZE)) {
            binding.tvTitle.textSize = intent.getFloatExtra(TITLE_SIZE, 40F)
        }
        if (intent.hasExtra(TITLE_COLOR)) {
            binding.tvTitle.setTextColor(
                ContextCompat.getColor(
                    this,
                    intent.getIntExtra(
                        TITLE_COLOR,
                        this.customColorResource(com.google.android.material.R.attr.colorOnSurface)
                    )
                )
            )
        }
        if (intent.hasExtra(TITLE_COLOR_VALUE)) {
            intent.getStringExtra(TITLE_COLOR_VALUE)?.let {
                binding.tvTitle.setTextColor(it.toColorInt())
            }
        }
        if (intent.hasExtra(TITLE_FONT_STYLE)) {
            intent.getStringExtra(TITLE_FONT_STYLE)?.let {
                val typeface =
                    Typeface.createFromAsset(assets, intent.getStringExtra(TITLE_FONT_STYLE))
                binding.tvTitle.typeface = typeface
            }
        }
    }

    private fun setSubTitle() {

        if (intent.hasExtra(SUBTITLE)) {
            binding.tvSubTitle.text = String.format("%s ", intent.getStringExtra(SUBTITLE))
            binding.tvSubTitle.visibility = View.VISIBLE
        }
        if (intent.hasExtra(SUBTITLE_RESOURCE)) {
            binding.tvSubTitle.text =
                resources.getString(intent.getIntExtra(SUBTITLE_RESOURCE, R.string.text_info))
            binding.tvSubTitle.visibility = View.VISIBLE
        }
        if (intent.hasExtra(SUBTITLE_SIZE)) {
            binding.tvSubTitle.textSize = intent.getFloatExtra(SUBTITLE_SIZE, 18F)
        }
        if (intent.hasExtra(SUBTITLE_COLOR)) {
            binding.tvSubTitle.setTextColor(
                ContextCompat.getColor(
                    this,
                    intent.getIntExtra(
                        SUBTITLE_COLOR,
                        this.customColorResource(com.google.android.material.R.attr.colorOutline)
                    )
                )
            )
        }
        if (intent.hasExtra(SUBTITLE_COLOR_VALUE)) {
            intent.getStringExtra(SUBTITLE_COLOR_VALUE)?.let {
                binding.tvSubTitle.setTextColor(it.toColorInt())
            }
        }
        if (intent.hasExtra(SUBTITLE_ITALIC)) {
            if (!intent.getBooleanExtra(SUBTITLE_ITALIC, true))
                binding.tvSubTitle.setTypeface(null, Typeface.NORMAL)
        }
        if (intent.hasExtra(SUBTITLE_FONT_STYLE)) {
            intent.getStringExtra(SUBTITLE_FONT_STYLE)?.let {
                val typeface =
                    Typeface.createFromAsset(assets, intent.getStringExtra(SUBTITLE_FONT_STYLE))
                binding.tvSubTitle.typeface = typeface
            }
        }
    }

    private fun setProgress() {
        if (intent.hasExtra(SHOW_PROGRESS)) {
            progressVisible = intent.getBooleanExtra(SHOW_PROGRESS, false)
            if (progressVisible) binding.pbLoad.visibility = View.VISIBLE
        }

        if (intent.hasExtra(PROGRESS_COLOR)) {
            val color = intent.getIntExtra(
                PROGRESS_COLOR,
                this.customColorResource(androidx.appcompat.R.attr.colorPrimary)
            )
            binding.pbLoad.indeterminateDrawable?.setColorFilter(
                ContextCompat.getColor(
                    this,
                    color
                ), PorterDuff.Mode.SRC_IN
            )
        }
        if (intent.hasExtra(PROGRESS_COLOR_VALUE)) {
            intent.getStringExtra(PROGRESS_COLOR_VALUE)?.let {
                binding.pbLoad.indeterminateDrawable?.setColorFilter(
                    it.toColorInt(),
                    PorterDuff.Mode.SRC_IN
                )
            }
        }
    }

    private fun setSplashBackground() {
        if (!intent.hasExtra(BACKGROUND_COLOR) && !intent.hasExtra(BACKGROUND_COLOR_VALUE) && !intent.hasExtra(
                BACKGROUND_RESOURCE
            )
        ) {
            binding.ivBackground.isVisible = false
        }
        if (intent.hasExtra(BACKGROUND_COLOR)) {
            binding.ivBackground.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    intent.getIntExtra(
                        BACKGROUND_COLOR,
                        this.customColorResource(com.google.android.material.R.attr.backgroundColor)
                    )
                )
            )
            binding.ivBackground.isVisible = true
        }
        if (intent.hasExtra(BACKGROUND_COLOR_VALUE)) {
            intent.getStringExtra(BACKGROUND_COLOR_VALUE)?.let {
                binding.ivBackground.setBackgroundColor(it.toColorInt())
                binding.ivBackground.isVisible = true
            }
        }
        if (intent.hasExtra(BACKGROUND_RESOURCE)) {
            binding.ivBackground.setBackgroundResource(
                intent.getIntExtra(
                    BACKGROUND_RESOURCE,
                    R.color.normal_color
                )
            )
            binding.ivBackground.isVisible = true
        }
    }

    private fun setFullScreen() {

        if (intent.hasExtra(FULL_SCREEN)) {

            if (intent.getBooleanExtra(FULL_SCREEN, false))
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

    }

    private fun setClickToHide() {

        if (intent.getBooleanExtra(CLICK_TO_HIDE, false)) {
            binding.rlMain.setOnClickListener {
                finish()
            }
        }
    }


    private fun setAnimation() {
        if (intent.hasExtra(ANIMATION_TYPE)) {
            val duration = intent.getLongExtra(ANIMATION_DURATION, 800)
            when (intent.getSerializableExtra(ANIMATION_TYPE)) {

                Splashy.Animation.SLIDE_IN_TOP_BOTTOM -> {
                    binding.pbLoad.visibility = View.GONE

                    binding.ivLogo.animation =
                        AnimationUtils.loadAnimation(this, R.anim.slide_from_top)
                    binding.tvTitle.animation =
                        AnimationUtils.loadAnimation(this, R.anim.slide_from_bottom)
                    binding.tvSubTitle.animation =
                        AnimationUtils.loadAnimation(this, R.anim.slide_from_bottom)

                    binding.ivLogo.animation.duration = duration
                    binding.tvTitle.animation.duration = duration
                    binding.tvSubTitle.animation.duration = duration

                    binding.ivLogo.animation.setAnimationListener(object :
                        Animation.AnimationListener {
                        override fun onAnimationRepeat(animation: Animation?) {}

                        override fun onAnimationStart(animation: Animation?) {}

                        override fun onAnimationEnd(animation: Animation?) {
                            if (progressVisible) binding.pbLoad.visibility = View.VISIBLE
                        }

                    })
                }

                Splashy.Animation.SLIDE_IN_LEFT_BOTTOM -> {
                    binding.pbLoad.visibility = View.GONE

                    binding.ivLogo.animation =
                        AnimationUtils.loadAnimation(this, R.anim.slide_from_left)
                    binding.tvTitle.animation =
                        AnimationUtils.loadAnimation(this, R.anim.slide_from_bottom)
                    binding.tvSubTitle.animation =
                        AnimationUtils.loadAnimation(this, R.anim.slide_from_bottom)

                    binding.ivLogo.animation.duration = duration
                    binding.tvTitle.animation.duration = duration
                    binding.tvSubTitle.animation.duration = duration

                    binding.ivLogo.animation.setAnimationListener(object :
                        Animation.AnimationListener {
                        override fun onAnimationRepeat(animation: Animation?) {}

                        override fun onAnimationStart(animation: Animation?) {}

                        override fun onAnimationEnd(animation: Animation?) {
                            if (progressVisible) binding.pbLoad.visibility = View.VISIBLE
                        }

                    })
                }

                Splashy.Animation.SLIDE_IN_LEFT_RIGHT -> {
                    binding.pbLoad.visibility = View.GONE

                    binding.ivLogo.animation =
                        AnimationUtils.loadAnimation(this, R.anim.slide_from_left)
                    binding.tvTitle.animation =
                        AnimationUtils.loadAnimation(this, R.anim.slide_from_right)
                    binding.tvSubTitle.animation =
                        AnimationUtils.loadAnimation(this, R.anim.slide_from_right)

                    binding.ivLogo.animation.duration = duration
                    binding.tvTitle.animation.duration = duration
                    binding.tvSubTitle.animation.duration = duration

                    binding.ivLogo.animation.setAnimationListener(object :
                        Animation.AnimationListener {
                        override fun onAnimationRepeat(animation: Animation?) {}

                        override fun onAnimationStart(animation: Animation?) {}

                        override fun onAnimationEnd(animation: Animation?) {
                            if (progressVisible) binding.pbLoad.visibility = View.VISIBLE
                        }

                    })
                }

                Splashy.Animation.SLIDE_LEFT_ENTER -> {
                    binding.pbLoad.visibility = View.GONE
                    binding.tvTitle.visibility = View.INVISIBLE
                    binding.tvSubTitle.visibility = View.INVISIBLE

                    binding.ivLogo.animation =
                        AnimationUtils.loadAnimation(this, R.anim.slide_from_left)

                    binding.ivLogo.animation.duration = duration


                    binding.ivLogo.animation.setAnimationListener(object :
                        Animation.AnimationListener {
                        override fun onAnimationRepeat(animation: Animation?) {}

                        override fun onAnimationStart(animation: Animation?) {}

                        override fun onAnimationEnd(animation: Animation?) {
                            if (progressVisible) binding.pbLoad.visibility = View.VISIBLE
                            binding.tvTitle.visibility = View.VISIBLE
                            binding.tvSubTitle.visibility = View.VISIBLE
                            binding.tvTitle.animation = AnimationUtils.loadAnimation(
                                this@SplashyActivity,
                                R.anim.slide_from_logo
                            )
                            binding.tvSubTitle.animation = AnimationUtils.loadAnimation(
                                this@SplashyActivity,
                                R.anim.slide_from_logo
                            )
                            binding.tvTitle.animation.duration = duration
                            binding.tvSubTitle.animation.duration = duration
                            binding.tvTitle.animation.fillAfter = false
                        }

                    })
                }

                Splashy.Animation.GLOW_LOGO -> {
                    val blinkAnimation =
                        AlphaAnimation(1f, 0f) // Change alpha from fully visible to invisible
                    blinkAnimation.duration = duration // duration - half a second
                    blinkAnimation.interpolator =
                        LinearInterpolator() // do not alter animation rate
                    blinkAnimation.repeatCount = -1 // Repeat animation infinitely
                    blinkAnimation.repeatMode = Animation.REVERSE
                    binding.ivLogo.animation = blinkAnimation
                }

                Splashy.Animation.GLOW_LOGO_TITLE -> {
                    val blinkAnimation =
                        AlphaAnimation(1f, 0f) // Change alpha from fully visible to invisible
                    blinkAnimation.duration = duration // duration - half a second
                    blinkAnimation.interpolator =
                        LinearInterpolator() // do not alter animation rate
                    blinkAnimation.repeatCount = -1 // Repeat animation infinitely
                    blinkAnimation.repeatMode = Animation.REVERSE
                    binding.ivLogo.animation = blinkAnimation
                    binding.tvTitle.animation = blinkAnimation
                }

                Splashy.Animation.GROW_LOGO_FROM_CENTER -> {
                    val fadeIn = ScaleAnimation(
                        0f,
                        1f,
                        0f,
                        1f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f
                    )
                    fadeIn.duration = duration // animation duration in milliseconds

                    fadeIn.fillAfter =
                        true // If fillAfter is true, the transformation that this animation performed will persist when it is finished.

                    binding.ivLogo.animation = fadeIn
                }
            }
        }
    }

    private fun setTime() {
        if (intent.hasExtra(TIME)) {
            time = intent.getLongExtra(TIME, time)
        }
        if (intent.hasExtra(INFINITE_TIME)) {
            isIntermediate = intent.getBooleanExtra(INFINITE_TIME, false)
        }
    }


    internal fun setOnComplete(getComplete: Splashy.OnComplete) {
        onComplete = getComplete

    }

    internal fun Context.customColorResource(@AttrRes idAttrRes: Int): Int {
        val typedValue = TypedValue()
        this.theme.resolveAttribute(idAttrRes, typedValue, true)
        return typedValue.data
    }

    private fun showSplashy() {
        Handler(mainLooper).postDelayed({
            if (onComplete != null) {
                onComplete?.onComplete()
            }
            finish()
        }, time)
    }

    companion object {

        // Splash Screen Time
        internal const val TIME = "time"
        internal const val INFINITE_TIME = "intermediate_time"


        // Title Attributes
        internal const val SHOW_TITLE = "show_title"
        internal const val TITLE = "title"
        internal const val TITLE_RESOURCE = "title_resource"
        internal const val TITLE_SIZE = "title_size"
        internal const val TITLE_COLOR = "title_color"
        internal const val TITLE_COLOR_VALUE = "title_color_value"
        internal const val TITLE_FONT_STYLE = "title_font_style"


        // Subtitle attributes
        internal const val SUBTITLE = "subtitle"
        internal const val SUBTITLE_RESOURCE = "subtitle_resource"
        internal const val SUBTITLE_SIZE = "subtitle_size"
        internal const val SUBTITLE_COLOR = "subtitle_color"
        internal const val SUBTITLE_COLOR_VALUE = "subtitle_color_value"
        internal const val SUBTITLE_ITALIC = "subtitle_italic"
        internal const val SUBTITLE_FONT_STYLE = "subtitle_font_style"

        // Splash Logo
        internal const val SHOW_LOGO = "show_logo"
        internal const val LOGO = "logo"
        internal const val LOGO_WIDTH = "logo_width"
        internal const val LOGO_HEIGHT = "logo_height"
        internal const val LOGO_SCALE_TYPE = "logo_scale_type"

        // Animation
        internal const val ANIMATION_TYPE = "animation_type"
        internal const val ANIMATION_DURATION = "animation_duration"


        // Progress bar
        internal const val SHOW_PROGRESS = "show_progress"
        internal const val PROGRESS_COLOR = "progress_color"
        internal const val PROGRESS_COLOR_VALUE = "progress_color_value"

        // Splash Screen Background
        internal const val BACKGROUND_COLOR = "background_color"
        internal const val BACKGROUND_COLOR_VALUE = "background_color_value"
        internal const val BACKGROUND_RESOURCE = "background_image"

        // Status and Navigation Bar Color
        const val FULL_SCREEN = "full_screen"
        const val CLICK_TO_HIDE = "click_to_hide"

        // on OnComplete listener
        internal var onComplete: Splashy.OnComplete? = null

        internal lateinit var activity: SplashyActivity

        internal fun hideSplashy() {
            if (onComplete != null) {
                onComplete?.onComplete()
                onComplete = null
            }
            activity.finish()
        }


    }

}