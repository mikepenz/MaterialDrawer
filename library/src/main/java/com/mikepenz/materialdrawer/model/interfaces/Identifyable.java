package com.mikepenz.materialdrawer.model.interfaces;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface Identifyable<T> {
    T withIdentifier(int identifier);

    int getIdentifier();
}
