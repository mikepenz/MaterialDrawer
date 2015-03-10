package com.mikepenz.materialdrawer.model.interfaces;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface Identifyable<T> {
    public T withIdentifier(int identifier);

    public int getIdentifier();

    public void setIdentifier(int identifier);
}
