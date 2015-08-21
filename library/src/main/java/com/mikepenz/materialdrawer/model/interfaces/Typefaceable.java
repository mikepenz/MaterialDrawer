package com.mikepenz.materialdrawer.model.interfaces;

import android.graphics.Typeface;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface Typefaceable<T> {
    T withTypeface(Typeface typeface);

    Typeface getTypeface();
}
