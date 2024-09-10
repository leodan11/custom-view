package com.github.leodan11.customview.drawable;

import static com.github.leodan11.customview.core.utils.DisplayMetrics.dipToPixels;
import static com.github.leodan11.customview.core.utils.DisplayMetrics.spToPixels;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MaterialBadgeDrawable extends Drawable {
    public static final int TYPE_NUMBER = 1;
    public static final int TYPE_ONLY_ONE_TEXT = 1 << 1;
    public static final int TYPE_WITH_TWO_TEXT = 1 << 2;
    public static final int TYPE_WITH_TWO_TEXT_COMPLEMENTARY = 1 << 3;

    @IntDef({TYPE_NUMBER, TYPE_ONLY_ONE_TEXT, TYPE_WITH_TWO_TEXT, TYPE_WITH_TWO_TEXT_COMPLEMENTARY})
    public @interface BadgeType {
    }

    private static class Config {
        private int badgeType = TYPE_NUMBER;
        private int number = 0;
        private String text1 = "";
        private String text2 = "";
        private float textSize = spToPixels(12);
        private int badgeColor = 0xffCC3333;
        private int textColor = 0xffFFFFFF;
        private Typeface typeface = Typeface.DEFAULT_BOLD;
        private float cornerRadius = dipToPixels(2);
        private float paddingLeft = dipToPixels(2);
        private float paddingTop = dipToPixels(2);
        private float paddingRight = dipToPixels(2);
        private float paddingBottom = dipToPixels(2);
        private float paddingCenter = dipToPixels(3);
        private int strokeWidth = (int) dipToPixels(1);
    }

    private final Config config;

    private final ShapeDrawable backgroundDrawable;
    private final ShapeDrawable backgroundDrawableOfText2;
    private final ShapeDrawable backgroundDrawableOfText1;
    private int badgeWidth;
    private int badgeHeight;
    private final float[] outerR = new float[]{0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
    private final float[] outerROfText1 = new float[]{0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
    private final float[] outerROfText2 = new float[]{0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
    private final Paint paint;
    private Paint.FontMetrics fontMetrics;
    private int text1Width, text2Width;

    public static class Builder {
        private final Config config;

        public Builder() {
            config = new Config();
        }

        private Builder(Config config) {
            this.config = config;
        }

        @NonNull
        public Builder type(@BadgeType int type) {
            config.badgeType = type;
            return this;
        }

        @NonNull
        public Builder number(int number) {
            config.number = number;
            return this;
        }

        @NonNull
        public Builder textOne(@Nullable String str) {
            config.text1 = str;
            return this;
        }

        @NonNull
        public Builder textTwo(@Nullable String str) {
            config.text2 = str;
            return this;
        }

        @NonNull
        public Builder textSize(float size) {
            config.textSize = size;
            return this;
        }

        @NonNull
        public Builder badgeColor(int color) {
            config.badgeColor = color;
            return this;
        }

        @NonNull
        public Builder textColor(int color) {
            config.textColor = color;
            return this;
        }

        @NonNull
        public Builder typeFace(@Nullable Typeface typeface) {
            config.typeface = typeface;
            return this;
        }

        @NonNull
        public Builder cornerRadius(float radius) {
            config.cornerRadius = radius;
            return this;
        }

        @NonNull
        public Builder padding(float value) {
            return padding(value, value, value, value, value);
        }

        @NonNull
        public Builder padding(float start, float top, float end, float bottom, float center) {
            config.paddingLeft = start;
            config.paddingTop = top;
            config.paddingRight = end;
            config.paddingBottom = bottom;
            config.paddingCenter = center;
            return this;
        }

        @NonNull
        public Builder strokeWidth(int width) {
            config.strokeWidth = width;
            return this;
        }

        @NonNull
        public MaterialBadgeDrawable build() {
            return new MaterialBadgeDrawable(config);
        }
    }

    private MaterialBadgeDrawable(Config config) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTypeface(config.typeface);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(255);

        this.config = config;

        setCornerRadius(config.cornerRadius);
        RoundRectShape shape = new RoundRectShape(outerR, null, null);
        backgroundDrawable = new ShapeDrawable(shape);
        shape = new RoundRectShape(outerROfText1, null, null);
        backgroundDrawableOfText1 = new ShapeDrawable(shape);
        shape = new RoundRectShape(outerROfText2, null, null);
        backgroundDrawableOfText2 = new ShapeDrawable(shape);

        setTextSize(config.textSize);
        measureBadge();
    }

    @NonNull
    public Builder buildUpon() {
        return new Builder(config);
    }

    public void setBadgeType(@BadgeType int type) {
        config.badgeType = type;
        measureBadge();
    }

    @BadgeType
    public int getBadgeType() {
        return config.badgeType;
    }

    public void setNumber(int number) {
        config.number = number;
    }

    public int getNumber() {
        return config.number;
    }

    public void setTextOne(@Nullable String str) {
        config.text1 = str;
        measureBadge();
    }

    @Nullable
    public String getTextOne() {
        return config.text1;
    }

    public void setTextTwo(@Nullable String str) {
        config.text2 = str;
        measureBadge();
    }

    @Nullable
    public String getTextTwo() {
        return config.text2;
    }

    public void setTextSize(float textSize) {
        config.textSize = textSize;
        paint.setTextSize(textSize);
        fontMetrics = paint.getFontMetrics();

        measureBadge();
    }

    public float getTextSize() {
        return config.textSize;
    }

    public void setBadgeColor(int color) {
        config.badgeColor = color;
    }

    public int getBadgeColor() {
        return config.badgeColor;
    }

    public void setTextColor(int color) {
        config.textColor = color;
    }

    public int getTextColor() {
        return config.textColor;
    }

    public void setTypeFace(@Nullable Typeface typeface) {
        config.typeface = typeface;
        paint.setTypeface(config.typeface);
    }

    @Nullable
    public Typeface getTypeFace() {
        return config.typeface;
    }

    public void setCornerRadius(float radius) {
        config.cornerRadius = radius;
        outerR[0] = outerR[1] = outerR[2] = outerR[3] =
                outerR[4] = outerR[5] = outerR[6] = outerR[7] = radius;

        outerROfText1[0] = outerROfText1[1] = outerROfText1[6] = outerROfText1[7] = radius;
        outerROfText1[2] = outerROfText1[3] = outerROfText1[4] = outerROfText1[5] = 0f;

        outerROfText2[0] = outerROfText2[1] = outerROfText2[6] = outerROfText2[7] = 0f;
        outerROfText2[2] = outerROfText2[3] = outerROfText2[4] = outerROfText2[5] = radius;
    }

    public float getCornerRadius() {
        return config.cornerRadius;
    }

    public void setPadding(float l, float t, float r, float b, float c) {
        config.paddingLeft = l;
        config.paddingTop = t;
        config.paddingRight = r;
        config.paddingBottom = b;
        config.paddingCenter = c;
        measureBadge();
    }

    public void setPaddingLeft(float l) {
        config.paddingLeft = l;
        measureBadge();
    }

    public float getPaddingLeft() {
        return config.paddingLeft;
    }

    public void setPaddingTop(float t) {
        config.paddingTop = t;
        measureBadge();
    }

    public float getPaddingTop() {
        return config.paddingTop;
    }

    public void setPaddingRight(float r) {
        config.paddingRight = r;
        measureBadge();
    }

    public float getPaddingRight() {
        return config.paddingRight;
    }

    public void setPaddingBottom(float b) {
        config.paddingBottom = b;
        measureBadge();
    }

    public float getPaddingBottom() {
        return config.paddingBottom;
    }

    public void setPaddingCenter(float c) {
        config.paddingCenter = c;
        measureBadge();
    }

    public float getPaddingCenter() {
        return config.paddingCenter;
    }

    public void setStrokeWidth(int width) {
        config.strokeWidth = width;
    }

    public int getStrokeWidth() {
        return config.strokeWidth;
    }

    private void measureBadge() {
        badgeHeight = (int) (getTextSize() + getPaddingTop() + getPaddingBottom());

        String text1 = getTextOne(), text2 = getTextTwo();
        text1 = (text1 != null ? text1 : "");
        text2 = (text2 != null ? text2 : "");

        switch (getBadgeType()) {
            case TYPE_ONLY_ONE_TEXT -> {
                text1Width = (int) paint.measureText(text1);
                badgeWidth = (int) (text1Width + getPaddingLeft() + getPaddingRight());
                setCornerRadius(getCornerRadius());
            }
            case TYPE_WITH_TWO_TEXT, TYPE_WITH_TWO_TEXT_COMPLEMENTARY -> {
                text1Width = (int) paint.measureText(text1);
                text2Width = (int) paint.measureText(text2);
                badgeWidth = (int) (text1Width + text2Width +
                        getPaddingLeft() + getPaddingRight() + getPaddingCenter());
                setCornerRadius(getCornerRadius());
            }
            default -> {
                badgeWidth = (int) (getTextSize() + getPaddingLeft() + getPaddingRight());
                setCornerRadius(badgeHeight);
            }
        }

        int boundsWidth = getBounds().width();
        if (boundsWidth > 0) {
            // If the bounds have been set, adjust the badge size
            switch (getBadgeType()) {
                case TYPE_ONLY_ONE_TEXT -> {
                    if (boundsWidth < badgeWidth) {
                        text1Width = (int) (boundsWidth - getPaddingLeft() - getPaddingRight());
                        text1Width = Math.max(text1Width, 0);

                        badgeWidth = boundsWidth;
                    }
                }
                case TYPE_WITH_TWO_TEXT, TYPE_WITH_TWO_TEXT_COMPLEMENTARY -> {
                    if (boundsWidth < badgeWidth) {
                        if (boundsWidth < (text1Width + getPaddingLeft() + getPaddingRight())) {
                            text1Width = (int) (boundsWidth - getPaddingLeft() - getPaddingRight());
                            text1Width = Math.max(text1Width, 0);
                            text2Width = 0;

                        } else {
                            text2Width = (int) (boundsWidth - text1Width -
                                    getPaddingLeft() - getPaddingRight() - getPaddingCenter());
                            text2Width = Math.max(text2Width, 0);
                        }

                        badgeWidth = boundsWidth;
                    }
                }
                default -> {
                }
            }
        }
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        measureBadge();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bounds = getBounds();

        int marginTopAndBottom = (int) ((bounds.height() - badgeHeight) / 2f);
        int marginLeftAndRight = (int) ((bounds.width() - badgeWidth) / 2f);

        backgroundDrawable.setBounds(
                bounds.left + marginLeftAndRight,
                bounds.top + marginTopAndBottom,
                bounds.right - marginLeftAndRight,
                bounds.bottom - marginTopAndBottom);
        backgroundDrawable.getPaint().setColor(getBadgeColor());
        backgroundDrawable.draw(canvas);

        float textCx = bounds.centerX();
        float textCy = bounds.centerY() - (fontMetrics.bottom + fontMetrics.top) / 2f;

        String text1 = getTextOne(), text2 = getTextTwo();
        text1 = (text1 != null ? text1 : "");
        text2 = (text2 != null ? text2 : "");

        switch (getBadgeType()) {
            case TYPE_ONLY_ONE_TEXT -> {
                paint.setColor(getTextColor());
                canvas.drawText(
                        cutText(text1, text1Width),
                        textCx,
                        textCy,
                        paint);
            }
            case TYPE_WITH_TWO_TEXT_COMPLEMENTARY -> {
                paint.setColor(getTextColor());
                canvas.drawText(
                        text1,
                        marginLeftAndRight + getPaddingLeft() + text1Width / 2f,
                        textCy,
                        paint);
                backgroundDrawableOfText2.setBounds(
                        (int) (bounds.left + marginLeftAndRight + getPaddingLeft() +
                                text1Width + getPaddingCenter() / 2f),
                        bounds.top + marginTopAndBottom + getStrokeWidth(),
                        bounds.width() - marginLeftAndRight - getStrokeWidth(),
                        bounds.bottom - marginTopAndBottom - getStrokeWidth());
                backgroundDrawableOfText2.getPaint().setColor(getTextColor());
                backgroundDrawableOfText2.draw(canvas);
                paint.setColor(getBadgeColor());
                canvas.drawText(
                        cutText(text2, text2Width),
                        bounds.width() - marginLeftAndRight - getPaddingRight() - text2Width / 2f,
                        textCy,
                        paint);
            }
            case TYPE_WITH_TWO_TEXT -> {
                backgroundDrawableOfText1.setBounds(
                        bounds.left + marginLeftAndRight + getStrokeWidth(),
                        bounds.top + marginTopAndBottom + getStrokeWidth(),
                        (int) (bounds.left + marginLeftAndRight + getPaddingLeft() +
                                text1Width + getPaddingCenter() / 2f - getStrokeWidth() / 2f),
                        bounds.bottom - marginTopAndBottom - getStrokeWidth());
                backgroundDrawableOfText1.getPaint().setColor(0xffFFFFFF);
                backgroundDrawableOfText1.draw(canvas);
                paint.setColor(getBadgeColor());
                canvas.drawText(
                        text1,
                        text1Width / 2f + marginLeftAndRight + getPaddingLeft(),
                        textCy,
                        paint);
                backgroundDrawableOfText2.setBounds(
                        (int) (bounds.left + marginLeftAndRight + getPaddingLeft() +
                                text1Width + getPaddingCenter() / 2f + getStrokeWidth() / 2f),
                        bounds.top + marginTopAndBottom + getStrokeWidth(),
                        bounds.width() - marginLeftAndRight - getStrokeWidth(),
                        bounds.bottom - marginTopAndBottom - getStrokeWidth());
                backgroundDrawableOfText2.getPaint().setColor(0xffFFFFFF);
                backgroundDrawableOfText2.draw(canvas);
                paint.setColor(getBadgeColor());
                canvas.drawText(
                        cutText(text2, text2Width),
                        bounds.width() - marginLeftAndRight - getPaddingRight() - text2Width / 2f,
                        textCy,
                        paint);
            }
            default -> {
                paint.setColor(getTextColor());
                canvas.drawText(
                        cutNumber(getNumber(), badgeWidth),
                        textCx,
                        textCy,
                        paint);
            }
        }
    }

    @Override
    public int getIntrinsicWidth() {
        return badgeWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return badgeHeight;
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    private String cutNumber(int number, int width) {
        String text = String.valueOf(number);
        if (paint.measureText(text) < width)
            return text;

        return "…";
    }

    private String cutText(String text, int width) {
        if (paint.measureText(text) <= width)
            return text;

        String suffix = "...";
        while (paint.measureText(text + suffix) > width) {
            if (!text.isEmpty())
                text = text.substring(0, text.length() - 1);

            if (text.isEmpty()) {
                suffix = suffix.substring(0, suffix.length() - 1);

                if (suffix.isEmpty()) break;
            }
        }

        return text + suffix;
    }

    public SpannableString toSpannable() {
        final SpannableString spanStr = new SpannableString(" ");
        spanStr.setSpan(new ImageSpan(this, ImageSpan.ALIGN_BOTTOM), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setBounds(0, 0, getIntrinsicWidth(), getIntrinsicHeight());

        return spanStr;
    }

}