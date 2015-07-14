package com.mikepenz.materialdrawer.model.interfaces;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface Selectable<T> {
    T withSelectable(boolean selectable);

    boolean isSelectable();
}
