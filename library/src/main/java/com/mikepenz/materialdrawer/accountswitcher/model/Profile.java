package com.mikepenz.materialdrawer.accountswitcher.model;

import android.graphics.drawable.Drawable;

/**
 * Created by mikepenz on 27.02.15.
 */
public class Profile {

    private Drawable image;
    private String name;
    private String email;

    public Profile withImage(Drawable image) {
        this.image = image;
        return this;
    }

    public Profile withName(String name) {
        this.name = name;
        return this;
    }

    public Profile withEmail(String email) {
        this.email = email;
        return this;
    }

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
