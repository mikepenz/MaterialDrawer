package com.mikepenz.materialdrawer.model.interfaces;

import com.mikepenz.materialdrawer.holder.ColorHolder;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface ColorfulBadgeable<T> extends Badgeable<T> {
    T withBadgeTextColor(int color);

    T withBadgeTextColorRes(int colorRes);

    ColorHolder getBadgeTextColor();

    T withBadgeBackgroundResource(int res);

    void setBadgeBackgroundResource(int res);

    int getBadgeBackgroundResource();

}
