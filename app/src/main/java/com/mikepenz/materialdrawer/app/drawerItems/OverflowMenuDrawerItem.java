package com.mikepenz.materialdrawer.app.drawerItems;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.app.R;
import com.mikepenz.materialdrawer.model.BaseDescribeableDrawerItem;
import com.mikepenz.materialdrawer.model.BaseViewHolder;

import java.util.List;

/**
 * Created by mikepenz on 03.02.15.
 */
public class OverflowMenuDrawerItem extends BaseDescribeableDrawerItem<OverflowMenuDrawerItem, OverflowMenuDrawerItem.ViewHolder> {
    private int mMenu;

    public OverflowMenuDrawerItem withMenu(int menu) {
        this.mMenu = menu;
        return this;
    }

    public int getMenu() {
        return mMenu;
    }

    private PopupMenu.OnMenuItemClickListener mOnMenuItemClickListener;

    public OverflowMenuDrawerItem withOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener onMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
        return this;
    }

    public PopupMenu.OnMenuItemClickListener getOnMenuItemClickListener() {
        return mOnMenuItemClickListener;
    }

    private PopupMenu.OnDismissListener mOnDismissListener;


    public OverflowMenuDrawerItem withOnDismissListener(PopupMenu.OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
        return this;
    }

    public PopupMenu.OnDismissListener getOnDismissListener() {
        return mOnDismissListener;
    }

    @Override
    public int getType() {
        return R.id.material_drawer_item_overflow_menu;
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_overflow_menu_primary;
    }

    @Override
    public void bindView(ViewHolder viewHolder, List payloads) {
        super.bindView(viewHolder, payloads);

        Context ctx = viewHolder.itemView.getContext();

        //bind the basic view parts
        bindViewHelper(viewHolder);

        //handle menu click
        viewHolder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(mMenu, popup.getMenu());

                popup.setOnMenuItemClickListener(mOnMenuItemClickListener);
                popup.setOnDismissListener(mOnDismissListener);

                popup.show();
            }
        });

        //handle image
        viewHolder.menu.setImageDrawable(new IconicsDrawable(ctx, GoogleMaterial.Icon.gmd_more_vert).sizeDp(12).color(getIconColor(ctx)));

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, viewHolder.itemView);
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    public static class ViewHolder extends BaseViewHolder {
        //protected ImageButton ibOverflow;
        private ImageButton menu;

        public ViewHolder(View view) {
            super(view);
            this.menu = (ImageButton) view.findViewById(R.id.material_drawer_menu_overflow);
        }
    }
}
