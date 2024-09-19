package com.github.leodan11.customview.widget.speeddial;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.github.leodan11.customview.widget.R;
import com.github.leodan11.customview.widget.tools.UiUtils;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SpeedDialOverlayLayout extends RelativeLayout {
    private static final String TAG = SpeedDialOverlayLayout.class.getSimpleName();
    private boolean mClickableOverlay;
    private int mAnimationDuration;
    @Nullable
    private OnClickListener mClickListener;

    public SpeedDialOverlayLayout(@NonNull Context context) {
        super(context);
    }

    public SpeedDialOverlayLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SpeedDialOverlayLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int
            defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public boolean hasClickableOverlay() {
        return mClickableOverlay;
    }

    /**
     * Enables or disables the click on the overlay view.
     *
     * @param clickableOverlay True to enable the click, false otherwise.
     */
    public void setClickableOverlay(boolean clickableOverlay) {
        mClickableOverlay = clickableOverlay;
        setOnClickListener(mClickListener);
    }

    public void setAnimationDuration(int animationDuration) {
        mAnimationDuration = animationDuration;
    }

    public void show() {
        show(true);
    }

    public void show(boolean animate) {
        if (animate) {
            UiUtils.fadeInAnim(this);
        } else {
            setVisibility(VISIBLE);
        }
    }

    public void hide() {
        hide(true);
    }

    public void hide(boolean animate) {
        if (animate) {
            UiUtils.fadeOutAnim(this);
        } else {
            setVisibility(GONE);
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener clickListener) {
        mClickListener = clickListener;
        super.setOnClickListener(hasClickableOverlay() ? clickListener : null);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray attr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SpeedDialOverlayLayout, 0, 0);
        int overlayColor = ResourcesCompat.getColor(getResources(), R.color.sd_overlay_color, context.getTheme());
        try {
            overlayColor = attr.getColor(R.styleable.SpeedDialOverlayLayout_android_background, overlayColor);
            mClickableOverlay = attr.getBoolean(R.styleable.SpeedDialOverlayLayout_clickable_overlay, true);
        } catch (Exception e) {
            Log.e(TAG, "Failure setting FabOverlayLayout attrs", e);
        } finally {
            attr.recycle();
        }
        setElevation(getResources().getDimension(R.dimen.sd_overlay_elevation));
        setBackgroundColor(overlayColor);
        setVisibility(View.GONE);
        mAnimationDuration = getResources().getInteger(android.R.integer.config_longAnimTime);
    }

}
