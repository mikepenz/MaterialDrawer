package com.tundem.holokitkatdrawer.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tundem.holokitkatdrawer.R;

/**
 * Created by mikepenz on 15.03.14.
 */
public class UIUtils {

    private static UIUtils singleton;

    private boolean initDone = false;

    private Context c;
    private int accentColor;
    private int accentSecondaryColor;
    private int backgroundColor;
    private boolean translucentStatusBar;
    private boolean marginStatusBar;
    private boolean translucentNavigationBar;
    private boolean marginNavigationBar;

    private UIUtils() {
    }

    public static UIUtils getInstance() {
        if (singleton == null) {
            singleton = new UIUtils();
        }
        return singleton;
    }

    public static UIUtils init(Context c, int accentColor, int accentSecondaryColor, int backgroundColor, boolean translucentStatusBar, boolean marginStatusBar, boolean translucentNavigationBar, boolean marginNavigationBar) {
        getInstance().c = c;
        getInstance().accentColor = accentColor;
        getInstance().accentSecondaryColor = accentSecondaryColor;
        getInstance().translucentStatusBar = translucentStatusBar;
        getInstance().backgroundColor = backgroundColor;
        getInstance().marginStatusBar = marginStatusBar;
        getInstance().translucentNavigationBar = translucentNavigationBar;
        getInstance().marginNavigationBar = marginNavigationBar;

        getInstance().initDone = true;

        return getInstance();
    }

    public UIUtils update(int accentColor, int accentSecondaryColor, int backgroundColor, boolean translucentStatusBar, boolean marginStatusBar, boolean translucentNavigationBar, boolean marginNavigationBar) {
        return init(getInstance().c, accentColor, accentSecondaryColor, backgroundColor, translucentStatusBar, marginStatusBar, translucentNavigationBar, marginNavigationBar);
    }

    public UIUtils updateColors(Activity act, int accentColor) {
        act.getWindow().getDecorView().setBackgroundColor(accentColor);
        int accentSecondaryColor = Color.parseColor("#88" + Integer.toHexString(accentColor).toUpperCase().substring(2));
        return init(getInstance().c, accentColor, accentSecondaryColor, getInstance().getBackgroundColor(), getInstance().translucentStatusBar, getInstance().marginStatusBar, getInstance().translucentNavigationBar, getInstance().marginNavigationBar);
    }

    public UIUtils updateColors(Activity act, int accentColor, int accentSecondaryColor, int backgroundColor) {
        act.getWindow().getDecorView().setBackgroundColor(accentColor);
        return init(getInstance().c, accentColor, accentSecondaryColor, backgroundColor, getInstance().translucentStatusBar, getInstance().marginStatusBar, getInstance().translucentNavigationBar, getInstance().marginNavigationBar);
    }

    public void initActivity(Activity act) {
        //set background color
        act.getWindow().getDecorView().setBackgroundColor(accentColor);

        if (translucentStatusBar && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //set translucent statusBar
            act.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }

        if (translucentNavigationBar && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //set translucent navigation
            act.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public void initActivity(Activity act, boolean cTranslucentStatusBar, boolean cTranslucentNavigationBar) {
        //set background color
        act.getWindow().getDecorView().setBackgroundColor(accentColor);

        if (cTranslucentStatusBar && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //set translucent statusBar
            act.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (cTranslucentNavigationBar && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //set translucent navigation
            act.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public void initActivity(Activity act, int cAccentColor, boolean cTranslucentStatusBar, boolean cTranslucentNavigationBar) {
        //set background color
        act.getWindow().getDecorView().setBackgroundColor(cAccentColor);

        if (cTranslucentStatusBar && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //set translucent statusBar
            act.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (cTranslucentNavigationBar && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //set translucent navigation
            act.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }


    public FrameLayout.LayoutParams handleTranslucentDecorMargins(int orientation, FrameLayout.LayoutParams layoutParams) {
        return handleTranslucentDecorMargins(orientation, layoutParams, marginStatusBar, marginNavigationBar);
    }

    public FrameLayout.LayoutParams handleTranslucentDecorMargins(int orientation, FrameLayout.LayoutParams layoutParams, boolean cMarginStatusBar, boolean cMarginNavigationBar) {
        int statusBarHeight = 0;
        int actionBarHeight = 0;
        if (marginStatusBar) {
            statusBarHeight = getStatusBarHeight();
            actionBarHeight = getActionBarHeight();
        }

        int navigationBarHeight = 0;
        if (marginNavigationBar) {
            navigationBarHeight = getNavigationBarHeight();
        }

        if (layoutParams != null) {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                layoutParams.setMargins(0, actionBarHeight + statusBarHeight, navigationBarHeight, 0);
            } else {
                layoutParams.setMargins(0, actionBarHeight + statusBarHeight, 0, navigationBarHeight);
            }
        }

        return layoutParams;
    }

    public FrameLayout.LayoutParams handleTranslucentDecorMargins(FrameLayout.LayoutParams layoutParams, Rect insets) {
        layoutParams.setMargins(insets.left, insets.top, insets.right, insets.bottom);
        return layoutParams;
    }

    public int getActionStatusBarHeight() {
        return getStatusBarHeight() + getActionBarHeight();
    }

    public int getActionBarHeight() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TypedValue tv = new TypedValue();
            if (c.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                return TypedValue.complexToDimensionPixelSize(tv.data, c.getResources().getDisplayMetrics());
            }
        }
        return 0;
    }

    public int getStatusBarHeight() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int resourceId = c.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return c.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return 0;
    }

    public int getNavigationBarHeight() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int resourceId = c.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return c.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return 0;
    }

    public StateListDrawable getDrawerListItem() {
        ColorDrawable clrPress = new ColorDrawable();
        clrPress.setColor(accentSecondaryColor);
        ColorDrawable clrActive = new ColorDrawable();
        clrActive.setColor(accentColor);
        ColorDrawable clrBase = new ColorDrawable();
        clrBase.setColor(c.getResources().getColor(R.color.list_background_normal));

        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, clrPress);
        states.addState(new int[]{android.R.attr.state_activated}, clrActive);
        states.addState(new int[]{android.R.attr.state_empty}, clrBase);

        return states;
    }

    public StateListDrawable getDrawerListSecondaryItem() {
        ColorDrawable clrPress = new ColorDrawable();
        clrPress.setColor(accentSecondaryColor);
        ColorDrawable clrActive = new ColorDrawable();
        clrActive.setColor(c.getResources().getColor(R.color.list_background_secondary_normal));
        ColorDrawable clrBase = new ColorDrawable();
        clrBase.setColor(c.getResources().getColor(R.color.list_background_secondary_normal));

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

    /**
     * GETTER UND SETTER!!
     */
    public int getAccentColor() {
        return accentColor;
    }

    public int getAccentSecondaryColor() {
        return accentSecondaryColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public boolean isTranslucentStatusBar() {
        return translucentStatusBar;
    }

    public boolean isTranslucentNavigationBar() {
        return translucentNavigationBar;
    }

    public boolean isMarginStatusBar() {
        return marginStatusBar;
    }

    public boolean isMarginNavigationBar() {
        return marginNavigationBar;
    }
}
