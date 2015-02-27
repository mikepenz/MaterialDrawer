package com.mikepenz.materialdrawer.accountswitcher;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.Iconics;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.utils.Utils;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.accountswitcher.model.Profile;
import com.mikepenz.materialdrawer.view.CircularImageView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by mikepenz on 27.02.15.
 */
public class AccountHeaderDrawer {

    // the activity to use
    protected Activity mActivity;

    /**
     * Pass the activity you use the drawer in ;)
     *
     * @param activity
     * @return
     */
    public AccountHeaderDrawer withActivity(Activity activity) {
        this.mActivity = activity;
        return this;
    }

    // set the account header height
    protected int mHeightPx = -1;
    protected int mHeightDp = -1;
    protected int mHeightRes = -1;

    /**
     * set the height for the header
     *
     * @param heightPx
     * @return
     */
    public AccountHeaderDrawer withHeightPx(int heightPx) {
        this.mHeightPx = heightPx;
        return this;
    }


    /**
     * set the height for the header
     *
     * @param heightDp
     * @return
     */
    public AccountHeaderDrawer withHeightDp(int heightDp) {
        this.mHeightDp = heightDp;
        return this;
    }

    /**
     * set the height for the header by ressource
     *
     * @param heightRes
     * @return
     */
    public AccountHeaderDrawer withHeightRes(int heightRes) {
        this.mHeightRes = heightRes;
        return this;
    }


    // set non translucent statusbar mode
    protected boolean mTranslucentStatusBar = true;

    /**
     * Set or disable this if you use a translucent statusbar
     *
     * @param translucentStatusBar
     * @return
     */
    public AccountHeaderDrawer withTranslucentStatusBar(boolean translucentStatusBar) {
        this.mTranslucentStatusBar = translucentStatusBar;
        return this;
    }

    //the background color for the header
    protected Drawable mHeaderBackground = null;
    protected int mHeaderBackgroundRes = -1;

    /**
     * set the background for the slider as color
     *
     * @param headerBackground
     * @return
     */
    public AccountHeaderDrawer withHeaderBackground(Drawable headerBackground) {
        this.mHeaderBackground = headerBackground;
        return this;
    }

    /**
     * set the background for the header as resource
     *
     * @param headerBackgroundRes
     * @return
     */
    public AccountHeaderDrawer withHeaderBackground(int headerBackgroundRes) {
        this.mHeaderBackgroundRes = headerBackgroundRes;
        return this;
    }


    // the drawerLayout to use
    protected View mAccountHeaderContainer;

    /**
     * You can pass a custom view for the drawer lib. note this requires the same structure as the drawer.xml
     *
     * @param accountHeader
     * @return
     */
    public AccountHeaderDrawer withAccountHeader(View accountHeader) {
        this.mAccountHeaderContainer = accountHeader;
        return this;
    }

    /**
     * You can pass a custom layout for the drawer lib. see the drawer.xml in layouts of this lib on GitHub
     *
     * @param resLayout
     * @return
     */
    public AccountHeaderDrawer withAccountHeader(int resLayout) {
        if (mActivity == null) {
            throw new RuntimeException("please pass an activity first to use this call");
        }

        if (resLayout != -1) {
            this.mAccountHeaderContainer = mActivity.getLayoutInflater().inflate(resLayout, null, false);
        } else {
            this.mAccountHeaderContainer = mActivity.getLayoutInflater().inflate(R.layout.drawer_header, null, false);
        }

        return this;
    }

    // the profiles to display
    protected ArrayList<Profile> mProfiles;

    /**
     * set the arrayList of DrawerItems for the drawer
     *
     * @param profiles
     * @return
     */
    public AccountHeaderDrawer withProfiles(ArrayList<Profile> profiles) {
        this.mProfiles = profiles;
        return this;
    }

    /**
     * add single ore more DrawerItems to the Drawer
     *
     * @param profiles
     * @return
     */
    public AccountHeaderDrawer addProfiles(Profile... profiles) {
        if (this.mProfiles == null) {
            this.mProfiles = new ArrayList<>();
        }

        if (profiles != null) {
            Collections.addAll(this.mProfiles, profiles);
        }
        return this;
    }

    // the drawer to set the AccountSwitcher for
    protected Drawer.Result mDrawer;

    /**
     * @param drawer
     * @return
     */
    public AccountHeaderDrawer withDrawer(Drawer.Result drawer) {
        this.mDrawer = drawer;
        return this;
    }

    /**
     * helper method to set the height for the header!
     *
     * @param height
     */
    private void setHeaderHeight(int height) {
        if (mAccountHeaderContainer != null) {
            ViewGroup.LayoutParams params = mAccountHeaderContainer.getLayoutParams();
            if (params != null) {
                params.height = height;
                mAccountHeaderContainer.setLayoutParams(params);
            }

            View accountHeader = mAccountHeaderContainer.findViewById(R.id.account_header_drawer);
            if (accountHeader != null) {
                params = accountHeader.getLayoutParams();
                params.height = height;
                accountHeader.setLayoutParams(params);
            }

            View accountHeaderBackground = mAccountHeaderContainer.findViewById(R.id.account_header_drawer_background);
            if (accountHeaderBackground != null) {
                params = accountHeaderBackground.getLayoutParams();
                params.height = height;
                accountHeaderBackground.setLayoutParams(params);
            }
        }
    }

    /**
     * @return
     */
    public Result build() {
        //register the google material design icons
        Iconics.registerFont(new GoogleMaterial());

        // if the user has not set a accountHeader use the default one :D
        if (mAccountHeaderContainer == null) {
            withAccountHeader(-1);
        }

        // get the header view within the container
        View accountHeader = mAccountHeaderContainer.findViewById(R.id.account_header_drawer);

        // handle the height for the header
        int height = -1;
        if (mHeightPx != -1) {
            height = mHeightPx;
        } else if (mHeightDp != -1) {
            height = Utils.convertDpToPx(mActivity, mHeightDp);
        } else if (mHeightRes != -1) {
            height = mActivity.getResources().getDimensionPixelSize(mHeightRes);
        } else {
            height = mActivity.getResources().getDimensionPixelSize(R.dimen.material_drawer_account_header_height);
        }

        // handle everything if we don't have a translucent status bar
        if (mTranslucentStatusBar) {
            accountHeader.setPadding(0, mActivity.getResources().getDimensionPixelSize(R.dimen.tool_bar_top_padding), 0, 0);
            height = height + mActivity.getResources().getDimensionPixelSize(R.dimen.tool_bar_top_padding);
        }

        //set the height for the header
        setHeaderHeight(height);

        // get the background view
        ImageView accountHeaderBackground = (ImageView) mAccountHeaderContainer.findViewById(R.id.account_header_drawer_background);

        // set the background
        if (mHeaderBackground != null) {
            accountHeaderBackground.setImageDrawable(mHeaderBackground);
        } else if (mHeaderBackgroundRes != -1) {
            accountHeaderBackground.setImageResource(mHeaderBackgroundRes);
        }

        //get the fields for the name
        CircularImageView selectedProfileText = (CircularImageView) accountHeader.findViewById(R.id.account_header_drawer_current);
        TextView selectedProfileName = (TextView) accountHeader.findViewById(R.id.account_header_drawer_name);
        TextView selectedProfileEmail = (TextView) accountHeader.findViewById(R.id.account_header_drawer_email);

        CircularImageView civ2 = (CircularImageView) accountHeader.findViewById(R.id.account_header_drawer_small_first);
        CircularImageView civ3 = (CircularImageView) accountHeader.findViewById(R.id.account_header_drawer_small_second);

        if (mProfiles != null && mProfiles.size() > 0) {
            Profile currentProfile = mProfiles.get(0);

            selectedProfileText.setImageDrawable(currentProfile.getImage());
            selectedProfileName.setText(currentProfile.getName());
            selectedProfileEmail.setText(currentProfile.getEmail());

            civ2.setImageDrawable(currentProfile.getImage());
            civ3.setImageDrawable(currentProfile.getImage());

            //show the arrow down if there is more than 1 account
            if (mProfiles.size() > 1) {
                ImageView accountSwitcherArrow = (ImageView) mAccountHeaderContainer.findViewById(R.id.account_header_drawer_text_switcher);
                accountSwitcherArrow.setImageDrawable(new IconicsDrawable(mActivity, GoogleMaterial.Icon.gmd_arrow_drop_down).sizeDp(24).paddingDp(6).color(Color.WHITE));
            }
        }


        return new Result(this);
    }

    public static class Result {
        private final AccountHeaderDrawer mAccountHeaderDrawer;

        protected Result(AccountHeaderDrawer accountHeaderDrawer) {
            this.mAccountHeaderDrawer = accountHeaderDrawer;
        }

        public View getView() {
            return mAccountHeaderDrawer.mAccountHeaderContainer;
        }
    }
}
