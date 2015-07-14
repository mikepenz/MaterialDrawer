package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.holder.ColorHolder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Identifyable;
import com.mikepenz.materialdrawer.model.interfaces.Tagable;
import com.mikepenz.materialdrawer.model.interfaces.Typefaceable;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;

/**
 * Created by mikepenz on 03.02.15.
 */
public class ProfileDrawerItem implements IDrawerItem, IProfile<ProfileDrawerItem>, Tagable<ProfileDrawerItem>, Identifyable<ProfileDrawerItem>, Typefaceable<ProfileDrawerItem> {

    private int identifier = -1;

    private boolean selectable = true;
    private boolean nameShown = false;

    private ImageHolder icon;

    private String name;
    private String email;

    private boolean enabled = true;
    private Object tag;

    private ColorHolder selectedColor;
    private ColorHolder textColor;

    private Typeface typeface = null;

    public ProfileDrawerItem withIdentifier(int identifier) {
        this.identifier = identifier;
        return this;
    }

    public ProfileDrawerItem withIcon(Drawable icon) {
        this.icon = new ImageHolder(icon);
        return this;
    }

    public ProfileDrawerItem withIcon(Bitmap iconBitmap) {
        this.icon = new ImageHolder(iconBitmap);
        return this;
    }

    @Override
    public ProfileDrawerItem withIcon(String url) {
        this.icon = new ImageHolder(url);
        return this;
    }

    @Override
    public ProfileDrawerItem withIcon(Uri uri) {
        this.icon = new ImageHolder(uri);
        return this;
    }

    public ProfileDrawerItem withName(String name) {
        this.name = name;
        return this;
    }

    public ProfileDrawerItem withEmail(String email) {
        this.email = email;
        return this;
    }

    public ProfileDrawerItem withTag(Object object) {
        this.tag = object;
        return this;
    }

    public ProfileDrawerItem setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public ProfileDrawerItem withNameShown(boolean nameShown) {
        this.nameShown = nameShown;
        return this;
    }

    public ProfileDrawerItem withSelectedColor(int selectedColor) {
        this.selectedColor = ColorHolder.fromColor(selectedColor);
        return this;
    }

    public ProfileDrawerItem withSelectedColorRes(int selectedColorRes) {
        this.selectedColor = ColorHolder.fromColorRes(selectedColorRes);
        return this;
    }

    public ProfileDrawerItem withTextColor(int textColor) {
        this.textColor = ColorHolder.fromColor(textColor);
        return this;
    }

    public ProfileDrawerItem withTextColorRes(int textColorRes) {
        this.textColor = ColorHolder.fromColorRes(textColorRes);
        return this;
    }

    @Override
    public ProfileDrawerItem withSelectable(boolean selectable) {
        this.selectable = selectable;
        return this;
    }

    public ProfileDrawerItem withTypeface(Typeface typeface) {
        this.typeface = typeface;
        return this;
    }

    public boolean isNameShown() {
        return nameShown;
    }

    public void setNameShown(boolean nameShown) {
        this.nameShown = nameShown;
    }

    public ColorHolder getSelectedColor() {
        return selectedColor;
    }

    public ColorHolder getTextColor() {
        return textColor;
    }

    @Override
    public Typeface getTypeface() {
        return typeface;
    }

    @Override
    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public void setTag(Object tag) {
        this.tag = tag;
    }

    public ImageHolder getIcon() {
        return icon;
    }

    @Override
    public boolean isSelectable() {
        return selectable;
    }

    @Override
    public ProfileDrawerItem setSelectable(boolean selectable) {
        this.selectable = selectable;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getType() {
        return "PROFILE_ITEM";
    }

    @Override
    public int getLayoutRes() {
        return R.layout.material_drawer_item_profile;
    }

    @Override
    public View convertView(LayoutInflater inflater, View convertView, ViewGroup parent) {
        Context ctx = parent.getContext();

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(getLayoutRes(), parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //set the identifier from the drawerItem here. It can be used to run tests
        convertView.setId(getIdentifier());

        //get the correct color for the background
        int selectedColor = ColorHolder.color(getSelectedColor(), ctx, R.attr.material_drawer_selected, R.color.material_drawer_selected);
        //get the correct color for the text
        int color = ColorHolder.color(getTextColor(), ctx, R.attr.material_drawer_primary_text, R.color.material_drawer_primary_text);

        UIUtils.setBackground(viewHolder.view, DrawerUIUtils.getDrawerItemBackground(selectedColor));

        if (nameShown) {
            viewHolder.name.setVisibility(View.VISIBLE);
            viewHolder.name.setText(this.getName());
        } else {
            viewHolder.name.setVisibility(View.GONE);
        }
        //the MaterialDrawer follows the Google Apps. those only show the e-mail
        //within the profile switcher. The problem this causes some confusion for
        //some developers. And if you only set the name, the item would be empty
        //so here's a small fallback which will prevent this issue of empty items ;)
        if (!nameShown && this.getEmail() == null && this.getName() != null) {
            viewHolder.email.setText(this.getName());
        } else {
            viewHolder.email.setText(this.getEmail());
        }

        if (getTypeface() != null) {
            viewHolder.name.setTypeface(getTypeface());
            viewHolder.email.setTypeface(getTypeface());
        }

        if (nameShown) {
            viewHolder.name.setTextColor(color);
        }
        viewHolder.email.setTextColor(color);

        //set the icon
        ImageHolder.applyToOrSetInvisible(getIcon(), viewHolder.profileIcon);

        return convertView;
    }

    private static class ViewHolder {
        private View view;
        private ImageView profileIcon;
        private TextView name;
        private TextView email;

        private ViewHolder(View view) {
            this.view = view;
            this.profileIcon = (ImageView) view.findViewById(R.id.profileIcon);
            this.name = (TextView) view.findViewById(R.id.name);
            this.email = (TextView) view.findViewById(R.id.email);
        }
    }
}
