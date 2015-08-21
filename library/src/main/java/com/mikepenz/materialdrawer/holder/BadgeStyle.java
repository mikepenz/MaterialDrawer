package com.mikepenz.materialdrawer.holder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.widget.TextView;

import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.model.utils.BadgeDrawableBuilder;
import com.mikepenz.materialize.util.UIUtils;

/**
 * Created by mikepenz on 02.07.15.
 */
public class BadgeStyle {
    private int mGradientDrawable = R.drawable.material_drawer_badge;
    private ColorHolder mColor;
    private ColorHolder mColorPressed;
    private ColorHolder mTextColor;
    private DimenHolder mCorners;
    private DimenHolder mPadding = DimenHolder.fromDp(2); //2 looks best
    private DimenHolder mMinWidth = DimenHolder.fromDp(20); //20 looks nice

    public int getGradientDrawable() {
        return mGradientDrawable;
    }

    public BadgeStyle withGradientDrawable(@DrawableRes int gradientDrawable) {
        this.mGradientDrawable = gradientDrawable;
        return this;
    }

    public ColorHolder getColor() {
        return mColor;
    }

    public BadgeStyle withColor(@ColorInt int color) {
        this.mColor = ColorHolder.fromColor(color);
        return this;
    }

    public BadgeStyle withColorRes(@ColorRes int color) {
        this.mColor = ColorHolder.fromColorRes(color);
        return this;
    }

    public ColorHolder getColorPressed() {
        return mColorPressed;
    }

    public BadgeStyle withColorPressed(@ColorInt int colorPressed) {
        this.mColorPressed = ColorHolder.fromColor(colorPressed);
        return this;
    }

    public BadgeStyle withColorPressedRes(@ColorRes int colorPressed) {
        this.mColorPressed = ColorHolder.fromColorRes(colorPressed);
        return this;
    }

    public ColorHolder getTextColor() {
        return mTextColor;
    }

    public BadgeStyle withTextColor(@ColorInt int textColor) {
        this.mTextColor = ColorHolder.fromColor(textColor);
        return this;
    }

    public BadgeStyle withTextColorRes(@ColorRes int textColor) {
        this.mTextColor = ColorHolder.fromColorRes(textColor);
        return this;
    }

    public DimenHolder getCorners() {
        return mCorners;
    }

    public BadgeStyle withCorners(int corners) {
        this.mCorners = DimenHolder.fromPixel(corners);
        return this;
    }

    public DimenHolder getPadding() {
        return mPadding;
    }

    public BadgeStyle withPadding(int padding) {
        this.mPadding = DimenHolder.fromPixel(padding);
        return this;
    }

    public BadgeStyle() {

    }

    public DimenHolder getMinWidth() {
        return mMinWidth;
    }

    public BadgeStyle withMinWidth(int minWidth) {
        this.mMinWidth = DimenHolder.fromPixel(minWidth);
        return this;
    }

    public BadgeStyle(@ColorInt int color, @ColorInt int colorPressed) {
        this.mColor = ColorHolder.fromColor(color);
        this.mColorPressed = ColorHolder.fromColor(colorPressed);
    }

    public BadgeStyle(@DrawableRes int gradientDrawable, @ColorInt int color, @ColorInt int colorPressed, @ColorInt int textColor) {
        this.mGradientDrawable = gradientDrawable;
        this.mColor = ColorHolder.fromColor(color);
        this.mColorPressed = ColorHolder.fromColor(colorPressed);
        this.mTextColor = ColorHolder.fromColor(textColor);
    }

    public void style(TextView badgeTextView) {
        style(badgeTextView, null);
    }

    public void style(TextView badgeTextView, ColorStateList colorStateList) {
        Context ctx = badgeTextView.getContext();
        //set background for badge
        UIUtils.setBackground(badgeTextView, new BadgeDrawableBuilder(this).build(ctx));

        //set the badge text color
        if (mTextColor != null) {
            ColorHolder.applyToOr(mTextColor, badgeTextView, null);
        } else if (colorStateList != null) {
            badgeTextView.setTextColor(colorStateList);
        }

        //set the padding
        int padding = mPadding.asPixel(ctx);
        badgeTextView.setPadding(badgeTextView.getPaddingLeft() + padding, badgeTextView.getPaddingTop() + padding, badgeTextView.getPaddingRight() + padding, badgeTextView.getPaddingBottom() + padding);

        //set the min width
        badgeTextView.setMinWidth(mMinWidth.asPixel(ctx));
    }
}
