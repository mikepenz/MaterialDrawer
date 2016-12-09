package com.mikepenz.materialdrawer.model;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.holder.DimenHolder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.List;

/**
 * Created by mikepenz on 03.02.15.
 */
public class MiniProfileDrawerItem extends AbstractDrawerItem<MiniProfileDrawerItem, MiniProfileDrawerItem.ViewHolder> implements IProfile<MiniProfileDrawerItem> {
    protected ImageHolder icon;

    protected DimenHolder customHeight;

    public MiniProfileDrawerItem() {
        withSelectable(false);
    }

    public MiniProfileDrawerItem(ProfileDrawerItem profile) {
        this.icon = profile.icon;
        this.mEnabled = profile.mEnabled;
        withSelectable(false);
    }

    @Override
    public MiniProfileDrawerItem withName(String name) {
        return null;
    }

    @Override
    public StringHolder getName() {
        return null;
    }

    @Override
    public MiniProfileDrawerItem withEmail(String email) {
        return null;
    }

    @Override
    public StringHolder getEmail() {
        return null;
    }

    @Override
    public MiniProfileDrawerItem withIcon(Drawable icon) {
        this.icon = new ImageHolder(icon);
        return this;
    }

    @Override
    public MiniProfileDrawerItem withIcon(@DrawableRes int iconRes) {
        this.icon = new ImageHolder(iconRes);
        return this;
    }

    @Override
    public MiniProfileDrawerItem withIcon(Bitmap iconBitmap) {
        this.icon = new ImageHolder(iconBitmap);
        return this;
    }

    @Override
    public MiniProfileDrawerItem withIcon(String url) {
        this.icon = new ImageHolder(url);
        return this;
    }

    @Override
    public MiniProfileDrawerItem withIcon(Uri uri) {
        this.icon = new ImageHolder(uri);
        return this;
    }

    @Override
    public MiniProfileDrawerItem withIcon(IIcon icon) {
        this.icon = new ImageHolder(icon);
        return this;
    }

    public MiniProfileDrawerItem withCustomHeightRes(@DimenRes int customHeightRes) {
        this.customHeight = DimenHolder.fromResource(customHeightRes);
        return this;
    }

    public MiniProfileDrawerItem withCustomHeightDp(int customHeightDp) {
        this.customHeight = DimenHolder.fromDp(customHeightDp);
        return this;
    }

    public MiniProfileDrawerItem withCustomHeightPx(int customHeightPx) {
        this.customHeight = DimenHolder.fromPixel(customHeightPx);
        return this;
    }

    public MiniProfileDrawerItem withCustomHeight(DimenHolder customHeight) {
        this.customHeight = customHeight;
        return this;
    }

    @Override
    public ImageHolder getIcon() {
        return icon;
    }

    @Override
    public int getType() {
        return R.id.material_drawer_item_mini_profile;
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_mini_profile;
    }

    @Override
    public void bindView(ViewHolder viewHolder, List payloads) {
        super.bindView(viewHolder, payloads);

        if (customHeight != null) {
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
            lp.height = customHeight.asPixel(viewHolder.itemView.getContext());
            viewHolder.itemView.setLayoutParams(lp);
        }

        //set the identifier from the drawerItem here. It can be used to run tests
        viewHolder.itemView.setId(hashCode());

        //set the item enabled if it is
        viewHolder.itemView.setEnabled(isEnabled());

        //set the icon
        ImageHolder.applyToOrSetInvisible(getIcon(), viewHolder.icon);

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, viewHolder.itemView);
    }

    @Override
    public ViewHolderFactory<ViewHolder> getFactory() {
        return new ItemFactory();
    }


    public static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;

        public ViewHolder(View view) {
            super(view);
            this.icon = (ImageView) view.findViewById(R.id.material_drawer_icon);
        }
    }
}
