package com.mikepenz.materialdrawer.util;

import android.support.annotation.NonNull;

import com.mikepenz.materialdrawer.model.interfaces.Identifyable;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by mikepenz on 19.09.15.
 */
public class IdDistributor {
    private static AtomicInteger idDistributor = new AtomicInteger(2000000000);

    /**
     * set an unique identifier for all drawerItems which do not have one set already
     *
     * @param items
     * @return
     */
    public static <T extends Identifyable> ArrayList<T> checkIds(@NonNull ArrayList<T> items) {
        for (T item : items) {
            checkId(item);
        }
        return items;
    }

    /**
     * set an unique identifier for all drawerItems which do not have one set already
     *
     * @param items
     * @return
     */
    public static <T extends Identifyable> T[] checkIds(@NonNull T... items) {
        for (T item : items) {
            checkId(item);
        }
        return items;
    }

    /**
     * set an unique identifier for the drawerItem which do not have one set already
     *
     * @param item
     * @return
     */
    public static <T extends Identifyable> T checkId(@NonNull T item) {
        if (item.getIdentifier() == -1) {
            item.withIdentifier(idDistributor.incrementAndGet());
        }
        return item;
    }
}
