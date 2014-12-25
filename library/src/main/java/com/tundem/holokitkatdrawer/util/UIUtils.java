package com.tundem.holokitkatdrawer.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.tundem.holokitkatdrawer.R;

/**
 * Created by mikepenz on 15.03.14.
 */
public class UIUtils {

    private static UIUtils singleton;

    private UIUtils() {
    }

    public static UIUtils getInstance() {
        if (singleton == null) {
            singleton = new UIUtils();
        }
        return singleton;
    }

    public int getActionStatusBarHeight(Context ctx) {
        return getStatusBarHeight(ctx) + getActionBarHeight(ctx);
    }

    public int getActionBarHeight(Context ctx) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TypedValue tv = new TypedValue();
            if (ctx.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                return TypedValue.complexToDimensionPixelSize(tv.data, ctx.getResources().getDisplayMetrics());
            }
        }
        return 0;
    }

    public int getStatusBarHeight(Context ctx) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int resourceId = ctx.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return ctx.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return 0;
    }

    public int getNavigationBarHeight(Context ctx) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int resourceId = ctx.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return ctx.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return 0;
    }

    public StateListDrawable getDrawerListItem(Context ctx) {
        ColorDrawable clrPress = new ColorDrawable();
        clrPress.setColor(ctx.getResources().getColor(R.color.material_primary_color_dark));
        ColorDrawable clrActive = new ColorDrawable();
        clrActive.setColor(ctx.getResources().getColor(R.color.material_primary_color));
        ColorDrawable clrBase = new ColorDrawable();
        clrBase.setColor(ctx.getResources().getColor(R.color.list_background_normal));

        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, clrPress);
        states.addState(new int[]{android.R.attr.state_activated}, clrActive);
        states.addState(new int[]{android.R.attr.state_empty}, clrBase);

        return states;
    }

    public StateListDrawable getDrawerListSecondaryItem(Context ctx) {
        ColorDrawable clrPress = new ColorDrawable();
        clrPress.setColor(ctx.getResources().getColor(R.color.material_primary_color_dark));
        ColorDrawable clrActive = new ColorDrawable();
        clrActive.setColor(ctx.getResources().getColor(R.color.material_primary_color));
        ColorDrawable clrBase = new ColorDrawable();
        clrBase.setColor(ctx.getResources().getColor(R.color.list_background_secondary_normal));

        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, clrPress);
        states.addState(new int[]{android.R.attr.state_activated}, clrActive);
        states.addState(new int[]{android.R.attr.state_empty}, clrBase);

        return states;
    }

    public LinearLayout getSpacer(Context ctx, int spacerSize, int color) {
        // I used padding instead of LayoutParams thinking it would be easier to
        // change in the future. It wasn't, but still made resizing my spacer easy
        LinearLayout spacer = new LinearLayout(ctx);
        spacer.setOrientation(LinearLayout.HORIZONTAL);
        spacer.setPadding(0, spacerSize, 0, 0);
        spacer.setBackgroundColor(color);
        return spacer;
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
