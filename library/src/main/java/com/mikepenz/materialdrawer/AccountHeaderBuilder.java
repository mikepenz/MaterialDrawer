package com.mikepenz.materialdrawer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.content.res.AppCompatResources;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.holder.ColorHolder;
import com.mikepenz.materialdrawer.holder.DimenHolder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.icons.MaterialDrawerFont;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialdrawer.view.BezelImageView;
import com.mikepenz.materialize.util.UIUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Created by mikepenz on 23.05.15.
 */
public class AccountHeaderBuilder {
    // global references to views we need later
    protected View mAccountHeader;
    protected ImageView mAccountHeaderBackground;
    protected BezelImageView mCurrentProfileView;
    protected View mAccountHeaderTextSection;
    protected ImageView mAccountSwitcherArrow;
    protected TextView mCurrentProfileName;
    protected TextView mCurrentProfileEmail;
    protected BezelImageView mProfileFirstView;
    protected BezelImageView mProfileSecondView;
    protected BezelImageView mProfileThirdView;

    // global references to the profiles
    protected IProfile mCurrentProfile;
    protected IProfile mProfileFirst;
    protected IProfile mProfileSecond;
    protected IProfile mProfileThird;


    // global stuff
    protected boolean mSelectionListShown = false;
    protected int mAccountHeaderTextSectionBackgroundResource = -1;

    // the activity to use
    protected Activity mActivity;

    /**
     * Pass the activity you use the drawer in ;)
     *
     * @param activity
     * @return
     */
    public AccountHeaderBuilder withActivity(@NonNull Activity activity) {
        this.mActivity = activity;
        return this;
    }

    // defines if we use the compactStyle
    protected boolean mCompactStyle = false;

    /**
     * Defines if we should use the compact style for the header.
     *
     * @param compactStyle
     * @return
     */
    public AccountHeaderBuilder withCompactStyle(boolean compactStyle) {
        this.mCompactStyle = compactStyle;
        return this;
    }

    // the typeface used for textViews within the AccountHeader
    protected Typeface mTypeface;

    // the typeface used for name textView only. overrides mTypeface
    protected Typeface mNameTypeface;

    // the typeface used for email textView only. overrides mTypeface
    protected Typeface mEmailTypeface;

    /**
     * Define the typeface which will be used for all textViews in the AccountHeader
     *
     * @param typeface
     * @return
     */
    public AccountHeaderBuilder withTypeface(@NonNull Typeface typeface) {
        this.mTypeface = typeface;
        return this;
    }

    /**
     * Define the typeface which will be used for name textView in the AccountHeader.
     * Overrides typeface supplied to {@link AccountHeaderBuilder#withTypeface(android.graphics.Typeface)}
     *
     * @param typeface
     * @return
     * @see #withTypeface(android.graphics.Typeface)
     */
    public AccountHeaderBuilder withNameTypeface(@NonNull Typeface typeface) {
        this.mNameTypeface = typeface;
        return this;
    }

    /**
     * Define the typeface which will be used for email textView in the AccountHeader.
     * Overrides typeface supplied to {@link AccountHeaderBuilder#withTypeface(android.graphics.Typeface)}
     *
     * @param typeface
     * @return
     * @see #withTypeface(android.graphics.Typeface)
     */
    public AccountHeaderBuilder withEmailTypeface(@NonNull Typeface typeface) {
        this.mEmailTypeface = typeface;
        return this;
    }

    // set the account header height
    protected DimenHolder mHeight;

    /**
     * set the height for the header
     *
     * @param heightPx
     * @return
     */
    public AccountHeaderBuilder withHeightPx(int heightPx) {
        this.mHeight = DimenHolder.fromPixel(heightPx);
        return this;
    }


    /**
     * set the height for the header
     *
     * @param heightDp
     * @return
     */
    public AccountHeaderBuilder withHeightDp(int heightDp) {
        this.mHeight = DimenHolder.fromDp(heightDp);
        return this;
    }

    /**
     * set the height for the header by resource
     *
     * @param heightRes
     * @return
     */
    public AccountHeaderBuilder withHeightRes(@DimenRes int heightRes) {
        this.mHeight = DimenHolder.fromResource(heightRes);
        return this;
    }

    //the background color for the slider
    protected ColorHolder mTextColor;

    /**
     * set the background for the slider as color
     *
     * @param textColor
     * @return
     */
    public AccountHeaderBuilder withTextColor(@ColorInt int textColor) {
        this.mTextColor = ColorHolder.fromColor(textColor);
        return this;
    }

    /**
     * set the background for the slider as resource
     *
     * @param textColorRes
     * @return
     */
    public AccountHeaderBuilder withTextColorRes(@ColorRes int textColorRes) {
        this.mTextColor = ColorHolder.fromColorRes(textColorRes);
        return this;
    }

    //the current selected profile is visible in the list
    protected boolean mCurrentHiddenInList = false;

    /**
     * hide the current selected profile from the list
     *
     * @param currentProfileHiddenInList
     * @return
     */
    public AccountHeaderBuilder withCurrentProfileHiddenInList(boolean currentProfileHiddenInList) {
        mCurrentHiddenInList = currentProfileHiddenInList;
        return this;
    }

    //set to hide the first or second line
    protected boolean mSelectionFirstLineShown = true;
    protected boolean mSelectionSecondLineShown = true;

    /**
     * set this to false if you want to hide the first line of the selection box in the header (first line would be the name)
     *
     * @param selectionFirstLineShown
     * @return
     * @deprecated replaced by {@link #withSelectionFirstLineShown}
     */
    @Deprecated
    public AccountHeaderBuilder withSelectionFistLineShown(boolean selectionFirstLineShown) {
        this.mSelectionFirstLineShown = selectionFirstLineShown;
        return this;
    }

    /**
     * set this to false if you want to hide the first line of the selection box in the header (first line would be the name)
     *
     * @param selectionFirstLineShown
     * @return
     */
    public AccountHeaderBuilder withSelectionFirstLineShown(boolean selectionFirstLineShown) {
        this.mSelectionFirstLineShown = selectionFirstLineShown;
        return this;
    }

    /**
     * set this to false if you want to hide the second line of the selection box in the header (second line would be the e-mail)
     *
     * @param selectionSecondLineShown
     * @return
     */
    public AccountHeaderBuilder withSelectionSecondLineShown(boolean selectionSecondLineShown) {
        this.mSelectionSecondLineShown = selectionSecondLineShown;
        return this;
    }


    //set one of these to define the text in the first or second line with in the account selector
    protected String mSelectionFirstLine;
    protected String mSelectionSecondLine;

    /**
     * set this to define the first line in the selection area if there is no profile
     * note this will block any values from profiles!
     *
     * @param selectionFirstLine
     * @return
     */
    public AccountHeaderBuilder withSelectionFirstLine(String selectionFirstLine) {
        this.mSelectionFirstLine = selectionFirstLine;
        return this;
    }

    /**
     * set this to define the second line in the selection area if there is no profile
     * note this will block any values from profiles!
     *
     * @param selectionSecondLine
     * @return
     */
    public AccountHeaderBuilder withSelectionSecondLine(String selectionSecondLine) {
        this.mSelectionSecondLine = selectionSecondLine;
        return this;
    }

    // set no divider below the header
    protected boolean mPaddingBelowHeader = true;

    /**
     * Set this to false if you want no padding below the Header
     *
     * @param paddingBelowHeader
     * @return
     */
    public AccountHeaderBuilder withPaddingBelowHeader(boolean paddingBelowHeader) {
        this.mPaddingBelowHeader = paddingBelowHeader;
        return this;
    }

    // set no divider below the header
    protected boolean mDividerBelowHeader = true;

    /**
     * Set this to false if you want no divider below the Header
     *
     * @param dividerBelowHeader
     * @return
     */
    public AccountHeaderBuilder withDividerBelowHeader(boolean dividerBelowHeader) {
        this.mDividerBelowHeader = dividerBelowHeader;
        return this;
    }

    // set non translucent statusBar mode
    protected boolean mTranslucentStatusBar = true;

    /**
     * Set or disable this if you use a translucent statusbar
     *
     * @param translucentStatusBar
     * @return
     */
    public AccountHeaderBuilder withTranslucentStatusBar(boolean translucentStatusBar) {
        this.mTranslucentStatusBar = translucentStatusBar;
        return this;
    }

    //the background for the header
    protected ImageHolder mHeaderBackground;

    /**
     * set the background for the slider as color
     *
     * @param headerBackground
     * @return
     */
    public AccountHeaderBuilder withHeaderBackground(Drawable headerBackground) {
        this.mHeaderBackground = new ImageHolder(headerBackground);
        return this;
    }

    /**
     * set the background for the header as resource
     *
     * @param headerBackgroundRes
     * @return
     */
    public AccountHeaderBuilder withHeaderBackground(@DrawableRes int headerBackgroundRes) {
        this.mHeaderBackground = new ImageHolder(headerBackgroundRes);
        return this;
    }

    /**
     * set the background for the header via the ImageHolder class
     *
     * @param headerBackground
     * @return
     */
    public AccountHeaderBuilder withHeaderBackground(ImageHolder headerBackground) {
        this.mHeaderBackground = headerBackground;
        return this;
    }

    //background scale type
    protected ImageView.ScaleType mHeaderBackgroundScaleType = null;

    /**
     * define the ScaleType for the header background
     *
     * @param headerBackgroundScaleType
     * @return
     */
    public AccountHeaderBuilder withHeaderBackgroundScaleType(ImageView.ScaleType headerBackgroundScaleType) {
        this.mHeaderBackgroundScaleType = headerBackgroundScaleType;
        return this;
    }

    //profile images in the header are shown or not
    protected boolean mProfileImagesVisible = true;

    /**
     * define if the profile images in the header are shown or not
     *
     * @param profileImagesVisible
     * @return
     */
    public AccountHeaderBuilder withProfileImagesVisible(boolean profileImagesVisible) {
        this.mProfileImagesVisible = profileImagesVisible;
        return this;
    }

    //only the main profile image is visible
    protected boolean mOnlyMainProfileImageVisible = false;

    /**
     * define if only the main (current selected) profile image should be visible
     *
     * @param onlyMainProfileImageVisible
     * @return
     */
    public AccountHeaderBuilder withOnlyMainProfileImageVisible(boolean onlyMainProfileImageVisible) {
        this.mOnlyMainProfileImageVisible = onlyMainProfileImageVisible;
        return this;
    }

    //show small profile images but hide MainProfileImage
    protected boolean mOnlySmallProfileImagesVisible = false;

    /**
     * define if only the small profile images should be visible
     *
     * @param onlySmallProfileImagesVisible
     * @return
     */
    public AccountHeaderBuilder withOnlySmallProfileImagesVisible(boolean onlySmallProfileImagesVisible) {
        this.mOnlySmallProfileImagesVisible = onlySmallProfileImagesVisible;
        return this;
    }

    //close the drawer after a profile was clicked in the list
    protected Boolean mCloseDrawerOnProfileListClick = null;

    /**
     * define if the drawer should close if the user clicks on a profile item if the selection list is shown
     *
     * @param closeDrawerOnProfileListClick
     * @return
     */
    public AccountHeaderBuilder withCloseDrawerOnProfileListClick(boolean closeDrawerOnProfileListClick) {
        this.mCloseDrawerOnProfileListClick = closeDrawerOnProfileListClick;
        return this;
    }

    //reset the drawer list to the main drawer list after the profile was clicked in the list
    protected boolean mResetDrawerOnProfileListClick = true;

    /**
     * define if the drawer selection list should be reseted after the user clicks on a profile item if the selection list is shown
     *
     * @param resetDrawerOnProfileListClick
     * @return
     */
    public AccountHeaderBuilder withResetDrawerOnProfileListClick(boolean resetDrawerOnProfileListClick) {
        this.mResetDrawerOnProfileListClick = resetDrawerOnProfileListClick;
        return this;
    }

    // set the profile images clickable or not
    protected boolean mProfileImagesClickable = true;

    /**
     * enable or disable the profile images to be clickable
     *
     * @param profileImagesClickable
     * @return
     */
    public AccountHeaderBuilder withProfileImagesClickable(boolean profileImagesClickable) {
        this.mProfileImagesClickable = profileImagesClickable;
        return this;
    }

    // set to use the alternative profile header switching
    protected boolean mAlternativeProfileHeaderSwitching = false;

    /**
     * enable the alternative profile header switching
     *
     * @param alternativeProfileHeaderSwitching
     * @return
     */
    public AccountHeaderBuilder withAlternativeProfileHeaderSwitching(boolean alternativeProfileHeaderSwitching) {
        this.mAlternativeProfileHeaderSwitching = alternativeProfileHeaderSwitching;
        return this;
    }

    // enable 3 small header previews
    protected boolean mThreeSmallProfileImages = false;

    /**
     * enable the extended profile icon view with 3 small header images instead of two
     *
     * @param threeSmallProfileImages
     * @return
     */
    public AccountHeaderBuilder withThreeSmallProfileImages(boolean threeSmallProfileImages) {
        this.mThreeSmallProfileImages = threeSmallProfileImages;
        return this;
    }

    //the delay which is waited before the drawer is closed
    protected int mOnProfileClickDrawerCloseDelay = 100;

    /**
     * Define the delay for the drawer close operation after a click.
     * This is a small trick to improve the speed (and remove lag) if you open a new activity after a DrawerItem
     * was selected.
     * NOTE: Disable this by passing -1
     *
     * @param onProfileClickDrawerCloseDelay the delay in MS (-1 to disable)
     * @return
     */
    public AccountHeaderBuilder withOnProfileClickDrawerCloseDelay(int onProfileClickDrawerCloseDelay) {
        this.mOnProfileClickDrawerCloseDelay = onProfileClickDrawerCloseDelay;
        return this;
    }

    // the onAccountHeaderProfileImageListener to set
    protected AccountHeader.OnAccountHeaderProfileImageListener mOnAccountHeaderProfileImageListener;

    /**
     * set click / longClick listener for the header images
     *
     * @param onAccountHeaderProfileImageListener
     * @return
     */
    public AccountHeaderBuilder withOnAccountHeaderProfileImageListener(AccountHeader.OnAccountHeaderProfileImageListener onAccountHeaderProfileImageListener) {
        this.mOnAccountHeaderProfileImageListener = onAccountHeaderProfileImageListener;
        return this;
    }

    // the onAccountHeaderSelectionListener to set
    protected AccountHeader.OnAccountHeaderSelectionViewClickListener mOnAccountHeaderSelectionViewClickListener;

    /**
     * set a onSelection listener for the selection box
     *
     * @param onAccountHeaderSelectionViewClickListener
     * @return
     */
    public AccountHeaderBuilder withOnAccountHeaderSelectionViewClickListener(AccountHeader.OnAccountHeaderSelectionViewClickListener onAccountHeaderSelectionViewClickListener) {
        this.mOnAccountHeaderSelectionViewClickListener = onAccountHeaderSelectionViewClickListener;
        return this;
    }

    //set the selection list enabled if there is only a single profile
    protected boolean mSelectionListEnabledForSingleProfile = true;

    /**
     * enable or disable the selection list if there is only a single profile
     *
     * @param selectionListEnabledForSingleProfile
     * @return
     */
    public AccountHeaderBuilder withSelectionListEnabledForSingleProfile(boolean selectionListEnabledForSingleProfile) {
        this.mSelectionListEnabledForSingleProfile = selectionListEnabledForSingleProfile;
        return this;
    }

    //set the selection enabled disabled
    protected boolean mSelectionListEnabled = true;

    /**
     * enable or disable the selection list
     *
     * @param selectionListEnabled
     * @return
     */
    public AccountHeaderBuilder withSelectionListEnabled(boolean selectionListEnabled) {
        this.mSelectionListEnabled = selectionListEnabled;
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
    public AccountHeaderBuilder withAccountHeader(@NonNull View accountHeader) {
        this.mAccountHeaderContainer = accountHeader;
        return this;
    }

    /**
     * You can pass a custom layout for the drawer lib. see the drawer.xml in layouts of this lib on GitHub
     *
     * @param resLayout
     * @return
     */
    public AccountHeaderBuilder withAccountHeader(@LayoutRes int resLayout) {
        if (mActivity == null) {
            throw new RuntimeException("please pass an activity first to use this call");
        }

        if (resLayout != -1) {
            this.mAccountHeaderContainer = mActivity.getLayoutInflater().inflate(resLayout, null, false);
        } else {
            if (mCompactStyle) {
                this.mAccountHeaderContainer = mActivity.getLayoutInflater().inflate(R.layout.material_drawer_compact_header, null, false);
            } else {
                this.mAccountHeaderContainer = mActivity.getLayoutInflater().inflate(R.layout.material_drawer_header, null, false);
            }
        }

        return this;
    }

    // the profiles to display
    protected List<IProfile> mProfiles;

    /**
     * set the arrayList of DrawerItems for the drawer
     *
     * @param profiles
     * @return
     */
    public AccountHeaderBuilder withProfiles(@NonNull List<IProfile> profiles) {
        if (mDrawer != null) {
            mDrawer.mDrawerBuilder.idDistributor.checkIds(profiles);
        }
        this.mProfiles = profiles;
        return this;
    }

    /**
     * add single ore more DrawerItems to the Drawer
     *
     * @param profiles
     * @return
     */
    public AccountHeaderBuilder addProfiles(@NonNull IProfile... profiles) {
        if (this.mProfiles == null) {
            this.mProfiles = new ArrayList<>();
        }
        if (mDrawer != null) {
            mDrawer.mDrawerBuilder.idDistributor.checkIds(profiles);
        }
        Collections.addAll(this.mProfiles, profiles);

        return this;
    }

    // the click listener to be fired on profile or selection click
    protected AccountHeader.OnAccountHeaderListener mOnAccountHeaderListener;

    /**
     * add a listener for the accountHeader
     *
     * @param onAccountHeaderListener
     * @return
     */
    public AccountHeaderBuilder withOnAccountHeaderListener(AccountHeader.OnAccountHeaderListener onAccountHeaderListener) {
        this.mOnAccountHeaderListener = onAccountHeaderListener;
        return this;
    }

    //the on long click listener to be fired on profile longClick inside the list
    protected AccountHeader.OnAccountHeaderItemLongClickListener mOnAccountHeaderItemLongClickListener;

    /**
     * the on long click listener to be fired on profile longClick inside the list
     *
     * @param onAccountHeaderItemLongClickListener
     * @return
     */
    public AccountHeaderBuilder withOnAccountHeaderItemLongClickListener(AccountHeader.OnAccountHeaderItemLongClickListener onAccountHeaderItemLongClickListener) {
        this.mOnAccountHeaderItemLongClickListener = onAccountHeaderItemLongClickListener;
        return this;
    }

    // the drawer to set the AccountSwitcher for
    protected Drawer mDrawer;

    /**
     * @param drawer
     * @return
     */
    public AccountHeaderBuilder withDrawer(@NonNull Drawer drawer) {
        this.mDrawer = drawer;

        //set the top padding to 0 as this would happen when the AccountHeader is created during Drawer build time
        drawer.getRecyclerView().setPadding(drawer.getRecyclerView().getPaddingLeft(), 0, drawer.getRecyclerView().getPaddingRight(), drawer.getRecyclerView().getPaddingBottom());
        return this;
    }

    // savedInstance to restore state
    protected Bundle mSavedInstance;

    /**
     * create the drawer with the values of a savedInstance
     *
     * @param savedInstance
     * @return
     */
    public AccountHeaderBuilder withSavedInstance(Bundle savedInstance) {
        this.mSavedInstance = savedInstance;
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

            View accountHeader = mAccountHeaderContainer.findViewById(R.id.material_drawer_account_header);
            if (accountHeader != null) {
                params = accountHeader.getLayoutParams();
                params.height = height;
                accountHeader.setLayoutParams(params);
            }

            View accountHeaderBackground = mAccountHeaderContainer.findViewById(R.id.material_drawer_account_header_background);
            if (accountHeaderBackground != null) {
                params = accountHeaderBackground.getLayoutParams();
                params.height = height;
                accountHeaderBackground.setLayoutParams(params);
            }
        }
    }

    /**
     * a small helper to handle the selectionView
     *
     * @param on
     */
    private void handleSelectionView(IProfile profile, boolean on) {
        if (on) {
            if (Build.VERSION.SDK_INT >= 21) {
                ((FrameLayout) mAccountHeaderContainer).setForeground(AppCompatResources.getDrawable(mAccountHeaderContainer.getContext(), mAccountHeaderTextSectionBackgroundResource));
                mAccountHeaderContainer.setOnClickListener(onSelectionClickListener);
                mAccountHeaderContainer.setTag(R.id.material_drawer_profile_header, profile);
            } else {
                mAccountHeaderTextSection.setBackgroundResource(mAccountHeaderTextSectionBackgroundResource);
                mAccountHeaderTextSection.setOnClickListener(onSelectionClickListener);
                mAccountHeaderTextSection.setTag(R.id.material_drawer_profile_header, profile);
            }
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                ((FrameLayout) mAccountHeaderContainer).setForeground(null);
                mAccountHeaderContainer.setOnClickListener(null);
            } else {
                UIUtils.setBackground(mAccountHeaderTextSection, null);
                mAccountHeaderTextSection.setOnClickListener(null);
            }
        }
    }

    /**
     * method to build the header view
     *
     * @return
     */
    public AccountHeader build() {
        // if the user has not set a accountHeader use the default one :D
        if (mAccountHeaderContainer == null) {
            withAccountHeader(-1);
        }

        // get the header view within the container
        mAccountHeader = mAccountHeaderContainer.findViewById(R.id.material_drawer_account_header);

        //the default min header height by default 148dp
        int defaultHeaderMinHeight = mActivity.getResources().getDimensionPixelSize(R.dimen.material_drawer_account_header_height);
        int statusBarHeight = UIUtils.getStatusBarHeight(mActivity, true);

        // handle the height for the header
        int height;
        if (mHeight != null) {
            height = mHeight.asPixel(mActivity);
        } else {
            if (mCompactStyle) {
                height = mActivity.getResources().getDimensionPixelSize(R.dimen.material_drawer_account_header_height_compact);
            } else {
                //calculate the header height by getting the optimal drawer width and calculating it * 9 / 16
                height = (int) (DrawerUIUtils.getOptimalDrawerWidth(mActivity) * AccountHeader.NAVIGATION_DRAWER_ACCOUNT_ASPECT_RATIO);

                //if we are lower than api 19 (>= 19 we have a translucentStatusBar) the height should be a bit lower
                //probably even if we are non translucent on > 19 devices?
                if (Build.VERSION.SDK_INT < 19) {
                    int tempHeight = height - statusBarHeight;
                    //if we are lower than api 19 we are not able to have a translucent statusBar so we remove the height of the statusBar from the padding
                    //to prevent display issues we only reduce the height if we still fit the required minHeight of 148dp (R.dimen.material_drawer_account_header_height)
                    //we remove additional 8dp from the defaultMinHeaderHeight as there is some buffer in the header and to prevent to large spacings
                    if (tempHeight > defaultHeaderMinHeight - UIUtils.convertDpToPixel(8, mActivity)) {
                        height = tempHeight;
                    }
                }
            }
        }

        // handle everything if we have a translucent status bar which only is possible on API >= 19
        if (mTranslucentStatusBar && Build.VERSION.SDK_INT >= 21) {
            mAccountHeader.setPadding(mAccountHeader.getPaddingLeft(), mAccountHeader.getPaddingTop() + statusBarHeight, mAccountHeader.getPaddingRight(), mAccountHeader.getPaddingBottom());
            //in fact it makes no difference if we have a translucent statusBar or not. we want 9/16 just if we are not compact
            if (mCompactStyle) {
                height = height + statusBarHeight;
            } else if ((height - statusBarHeight) <= defaultHeaderMinHeight) {
                //if the height + statusBar of the header is lower than the required 148dp + statusBar we change the height to be able to display all the data
                height = defaultHeaderMinHeight + statusBarHeight;
            }
        }

        //set the height for the header
        setHeaderHeight(height);

        // get the background view
        mAccountHeaderBackground = (ImageView) mAccountHeaderContainer.findViewById(R.id.material_drawer_account_header_background);
        // set the background
        ImageHolder.applyTo(mHeaderBackground, mAccountHeaderBackground, DrawerImageLoader.Tags.ACCOUNT_HEADER.name());

        if (mHeaderBackgroundScaleType != null) {
            mAccountHeaderBackground.setScaleType(mHeaderBackgroundScaleType);
        }

        // get the text color to use for the text section
        int textColor = ColorHolder.color(mTextColor, mActivity, R.attr.material_drawer_header_selection_text, R.color.material_drawer_header_selection_text);

        // set the background for the section
        if (mCompactStyle) {
            mAccountHeaderTextSection = mAccountHeader;
        } else {
            mAccountHeaderTextSection = mAccountHeaderContainer.findViewById(R.id.material_drawer_account_header_text_section);
        }

        mAccountHeaderTextSectionBackgroundResource = UIUtils.getSelectableBackgroundRes(mActivity);
        handleSelectionView(mCurrentProfile, true);

        // set the arrow :D
        mAccountSwitcherArrow = (ImageView) mAccountHeaderContainer.findViewById(R.id.material_drawer_account_header_text_switcher);
        mAccountSwitcherArrow.setImageDrawable(new IconicsDrawable(mActivity, MaterialDrawerFont.Icon.mdf_arrow_drop_down).sizeRes(R.dimen.material_drawer_account_header_dropdown).paddingRes(R.dimen.material_drawer_account_header_dropdown_padding).color(textColor));

        //get the fields for the name
        mCurrentProfileView = (BezelImageView) mAccountHeader.findViewById(R.id.material_drawer_account_header_current);
        mCurrentProfileName = (TextView) mAccountHeader.findViewById(R.id.material_drawer_account_header_name);
        mCurrentProfileEmail = (TextView) mAccountHeader.findViewById(R.id.material_drawer_account_header_email);

        //set the typeface for the AccountHeader
        if (mNameTypeface != null) {
            mCurrentProfileName.setTypeface(mNameTypeface);
        } else if (mTypeface != null) {
            mCurrentProfileName.setTypeface(mTypeface);
        }

        if (mEmailTypeface != null) {
            mCurrentProfileEmail.setTypeface(mEmailTypeface);
        } else if (mTypeface != null) {
            mCurrentProfileEmail.setTypeface(mTypeface);
        }

        mCurrentProfileName.setTextColor(textColor);
        mCurrentProfileEmail.setTextColor(textColor);

        mProfileFirstView = (BezelImageView) mAccountHeader.findViewById(R.id.material_drawer_account_header_small_first);
        mProfileSecondView = (BezelImageView) mAccountHeader.findViewById(R.id.material_drawer_account_header_small_second);
        mProfileThirdView = (BezelImageView) mAccountHeader.findViewById(R.id.material_drawer_account_header_small_third);

        //calculate the profiles to set
        calculateProfiles();

        //process and build the profiles
        buildProfiles();

        // try to restore all saved values again
        if (mSavedInstance != null) {
            int selection = mSavedInstance.getInt(AccountHeader.BUNDLE_SELECTION_HEADER, -1);
            if (selection != -1) {
                //predefine selection (should be the first element
                if (mProfiles != null && (selection) > -1 && selection < mProfiles.size()) {
                    switchProfiles(mProfiles.get(selection));
                }
            }
        }

        //everything created. now set the header
        if (mDrawer != null) {
            mDrawer.setHeader(mAccountHeaderContainer, mPaddingBelowHeader, mDividerBelowHeader);
        }

        //forget the reference to the activity
        mActivity = null;

        return new AccountHeader(this);
    }

    /**
     * helper method to calculate the order of the profiles
     */
    protected void calculateProfiles() {
        if (mProfiles == null) {
            mProfiles = new ArrayList<>();
        }

        if (mCurrentProfile == null) {
            int setCount = 0;
            int size = mProfiles.size();
            for (int i = 0; i < size; i++) {
                if (mProfiles.size() > i && mProfiles.get(i).isSelectable()) {
                    if (setCount == 0 && (mCurrentProfile == null)) {
                        mCurrentProfile = mProfiles.get(i);
                    } else if (setCount == 1 && (mProfileFirst == null)) {
                        mProfileFirst = mProfiles.get(i);
                    } else if (setCount == 2 && (mProfileSecond == null)) {
                        mProfileSecond = mProfiles.get(i);
                    } else if (setCount == 3 && (mProfileThird == null)) {
                        mProfileThird = mProfiles.get(i);
                    }
                    setCount++;
                }
            }

            return;
        }

        IProfile[] previousActiveProfiles = new IProfile[]{
                mCurrentProfile,
                mProfileFirst,
                mProfileSecond,
                mProfileThird
        };

        IProfile[] newActiveProfiles = new IProfile[4];
        Stack<IProfile> unusedProfiles = new Stack<>();

        // try to keep existing active profiles in the same positions
        for (int i = 0; i < mProfiles.size(); i++) {
            IProfile p = mProfiles.get(i);
            if (p.isSelectable()) {
                boolean used = false;
                for (int j = 0; j < 4; j++) {
                    if (previousActiveProfiles[j] == p) {
                        newActiveProfiles[j] = p;
                        used = true;
                        break;
                    }
                }
                if (!used) {
                    unusedProfiles.push(p);
                }
            }
        }

        Stack<IProfile> activeProfiles = new Stack<>();
        // try to fill the gaps with new available profiles
        for (int i = 0; i < 4; i++) {
            if (newActiveProfiles[i] != null) {
                activeProfiles.push(newActiveProfiles[i]);
            } else if (!unusedProfiles.isEmpty()) {
                activeProfiles.push(unusedProfiles.pop());
            }
        }

        Stack<IProfile> reversedActiveProfiles = new Stack<>();
        while (!activeProfiles.empty()) {
            reversedActiveProfiles.push(activeProfiles.pop());
        }

        // reassign active profiles
        if (reversedActiveProfiles.isEmpty()) {
            mCurrentProfile = null;
        } else {
            mCurrentProfile = reversedActiveProfiles.pop();
        }
        if (reversedActiveProfiles.isEmpty()) {
            mProfileFirst = null;
        } else {
            mProfileFirst = reversedActiveProfiles.pop();
        }
        if (reversedActiveProfiles.isEmpty()) {
            mProfileSecond = null;
        } else {
            mProfileSecond = reversedActiveProfiles.pop();
        }
        if (reversedActiveProfiles.isEmpty()) {
            mProfileThird = null;
        } else {
            mProfileThird = reversedActiveProfiles.pop();
        }
    }

    /**
     * helper method to switch the profiles
     *
     * @param newSelection
     * @return true if the new selection was the current profile
     */
    protected boolean switchProfiles(IProfile newSelection) {
        if (newSelection == null) {
            return false;
        }
        if (mCurrentProfile == newSelection) {
            return true;
        }

        if (mAlternativeProfileHeaderSwitching) {
            int prevSelection = -1;
            if (mProfileFirst == newSelection) {
                prevSelection = 1;
            } else if (mProfileSecond == newSelection) {
                prevSelection = 2;
            } else if (mProfileThird == newSelection) {
                prevSelection = 3;
            }

            IProfile tmp = mCurrentProfile;
            mCurrentProfile = newSelection;

            if (prevSelection == 1) {
                mProfileFirst = tmp;
            } else if (prevSelection == 2) {
                mProfileSecond = tmp;
            } else if (prevSelection == 3) {
                mProfileThird = tmp;
            }
        } else {
            if (mProfiles != null) {
                ArrayList<IProfile> previousActiveProfiles = new ArrayList<>(Arrays.asList(mCurrentProfile, mProfileFirst, mProfileSecond, mProfileThird));

                if (previousActiveProfiles.contains(newSelection)) {
                    int position = -1;

                    for (int i = 0; i < 4; i++) {
                        if (previousActiveProfiles.get(i) == newSelection) {
                            position = i;
                            break;
                        }
                    }

                    if (position != -1) {
                        previousActiveProfiles.remove(position);
                        previousActiveProfiles.add(0, newSelection);

                        mCurrentProfile = previousActiveProfiles.get(0);
                        mProfileFirst = previousActiveProfiles.get(1);
                        mProfileSecond = previousActiveProfiles.get(2);
                        mProfileThird = previousActiveProfiles.get(3);
                    }
                } else {
                    mProfileThird = mProfileSecond;
                    mProfileSecond = mProfileFirst;
                    mProfileFirst = mCurrentProfile;
                    mCurrentProfile = newSelection;
                }
            }
        }

        //if we only show the small profile images we have to make sure the first (would be the current selected) profile is also shown
        if (mOnlySmallProfileImagesVisible) {
            mProfileThird = mProfileSecond;
            mProfileSecond = mProfileFirst;
            mProfileFirst = mCurrentProfile;
            //mCurrentProfile = mProfileThird;
        }

        buildProfiles();

        return false;
    }

    /**
     * helper method to build the views for the ui
     */
    protected void buildProfiles() {
        mCurrentProfileView.setVisibility(View.INVISIBLE);
        mAccountHeaderTextSection.setVisibility(View.INVISIBLE);
        mAccountSwitcherArrow.setVisibility(View.GONE);
        mProfileFirstView.setVisibility(View.GONE);
        mProfileFirstView.setOnClickListener(null);
        mProfileSecondView.setVisibility(View.GONE);
        mProfileSecondView.setOnClickListener(null);
        mProfileThirdView.setVisibility(View.GONE);
        mProfileThirdView.setOnClickListener(null);
        mCurrentProfileName.setText("");
        mCurrentProfileEmail.setText("");

        //we only handle the padding if we are not in compact mode
        if (!mCompactStyle) {
            mAccountHeaderTextSection.setPadding(0, 0, (int) UIUtils.convertDpToPixel(56, mAccountHeaderTextSection.getContext()), 0);
        }

        handleSelectionView(mCurrentProfile, true);

        if (mCurrentProfile != null) {
            if ((mProfileImagesVisible || mOnlyMainProfileImageVisible) && !mOnlySmallProfileImagesVisible) {
                setImageOrPlaceholder(mCurrentProfileView, mCurrentProfile.getIcon());
                if (mProfileImagesClickable) {
                    mCurrentProfileView.setOnClickListener(onCurrentProfileClickListener);
                    mCurrentProfileView.setOnLongClickListener(onCurrentProfileLongClickListener);
                    mCurrentProfileView.disableTouchFeedback(false);
                } else {
                    mCurrentProfileView.disableTouchFeedback(true);
                }
                mCurrentProfileView.setVisibility(View.VISIBLE);

                mCurrentProfileView.invalidate();
            } else if (mCompactStyle) {
                mCurrentProfileView.setVisibility(View.GONE);
            }

            mAccountHeaderTextSection.setVisibility(View.VISIBLE);
            handleSelectionView(mCurrentProfile, true);
            mAccountSwitcherArrow.setVisibility(View.VISIBLE);
            mCurrentProfileView.setTag(R.id.material_drawer_profile_header, mCurrentProfile);

            StringHolder.applyTo(mCurrentProfile.getName(), mCurrentProfileName);
            StringHolder.applyTo(mCurrentProfile.getEmail(), mCurrentProfileEmail);

            if (mProfileFirst != null && mProfileImagesVisible && !mOnlyMainProfileImageVisible) {
                setImageOrPlaceholder(mProfileFirstView, mProfileFirst.getIcon());
                mProfileFirstView.setTag(R.id.material_drawer_profile_header, mProfileFirst);
                if (mProfileImagesClickable) {
                    mProfileFirstView.setOnClickListener(onProfileClickListener);
                    mProfileFirstView.setOnLongClickListener(onProfileLongClickListener);
                    mProfileFirstView.disableTouchFeedback(false);
                } else {
                    mProfileFirstView.disableTouchFeedback(true);
                }
                mProfileFirstView.setVisibility(View.VISIBLE);
                mProfileFirstView.invalidate();
            }
            if (mProfileSecond != null && mProfileImagesVisible && !mOnlyMainProfileImageVisible) {
                setImageOrPlaceholder(mProfileSecondView, mProfileSecond.getIcon());
                mProfileSecondView.setTag(R.id.material_drawer_profile_header, mProfileSecond);
                if (mProfileImagesClickable) {
                    mProfileSecondView.setOnClickListener(onProfileClickListener);
                    mProfileSecondView.setOnLongClickListener(onProfileLongClickListener);
                    mProfileSecondView.disableTouchFeedback(false);
                } else {
                    mProfileSecondView.disableTouchFeedback(true);
                }
                mProfileSecondView.setVisibility(View.VISIBLE);
                mProfileSecondView.invalidate();
            }
            if (mProfileThird != null && mThreeSmallProfileImages && mProfileImagesVisible && !mOnlyMainProfileImageVisible) {
                setImageOrPlaceholder(mProfileThirdView, mProfileThird.getIcon());
                mProfileThirdView.setTag(R.id.material_drawer_profile_header, mProfileThird);
                if (mProfileImagesClickable) {
                    mProfileThirdView.setOnClickListener(onProfileClickListener);
                    mProfileThirdView.setOnLongClickListener(onProfileLongClickListener);
                    mProfileThirdView.disableTouchFeedback(false);
                } else {
                    mProfileThirdView.disableTouchFeedback(true);
                }
                mProfileThirdView.setVisibility(View.VISIBLE);
                mProfileThirdView.invalidate();
            }
        } else if (mProfiles != null && mProfiles.size() > 0) {
            IProfile profile = mProfiles.get(0);
            mAccountHeaderTextSection.setTag(R.id.material_drawer_profile_header, profile);
            mAccountHeaderTextSection.setVisibility(View.VISIBLE);
            handleSelectionView(mCurrentProfile, true);
            mAccountSwitcherArrow.setVisibility(View.VISIBLE);
            if (mCurrentProfile != null) {
                StringHolder.applyTo(mCurrentProfile.getName(), mCurrentProfileName);
                StringHolder.applyTo(mCurrentProfile.getEmail(), mCurrentProfileEmail);
            }
        }

        if (!mSelectionFirstLineShown) {
            mCurrentProfileName.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mSelectionFirstLine)) {
            mCurrentProfileName.setText(mSelectionFirstLine);
            mAccountHeaderTextSection.setVisibility(View.VISIBLE);
        }
        if (!mSelectionSecondLineShown) {
            mCurrentProfileEmail.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mSelectionSecondLine)) {
            mCurrentProfileEmail.setText(mSelectionSecondLine);
            mAccountHeaderTextSection.setVisibility(View.VISIBLE);
        }

        //if we disabled the list
        if (!mSelectionListEnabled || !mSelectionListEnabledForSingleProfile && mProfileFirst == null && (mProfiles == null || mProfiles.size() == 1)) {
            mAccountSwitcherArrow.setVisibility(View.GONE);
            handleSelectionView(null, false);

            //if we are not in compact mode minimize the padding to make use of the space
            if (!mCompactStyle) {
                mAccountHeaderTextSection.setPadding(0, 0, (int) UIUtils.convertDpToPixel(16, mAccountHeaderTextSection.getContext()), 0);
            }
        }

        //if we disabled the list but still have set a custom listener
        if (mOnAccountHeaderSelectionViewClickListener != null) {
            handleSelectionView(mCurrentProfile, true);
        }
    }

    /**
     * small helper method to set an profile image or a placeholder
     *
     * @param iv
     * @param imageHolder
     */
    private void setImageOrPlaceholder(ImageView iv, ImageHolder imageHolder) {
        //cancel previous started image loading processes
        DrawerImageLoader.getInstance().cancelImage(iv);
        //set the placeholder
        iv.setImageDrawable(DrawerImageLoader.getInstance().getImageLoader().placeholder(iv.getContext(), DrawerImageLoader.Tags.PROFILE.name()));
        //set the real image (probably also the uri)
        ImageHolder.applyTo(imageHolder, iv, DrawerImageLoader.Tags.PROFILE.name());
    }

    /**
     * onProfileClickListener to notify onClick on the current profile image
     */
    private View.OnClickListener onCurrentProfileClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            onProfileImageClick(v, true);
        }
    };

    /**
     * onProfileClickListener to notify onClick on a profile image
     */
    private View.OnClickListener onProfileClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            onProfileImageClick(v, false);
        }
    };

    /**
     * calls the mOnAccountHEaderProfileImageListener and continues with the actions afterwards
     *
     * @param v
     * @param current
     */
    private void onProfileImageClick(View v, boolean current) {
        IProfile profile = (IProfile) v.getTag(R.id.material_drawer_profile_header);

        boolean consumed = false;
        if (mOnAccountHeaderProfileImageListener != null) {
            consumed = mOnAccountHeaderProfileImageListener.onProfileImageClick(v, profile, current);
        }

        //if the event was already consumed by the click don't continue. note that this will also stop the profile change event
        if (!consumed) {
            onProfileClick(v, current);
        }
    }

    /**
     * onProfileLongClickListener to call the onProfileImageLongClick on the current profile image
     */
    private View.OnLongClickListener onCurrentProfileLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (mOnAccountHeaderProfileImageListener != null) {
                IProfile profile = (IProfile) v.getTag(R.id.material_drawer_profile_header);
                return mOnAccountHeaderProfileImageListener.onProfileImageLongClick(v, profile, true);
            }
            return false;
        }
    };

    /**
     * onProfileLongClickListener to call the onProfileImageLongClick on a profile image
     */
    private View.OnLongClickListener onProfileLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (mOnAccountHeaderProfileImageListener != null) {
                IProfile profile = (IProfile) v.getTag(R.id.material_drawer_profile_header);
                return mOnAccountHeaderProfileImageListener.onProfileImageLongClick(v, profile, false);
            }
            return false;
        }
    };

    protected void onProfileClick(View v, boolean current) {
        final IProfile profile = (IProfile) v.getTag(R.id.material_drawer_profile_header);
        switchProfiles(profile);

        //reset the drawer content
        resetDrawerContent(v.getContext());

        //notify the MiniDrawer about the clicked profile (only if one exists and is hooked to the Drawer
        if (mDrawer != null && mDrawer.getDrawerBuilder() != null && mDrawer.getDrawerBuilder().mMiniDrawer != null) {
            mDrawer.getDrawerBuilder().mMiniDrawer.onProfileClick();
        }

        //notify about the changed profile
        boolean consumed = false;
        if (mOnAccountHeaderListener != null) {
            consumed = mOnAccountHeaderListener.onProfileChanged(v, profile, current);
        }

        if (!consumed) {
            if (mOnProfileClickDrawerCloseDelay > 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mDrawer != null) {
                            mDrawer.closeDrawer();
                        }
                    }
                }, mOnProfileClickDrawerCloseDelay);
            } else {
                if (mDrawer != null) {
                    mDrawer.closeDrawer();
                }
            }
        }
    }

    /**
     * get the current selection
     *
     * @return
     */
    protected int getCurrentSelection() {
        if (mCurrentProfile != null && mProfiles != null) {
            int i = 0;
            for (IProfile profile : mProfiles) {
                if (profile == mCurrentProfile) {
                    return i;
                }
                i++;
            }
        }
        return -1;
    }

    /**
     * onSelectionClickListener to notify the onClick on the checkbox
     */
    private View.OnClickListener onSelectionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean consumed = false;
            if (mOnAccountHeaderSelectionViewClickListener != null) {
                consumed = mOnAccountHeaderSelectionViewClickListener.onClick(v, (IProfile) v.getTag(R.id.material_drawer_profile_header));
            }

            if (mAccountSwitcherArrow.getVisibility() == View.VISIBLE && !consumed) {
                toggleSelectionList(v.getContext());
            }
        }
    };

    /**
     * helper method to toggle the collection
     *
     * @param ctx
     */
    protected void toggleSelectionList(Context ctx) {
        if (mDrawer != null) {
            //if we already show the list. reset everything instead
            if (mDrawer.switchedDrawerContent()) {
                resetDrawerContent(ctx);
                mSelectionListShown = false;
            } else {
                //build and set the drawer selection list
                buildDrawerSelectionList();

                // update the arrow image within the drawer
                mAccountSwitcherArrow.clearAnimation();
                ViewCompat.animate(mAccountSwitcherArrow).rotation(180).start();
                //mAccountSwitcherArrow.setImageDrawable(new IconicsDrawable(ctx, MaterialDrawerFont.Icon.mdf_arrow_drop_up).sizeRes(R.dimen.material_drawer_account_header_dropdown).paddingRes(R.dimen.material_drawer_account_header_dropdown_padding).color(ColorHolder.color(mTextColor, ctx, R.attr.material_drawer_header_selection_text, R.color.material_drawer_header_selection_text)));
                mSelectionListShown = true;
            }
        }
    }

    /**
     * helper method to build and set the drawer selection list
     */
    protected void buildDrawerSelectionList() {
        int selectedPosition = -1;
        int position = 0;
        ArrayList<IDrawerItem> profileDrawerItems = new ArrayList<>();
        if (mProfiles != null) {
            for (IProfile profile : mProfiles) {
                if (profile == mCurrentProfile) {
                    if (mCurrentHiddenInList) {
                        continue;
                    } else {
                        selectedPosition = mDrawer.mDrawerBuilder.getItemAdapter().getGlobalPosition(position);
                    }
                }
                if (profile instanceof IDrawerItem) {
                    ((IDrawerItem) profile).withSetSelected(false);
                    profileDrawerItems.add((IDrawerItem) profile);
                }
                position = position + 1;
            }
        }
        mDrawer.switchDrawerContent(onDrawerItemClickListener, onDrawerItemLongClickListener, profileDrawerItems, selectedPosition);
    }

    /**
     * onDrawerItemClickListener to catch the selection for the new profile!
     */
    private Drawer.OnDrawerItemClickListener onDrawerItemClickListener = new Drawer.OnDrawerItemClickListener() {
        @Override
        public boolean onItemClick(final View view, int position, final IDrawerItem drawerItem) {
            final boolean isCurrentSelectedProfile;
            if (drawerItem != null && drawerItem instanceof IProfile && drawerItem.isSelectable()) {
                isCurrentSelectedProfile = switchProfiles((IProfile) drawerItem);
            } else {
                isCurrentSelectedProfile = false;
            }

            if (mResetDrawerOnProfileListClick) {
                mDrawer.setOnDrawerItemClickListener(null);
            }

            //wrap the onSelection call and the reset stuff within a handler to prevent lag
            if (mResetDrawerOnProfileListClick && mDrawer != null && view != null && view.getContext() != null) {
                resetDrawerContent(view.getContext());
            }

            //notify the MiniDrawer about the clicked profile (only if one exists and is hooked to the Drawer
            if (mDrawer != null && mDrawer.getDrawerBuilder() != null && mDrawer.getDrawerBuilder().mMiniDrawer != null) {
                mDrawer.getDrawerBuilder().mMiniDrawer.onProfileClick();
            }

            boolean consumed = false;
            if (drawerItem != null && drawerItem instanceof IProfile) {
                if (mOnAccountHeaderListener != null) {
                    consumed = mOnAccountHeaderListener.onProfileChanged(view, (IProfile) drawerItem, isCurrentSelectedProfile);
                }
            }

            //if a custom behavior was chosen via the CloseDrawerOnProfileListClick then use this. else react on the result of the onProfileChanged listener
            if (mCloseDrawerOnProfileListClick != null) {
                consumed = consumed && !mCloseDrawerOnProfileListClick;
            }

            //totally custom handling of the drawer behavior as otherwise the selection of the profile list is set to the Drawer
            if (mDrawer != null && !consumed) {
                //close the drawer after click
                mDrawer.mDrawerBuilder.closeDrawerDelayed();
            }

            //consume the event to prevent setting the clicked item as selected in the already switched item list
            return true;
        }
    };

    /**
     * onDrawerItemLongClickListener to catch the longClick for a profile
     */
    private Drawer.OnDrawerItemLongClickListener onDrawerItemLongClickListener = new Drawer.OnDrawerItemLongClickListener() {
        @Override
        public boolean onItemLongClick(View view, int position, IDrawerItem drawerItem) {
            //if a longClickListener was defined use it
            if (mOnAccountHeaderItemLongClickListener != null) {
                final boolean isCurrentSelectedProfile;
                isCurrentSelectedProfile = drawerItem != null && drawerItem.isSelected();

                if (drawerItem != null && drawerItem instanceof IProfile) {
                    return mOnAccountHeaderItemLongClickListener.onProfileLongClick(view, (IProfile) drawerItem, isCurrentSelectedProfile);
                }
            }
            return false;
        }
    };

    /**
     * helper method to reset the drawer content
     */
    private void resetDrawerContent(Context ctx) {
        if (mDrawer != null) {
            mDrawer.resetDrawerContent();
        }

        mAccountSwitcherArrow.clearAnimation();
        ViewCompat.animate(mAccountSwitcherArrow).rotation(0).start();
        //mAccountSwitcherArrow.setImageDrawable(new IconicsDrawable(ctx, MaterialDrawerFont.Icon.mdf_arrow_drop_down).sizeRes(R.dimen.material_drawer_account_header_dropdown).paddingRes(R.dimen.material_drawer_account_header_dropdown_padding).color(ColorHolder.color(mTextColor, ctx, R.attr.material_drawer_header_selection_text, R.color.material_drawer_header_selection_text)));
    }

    /**
     * small helper class to update the header and the list
     */
    protected void updateHeaderAndList() {
        //recalculate the profiles
        calculateProfiles();
        //update the profiles in the header
        buildProfiles();
        //if we currently show the list add the new item directly to it
        if (mSelectionListShown) {
            buildDrawerSelectionList();
        }
    }
}
