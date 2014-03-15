package com.tundem.holokitkatdrawer.util;

import android.content.Context;
import android.content.res.Configuration;
import android.util.TypedValue;
import android.widget.FrameLayout;

/**
 * Created by mikepenz on 15.03.14.
 */
public class UIUtils {

    public static FrameLayout.LayoutParams handleTranslucentDecorMargins(Context c, int orientation, FrameLayout.LayoutParams layoutParams) {
        if (layoutParams != null) {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                layoutParams.setMargins(0, UIUtils.getActionBarHeight(c) + UIUtils.getStatusBarHeight(c), 0, 0);
            } else {
                layoutParams.setMargins(0, UIUtils.getActionBarHeight(c) + UIUtils.getStatusBarHeight(c), 0, UIUtils.getNavigationBarHeight(c));
            }
        }

        return layoutParams;
    }

    public static int getActionBarHeight(Context c) {
        TypedValue tv = new TypedValue();
        if (c.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, c.getResources().getDisplayMetrics());
        }
        return 0;
    }

    public static int getStatusBarHeight(Context c) {
        int resourceId = c.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return c.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getNavigationBarHeight(Context c) {
        int resourceId = c.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return c.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
