package com.mikepenz.materialdrawer.holder;

import android.support.annotation.DimenRes;

/**
 * Created by mikepenz on 13.07.15.
 */
public class DimenHolder extends com.mikepenz.materialize.holder.DimenHolder {
    public DimenHolder() {

    }

    public static DimenHolder fromPixel(int pixel) {
        DimenHolder dimenHolder = new DimenHolder();
        dimenHolder.setPixel(pixel);
        return dimenHolder;
    }

    public static DimenHolder fromDp(int dp) {
        DimenHolder dimenHolder = new DimenHolder();
        dimenHolder.setDp(dp);
        return dimenHolder;
    }

    public static DimenHolder fromResource(@DimenRes int resource) {
        DimenHolder dimenHolder = new DimenHolder();
        dimenHolder.setResource(resource);
        return dimenHolder;
    }
}
