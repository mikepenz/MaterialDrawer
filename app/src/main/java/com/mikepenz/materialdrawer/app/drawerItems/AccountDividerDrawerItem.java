package com.mikepenz.materialdrawer.app.drawerItems;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.app.R;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.AbstractDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialize.util.UIUtils;

import java.util.List;

public class AccountDividerDrawerItem extends AbstractDrawerItem<AccountDividerDrawerItem, AccountDividerDrawerItem.ViewHolder> implements IProfile<AccountDividerDrawerItem> {
    @Override
    public int getType() {
        return R.id.material_drawer_profile_item_divider;
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return com.mikepenz.materialdrawer.R.layout.material_drawer_item_divider;
    }

    @Override
    public void bindView(ViewHolder viewHolder, List payloads) {
        super.bindView(viewHolder, payloads);

        Context ctx = viewHolder.itemView.getContext();

        //set the identifier from the drawerItem here. It can be used to run tests
        viewHolder.itemView.setId(hashCode());

        //define how the divider should look like
        viewHolder.view.setClickable(false);
        viewHolder.view.setEnabled(false);
        viewHolder.view.setMinimumHeight(1);
        ViewCompat.setImportantForAccessibility(viewHolder.view,
                ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO);

        //set the color for the divider
        viewHolder.divider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(ctx, com.mikepenz.materialdrawer.R.attr.material_drawer_divider, com.mikepenz.materialdrawer.R.color.material_drawer_divider));

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, viewHolder.itemView);
    }

    @Override
    public ViewHolderFactory<ViewHolder> getFactory() {
        return new ItemFactory();
    }

    @Override
    public AccountDividerDrawerItem withName(String name) {
        return null;
    }

    @Override
    public StringHolder getName() {
        return null;
    }

    @Override
    public AccountDividerDrawerItem withEmail(String email) {
        return null;
    }

    @Override
    public StringHolder getEmail() {
        return null;
    }

    @Override
    public AccountDividerDrawerItem withIcon(Drawable icon) {
        return null;
    }

    @Override
    public AccountDividerDrawerItem withIcon(Bitmap bitmap) {
        return null;
    }

    @Override
    public AccountDividerDrawerItem withIcon(@DrawableRes int iconRes) {
        return null;
    }

    @Override
    public AccountDividerDrawerItem withIcon(String url) {
        return null;
    }

    @Override
    public AccountDividerDrawerItem withIcon(Uri uri) {
        return null;
    }

    @Override
    public AccountDividerDrawerItem withIcon(IIcon icon) {
        return null;
    }

    @Override
    public ImageHolder getIcon() {
        return null;
    }

    public static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private View divider;

        private ViewHolder(View view) {
            super(view);
            this.view = view;
            this.divider = view.findViewById(com.mikepenz.materialdrawer.R.id.material_drawer_divider);
        }
    }
}