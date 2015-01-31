package com.tundem.materialdrawer.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;

import com.tundem.materialdrawer.R;

/**
 * Created by mikepenz on 15.03.14.
 */
public class UIUtils {
    public static StateListDrawable getDrawerListItem(Context ctx) {
        ColorDrawable clrPress = new ColorDrawable();
        clrPress.setColor(ctx.getResources().getColor(R.color.material_drawer_primary_dark));
        ColorDrawable clrActive = new ColorDrawable();
        clrActive.setColor(ctx.getResources().getColor(R.color.material_drawer_primary));
        ColorDrawable clrBase = new ColorDrawable();
        clrBase.setColor(ctx.getResources().getColor(R.color.material_drawer_list));

        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, clrPress);
        states.addState(new int[]{android.R.attr.state_activated}, clrActive);
        states.addState(new int[]{android.R.attr.state_empty}, clrBase);

        return states;
    }

    public static StateListDrawable getDrawerListSecondaryItem(Context ctx) {
        ColorDrawable clrPress = new ColorDrawable();
        clrPress.setColor(ctx.getResources().getColor(R.color.material_drawer_primary_dark));
        ColorDrawable clrActive = new ColorDrawable();
        clrActive.setColor(ctx.getResources().getColor(R.color.material_drawer_primary));
        ColorDrawable clrBase = new ColorDrawable();
        clrBase.setColor(ctx.getResources().getColor(R.color.material_drawer_list_secondary));

        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, clrPress);
        states.addState(new int[]{android.R.attr.state_activated}, clrActive);
        states.addState(new int[]{android.R.attr.state_empty}, clrBase);

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
