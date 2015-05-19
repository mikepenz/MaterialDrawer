package com.mikepenz.materialdrawer.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.R;

/**
 * Created by mikepenz on 15.03.14.
 */
@SuppressLint("InlinedApi")
public class UIUtils {
    public static ColorStateList getTextColorStateList(int text_color, int selected_text_color) {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_activated},
                        new int[]{}
                },
                new int[]{
                        selected_text_color,
                        text_color
                }
        );
    }

    public static StateListDrawable getIconStateList(Drawable icon, Drawable selectedIcon) {
        StateListDrawable iconStateListDrawable = new StateListDrawable();
        iconStateListDrawable.addState(new int[]{android.R.attr.state_activated}, selectedIcon);
        iconStateListDrawable.addState(new int[]{}, icon);
        return iconStateListDrawable;
    }

    public static StateListDrawable getDrawerItemBackground(int selected_color) {
        ColorDrawable clrActive = new ColorDrawable(selected_color);
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_activated}, clrActive);
        return states;
    }


    /**
     * helper to get the system default selectable background inclusive an active state
     *
     * @param ctx
     * @param selected_color
     * @return
     */
    public static StateListDrawable getSelectableBackground(Context ctx, int selected_color) {
        StateListDrawable states = getDrawerItemBackground(selected_color);
        states.addState(new int[]{}, getCompatDrawable(ctx, getSelectableBackground(ctx)));
        return states;
    }

    /**
     * helper to get the system default selectable background
     *
     * @param ctx
     * @return
     */
    public static int getSelectableBackground(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // If we're running on Honeycomb or newer, then we can use the Theme's
            // selectableItemBackground to ensure that the View has a pressed state
            TypedValue outValue = new TypedValue();
            ctx.getTheme().resolveAttribute(R.attr.selectableItemBackground, outValue, true);
            return outValue.resourceId;
        } else {
            TypedValue outValue = new TypedValue();
            ctx.getTheme().resolveAttribute(android.R.attr.itemBackground, outValue, true);
            return outValue.resourceId;
        }
    }

    public static int getThemeColor(Context ctx, int attr) {
        TypedValue tv = new TypedValue();
        if (ctx.getTheme().resolveAttribute(attr, tv, true)) {
            return tv.data;
        }
        return 0;
    }

    /**
     * helper method to get the color by attr (which is defined in the style) or by resource.
     *
     * @param ctx
     * @param attr
     * @param res
     * @return
     */
    public static int getThemeColorFromAttrOrRes(Context ctx, int attr, int res) {
        int color = getThemeColor(ctx, attr);
        if (color == 0) {
            color = ctx.getResources().getColor(res);
        }
        return color;
    }

    /**
     * helper method to set the background depending on the android version
     *
     * @param v
     * @param d
     */
    @SuppressLint("NewApi")
    public static void setBackground(View v, Drawable d) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackgroundDrawable(d);
        } else {
            v.setBackground(d);
        }
    }

    /**
     * helper method to set the background depending on the android version
     *
     * @param v
     * @param drawableRes
     */
    public static void setBackground(View v, int drawableRes) {
        setBackground(v, getCompatDrawable(v.getContext(), drawableRes));
    }

    /**
     * helper method to get the drawable by its resource. specific to the correct android version
     *
     * @param c
     * @param drawableRes
     * @return
     */
    public static Drawable getCompatDrawable(Context c, int drawableRes) {
        Drawable d = null;
        try {
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                d = c.getResources().getDrawable(drawableRes);
            } else {
                d = c.getResources().getDrawable(drawableRes, c.getTheme());
            }
        } catch (Exception ex) {
        }
        return d;
    }

    /**
     * Returns the screen width in pixels
     *
     * @param context is the context to get the resources
     * @return the screen width in pixels
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    /**
     * Returns the size in pixels of an attribute dimension
     *
     * @param context the context to get the resources from
     * @param attr    is the attribute dimension we want to know the size from
     * @return the size in pixels of an attribute dimension
     */
    public static int getThemeAttributeDimensionSize(Context context, int attr) {
        TypedArray a = null;
        try {
            a = context.getTheme().obtainStyledAttributes(new int[]{attr});
            return a.getDimensionPixelSize(0, 0);
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
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
     * helper to calculate the navigationBar height
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int id = resources.getIdentifier(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape", "dimen", "android");
        if (id > 0) {
            return resources.getDimensionPixelSize(id);
        }
        return 0;
    }

    /**
     * helper to calculate the actionBar height
     *
     * @param context
     * @return
     */
    public static int getActionBarHeight(Context context) {
        int actionBarHeight = UIUtils.getThemeAttributeDimensionSize(context, android.R.attr.actionBarSize);
        if (actionBarHeight == 0) {
            actionBarHeight = context.getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material);
        }
        return actionBarHeight;
    }

    /**
     * helper to calculate the statusBar height
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        return getStatusBarHeight(context, false);
    }

    /**
     * helper to calculate the statusBar height
     *
     * @param context
     * @param force   pass true to get the height even if the device has no translucent statusBar
     * @return
     */
    public static int getStatusBarHeight(Context context, boolean force) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        int dimenResult = context.getResources().getDimensionPixelSize(R.dimen.tool_bar_top_padding);
        //if our dimension is 0 return 0 because on those devices we don't need the height
        if (dimenResult == 0 && !force) {
            return 0;
        } else {
            //if our dimens is > 0 && the result == 0 use the dimenResult else the result;
            return result == 0 ? dimenResult : result;
        }
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }


    /**
     * helper method to get a person placeHolder drawable
     *
     * @param ctx
     * @return
     */
    public static Drawable getPlaceHolder(Context ctx) {
        int textColor = UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_primary_text, R.color.material_drawer_primary_text);
        return new IconicsDrawable(ctx, GoogleMaterial.Icon.gmd_person).color(textColor).backgroundColorRes(R.color.primary).iconOffsetYDp(2).paddingDp(2).sizeDp(56);
    }


    /**
     * a small helper method to simplify the color decision and get the correct value
     *
     * @param ctx
     * @param color
     * @param colorRes
     * @param defStyle
     * @param defColor
     * @return
     */
    public static int decideColor(Context ctx, int color, int colorRes, int defStyle, int defColor) {
        if (color == 0 && colorRes != -1) {
            color = ctx.getResources().getColor(colorRes);
        } else if (color == 0) {
            color = UIUtils.getThemeColorFromAttrOrRes(ctx, defStyle, defColor);
        }
        return color;
    }

    /**
     * a small helper method to simplify the icon decision and coloring for the drawerItems
     *
     * @param ctx
     * @param icon
     * @param iicon
     * @param iconRes
     * @param iconColor
     * @param tint
     * @return
     */
    public static Drawable decideIcon(Context ctx, Drawable icon, IIcon iicon, int iconRes, int iconColor, boolean tint) {
        if (icon == null && iicon != null) {
            icon = new IconicsDrawable(ctx, iicon).color(iconColor).actionBarSize().paddingDp(1);
        } else if (icon == null && iconRes > -1) {
            icon = UIUtils.getCompatDrawable(ctx, iconRes);
        }

        //if we got an icon AND we have auto tinting enabled AND it is no IIcon, tint it ;)
        if (icon != null && tint && iicon == null) {
            icon = icon.mutate();
            icon.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
            //icon.setAlpha(Color.alpha(iconColor));
        }

        return icon;
    }
}
