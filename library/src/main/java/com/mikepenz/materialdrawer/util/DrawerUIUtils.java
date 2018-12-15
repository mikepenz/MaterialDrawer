package com.mikepenz.materialdrawer.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.StyleableRes;
import androidx.core.view.ViewCompat;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.icons.MaterialDrawerFont;
import com.mikepenz.materialize.util.UIUtils;

/**
 * Created by mikepenz on 15.03.14.
 */
@SuppressLint("InlinedApi")
public class DrawerUIUtils {

    /**
     * Get the boolean value of a given styleable.
     *
     * @param ctx
     * @param styleable
     * @param def
     * @return
     */
    public static boolean getBooleanStyleable(Context ctx, @StyleableRes int styleable, boolean def) {
        TypedArray ta = ctx.getTheme().obtainStyledAttributes(R.styleable.MaterialDrawer);
        return ta.getBoolean(styleable, def);
    }

    /**
     * Util method to theme the drawer item view's background (and foreground if possible)
     *
     * @param ctx            the context to use
     * @param view           the view to theme
     * @param selected_color the selected color to use
     * @param animate        true if we want to animate the StateListDrawable
     */
    public static void themeDrawerItem(Context ctx, View view, int selected_color, boolean animate) {
        boolean legacyStyle = getBooleanStyleable(ctx, R.styleable.MaterialDrawer_material_drawer_legacy_style, false);

        Drawable selected;
        Drawable unselected;

        if (legacyStyle) {
            // Material 1.0 styling
            selected = new ColorDrawable(selected_color);
            unselected = UIUtils.getSelectableBackground(ctx);
        } else {
            // Material 2.0 styling
            int cornerRadius = ctx.getResources().getDimensionPixelSize(R.dimen.material_drawer_item_corner_radius);
            int paddingTopBottom = ctx.getResources().getDimensionPixelSize(R.dimen.material_drawer_item_background_padding_top_bottom);
            int paddingStartEnd = ctx.getResources().getDimensionPixelSize(R.dimen.material_drawer_item_background_padding_start_end);

            // define normal selected background
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(selected_color);
            gradientDrawable.setCornerRadius(cornerRadius);
            selected = new InsetDrawable(gradientDrawable, paddingStartEnd, paddingTopBottom, paddingStartEnd, paddingTopBottom);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // define mask for ripple
                GradientDrawable gradientMask = new GradientDrawable();
                gradientMask.setColor(Color.BLACK);
                gradientMask.setCornerRadius(cornerRadius);
                Drawable mask = new InsetDrawable(gradientMask, paddingStartEnd, paddingTopBottom, paddingStartEnd, paddingTopBottom);

                unselected = new RippleDrawable(new ColorStateList(new int[][]{new int[]{}}, new int[]{UIUtils.getThemeColor(ctx, R.attr.colorControlHighlight)}), null, mask);
            } else {
                // define touch drawable
                GradientDrawable touchDrawable = new GradientDrawable();
                touchDrawable.setColor(UIUtils.getThemeColor(ctx, R.attr.colorControlHighlight));
                touchDrawable.setCornerRadius(cornerRadius);
                Drawable touchInsetDrawable = new InsetDrawable(touchDrawable, paddingStartEnd, paddingTopBottom, paddingStartEnd, paddingTopBottom);

                StateListDrawable unselectedStates = new StateListDrawable();
                //if possible and wanted we enable animating across states
                if (animate) {
                    int duration = ctx.getResources().getInteger(android.R.integer.config_shortAnimTime);
                    unselectedStates.setEnterFadeDuration(duration);
                    unselectedStates.setExitFadeDuration(duration);
                }
                unselectedStates.addState(new int[]{android.R.attr.state_pressed}, touchInsetDrawable);
                unselectedStates.addState(new int[]{}, new ColorDrawable(Color.TRANSPARENT));
                unselected = unselectedStates;
            }
        }

        StateListDrawable states = new StateListDrawable();

        //if possible and wanted we enable animating across states
        if (animate) {
            int duration = ctx.getResources().getInteger(android.R.integer.config_shortAnimTime);
            states.setEnterFadeDuration(duration);
            states.setExitFadeDuration(duration);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            states.addState(new int[]{android.R.attr.state_selected}, selected);
            states.addState(new int[]{}, new ColorDrawable(Color.TRANSPARENT));

            ViewCompat.setBackground(view, states);
            view.setForeground(unselected);
        } else {
            states.addState(new int[]{android.R.attr.state_selected}, selected);
            states.addState(new int[]{}, unselected);

            ViewCompat.setBackground(view, states);
        }
    }

    /**
     * helper to create a colorStateList for the text
     *
     * @param text_color
     * @param selected_text_color
     * @return
     */
    public static ColorStateList getTextColorStateList(int text_color, int selected_text_color) {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_selected},
                        new int[]{}
                },
                new int[]{
                        selected_text_color,
                        text_color
                }
        );
    }

    /**
     * helper to create a stateListDrawable for the icon
     *
     * @param icon
     * @param selectedIcon
     * @return
     */
    public static StateListDrawable getIconStateList(Drawable icon, Drawable selectedIcon) {
        StateListDrawable iconStateListDrawable = new StateListDrawable();
        iconStateListDrawable.addState(new int[]{android.R.attr.state_selected}, selectedIcon);
        iconStateListDrawable.addState(new int[]{}, icon);
        return iconStateListDrawable;
    }

    /**
     * helper to create a StateListDrawable for the drawer item background
     *
     * @param selected_color
     * @return
     */
    public static StateListDrawable getDrawerItemBackground(int selected_color) {
        ColorDrawable clrActive = new ColorDrawable(selected_color);
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_selected}, clrActive);
        return states;
    }

    /**
     * helper to calculate the optimal drawer width
     *
     * @param context
     * @return
     */
    public static int getOptimalDrawerWidth(Context context) {
        int possibleMinDrawerWidth = UIUtils.getScreenWidth(context) - UIUtils.getActionBarHeight(context);
        int maxDrawerWidth = context.getResources().getDimensionPixelSize(R.dimen.material_drawer_width);
        return Math.min(possibleMinDrawerWidth, maxDrawerWidth);
    }


    /**
     * helper method to get a person placeHolder drawable
     *
     * @param ctx
     * @return
     */
    public static Drawable getPlaceHolder(Context ctx) {
        return new IconicsDrawable(ctx, MaterialDrawerFont.Icon.mdf_person).colorRes(R.color.accent).backgroundColorRes(R.color.primary).sizeDp(56).paddingDp(16);
    }

    /**
     * helper to set the vertical padding to the DrawerItems
     * this is required because on API Level 17 the padding is ignored which is set via the XML
     *
     * @param v
     */
    public static void setDrawerVerticalPadding(View v) {
        int verticalPadding = v.getContext().getResources().getDimensionPixelSize(R.dimen.material_drawer_vertical_padding);
        v.setPadding(verticalPadding, 0, verticalPadding, 0);
    }

    /**
     * helper to set the vertical padding including the extra padding for deeper item hirachy level to the DrawerItems
     * this is required because on API Level 17 the padding is ignored which is set via the XML
     *
     * @param v
     * @param level
     */
    public static void setDrawerVerticalPadding(View v, int level) {
        int verticalPadding = v.getContext().getResources().getDimensionPixelSize(R.dimen.material_drawer_vertical_padding);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            v.setPaddingRelative(verticalPadding * level, 0, verticalPadding, 0);
        } else {
            v.setPadding(verticalPadding * level, 0, verticalPadding, 0);
        }
    }

    /**
     * helper to check if the system bar is on the bottom of the screen
     *
     * @param ctx
     * @return
     */
    public static boolean isSystemBarOnBottom(Context ctx) {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        Configuration cfg = ctx.getResources().getConfiguration();
        boolean canMove = (metrics.widthPixels != metrics.heightPixels &&
                cfg.smallestScreenWidthDp < 600);

        return (!canMove || metrics.widthPixels < metrics.heightPixels);
    }
}
