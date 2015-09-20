package com.mikepenz.materialdrawer.util;

import android.support.v7.widget.RecyclerView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.HashMap;
import java.util.Stack;

/**
 * Created by mikepenz on 18.09.15.
 */
public class RecyclerViewCacheUtil {
    private static RecyclerViewCacheUtil SINGLETON = null;

    private int CACHE_SIZE = 3;
    private HashMap<String, Stack<RecyclerView.ViewHolder>> CACHE = new HashMap<>();

    private RecyclerViewCacheUtil() {

    }

    public static RecyclerViewCacheUtil getInstance() {
        if (SINGLETON == null) {
            SINGLETON = new RecyclerViewCacheUtil();
        }
        return SINGLETON;
    }

    /**
     * define the amount of elements which should be cached for a specific drawerItem type
     *
     * @param cacheSize
     * @return
     */
    public RecyclerViewCacheUtil withCacheSize(int cacheSize) {
        CACHE_SIZE = cacheSize;
        return this;
    }

    /**
     * init the cache with an initialized drawer
     *
     * @param drawer
     */
    public void init(Drawer drawer) {
        if (drawer.getDrawerItems() != null) {
            init(drawer.getRecyclerView(), drawer.getDrawerItems().toArray(new IDrawerItem[drawer.getDrawerItems().size()]));
        }
    }

    /**
     * clear the CACHE
     */
    public void clear() {
        CACHE.clear();
    }

    /**
     * init the cache on your own.
     *
     * @param recyclerView
     * @param drawerItems
     */
    public void init(RecyclerView recyclerView, IDrawerItem... drawerItems) {
        if (drawerItems != null) {
            for (IDrawerItem d : drawerItems) {
                if (!CACHE.containsKey(d.getType())) {
                    CACHE.put(d.getType(), new Stack<RecyclerView.ViewHolder>());
                }

                if (CACHE_SIZE == -1 || CACHE.get(d.getType()).size() <= CACHE_SIZE) {
                    CACHE.get(d.getType()).push(d.getViewHolder(recyclerView));
                }
            }
        }
    }

    /**
     * this is used to get a cached drawerItem from the cache or null
     *
     * @param type
     * @return
     */
    public RecyclerView.ViewHolder obtain(String type) {
        if (CACHE.containsKey(type)) {
            if (CACHE.get(type).size() > 0) {
                return CACHE.get(type).pop();
            }
        }
        return null;
    }
}
