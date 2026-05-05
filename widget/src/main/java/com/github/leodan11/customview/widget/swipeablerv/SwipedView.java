package com.github.leodan11.customview.widget.swipeablerv;

import androidx.annotation.NonNull;

import java.util.Arrays;


public class SwipedView {
    private int[] mIcons;
    private int[] mBackgrounds;
    private String[] mTexts;
    private int mTextColor;
    private int mTextSize;
    private float mCornerRadius;
    private float mLeftCornerRadius = -1f;
    private float mRightCornerRadius = -1f;
    private boolean mIsRTL;
    private boolean mShouldSupportRTL;
    private boolean mShouldForceRTL;

    public SwipedView() {
        mIsRTL = RTL.isRTL();
    }

    /**
     * Represents the child to draw in {@link SwipeLeftRightCallback}
     *
     * @param icons       - must contain 2 icons - [0] - left, [1] - right
     * @param texts       - must contain 2 strings - [0] - left, [1] - right
     * @param backgrounds - must contain 2 backgrounds int - [0] - left, [1] - right
     *                    - assign null/-1 for unwanted side
     */
    public SwipedView(int[] icons, int[] backgrounds, String[] texts) {
        mIcons = icons;
        mBackgrounds = backgrounds;
        mTexts = texts;
    }

    public int getLeftIcon() {
        if (shouldShowRTL())
            return mIcons[1];
        return mIcons[0];
    }

    public int getRightIcon() {
        if (shouldShowRTL())
            return mIcons[0];
        return mIcons[1];
    }

    public int getLeftBg() {
        if (shouldShowRTL())
            return mBackgrounds[1];
        return mBackgrounds[0];
    }

    public int getRightBg() {
        if (shouldShowRTL())
            return mBackgrounds[0];
        return mBackgrounds[1];
    }

    public String getLeftText() {
        if (shouldShowRTL())
            return mTexts[1] == null ? "" : mTexts[1];
        return mTexts[0] == null ? "" : mTexts[0];
    }

    public String getRightText() {
        if (shouldShowRTL())
            return mTexts[0] == null ? "" : mTexts[0];
        return mTexts[1] == null ? "" : mTexts[1];
    }

    float getCornerRadius() {
        return mCornerRadius;
    }

    public float getLeftCornerRadius() {
        if (shouldShowRTL())
            return mRightCornerRadius;
        return mLeftCornerRadius;
    }

    public float getRightCornerRadius() {
        if (shouldShowRTL())
            return mLeftCornerRadius;
        return mRightCornerRadius;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public int getTextSize() {
        return mTextSize;
    }


    public void setCornerRadius(float cornerRadius) {
        mCornerRadius = cornerRadius;
    }

    public void setLeftCornerRadius(float leftCornerRadius) {
        mLeftCornerRadius = leftCornerRadius;
    }

    public void setRightCornerRadius(float rightCornerRadius) {
        mRightCornerRadius = rightCornerRadius;
    }

    public void setIcons(int[] icons) {
        mIcons = icons;
    }

    public void setBackgrounds(int[] backgrounds) {
        mBackgrounds = backgrounds;
    }

    public void setTexts(String[] texts) {
        mTexts = texts;
    }

    public void setTextColor(int color) {
        mTextColor = color;
    }

    public void setTextSize(int size) {
        mTextSize = size;
    }

    public void setShouldSupportRTL(boolean shouldSupportRTL) {
        mShouldSupportRTL = shouldSupportRTL;
    }

    public void setShouldForceRTL(boolean shouldForceRTL) {
        mShouldForceRTL = shouldForceRTL;
    }

    public boolean shouldShowRTL() {
        return (mIsRTL && mShouldSupportRTL) || mShouldForceRTL;
    }

    @NonNull
    @Override
    public String toString() {
        return "SwipedView{" +
                "mIcons=" + Arrays.toString(mIcons) +
                ", mBackgrounds=" + Arrays.toString(mBackgrounds) +
                ", mTexts=" + Arrays.toString(mTexts) +
                ", mTextColor=" + mTextColor +
                ", mTextSize=" + mTextSize +
                ", mCornerRadius=" + mCornerRadius +
                ", mLeftCornerRadius=" + mLeftCornerRadius +
                ", mRightCornerRadius=" + mRightCornerRadius +
                ", mIsRTL=" + mIsRTL +
                ", mShouldSupportRTL=" + mShouldSupportRTL +
                ", mShouldForceRTL=" + mShouldForceRTL +
                '}';
    }
}
