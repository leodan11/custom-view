package com.github.leodan11.customview.widget.speeddial;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class ViewGroupUtils {

    private static final ThreadLocal<Matrix> MATRIX_THREAD_LOCAL = new ThreadLocal<>();
    private static final ThreadLocal<RectF> RECT_F = new ThreadLocal<>();

    private ViewGroupUtils() {
    }

    /**
     * This is a port of the common
     * {@link ViewGroup#offsetDescendantRectToMyCoords(View, Rect)}
     * from the framework, but adapted to take transformations into account. The result
     * will be the bounding rect of the real transformed rect.
     *
     * @param descendant view defining the original coordinate system of rect
     * @param rect       (in/out) the rect to offset from descendant to this view's coordinate system
     */
    static void offsetDescendantRect(ViewGroup parent, View descendant, Rect rect) {
        Matrix m = MATRIX_THREAD_LOCAL.get();
        if (m == null) {
            m = new Matrix();
            MATRIX_THREAD_LOCAL.set(m);
        } else {
            m.reset();
        }

        offsetDescendantMatrix(parent, descendant, m);

        RectF rectF = RECT_F.get();
        if (rectF == null) {
            rectF = new RectF();
            RECT_F.set(rectF);
        }
        rectF.set(rect);
        m.mapRect(rectF);
        rect.set((int) (rectF.left + 0.5f), (int) (rectF.top + 0.5f),
                (int) (rectF.right + 0.5f), (int) (rectF.bottom + 0.5f));
    }

    /**
     * Retrieve the transformed bounding rect of an arbitrary descendant view.
     * This does not need to be a direct child.
     *
     * @param descendant descendant view to reference
     * @param out        rect to set to the bounds of the descendant view
     */
    public static void getDescendantRect(ViewGroup parent, View descendant, Rect out) {
        out.set(0, 0, descendant.getWidth(), descendant.getHeight());
        offsetDescendantRect(parent, descendant, out);
    }

    private static void offsetDescendantMatrix(ViewParent target, View view, Matrix m) {
        final ViewParent parent = view.getParent();
        if (parent instanceof View vp && parent != target) {
            offsetDescendantMatrix(target, vp, m);
            m.preTranslate(-vp.getScrollX(), -vp.getScrollY());
        }

        m.preTranslate(view.getLeft(), view.getTop());

        if (!view.getMatrix().isIdentity()) {
            m.preConcat(view.getMatrix());
        }
    }

}
