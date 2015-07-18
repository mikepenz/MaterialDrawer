package com.mikepenz.materialdrawer.model;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.utils.ViewHolderFactory;

/**
 * Created by mikepenz on 03.02.15.
 */
public class MiniProfileDrawerItem extends AbstractDrawerItem<MiniProfileDrawerItem> implements IProfile<MiniProfileDrawerItem> {
    protected ImageHolder icon;

    public MiniProfileDrawerItem() {

    }

    public MiniProfileDrawerItem(ProfileDrawerItem profile) {
        this.icon = profile.icon;
    }

    @Override
    public MiniProfileDrawerItem withName(String name) {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public MiniProfileDrawerItem withEmail(String email) {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public void setEmail(String email) {

    }

    public MiniProfileDrawerItem withIcon(Drawable icon) {
        this.icon = new ImageHolder(icon);
        return this;
    }

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
    public ImageHolder getIcon() {
        return icon;
    }

    @Override
    public String getType() {
        return "MINI_PROFILE_ITEM";
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_mini_profile;
    }

    @Override
    public void bindView(RecyclerView.ViewHolder holder) {
        //get our viewHolder
        ViewHolder viewHolder = (ViewHolder) holder;

        //set the identifier from the drawerItem here. It can be used to run tests
        viewHolder.itemView.setId(getIdentifier());

        //set the icon
        ImageHolder.applyToOrSetInvisible(getIcon(), viewHolder.icon);
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
        private ImageView icon;

        public ViewHolder(View view) {
            super(view);

            this.icon = (ImageView) view.findViewById(R.id.icon);
        }
    }
}
