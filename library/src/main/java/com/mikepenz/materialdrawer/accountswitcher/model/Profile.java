package com.mikepenz.materialdrawer.accountswitcher.model;

import android.graphics.drawable.Drawable;

/**
 * Created by mikepenz on 27.02.15.
 */
public class Profile {

    private Drawable image;
    private String name;
    private String email;

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
