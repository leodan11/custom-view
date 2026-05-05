package com.github.leodan11.customview.widget.swipeablerv;

import java.util.Locale;

public final class RTL {

    public static boolean isRTL() {
        return isRTL(null);
    }

    public static boolean isRTL(Locale locale) {
        String displayName = (locale != null) ? locale.getDisplayName() : Locale.getDefault().getDisplayName();
        final int directionality = Character.getDirectionality(displayName.charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT || directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }

}
