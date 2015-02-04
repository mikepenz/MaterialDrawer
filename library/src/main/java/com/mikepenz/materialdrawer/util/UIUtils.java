package com.mikepenz.materialdrawer.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;

import com.mikepenz.materialdrawer.R;

/**
 * Created by mikepenz on 15.03.14.
 */
public class UIUtils {
    public static ColorStateList getTextColor(Context ctx, int text_color) {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_activated},
                        new int[]{android.R.attr.state_enabled}
                },
                new int[]{
                        ctx.getResources().getColor(R.color.material_drawer_contrast_text),
                        text_color,
                        text_color
                }
        );
    }

    public static StateListDrawable getDrawerListItem(Context ctx) {
        ColorDrawable clrPress = new ColorDrawable();
        clrPress.setColor(ctx.getResources().getColor(R.color.material_drawer_primary));
        ColorDrawable clrActive = new ColorDrawable();
        clrActive.setColor(ctx.getResources().getColor(R.color.material_drawer_selected));

        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, clrPress);
        states.addState(new int[]{android.R.attr.state_activated}, clrActive);

        return states;
    }

    public static StateListDrawable getDrawerListSecondaryItem(Context ctx) {
        ColorDrawable clrPress = new ColorDrawable();
        clrPress.setColor(ctx.getResources().getColor(R.color.material_drawer_primary));
        ColorDrawable clrActive = new ColorDrawable();
        clrActive.setColor(ctx.getResources().getColor(R.color.material_drawer_selected));

        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, clrPress);
        states.addState(new int[]{android.R.attr.state_activated}, clrActive);

        return states;
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
