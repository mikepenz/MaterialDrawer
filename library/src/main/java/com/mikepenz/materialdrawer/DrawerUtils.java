package com.mikepenz.materialdrawer;

import android.app.Activity;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Checkable;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.UIUtils;

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
     * @param fireOnClick
     */
    public static void onFooterDrawerItemClick(DrawerBuilder drawer, IDrawerItem drawerItem, View v, boolean fireOnClick) {
        boolean checkable = !(drawerItem != null && drawerItem instanceof Checkable && !((Checkable) drawerItem).isCheckable());
        if (checkable) {
            drawer.resetStickyFooterSelection();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                v.setActivated(true);
            }
            v.setSelected(true);

            //remove the selection in the list
            drawer.mListView.setSelection(-1);
            drawer.mListView.setItemChecked(drawer.mCurrentSelection + drawer.mHeaderOffset, false);

            //set currentSelection to -1 because we selected a stickyFooter element
            drawer.mCurrentSelection = -1;

            //find the position of the clicked footer item
            if (drawer.mStickyFooterView != null && drawer.mStickyFooterView instanceof LinearLayout) {
                LinearLayout footer = (LinearLayout) drawer.mStickyFooterView;
                for (int i = 0; i < footer.getChildCount(); i++) {
                    if (footer.getChildAt(i) == v) {
                        drawer.mCurrentFooterSelection = i;
                        break;
                    }
                }
            }
        }


        boolean consumed = false;
        if (fireOnClick && drawer.mOnDrawerItemClickListener != null) {
            consumed = drawer.mOnDrawerItemClickListener.onItemClick(null, v, -1, -1, drawerItem);
        }

        if (!consumed) {
            //close the drawer after click
            drawer.closeDrawerDelayed();
        }
    }

    /**
     * helper method to set the selection in the lsit
     *
     * @param drawer
     * @param position
     * @param fireOnClick
     * @return
     */
    public static boolean setListSelection(DrawerBuilder drawer, int position, boolean fireOnClick) {
        return setListSelection(drawer, position, fireOnClick, null);
    }

    /**
     * helper method to set the selection in the list
     *
     * @param drawer
     * @param position
     * @param fireOnClick
     * @param drawerItem
     * @return
     */
    public static boolean setListSelection(DrawerBuilder drawer, int position, boolean fireOnClick, IDrawerItem drawerItem) {
        if (position >= -1) {
            //predefine selection (should be the first element
            if (drawer.mListView != null && (position + drawer.mHeaderOffset) > -1) {
                drawer.resetStickyFooterSelection();
                drawer.mListView.setSelection(position + drawer.mHeaderOffset);
                drawer.mListView.setItemChecked(position + drawer.mHeaderOffset, true);
                drawer.mCurrentSelection = position;
                drawer.mCurrentFooterSelection = -1;
            }

            if (fireOnClick && drawer.mOnDrawerItemClickListener != null) {
                return drawer.mOnDrawerItemClickListener.onItemClick(null, null, position, -1, drawerItem);
            }
        }

        return false;
    }

    /**
     * helper method to set the selection of the footer
     *
     * @param drawer
     * @param position
     * @param fireOnClick
     */
    public static void setFooterSelection(DrawerBuilder drawer, int position, boolean fireOnClick) {
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
    public static int getPositionFromIdentifier(DrawerBuilder drawer, int identifier) {
        if (identifier >= 0) {
            if (drawer.mDrawerItems != null) {
                int position = 0;
                for (IDrawerItem i : drawer.mDrawerItems) {
                    if (i.getIdentifier() == identifier) {
                        return position;
                    }
                    position = position + 1;
                }
            }
        }

        return -1;
    }

    /**
     * calculates the position of an drawerItem inside the footer. searching by it's identifier
     *
     * @param identifier
     * @return
     */
    public static int getFooterPositionFromIdentifier(DrawerBuilder drawer, int identifier) {
        if (identifier >= 0) {
            if (drawer.mStickyFooterView != null && drawer.mStickyFooterView instanceof LinearLayout) {
                LinearLayout footer = (LinearLayout) drawer.mStickyFooterView;

                for (int i = 0; i < footer.getChildCount(); i++) {
                    Object o = footer.getChildAt(i).getTag();
                    if (o != null && o instanceof IDrawerItem && ((IDrawerItem) o).getIdentifier() == identifier) {
                        return i;
                    }
                }
            }
        }

        return -1;
    }

    /**
     * helper method to set the TranslucentStatusFlag
     *
     * @param on
     */
    public static void setTranslucentStatusFlag(Activity activity, boolean on) {
        if (Build.VERSION.SDK_INT >= 19) {
            setFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, on);
        }
    }

    /**
     * helper method to set the TranslucentNavigationFlag
     *
     * @param on
     */
    public static void setTranslucentNavigationFlag(Activity activity, boolean on) {
        if (Build.VERSION.SDK_INT >= 19) {
            setFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, on);
        }
    }

    /**
     * helper method to activate or deactivate a specific flag
     *
     * @param bits
     * @param on
     */
    public static void setFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
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
            }
        }

        //sticky header view
        if (drawer.mStickyHeaderView != null) {
            //add the sticky footer view and align it to the bottom
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
            drawer.mStickyHeaderView.setId(R.id.sticky_header);
            drawer.mSliderLayout.addView(drawer.mStickyHeaderView, 0, layoutParams);

            //now align the listView above the stickyFooterView ;)
            RelativeLayout.LayoutParams layoutParamsListView = (RelativeLayout.LayoutParams) drawer.mListView.getLayoutParams();
            layoutParamsListView.addRule(RelativeLayout.BELOW, R.id.sticky_header);
            drawer.mListView.setLayoutParams(layoutParamsListView);

            //remove the padding of the listView again we have the header on top of it
            drawer.mListView.setPadding(0, 0, 0, 0);
        }

        // set the header (do this before the setAdapter because some devices will crash else
        if (drawer.mHeaderView != null) {
            if (drawer.mListView == null) {
                throw new RuntimeException("can't use a headerView without a listView");
            }

            if (drawer.mHeaderDivider) {
                LinearLayout headerContainer = (LinearLayout) drawer.mActivity.getLayoutInflater().inflate(R.layout.material_drawer_item_header, drawer.mListView, false);
                headerContainer.addView(drawer.mHeaderView, 0);
                //set the color for the divider
                headerContainer.findViewById(R.id.divider).setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(drawer.mActivity, R.attr.material_drawer_divider, R.color.material_drawer_divider));
                //add the headerContainer to the list
                drawer.mListView.addHeaderView(headerContainer, null, drawer.mHeaderClickable);
                //link the view including the container to the headerView field
                drawer.mHeaderView = headerContainer;
            } else {
                drawer.mListView.addHeaderView(drawer.mHeaderView, null, drawer.mHeaderClickable);
            }
            //set the padding on the top to 0
            drawer.mListView.setPadding(drawer.mListView.getPaddingLeft(), 0, drawer.mListView.getPaddingRight(), drawer.mListView.getPaddingBottom());
        }
    }

    /**
     * small helper to rebuild the FooterView
     *
     * @param drawer
     */
    public static void rebuildFooterView(final DrawerBuilder drawer) {
        if (drawer.mSliderLayout != null) {
            if (drawer.mStickyFooterView != null && drawer.mStickyFooterView instanceof ViewGroup) {
                ((LinearLayout) drawer.mStickyFooterView).removeAllViews();
            }

            //handle the footer
            DrawerUtils.fillStickyDrawerItemFooter(drawer, drawer.mStickyFooterView, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IDrawerItem drawerItem = (IDrawerItem) v.getTag();
                    DrawerUtils.onFooterDrawerItemClick(drawer, drawerItem, v, true);
                }
            });

            setFooterSelection(drawer, drawer.mCurrentFooterSelection, false);
        }
    }

    /**
     * helper method to handle the footerView
     *
     * @param drawer
     */
    public static void handleFooterView(DrawerBuilder drawer, View.OnClickListener onClickListener) {
        //use the StickyDrawerItems if set
        if (drawer.mStickyDrawerItems != null && drawer.mStickyDrawerItems.size() > 0) {
            drawer.mStickyFooterView = DrawerUtils.buildStickyDrawerItemFooter(drawer, onClickListener);
        }

        //sticky footer view
        if (drawer.mStickyFooterView != null) {
            //add the sticky footer view and align it to the bottom
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
            drawer.mStickyFooterView.setId(R.id.sticky_footer);
            drawer.mSliderLayout.addView(drawer.mStickyFooterView, layoutParams);

            if ((drawer.mTranslucentNavigationBar || drawer.mFullscreen) && Build.VERSION.SDK_INT >= 19) {
                drawer.mStickyFooterView.setPadding(0, 0, 0, UIUtils.getNavigationBarHeight(drawer.mActivity));
            }

            //now align the listView above the stickyFooterView ;)
            RelativeLayout.LayoutParams layoutParamsListView = (RelativeLayout.LayoutParams) drawer.mListView.getLayoutParams();
            layoutParamsListView.addRule(RelativeLayout.ABOVE, R.id.sticky_footer);
            drawer.mListView.setLayoutParams(layoutParamsListView);

            //remove the padding of the listView again we have the footer below it
            drawer.mListView.setPadding(drawer.mListView.getPaddingLeft(), drawer.mListView.getPaddingTop(), drawer.mListView.getPaddingRight(), drawer.mActivity.getResources().getDimensionPixelSize(R.dimen.material_drawer_padding));
        }

        // set the footer (do this before the setAdapter because some devices will crash else
        if (drawer.mFooterView != null) {
            if (drawer.mListView == null) {
                throw new RuntimeException("can't use a footerView without a listView");
            }

            if (drawer.mFooterDivider) {
                LinearLayout footerContainer = (LinearLayout) drawer.mActivity.getLayoutInflater().inflate(R.layout.material_drawer_item_footer, drawer.mListView, false);
                footerContainer.addView(drawer.mFooterView, 1);
                //set the color for the divider
                footerContainer.findViewById(R.id.divider).setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(drawer.mActivity, R.attr.material_drawer_divider, R.color.material_drawer_divider));
                //add the footerContainer to the list
                drawer.mListView.addFooterView(footerContainer, null, drawer.mFooterClickable);
                //link the view including the container to the footerView field
                drawer.mFooterView = footerContainer;
            } else {
                drawer.mListView.addFooterView(drawer.mFooterView, null, drawer.mFooterClickable);
            }
        }
    }


    /**
     * build the sticky footer item view
     *
     * @return
     */
    public static ViewGroup buildStickyDrawerItemFooter(DrawerBuilder drawer, View.OnClickListener onClickListener) {
        //create the container view
        final LinearLayout linearLayout = new LinearLayout(drawer.mActivity);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        //set the background color to the drawer background color (if it has alpha the shadow won't be visible)
        linearLayout.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(drawer.mActivity, R.attr.material_drawer_background, R.color.material_drawer_background));

        if (Build.VERSION.SDK_INT >= 21) {
            //set the elevation shadow
            linearLayout.setElevation(UIUtils.convertDpToPixel(4f, drawer.mActivity));
        } else {
            //if we use the default values and we are on a older sdk version we want the divider
            if (drawer.mStickyFooterDivider == null) {
                drawer.mStickyFooterDivider = true;
            }
        }

        //create the divider
        if (drawer.mStickyFooterDivider != null && drawer.mStickyFooterDivider) {
            LinearLayout divider = new LinearLayout(drawer.mActivity);
            LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //remove bottomMargin --> See inbox it also has no margin here
            //dividerParams.bottomMargin = mActivity.getResources().getDimensionPixelSize(R.dimen.material_drawer_padding);
            divider.setMinimumHeight((int) UIUtils.convertDpToPixel(1, drawer.mActivity));
            divider.setOrientation(LinearLayout.VERTICAL);
            divider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(drawer.mActivity, R.attr.material_drawer_divider, R.color.material_drawer_divider));
            linearLayout.addView(divider, dividerParams);
        }

        fillStickyDrawerItemFooter(drawer, linearLayout, onClickListener);

        return linearLayout;
    }

    /**
     * helper method to fill the sticky footer with it's elements
     *
     * @param drawer
     * @param container
     * @param onClickListener
     */
    public static void fillStickyDrawerItemFooter(DrawerBuilder drawer, ViewGroup container, View.OnClickListener onClickListener) {
        //get the inflater
        LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());

        //add all drawer items
        for (IDrawerItem drawerItem : drawer.mStickyDrawerItems) {
            //get the selected_color
            int selected_color = UIUtils.getThemeColorFromAttrOrRes(container.getContext(), R.attr.material_drawer_selected, R.color.material_drawer_selected);
            if (drawerItem instanceof PrimaryDrawerItem) {
                if (selected_color == 0 && ((PrimaryDrawerItem) drawerItem).getSelectedColorRes() != -1) {
                    selected_color = container.getContext().getResources().getColor(((PrimaryDrawerItem) drawerItem).getSelectedColorRes());
                } else if (((PrimaryDrawerItem) drawerItem).getSelectedColor() != 0) {
                    selected_color = ((PrimaryDrawerItem) drawerItem).getSelectedColor();
                }
            } else if (drawerItem instanceof SecondaryDrawerItem) {
                if (selected_color == 0 && ((SecondaryDrawerItem) drawerItem).getSelectedColorRes() != -1) {
                    selected_color = container.getContext().getResources().getColor(((SecondaryDrawerItem) drawerItem).getSelectedColorRes());
                } else if (((SecondaryDrawerItem) drawerItem).getSelectedColor() != 0) {
                    selected_color = ((SecondaryDrawerItem) drawerItem).getSelectedColor();
                }
            }

            View view = drawerItem.convertView(layoutInflater, null, container);
            view.setTag(drawerItem);

            if (drawerItem.isEnabled()) {
                UIUtils.setBackground(view, UIUtils.getSelectableBackground(container.getContext(), selected_color));
                view.setOnClickListener(onClickListener);
            }

            container.addView(view);
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

            if (drawer.mTranslucentActionBarCompatibility) {
                int topMargin = UIUtils.getActionBarHeight(drawer.mActivity);
                if (drawer.mTranslucentStatusBar) {
                    topMargin = topMargin + UIUtils.getStatusBarHeight(drawer.mActivity);
                }
                params.topMargin = topMargin;
            } else if (drawer.mDisplayBelowStatusBar != null && drawer.mDisplayBelowStatusBar) {
                params.topMargin = UIUtils.getStatusBarHeight(drawer.mActivity, true);
            }

            if (drawer.mDrawerWidth > -1) {
                params.width = drawer.mDrawerWidth;
            } else {
                params.width = UIUtils.getOptimalDrawerWidth(drawer.mActivity);
            }
        }

        return params;
    }
}
