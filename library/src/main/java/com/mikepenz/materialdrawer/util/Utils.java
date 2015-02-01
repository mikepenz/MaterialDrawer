package com.mikepenz.materialdrawer.util;

import java.util.List;

/**
 * Created by mikepenz on 01.02.15.
 */
public class Utils {

    public static <T> T getItem(List<T> col, int position) {
        if (col != null && col.size() > position) {
            return col.get(position);
        }
        return null;
    }
}
