package com.mikepenz.materialdrawer.holder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
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

    public int getColorInt() {
        return mColorInt;
    }

    public int getColorRes() {
        return mColorRes;
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
     * set the textColor of the ColorHolder to an drawable
     *
     * @param ctx
     * @param drawable
     */
    public void applyTo(Context ctx, GradientDrawable drawable) {
        if (mColorInt != 0) {
            drawable.setColor(mColorInt);
        } else if (mColorRes != -1) {
            drawable.setColor(ctx.getResources().getColor(mColorRes));
        }
    }


    /**
     * set the textColor of the ColorHolder to a view
     *
     * @param view
     */
    public void applyToBackground(View view) {
        if (mColorInt != 0) {
            view.setBackgroundColor(mColorInt);
        } else if (mColorRes != -1) {
            view.setBackgroundResource(mColorRes);
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
        } else if (colorDefault != null) {
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

    /**
     * a small static helper to set the color to a GradientDrawable null save
     *
     * @param colorHolder
     * @param ctx
     * @param gradientDrawable
     */
    public static void applyToOrTransparent(ColorHolder colorHolder, Context ctx, GradientDrawable gradientDrawable) {
        if (colorHolder != null && gradientDrawable != null) {
            colorHolder.applyTo(ctx, gradientDrawable);
        } else if (gradientDrawable != null) {
            gradientDrawable.setColor(Color.TRANSPARENT);
        }
    }
}
