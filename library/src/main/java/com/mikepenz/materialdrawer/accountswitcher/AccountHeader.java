package com.mikepenz.materialdrawer.accountswitcher;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
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
public class AccountHeader {

    // global references to views we need later
    protected View mAccountHeader;
    protected CircularImageView mCurrentProfileView;
    protected View mAccountHeaderTextSection;
    protected ImageView mAccountSwitcherArrow;
    protected TextView mCurrentProfileName;
    protected TextView mCurrentProfileEmail;
    protected CircularImageView mProfileFirstView;
    protected CircularImageView mProfileSecondView;
    protected CircularImageView mProfileThirdView;

    // global references to the profiles
    protected Profile mCurrentProfile;
    protected Profile mProfileFirst;
    protected Profile mProfileSecond;
    protected Profile mProfileThird;


    // the activity to use
    protected Activity mActivity;

    /**
     * Pass the activity you use the drawer in ;)
     *
     * @param activity
     * @return
     */
    public AccountHeader withActivity(Activity activity) {
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
    public AccountHeader withHeightPx(int heightPx) {
        this.mHeightPx = heightPx;
        return this;
    }


    /**
     * set the height for the header
     *
     * @param heightDp
     * @return
     */
    public AccountHeader withHeightDp(int heightDp) {
        this.mHeightDp = heightDp;
        return this;
    }

    /**
     * set the height for the header by ressource
     *
     * @param heightRes
     * @return
     */
    public AccountHeader withHeightRes(int heightRes) {
        this.mHeightRes = heightRes;
        return this;
    }

    //the background color for the slider
    protected int mTextColor = -1;
    protected int mTextColorRes = -1;

    /**
     * set the background for the slider as color
     *
     * @param textColor
     * @return
     */
    public AccountHeader withTextColor(int textColor) {
        this.mTextColor = textColor;
        return this;
    }

    /**
     * set the background for the slider as resource
     *
     * @param textColorRes
     * @return
     */
    public AccountHeader withTextColorRes(int textColorRes) {
        this.mTextColorRes = textColorRes;
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
    public AccountHeader withTranslucentStatusBar(boolean translucentStatusBar) {
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
    public AccountHeader withHeaderBackground(Drawable headerBackground) {
        this.mHeaderBackground = headerBackground;
        return this;
    }

    /**
     * set the background for the header as resource
     *
     * @param headerBackgroundRes
     * @return
     */
    public AccountHeader withHeaderBackground(int headerBackgroundRes) {
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
    public AccountHeader withAccountHeader(View accountHeader) {
        this.mAccountHeaderContainer = accountHeader;
        return this;
    }

    /**
     * You can pass a custom layout for the drawer lib. see the drawer.xml in layouts of this lib on GitHub
     *
     * @param resLayout
     * @return
     */
    public AccountHeader withAccountHeader(int resLayout) {
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
    public AccountHeader withProfiles(ArrayList<Profile> profiles) {
        this.mProfiles = profiles;
        return this;
    }

    /**
     * add single ore more DrawerItems to the Drawer
     *
     * @param profiles
     * @return
     */
    public AccountHeader addProfiles(Profile... profiles) {
        if (this.mProfiles == null) {
            this.mProfiles = new ArrayList<>();
        }

        if (profiles != null) {
            Collections.addAll(this.mProfiles, profiles);
        }
        return this;
    }

    // the click listener to be fired on profile or selection click
    protected OnAccountHeaderClickListener mOnAccountHeaderClickListener;

    /**
     * add a click listener for the accountHeader
     *
     * @param onAccountHeaderClickListener
     * @return
     */
    public AccountHeader withOnAccountHeaderClickListener(OnAccountHeaderClickListener onAccountHeaderClickListener) {
        this.mOnAccountHeaderClickListener = onAccountHeaderClickListener;
        return this;
    }

    // the drawer to set the AccountSwitcher for
    protected Drawer.Result mDrawer;

    /**
     * @param drawer
     * @return
     */
    public AccountHeader withDrawer(Drawer.Result drawer) {
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
        // if the user has not set a accountHeader use the default one :D
        if (mAccountHeaderContainer == null) {
            withAccountHeader(-1);
        }

        // get the header view within the container
        mAccountHeader = mAccountHeaderContainer.findViewById(R.id.account_header_drawer);

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
            mAccountHeader.setPadding(0, mActivity.getResources().getDimensionPixelSize(R.dimen.tool_bar_top_padding), 0, 0);
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

        // get the text color to use for the text section
        int textColor = mTextColor;
        if (textColor == -1 && mTextColorRes != -1) {
            textColor = mActivity.getResources().getColor(mTextColorRes);
        } else {
            textColor = mActivity.getResources().getColor(R.color.material_drawer_icons);
        }

        // set the background for the section
        mAccountHeaderTextSection = mAccountHeaderContainer.findViewById(R.id.account_header_drawer_text_section);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // If we're running on Honeycomb or newer, then we can use the Theme's
            // selectableItemBackground to ensure that the View has a pressed state
            TypedValue outValue = new TypedValue();
            mActivity.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            mAccountHeaderTextSection.setBackgroundResource(outValue.resourceId);
        } else {
            TypedValue outValue = new TypedValue();
            mActivity.getTheme().resolveAttribute(android.R.attr.itemBackground, outValue, true);
            mAccountHeaderTextSection.setBackgroundResource(outValue.resourceId);
        }
        mAccountHeaderTextSection.setOnClickListener(onSelectionClickListener);

        // set the arrow :D
        mAccountSwitcherArrow = (ImageView) mAccountHeaderContainer.findViewById(R.id.account_header_drawer_text_switcher);
        mAccountSwitcherArrow.setImageDrawable(new IconicsDrawable(mActivity, GoogleMaterial.Icon.gmd_arrow_drop_down).sizeDp(24).paddingDp(6).color(textColor));

        //get the fields for the name
        mCurrentProfileView = (CircularImageView) mAccountHeader.findViewById(R.id.account_header_drawer_current);
        mCurrentProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mActivity, "Wuhu", Toast.LENGTH_LONG).show();
            }
        });
        mCurrentProfileName = (TextView) mAccountHeader.findViewById(R.id.account_header_drawer_name);
        mCurrentProfileEmail = (TextView) mAccountHeader.findViewById(R.id.account_header_drawer_email);

        mCurrentProfileName.setTextColor(textColor);
        mCurrentProfileEmail.setTextColor(textColor);

        mProfileFirstView = (CircularImageView) mAccountHeader.findViewById(R.id.account_header_drawer_small_first);
        mProfileSecondView = (CircularImageView) mAccountHeader.findViewById(R.id.account_header_drawer_small_second);
        mProfileThirdView = (CircularImageView) mAccountHeader.findViewById(R.id.account_header_drawer_small_third);

        //set the active profiles
        if (mProfiles != null) {
            if (mProfiles.size() > 0) {
                mCurrentProfile = mProfiles.get(0);
            }
            if (mProfiles.size() > 1) {
                mProfileFirst = mProfiles.get(1);
            }
            if (mProfiles.size() > 2) {
                mProfileSecond = mProfiles.get(2);
            }
            if (mProfiles.size() > 3) {
                mProfileThird = mProfiles.get(3);
            }
        }

        //process and build the profiles
        buildProfiles();

        //forget the reference to the activity
        mActivity = null;

        return new Result(this);
    }

    protected void switchProfiles(Profile newSelection) {
        if (mCurrentProfile == newSelection) {
            return;
        }

        int prevSelection = -1;
        if (mProfileFirst == newSelection) {
            prevSelection = 1;
        } else if (mProfileSecond == newSelection) {
            prevSelection = 2;
        } else if (mProfileThird == newSelection) {
            prevSelection = 3;
        }

        Profile tmp = mCurrentProfile;
        mCurrentProfile = newSelection;

        if (prevSelection == 1) {
            mProfileFirst = tmp;
        } else if (prevSelection == 2) {
            mProfileSecond = tmp;
        } else if (prevSelection == 3) {
            mProfileThird = tmp;
        }

        buildProfiles();
    }

    protected void buildProfiles() {
        mCurrentProfileView.setVisibility(View.GONE);
        mAccountHeaderTextSection.setVisibility(View.GONE);
        mAccountSwitcherArrow.setVisibility(View.GONE);
        mProfileFirstView.setVisibility(View.GONE);
        mProfileSecondView.setVisibility(View.GONE);
        mProfileThirdView.setVisibility(View.GONE);

        if (mCurrentProfile != null) {
            mCurrentProfileView.setImageDrawable(mCurrentProfile.getImage());
            mCurrentProfileView.setTag(mCurrentProfile);
            mCurrentProfileView.setOnClickListener(onProfileClickListener);
            mCurrentProfileView.setVisibility(View.VISIBLE);
            mAccountHeaderTextSection.setTag(mCurrentProfile);
            mAccountHeaderTextSection.setVisibility(View.VISIBLE);
            mCurrentProfileName.setText(mCurrentProfile.getName());
            mCurrentProfileEmail.setText(mCurrentProfile.getEmail());

            if (mProfileFirst != null) {
                mAccountSwitcherArrow.setVisibility(View.VISIBLE);
                mProfileFirstView.setImageDrawable(mProfileFirst.getImage());
                mProfileFirstView.setTag(mProfileFirst);
                mProfileFirstView.setOnClickListener(onProfileClickListener);
                mProfileFirstView.setVisibility(View.VISIBLE);
            }
            if (mProfileSecond != null) {
                mProfileSecondView.setImageDrawable(mProfileSecond.getImage());
                mProfileSecondView.setTag(mProfileSecond);
                mProfileSecondView.setOnClickListener(onProfileClickListener);
                mProfileSecondView.setVisibility(View.VISIBLE);
            }
            if (mProfileThird != null) {
                mProfileThirdView.setImageDrawable(mProfileThird.getImage());
                mProfileThirdView.setTag(mProfileThird);
                mProfileThirdView.setOnClickListener(onProfileClickListener);
                mProfileThirdView.setVisibility(View.VISIBLE);
            }
        }
    }

    private View.OnClickListener onProfileClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnAccountHeaderClickListener != null) {
                Profile profile = (Profile) v.getTag();

                switchProfiles(profile);

                mOnAccountHeaderClickListener.onProfileClick(v, profile);
            }
        }
    };

    private View.OnClickListener onSelectionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnAccountHeaderClickListener != null) {
                mOnAccountHeaderClickListener.onSelectionClick(v, (Profile) v.getTag());
            }
        }
    };

    public static class Result {
        private final AccountHeader mAccountHeader;

        protected Result(AccountHeader accountHeader) {
            this.mAccountHeader = accountHeader;
        }

        public View getView() {
            return mAccountHeader.mAccountHeaderContainer;
        }
    }


    public interface OnAccountHeaderClickListener {
        public void onProfileClick(View view, Profile profile);

        public void onSelectionClick(View view, Profile currentProfile);
    }
}
