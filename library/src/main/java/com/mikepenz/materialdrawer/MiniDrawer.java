package com.mikepenz.materialdrawer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.CrossFader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;

import java.util.ArrayList;

/**
 * Created by mikepenz on 15.07.15.
 * Don't count this for real yet. it's just a quick try on creating a Gmail like panel
 */
public class MiniDrawer {
    private ScrollView mScrollView;
    private LinearLayout mLinearLayout;

    private Drawer mDrawer;

    public MiniDrawer withDrawer(@NonNull Drawer drawer) {
        this.mDrawer = drawer;
        return this;
    }

    private AccountHeader mAccountHeader;

    public MiniDrawer withAccountHeader(@NonNull AccountHeader accountHeader) {
        this.mAccountHeader = accountHeader;
        return this;
    }

    private CrossFader mCrossFader;

    public MiniDrawer withCrossFader(@NonNull CrossFader crossFader) {
        this.mCrossFader = crossFader;
        return this;
    }

    public View build(Context ctx) {
        mScrollView = new ScrollView(ctx);

        mLinearLayout = new LinearLayout(ctx);
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);

        mScrollView.addView(mLinearLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        update();

        return mScrollView;
    }


    public void update() {
        Context ctx = mLinearLayout.getContext();
        mLinearLayout.removeAllViews();

        int size = (int) UIUtils.convertDpToPixel(72, ctx);

        if (mAccountHeader != null) {
            IProfile profile = mAccountHeader.getActiveProfile();
            if (profile != null) {
                View view = LayoutInflater.from(ctx).inflate(R.layout.material_drawer_item_mini_profile, null);
                ImageHolder.applyTo(profile.getIcon(), (ImageView) view.findViewById(R.id.icon));

                view.findViewById(R.id.icon).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCrossFader != null) {
                            mCrossFader.crossFade();
                        }
                    }
                });

                mLinearLayout.addView(view, size, size);
            }
        }

        if (mDrawer != null) {
            if (mDrawer.getDrawerItems() != null) {
                int selected_color = UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_selected, R.color.material_drawer_selected);

                ArrayList<IDrawerItem> drawerItems = mDrawer.getDrawerItems();
                if (mDrawer.switchedDrawerContent()) {
                    drawerItems = mDrawer.getOriginalDrawerItems();
                }

                for (IDrawerItem drawerItem : drawerItems) {
                    if (drawerItem instanceof PrimaryDrawerItem) {
                        PrimaryDrawerItem primaryDrawerItem = (PrimaryDrawerItem) drawerItem;

                        View view = LayoutInflater.from(ctx).inflate(R.layout.material_drawer_item_mini, null);
                        view.setTag(primaryDrawerItem);

                        ImageView imageView = (ImageView) view.findViewById(R.id.icon);

                        UIUtils.setBackground(imageView, DrawerUIUtils.getSelectableBackground(ctx, selected_color));

                        Drawable icon = ImageHolder.decideIcon(primaryDrawerItem.getIcon(), ctx, primaryDrawerItem.getIconColor(ctx), primaryDrawerItem.isIconTinted(), 1);
                        imageView.setImageDrawable(icon);

                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDrawer.setSelection((IDrawerItem) v.getTag(), true);

                                clearSelections();
                                v.findViewById(R.id.icon).setSelected(true);
                            }
                        });

                        if (drawerItem.isSelected()) {
                            imageView.setSelected(true);
                        }

                        mLinearLayout.addView(view, size, size);
                    }
                }
            }
        }
        mScrollView.scrollTo(0, 0);
    }

    protected void clearSelections() {
        for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
            mLinearLayout.getChildAt(i).findViewById(R.id.icon).setSelected(false);
        }
    }
}
