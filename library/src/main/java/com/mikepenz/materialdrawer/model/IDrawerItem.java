package com.mikepenz.materialdrawer.model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mikepenz on 03.02.15.
 */
public interface IDrawerItem {
    public enum Type {
        PRIMARY(0),
        SECONDARY(1),
        SPACER(2);

        int identifier;

        Type(int identifier) {
            this.identifier = identifier;
        }

        public int getIdentifier() {
            return this.identifier;
        }
    }

    public boolean isEnabled();

    public Type getType();

    public int getLayoutRes();

    public View convertView(Activity activity, LayoutInflater inflater, View convertView, ViewGroup parent);
}
