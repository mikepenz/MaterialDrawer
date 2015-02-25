package com.mikepenz.materialdrawer.model.interfaces;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface Tagable<T> {
    public T withTag(Object tag);

    public Object getTag();

    public void setTag(Object tag);
}
