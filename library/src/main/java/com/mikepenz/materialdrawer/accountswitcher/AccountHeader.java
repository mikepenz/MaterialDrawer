package com.mikepenz.materialdrawer.accountswitcher;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.utils.Utils;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Identifyable;
import com.mikepenz.materialdrawer.util.UIUtils;
import com.mikepenz.materialdrawer.view.BezelImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

/**
 * Created by mikepenz on 27.02.15.
 */
public class AccountHeader {
    private final static double NAVIGATION_DRAWER_ACCOUNT_ASPECT_RATIO = 9d / 16d;

    private static final String BUNDLE_SELECTION_HEADER = "bundle_selection_header";

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
    public AccountHeader withActivity(Activity activity) {
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
    public AccountHeader withCompactStyle(boolean compactStyle) {
        this.mCompactStyle = compactStyle;
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
     * set the height for the header by resource
     *
     * @param heightRes
     * @return
     */
    public AccountHeader withHeightRes(int heightRes) {
        this.mHeightRes = heightRes;
        return this;
    }

    //the background color for the slider
    protected int mTextColor = 0;
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

    //set to hide the first or second line
    protected boolean mSelectionFirstLineShown = true;
    protected boolean mSelectionSecondLineShown = true;

    /**
     * set this to false if you want to hide the first line of the selection box in the header (first line would be the name)
     *
     * @param selectionFirstLineShown
     * @return
     */
    public AccountHeader withSelectionFistLineShown(boolean selectionFirstLineShown) {
        this.mSelectionFirstLineShown = selectionFirstLineShown;
        return this;
    }

    /**
     * set this to false if you want to hide the second line of the selection box in the header (second line would be the e-mail)
     *
     * @param selectionSecondLineShown
     * @return
     */
    public AccountHeader withSelectionSecondLineShown(boolean selectionSecondLineShown) {
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
    public AccountHeader withSelectionFirstLine(String selectionFirstLine) {
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
    public AccountHeader withSelectionSecondLine(String selectionSecondLine) {
        this.mSelectionSecondLine = selectionSecondLine;
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
    public AccountHeader withTranslucentStatusBar(boolean translucentStatusBar) {
        this.mTranslucentStatusBar = translucentStatusBar;
        return this;
    }

    //the background for the header
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

    //background scale type
    protected ImageView.ScaleType mHeaderBackgroundScaleType = null;

    /**
     * define the ScaleType for the header background
     *
     * @param headerBackgroundScaleType
     * @return
     */
    public AccountHeader withHeaderBackgroundScaleType(ImageView.ScaleType headerBackgroundScaleType) {
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
    public AccountHeader withProfileImagesVisible(boolean profileImagesVisible) {
        this.mProfileImagesVisible = profileImagesVisible;
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
    public AccountHeader withProfileImagesClickable(boolean profileImagesClickable) {
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
    public AccountHeader withAlternativeProfileHeaderSwitching(boolean alternativeProfileHeaderSwitching) {
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
    public AccountHeader withThreeSmallProfileImages(boolean threeSmallProfileImages) {
        this.mThreeSmallProfileImages = threeSmallProfileImages;
        return this;
    }

    // the onAccountHeaderSelectionListener to set
    protected OnAccountHeaderSelectionViewClickListener mOnAccountHeaderSelectionViewClickListener;

    /**
     * set a onSelection listener for the selection box
     *
     * @param onAccountHeaderSelectionViewClickListener
     * @return
     */
    public AccountHeader withOnAccountHeaderSelectionViewClickListener(OnAccountHeaderSelectionViewClickListener onAccountHeaderSelectionViewClickListener) {
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
    public AccountHeader withSelectionListEnabledForSingleProfile(boolean selectionListEnabledForSingleProfile) {
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
    public AccountHeader withSelectionListEnabled(boolean selectionListEnabled) {
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
            if (mCompactStyle) {
                this.mAccountHeaderContainer = mActivity.getLayoutInflater().inflate(R.layout.material_drawer_compact_header, null, false);
            } else {
                this.mAccountHeaderContainer = mActivity.getLayoutInflater().inflate(R.layout.material_drawer_header, null, false);
            }
        }

        return this;
    }

    // the profiles to display
    protected ArrayList<IProfile> mProfiles;

    /**
     * set the arrayList of DrawerItems for the drawer
     *
     * @param profiles
     * @return
     */
    public AccountHeader withProfiles(ArrayList<IProfile> profiles) {
        this.mProfiles = profiles;
        return this;
    }

    /**
     * add single ore more DrawerItems to the Drawer
     *
     * @param profiles
     * @return
     */
    public AccountHeader addProfiles(IProfile... profiles) {
        if (this.mProfiles == null) {
            this.mProfiles = new ArrayList<>();
        }

        if (profiles != null) {
            Collections.addAll(this.mProfiles, profiles);
        }
        return this;
    }

    // the click listener to be fired on profile or selection click
    protected OnAccountHeaderListener mOnAccountHeaderListener;

    /**
     * add a listener for the accountHeader
     *
     * @param onAccountHeaderListener
     * @return
     */
    public AccountHeader withOnAccountHeaderListener(OnAccountHeaderListener onAccountHeaderListener) {
        this.mOnAccountHeaderListener = onAccountHeaderListener;
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

    // savedInstance to restore state
    protected Bundle mSavedInstance;

    /**
     * create the drawer with the values of a savedInstance
     *
     * @param savedInstance
     * @return
     */
    public AccountHeader withSavedInstance(Bundle savedInstance) {
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
     * method to build the header view
     *
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
            if (mCompactStyle) {
                height = mActivity.getResources().getDimensionPixelSize(R.dimen.material_drawer_account_header_height_compact);
            } else {
                //calculate the header height by getting the optimal drawer width and calculating it * 9 / 16
                height = (int) (UIUtils.getOptimalDrawerWidth(mActivity) * NAVIGATION_DRAWER_ACCOUNT_ASPECT_RATIO);
            }
        }

        // handle everything if we don't have a translucent status bar
        if (mTranslucentStatusBar) {
            mAccountHeader.setPadding(0, mActivity.getResources().getDimensionPixelSize(R.dimen.tool_bar_top_padding), 0, 0);
            //in fact it makes no difference if we have a translucent statusBar or not. we want 9/16 just if we are compact
            if (mCompactStyle) {
                height = height + mActivity.getResources().getDimensionPixelSize(R.dimen.tool_bar_top_padding);
            }
        }


        //set the height for the header
        setHeaderHeight(height);

        // get the background view
        mAccountHeaderBackground = (ImageView) mAccountHeaderContainer.findViewById(R.id.account_header_drawer_background);
        // set the background
        if (mHeaderBackground != null) {
            mAccountHeaderBackground.setImageDrawable(mHeaderBackground);
        } else if (mHeaderBackgroundRes != -1) {
            mAccountHeaderBackground.setImageResource(mHeaderBackgroundRes);
        }

        if (mHeaderBackgroundScaleType != null) {
            mAccountHeaderBackground.setScaleType(mHeaderBackgroundScaleType);
        }

        // get the text color to use for the text section
        if (mTextColor == 0 && mTextColorRes != -1) {
            mTextColor = mActivity.getResources().getColor(mTextColorRes);
        } else if (mTextColor == 0) {
            mTextColor = UIUtils.getThemeColorFromAttrOrRes(mActivity, R.attr.material_drawer_header_selection_text, R.color.material_drawer_header_selection_text);
        }

        // set the background for the section
        if (mCompactStyle) {
            mAccountHeaderTextSection = mAccountHeader;
        } else {
            mAccountHeaderTextSection = mAccountHeaderContainer.findViewById(R.id.account_header_drawer_text_section);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // If we're running on Honeycomb or newer, then we can use the Theme's
            // selectableItemBackground to ensure that the View has a pressed state
            TypedValue outValue = new TypedValue();
            mActivity.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            mAccountHeaderTextSectionBackgroundResource = outValue.resourceId;
        } else {
            TypedValue outValue = new TypedValue();
            mActivity.getTheme().resolveAttribute(android.R.attr.itemBackground, outValue, true);
            mAccountHeaderTextSectionBackgroundResource = outValue.resourceId;
        }
        mAccountHeaderTextSection.setBackgroundResource(mAccountHeaderTextSectionBackgroundResource);

        // set the arrow :D
        mAccountSwitcherArrow = (ImageView) mAccountHeaderContainer.findViewById(R.id.account_header_drawer_text_switcher);
        mAccountSwitcherArrow.setImageDrawable(new IconicsDrawable(mActivity, GoogleMaterial.Icon.gmd_arrow_drop_down).sizeDp(24).paddingDp(6).color(mTextColor));

        //get the fields for the name
        mCurrentProfileView = (BezelImageView) mAccountHeader.findViewById(R.id.account_header_drawer_current);
        mCurrentProfileName = (TextView) mAccountHeader.findViewById(R.id.account_header_drawer_name);
        mCurrentProfileEmail = (TextView) mAccountHeader.findViewById(R.id.account_header_drawer_email);

        mCurrentProfileName.setTextColor(mTextColor);
        mCurrentProfileEmail.setTextColor(mTextColor);

        mProfileFirstView = (BezelImageView) mAccountHeader.findViewById(R.id.account_header_drawer_small_first);
        mProfileSecondView = (BezelImageView) mAccountHeader.findViewById(R.id.account_header_drawer_small_second);
        mProfileThirdView = (BezelImageView) mAccountHeader.findViewById(R.id.account_header_drawer_small_third);

        //calculate the profiles to set
        calculateProfiles();

        //process and build the profiles
        buildProfiles();

        // try to restore all saved values again
        if (mSavedInstance != null) {
            int selection = mSavedInstance.getInt(BUNDLE_SELECTION_HEADER, -1);
            if (selection != -1) {
                //predefine selection (should be the first element
                if (mProfiles != null && (selection) > -1 && selection < mProfiles.size()) {
                    switchProfiles(mProfiles.get(selection));
                }
            }
        }

        //everything created. now set the header
        if (mDrawer != null) {
            mDrawer.setHeader(mAccountHeaderContainer);
        }

        //forget the reference to the activity
        mActivity = null;

        return new Result(this);
    }

    /**
     * helper method to calculate the order of the profiles
     */
    protected void calculateProfiles() {
        if (mProfiles != null) {
            if (mCurrentProfile == null) {

                int setCount = 0;
                for (int i = 0; i < mProfiles.size(); i++) {
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

            Stack<IProfile> reversedActiveProfiles = new Stack<IProfile>();
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
    }

    /**
     * helper method to switch the profiles
     *
     * @param newSelection
     */
    protected void switchProfiles(IProfile newSelection) {
        if (newSelection == null) {
            return;
        }
        if (mCurrentProfile == newSelection) {
            return;
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

        buildProfiles();
    }

    /**
     * helper method to build the views for the ui
     */
    protected void buildProfiles() {
        mCurrentProfileView.setVisibility(View.INVISIBLE);
        mAccountHeaderTextSection.setVisibility(View.INVISIBLE);
        mAccountHeaderTextSection.setOnClickListener(onSelectionClickListener);
        mAccountHeaderTextSection.setBackgroundResource(mAccountHeaderTextSectionBackgroundResource);
        mAccountSwitcherArrow.setVisibility(View.INVISIBLE);
        mProfileFirstView.setVisibility(View.INVISIBLE);
        mProfileFirstView.setOnClickListener(null);
        mProfileSecondView.setVisibility(View.INVISIBLE);
        mProfileSecondView.setOnClickListener(null);
        mProfileThirdView.setVisibility(View.INVISIBLE);
        mProfileThirdView.setOnClickListener(null);

        if (mCurrentProfile != null) {
            if (mProfileImagesVisible) {
                setImageOrPlaceholder(mCurrentProfileView, mCurrentProfile.getIcon(), mCurrentProfile.getImageUrl());
                mCurrentProfileView.setTag(mCurrentProfile);
                if (mProfileImagesClickable) {
                    mCurrentProfileView.setOnClickListener(onProfileClickListener);
                    mCurrentProfileView.disableTouchFeedback(false);
                } else {
                    mCurrentProfileView.disableTouchFeedback(true);
                }
                mCurrentProfileView.setVisibility(View.VISIBLE);
            } else if (mCompactStyle) {
                mCurrentProfileView.setVisibility(View.GONE);
            }

            mAccountHeaderTextSection.setTag(mCurrentProfile);
            mAccountHeaderTextSection.setVisibility(View.VISIBLE);
            mAccountHeaderTextSection.setBackgroundResource(mAccountHeaderTextSectionBackgroundResource);
            mAccountSwitcherArrow.setVisibility(View.VISIBLE);
            mCurrentProfileName.setText(mCurrentProfile.getName());
            mCurrentProfileEmail.setText(mCurrentProfile.getEmail());

            if (mProfileFirst != null && mProfileImagesVisible) {
                setImageOrPlaceholder(mProfileFirstView, mProfileFirst.getIcon(), mProfileFirst.getImageUrl());
                mProfileFirstView.setTag(mProfileFirst);
                if (mProfileImagesClickable) {
                    mProfileFirstView.setOnClickListener(onProfileClickListener);
                    mProfileFirstView.disableTouchFeedback(false);
                } else {
                    mProfileFirstView.disableTouchFeedback(true);
                }
                mProfileFirstView.setVisibility(View.VISIBLE);
            }
            if (mProfileSecond != null && mProfileImagesVisible) {
                setImageOrPlaceholder(mProfileSecondView, mProfileSecond.getIcon(), mProfileSecond.getImageUrl());
                mProfileSecondView.setTag(mProfileSecond);
                if (mProfileImagesClickable) {
                    mProfileSecondView.setOnClickListener(onProfileClickListener);
                    mProfileSecondView.disableTouchFeedback(false);
                } else {
                    mProfileSecondView.disableTouchFeedback(true);
                }
                mProfileSecondView.setVisibility(View.VISIBLE);
                alignParentLayoutParam(mProfileFirstView, 0);
            } else {
                alignParentLayoutParam(mProfileFirstView, 1);
            }
            if (mProfileThird != null && mThreeSmallProfileImages && mProfileImagesVisible) {
                setImageOrPlaceholder(mProfileThirdView, mProfileThird.getIcon(), mProfileThird.getImageUrl());
                mProfileThirdView.setTag(mProfileThird);
                if (mProfileImagesClickable) {
                    mProfileThirdView.setOnClickListener(onProfileClickListener);
                    mProfileThirdView.disableTouchFeedback(false);
                } else {
                    mProfileThirdView.disableTouchFeedback(true);
                }
                mProfileThirdView.setVisibility(View.VISIBLE);
                alignParentLayoutParam(mProfileSecondView, 0);
            } else {
                alignParentLayoutParam(mProfileSecondView, 1);
            }
        } else if (mProfiles != null && mProfiles.size() > 0) {
            IProfile profile = mProfiles.get(0);
            mAccountHeaderTextSection.setTag(profile);
            mAccountHeaderTextSection.setVisibility(View.VISIBLE);
            mAccountSwitcherArrow.setVisibility(View.VISIBLE);
            mAccountHeaderTextSection.setBackgroundResource(mAccountHeaderTextSectionBackgroundResource);
            mCurrentProfileName.setText(profile.getName());
            mCurrentProfileEmail.setText(profile.getEmail());
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
        if (!mSelectionListEnabled) {
            mAccountSwitcherArrow.setVisibility(View.INVISIBLE);
            mAccountHeaderTextSection.setBackground(null);
        }
        if (!mSelectionListEnabledForSingleProfile && mProfileFirst == null) {
            mAccountSwitcherArrow.setVisibility(View.INVISIBLE);
            mAccountHeaderTextSection.setBackground(null);
        }
    }

    /**
     * small helper method to change the align parent lp for the view
     *
     * @param view
     * @param add
     */
    private void alignParentLayoutParam(View view, int add) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, add);
        if (Build.VERSION.SDK_INT >= 17) {
            lp.addRule(RelativeLayout.ALIGN_PARENT_END, add);
        }
        view.setLayoutParams(lp);
    }

    /**
     * small helper method to set an profile image or a placeholder
     *
     * @param iv
     * @param d
     */
    private void setImageOrPlaceholder(ImageView iv, Drawable d, String i) {
        if (d == null && i == null) {
            iv.setImageDrawable(new IconicsDrawable(iv.getContext(), GoogleMaterial.Icon.gmd_person).color(mTextColor).backgroundColorRes(R.color.primary).iconOffsetYDp(2).paddingDp(2).sizeDp(56));
        } else if (d != null){
            iv.setImageDrawable(d);
        } else {
            Context ctx = iv.getContext();
            Picasso.with(ctx).load(i).resize(ctx.getResources().getDimensionPixelSize(R.dimen.material_drawer_item_profile_icon), ctx.getResources().getDimensionPixelSize(R.dimen.material_drawer_item_profile_icon)).centerCrop().into(iv);
        }
    }

    /**
     * onProfileClickListener to notify onClick on a profile image
     */
    private View.OnClickListener onProfileClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final IProfile profile = (IProfile) v.getTag();
            switchProfiles(profile);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mOnAccountHeaderListener != null) {
                        mOnAccountHeaderListener.onProfileChanged(v, profile);
                    }

                    if (mDrawer != null) {
                        mDrawer.closeDrawer();
                    }
                }
            }, 200);
        }
    };

    /**
     * get the current selection
     *
     * @return
     */
    private int getCurrentSelection() {
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
            if (mOnAccountHeaderSelectionViewClickListener != null) {
                mOnAccountHeaderSelectionViewClickListener.onClick(v, (IProfile) v.getTag());
            }

            if (mAccountSwitcherArrow.getVisibility() == View.VISIBLE) {
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
                mAccountSwitcherArrow.setImageDrawable(new IconicsDrawable(ctx, GoogleMaterial.Icon.gmd_arrow_drop_up).sizeDp(24).paddingDp(6).color(mTextColor));
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
        for (IProfile profile : mProfiles) {
            if (profile == mCurrentProfile) {
                selectedPosition = position;
            }
            if (profile instanceof IDrawerItem) {
                profileDrawerItems.add((IDrawerItem) profile);
            }
            position = position + 1;
        }

        mDrawer.switchDrawerContent(onDrawerItemClickListener, profileDrawerItems, selectedPosition);
    }

    /**
     * onDrawerItemClickListener to catch the selection for the new profile!
     */
    private Drawer.OnDrawerItemClickListener onDrawerItemClickListener = new Drawer.OnDrawerItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, final View view, int position, long id, final IDrawerItem drawerItem) {
            if (drawerItem != null && drawerItem instanceof IProfile && ((IProfile) drawerItem).isSelectable()) {
                switchProfiles((IProfile) drawerItem);
            }
            mDrawer.setOnDrawerItemClickListener(null);
            //wrap the onSelection call and the reset stuff within a handler to prevent lag
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (drawerItem != null && drawerItem instanceof IProfile) {
                        if (mOnAccountHeaderListener != null) {
                            mOnAccountHeaderListener.onProfileChanged(view, (IProfile) drawerItem);
                        }
                    }
                    if (mDrawer != null) {
                        resetDrawerContent(view.getContext());
                    }

                }
            }, 350);
        }
    };

    /**
     * helper method to reset the drawer content
     */
    private void resetDrawerContent(Context ctx) {
        mDrawer.resetDrawerContent();
        mAccountSwitcherArrow.setImageDrawable(new IconicsDrawable(ctx, GoogleMaterial.Icon.gmd_arrow_drop_down).sizeDp(24).paddingDp(6).color(mTextColor));
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

    public static class Result {
        private final AccountHeader mAccountHeader;

        protected Result(AccountHeader accountHeader) {
            this.mAccountHeader = accountHeader;
        }

        /**
         * Get the Root view for the Header
         *
         * @return
         */
        public View getView() {
            return mAccountHeader.mAccountHeaderContainer;
        }

        /**
         * Set the drawer for the AccountHeader so we can use it for the select
         *
         * @param drawer
         */
        public void setDrawer(Drawer.Result drawer) {
            mAccountHeader.mDrawer = drawer;
        }

        /**
         * Returns the header background view so the dev can set everything on it
         *
         * @return
         */
        public ImageView getHeaderBackgroundView() {
            return mAccountHeader.mAccountHeaderBackground;
        }

        /**
         * Set the background for the Header
         *
         * @param headerBackground
         */
        public void setBackground(Drawable headerBackground) {
            mAccountHeader.mAccountHeaderBackground.setImageDrawable(headerBackground);
        }

        /**
         * Set the background for the Header as resource
         *
         * @param headerBackgroundRes
         */
        public void setBackgroundRes(int headerBackgroundRes) {
            mAccountHeader.mAccountHeaderBackground.setImageResource(headerBackgroundRes);
        }

        /**
         * Toggle the selection list (show or hide it)
         *
         * @param ctx
         */
        public void toggleSelectionList(Context ctx) {
            mAccountHeader.toggleSelectionList(ctx);
        }

        /**
         * returns if the selection list is currently shown
         *
         * @return
         */
        public boolean isSelectionListShown() {
            return mAccountHeader.mSelectionListShown;
        }

        /**
         * returns the current list of profiles set for this header
         *
         * @return
         */
        public ArrayList<IProfile> getProfiles() {
            return mAccountHeader.mProfiles;
        }

        /**
         * Set a new list of profiles for the header
         *
         * @param profiles
         */
        public void setProfiles(ArrayList<IProfile> profiles) {
            mAccountHeader.mProfiles = profiles;
            mAccountHeader.updateHeaderAndList();
        }

        /**
         * Selects the given profile and sets it to the new active profile
         *
         * @param profile
         */
        public void setActiveProfile(IProfile profile) {
            mAccountHeader.switchProfiles(profile);
        }

        /**
         * Selects a profile by its identifier
         *
         * @param identifier
         */
        public void setActiveProfile(int identifier) {
            if (mAccountHeader.mProfiles != null) {
                for (IProfile profile : mAccountHeader.mProfiles) {
                    if (profile instanceof Identifyable) {
                        if (profile.getIdentifier() == identifier) {
                            mAccountHeader.switchProfiles(profile);
                            return;
                        }
                    }
                }
            }
        }

        /**
         * Helper method to update a profile using it's identifier
         *
         * @param newProfile
         */
        public void updateProfileByIdentifier(IProfile newProfile) {
            if (mAccountHeader.mProfiles != null) {
                for (IProfile profile : mAccountHeader.mProfiles) {
                    if (profile instanceof Identifyable) {
                        if (profile.getIdentifier() == newProfile.getIdentifier()) {
                            profile = newProfile;
                            mAccountHeader.updateHeaderAndList();
                            return;
                        }
                    }
                }
            }
        }

        /**
         * Add new profiles to the existing list of profiles
         *
         * @param profiles
         */
        public void addProfiles(IProfile... profiles) {
            if (mAccountHeader.mProfiles == null) {
                mAccountHeader.mProfiles = new ArrayList<IProfile>();
            }
            if (profiles != null) {
                Collections.addAll(mAccountHeader.mProfiles, profiles);
            }

            mAccountHeader.updateHeaderAndList();
        }

        /**
         * Add a new profile at a specific position to the list
         *
         * @param profile
         * @param position
         */
        public void addProfile(IProfile profile, int position) {
            if (mAccountHeader.mProfiles == null) {
                mAccountHeader.mProfiles = new ArrayList<IProfile>();
            }
            mAccountHeader.mProfiles.add(position, profile);

            mAccountHeader.updateHeaderAndList();
        }

        /**
         * remove a profile from the given position
         *
         * @param position
         */
        public void removeProfile(int position) {
            if (mAccountHeader.mProfiles != null && mAccountHeader.mProfiles.size() > position) {
                mAccountHeader.mProfiles.remove(position);
            }

            mAccountHeader.updateHeaderAndList();
        }

        /**
         * try to remove the given profile
         *
         * @param profile
         */
        public void removeProfile(IProfile profile) {
            if (mAccountHeader.mProfiles != null) {
                mAccountHeader.mProfiles.remove(profile);
            }

            mAccountHeader.updateHeaderAndList();
        }

        /**
         * add the values to the bundle for saveInstanceState
         *
         * @param savedInstanceState
         * @return
         */
        public Bundle saveInstanceState(Bundle savedInstanceState) {
            if (savedInstanceState != null) {
                savedInstanceState.putInt(BUNDLE_SELECTION_HEADER, mAccountHeader.getCurrentSelection());
            }
            return savedInstanceState;
        }
    }


    public interface OnAccountHeaderListener {
        public void onProfileChanged(View view, IProfile profile);
    }

    public interface OnAccountHeaderSelectionViewClickListener {
        public void onClick(View view, IProfile profile);
    }
}
