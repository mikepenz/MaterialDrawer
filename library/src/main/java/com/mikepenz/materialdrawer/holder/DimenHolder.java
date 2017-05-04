package com.mikepenz.materialdrawer.holder;

import android.support.annotation.DimenRes;
import android.support.annotation.Dimension;

import static android.support.annotation.Dimension.DP;
import static android.support.annotation.Dimension.PX;

/**
 * Created by mikepenz on 13.07.15.
 */
public class DimenHolder extends com.mikepenz.materialize.holder.DimenHolder {
    public DimenHolder() {

    }

    public static DimenHolder fromPixel(@Dimension(unit = PX) int pixel) {
        DimenHolder dimenHolder = new DimenHolder();
        dimenHolder.setPixel(pixel);
        return dimenHolder;
    }

    public static DimenHolder fromDp(@Dimension(unit = DP) int dp) {
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
