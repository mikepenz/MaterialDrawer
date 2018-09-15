package com.mikepenz.materialdrawer.holder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.model.utils.BadgeDrawableBuilder;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.Dimension;
import androidx.annotation.DrawableRes;
import androidx.core.view.ViewCompat;

import static androidx.annotation.Dimension.DP;
import static androidx.annotation.Dimension.PX;

/**
 * Class to allow defining a BadgeStyle for the `BadgeDrawerItem`
 */
public class BadgeStyle {
    private int mGradientDrawable = R.drawable.material_drawer_badge;
    private Drawable mBadgeBackground;
    private ColorHolder mColor;
    private ColorHolder mColorPressed;
    private ColorHolder mTextColor;
    private ColorStateList mTextColorStateList;
    private DimenHolder mCorners;
    private DimenHolder mPaddingTopBottom = DimenHolder.fromDp(2); //2 looks best
    private DimenHolder mPaddingLeftRight = DimenHolder.fromDp(3); //3 looks best
    private DimenHolder mMinWidth = DimenHolder.fromDp(20); //20 looks nice

    public int getGradientDrawable() {
        return mGradientDrawable;
    }

    public BadgeStyle withGradientDrawable(@DrawableRes int gradientDrawable) {
        this.mGradientDrawable = gradientDrawable;
        this.mBadgeBackground = null;
        return this;
    }

    public Drawable getBadgeBackground() {
        return mBadgeBackground;
    }

    public BadgeStyle withBadgeBackground(Drawable badgeBackground) {
        this.mBadgeBackground = badgeBackground;
        this.mGradientDrawable = -1;
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

    public BadgeStyle withTextColorStateList(ColorStateList textColorStateList) {
        this.mTextColor = null;
        this.mTextColorStateList = textColorStateList;
        return this;
    }

    public DimenHolder getCorners() {
        return mCorners;
    }

    public BadgeStyle withCorners(@Dimension(unit = PX) int cornersPx) {
        this.mCorners = DimenHolder.fromPixel(cornersPx);
        return this;
    }

    public BadgeStyle withCornersDp(@Dimension(unit = DP) int corners) {
        this.mCorners = DimenHolder.fromDp(corners);
        return this;
    }

    public BadgeStyle withCorners(DimenHolder corners) {
        this.mCorners = corners;
        return this;
    }

    public DimenHolder getPaddingLeftRight() {
        return mPaddingLeftRight;
    }

    public BadgeStyle withPaddingLeftRightPx(@Dimension(unit = PX) int paddingLeftRight) {
        this.mPaddingLeftRight = DimenHolder.fromPixel(paddingLeftRight);
        return this;
    }

    public BadgeStyle withPaddingLeftRightDp(@Dimension(unit = DP) int paddingLeftRight) {
        this.mPaddingLeftRight = DimenHolder.fromDp(paddingLeftRight);
        return this;
    }

    public BadgeStyle withPaddingLeftRightRes(@DimenRes int paddingLeftRight) {
        this.mPaddingLeftRight = DimenHolder.fromResource(paddingLeftRight);
        return this;
    }

    public DimenHolder getPaddingTopBottom() {
        return mPaddingTopBottom;
    }

    public BadgeStyle withPaddingTopBottomPx(@Dimension(unit = PX) int paddingTopBottom) {
        this.mPaddingTopBottom = DimenHolder.fromPixel(paddingTopBottom);
        return this;
    }

    public BadgeStyle withPaddingTopBottomDp(@Dimension(unit = DP) int paddingTopBottom) {
        this.mPaddingTopBottom = DimenHolder.fromDp(paddingTopBottom);
        return this;
    }

    public BadgeStyle withPaddingTopBottomRes(@DimenRes int paddingTopBottom) {
        this.mPaddingTopBottom = DimenHolder.fromResource(paddingTopBottom);
        return this;
    }

    public BadgeStyle withPadding(@Dimension(unit = PX) int padding) {
        this.mPaddingLeftRight = DimenHolder.fromPixel(padding);
        this.mPaddingTopBottom = DimenHolder.fromPixel(padding);
        return this;
    }

    public BadgeStyle withPadding(DimenHolder padding) {
        this.mPaddingLeftRight = padding;
        this.mPaddingTopBottom = padding;
        return this;
    }

    public DimenHolder getMinWidth() {
        return mMinWidth;
    }

    public BadgeStyle withMinWidth(@Dimension(unit = PX) int minWidth) {
        this.mMinWidth = DimenHolder.fromPixel(minWidth);
        return this;
    }

    public BadgeStyle withMinWidth(DimenHolder minWidth) {
        this.mMinWidth = minWidth;
        return this;
    }

    public BadgeStyle() {
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
        if (mBadgeBackground == null) {
            ViewCompat.setBackground(badgeTextView, new BadgeDrawableBuilder(this).build(ctx));
        } else {
            ViewCompat.setBackground(badgeTextView, mBadgeBackground);
        }

        //set the badge text color
        if (mTextColor != null) {
            ColorHolder.applyToOr(mTextColor, badgeTextView, null);
        } else if (mTextColorStateList != null) {
            badgeTextView.setTextColor(mTextColorStateList);
        } else if (colorStateList != null) {
            badgeTextView.setTextColor(colorStateList);
        }

        //set the padding
        int paddingLeftRight = mPaddingLeftRight.asPixel(ctx);
        int paddingTopBottom = mPaddingTopBottom.asPixel(ctx);
        badgeTextView.setPadding(paddingLeftRight, paddingTopBottom, paddingLeftRight, paddingTopBottom);

        //set the min width
        badgeTextView.setMinWidth(mMinWidth.asPixel(ctx));
    }
}
