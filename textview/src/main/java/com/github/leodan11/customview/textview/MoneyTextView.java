package com.github.leodan11.customview.textview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MoneyTextView extends View {

    private static final int GRAVITY_START = 1;
    private static final int GRAVITY_END = 2;
    private static final int GRAVITY_TOP = 4;
    private static final int GRAVITY_BOTTOM = 8;
    private static final int GRAVITY_CENTER_VERTICAL = 16;
    private static final int GRAVITY_CENTER_HORIZONTAL = 32;
    private static final float MIN_PADDING = 2;

    private TextPaint mTextPaint;
    private DecimalFormat mDecimalFormat;
    private Section mSymbolSection;
    private Section mIntegerSection;
    private Section mDecimalSection;
    private char mDecimalSeparator;
    private float mAmount;
    private int mGravity;
    private int mSymbolGravity;
    private int mDecimalGravity;
    private float mSymbolMargin;
    private float mDecimalMargin;
    private boolean mIncludeDecimalSeparator;
    private int mWidth;
    private int mHeight;
    private float mTextPaintRoomSize;

    public MoneyTextView(Context context) {
        super(context);
        init(context, null);
    }

    public MoneyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MoneyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public MoneyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mSymbolSection = new Section();
        mIntegerSection = new Section();
        mDecimalSection = new Section();

        Resources r = getResources();
        mTextPaintRoomSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mTextPaint.density, r.getDisplayMetrics());

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MoneyTextView,
                0, R.style.MoneyTextViewDefaultStyle);

        try {
            mSymbolSection.text = typedArray.getString(R.styleable.MoneyTextView_symbol);
            mAmount = typedArray.getFloat(R.styleable.MoneyTextView_amount, 0);
            mGravity = typedArray.getInt(R.styleable.MoneyTextView_gravity, GRAVITY_CENTER_VERTICAL | GRAVITY_CENTER_HORIZONTAL);
            mSymbolGravity = typedArray.getInt(R.styleable.MoneyTextView_symbolGravity, GRAVITY_TOP | GRAVITY_START);
            mDecimalGravity = typedArray.getInt(R.styleable.MoneyTextView_decimalGravity, GRAVITY_TOP);
            mIncludeDecimalSeparator = typedArray.getBoolean(R.styleable.MoneyTextView_includeDecimalSeparator, true);
            mSymbolMargin = typedArray.getDimensionPixelSize(R.styleable.MoneyTextView_symbolMargin, 0);
            mDecimalMargin = typedArray.getDimensionPixelSize(R.styleable.MoneyTextView_decimalMargin, 0);
            mIntegerSection.textSize = typedArray.getDimension(R.styleable.MoneyTextView_baseTextSize, 12f);
            mSymbolSection.textSize = typedArray.getDimension(R.styleable.MoneyTextView_symbolTextSize, mIntegerSection.textSize);
            mDecimalSection.textSize = typedArray.getDimension(R.styleable.MoneyTextView_decimalDigitsTextSize, mIntegerSection.textSize);
            mIntegerSection.color = typedArray.getInt(R.styleable.MoneyTextView_baseTextColor, 0);
            mSymbolSection.color = typedArray.getInt(R.styleable.MoneyTextView_symbolTextColor, mIntegerSection.color);
            mDecimalSection.color = typedArray.getInt(R.styleable.MoneyTextView_decimalTextColor, mIntegerSection.color);
            mDecimalSection.drawUnderline = typedArray.getBoolean(R.styleable.MoneyTextView_decimalUnderline, false);

            String format = typedArray.getString(R.styleable.MoneyTextView_format);
            String decimalSeparator = typedArray.getString(R.styleable.MoneyTextView_decimalSeparator);
            String fontPath = typedArray.getString(R.styleable.MoneyTextView_fontPath);
            if (fontPath != null) {
                Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontPath);
                mTextPaint.setTypeface(typeface);
            } else {
                int mTextStyle = typedArray.getInt(R.styleable.MoneyTextView_baseTextStyle, -1);
                if (mTextStyle >= 0 && mTextStyle <= 3) {
                    int[] typefaceStyles = {
                            Typeface.NORMAL,             // 0 -> NORMAL
                            Typeface.BOLD,               // 1 -> BOLD
                            Typeface.ITALIC,             // 2 -> ITALIC
                            Typeface.BOLD_ITALIC        // 3 -> BOLD_ITALIC
                    };
                    Typeface typeface = Typeface.create(mTextPaint.getTypeface(), typefaceStyles[mTextStyle]);
                    mTextPaint.setTypeface(typeface);
                }
            }

            if (format == null) {
                format = context.getString(R.string.default_format);
            }

            mDecimalFormat = new DecimalFormat(format);
            DecimalFormatSymbols decimalFormatSymbol = new DecimalFormatSymbols(Locale.getDefault());

            if (!TextUtils.isEmpty(decimalSeparator)) {
                mDecimalSeparator = decimalSeparator.charAt(0);
            } else {
                mDecimalSeparator = context.getString(R.string.default_decimal_separator).charAt(0);
            }

            decimalFormatSymbol.setDecimalSeparator(mDecimalSeparator);
            mDecimalFormat.setDecimalFormatSymbols(decimalFormatSymbol);

            setAmount(mAmount);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setPadding(getMinPadding(getPaddingLeft()), getMinVerticalPadding(getPaddingTop()),
                getMinPadding(getPaddingRight()), getMinVerticalPadding(getPaddingBottom()));

        createTextFromAmount();
        calculateBounds(widthMeasureSpec, heightMeasureSpec);
        calculatePositions();
        setMeasuredDimension(mWidth, mHeight);
    }

    public void setAmount(@NonNull Number amount) {
        mAmount = amount.floatValue();
        requestLayout();
    }

    private void createTextFromAmount() {
        String formattedAmount = mDecimalFormat.format(mAmount);

        int separatorIndex = formattedAmount.lastIndexOf(mDecimalSeparator);

        if (separatorIndex > -1) {
            mIntegerSection.text = formattedAmount.substring(0, separatorIndex);
            mDecimalSection.text = formattedAmount.substring(mIncludeDecimalSeparator ? separatorIndex :
                    separatorIndex + 1);
        } else {
            mIntegerSection.text = formattedAmount;
            mDecimalSection.text = "";
        }
    }

    private void calculateBounds(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        mSymbolSection.calculateBounds(mTextPaint);
        mIntegerSection.calculateBounds(mTextPaint);
        mDecimalSection.calculateBounds(mTextPaint);

        mDecimalSection.calculateNumbersHeight(mTextPaint);
        mIntegerSection.calculateNumbersHeight(mTextPaint);


        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                mWidth = widthSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                mWidth = (int) (mIntegerSection.width + mDecimalSection.width + mSymbolSection.width
                        + mSymbolMargin + mDecimalMargin + getPaddingLeft() + getPaddingRight());
                break;
            default:
                break;
        }

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                mHeight = heightSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                mHeight = getPaddingTop() + getPaddingBottom()
                        + Math.max(mIntegerSection.height, Math.max(mDecimalSection.height, mSymbolSection.height));
                break;
            default:
                break;
        }
    }

    private void calculatePositions() {
        int symbolGravityXAxis = mSymbolGravity & GRAVITY_START;
        int symbolGravityYAxis = mSymbolGravity & GRAVITY_TOP;
        int fromY;
        int fromX;
        int width = (int) (mIntegerSection.width + mDecimalSection.width + mSymbolSection.width
                + mSymbolMargin + mDecimalMargin);

        if ((mGravity & GRAVITY_START) == GRAVITY_START) {
            fromX = getPaddingLeft();
        } else if ((mGravity & GRAVITY_END) == GRAVITY_END) {
            fromX = mWidth - width - getPaddingRight();
        } else {
            fromX = (mWidth >> 1) - (width >> 1);
        }

        if ((mGravity & GRAVITY_TOP) == GRAVITY_TOP) {
            fromY = getPaddingTop() + mIntegerSection.height;
        } else if ((mGravity & GRAVITY_BOTTOM) == GRAVITY_BOTTOM) {
            fromY = mHeight - getPaddingBottom();
        } else {
            fromY = (mHeight >> 1) + (mIntegerSection.height >> 1);
        }

        calculateY(fromY, symbolGravityYAxis);
        calculateX(fromX, symbolGravityXAxis);
    }


    private void calculateY(int from, int symbolGravityYAxis) {
        mIntegerSection.y = from;
        mSymbolSection.y = from - (symbolGravityYAxis == GRAVITY_TOP ?
                mIntegerSection.height - mSymbolSection.height + mSymbolSection.bounds.bottom : 0);
        mDecimalSection.y = from - (mDecimalGravity == GRAVITY_TOP ?
                mIntegerSection.height - mDecimalSection.height : 0);
    }

    private void calculateX(int from, int symbolGravityXAxis) {
        if (symbolGravityXAxis == GRAVITY_START) {
            mSymbolSection.x = from;
            mIntegerSection.x = (int) (mSymbolSection.x + mSymbolSection.width + mSymbolMargin);
            mDecimalSection.x = (int) (mIntegerSection.x + mIntegerSection.width + mDecimalMargin);
        } else {
            mIntegerSection.x = from;
            mDecimalSection.x = (int) (mIntegerSection.x + mIntegerSection.width + mDecimalMargin);
            mSymbolSection.x = (int) (mDecimalSection.x + mDecimalSection.width + mSymbolMargin);
        }
    }


    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);

        drawSection(canvas, mIntegerSection);
        drawSection(canvas, mDecimalSection);
        drawSection(canvas, mSymbolSection);
    }


    private void drawSection(Canvas canvas, Section section) {
        mTextPaint.setTextSize(section.textSize);
        mTextPaint.setColor(section.color);
        mTextPaint.setUnderlineText(section.drawUnderline);

        canvas.drawText(section.text, section.x - mTextPaintRoomSize * 2, section.y - mTextPaintRoomSize / 2, mTextPaint);
    }


    /// SETTERS

    public void setAmount(@NonNull Number amount, @NonNull String symbol) {
        mAmount = amount.floatValue();
        mSymbolSection.text = symbol;
        requestLayout();
    }

    public void setDecimalFormat(@NonNull DecimalFormat decimalFormat) {
        mDecimalFormat = decimalFormat;
        requestLayout();
    }

    public void setDecimalSeparator(char decimalSeparator) {
        mDecimalSeparator = decimalSeparator;
        requestLayout();
    }

    public void setSymbolMargin(float symbolMargin) {
        mSymbolMargin = symbolMargin;
        requestLayout();
    }

    public void setDecimalMargin(float decimalMargin) {
        mDecimalMargin = decimalMargin;
        requestLayout();
    }

    public void setIncludeDecimalSeparator(boolean includeDecimalSeparator) {
        mIncludeDecimalSeparator = includeDecimalSeparator;
        requestLayout();
    }

    public void setBaseTextSize(float textSize) {
        mIntegerSection.textSize = textSize;
        requestLayout();
    }

    public void setSymbol(@NonNull String symbol) {
        mSymbolSection.text = symbol;
        requestLayout();
    }

    public void setSymbolTextSize(float textSize) {
        mSymbolSection.textSize = textSize;
        requestLayout();
    }

    public void setDecimalsTextSize(float textSize) {
        mDecimalSection.textSize = textSize;
        requestLayout();
    }

    public void setBaseColor(int color) {
        mIntegerSection.color = color;
        invalidate();
    }

    public void setSymbolColor(int color) {
        mSymbolSection.color = color;
        invalidate();
    }

    public void setDecimalsColor(int color) {
        mDecimalSection.color = color;
        invalidate();
    }

    public float getAmount() {
        return mAmount;
    }

    private int getMinPadding(int padding) {
        if (padding == 0) {
            return (int) (MIN_PADDING * Resources.getSystem().getDisplayMetrics().density);
        }

        return padding;
    }

    private int getMinVerticalPadding(int padding) {

        float maxTextSize = Math.max(mIntegerSection.textSize, mDecimalSection.textSize);
        mTextPaint.setTextSize(maxTextSize);
        float maximumDistanceLowestGlyph = mTextPaint.getFontMetrics().bottom;

        if (padding < maximumDistanceLowestGlyph) {
            return (int) maximumDistanceLowestGlyph;
        }

        return padding;
    }

    private static class Section {
        public int x;
        public int y;
        public Rect bounds;
        public String text;
        public float textSize;
        public int color;
        public int width;
        public int height;
        public boolean drawUnderline;

        public Section() {
            bounds = new Rect();
        }

        public void calculateBounds(TextPaint paint) {
            paint.setTextSize(textSize);
            paint.getTextBounds(text, 0, text.length(), bounds);
            width = bounds.width();
            height = bounds.height();
        }

        public void calculateNumbersHeight(TextPaint paint) {
            String numbers = text.replaceAll("\\D", "");
            paint.setTextSize(textSize);
            paint.getTextBounds(numbers, 0, numbers.length(), bounds);
            height = bounds.height();
        }
    }

}