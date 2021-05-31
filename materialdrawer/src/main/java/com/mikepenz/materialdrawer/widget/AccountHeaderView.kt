package com.mikepenz.materialdrawer.widget

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.core.view.ViewCompat
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.holder.DimenHolder
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.interfaces.ColorfulBadgeable
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import com.mikepenz.materialdrawer.util.*
import com.mikepenz.materialdrawer.view.BezelImageView
import java.util.*

/**
 * This view offers support to add an account switcher to the [MaterialDrawerSliderView]
 * It will hook onto the [MaterialDrawerSliderView] and coordinate updating and showing the proper set of elements
 */
open class AccountHeaderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, compact: Boolean? = null) :
    ConstraintLayout(context, attrs, defStyleAttr) {
    var savedInstanceKey: String = ""

    // global references to views we need later
    val accountHeader: View
    val statusBarGuideline: Guideline
    val accountHeaderBackground: ImageView
    val currentProfileView: BezelImageView
    val currentProfileBadgeView: TextView
    val accountSwitcherArrow: ImageView
    val currentProfileName: TextView
    val currentProfileEmail: TextView
    val profileFirstView: BezelImageView
    val profileFirstBadgeView: TextView
    val profileSecondView: BezelImageView
    val profileSecondBadgeView: TextView
    val profileThirdView: BezelImageView
    val profileThirdBadgeView: TextView

    /** Temporarily disable invalidation for optimizations */
    private var invalidationEnabled: Boolean = true
    private var invalidateHeader: Boolean = false
    private var invalidateList: Boolean = false

    /**
     * Selects the given profile and sets it to the new active profile
     *
     * @param profile
     */
    var activeProfile: IProfile?
        get() = currentProfile
        set(profile) {
            profile?.also { setActiveProfile(it, false) }
        }

    // global references to the profiles
    internal var currentProfile: IProfile? = null
    internal var profileFirst: IProfile? = null
    internal var profileSecond: IProfile? = null
    internal var profileThird: IProfile? = null

    // global stuff
    internal var _selectionListShown = false
    var selectionListShown: Boolean
        get() = _selectionListShown
        set(value) {
            if (value != _selectionListShown) {
                toggleSelectionList()
            }
        }
    var accountHeaderTextSectionBackgroundResource = -1
        set(value) {
            field = value
            buildProfiles()
        }

    // defines if we use the compactStyle
    internal var compactStyle = false

    // the typeface used for textViews within the AccountHeader
    var typeface: Typeface? = null
        set(value) {
            field = value
            reconstructHeader()
        }

    // the typeface used for name textView only. overrides typeface
    var nameTypeface: Typeface? = null
        set(value) {
            field = value
            reconstructHeader()
        }

    // the typeface used for email textView only. overrides typeface
    var emailTypeface: Typeface? = null
        set(value) {
            field = value
            reconstructHeader()
        }

    // set the account header height
    var height: DimenHolder? = null
        set(value) {
            field = value
            reconstructHeader()
        }

    //the current selected profile is visible in the list
    var currentHiddenInList = false

    //set to hide the first or second line
    var selectionFirstLineShown = true
        set(value) {
            field = value
            updateHeaderAndList()
        }
    var selectionSecondLineShown = true
        set(value) {
            field = value
            updateHeaderAndList()
        }

    //set one of these to define the text in the first or second line with in the account selector
    var selectionFirstLine: String? = null
        set(value) {
            field = value
            updateHeaderAndList()
        }

    var selectionSecondLine: String? = null
        set(value) {
            field = value
            updateHeaderAndList()
        }

    // set no divider below the header
    var paddingBelowHeader = true
        set(value) {
            field = value
            sliderView?.headerPadding = paddingBelowHeader
        }

    // set no divider below the header
    var dividerBelowHeader = true
        set(value) {
            field = value
            sliderView?.headerDivider = dividerBelowHeader
        }

    //the background for the header
    var headerBackground: ImageHolder? = null
        set(value) {
            value?.applyTo(accountHeaderBackground, DrawerImageLoader.Tags.ACCOUNT_HEADER.name)
            field = value
        }

    //background scale type
    var headerBackgroundScaleType: ImageView.ScaleType?
        get() = accountHeaderBackground.scaleType
        set(value) {
            if (value != null) {
                accountHeaderBackground.scaleType = value
            }
        }

    //profile images in the header are shown or not
    var profileImagesVisible = true
        set(value) {
            field = value
            buildProfiles()
        }

    //only the main profile image is visible
    var onlyMainProfileImageVisible = false
        set(value) {
            field = value
            buildProfiles()
        }

    //show small profile images but hide MainProfileImage
    var onlySmallProfileImagesVisible = false
        set(value) {
            field = value
            buildProfiles()
        }

    //close the drawer after a profile was clicked in the list
    var closeDrawerOnProfileListClick: Boolean? = null

    //reset the drawer list to the main drawer list after the profile was clicked in the list
    var resetDrawerOnProfileListClick = true

    // set the profile images clickable or not
    var profileImagesClickable = true
        set(value) {
            field = value
            buildProfiles()
        }

    // set to use the alternative profile header switching
    var alternativeProfileHeaderSwitching = false

    // enable 3 small header previews
    var threeSmallProfileImages = false
        set(value) {
            field = value
            buildProfiles()
        }

    // enable to show badges on current profile images
    var displayBadgesOnCurrentProfileImage = true
        set(value) {
            field = value
            buildProfiles()
        }

    // enable to show badges on small profile images
    var displayBadgesOnSmallProfileImages = false
        set(value) {
            field = value
            buildProfiles()
        }

    //the delay which is waited before the drawer is closed
    var onProfileClickDrawerCloseDelay = 100

    // the onAccountHeaderProfileImageListener to set
    var onAccountHeaderProfileImageListener: ((view: View, profile: IProfile, current: Boolean) -> Boolean)? = null

    // the onAccountHeaderSelectionListener to set
    var onAccountHeaderSelectionViewClickListener: ((view: View, profile: IProfile) -> Boolean)? = null

    //set the selection list enabled if there is only a single profile
    var selectionListEnabledForSingleProfile = true
        set(value) {
            field = value
            buildProfiles()
        }

    //set the selection enabled disabled
    var selectionListEnabled = true
        set(value) {
            field = value
            buildProfiles()
        }

    // the profiles to display
    var profiles: MutableList<IProfile>? = null
        set(value) {
            field = value
            value?.mapNotNull { it as? IDrawerItem<*> }?.forEach { item ->
                sliderView?.idDistributor?.checkId(item)
            }
            updateHeaderAndList()
        }

    // the click listener to be fired on profile or selection click
    var onAccountHeaderListener: ((view: View?, profile: IProfile, current: Boolean) -> Boolean)? = null

    //the on long click listener to be fired on profile longClick inside the list
    var onAccountHeaderItemLongClickListener: ((view: View?, profile: IProfile, current: Boolean) -> Boolean)? = null

    // the drawer to set the AccountSwitcher for
    var sliderView: MaterialDrawerSliderView? = null
        set(value) {
            field = value
            if (field?.accountHeader != this) {
                field?.accountHeader = this
            }
        }

    // miniDrawer
    val miniDrawer: MiniDrawerSliderView?
        get() = sliderView?.miniDrawer

    /**
     * onProfileClickListener to notify onClick on the current profile image
     */
    private val onCurrentProfileClickListener = OnClickListener { v -> onProfileImageClick(v, true) }

    /**
     * onProfileClickListener to notify onClick on a profile image
     */
    private val onProfileClickListener = OnClickListener { v -> onProfileImageClick(v, false) }

    /**
     * onProfileLongClickListener to call the onProfileImageLongClick on the current profile image
     */
    private val onCurrentProfileLongClickListener = OnLongClickListener { v ->
        if (onAccountHeaderProfileImageListener != null) {
            val profile = v.getTag(R.id.material_drawer_profile_header) as IProfile
            return@OnLongClickListener onAccountHeaderProfileImageListener?.invoke(v, profile, true)
                ?: false
        }
        false
    }

    /**
     * onProfileLongClickListener to call the onProfileImageLongClick on a profile image
     */
    private val onProfileLongClickListener = OnLongClickListener { v ->
        if (onAccountHeaderProfileImageListener != null) {
            val profile = v.getTag(R.id.material_drawer_profile_header) as IProfile
            return@OnLongClickListener onAccountHeaderProfileImageListener?.invoke(v, profile, false)
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
     * onDrawerItemClickListener to catch the selection for the new profile!
     */
    private val onDrawerItemClickListener: ((View?, IDrawerItem<*>, Int) -> Boolean) = { view: View?, drawerItem: IDrawerItem<*>, position: Int ->
        val isCurrentSelectedProfile: Boolean = if (drawerItem is IProfile && drawerItem.isSelectable) {
            switchProfiles(drawerItem as IProfile)
        } else {
            false
        }

        if (resetDrawerOnProfileListClick) {
            sliderView?.onDrawerItemClickListener = null
        }

        //wrap the onSelection call and the reset stuff within a handler to prevent lag
        if (resetDrawerOnProfileListClick && sliderView != null) {
            resetDrawerContent()
        }

        //notify the MiniDrawer about the clicked profile (only if one exists and is hooked to the Drawer
        miniDrawer?.onProfileClick()

        var consumed = false
        if (drawerItem is IProfile) {
            consumed = onAccountHeaderListener?.invoke(view, drawerItem as IProfile, isCurrentSelectedProfile) ?: false
        }

        //if a custom behavior was chosen via the CloseDrawerOnProfileListClick then use this. else react on the result of the onProfileChanged listener
        closeDrawerOnProfileListClick?.let {
            consumed = consumed && (!it)
        }

        //totally custom handling of the drawer behavior as otherwise the selection of the profile list is set to the Drawer
        if (!consumed) {
            //close the drawer after click
            sliderView?.closeDrawerDelayed()
        }

        //consume the event to prevent setting the clicked item as selected in the already switched item list
        true
    }

    /**
     * onDrawerItemLongClickListener to catch the longClick for a profile
     */
    private val onDrawerItemLongClickListener: ((View?, IDrawerItem<*>, Int) -> Boolean) = { view: View?, drawerItem: IDrawerItem<*>, position: Int ->
        //if a longClickListener was defined use it
        if (onAccountHeaderItemLongClickListener != null) {
            val isCurrentSelectedProfile: Boolean = drawerItem.isSelected

            if (drawerItem is IProfile) {
                onAccountHeaderItemLongClickListener?.invoke(view, drawerItem as IProfile, isCurrentSelectedProfile) ?: false
            } else {
                false
            }
        } else {
            false
        }
    }

    init {
        val headerLayout = context.resolveStyledHeaderValue {
            compactStyle = compact ?: it.getBoolean(R.styleable.AccountHeaderView_materialDrawerCompactStyle, false)
            it.getResourceId(
                R.styleable.AccountHeaderView_materialDrawerHeaderLayout,
                if (compactStyle) R.layout.material_drawer_compact_header else R.layout.material_drawer_header
            )
        }

        // the account header
        accountHeader = LayoutInflater.from(context).inflate(headerLayout, this, true)

        // get the header view within the container
        statusBarGuideline = findViewById(R.id.material_drawer_statusbar_guideline)

        // get the background view
        accountHeaderBackground = findViewById(R.id.material_drawer_account_header_background)

        // set the arrow :D
        accountSwitcherArrow = findViewById(R.id.material_drawer_account_header_text_switcher)

        //get the fields for the name
        currentProfileView = findViewById(R.id.material_drawer_account_header_current)
        currentProfileBadgeView = findViewById(R.id.material_drawer_account_header_current_badge)
        currentProfileName = findViewById(R.id.material_drawer_account_header_name)
        currentProfileEmail = findViewById(R.id.material_drawer_account_header_email)

        profileFirstView = findViewById(R.id.material_drawer_account_header_small_first)
        profileFirstBadgeView = findViewById(R.id.material_drawer_account_header_small_first_badge)
        profileSecondView = findViewById(R.id.material_drawer_account_header_small_second)
        profileSecondBadgeView = findViewById(R.id.material_drawer_account_header_small_second_badge)
        profileThirdView = findViewById(R.id.material_drawer_account_header_small_third)
        profileThirdBadgeView = findViewById(R.id.material_drawer_account_header_small_third_badge)

        reconstructHeader()

        //the default min header height by default 148dp
        val defaultHeaderMinHeight = context.resources.getDimensionPixelSize(R.dimen.material_drawer_account_header_height)

        // set the insets
        ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
            // handle everything if we have a translucent status bar which only is possible on API >= 19
            val topInset = insets.systemWindowInsetTop ?: 0
            statusBarGuideline.setGuidelineBegin(topInset)

            val height = resolveHeight()
            var newHeight = height
            //in fact it makes no difference if we have a translucent statusBar or not. we want 9/16 just if we are not compact
            if (compactStyle) {
                newHeight += topInset
            } else if (newHeight - topInset <= defaultHeaderMinHeight) {
                //if the height + statusBar of the header is lower than the required 148dp + statusBar we change the height to be able to display all the data
                newHeight = defaultHeaderMinHeight + topInset
            }

            //set the height for the header
            setHeaderHeight(newHeight)

            insets
        }
    }

    private fun resolveHeight(): Int {
        // handle the height for the header
        var height = 0
        this.height?.let {
            height = it.asPixel(context)
        } ?: run {
            height = if (compactStyle) {
                context.resources.getDimensionPixelSize(R.dimen.material_drawer_account_header_height_compact)
            } else {
                //calculate the header height by getting the optimal drawer width and calculating it * 9 / 16
                (getOptimalDrawerWidth(context) * NAVIGATION_DRAWER_ACCOUNT_ASPECT_RATIO).toInt()
            }
        }
        return height
    }

    private fun reconstructHeader() {
        if (!invalidationEnabled) {
            invalidateHeader = true
            return
        }
        invalidateHeader = false

        //set the height for the header
        setHeaderHeight(resolveHeight())

        // set the background
        headerBackground?.applyTo(accountHeaderBackground, DrawerImageLoader.Tags.ACCOUNT_HEADER.name)

        // get the text color to use for the text section
        val textColor =
            context.getHeaderSelectionTextColor() // textColor.applyColor(context, R.attr.materialDrawerHeaderSelectionText, R.color.material_drawer_header_selection_text)
        val subTextColor =
            context.getHeaderSelectionSubTextColor()  // this.textColor.applyColor(context, R.attr.materialDrawerHeaderSelectionSubtext, R.color.material_drawer_header_selection_subtext)

        if (accountHeaderTextSectionBackgroundResource == -1) {
            accountHeaderTextSectionBackgroundResource = context.getSelectableBackgroundRes()
        }
        handleSelectionView(currentProfile, true)

        // set the arrow
        val drawable = AppCompatResources.getDrawable(context, R.drawable.material_drawer_ico_menu_down)
        if (drawable != null) {
            val size = context.resources.getDimensionPixelSize(R.dimen.material_drawer_account_header_dropdown)
            accountSwitcherArrow.setImageDrawable(FixStateListDrawable(drawable, subTextColor).apply {
                setBounds(0, 0, size, size)
            })
        }

        //IconicsDrawable(context, MaterialDrawerFont.Icon.mdf_arrow_drop_down).size(IconicsSize.res(R.dimen.material_drawer_account_header_dropdown)).padding(IconicsSize.res(R.dimen.material_drawer_account_header_dropdown_padding)).color(IconicsColor.colorList(subTextColor))
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

        //calculate the profiles to set
        calculateProfiles()

        //process and build the profiles
        buildProfiles()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            requestApplyInsets()
        }
    }

    /**
     * onSelectionClickListener to notify the onClick on the checkbox
     */
    private val onSelectionClickListener = OnClickListener { v ->
        val consumed = onAccountHeaderSelectionViewClickListener?.invoke(v, v.getTag(R.id.material_drawer_profile_header) as IProfile)
            ?: false
        if (accountSwitcherArrow.visibility == View.VISIBLE && !consumed) {
            toggleSelectionList()
        }
    }

    /**
     * Add a new profile at a specific position to the list
     *
     * @param profile
     * @param position
     */
    fun addProfile(profile: IProfile, position: Int) {
        if (profiles == null) {
            profiles = ArrayList()
        }

        profiles?.add(position, profile)
        updateHeaderAndList()
    }

    /**
     * add single ore more DrawerItems to the Drawer
     */
    fun addProfiles(vararg profiles: IProfile) {
        if (this.profiles == null) {
            this.profiles = ArrayList()
        }

        this.profiles?.let {
            it.mapNotNull { di -> di as? IDrawerItem<*> }.forEach { item ->
                sliderView?.idDistributor?.checkId(item)
            }
            Collections.addAll<IProfile>(it, *profiles)
        }
        updateHeaderAndList()
    }

    /**
     * remove a profile from the given position
     */
    fun removeProfile(position: Int) {
        if (this.profiles != null && this.profiles?.size ?: 0 > position) {
            this.profiles?.removeAt(position)
        }
        updateHeaderAndList()
    }

    /**
     * remove the profile with the given identifier
     */
    fun removeProfileByIdentifier(identifier: Long) {
        val found = getPositionByIdentifier(identifier)
        if (found > -1) {
            this.profiles?.removeAt(found)
        }
        this.updateHeaderAndList()
    }

    /**
     * try to remove the given profile
     */
    fun removeProfile(profile: IProfile) {
        removeProfileByIdentifier(profile.identifier)
    }

    /**
     * Clear the header
     */
    fun clear() {
        this.profiles = null
        //calculate the profiles to set
        calculateProfiles()
        //process and build the profiles
        buildProfiles()
    }

    /**
     * @param drawer
     * @return
     */
    fun attachToSliderView(sliderView: MaterialDrawerSliderView) {
        this.sliderView = sliderView

        //set the top padding to 0 as this would happen when the AccountHeader is created during Drawer build time
        sliderView.recyclerView.setPadding(sliderView.recyclerView.paddingLeft, 0, sliderView.recyclerView.paddingRight, sliderView.recyclerView.paddingBottom)

        //everything created. now set the header
        sliderView.headerView = this

        this.sliderView?.accountHeader = this
    }

    /**
     * create the drawer with the values of a savedInstance
     *
     * @param savedInstance
     * @return
     */
    fun withSavedInstance(savedInstance: Bundle?) {
        savedInstance ?: return
        // try to restore all saved values again
        val selection = savedInstance.getInt(BUNDLE_SELECTION_HEADER + savedInstanceKey, -1)
        if (selection != -1) {
            //predefine selection (should be the first element
            profiles?.let {
                if (selection > -1 && selection < it.size) {
                    switchProfiles(it[selection])
                }
            }
        }
    }

    /**
     * helper method to set the height for the header!
     *
     * @param height
     */
    private fun setHeaderHeight(height: Int) {
        this.layoutParams?.let {
            it.height = height
            this.layoutParams = it
        }

        val lp = accountHeader.layoutParams
        if (lp != null) {
            lp.height = height
            accountHeader.layoutParams = lp
        }

        val p = accountHeaderBackground.layoutParams
        p.height = height
        accountHeaderBackground.layoutParams = p
    }

    /**
     * a small helper to handle the selectionView
     *
     * @param on
     */
    private fun handleSelectionView(profile: IProfile?, on: Boolean) {
        if (on) {
            if (Build.VERSION.SDK_INT >= 23) {
                this.foreground = AppCompatResources.getDrawable(this.context, accountHeaderTextSectionBackgroundResource)
            } else {
                // todo foreground thing?
            }
            this.setOnClickListener(onSelectionClickListener)
            this.setTag(R.id.material_drawer_profile_header, profile)
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                this.foreground = null
            } else {
                // TODO foreground reset
            }
            this.setOnClickListener(null)
        }
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

            val newActiveProfiles = arrayOfNulls<IProfile>(4)
            val unusedProfiles = Stack<IProfile>()

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

            val activeProfiles = Stack<IProfile>()
            // try to fill the gaps with new available profiles
            for (i in 0..3) {
                if (newActiveProfiles[i] != null) {
                    activeProfiles.push(newActiveProfiles[i])
                } else if (!unusedProfiles.isEmpty()) {
                    activeProfiles.push(unusedProfiles.pop())
                }
            }

            val reversedActiveProfiles = Stack<IProfile>()
            while (!activeProfiles.empty()) {
                reversedActiveProfiles.push(activeProfiles.pop())
            }

            // reassign active profiles
            currentProfile = if (reversedActiveProfiles.isEmpty()) {
                null
            } else {
                reversedActiveProfiles.pop()
            }
            profileFirst = if (reversedActiveProfiles.isEmpty()) {
                null
            } else {
                reversedActiveProfiles.pop()
            }
            profileSecond = if (reversedActiveProfiles.isEmpty()) {
                null
            } else {
                reversedActiveProfiles.pop()
            }
            profileThird = if (reversedActiveProfiles.isEmpty()) {
                null
            } else {
                reversedActiveProfiles.pop()
            }
        }
    }

    /**
     * helper method to switch the profiles
     *
     * @param newSelection
     * @return true if the new selection was the current profile
     */
    internal fun switchProfiles(newSelection: IProfile?): Boolean {
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
                val previousActiveProfiles = ArrayList<IProfile>(Arrays.asList<IProfile>(currentProfile, profileFirst, profileSecond, profileThird))

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
        if (!invalidationEnabled) {
            invalidateList = true
            return
        }
        invalidateList = false

        currentProfileView.visibility = View.GONE
        currentProfileBadgeView.visibility = View.GONE
        accountSwitcherArrow.visibility = View.GONE
        profileFirstView.visibility = View.GONE
        profileFirstView.setOnClickListener(null)
        profileFirstBadgeView.visibility = View.GONE
        profileSecondView.visibility = View.GONE
        profileSecondView.setOnClickListener(null)
        profileSecondBadgeView.visibility = View.GONE
        profileThirdView.visibility = View.GONE
        profileThirdView.setOnClickListener(null)
        profileThirdBadgeView.visibility = View.GONE
        currentProfileName.text = ""
        currentProfileEmail.text = ""

        handleSelectionView(currentProfile, true)

        val mCurrentProfile = this.currentProfile
        val mProfiles = this.profiles
        if (mCurrentProfile != null) {
            if ((profileImagesVisible || onlyMainProfileImageVisible) && !onlySmallProfileImagesVisible) {
                currentProfileView.contentDescription = mCurrentProfile.description?.getText(context) ?: mCurrentProfile.name?.getText(context)
                        ?: currentProfileView.context.getString(R.string.material_drawer_profile_content_description)
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

                var badgeVisible = false
                if (displayBadgesOnCurrentProfileImage) {
                    (mCurrentProfile as? ColorfulBadgeable)?.let { badgeable ->
                        badgeVisible = StringHolder.applyToOrHide(badgeable.badge, currentProfileBadgeView)
                        if (badgeVisible) {
                            badgeable.badgeStyle?.style(currentProfileBadgeView, context.getPrimaryDrawerTextColor())
                            typeface?.let { typeface -> currentProfileBadgeView.typeface = typeface }
                        }
                    }
                }
                currentProfileBadgeView.visibility = if (badgeVisible) View.VISIBLE else View.GONE
            } else if (compactStyle) {
                currentProfileView.visibility = View.GONE
                currentProfileBadgeView.visibility = View.GONE
            }

            handleSelectionView(mCurrentProfile, true)
            accountSwitcherArrow.visibility = View.VISIBLE
            currentProfileView.setTag(R.id.material_drawer_profile_header, mCurrentProfile)

            StringHolder.applyTo(mCurrentProfile.name, currentProfileName)
            StringHolder.applyTo(mCurrentProfile.description, currentProfileEmail)

            /**
             * Apply the profile information to the provided imageView
             */
            fun IProfile?.applyProfile(imageView: BezelImageView, badgeView: TextView) {
                this ?: return
                setImageOrPlaceholder(imageView, this.icon)
                imageView.setTag(R.id.material_drawer_profile_header, this)
                imageView.contentDescription = this.description?.getText(context) ?: this.name?.getText(context)
                        ?: imageView.context.getString(R.string.material_drawer_profile_content_description)
                if (profileImagesClickable) {
                    imageView.setOnClickListener(onProfileClickListener)
                    imageView.setOnLongClickListener(onProfileLongClickListener)
                    imageView.disableTouchFeedback(false)
                } else {
                    imageView.disableTouchFeedback(true)
                }
                imageView.visibility = View.VISIBLE
                imageView.invalidate()

                var badgeVisible = false
                if (displayBadgesOnSmallProfileImages) {
                    (this as? ColorfulBadgeable)?.let { badgeable ->
                        badgeVisible = StringHolder.applyToOrHide(badgeable.badge, badgeView)
                        if (badgeVisible) {
                            badgeable.badgeStyle?.style(badgeView, context.getPrimaryDrawerTextColor())
                            typeface?.let { typeface -> badgeView.typeface = typeface }
                        }
                    }
                }
                badgeView.visibility = if (badgeVisible) View.VISIBLE else View.GONE
            }

            if (profileImagesVisible && !onlyMainProfileImageVisible) {
                profileFirst.applyProfile(profileFirstView, profileFirstBadgeView)
                profileSecond.applyProfile(profileSecondView, profileSecondBadgeView)

                if (threeSmallProfileImages) {
                    profileThird.applyProfile(profileThirdView, profileThirdBadgeView)
                }
            }
        } else if (mProfiles != null && mProfiles.size > 0) {
            val profile = mProfiles[0]
            setTag(R.id.material_drawer_profile_header, profile)
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
        val profile = v.getTag(R.id.material_drawer_profile_header) as IProfile

        val consumed = onAccountHeaderProfileImageListener?.invoke(v, profile, current)
            ?: false

        //if the event was already consumed by the click don't continue. note that this will also stop the profile change event
        if (!consumed) {
            onProfileClick(v, current)
        }
    }

    internal fun onProfileClick(v: View, current: Boolean) {
        val profile = v.getTag(R.id.material_drawer_profile_header) as IProfile
        switchProfiles(profile)

        //reset the drawer content
        resetDrawerContent()

        //notify the MiniDrawer about the clicked profile (only if one exists and is hooked to the Drawer
        miniDrawer?.onProfileClick()

        //notify about the changed profile
        val consumed = onAccountHeaderListener?.invoke(v, profile, current) ?: false
        if (!consumed) {
            if (onProfileClickDrawerCloseDelay > 0) {
                Handler().postDelayed({
                    sliderView?.drawerLayout?.closeDrawers()
                }, onProfileClickDrawerCloseDelay.toLong())
            } else {
                sliderView?.drawerLayout?.closeDrawers()
            }
        }
    }

    /**
     * helper method to toggle the collection
     */
    internal fun toggleSelectionList() {
        val sliderView = sliderView ?: return
        //if we already show the list. reset everything instead
        _selectionListShown = if (sliderView.switchedDrawerContent()) {
            resetDrawerContent()
            false
        } else {
            //build and set the drawer selection list
            buildDrawerSelectionList()

            // update the arrow image within the drawer
            accountSwitcherArrow.clearAnimation()
            ViewCompat.animate(accountSwitcherArrow).rotation(180f).start()
            true
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
                        selectedPosition = sliderView?.itemAdapter?.getGlobalPosition(position)
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
        sliderView?.switchDrawerContent(onDrawerItemClickListener, onDrawerItemLongClickListener, profileDrawerItems, selectedPosition)
    }

    /**
     * helper method to reset the drawer content
     */
    private fun resetDrawerContent() {
        sliderView?.resetDrawerContent()

        accountSwitcherArrow.clearAnimation()
        ViewCompat.animate(accountSwitcherArrow).rotation(0f).start()
    }

    /**
     * Updates the header and also rebuids the list.
     * This is called after modifications to the items were made
     */
    fun updateHeaderAndList() {
        if (!invalidationEnabled) {
            invalidateList = true
            return
        }
        invalidateList = false

        //recalculate the profiles
        calculateProfiles()
        //update the profiles in the header
        buildProfiles()
        //if we currently show the list add the new item directly to it
        if (selectionListShown) {
            buildDrawerSelectionList()
        }
    }

    /**
     * add the values to the bundle for saveInstanceState
     *
     * @param savedInstanceState
     * @return
     */
    fun saveInstanceState(savedInstanceState: Bundle): Bundle {
        savedInstanceState.putInt(BUNDLE_SELECTION_HEADER + savedInstanceKey, currentSelection)
        return savedInstanceState
    }

    /**
     * Selects the given profile and sets it to the new active profile
     *
     * @param profile
     */
    fun setActiveProfile(profile: IProfile, fireOnProfileChanged: Boolean) {
        val isCurrentSelectedProfile = switchProfiles(profile)
        //if the selectionList is shown we should also update the current selected profile in the list
        if (sliderView != null && selectionListShown) {
            sliderView?.setSelection(profile.identifier, false)
        }
        //fire the event if enabled and a listener is set
        if (fireOnProfileChanged && onAccountHeaderListener != null) {
            onAccountHeaderListener?.invoke(null, profile, isCurrentSelectedProfile)
        }
    }

    /**
     * Selects a profile by its identifier
     *
     * @param identifier
     */
    @JvmOverloads
    fun setActiveProfile(identifier: Long, fireOnProfileChanged: Boolean = false) {
        profiles?.forEach { profile ->
            if (profile.identifier == identifier) {
                setActiveProfile(profile, fireOnProfileChanged)
                return
            }
        }
    }

    /**
     * Helper method to update a profile using its identifier
     *
     * @param newProfile
     */
    fun updateProfile(newProfile: IProfile) {
        val found = getPositionByIdentifier(newProfile.identifier)
        if (found > -1) {
            profiles?.set(found, newProfile)
            updateHeaderAndList()
        }
    }

    /**
     * gets the position of a profile by its identifier
     *
     * @param identifier
     * @return
     */
    private fun getPositionByIdentifier(identifier: Long): Int {
        if (identifier != -1L) {
            profiles?.forEachIndexed { index, iProfile ->
                if (iProfile.identifier == identifier) {
                    return index
                }
            }
        }
        return -1
    }

    /** Applies properties in an optimized form. Will disable invalidation of the AccountHeaderView for the inner property set operations */
    fun apply(block: AccountHeaderView.() -> Unit): AccountHeaderView {
        invalidationEnabled = false
        block()
        invalidationEnabled = true
        if (invalidateList) {
            updateHeaderAndList()
        }
        if (invalidateHeader) {
            reconstructHeader()
        }
        return this
    }

    companion object {
        const val NAVIGATION_DRAWER_ACCOUNT_ASPECT_RATIO = 9.0 / 16.0

        const val BUNDLE_SELECTION_HEADER = "bundle_selection_header"
    }
}