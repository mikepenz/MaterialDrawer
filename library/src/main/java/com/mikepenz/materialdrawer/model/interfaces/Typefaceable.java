package com.mikepenz.materialdrawer.model.interfaces;

import android.graphics.Typeface;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface Typefaceable<T> {
    public T withTypeface(Typeface typeface);

    public Typeface getTypeface();

    public void setTypeface(Typeface typeface);
}
