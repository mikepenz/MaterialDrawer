package com.mikepenz.materialdrawer.model.interfaces;

import com.mikepenz.materialdrawer.holder.ColorHolder;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface ColorfulBadgeable<T> extends Badgeable<T> {
    public T withBadgeTextColor(int color);

    public T withBadgeTextColorRes(int colorRes);

    public ColorHolder getBadgeTextColor();

    public T withBadgeBackgroundResource(int res);

    public void setBadgeBackgroundResource(int res);

    public int getBadgeBackgroundResource();

}
