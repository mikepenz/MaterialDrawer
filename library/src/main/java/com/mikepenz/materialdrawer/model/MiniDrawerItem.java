package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DimenRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.holder.DimenHolder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialize.util.UIUtils;

/**
 * Created by mikepenz on 03.02.15.
 */
public class MiniDrawerItem extends BaseDrawerItem<MiniDrawerItem, MiniDrawerItem.ViewHolder> {
    private StringHolder mBadge;
    private BadgeStyle mBadgeStyle = new BadgeStyle();

    private boolean mEnableSelectedBackground = false;
    protected DimenHolder mCustomHeight;

    public MiniDrawerItem() {

    }

    public MiniDrawerItem(PrimaryDrawerItem primaryDrawerItem) {
        this.mIdentifier = primaryDrawerItem.mIdentifier;
        this.mTag = primaryDrawerItem.mTag;

        this.mBadge = primaryDrawerItem.mBadge;
        this.mBadgeStyle = primaryDrawerItem.mBadgeStyle;

        this.mEnabled = primaryDrawerItem.mEnabled;
        this.mSelectable = primaryDrawerItem.mSelectable;
        this.mSelected = primaryDrawerItem.mSelected;

        this.icon = primaryDrawerItem.icon;
        this.selectedIcon = primaryDrawerItem.selectedIcon;

        this.iconTinted = primaryDrawerItem.iconTinted;
        this.selectedColor = primaryDrawerItem.selectedColor;

        this.iconColor = primaryDrawerItem.iconColor;
        this.selectedIconColor = primaryDrawerItem.selectedIconColor;
        this.disabledIconColor = primaryDrawerItem.disabledIconColor;
    }

    public MiniDrawerItem(SecondaryDrawerItem secondaryDrawerItem) {
        this.mIdentifier = secondaryDrawerItem.mIdentifier;
        this.mTag = secondaryDrawerItem.mTag;

        this.mBadge = secondaryDrawerItem.mBadge;
        this.mBadgeStyle = secondaryDrawerItem.mBadgeStyle;

        this.mEnabled = secondaryDrawerItem.mEnabled;
        this.mSelectable = secondaryDrawerItem.mSelectable;
        this.mSelected = secondaryDrawerItem.mSelected;

        this.icon = secondaryDrawerItem.icon;
        this.selectedIcon = secondaryDrawerItem.selectedIcon;

        this.iconTinted = secondaryDrawerItem.iconTinted;
        this.selectedColor = secondaryDrawerItem.selectedColor;

        this.iconColor = secondaryDrawerItem.iconColor;
        this.selectedIconColor = secondaryDrawerItem.selectedIconColor;
        this.disabledIconColor = secondaryDrawerItem.disabledIconColor;
    }


    public MiniDrawerItem withCustomHeightRes(@DimenRes int customHeightRes) {
        this.mCustomHeight = DimenHolder.fromResource(customHeightRes);
        return this;
    }

    public MiniDrawerItem withCustomHeightDp(int customHeightDp) {
        this.mCustomHeight = DimenHolder.fromDp(customHeightDp);
        return this;
    }

    public MiniDrawerItem withCustomHeightPx(int customHeightPx) {
        this.mCustomHeight = DimenHolder.fromPixel(customHeightPx);
        return this;
    }

    public MiniDrawerItem withCustomHeight(DimenHolder customHeight) {
        this.mCustomHeight = customHeight;
        return this;
    }

    public MiniDrawerItem withEnableSelectedBackground(boolean enableSelectedBackground) {
        this.mEnableSelectedBackground = enableSelectedBackground;
        return this;
    }

    @Override
    public int getType() {
        return R.id.material_drawer_item_mini;
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_mini;
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        Context ctx = viewHolder.itemView.getContext();

        //set a different height for this item
        if (mCustomHeight != null) {
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
            lp.height = mCustomHeight.asPixel(ctx);
            viewHolder.itemView.setLayoutParams(lp);
        }

        //set the identifier from the drawerItem here. It can be used to run tests
        viewHolder.itemView.setId(hashCode());

        //set the item enabled if it is
        viewHolder.itemView.setEnabled(isEnabled());

        //set the item selected if it is
        viewHolder.itemView.setSelected(isSelected());

        //
        viewHolder.itemView.setTag(this);

        //get the correct color for the icon
        int iconColor = getIconColor(ctx);
        int selectedIconColor = getSelectedIconColor(ctx);

        if (mEnableSelectedBackground) {
            //get the correct color for the background
            int selectedColor = getSelectedColor(ctx);
            //set the background for the item
            UIUtils.setBackground(viewHolder.view, UIUtils.getSelectableBackground(ctx, selectedColor, true));
        }

        //set the text for the badge or hide
        boolean badgeVisible = StringHolder.applyToOrHide(mBadge, viewHolder.badge);
        //style the badge if it is visible
        if (badgeVisible) {
            mBadgeStyle.style(viewHolder.badge);
        }

        //get the drawables for our icon and set it
        Drawable icon = ImageHolder.decideIcon(getIcon(), ctx, iconColor, isIconTinted(), 1);
        Drawable selectedIcon = ImageHolder.decideIcon(getSelectedIcon(), ctx, selectedIconColor, isIconTinted(), 1);
        ImageHolder.applyMultiIconTo(icon, iconColor, selectedIcon, selectedIconColor, isIconTinted(), viewHolder.icon);

        //for android API 17 --> Padding not applied via xml
        int verticalPadding = ctx.getResources().getDimensionPixelSize(R.dimen.material_drawer_padding);
        int topBottomPadding = ctx.getResources().getDimensionPixelSize(R.dimen.material_mini_drawer_item_padding);
        viewHolder.itemView.setPadding(verticalPadding, topBottomPadding, verticalPadding, topBottomPadding);

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
        private View view;
        private ImageView icon;
        private TextView badge;

        public ViewHolder(View view) {
            super(view);

            this.view = view;
            this.icon = (ImageView) view.findViewById(R.id.material_drawer_icon);
            this.badge = (TextView) view.findViewById(R.id.material_drawer_badge);
        }
    }
}
