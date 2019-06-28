package com.mikepenz.materialdrawer

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.Guideline
import androidx.core.view.ViewCompat
import com.mikepenz.iconics.IconicsColor.Companion.colorInt
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.IconicsSize.Companion.res
import com.mikepenz.materialdrawer.holder.*
import com.mikepenz.materialdrawer.icons.MaterialDrawerFont
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerUIUtils
import com.mikepenz.materialdrawer.view.BezelImageView
import com.mikepenz.materialize.util.UIUtils
import java.util.*

/**
 * Created by mikepenz on 23.05.15.
 */
open class AccountHeaderBuilder {
    // global references to views we need later
    internal lateinit var statusBarGuideline: Guideline
    internal lateinit var accountHeader: View
    internal lateinit var accountHeaderBackground: ImageView
    internal lateinit var currentProfileView: BezelImageView
    internal lateinit var accountSwitcherArrow: ImageView
    internal lateinit var currentProfileName: TextView
    internal lateinit var currentProfileEmail: TextView
    internal lateinit var profileFirstView: BezelImageView
    internal lateinit var profileSecondView: BezelImageView
    internal lateinit var profileThirdView: BezelImageView

    // global references to the profiles
    internal var currentProfile: IProfile<*>? = null
    internal var profileFirst: IProfile<*>? = null
    internal var profileSecond: IProfile<*>? = null
    internal var profileThird: IProfile<*>? = null


    // global stuff
    internal var selectionListShown = false
    internal var accountHeaderTextSectionBackgroundResource = -1

    // the activity to use
    internal var activity: Activity? = null

    // defines if we use the compactStyle
    internal var compactStyle = false

    // the typeface used for textViews within the AccountHeader
    internal var typeface: Typeface? = null

    // the typeface used for name textView only. overrides typeface
    internal var nameTypeface: Typeface? = null

    // the typeface used for email textView only. overrides typeface
    internal var emailTypeface: Typeface? = null

    // set the account header height
    internal var height: DimenHolder? = null

    //the background color for the slider
    internal var textColor: ColorHolder? = null

    //the current selected profile is visible in the list
    internal var currentHiddenInList = false

    //set to hide the first or second line
    internal var selectionFirstLineShown = true
    internal var selectionSecondLineShown = true


    //set one of these to define the text in the first or second line with in the account selector
    internal var selectionFirstLine: String? = null
    internal var selectionSecondLine: String? = null

    // set no divider below the header
    var paddingBelowHeader = true

    // set no divider below the header
    var dividerBelowHeader = true

    // set non translucent statusBar mode
    internal var translucentStatusBar = true

    //the background for the header
    internal var headerBackground: ImageHolder? = null

    //background scale type
    internal var headerBackgroundScaleType: ImageView.ScaleType? = null

    //profile images in the header are shown or not
    var profileImagesVisible = true

    //only the main profile image is visible
    internal var onlyMainProfileImageVisible = false

    //show small profile images but hide MainProfileImage
    internal var onlySmallProfileImagesVisible = false

    //close the drawer after a profile was clicked in the list
    internal var closeDrawerOnProfileListClick: Boolean? = null

    //reset the drawer list to the main drawer list after the profile was clicked in the list
    internal var resetDrawerOnProfileListClick = true

    // set the profile images clickable or not
    internal var profileImagesClickable = true

    // set to use the alternative profile header switching
    internal var alternativeProfileHeaderSwitching = false

    // enable 3 small header previews
    internal var threeSmallProfileImages = false

    //the delay which is waited before the drawer is closed
    internal var onProfileClickDrawerCloseDelay = 100

    // the onAccountHeaderProfileImageListener to set
    internal var onAccountHeaderProfileImageListener: AccountHeader.OnAccountHeaderProfileImageListener? = null

    // the onAccountHeaderSelectionListener to set
    internal var onAccountHeaderSelectionViewClickListener: AccountHeader.OnAccountHeaderSelectionViewClickListener? = null

    //set the selection list enabled if there is only a single profile
    internal var selectionListEnabledForSingleProfile = true

    //set the selection enabled disabled
    internal var selectionListEnabled = true

    // the drawerLayout to use
    internal lateinit var accountHeaderContainer: View

    // the profiles to display
    internal var profiles: MutableList<IProfile<*>>? = null

    // the click listener to be fired on profile or selection click
    internal var onAccountHeaderListener: AccountHeader.OnAccountHeaderListener? = null

    //the on long click listener to be fired on profile longClick inside the list
    internal var onAccountHeaderItemLongClickListener: AccountHeader.OnAccountHeaderItemLongClickListener? = null

    // the drawer to set the AccountSwitcher for
    internal var drawer: Drawer? = null

    // savedInstance to restore state
    internal var savedInstance: Bundle? = null

    /**
     * onProfileClickListener to notify onClick on the current profile image
     */
    private val onCurrentProfileClickListener = View.OnClickListener { v -> onProfileImageClick(v, true) }

    /**
     * onProfileClickListener to notify onClick on a profile image
     */
    private val onProfileClickListener = View.OnClickListener { v -> onProfileImageClick(v, false) }

    /**
     * onProfileLongClickListener to call the onProfileImageLongClick on the current profile image
     */
    private val onCurrentProfileLongClickListener = View.OnLongClickListener { v ->
        if (onAccountHeaderProfileImageListener != null) {
            val profile = v.getTag(R.id.material_drawer_profile_header) as IProfile<*>
            return@OnLongClickListener onAccountHeaderProfileImageListener?.onProfileImageLongClick(v, profile, true)
                    ?: false
        }
        false
    }

    /**
     * onProfileLongClickListener to call the onProfileImageLongClick on a profile image
     */
    private val onProfileLongClickListener = View.OnLongClickListener { v ->
        if (onAccountHeaderProfileImageListener != null) {
            val profile = v.getTag(R.id.material_drawer_profile_header) as IProfile<*>
            return@OnLongClickListener onAccountHeaderProfileImageListener?.onProfileImageLongClick(v, profile, false)
                    ?: false
        }
        false
    }

    /**
     * get the current selection
     *
     * @return
     */
    internal val currentSelection: Int
        get() {
            val mProfiles = this.profiles ?: return -1
            val mCurrentProfile = this.currentProfile ?: return -1
            for ((i, profile) in mProfiles.withIndex()) {
                if (profile === mCurrentProfile) {
                    return i
                }
            }
            return -1
        }

    /**
     * onSelectionClickListener to notify the onClick on the checkbox
     */
    private val onSelectionClickListener = View.OnClickListener { v ->
        val consumed = onAccountHeaderSelectionViewClickListener?.onClick(v, v.getTag(R.id.material_drawer_profile_header) as IProfile<*>)
                ?: false
        if (accountSwitcherArrow.visibility == View.VISIBLE && !consumed) {
            toggleSelectionList(v.context)
        }
    }

    /**
     * onDrawerItemClickListener to catch the selection for the new profile!
     */
    private val onDrawerItemClickListener = object : Drawer.OnDrawerItemClickListener {
        override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
            val isCurrentSelectedProfile: Boolean = if (drawerItem is IProfile<*> && drawerItem.isSelectable) {
                switchProfiles(drawerItem as IProfile<*>)
            } else {
                false
            }

            if (resetDrawerOnProfileListClick) {
                drawer?.onDrawerItemClickListener = null
            }

            //wrap the onSelection call and the reset stuff within a handler to prevent lag
            if (resetDrawerOnProfileListClick && drawer != null && view != null && view.context != null) {
                resetDrawerContent(view.context)
            }

            //notify the MiniDrawer about the clicked profile (only if one exists and is hooked to the Drawer
            drawer?.drawerBuilder?.let {
                it.mMiniDrawer?.onProfileClick()
            }


            var consumed = false
            if (drawerItem is IProfile<*>) {
                consumed = onAccountHeaderListener?.onProfileChanged(view, drawerItem as IProfile<*>, isCurrentSelectedProfile)
                        ?: false
            }

            //if a custom behavior was chosen via the CloseDrawerOnProfileListClick then use this. else react on the result of the onProfileChanged listener
            closeDrawerOnProfileListClick?.let {
                consumed = consumed && (!it)
            }

            //totally custom handling of the drawer behavior as otherwise the selection of the profile list is set to the Drawer
            if (!consumed) {
                //close the drawer after click
                drawer?.drawerBuilder?.closeDrawerDelayed()
            }

            //consume the event to prevent setting the clicked item as selected in the already switched item list
            return true
        }
    }

    /**
     * onDrawerItemLongClickListener to catch the longClick for a profile
     */
    private val onDrawerItemLongClickListener = object : Drawer.OnDrawerItemLongClickListener {
        override fun onItemLongClick(view: View, position: Int, drawerItem: IDrawerItem<*>): Boolean {
            //if a longClickListener was defined use it
            if (onAccountHeaderItemLongClickListener != null) {
                val isCurrentSelectedProfile: Boolean = drawerItem.isSelected

                if (drawerItem is IProfile<*>) {
                    return onAccountHeaderItemLongClickListener?.onProfileLongClick(view, drawerItem as IProfile<*>, isCurrentSelectedProfile)
                            ?: false
                }
            }
            return false
        }
    }

    /**
     * Pass the activity you use the drawer in ;)
     *
     * @param activity
     * @return
     */
    fun withActivity(activity: Activity): AccountHeaderBuilder {
        this.activity = activity
        return this
    }

    /**
     * Defines if we should use the compact style for the header.
     *
     * @param compactStyle
     * @return
     */
    fun withCompactStyle(compactStyle: Boolean): AccountHeaderBuilder {
        this.compactStyle = compactStyle
        return this
    }

    /**
     * Define the typeface which will be used for all textViews in the AccountHeader
     *
     * @param typeface
     * @return
     */
    fun withTypeface(typeface: Typeface): AccountHeaderBuilder {
        this.typeface = typeface
        return this
    }

    /**
     * Define the typeface which will be used for name textView in the AccountHeader.
     * Overrides typeface supplied to [AccountHeaderBuilder.withTypeface]
     *
     * @param typeface
     * @return
     * @see .withTypeface
     */
    fun withNameTypeface(typeface: Typeface): AccountHeaderBuilder {
        this.nameTypeface = typeface
        return this
    }

    /**
     * Define the typeface which will be used for email textView in the AccountHeader.
     * Overrides typeface supplied to [AccountHeaderBuilder.withTypeface]
     *
     * @param typeface
     * @return
     * @see .withTypeface
     */
    fun withEmailTypeface(typeface: Typeface): AccountHeaderBuilder {
        this.emailTypeface = typeface
        return this
    }

    /**
     * set the height for the header
     *
     * @param heightPx
     * @return
     */
    fun withHeightPx(heightPx: Int): AccountHeaderBuilder {
        this.height = DimenHolder.fromPixel(heightPx)
        return this
    }


    /**
     * set the height for the header
     *
     * @param heightDp
     * @return
     */
    fun withHeightDp(heightDp: Int): AccountHeaderBuilder {
        this.height = DimenHolder.fromDp(heightDp)
        return this
    }

    /**
     * set the height for the header by resource
     *
     * @param heightRes
     * @return
     */
    fun withHeightRes(@DimenRes heightRes: Int): AccountHeaderBuilder {
        this.height = DimenHolder.fromResource(heightRes)
        return this
    }

    /**
     * set the background for the slider as color
     *
     * @param textColor
     * @return
     */
    fun withTextColor(@ColorInt textColor: Int): AccountHeaderBuilder {
        this.textColor = ColorHolder.fromColor(textColor)
        return this
    }

    /**
     * set the background for the slider as resource
     *
     * @param textColorRes
     * @return
     */
    fun withTextColorRes(@ColorRes textColorRes: Int): AccountHeaderBuilder {
        this.textColor = ColorHolder.fromColorRes(textColorRes)
        return this
    }

    /**
     * hide the current selected profile from the list
     *
     * @param currentProfileHiddenInList
     * @return
     */
    fun withCurrentProfileHiddenInList(currentProfileHiddenInList: Boolean): AccountHeaderBuilder {
        currentHiddenInList = currentProfileHiddenInList
        return this
    }

    /**
     * set this to false if you want to hide the first line of the selection box in the header (first line would be the name)
     *
     * @param selectionFirstLineShown
     * @return
     */
    @Deprecated("replaced by {@link #withSelectionFirstLineShown}")
    fun withSelectionFistLineShown(selectionFirstLineShown: Boolean): AccountHeaderBuilder {
        this.selectionFirstLineShown = selectionFirstLineShown
        return this
    }

    /**
     * set this to false if you want to hide the first line of the selection box in the header (first line would be the name)
     *
     * @param selectionFirstLineShown
     * @return
     */
    fun withSelectionFirstLineShown(selectionFirstLineShown: Boolean): AccountHeaderBuilder {
        this.selectionFirstLineShown = selectionFirstLineShown
        return this
    }

    /**
     * set this to false if you want to hide the second line of the selection box in the header (second line would be the e-mail)
     *
     * @param selectionSecondLineShown
     * @return
     */
    fun withSelectionSecondLineShown(selectionSecondLineShown: Boolean): AccountHeaderBuilder {
        this.selectionSecondLineShown = selectionSecondLineShown
        return this
    }

    /**
     * set this to define the first line in the selection area if there is no profile
     * note this will block any values from profiles!
     *
     * @param selectionFirstLine
     * @return
     */
    fun withSelectionFirstLine(selectionFirstLine: String): AccountHeaderBuilder {
        this.selectionFirstLine = selectionFirstLine
        return this
    }

    /**
     * set this to define the second line in the selection area if there is no profile
     * note this will block any values from profiles!
     *
     * @param selectionSecondLine
     * @return
     */
    fun withSelectionSecondLine(selectionSecondLine: String): AccountHeaderBuilder {
        this.selectionSecondLine = selectionSecondLine
        return this
    }

    /**
     * Set this to false if you want no padding below the Header
     *
     * @param paddingBelowHeader
     * @return
     */
    fun withPaddingBelowHeader(paddingBelowHeader: Boolean): AccountHeaderBuilder {
        this.paddingBelowHeader = paddingBelowHeader
        return this
    }

    /**
     * Set this to false if you want no divider below the Header
     *
     * @param dividerBelowHeader
     * @return
     */
    fun withDividerBelowHeader(dividerBelowHeader: Boolean): AccountHeaderBuilder {
        this.dividerBelowHeader = dividerBelowHeader
        return this
    }

    /**
     * Set or disable this if you use a translucent statusbar
     *
     * @param translucentStatusBar
     * @return
     */
    fun withTranslucentStatusBar(translucentStatusBar: Boolean): AccountHeaderBuilder {
        this.translucentStatusBar = translucentStatusBar
        return this
    }

    /**
     * set the background for the slider as color
     *
     * @param headerBackground
     * @return
     */
    fun withHeaderBackground(headerBackground: Drawable): AccountHeaderBuilder {
        this.headerBackground = ImageHolder(headerBackground)
        return this
    }

    /**
     * set the background for the header as resource
     *
     * @param headerBackgroundRes
     * @return
     */
    fun withHeaderBackground(@DrawableRes headerBackgroundRes: Int): AccountHeaderBuilder {
        this.headerBackground = ImageHolder(headerBackgroundRes)
        return this
    }

    /**
     * set the background for the header via the ImageHolder class
     *
     * @param headerBackground
     * @return
     */
    fun withHeaderBackground(headerBackground: ImageHolder): AccountHeaderBuilder {
        this.headerBackground = headerBackground
        return this
    }

    /**
     * define the ScaleType for the header background
     *
     * @param headerBackgroundScaleType
     * @return
     */
    fun withHeaderBackgroundScaleType(headerBackgroundScaleType: ImageView.ScaleType): AccountHeaderBuilder {
        this.headerBackgroundScaleType = headerBackgroundScaleType
        return this
    }

    /**
     * define if the profile images in the header are shown or not
     *
     * @param profileImagesVisible
     * @return
     */
    fun withProfileImagesVisible(profileImagesVisible: Boolean): AccountHeaderBuilder {
        this.profileImagesVisible = profileImagesVisible
        return this
    }

    /**
     * define if only the main (current selected) profile image should be visible
     *
     * @param onlyMainProfileImageVisible
     * @return
     */
    fun withOnlyMainProfileImageVisible(onlyMainProfileImageVisible: Boolean): AccountHeaderBuilder {
        this.onlyMainProfileImageVisible = onlyMainProfileImageVisible
        return this
    }

    /**
     * define if only the small profile images should be visible
     *
     * @param onlySmallProfileImagesVisible
     * @return
     */
    fun withOnlySmallProfileImagesVisible(onlySmallProfileImagesVisible: Boolean): AccountHeaderBuilder {
        this.onlySmallProfileImagesVisible = onlySmallProfileImagesVisible
        return this
    }

    /**
     * define if the drawer should close if the user clicks on a profile item if the selection list is shown
     *
     * @param closeDrawerOnProfileListClick
     * @return
     */
    fun withCloseDrawerOnProfileListClick(closeDrawerOnProfileListClick: Boolean): AccountHeaderBuilder {
        this.closeDrawerOnProfileListClick = closeDrawerOnProfileListClick
        return this
    }

    /**
     * define if the drawer selection list should be reseted after the user clicks on a profile item if the selection list is shown
     *
     * @param resetDrawerOnProfileListClick
     * @return
     */
    fun withResetDrawerOnProfileListClick(resetDrawerOnProfileListClick: Boolean): AccountHeaderBuilder {
        this.resetDrawerOnProfileListClick = resetDrawerOnProfileListClick
        return this
    }

    /**
     * enable or disable the profile images to be clickable
     *
     * @param profileImagesClickable
     * @return
     */
    fun withProfileImagesClickable(profileImagesClickable: Boolean): AccountHeaderBuilder {
        this.profileImagesClickable = profileImagesClickable
        return this
    }

    /**
     * enable the alternative profile header switching
     *
     * @param alternativeProfileHeaderSwitching
     * @return
     */
    fun withAlternativeProfileHeaderSwitching(alternativeProfileHeaderSwitching: Boolean): AccountHeaderBuilder {
        this.alternativeProfileHeaderSwitching = alternativeProfileHeaderSwitching
        return this
    }

    /**
     * enable the extended profile icon view with 3 small header images instead of two
     *
     * @param threeSmallProfileImages
     * @return
     */
    fun withThreeSmallProfileImages(threeSmallProfileImages: Boolean): AccountHeaderBuilder {
        this.threeSmallProfileImages = threeSmallProfileImages
        return this
    }

    /**
     * Define the delay for the drawer close operation after a click.
     * This is a small trick to improve the speed (and remove lag) if you open a new activity after a DrawerItem
     * was selected.
     * NOTE: Disable this by passing -1
     *
     * @param onProfileClickDrawerCloseDelay the delay in MS (-1 to disable)
     * @return
     */
    fun withOnProfileClickDrawerCloseDelay(onProfileClickDrawerCloseDelay: Int): AccountHeaderBuilder {
        this.onProfileClickDrawerCloseDelay = onProfileClickDrawerCloseDelay
        return this
    }

    /**
     * set click / longClick listener for the header images
     *
     * @param onAccountHeaderProfileImageListener
     * @return
     */
    fun withOnAccountHeaderProfileImageListener(onAccountHeaderProfileImageListener: AccountHeader.OnAccountHeaderProfileImageListener): AccountHeaderBuilder {
        this.onAccountHeaderProfileImageListener = onAccountHeaderProfileImageListener
        return this
    }

    /**
     * set a onSelection listener for the selection box
     *
     * @param onAccountHeaderSelectionViewClickListener
     * @return
     */
    fun withOnAccountHeaderSelectionViewClickListener(onAccountHeaderSelectionViewClickListener: AccountHeader.OnAccountHeaderSelectionViewClickListener): AccountHeaderBuilder {
        this.onAccountHeaderSelectionViewClickListener = onAccountHeaderSelectionViewClickListener
        return this
    }

    /**
     * enable or disable the selection list if there is only a single profile
     *
     * @param selectionListEnabledForSingleProfile
     * @return
     */
    fun withSelectionListEnabledForSingleProfile(selectionListEnabledForSingleProfile: Boolean): AccountHeaderBuilder {
        this.selectionListEnabledForSingleProfile = selectionListEnabledForSingleProfile
        return this
    }

    /**
     * enable or disable the selection list
     *
     * @param selectionListEnabled
     * @return
     */
    fun withSelectionListEnabled(selectionListEnabled: Boolean): AccountHeaderBuilder {
        this.selectionListEnabled = selectionListEnabled
        return this
    }

    /**
     * You can pass a custom view for the drawer lib. note this requires the same structure as the drawer.xml
     *
     * @param accountHeader
     * @return
     */
    fun withAccountHeader(accountHeader: View): AccountHeaderBuilder {
        this.accountHeaderContainer = accountHeader
        return this
    }

    /**
     * You can pass a custom layout for the drawer lib. see the drawer.xml in layouts of this lib on GitHub
     *
     * @param resLayout
     * @return
     */
    fun withAccountHeader(@LayoutRes resLayout: Int): AccountHeaderBuilder {
        if (activity == null) {
            throw RuntimeException("please pass an activity first to use this call")
        }

        activity?.let {
            if (resLayout != -1) {
                this.accountHeaderContainer = it.layoutInflater.inflate(resLayout, null, false)
            } else {
                if (compactStyle) {
                    this.accountHeaderContainer = it.layoutInflater.inflate(R.layout.material_drawer_compact_header, null, false)
                } else {
                    this.accountHeaderContainer = it.layoutInflater.inflate(R.layout.material_drawer_header, null, false)
                }
            }
        }

        return this
    }

    /**
     * set the arrayList of DrawerItems for the drawer
     *
     * @param profiles
     * @return
     */
    fun withProfiles(profiles: MutableList<IProfile<*>>): AccountHeaderBuilder {
        drawer?.drawerBuilder?.idDistributor?.checkIds(profiles)
        this.profiles = profiles
        return this
    }

    /**
     * add single ore more DrawerItems to the Drawer
     *
     * @param profiles
     * @return
     */
    fun addProfiles(vararg profiles: IProfile<*>): AccountHeaderBuilder {
        if (this.profiles == null) {
            this.profiles = ArrayList()
        }

        this.profiles?.let {
            drawer?.drawerBuilder?.idDistributor?.checkIds(*profiles)
            Collections.addAll<IProfile<*>>(it, *profiles)
        }

        return this
    }

    /**
     * add a listener for the accountHeader
     *
     * @param onAccountHeaderListener
     * @return
     */
    fun withOnAccountHeaderListener(onAccountHeaderListener: AccountHeader.OnAccountHeaderListener): AccountHeaderBuilder {
        this.onAccountHeaderListener = onAccountHeaderListener
        return this
    }

    /**
     * the on long click listener to be fired on profile longClick inside the list
     *
     * @param onAccountHeaderItemLongClickListener
     * @return
     */
    fun withOnAccountHeaderItemLongClickListener(onAccountHeaderItemLongClickListener: AccountHeader.OnAccountHeaderItemLongClickListener): AccountHeaderBuilder {
        this.onAccountHeaderItemLongClickListener = onAccountHeaderItemLongClickListener
        return this
    }

    /**
     * @param drawer
     * @return
     */
    fun withDrawer(drawer: Drawer): AccountHeaderBuilder {
        this.drawer = drawer

        //set the top padding to 0 as this would happen when the AccountHeader is created during Drawer build time
        drawer.recyclerView.setPadding(drawer.recyclerView.paddingLeft, 0, drawer.recyclerView.paddingRight, drawer.recyclerView.paddingBottom)
        return this
    }

    /**
     * create the drawer with the values of a savedInstance
     *
     * @param savedInstance
     * @return
     */
    fun withSavedInstance(savedInstance: Bundle?): AccountHeaderBuilder {
        this.savedInstance = savedInstance
        return this
    }

    /**
     * helper method to set the height for the header!
     *
     * @param height
     */
    private fun setHeaderHeight(height: Int) {
        accountHeaderContainer.layoutParams?.let {
            it.height = height
            accountHeaderContainer.layoutParams = it
        }

        accountHeaderContainer.findViewById<View>(R.id.material_drawer_account_header)?.let { accountHeader ->
            val p = accountHeader.layoutParams
            if (p != null) {
                p.height = height
                accountHeader.layoutParams = p
            }
        }

        accountHeaderContainer.findViewById<View>(R.id.material_drawer_account_header_background)?.let { accountHeaderBackground ->
            val p = accountHeaderBackground.layoutParams
            p.height = height
            accountHeaderBackground.layoutParams = p
        }
    }

    /**
     * a small helper to handle the selectionView
     *
     * @param on
     */
    private fun handleSelectionView(profile: IProfile<*>?, on: Boolean) {
        if (on) {
            if (Build.VERSION.SDK_INT >= 23) {
                accountHeaderContainer.foreground = AppCompatResources.getDrawable(accountHeaderContainer.context, accountHeaderTextSectionBackgroundResource)
            } else {
                // todo foreground thing?
            }
            accountHeaderContainer.setOnClickListener(onSelectionClickListener)
            accountHeaderContainer.setTag(R.id.material_drawer_profile_header, profile)
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                accountHeaderContainer.foreground = null
            } else {
                // TODO foreground reset
            }
            accountHeaderContainer.setOnClickListener(null)
        }
    }

    /**
     * method to build the header view
     *
     * @return
     */
    fun build(): AccountHeader {
        val activity = this.activity
                ?: throw RuntimeException("please pass an activity first to use this call")

        // if the user has not set a accountHeader use the default one :D
        if (!::accountHeaderContainer.isInitialized) {
            withAccountHeader(-1)
        }

        // get the header view within the container
        accountHeader = accountHeaderContainer.findViewById(R.id.material_drawer_account_header)
        statusBarGuideline = accountHeaderContainer.findViewById(R.id.material_drawer_statusbar_guideline)

        //the default min header height by default 148dp
        val defaultHeaderMinHeight = activity.resources.getDimensionPixelSize(R.dimen.material_drawer_account_header_height)
        val statusBarHeight = UIUtils.getStatusBarHeight(activity, true)

        // handle the height for the header
        var height = 0
        this.height?.let {
            height = it.asPixel(activity)
        } ?: run {
            if (compactStyle) {
                height = activity.resources.getDimensionPixelSize(R.dimen.material_drawer_account_header_height_compact)
            } else {
                //calculate the header height by getting the optimal drawer width and calculating it * 9 / 16
                height = (DrawerUIUtils.getOptimalDrawerWidth(activity) * AccountHeader.NAVIGATION_DRAWER_ACCOUNT_ASPECT_RATIO).toInt()

                //if we are lower than api 19 (>= 19 we have a translucentStatusBar) the height should be a bit lower
                //probably even if we are non translucent on > 19 devices?
                if (Build.VERSION.SDK_INT < 19) {
                    val tempHeight = height - statusBarHeight
                    //if we are lower than api 19 we are not able to have a translucent statusBar so we remove the height of the statusBar from the padding
                    //to prevent display issues we only reduce the height if we still fit the required minHeight of 148dp (R.dimen.material_drawer_account_header_height)
                    //we remove additional 8dp from the defaultMinHeaderHeight as there is some buffer in the header and to prevent to large spacings
                    if (tempHeight > defaultHeaderMinHeight - UIUtils.convertDpToPixel(8f, activity)) {
                        height = tempHeight
                    }
                }
            }
        }

        // handle everything if we have a translucent status bar which only is possible on API >= 19
        if (translucentStatusBar && Build.VERSION.SDK_INT >= 21) {
            statusBarGuideline.setGuidelineBegin(statusBarHeight)

            //in fact it makes no difference if we have a translucent statusBar or not. we want 9/16 just if we are not compact
            if (compactStyle) {
                height += statusBarHeight
            } else if (height - statusBarHeight <= defaultHeaderMinHeight) {
                //if the height + statusBar of the header is lower than the required 148dp + statusBar we change the height to be able to display all the data
                height = defaultHeaderMinHeight + statusBarHeight
            }
        }

        //set the height for the header
        setHeaderHeight(height)

        // get the background view
        accountHeaderBackground = accountHeaderContainer.findViewById(R.id.material_drawer_account_header_background)
        // set the background
        headerBackground?.applyTo(accountHeaderBackground, DrawerImageLoader.Tags.ACCOUNT_HEADER.name)

        if (headerBackgroundScaleType != null) {
            accountHeaderBackground.scaleType = headerBackgroundScaleType
        }

        // get the text color to use for the text section
        val textColor = textColor.applyColor(activity, R.attr.material_drawer_header_selection_text, R.color.material_drawer_header_selection_text)
        val subTextColor = this.textColor.applyColor(activity, R.attr.material_drawer_header_selection_subtext, R.color.material_drawer_header_selection_subtext)

        accountHeaderTextSectionBackgroundResource = UIUtils.getSelectableBackgroundRes(activity)
        handleSelectionView(currentProfile, true)

        // set the arrow :D
        accountSwitcherArrow = accountHeaderContainer.findViewById(R.id.material_drawer_account_header_text_switcher)
        accountSwitcherArrow.setImageDrawable(IconicsDrawable(activity, MaterialDrawerFont.Icon.mdf_arrow_drop_down).size(res(R.dimen.material_drawer_account_header_dropdown)).padding(res(R.dimen.material_drawer_account_header_dropdown_padding)).color(colorInt(subTextColor)))

        //get the fields for the name
        currentProfileView = accountHeader.findViewById(R.id.material_drawer_account_header_current)
        currentProfileName = accountHeader.findViewById(R.id.material_drawer_account_header_name)
        currentProfileEmail = accountHeader.findViewById(R.id.material_drawer_account_header_email)

        //set the typeface for the AccountHeader
        if (nameTypeface != null) {
            currentProfileName.typeface = nameTypeface
        } else if (typeface != null) {
            currentProfileName.typeface = typeface
        }

        if (emailTypeface != null) {
            currentProfileEmail.typeface = emailTypeface
        } else if (typeface != null) {
            currentProfileEmail.typeface = typeface
        }

        currentProfileName.setTextColor(textColor)
        currentProfileEmail.setTextColor(subTextColor)

        profileFirstView = accountHeader.findViewById(R.id.material_drawer_account_header_small_first)
        profileSecondView = accountHeader.findViewById(R.id.material_drawer_account_header_small_second)
        profileThirdView = accountHeader.findViewById(R.id.material_drawer_account_header_small_third)

        //calculate the profiles to set
        calculateProfiles()

        //process and build the profiles
        buildProfiles()

        // try to restore all saved values again
        savedInstance?.let { savedInstance ->
            val selection = savedInstance.getInt(AccountHeader.BUNDLE_SELECTION_HEADER, -1)
            if (selection != -1) {
                //predefine selection (should be the first element
                profiles?.let {
                    if (selection > -1 && selection < it.size) {
                        switchProfiles(it[selection])
                    }
                }
            }
        }

        //everything created. now set the header
        drawer?.apply {
            setHeader(accountHeaderContainer, paddingBelowHeader, dividerBelowHeader)
        }

        //forget the reference to the activity
        this.activity = null

        return AccountHeader(this)
    }

    /**
     * helper method to calculate the order of the profiles
     */
    internal fun calculateProfiles() {
        if (profiles == null) {
            profiles = ArrayList()
        }

        profiles?.let { mProfiles ->
            if (currentProfile == null) {
                var setCount = 0
                val size = mProfiles.size
                for (i in 0 until size) {
                    if (mProfiles.size > i && mProfiles[i].isSelectable) {
                        if (setCount == 0 && currentProfile == null) {
                            currentProfile = mProfiles[i]
                        } else if (setCount == 1 && profileFirst == null) {
                            profileFirst = mProfiles[i]
                        } else if (setCount == 2 && profileSecond == null) {
                            profileSecond = mProfiles[i]
                        } else if (setCount == 3 && profileThird == null) {
                            profileThird = mProfiles[i]
                        }
                        setCount++
                    }
                }

                return
            }

            val previousActiveProfiles = arrayOf(currentProfile, profileFirst, profileSecond, profileThird)

            val newActiveProfiles = arrayOfNulls<IProfile<*>>(4)
            val unusedProfiles = Stack<IProfile<*>>()

            // try to keep existing active profiles in the same positions
            for (i in mProfiles.indices) {
                val p = mProfiles[i]
                if (p.isSelectable) {
                    var used = false
                    for (j in 0..3) {
                        if (previousActiveProfiles[j] === p) {
                            newActiveProfiles[j] = p
                            used = true
                            break
                        }
                    }
                    if (!used) {
                        unusedProfiles.push(p)
                    }
                }
            }

            val activeProfiles = Stack<IProfile<*>>()
            // try to fill the gaps with new available profiles
            for (i in 0..3) {
                if (newActiveProfiles[i] != null) {
                    activeProfiles.push(newActiveProfiles[i])
                } else if (!unusedProfiles.isEmpty()) {
                    activeProfiles.push(unusedProfiles.pop())
                }
            }

            val reversedActiveProfiles = Stack<IProfile<*>>()
            while (!activeProfiles.empty()) {
                reversedActiveProfiles.push(activeProfiles.pop())
            }

            // reassign active profiles
            if (reversedActiveProfiles.isEmpty()) {
                currentProfile = null
            } else {
                currentProfile = reversedActiveProfiles.pop()
            }
            if (reversedActiveProfiles.isEmpty()) {
                profileFirst = null
            } else {
                profileFirst = reversedActiveProfiles.pop()
            }
            if (reversedActiveProfiles.isEmpty()) {
                profileSecond = null
            } else {
                profileSecond = reversedActiveProfiles.pop()
            }
            if (reversedActiveProfiles.isEmpty()) {
                profileThird = null
            } else {
                profileThird = reversedActiveProfiles.pop()
            }
        }
    }

    /**
     * helper method to switch the profiles
     *
     * @param newSelection
     * @return true if the new selection was the current profile
     */
    internal fun switchProfiles(newSelection: IProfile<*>?): Boolean {
        if (newSelection == null) {
            return false
        }
        if (currentProfile === newSelection) {
            return true
        }

        if (alternativeProfileHeaderSwitching) {
            var prevSelection = -1
            when {
                profileFirst === newSelection -> prevSelection = 1
                profileSecond === newSelection -> prevSelection = 2
                profileThird === newSelection -> prevSelection = 3
            }

            val tmp = currentProfile
            currentProfile = newSelection

            when (prevSelection) {
                1 -> profileFirst = tmp
                2 -> profileSecond = tmp
                3 -> profileThird = tmp
            }
        } else {
            if (profiles != null) {
                val previousActiveProfiles = ArrayList<IProfile<*>>(Arrays.asList<IProfile<*>>(currentProfile, profileFirst, profileSecond, profileThird))

                if (previousActiveProfiles.contains(newSelection)) {
                    var position = -1

                    for (i in 0..3) {
                        if (previousActiveProfiles[i] === newSelection) {
                            position = i
                            break
                        }
                    }

                    if (position != -1) {
                        previousActiveProfiles.removeAt(position)
                        previousActiveProfiles.add(0, newSelection)

                        currentProfile = previousActiveProfiles[0]
                        profileFirst = previousActiveProfiles[1]
                        profileSecond = previousActiveProfiles[2]
                        profileThird = previousActiveProfiles[3]
                    }
                } else {
                    profileThird = profileSecond
                    profileSecond = profileFirst
                    profileFirst = currentProfile
                    currentProfile = newSelection
                }
            }
        }

        //if we only show the small profile images we have to make sure the first (would be the current selected) profile is also shown
        if (onlySmallProfileImagesVisible) {
            profileThird = profileSecond
            profileSecond = profileFirst
            profileFirst = currentProfile
            //currentProfile = profileThird;
        }

        buildProfiles()

        return false
    }

    /**
     * helper method to build the views for the ui
     */
    internal fun buildProfiles() {
        currentProfileView.visibility = View.GONE
        accountSwitcherArrow.visibility = View.GONE
        profileFirstView.visibility = View.GONE
        profileFirstView.setOnClickListener(null)
        profileSecondView.visibility = View.GONE
        profileSecondView.setOnClickListener(null)
        profileThirdView.visibility = View.GONE
        profileThirdView.setOnClickListener(null)
        currentProfileName.text = ""
        currentProfileEmail.text = ""

        handleSelectionView(currentProfile, true)

        val mCurrentProfile = this.currentProfile
        val mProfiles = this.profiles
        if (mCurrentProfile != null) {
            if ((profileImagesVisible || onlyMainProfileImageVisible) && !onlySmallProfileImagesVisible) {
                currentProfileView.contentDescription = mCurrentProfile.email?.text ?: mCurrentProfile.name?.text ?: currentProfileView.context.getString(R.string.material_drawer_profile_content_description)
                setImageOrPlaceholder(currentProfileView, mCurrentProfile.icon)
                if (profileImagesClickable) {
                    currentProfileView.setOnClickListener(onCurrentProfileClickListener)
                    currentProfileView.setOnLongClickListener(onCurrentProfileLongClickListener)
                    currentProfileView.disableTouchFeedback(false)
                } else {
                    currentProfileView.disableTouchFeedback(true)
                }
                currentProfileView.visibility = View.VISIBLE
                currentProfileView.invalidate()
            } else if (compactStyle) {
                currentProfileView.visibility = View.GONE
            }

            handleSelectionView(mCurrentProfile, true)
            accountSwitcherArrow.visibility = View.VISIBLE
            currentProfileView.setTag(R.id.material_drawer_profile_header, mCurrentProfile)

            StringHolder.applyTo(mCurrentProfile.name, currentProfileName)
            StringHolder.applyTo(mCurrentProfile.email, currentProfileEmail)

            /**
             * Apply the profile information to the provided imageView
             */
            fun IProfile<*>?.applyProfile(imageView: BezelImageView) {
                this ?: return
                setImageOrPlaceholder(imageView, this.icon)
                imageView.setTag(R.id.material_drawer_profile_header, this)
                imageView.contentDescription = this.email?.text ?: this.name?.text ?: imageView.context.getString(R.string.material_drawer_profile_content_description)
                if (profileImagesClickable) {
                    imageView.setOnClickListener(onProfileClickListener)
                    imageView.setOnLongClickListener(onProfileLongClickListener)
                    imageView.disableTouchFeedback(false)
                } else {
                    imageView.disableTouchFeedback(true)
                }
                imageView.visibility = View.VISIBLE
                imageView.invalidate()
            }

            if (profileImagesVisible && !onlyMainProfileImageVisible) {
                profileFirst.applyProfile(profileFirstView)
                profileSecond.applyProfile(profileSecondView)

                if (threeSmallProfileImages) {
                    profileThird.applyProfile(profileThirdView)
                }
            }
        } else if (mProfiles != null && mProfiles.size > 0) {
            val profile = mProfiles[0]
            accountHeader.setTag(R.id.material_drawer_profile_header, profile)
            handleSelectionView(mCurrentProfile, true)
            accountSwitcherArrow.visibility = View.VISIBLE
        }

        if (!selectionFirstLineShown) {
            currentProfileName.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(selectionFirstLine)) {
            currentProfileName.text = selectionFirstLine
        }
        if (!selectionSecondLineShown) {
            currentProfileEmail.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(selectionSecondLine)) {
            currentProfileEmail.text = selectionSecondLine
        }

        //if we disabled the list
        if (!selectionListEnabled || !selectionListEnabledForSingleProfile && profileFirst == null && (mProfiles == null || mProfiles?.size == 1)) {
            accountSwitcherArrow.visibility = View.GONE
            handleSelectionView(null, false)
        }

        //if we disabled the list but still have set a custom listener
        if (onAccountHeaderSelectionViewClickListener != null) {
            handleSelectionView(mCurrentProfile, true)
        }
    }

    /**
     * small helper method to set an profile image or a placeholder
     *
     * @param iv
     * @param imageHolder
     */
    private fun setImageOrPlaceholder(iv: ImageView, imageHolder: ImageHolder?) {
        //cancel previous started image loading processes
        DrawerImageLoader.instance.cancelImage(iv)
        //set the placeholder
        iv.setImageDrawable(DrawerImageLoader.instance.imageLoader?.placeholder(iv.context, DrawerImageLoader.Tags.PROFILE.name))
        //set the real image (probably also the uri)
        imageHolder?.applyTo(iv, DrawerImageLoader.Tags.PROFILE.name)
    }

    /**
     * calls the mOnAccountHEaderProfileImageListener and continues with the actions afterwards
     *
     * @param v
     * @param current
     */
    private fun onProfileImageClick(v: View, current: Boolean) {
        val profile = v.getTag(R.id.material_drawer_profile_header) as IProfile<*>

        val consumed = onAccountHeaderProfileImageListener?.onProfileImageClick(v, profile, current)
                ?: false

        //if the event was already consumed by the click don't continue. note that this will also stop the profile change event
        if (!consumed) {
            onProfileClick(v, current)
        }
    }

    internal fun onProfileClick(v: View, current: Boolean) {
        val profile = v.getTag(R.id.material_drawer_profile_header) as IProfile<*>
        switchProfiles(profile)

        //reset the drawer content
        resetDrawerContent(v.context)

        //notify the MiniDrawer about the clicked profile (only if one exists and is hooked to the Drawer
        drawer?.let {
            it.drawerBuilder.mMiniDrawer?.onProfileClick()
        }


        //notify about the changed profile
        val consumed = onAccountHeaderListener?.onProfileChanged(v, profile, current) ?: false
        if (!consumed) {
            if (onProfileClickDrawerCloseDelay > 0) {
                Handler().postDelayed({
                    drawer?.closeDrawer()
                }, onProfileClickDrawerCloseDelay.toLong())
            } else {
                drawer?.closeDrawer()
            }
        }
    }

    /**
     * helper method to toggle the collection
     *
     * @param ctx
     */
    internal fun toggleSelectionList(ctx: Context) {
        drawer?.let { mDrawer ->
            //if we already show the list. reset everything instead
            selectionListShown = if (mDrawer.switchedDrawerContent()) {
                resetDrawerContent(ctx)
                false
            } else {
                //build and set the drawer selection list
                buildDrawerSelectionList()

                // update the arrow image within the drawer
                accountSwitcherArrow.clearAnimation()
                ViewCompat.animate(accountSwitcherArrow).rotation(180f).start()
                //accountSwitcherArrow.setImageDrawable(new IconicsDrawable(ctx, MaterialDrawerFont.Icon.mdf_arrow_drop_up).sizeRes(R.dimen.material_drawer_account_header_dropdown).paddingRes(R.dimen.material_drawer_account_header_dropdown_padding).color(ColorHolder.color(textColor, ctx, R.attr.material_drawer_header_selection_text, R.color.material_drawer_header_selection_text)));
                true
            }
        }
    }

    /**
     * helper method to build and set the drawer selection list
     */
    internal fun buildDrawerSelectionList() {
        var selectedPosition = -1
        var position = 0
        val profileDrawerItems = ArrayList<IDrawerItem<*>>()
        profiles?.let { mProfiles ->
            for (profile in mProfiles) {
                if (profile === currentProfile) {
                    if (currentHiddenInList) {
                        continue
                    } else {
                        selectedPosition = drawer?.drawerBuilder?.itemAdapter?.getGlobalPosition(position)
                                ?: 0
                    }
                }
                if (profile is IDrawerItem<*>) {
                    (profile as IDrawerItem<*>).isSelected = false
                    profileDrawerItems.add(profile as IDrawerItem<*>)
                }
                position += 1
            }
        }
        drawer?.switchDrawerContent(onDrawerItemClickListener, onDrawerItemLongClickListener, profileDrawerItems, selectedPosition)
    }

    /**
     * helper method to reset the drawer content
     */
    private fun resetDrawerContent(ctx: Context) {
        drawer?.resetDrawerContent()

        accountSwitcherArrow.clearAnimation()
        ViewCompat.animate(accountSwitcherArrow).rotation(0f).start()
        //accountSwitcherArrow.setImageDrawable(new IconicsDrawable(ctx, MaterialDrawerFont.Icon.mdf_arrow_drop_down).sizeRes(R.dimen.material_drawer_account_header_dropdown).paddingRes(R.dimen.material_drawer_account_header_dropdown_padding).color(ColorHolder.color(textColor, ctx, R.attr.material_drawer_header_selection_text, R.color.material_drawer_header_selection_text)));
    }

    /**
     * small helper class to update the header and the list
     */
    internal fun updateHeaderAndList() {
        //recalculate the profiles
        calculateProfiles()
        //update the profiles in the header
        buildProfiles()
        //if we currently show the list add the new item directly to it
        if (selectionListShown) {
            buildDrawerSelectionList()
        }
    }
}
