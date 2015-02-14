package com.mikepenz.materialdrawer.model.interfaces;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface Nameable<T> {
    public T withName(String name);

    public T withName(int nameRes);

    public String getName();

    public int getNameRes();

    public void setName(String name);

    public void setNameRes(int nameRes);
}
