package com.mikepenz.materialdrawer.util;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * This class allow correctly resize layout with translucent status bar when adjustResize flag doesn't work.
 * Should be used as root container in activity.
 *
 * Solution from here: https://stackoverflow.com/a/42381132/5166771
 */

public class FullScreenWrapperLayout extends FrameLayout {

    public FullScreenWrapperLayout(@NonNull Context context) {
        super(context);
        setFitsSystemWindows(true);
    }

    public FullScreenWrapperLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setFitsSystemWindows(true);
    }

    public FullScreenWrapperLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFitsSystemWindows(true);
    }

    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0,
                    insets.getSystemWindowInsetBottom()));
        } else {
            return insets;
        }
    }
}
