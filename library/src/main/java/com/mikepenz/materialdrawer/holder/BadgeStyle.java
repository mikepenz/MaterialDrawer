package com.mikepenz.materialdrawer.holder;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.model.utils.BadgeDrawableBuilder;
import com.mikepenz.materialize.util.UIUtils;

/**
 * Created by mikepenz on 02.07.15.
 */
public class BadgeStyle {
    private int mGradientDrawable = R.drawable.material_drawer_badge;
    private ColorHolder mColor = ColorHolder.fromColor(Color.RED);
    private ColorHolder mColorPressed = ColorHolder.fromColor(Color.GREEN);
    private ColorHolder mTextColor = ColorHolder.fromColor(Color.WHITE);
    private DimenHolder mCorners;
    private DimenHolder mPadding = DimenHolder.fromDp(2); //2 looks best
    private DimenHolder mMinWidth = DimenHolder.fromDp(20); //20 looks nice

    public int getGradientDrawable() {
        return mGradientDrawable;
    }

    public BadgeStyle withGradientDrawable(int gradientDrawable) {
        this.mGradientDrawable = gradientDrawable;
        return this;
    }

    public ColorHolder getColor() {
        return mColor;
    }

    public BadgeStyle withColor(int color) {
        this.mColor = ColorHolder.fromColor(color);
        return this;
    }

    public ColorHolder getColorPressed() {
        return mColorPressed;
    }

    public BadgeStyle withColorPressed(int colorPressed) {
        this.mColorPressed = ColorHolder.fromColor(colorPressed);
        return this;
    }

    public ColorHolder getTextColor() {
        return mTextColor;
    }

    public BadgeStyle withTextColor(int textColor) {
        this.mTextColor = ColorHolder.fromColor(textColor);
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

    public BadgeStyle(int color, int colorPressed) {
        this.mColor = ColorHolder.fromColor(color);
        this.mColorPressed = ColorHolder.fromColor(colorPressed);
    }

    public BadgeStyle(int gradientDrawable, int color, int colorPressed, int textColor) {
        this.mGradientDrawable = gradientDrawable;
        this.mColor = ColorHolder.fromColor(color);
        this.mColorPressed = ColorHolder.fromColor(colorPressed);
        this.mTextColor = ColorHolder.fromColor(textColor);
    }

    public void style(TextView badgeTextView) {
        Context ctx = badgeTextView.getContext();
        //set background for badge
        UIUtils.setBackground(badgeTextView, new BadgeDrawableBuilder(this).build(ctx));

        //set the badge text color
        ColorHolder.applyToOr(mTextColor, badgeTextView, null);

        //set the padding
        int padding = mPadding.asPixel(ctx);
        badgeTextView.setPadding(padding, padding, padding, padding);

        //set the min width
        badgeTextView.setMinWidth(mMinWidth.asPixel(ctx));
    }
}
