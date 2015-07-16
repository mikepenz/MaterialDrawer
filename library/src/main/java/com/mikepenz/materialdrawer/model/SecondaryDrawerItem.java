package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.holder.ColorHolder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.interfaces.ColorfulBadgeable;
import com.mikepenz.materialdrawer.model.utils.ViewHolderFactory;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;

/**
 * Created by mikepenz on 03.02.15.
 */
public class SecondaryDrawerItem extends BaseDrawerItem<SecondaryDrawerItem> implements ColorfulBadgeable<SecondaryDrawerItem> {

    private StringHolder badge;
    private ColorHolder badgeTextColor;
    private int badgeBackgroundRes = -1;

    public SecondaryDrawerItem withBadge(String badge) {
        this.badge = new StringHolder(badge);
        return this;
    }

    public SecondaryDrawerItem withBadge(@StringRes int badgeRes) {
        this.badge = new StringHolder(badgeRes);
        return this;
    }

    public StringHolder getBadge() {
        return badge;
    }

    @Override
    public SecondaryDrawerItem withBadgeTextColor(@ColorInt int color) {
        this.badgeTextColor = ColorHolder.fromColor(color);
        return this;
    }

    @Override
    public SecondaryDrawerItem withBadgeTextColorRes(@ColorRes int colorRes) {
        this.badgeTextColor = ColorHolder.fromColorRes(colorRes);
        return this;
    }

    @Override
    public ColorHolder getBadgeTextColor() {
        return badgeTextColor;
    }

    @Override
    public int getBadgeBackgroundResource() {
        return badgeBackgroundRes;
    }

    @Override
    public SecondaryDrawerItem withBadgeBackgroundResource(int res) {
        this.badgeBackgroundRes = res;
        return this;
    }

    @Override
    public String getType() {
        return "SECONDARY_ITEM";
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_secondary;
    }

    @Override
    public void bindView(RecyclerView.ViewHolder holder) {
        Context ctx = holder.itemView.getContext();

        //get our viewHolder
        ViewHolder viewHolder = (ViewHolder) holder;

        //set the identifier from the drawerItem here. It can be used to run tests
        viewHolder.itemView.setId(getIdentifier());

        //set the item selected if it is
        viewHolder.itemView.setSelected(isSelected());

        //get the correct color for the background
        int selectedColor = getSelectedColor(ctx);
        //get the correct color for the text
        int color = getColor(ctx);
        int selectedTextColor = getSelectedTextColor(ctx);
        //get the correct color for the icon
        int iconColor = getIconColor(ctx);
        int selectedIconColor = getSelectedIconColor(ctx);

        //set the background for the item
        UIUtils.setBackground(viewHolder.view, DrawerUIUtils.getSelectableBackground(ctx, selectedColor));
        //set the text for the name
        StringHolder.applyTo(this.getName(), viewHolder.name);
        //set the text for the badge or hide
        StringHolder.applyToOrHide(badge, viewHolder.badge);

        //set the colors for textViews
        viewHolder.name.setTextColor(getTextColorStateList(color, selectedTextColor));
        //set the badge text color
        ColorHolder.applyToOr(getBadgeTextColor(), viewHolder.badge, getTextColorStateList(color, selectedTextColor));

        //set background for badge
        if (badgeBackgroundRes != -1) {
            viewHolder.badge.setBackgroundResource(badgeBackgroundRes);
        }

        //define the typeface for our textViews
        if (getTypeface() != null) {
            viewHolder.name.setTypeface(getTypeface());
            viewHolder.badge.setTypeface(getTypeface());
        }

        //get the drawables for our icon and set it
        Drawable icon = ImageHolder.decideIcon(getIcon(), ctx, iconColor, isIconTinted(), 1);
        Drawable selectedIcon = ImageHolder.decideIcon(getSelectedIcon(), ctx, selectedIconColor, isIconTinted(), 1);
        ImageHolder.applyMultiIconTo(icon, iconColor, selectedIcon, selectedIconColor, isIconTinted(), viewHolder.icon);

        //fix padding issues
        viewHolder.view.setPadding((int) UIUtils.convertDpToPixel(16, ctx), 0, (int) UIUtils.convertDpToPixel(16, ctx), 0);
    }

    /**
     * helper method to decide for the correct color
     * OVERWRITE to get the correct secondary color
     *
     * @param ctx
     * @return
     */
    @Override
    protected int getColor(Context ctx) {
        int color;
        if (this.isEnabled()) {
            color = ColorHolder.color(getTextColor(), ctx, R.attr.material_drawer_secondary_text, R.color.material_drawer_secondary_text);
        } else {
            color = ColorHolder.color(getDisabledTextColor(), ctx, R.attr.material_drawer_hint_text, R.color.material_drawer_hint_text);
        }
        return color;
    }

    @Override
    public ViewHolderFactory getFactory() {
        return new ItemFactory();
    }

    public static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder factory(View v) {
            return new ViewHolder(v);
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private ImageView icon;
        private TextView name;
        private TextView badge;

        private ViewHolder(View view) {
            super(view);
            this.view = view;
            this.icon = (ImageView) view.findViewById(R.id.icon);
            this.name = (TextView) view.findViewById(R.id.name);
            this.badge = (TextView) view.findViewById(R.id.badge);
        }
    }
}
