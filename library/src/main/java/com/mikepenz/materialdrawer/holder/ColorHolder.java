package com.mikepenz.materialdrawer.holder;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;

/**
 * Created by mikepenz on 13.07.15.
 */
public class ColorHolder extends com.mikepenz.materialize.holder.ColorHolder {
    public ColorHolder() {
    }

    public static ColorHolder fromColorRes(@ColorRes int colorRes) {
        ColorHolder colorHolder = new ColorHolder();
        colorHolder.setColorRes(colorRes);
        return colorHolder;
    }

    public static ColorHolder fromColor(@ColorInt int colorInt) {
        ColorHolder colorHolder = new ColorHolder();
        colorHolder.setColorInt(colorInt);
        return colorHolder;
    }

}
