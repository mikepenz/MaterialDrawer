package com.mikepenz.materialdrawer.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by mikepenz on 15.03.14.
 */
@SuppressLint("InlinedApi")
public class UIUtils {
    public static ColorStateList getTextColor(int text_color, int selected_text_color) {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_activated},
                        new int[]{android.R.attr.state_enabled}
                },
                new int[]{
                        selected_text_color,
                        text_color
                }
        );
    }

    public static StateListDrawable getIconColor(Drawable icon, Drawable selectedIcon) {
        StateListDrawable iconStateListDrawable = new StateListDrawable();
        iconStateListDrawable.addState(new int[]{android.R.attr.state_activated}, selectedIcon);
        iconStateListDrawable.addState(new int[]{android.R.attr.state_enabled}, icon);
        return iconStateListDrawable;
    }

    public static StateListDrawable getDrawerItemBackground(Context ctx, int selected_color) {
        ColorDrawable clrActive = new ColorDrawable(selected_color);
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_activated}, clrActive);

        return states;
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

    @SuppressLint("NewApi")
    public static void setBackground(View v, Drawable d) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackgroundDrawable(d);
        } else {
            v.setBackground(d);
        }
    }
}
