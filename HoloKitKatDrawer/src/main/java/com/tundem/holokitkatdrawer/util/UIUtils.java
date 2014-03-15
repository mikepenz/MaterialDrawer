package com.tundem.holokitkatdrawer.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.FrameLayout;

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

    public static UIUtils init(Context c, int accentColor, int accentSecondaryColor, boolean translucentStatusBar, boolean marginStatusBar, boolean translucentNavigationBar, boolean marginNavigationBar) {
        getInstance().c = c;
        getInstance().accentColor = c.getResources().getColor(accentColor);
        getInstance().accentSecondaryColor = c.getResources().getColor(accentSecondaryColor);
        getInstance().translucentStatusBar = translucentStatusBar;
        getInstance().marginStatusBar = marginStatusBar;
        getInstance().translucentNavigationBar = translucentNavigationBar;
        getInstance().marginNavigationBar = marginNavigationBar;

        getInstance().initDone = true;

        return getInstance();
    }

    public static UIUtils update(int accentColor, int accentSecondaryColor, boolean translucentStatusBar, boolean marginStatusBar, boolean translucentNavigationBar, boolean marginNavigationBar) {
        return init(getInstance().c, accentColor, accentSecondaryColor, translucentStatusBar, marginStatusBar, translucentNavigationBar, marginNavigationBar);
    }

    public void initActivity(Activity act) {
        //set background color
        act.getWindow().getDecorView().setBackgroundColor(accentColor);

        if (translucentStatusBar) {
            //set translucent statusBar
            act.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (translucentNavigationBar) {
            //set translucent navigation
            act.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public FrameLayout.LayoutParams handleTranslucentDecorMargins(int orientation, FrameLayout.LayoutParams layoutParams) {
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
                layoutParams.setMargins(0, actionBarHeight + statusBarHeight, 0, 0);
            } else {
                layoutParams.setMargins(0, actionBarHeight + statusBarHeight, 0, navigationBarHeight);
            }
        }

        return layoutParams;
    }

    public int getActionBarHeight() {
        TypedValue tv = new TypedValue();
        if (c.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, c.getResources().getDisplayMetrics());
        }
        return 0;
    }

    public int getStatusBarHeight() {
        int resourceId = c.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return c.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public int getNavigationBarHeight() {
        int resourceId = c.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return c.getResources().getDimensionPixelSize(resourceId);
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
}
