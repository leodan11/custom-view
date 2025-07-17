package com.github.leodan11.customview.progress;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.github.leodan11.customview.progress.utils.DirectionUtils;
import com.github.leodan11.customview.progress.utils.GradientDirection;
import com.github.leodan11.customview.progress.utils.ProgressDirection;

public class CircularProgressBar extends View {

    private static final float DEFAULT_MAX_VALUE = 100f;
    private static final float DEFAULT_START_ANGLE = 270f;
    private static final long DEFAULT_ANIMATION_DURATION = 1500L;

    private final Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint foregroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF rectF = new RectF();

    private float progress = 0f;
    private float progressMax = DEFAULT_MAX_VALUE;
    private float progressBarWidth;
    private float backgroundProgressBarWidth;
    private int progressBarColor = Color.BLACK;
    private Integer progressBarColorStart, progressBarColorEnd;
    private GradientDirection progressBarColorDirection = GradientDirection.LEFT_TO_RIGHT;
    private int backgroundBarColor = Color.GRAY;
    private Integer backgroundBarColorStart, backgroundBarColorEnd;
    private GradientDirection backgroundColorDirection = GradientDirection.LEFT_TO_RIGHT;
    private boolean roundBorder = false;
    private float startAngle = DEFAULT_START_ANGLE;
    private ProgressDirection progressDirection = ProgressDirection.TO_RIGHT;
    private boolean indeterminateMode = false;

    private float progressIndeterminate = 0f;
    private ProgressDirection progressDirIndeterminate = ProgressDirection.TO_RIGHT;
    private float startAngleIndeterminate = DEFAULT_START_ANGLE;

    private ValueAnimator animator;
    private Handler indeterminateHandler;
    private Runnable indeterminateRunnable;

    private OnProgressChangeListener onProgressChangeListener;
    private OnIndeterminateModeChangeListener onIndeterminateModeChangeListener;

    public CircularProgressBar(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        initAttrs(ctx, attrs);
        initPaints();
        initIndeterminateRunnable();
    }

    private void initAttrs(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CircularProgressBar);

        setProgress(a.getFloat(R.styleable.CircularProgressBar_cpb_progress, progress));
        setProgressMax(a.getFloat(R.styleable.CircularProgressBar_cpb_progress_max, progressMax));

        setProgressBarWidth(pxToDp(a.getDimension(R.styleable.CircularProgressBar_cpb_progressbar_width, dpToPx(4))));
        setBackgroundProgressBarWidth(pxToDp(a.getDimension(R.styleable.CircularProgressBar_cpb_background_progressbar_width, dpToPx(4))));

        setProgressBarColor(a.getColor(R.styleable.CircularProgressBar_cpb_progressbar_color, progressBarColor));
        int ps = a.getColor(R.styleable.CircularProgressBar_cpb_progressbar_color_start, 0);
        if (ps != 0) setProgressBarColorStart(ps);
        int pe = a.getColor(R.styleable.CircularProgressBar_cpb_progressbar_color_end, 0);
        if (pe != 0) setProgressBarColorEnd(pe);
        setProgressBarColorDirection(GradientDirection.fromInt(a.getInt(R.styleable.CircularProgressBar_cpb_progressbar_color_direction, progressBarColorDirection.getValue())));

        setBackgroundBarColor(a.getColor(R.styleable.CircularProgressBar_cpb_background_progressbar_color, backgroundBarColor));
        int bs = a.getColor(R.styleable.CircularProgressBar_cpb_background_progressbar_color_start, 0);
        if (bs != 0) setBackgroundBarColorStart(bs);
        int be = a.getColor(R.styleable.CircularProgressBar_cpb_background_progressbar_color_end, 0);
        if (be != 0) setBackgroundBarColorEnd(be);
        setBackgroundColorDirection(GradientDirection.fromInt(a.getInt(R.styleable.CircularProgressBar_cpb_background_progressbar_color_direction, backgroundColorDirection.getValue())));

        setProgressDirection(ProgressDirection.fromInt(a.getInt(R.styleable.CircularProgressBar_cpb_progress_direction, progressDirection.getValue())));
        setRoundBorder(a.getBoolean(R.styleable.CircularProgressBar_cpb_round_border, roundBorder));
        setStartAngle(a.getFloat(R.styleable.CircularProgressBar_cpb_start_angle, 0f));
        setIndeterminateMode(a.getBoolean(R.styleable.CircularProgressBar_cpb_indeterminate_mode, indeterminateMode));

        a.recycle();
    }

    private void initPaints() {
        backgroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        setProgressBarWidth(4);  // valor por defecto
        setBackgroundProgressBarWidth(4);
    }

    private void initIndeterminateRunnable() {
        indeterminateRunnable = new Runnable() {
            @Override
            public void run() {
                if (indeterminateMode) {
                    progressDirIndeterminate = progressDirIndeterminate.reverse();
                    if (progressDirIndeterminate.isToRight()) {
                        animateProgress(0f, DEFAULT_ANIMATION_DURATION, null, null);
                    } else {
                        animateProgress(progressMax, DEFAULT_ANIMATION_DURATION, null, null);
                    }
                    indeterminateHandler.postDelayed(this, DEFAULT_ANIMATION_DURATION);
                }
            }
        };
    }

    public void setProgress(float value) {
        progress = Math.min(value, progressMax);
        if (onProgressChangeListener != null) onProgressChangeListener.onProgressChanged(progress);
        invalidate();
    }

    public void setProgressMax(float max) {
        progressMax = max >= 0 ? max : DEFAULT_MAX_VALUE;
        invalidate();
    }

    public void setProgressBarWidth(float dp) {
        progressBarWidth = dpToPx(dp);
        foregroundPaint.setStrokeWidth(progressBarWidth);
        requestLayout();
        invalidate();
    }

    public void setBackgroundProgressBarWidth(float dp) {
        backgroundProgressBarWidth = dpToPx(dp);
        backgroundPaint.setStrokeWidth(backgroundProgressBarWidth);
        requestLayout();
        invalidate();
    }

    public void setProgressBarColor(int color) {
        progressBarColor = color;
        applyGradientToPaint(foregroundPaint, progressBarColorStart, progressBarColorEnd, progressBarColorDirection);
    }

    public void setProgressBarColorStart(int start) {
        progressBarColorStart = start;
        applyGradientToPaint(foregroundPaint, start, progressBarColorEnd, progressBarColorDirection);
    }

    public void setProgressBarColorEnd(int end) {
        progressBarColorEnd = end;
        applyGradientToPaint(foregroundPaint, progressBarColorStart, end, progressBarColorDirection);
    }

    public void setProgressBarColorDirection(GradientDirection dir) {
        progressBarColorDirection = dir;
        applyGradientToPaint(foregroundPaint, progressBarColorStart, progressBarColorEnd, dir);
    }

    public void setBackgroundBarColor(int color) {
        backgroundBarColor = color;
        applyGradientToPaint(backgroundPaint, backgroundBarColorStart, backgroundBarColorEnd, backgroundColorDirection);
    }

    public void setBackgroundBarColorStart(int start) {
        backgroundBarColorStart = start;
        applyGradientToPaint(backgroundPaint, start, backgroundBarColorEnd, backgroundColorDirection);
    }

    public void setBackgroundBarColorEnd(int end) {
        backgroundBarColorEnd = end;
        applyGradientToPaint(backgroundPaint, backgroundBarColorStart, end, backgroundColorDirection);
    }

    public void setBackgroundColorDirection(GradientDirection dir) {
        backgroundColorDirection = dir;
        applyGradientToPaint(backgroundPaint, backgroundBarColorStart, backgroundBarColorEnd, dir);
    }

    public void setRoundBorder(boolean round) {
        roundBorder = round;
        foregroundPaint.setStrokeCap(round ? Paint.Cap.ROUND : Paint.Cap.BUTT);
        invalidate();
    }

    public void setStartAngle(float angle) {
        float a = angle + DEFAULT_START_ANGLE % 360;
        startAngle = Math.max(0f, Math.min(a, 360f));
        invalidate();
    }

    public void setProgressDirection(ProgressDirection dir) {
        progressDirection = dir;
        invalidate();
    }

    public void setIndeterminateMode(boolean enable) {
        indeterminateMode = enable;
        if (onIndeterminateModeChangeListener != null)
            onIndeterminateModeChangeListener.onIndeterminateModeChanged(indeterminateMode);
        progressIndeterminate = 0f;
        progressDirIndeterminate = ProgressDirection.TO_RIGHT;
        startAngleIndeterminate = DEFAULT_START_ANGLE;
        if (indeterminateHandler != null)
            indeterminateHandler.removeCallbacks(indeterminateRunnable);
        if (indeterminateMode) {
            indeterminateHandler = new Handler();
            indeterminateHandler.post(indeterminateRunnable);
        } else if (indeterminateHandler != null) {
            indeterminateHandler.removeCallbacks(indeterminateRunnable);
        }
        invalidate();
    }

    public float getProgress() {
        return progress;
    }

    public float getProgressMax() {
        return progressMax;
    }

    public float getProgressBarWidth() {
        return progressBarWidth;
    }

    public float getBackgroundProgressBarWidth() {
        return backgroundProgressBarWidth;
    }

    public int getProgressBarColor() {
        return progressBarColor;
    }

    public Integer getProgressBarColorStart() {
        return progressBarColorStart;
    }

    public Integer getProgressBarColorEnd() {
        return progressBarColorEnd;
    }

    public GradientDirection getProgressBarColorDirection() {
        return progressBarColorDirection;
    }

    public int getBackgroundBarColor() {
        return backgroundBarColor;
    }

    public Integer getBackgroundBarColorStart() {
        return backgroundBarColorStart;
    }

    public Integer getBackgroundBarColorEnd() {
        return backgroundBarColorEnd;
    }

    public GradientDirection getBackgroundColorDirection() {
        return backgroundColorDirection;
    }

    public boolean hasRoundBorder() {
        return roundBorder;
    }

    public float getStartAngle() {
        return startAngle;
    }

    public ProgressDirection getProgressDirection() {
        return progressDirection;
    }

    public boolean isIndeterminateMode() {
        return indeterminateMode;
    }

    public float getProgressIndeterminate() {
        return progressIndeterminate;
    }

    public ProgressDirection getProgressDirIndeterminate() {
        return progressDirIndeterminate;
    }

    public float getStartAngleIndeterminate() {
        return startAngleIndeterminate;
    }

    public void animateProgress(float toValue, long duration, TimeInterpolator interp, Long delay) {
        if (animator != null) animator.cancel();
        float from = indeterminateMode ? progressIndeterminate : progress;
        animator = ValueAnimator.ofFloat(from, toValue);
        animator.setDuration(duration);
        if (interp != null) animator.setInterpolator(interp);
        if (delay != null) animator.setStartDelay(delay);

        animator.addUpdateListener(a -> {
            float v = (float) a.getAnimatedValue();
            if (indeterminateMode) {
                progressIndeterminate = v;
                startAngleIndeterminate = DEFAULT_START_ANGLE +
                        (progressDirIndeterminate.isToRight() ? v : -v) * 360 / 100;
            } else {
                setProgress(v);
            }
        });
        animator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        applyGradientToPaint(foregroundPaint, progressBarColorStart, progressBarColorEnd, progressBarColorDirection);
        applyGradientToPaint(backgroundPaint, backgroundBarColorStart, backgroundBarColorEnd, backgroundColorDirection);
    }

    @Override
    protected void onMeasure(int wSpec, int hSpec) {
        int w = getDefaultSize(getSuggestedMinimumWidth(), wSpec);
        int h = getDefaultSize(getSuggestedMinimumHeight(), hSpec);
        int size = Math.min(w, h);
        setMeasuredDimension(size, size);
        float stroke = Math.max(progressBarWidth, backgroundProgressBarWidth);
        rectF.set(stroke / 2f, stroke / 2f, size - stroke / 2f, size - stroke / 2f);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawOval(rectF, backgroundPaint);

        float realProgress = ((indeterminateMode ? progressIndeterminate : progress) * DEFAULT_MAX_VALUE) / progressMax;
        boolean toRight = indeterminateMode
                ? progressDirIndeterminate.isToRight()
                : progressDirection.isToRight();
        float sweep = (toRight ? 360f : -360f) * realProgress / 100f;
        float start = indeterminateMode ? startAngleIndeterminate : startAngle;
        canvas.drawArc(rectF, start, sweep, false, foregroundPaint);
    }

    @Override
    public void setBackgroundColor(int bg) {
        setBackgroundBarColor(bg);
    }

    private void applyGradientToPaint(Paint paint, Integer startColor, Integer endColor, GradientDirection dir) {
        int sc = startColor != null ? startColor : (paint == foregroundPaint ? progressBarColor : backgroundBarColor);
        int ec = endColor != null ? endColor : sc;
        LinearGradient grad = DirectionUtils.createLinearGradient(dir, getWidth(), getHeight(), sc, ec);
        paint.setShader(grad);
        invalidate();
    }

    private float dpToPx(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }

    private float pxToDp(float px) {
        return px / getResources().getDisplayMetrics().density;
    }


    public interface OnProgressChangeListener {
        void onProgressChanged(float newProgress);
    }

    public interface OnIndeterminateModeChangeListener {
        void onIndeterminateModeChanged(boolean isIndeterminate);
    }

    public void setOnProgressChangeListener(OnProgressChangeListener l) {
        onProgressChangeListener = l;
    }

    public void setOnIndeterminateModeChangeListener(OnIndeterminateModeChangeListener l) {
        onIndeterminateModeChangeListener = l;
    }
}
