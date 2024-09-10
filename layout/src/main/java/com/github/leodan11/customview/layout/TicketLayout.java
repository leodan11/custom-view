package com.github.leodan11.customview.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.graphics.Bitmap.Config.ALPHA_8;
import static android.graphics.Color.BLACK;
import static android.graphics.Color.TRANSPARENT;
import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.graphics.PorterDuff.Mode.SRC_IN;

import static com.github.leodan11.customview.core.utils.DisplayMetrics.dpToPx;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import com.github.leodan11.customview.core.utils.BlurBuilder;

public class TicketLayout extends FrameLayout {

    public static final String TAG = TicketLayout.class.getSimpleName();
    private int mViewID;
    private View mView;
    private @DividerLocation
    int mDividerLocation;

    private @PunchLocation
    int mPunchLocation;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Orientation.HORIZONTAL, Orientation.VERTICAL})
    public @interface Orientation {
        int HORIZONTAL = 0;
        int VERTICAL = 1;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({DividerType.NORMAL, DividerType.DASH})
    public @interface DividerType {
        int NORMAL = 0;
        int DASH = 1;
    }


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({DividerDesign.COLOR, DividerDesign.PUNCH})
    public @interface DividerDesign {
        int COLOR = 0;
        int PUNCH = 1;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CornerType.NORMAL, CornerType.ROUNDED})
    public @interface CornerType {
        int NORMAL = 0;
        int ROUNDED = 1;
        int SCALLOP = 2;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({DividerLocation.NONE, DividerLocation.TOP, DividerLocation.BOTTOM, DividerLocation.LEFT, DividerLocation.RIGHT})
    public @interface DividerLocation {
        int NONE = 0;
        int TOP = 1;
        int BOTTOM = 2;
        int LEFT = 4;
        int RIGHT = 8;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PunchLocation.NONE, PunchLocation.TOP, PunchLocation.BOTTOM, PunchLocation.LEFT, PunchLocation.RIGHT})
    public @interface PunchLocation {
        int NONE = 0;
        int TOP = 1;
        int BOTTOM = 2;
        int LEFT = 4;
        int RIGHT = 8;
    }

    private final Paint mBackgroundPaint = new Paint();
    private final Paint mBorderPaint = new Paint();
    private Paint mDividerPaint = new Paint();
    private int mOrientation = Orientation.HORIZONTAL;
    private final Path mPath = new Path();
    private boolean mDirty = true;
    private float mTopDividerStartX, mTopDividerStartY, mTopDividerStopX, mTopDividerStopY;
    private float mBottomDividerStartX, mBottomDividerStartY, mBottomDividerStopX, mBottomDividerStopY;
    private float mLeftDividerStartX, mLeftDividerStartY, mLeftDividerStopX, mLeftDividerStopY;
    private float mRightDividerStartX, mRightDividerStartY, mRightDividerStopX, mRightDividerStopY;
    private final RectF mRoundedCornerArc = new RectF();
    private final RectF mScallopCornerArc = new RectF();
    private int mBackgroundColor;
    private boolean mShowBorder;
    private int mBorderWidth;
    private int mBorderColor;
    private boolean mShowDivider;
    private int mScallopRadius;
    private int mPunchRadius;
    private int mDividerDashLength;
    private int mDividerDashGap;
    private int mDividerType;
    private int mDividerDesign;
    private int mDividerWidth;
    private int mDividerColor;
    private int mCornerType;
    private int mCornerRadius;
    private int mDividerPadding;
    private Bitmap mShadow;
    private final Paint mShadowPaint = new Paint(ANTI_ALIAS_FLAG);
    private float mShadowBlurRadius = 0f;

    public TicketLayout(Context context) {
        super(context);
        init(null);
    }

    public TicketLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TicketLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        // call block() here if you want to draw behind children
        super.dispatchDraw(canvas);
        // call block() here if you want to draw over children

        if (mShowDivider) {

            if (mDividerLocation == (mDividerLocation | DividerLocation.TOP))
                canvas.drawLine(mTopDividerStartX, mTopDividerStartY, mTopDividerStopX, mTopDividerStopY, mDividerPaint);
            if (mDividerLocation == (mDividerLocation | DividerLocation.BOTTOM))
                canvas.drawLine(mBottomDividerStartX, mBottomDividerStartY, mBottomDividerStopX, mBottomDividerStopY, mDividerPaint);
            if (mDividerLocation == (mDividerLocation | DividerLocation.LEFT))
                canvas.drawLine(mLeftDividerStartX, mLeftDividerStartY, mLeftDividerStopX, mLeftDividerStopY, mDividerPaint);
            if (mDividerLocation == (mDividerLocation | DividerLocation.RIGHT))
                canvas.drawLine(mRightDividerStartX, mRightDividerStartY, mRightDividerStopX, mRightDividerStopY, mDividerPaint);

        }

    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        if (mDirty) {
            doLayout();
        }
        if (mShadowBlurRadius > 0f && !isInEditMode()) {
            canvas.drawBitmap(mShadow, 0f, mShadowBlurRadius / 2f, null);
        }
        canvas.drawPath(mPath, mBackgroundPaint);
        canvas.clipPath(mPath);

        if (mShowBorder) {
            canvas.drawPath(mPath, mBorderPaint);
        }

        super.onDraw(canvas);
    }

    private void doLayout() {
        RectF mRect = new RectF();
        float left = getPaddingLeft() + mShadowBlurRadius;
        float right = getWidth() - getPaddingRight() - mShadowBlurRadius;
        float top = getPaddingTop() + mShadowBlurRadius + (mShadowBlurRadius / 2);
        float bottom = getHeight() - getPaddingBottom() - mShadowBlurRadius - (mShadowBlurRadius / 2);
        mPath.reset();

        if (mOrientation == Orientation.VERTICAL) {

            if (mCornerType == CornerType.ROUNDED) {
                mPath.arcTo(getTopLeftCornerRoundedArc(top, left), 180.0f, 90.0f, false);
                mPath.lineTo(left + mCornerRadius, top);

                mPath.lineTo(right - mCornerRadius, top);
                mPath.arcTo(getTopRightCornerRoundedArc(top, right), -90.0f, 90.0f, false);

                mPath.arcTo(getBottomRightCornerRoundedArc(bottom, right), 0.0f, 90.0f, false);
                mPath.lineTo(right - mCornerRadius, bottom);

                mPath.lineTo(left + mCornerRadius, bottom);
                mPath.arcTo(getBottomLeftCornerRoundedArc(left, bottom), 90.0f, 90.0f, false);

            } else if (mCornerType == CornerType.SCALLOP) {
                mPath.arcTo(getTopLeftCornerScallopArc(top, left), 90.0f, -90.0f, false);
                mPath.lineTo(left + mCornerRadius, top);

                mPath.lineTo(right - mCornerRadius, top);
                mPath.arcTo(getTopRightCornerScallopArc(top, right), 180.0f, -90.0f, false);

                mPath.arcTo(getBottomRightCornerScallopArc(bottom, right), 270.0f, -90.0f, false);
                mPath.lineTo(right - mCornerRadius, bottom);

                mPath.lineTo(left + mCornerRadius, bottom);
                mPath.arcTo(getBottomLeftCornerScallopArc(left, bottom), 0.0f, -90.0f, false);
            } else {
                mPath.moveTo(left, top);
                mPath.lineTo(right, top);
                mPath.lineTo(right, bottom);
                mPath.lineTo(left, bottom);
            }


            if (mView != null) {
                if (mDividerLocation == (mDividerLocation | DividerLocation.TOP)) {
                    Path topP = new Path();
                    mRect.set(left - mScallopRadius, mView.getTop() - mScallopRadius, left + mScallopRadius, mScallopRadius + mView.getTop());
                    topP.addArc(mRect, 90.0f, -180.0f);
                    mRect.set(right - mScallopRadius, mView.getTop() - mScallopRadius, right + mScallopRadius, mScallopRadius + mView.getTop());
                    topP.addArc(mRect, 270, -180.0f);

                    mPath.op(topP, Path.Op.DIFFERENCE);


                    mTopDividerStartX = left + mScallopRadius + mDividerPadding;
                    mTopDividerStartY = mView.getTop();
                    mTopDividerStopX = right - mScallopRadius - mDividerPadding;
                    mTopDividerStopY = mView.getTop();
                }

                if (mDividerLocation == (mDividerLocation | DividerLocation.BOTTOM)) {
                    Path bottomP = new Path();
                    mRect.set(left - mScallopRadius, mView.getBottom() - mScallopRadius, left + mScallopRadius, mScallopRadius + mView.getBottom());
                    bottomP.addArc(mRect, 90.0f, -180.0f);
                    mRect.set(right - mScallopRadius, mView.getBottom() - mScallopRadius, right + mScallopRadius, mScallopRadius + mView.getBottom());
                    bottomP.addArc(mRect, 270, -180.0f);

                    mPath.op(bottomP, Path.Op.DIFFERENCE);

                    mBottomDividerStartX = left + mScallopRadius + mDividerPadding;
                    mBottomDividerStartY = mView.getBottom();
                    mBottomDividerStopX = right - mScallopRadius - mDividerPadding;
                    mBottomDividerStopY = mView.getBottom();
                }
            }

            if (mPunchLocation == (mPunchLocation | PunchLocation.TOP)) {
                Path topC = new Path();

                mRect.set(((getRight() - getLeft()) / 2f) - mPunchRadius, top - (mPunchRadius * 1.5f), ((getRight() - getLeft()) / 2f) + mPunchRadius, top + (mPunchRadius / 1.5f));
                topC.addArc(mRect, 0f, 360f);

                mPath.op(topC, Path.Op.DIFFERENCE);
            }

            if (mPunchLocation == (mPunchLocation | PunchLocation.BOTTOM)) {
                Path bottomC = new Path();

                mRect.set(((getRight() - getLeft()) / 2f) - mPunchRadius, bottom - (mPunchRadius / 1.5f), ((getRight() - getLeft()) / 2f) + mPunchRadius, bottom + (mPunchRadius * 1.5f));
                bottomC.addArc(mRect, 0f, 360f);

                mPath.op(bottomC, Path.Op.DIFFERENCE);
            }

        } else {

            if (mCornerType == CornerType.ROUNDED) {
                mPath.arcTo(getTopLeftCornerRoundedArc(top, left), 180.0f, 90.0f, false);
                mPath.lineTo(left + mCornerRadius, top);

                mPath.lineTo(right - mCornerRadius, top);
                mPath.arcTo(getTopRightCornerRoundedArc(top, right), -90.0f, 90.0f, false);

                mPath.arcTo(getBottomRightCornerRoundedArc(bottom, right), 0.0f, 90.0f, false);
                mPath.lineTo(right - mCornerRadius, bottom);

                mPath.arcTo(getBottomLeftCornerRoundedArc(left, bottom), 90.0f, 90.0f, false);
                mPath.lineTo(left, bottom - mCornerRadius);

            } else if (mCornerType == CornerType.SCALLOP) {

                mPath.arcTo(getTopLeftCornerScallopArc(top, left), 90.0f, -90.0f, false);
                mPath.lineTo(left + mCornerRadius, top);

                mPath.lineTo(right - mCornerRadius, top);
                mPath.arcTo(getTopRightCornerScallopArc(top, right), 180.0f, -90.0f, false);

                mPath.arcTo(getBottomRightCornerScallopArc(bottom, right), 270.0f, -90.0f, false);
                mPath.lineTo(right - mCornerRadius, bottom);

                mPath.arcTo(getBottomLeftCornerScallopArc(left, bottom), 0.0f, -90.0f, false);
                mPath.lineTo(left, bottom - mCornerRadius);

            } else {
                mPath.moveTo(left, top);
                mPath.lineTo(right, top);
                mPath.lineTo(right, bottom);
                mPath.lineTo(left, bottom);
            }


            if (mView != null) {
                if (mDividerLocation == (mDividerLocation | DividerLocation.LEFT)) {

                    Path leftP = new Path();

                    mRect.set(mView.getLeft() - mScallopRadius, bottom - mScallopRadius, mScallopRadius + mView.getLeft(), bottom + mScallopRadius);
                    leftP.addArc(mRect, 0f, 360f);
                    mRect.set(mView.getLeft() - mScallopRadius, top - mScallopRadius, mScallopRadius + mView.getLeft(), top + mScallopRadius);
                    leftP.addArc(mRect, 0f, 360f);

                    mPath.op(leftP, Path.Op.DIFFERENCE);


                    mLeftDividerStartX = mView.getLeft();
                    mLeftDividerStartY = top + mScallopRadius + mDividerPadding;
                    mLeftDividerStopX = mView.getLeft();
                    mLeftDividerStopY = bottom - mScallopRadius - mDividerPadding;
                }

                if (mDividerLocation == (mDividerLocation | DividerLocation.RIGHT)) {

                    Path rightP = new Path();

                    mRect.set(mView.getRight() - mScallopRadius, bottom - mScallopRadius, mScallopRadius + mView.getRight(), bottom + mScallopRadius);
                    rightP.addArc(mRect, 0f, 360f);
                    mRect.set(mView.getRight() - mScallopRadius, top - mScallopRadius, mScallopRadius + mView.getRight(), top + mScallopRadius);
                    rightP.addArc(mRect, 0f, 360f);

                    mPath.op(rightP, Path.Op.DIFFERENCE);

                    mRightDividerStartX = mView.getRight();
                    mRightDividerStartY = top + mScallopRadius + mDividerPadding;
                    mRightDividerStopX = mView.getRight();
                    mRightDividerStopY = bottom - mScallopRadius - mDividerPadding;
                }
            }

            if (mPunchLocation == (mPunchLocation | PunchLocation.LEFT)) {
                Path leftC = new Path();

                mRect.set(left - (mPunchRadius * 1.5f), ((getBottom() - getTop()) / 2f) - mPunchRadius, (mPunchRadius / 1.5f) + left, ((getBottom() - getTop()) / 2f) + mPunchRadius);
                leftC.addArc(mRect, 0f, 360f);

                mPath.op(leftC, Path.Op.DIFFERENCE);
            }

            if (mPunchLocation == (mPunchLocation | PunchLocation.RIGHT)) {
                Path rightC = new Path();

                mRect.set(right - (mPunchRadius / 1.5f), ((getBottom() - getTop()) / 2f) - mPunchRadius, (mPunchRadius * 1.5f) + right, ((getBottom() - getTop()) / 2f) + mPunchRadius);
                rightC.addArc(mRect, 0f, 360f);

                mPath.op(rightC, Path.Op.DIFFERENCE);
            }

        }

        generateShadow();
        mDirty = false;
    }

    private void generateShadow() {
        if (!isInEditMode()) {
            if (mShadowBlurRadius == 0f) return;

            if (mShadow == null) {
                mShadow = Bitmap.createBitmap(getWidth(), getHeight(), ALPHA_8);
            } else {
                mShadow.eraseColor(TRANSPARENT);
            }
            Canvas c = new Canvas(mShadow);
            c.drawPath(mPath, mShadowPaint);
            if (mShowBorder) {
                c.drawPath(mPath, mShadowPaint);
            }
            mShadow = BlurBuilder.blur(getContext(), mShadow, mShadowBlurRadius);
        }
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedValue a = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.windowBackground, a, true);

            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TicketLayout);
            mOrientation = typedArray.getInt(R.styleable.TicketLayout_ticketOrientation, Orientation.HORIZONTAL);
            mBackgroundColor = typedArray.getColor(R.styleable.TicketLayout_ticketBackgroundColor, getResources().getColor(android.R.color.white));
            mScallopRadius = typedArray.getDimensionPixelSize(R.styleable.TicketLayout_ticketScallopRadius, dpToPx(20f, getContext()));
            mShowBorder = typedArray.getBoolean(R.styleable.TicketLayout_ticketShowBorder, false);
            mBorderWidth = typedArray.getDimensionPixelSize(R.styleable.TicketLayout_ticketBorderWidth, dpToPx(2f, getContext()));
            mBorderColor = typedArray.getColor(R.styleable.TicketLayout_ticketBorderColor, getResources().getColor(android.R.color.black));
            mShowDivider = typedArray.getBoolean(R.styleable.TicketLayout_ticketShowDivider, false);
            mDividerType = typedArray.getInt(R.styleable.TicketLayout_ticketDividerType, DividerType.NORMAL);
            mDividerDesign = typedArray.getInt(R.styleable.TicketLayout_ticketDividerDesign, DividerDesign.COLOR);
            mDividerWidth = typedArray.getDimensionPixelSize(R.styleable.TicketLayout_ticketDividerWidth, dpToPx(2f, getContext()));
            mDividerColor = typedArray.getColor(R.styleable.TicketLayout_ticketDividerColor, getResources().getColor(android.R.color.darker_gray));
            mDividerDashLength = typedArray.getDimensionPixelSize(R.styleable.TicketLayout_ticketDividerDashLength, dpToPx(8f, getContext()));
            mDividerDashGap = typedArray.getDimensionPixelSize(R.styleable.TicketLayout_ticketDividerDashGap, dpToPx(4f, getContext()));
            mCornerType = typedArray.getInt(R.styleable.TicketLayout_ticketCornerType, CornerType.NORMAL);
            mCornerRadius = typedArray.getDimensionPixelSize(R.styleable.TicketLayout_ticketCornerRadius, dpToPx(4f, getContext()));
            mDividerPadding = typedArray.getDimensionPixelSize(R.styleable.TicketLayout_ticketDividerPadding, dpToPx(10f, getContext()));
            mViewID = typedArray.getResourceId(R.styleable.TicketLayout_view, -1);
            mDividerLocation = typedArray.getInt(R.styleable.TicketLayout_ticketDividerLocation, DividerLocation.NONE);
            mPunchLocation = typedArray.getInt(R.styleable.TicketLayout_ticketPunchLocation, PunchLocation.NONE);
            mPunchRadius = typedArray.getDimensionPixelSize(R.styleable.TicketLayout_ticketPunchRadius, dpToPx(20f, getContext()));
            float elevation = 0f;
            if (typedArray.hasValue(R.styleable.TicketLayout_ticketElevation)) {
                elevation = typedArray.getDimension(R.styleable.TicketLayout_ticketElevation, elevation);
            } else if (typedArray.hasValue(R.styleable.TicketLayout_android_elevation)) {
                elevation = typedArray.getDimension(R.styleable.TicketLayout_android_elevation, elevation);
            }
            if (elevation > 0f) {
                setShadowBlurRadius(elevation);
            }

            typedArray.recycle();
        }

        mShadowPaint.setColorFilter(new PorterDuffColorFilter(BLACK, SRC_IN));
        mShadowPaint.setAlpha(51); // 20%

        initElements();

        setWillNotDraw(false);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    private void initElements() {

        if (mDividerWidth > mScallopRadius) {
            Log.w(TAG, "You cannot apply divider width greater than scallop radius. Applying divider width to scallop radius.");
        }

        setBackgroundPaint();
        setBorderPaint();

        if (mDividerDesign == DividerDesign.COLOR)
            setDividerPaintAsColor();
        else
            setDividerPaintAsPunchThrough();

        mDirty = true;
        invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mView = findViewById(mViewID);
    }

    private void setBackgroundPaint() {
        mBackgroundPaint.setAlpha(0);
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setColor(mBackgroundColor);
        mBackgroundPaint.setStyle(Paint.Style.FILL);

        if (mShadowBlurRadius > 0f)
            mBackgroundPaint.setShadowLayer(mShadowBlurRadius, 0, 0, Color.parseColor("#D3000000"));
    }

    private void setBorderPaint() {
        mBorderPaint.setAlpha(0);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBorderPaint.setStyle(Paint.Style.STROKE);
    }

    private void setDividerPaintAsPunchThrough() {
        mDividerPaint = new Paint();
        mDividerPaint.setAntiAlias(true);
        mDividerPaint.setStrokeWidth(mDividerWidth);

        if (mDividerType == DividerType.DASH)
            mDividerPaint.setPathEffect(new DashPathEffect(new float[]{(float) mDividerDashLength, (float) mDividerDashGap}, 0.0f));
        else
            mDividerPaint.setPathEffect(new PathEffect());
        mDividerPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    private void setDividerPaintAsColor() {
        mDividerPaint = new Paint();
        mDividerPaint.setAlpha(1);
        mDividerPaint.setAntiAlias(true);
        mDividerPaint.setColor(mDividerColor);
        mDividerPaint.setStrokeWidth(mDividerWidth);

        if (mDividerType == DividerType.DASH)
            mDividerPaint.setPathEffect(new DashPathEffect(new float[]{(float) mDividerDashLength, (float) mDividerDashGap}, 0.0f));
        else
            mDividerPaint.setPathEffect(new PathEffect());
    }

    private RectF getTopLeftCornerRoundedArc(float top, float left) {
        mRoundedCornerArc.set(left, top, left + mCornerRadius * 2, top + mCornerRadius * 2);
        return mRoundedCornerArc;
    }

    private RectF getTopRightCornerRoundedArc(float top, float right) {
        mRoundedCornerArc.set(right - mCornerRadius * 2, top, right, top + mCornerRadius * 2);
        return mRoundedCornerArc;
    }

    private RectF getBottomLeftCornerRoundedArc(float left, float bottom) {
        mRoundedCornerArc.set(left, bottom - mCornerRadius * 2, left + mCornerRadius * 2, bottom);
        return mRoundedCornerArc;
    }

    private RectF getBottomRightCornerRoundedArc(float bottom, float right) {
        mRoundedCornerArc.set(right - mCornerRadius * 2, bottom - mCornerRadius * 2, right, bottom);
        return mRoundedCornerArc;
    }

    private RectF getTopLeftCornerScallopArc(float top, float left) {
        mScallopCornerArc.set(left - mCornerRadius, top - mCornerRadius, left + mCornerRadius, top + mCornerRadius);
        return mScallopCornerArc;
    }

    private RectF getTopRightCornerScallopArc(float top, float right) {
        mScallopCornerArc.set(right - mCornerRadius, top - mCornerRadius, right + mCornerRadius, top + mCornerRadius);
        return mScallopCornerArc;
    }

    private RectF getBottomLeftCornerScallopArc(float left, float bottom) {
        mScallopCornerArc.set(left - mCornerRadius, bottom - mCornerRadius, left + mCornerRadius, bottom + mCornerRadius);
        return mScallopCornerArc;
    }

    private RectF getBottomRightCornerScallopArc(float bottom, float right) {
        mScallopCornerArc.set(right - mCornerRadius, bottom - mCornerRadius, right + mCornerRadius, bottom + mCornerRadius);
        return mScallopCornerArc;
    }

    public int getOrientation() {
        return mOrientation;
    }

    public void setOrientation(int orientation) {
        this.mOrientation = orientation;
        initElements();
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
        initElements();
    }

    public boolean isShowBorder() {
        return mShowBorder;
    }

    public void setShowBorder(boolean showBorder) {
        this.mShowBorder = showBorder;
        initElements();
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.mBorderWidth = borderWidth;
        initElements();
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int borderColor) {
        this.mBorderColor = borderColor;
        initElements();
    }

    public boolean isShowDivider() {
        return mShowDivider;
    }

    public void setShowDivider(boolean showDivider) {
        this.mShowDivider = showDivider;
        initElements();
    }

    public int getScallopRadius() {
        return mScallopRadius;
    }

    public void setScallopRadius(int scallopRadius) {
        this.mScallopRadius = scallopRadius;
        initElements();
    }

    public int getDividerDashLength() {
        return mDividerDashLength;
    }

    public void setDividerDashLength(int dividerDashLength) {
        this.mDividerDashLength = dividerDashLength;
        initElements();
    }

    public int getDividerDashGap() {
        return mDividerDashGap;
    }

    public void setDividerDashGap(int dividerDashGap) {
        this.mDividerDashGap = dividerDashGap;
        initElements();
    }

    public int getDividerType() {
        return mDividerType;
    }

    public void setDividerType(int dividerType) {
        this.mDividerType = dividerType;
        initElements();
    }

    public int getDividerWidth() {
        return mDividerWidth;
    }

    public void setDividerWidth(int dividerWidth) {
        this.mDividerWidth = dividerWidth;
        initElements();
    }

    public int getDividerPadding() {
        return mDividerPadding;
    }

    public void setDividerPadding(int mDividerPadding) {
        this.mDividerPadding = mDividerPadding;
        initElements();
    }

    public int getDividerColor() {
        return mDividerColor;
    }

    public void setDividerColor(int dividerColor) {
        this.mDividerColor = dividerColor;
        initElements();
    }

    public int getCornerType() {
        return mCornerType;
    }

    public void setCornerType(int cornerType) {
        this.mCornerType = cornerType;
        initElements();
    }

    public int getCornerRadius() {
        return mCornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.mCornerRadius = cornerRadius;
        initElements();
    }

    public void setTicketElevation(float elevation) {
        setShadowBlurRadius(elevation);
        initElements();
    }

    private void setShadowBlurRadius(float elevation) {
        float maxElevation = dpToPx(24f, getContext());
        mShadowBlurRadius = Math.min(25f * (elevation / maxElevation), 25f);
    }

}
