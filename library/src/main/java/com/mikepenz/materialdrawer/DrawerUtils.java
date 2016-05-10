package com.mikepenz.materialdrawer;

import android.content.Context;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mikepenz.materialdrawer.model.ContainerDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Selectable;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;

import java.util.List;

/**
 * Created by mikepenz on 23.05.15.
 */
class DrawerUtils {
    /**
     * helper method to handle the onClick of the footer
     *
     * @param drawer
     * @param drawerItem
     * @param v
     * @param fireOnClick true if we should call the listener, false if not, null to not call the listener and not close the drawer
     */
    public static void onFooterDrawerItemClick(DrawerBuilder drawer, IDrawerItem drawerItem, View v, Boolean fireOnClick) {
        boolean checkable = !(drawerItem != null && drawerItem instanceof Selectable && !((Selectable) drawerItem).isSelectable());
        if (checkable) {
            drawer.resetStickyFooterSelection();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                v.setActivated(true);
            }
            v.setSelected(true);

            //remove the selection in the list
            drawer.getAdapter().deselect();

            //find the position of the clicked footer item
            if (drawer.mStickyFooterView != null && drawer.mStickyFooterView instanceof LinearLayout) {
                LinearLayout footer = (LinearLayout) drawer.mStickyFooterView;
                for (int i = 0; i < footer.getChildCount(); i++) {
                    if (footer.getChildAt(i) == v) {
                        drawer.mCurrentStickyFooterSelection = i;
                        break;
                    }
                }
            }
        }


        if (fireOnClick != null) {
            boolean consumed = false;
            if (fireOnClick && drawer.mOnDrawerItemClickListener != null) {
                consumed = drawer.mOnDrawerItemClickListener.onItemClick(v, -1, drawerItem);
            }

            if (!consumed) {
                //close the drawer after click
                drawer.closeDrawerDelayed();
            }
        }
    }

    /**
     * helper method to set the selection of the footer
     *
     * @param drawer
     * @param position
     * @param fireOnClick
     */
    public static void setStickyFooterSelection(DrawerBuilder drawer, int position, Boolean fireOnClick) {
        if (position > -1) {
            if (drawer.mStickyFooterView != null && drawer.mStickyFooterView instanceof LinearLayout) {
                LinearLayout footer = (LinearLayout) drawer.mStickyFooterView;

                if (footer.getChildCount() > position && position >= 0) {
                    IDrawerItem drawerItem = (IDrawerItem) footer.getChildAt(position).getTag();
                    onFooterDrawerItemClick(drawer, drawerItem, footer.getChildAt(position), fireOnClick);
                }
            }
        }
    }

    /**
     * calculates the position of an drawerItem. searching by it's identifier
     *
     * @param identifier
     * @return
     */
    public static int getPositionByIdentifier(DrawerBuilder drawer, long identifier) {
        if (identifier >= 0) {
            for (int i = 0; i < drawer.getAdapter().getItemCount(); i++) {
                if (drawer.getAdapter().getItem(i).getIdentifier() == identifier) {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     * gets the drawerItem with the specific identifier from a drawerItem list
     *
     * @param drawerItems
     * @param identifier
     * @return
     */
    public static IDrawerItem getDrawerItem(List<IDrawerItem> drawerItems, long identifier) {
        if (identifier >= 0) {
            for (IDrawerItem drawerItem : drawerItems) {
                if (drawerItem.getIdentifier() == identifier) {
                    return drawerItem;
                }
            }
        }
        return null;
    }

    /**
     * gets the drawerItem by a defined tag from a drawerItem list
     *
     * @param drawerItems
     * @param tag
     * @return
     */
    public static IDrawerItem getDrawerItem(List<IDrawerItem> drawerItems, Object tag) {
        if (tag != null) {
            for (IDrawerItem drawerItem : drawerItems) {
                if (tag.equals(drawerItem.getTag())) {
                    return drawerItem;
                }
            }
        }
        return null;
    }

    /**
     * calculates the position of an drawerItem inside the footer. searching by it's identifier
     *
     * @param identifier
     * @return
     */
    public static int getStickyFooterPositionByIdentifier(DrawerBuilder drawer, long identifier) {
        if (identifier >= 0) {
            if (drawer.mStickyFooterView != null && drawer.mStickyFooterView instanceof LinearLayout) {
                LinearLayout footer = (LinearLayout) drawer.mStickyFooterView;

                int shadowOffset = 0;
                for (int i = 0; i < footer.getChildCount(); i++) {
                    Object o = footer.getChildAt(i).getTag();

                    //count up the shadowOffset to return the correct position of the given item
                    if (o == null && drawer.mStickyFooterDivider) {
                        shadowOffset = shadowOffset + 1;
                    }

                    if (o != null && o instanceof IDrawerItem && ((IDrawerItem) o).getIdentifier() == identifier) {
                        return i - shadowOffset;
                    }
                }
            }
        }

        return -1;
    }

    /**
     * helper method to handle the headerView
     *
     * @param drawer
     */
    public static void handleHeaderView(DrawerBuilder drawer) {
        //use the AccountHeader if set
        if (drawer.mAccountHeader != null) {
            if (drawer.mAccountHeaderSticky) {
                drawer.mStickyHeaderView = drawer.mAccountHeader.getView();
            } else {
                drawer.mHeaderView = drawer.mAccountHeader.getView();
                drawer.mHeaderDivider = drawer.mAccountHeader.mAccountHeaderBuilder.mDividerBelowHeader;
                drawer.mHeaderPadding = drawer.mAccountHeader.mAccountHeaderBuilder.mPaddingBelowHeader;
            }
        }

        //sticky header view
        if (drawer.mStickyHeaderView != null) {
            //add the sticky footer view and align it to the bottom
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
            drawer.mStickyHeaderView.setId(R.id.material_drawer_sticky_header);
            drawer.mSliderLayout.addView(drawer.mStickyHeaderView, 0, layoutParams);

            //now align the recyclerView below the stickyFooterView ;)
            RelativeLayout.LayoutParams layoutParamsListView = (RelativeLayout.LayoutParams) drawer.mRecyclerView.getLayoutParams();
            layoutParamsListView.addRule(RelativeLayout.BELOW, R.id.material_drawer_sticky_header);
            drawer.mRecyclerView.setLayoutParams(layoutParamsListView);

            //set a background color or the elevation will not work
            drawer.mStickyHeaderView.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(drawer.mActivity, R.attr.material_drawer_background, R.color.material_drawer_background));

            if (drawer.mStickyHeaderShadow) {
                //add a shadow
                if (Build.VERSION.SDK_INT >= 21) {
                    drawer.mStickyHeaderView.setElevation(UIUtils.convertDpToPixel(4, drawer.mActivity));
                } else {
                    View view = new View(drawer.mActivity);
                    view.setBackgroundResource(R.drawable.material_drawer_shadow_bottom);
                    drawer.mSliderLayout.addView(view, RelativeLayout.LayoutParams.MATCH_PARENT, (int) UIUtils.convertDpToPixel(4, drawer.mActivity));
                    //now align the shadow below the stickyHeader ;)
                    RelativeLayout.LayoutParams lps = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    lps.addRule(RelativeLayout.BELOW, R.id.material_drawer_sticky_header);
                    view.setLayoutParams(lps);
                }
            }

            //remove the padding of the recyclerView again we have the header on top of it
            drawer.mRecyclerView.setPadding(0, 0, 0, 0);
        }

        // set the header (do this before the setAdapter because some devices will crash else
        if (drawer.mHeaderView != null) {
            if (drawer.mRecyclerView == null) {
                throw new RuntimeException("can't use a headerView without a recyclerView");
            }

            if (drawer.mHeaderPadding) {
                drawer.getHeaderAdapter().add(new ContainerDrawerItem().withView(drawer.mHeaderView).withHeight(drawer.mHeiderHeight).withDivider(drawer.mHeaderDivider).withViewPosition(ContainerDrawerItem.Position.TOP));
            } else {
                drawer.getHeaderAdapter().add(new ContainerDrawerItem().withView(drawer.mHeaderView).withHeight(drawer.mHeiderHeight).withDivider(drawer.mHeaderDivider).withViewPosition(ContainerDrawerItem.Position.NONE));
            }
            //set the padding on the top to 0
            drawer.mRecyclerView.setPadding(drawer.mRecyclerView.getPaddingLeft(), 0, drawer.mRecyclerView.getPaddingRight(), drawer.mRecyclerView.getPaddingBottom());
        }
    }

    /**
     * small helper to rebuild the FooterView
     *
     * @param drawer
     */
    public static void rebuildStickyFooterView(final DrawerBuilder drawer) {
        if (drawer.mSliderLayout != null) {
            if (drawer.mStickyFooterView != null) {
                drawer.mStickyFooterView.removeAllViews();

                //create the divider
                if (drawer.mStickyFooterDivider) {
                    addStickyFooterDivider(drawer.mStickyFooterView.getContext(), drawer.mStickyFooterView);
                }

                //fill the footer with items
                DrawerUtils.fillStickyDrawerItemFooter(drawer, drawer.mStickyFooterView, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IDrawerItem drawerItem = (IDrawerItem) v.getTag();
                        com.mikepenz.materialdrawer.DrawerUtils.onFooterDrawerItemClick(drawer, drawerItem, v, true);
                    }
                });

                drawer.mStickyFooterView.setVisibility(View.VISIBLE);
            } else {
                //there was no footer yet. now just create one
                DrawerUtils.handleFooterView(drawer, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IDrawerItem drawerItem = (IDrawerItem) v.getTag();
                        DrawerUtils.onFooterDrawerItemClick(drawer, drawerItem, v, true);
                    }
                });
            }

            setStickyFooterSelection(drawer, drawer.mCurrentStickyFooterSelection, false);
        }
    }

    /**
     * helper method to handle the footerView
     *
     * @param drawer
     */
    public static void handleFooterView(DrawerBuilder drawer, View.OnClickListener onClickListener) {
        Context ctx = drawer.mSliderLayout.getContext();

        //use the StickyDrawerItems if set
        if (drawer.mStickyDrawerItems != null && drawer.mStickyDrawerItems.size() > 0) {
            drawer.mStickyFooterView = DrawerUtils.buildStickyDrawerItemFooter(ctx, drawer, onClickListener);
        }

        //sticky footer view
        if (drawer.mStickyFooterView != null) {
            //add the sticky footer view and align it to the bottom
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
            drawer.mStickyFooterView.setId(R.id.material_drawer_sticky_footer);
            drawer.mSliderLayout.addView(drawer.mStickyFooterView, layoutParams);

            if ((drawer.mTranslucentNavigationBar || drawer.mFullscreen) && Build.VERSION.SDK_INT >= 19) {
                drawer.mStickyFooterView.setPadding(0, 0, 0, UIUtils.getNavigationBarHeight(ctx));
            }

            //now align the recyclerView above the stickyFooterView ;)
            RelativeLayout.LayoutParams layoutParamsListView = (RelativeLayout.LayoutParams) drawer.mRecyclerView.getLayoutParams();
            layoutParamsListView.addRule(RelativeLayout.ABOVE, R.id.material_drawer_sticky_footer);
            drawer.mRecyclerView.setLayoutParams(layoutParamsListView);

            //handle shadow on top of the sticky footer
            if (drawer.mStickyFooterShadow) {
                drawer.mStickyFooterShadowView = new View(ctx);
                drawer.mStickyFooterShadowView.setBackgroundResource(R.drawable.material_drawer_shadow_top);
                drawer.mSliderLayout.addView(drawer.mStickyFooterShadowView, RelativeLayout.LayoutParams.MATCH_PARENT, ctx.getResources().getDimensionPixelSize(R.dimen.material_drawer_sticky_footer_elevation));
                //now align the shadow below the stickyHeader ;)
                RelativeLayout.LayoutParams lps = (RelativeLayout.LayoutParams) drawer.mStickyFooterShadowView.getLayoutParams();
                lps.addRule(RelativeLayout.ABOVE, R.id.material_drawer_sticky_footer);
                drawer.mStickyFooterShadowView.setLayoutParams(lps);
            }

            //remove the padding of the recyclerView again we have the footer below it
            drawer.mRecyclerView.setPadding(drawer.mRecyclerView.getPaddingLeft(), drawer.mRecyclerView.getPaddingTop(), drawer.mRecyclerView.getPaddingRight(), ctx.getResources().getDimensionPixelSize(R.dimen.material_drawer_padding));
        }

        // set the footer (do this before the setAdapter because some devices will crash else
        if (drawer.mFooterView != null) {
            if (drawer.mRecyclerView == null) {
                throw new RuntimeException("can't use a footerView without a recyclerView");
            }

            if (drawer.mFooterDivider) {
                drawer.getFooterAdapter().add(new ContainerDrawerItem().withView(drawer.mFooterView).withViewPosition(ContainerDrawerItem.Position.BOTTOM));
            } else {
                drawer.getFooterAdapter().add(new ContainerDrawerItem().withView(drawer.mFooterView).withViewPosition(ContainerDrawerItem.Position.NONE));
            }
        }
    }


    /**
     * build the sticky footer item view
     *
     * @return
     */
    public static ViewGroup buildStickyDrawerItemFooter(Context ctx, DrawerBuilder drawer, View.OnClickListener onClickListener) {
        //create the container view
        final LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        //set the background color to the drawer background color (if it has alpha the shadow won't be visible)
        linearLayout.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_background, R.color.material_drawer_background));

        //create the divider
        if (drawer.mStickyFooterDivider) {
            addStickyFooterDivider(ctx, linearLayout);
        }

        fillStickyDrawerItemFooter(drawer, linearLayout, onClickListener);

        return linearLayout;
    }

    /**
     * adds the shadow to the stickyFooter
     *
     * @param ctx
     * @param footerView
     */
    private static void addStickyFooterDivider(Context ctx, ViewGroup footerView) {
        LinearLayout divider = new LinearLayout(ctx);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        divider.setMinimumHeight((int) UIUtils.convertDpToPixel(1, ctx));
        divider.setOrientation(LinearLayout.VERTICAL);
        divider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_divider, R.color.material_drawer_divider));
        footerView.addView(divider, dividerParams);
    }

    /**
     * helper method to fill the sticky footer with it's elements
     *
     * @param drawer
     * @param container
     * @param onClickListener
     */
    public static void fillStickyDrawerItemFooter(DrawerBuilder drawer, ViewGroup container, View.OnClickListener onClickListener) {
        //add all drawer items
        for (IDrawerItem drawerItem : drawer.mStickyDrawerItems) {
            View view = drawerItem.generateView(container.getContext(), container);
            view.setTag(drawerItem);

            if (drawerItem.isEnabled()) {
                //UIUtils.setBackground(view, UIUtils.getSelectableBackground(container.getContext(), selected_color, true));
                view.setOnClickListener(onClickListener);
            }

            container.addView(view);

            //for android API 17 --> Padding not applied via xml
            DrawerUIUtils.setDrawerVerticalPadding(view);
        }
        //and really. don't ask about this. it won't set the padding if i don't set the padding for the container
        container.setPadding(0, 0, 0, 0);
    }


    /**
     * helper to extend the layoutParams of the drawer
     *
     * @param params
     * @return
     */
    public static DrawerLayout.LayoutParams processDrawerLayoutParams(DrawerBuilder drawer, DrawerLayout.LayoutParams params) {
        if (params != null) {
            if (drawer.mDrawerGravity != null && (drawer.mDrawerGravity == Gravity.RIGHT || drawer.mDrawerGravity == Gravity.END)) {
                params.rightMargin = 0;
                if (Build.VERSION.SDK_INT >= 17) {
                    params.setMarginEnd(0);
                }

                params.leftMargin = drawer.mActivity.getResources().getDimensionPixelSize(R.dimen.material_drawer_margin);
                if (Build.VERSION.SDK_INT >= 17) {
                    params.setMarginEnd(drawer.mActivity.getResources().getDimensionPixelSize(R.dimen.material_drawer_margin));
                }
            }

            if (drawer.mDrawerWidth > -1) {
                params.width = drawer.mDrawerWidth;
            } else {
                params.width = DrawerUIUtils.getOptimalDrawerWidth(drawer.mActivity);
            }
        }

        return params;
    }
}
