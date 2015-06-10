package com.mikepenz.materialdrawer.model.interfaces;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface ColorfulBadgeable<T> extends Badgeable<T> {
    public T withBadgeTextColor(int color);

    public int getBadgeTextColor();

    public void setBadgeTextColor(int color);

    public void setBadgeBackgroundResource(int res);

    public int getBadgeBackgroundResource();

    public T withBadgeBackgroundResource(int res);
}
