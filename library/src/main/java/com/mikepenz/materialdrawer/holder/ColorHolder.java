package com.mikepenz.materialdrawer.holder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.widget.TextView;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialize.util.UIUtils;

/**
 * Created by mikepenz on 13.07.15.
 */
public class ColorHolder {
    private int mColorInt = 0;
    private int mColorRes = -1;

    public ColorHolder() {

    }

    public static ColorHolder fromColorRes(int colorRes) {
        ColorHolder colorHolder = new ColorHolder();
        colorHolder.mColorRes = colorRes;
        return colorHolder;
    }

    public static ColorHolder fromColor(int colorInt) {
        ColorHolder colorHolder = new ColorHolder();
        colorHolder.mColorInt = colorInt;
        return colorHolder;
    }

    /**
     * set the textColor of the ColorHolder to an iconicsDrawable
     *
     * @param iconicsDrawable
     */
    public void applyTo(IconicsDrawable iconicsDrawable) {
        if (mColorInt != 0) {
            iconicsDrawable.color(mColorInt);
        } else if (mColorRes != -1) {
            iconicsDrawable.colorRes(mColorRes);
        }
    }

    /**
     * a small helper to set the text color to a textView null save
     *
     * @param textView
     * @param colorDefault
     */
    public void applyToOr(TextView textView, ColorStateList colorDefault) {
        if (mColorInt != 0) {
            textView.setTextColor(mColorInt);
        } else if (mColorRes != -1) {
            textView.setTextColor(textView.getContext().getResources().getColor(mColorRes));
        } else {
            textView.setTextColor(colorDefault);
        }
    }

    /**
     * a small helper class to get the color from the colorHolder or from the theme or from the default color value
     *
     * @param ctx
     * @param colorStyle
     * @param colorDefault
     * @return
     */
    public int color(Context ctx, int colorStyle, int colorDefault) {
        if (mColorInt == 0 && mColorRes != -1) {
            mColorInt = ctx.getResources().getColor(mColorRes);
        } else if (mColorInt == 0) {
            mColorInt = UIUtils.getThemeColorFromAttrOrRes(ctx, colorStyle, colorDefault);
        }
        return mColorInt;
    }

    /**
     * a small static helper class to get the color from the colorHolder or from the theme or from the default color value
     *
     * @param colorHolder
     * @param ctx
     * @param colorStyle
     * @param colorDefault
     * @return
     */
    public static int color(ColorHolder colorHolder, Context ctx, int colorStyle, int colorDefault) {
        if (colorHolder == null) {
            return UIUtils.getThemeColorFromAttrOrRes(ctx, colorStyle, colorDefault);
        } else {
            return colorHolder.color(ctx, colorStyle, colorDefault);
        }
    }

    /**
     * a small static helper to set the text color to a textView null save
     *
     * @param colorHolder
     * @param textView
     * @param colorDefault
     */
    public static void applyToOr(ColorHolder colorHolder, TextView textView, ColorStateList colorDefault) {
        if (colorHolder != null && textView != null) {
            colorHolder.applyToOr(textView, colorDefault);
        } else if (textView != null) {
            textView.setTextColor(colorDefault);
        }
    }
}
