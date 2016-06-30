package com.mikepenz.materialdrawer.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

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
