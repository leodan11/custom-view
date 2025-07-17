package com.github.leodan11.customview.layout.fixed;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class SubTableLayout extends LinearLayout {

    private static final String LOG_TAG = SubTableLayout.class.getSimpleName();

    public SubTableLayout(Context context) {
        super(context);
        init();
    }

    public SubTableLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SubTableLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public SubTableLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        // Row are always vertical
        super.setOrientation(LinearLayout.VERTICAL);
        // set background to be white as transparent shows the main table panning underneath the others
        setBackgroundColor(Color.WHITE);
    }

    /**
     * Changing the Orientation of this class is not supported.
     * Tables are always vertical
     *
     * @param orientation Ignored
     * @throws RuntimeException Throws exception
     */
    @Override
    public void setOrientation(int orientation) throws RuntimeException {
        throw new RuntimeException();
    }

    /**
     * Adds only FixedHeaderTableRow child views
     *
     * @param child  the child view to add
     * @param index  the position at which to add the child
     * @param params the layout parameters to set on the child
     * @throws RuntimeException Throws exception if no a FixedHeaderTableRow child
     */
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof TableOnRow) {
            super.addView(child, index, params);
        } else {
            throw new UnsupportedOperationException("Adding non FixedHeaderTableRow children is not supported");
        }
    }

    /**
     * Measure the Table
     * The Table is always measure at (UNSPECIFIED) so the full table is drawn
     * so that Pan and Scale work
     *
     * @param widthMeasureSpec  Ignored
     * @param heightMeasureSpec Ignored
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Always measure the tables to full size so pan and Scale works
        int measureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        super.onMeasure(measureSpec, measureSpec);
    }
}
