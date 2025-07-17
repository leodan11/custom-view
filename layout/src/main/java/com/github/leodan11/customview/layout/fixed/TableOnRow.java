package com.github.leodan11.customview.layout.fixed;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TableOnRow extends LinearLayout {

    private ArrayList<Integer> mColumnWidths = new ArrayList<>();
    private int myWidth = 0;
    private int myHeight = 0;
    private int maxChildHeight = 0;
    private boolean preMeasured = false;

    private static final String LOG_TAG = TableOnRow.class.getSimpleName();

    public TableOnRow(Context context) {
        super(context);
        init();
    }

    public TableOnRow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TableOnRow(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public TableOnRow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        // Log.d(LOG_TAG, "init: " + Integer.toHexString(System.identityHashCode(this)) );
        // Row are always horizontal
        super.setOrientation(HORIZONTAL);
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public ArrayList<Integer> getColumnWidths() {
        return mColumnWidths;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void setColumnWidths(ArrayList<Integer> mColumnWidths) {
        this.mColumnWidths = mColumnWidths;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public int getMaxChildHeight() {
        return maxChildHeight;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void setMaxChildHeight(int maxChildHeight) {
        this.maxChildHeight = maxChildHeight;
    }

    /**
     * Changing the Orientation of this class is not supported.
     * Rows are always horizontal
     *
     * @param orientation Ignored
     * @throws RuntimeException Throws exception
     */
    @Override
    public void setOrientation(int orientation) {
        throw new UnsupportedOperationException("Setting the Orientation is not supported");
    }

    private void preMeasure() {
        // Reset stored size as we are measuring again
        myWidth = 0;
        myHeight = 0;
        // Measure UNSPECIFIED
        int measureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        final int count = getChildCount();
        for (int i = 0; i < count; ++i) {
            final View child = getChildAt(i);
            if (child == null) {
                continue;
            }
            if (child.getVisibility() == View.GONE) {
                continue;
            }

            measureChildWithMargins(child, measureSpec, 0, measureSpec, 0);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            mColumnWidths.add(childWidth);
            myWidth += childWidth;
            maxChildHeight = Math.max(maxChildHeight, childHeight);

        }

        // Add my padding
        myWidth = myWidth + getPaddingLeft() + getPaddingRight();
        myHeight = maxChildHeight + getPaddingTop() + getPaddingBottom();

        // Check against our minimum height and width
        myWidth = Math.max(myWidth, getSuggestedMinimumWidth());
        myHeight = Math.max(myHeight, getSuggestedMinimumHeight());

        setMeasuredDimension(myWidth, myHeight);

        preMeasured = true;
    }

    private void fixedMeasure() {
        // Reset stored size as we are measuring again
        myWidth = 0;
        myHeight = 0;
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxChildHeight, MeasureSpec.EXACTLY);

        final int count = getChildCount();
        for (int i = 0; i < count; ++i) {
            int widthMeasureSpec = MeasureSpec.makeMeasureSpec(mColumnWidths.get(i), MeasureSpec.EXACTLY);
            View child = getChildAt(i);
            if (child == null) {
                continue;
            }
            if (child.getVisibility() == View.GONE) {
                continue;
            }

            // Ask the child to match the parent so it fills out the whole cell
            LinearLayout.LayoutParams childLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            child.setLayoutParams(childLayoutParams);

            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

            // Calculate new row width using the width we have set each column to
            myWidth += mColumnWidths.get(i);
        }

        // Add my padding
        myWidth = myWidth + getPaddingLeft() + getPaddingRight();
        myHeight = maxChildHeight + getPaddingTop() + getPaddingBottom();

        // Check against our minimum height and width
        myWidth = Math.max(myWidth, getSuggestedMinimumWidth());
        myHeight = Math.max(myHeight, getSuggestedMinimumHeight());

        setMeasuredDimension(myWidth, myHeight);
    }

    /**
     * Measure the row
     * A row is either measured to full size of all it's children (UNSPECIFIED)
     * or measured to the size of having fixed size children to match other rows (EXACTLY)
     *
     * @param widthMeasureSpec  Ignored
     * @param heightMeasureSpec Ignored
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (preMeasured) {
            fixedMeasure();
        } else {
            preMeasure();
        }

    }

}
