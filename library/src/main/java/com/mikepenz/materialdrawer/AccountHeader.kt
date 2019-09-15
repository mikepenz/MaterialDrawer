package com.mikepenz.materialdrawer

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import com.mikepenz.materialdrawer.widget.MaterialDrawerSliderView
import java.util.*

/**
 * Created by mikepenz on 27.02.15.
 */
class AccountHeader(val accountHeaderBuilder: AccountHeaderBuilder) {

    /**
     * Get the Root view for the Header
     *
     * @return
     */
    val view: View
        get() = accountHeaderBuilder.accountHeaderContainer

    /**
     * Returns the header background view so the dev can set everything on it
     *
     * @return
     */
    val headerBackgroundView: ImageView
        get() = accountHeaderBuilder.accountHeaderBackground

    /**
     * returns if the selection list is currently shown
     *
     * @return
     */
    val isSelectionListShown: Boolean
        get() = accountHeaderBuilder.selectionListShown

    /**
     * returns the current list of profiles set for this header
     *
     * @return
     */
    /**
     * Set a new list of profiles for the header
     *
     * @param profiles
     */
    var profiles: MutableList<IProfile<*>>?
        get() = accountHeaderBuilder.profiles
        set(profiles) {
            accountHeaderBuilder.profiles = profiles
            accountHeaderBuilder.updateHeaderAndList()
        }

    /**
     * get the current active profile
     *
     * @return
     */
    /**
     * Selects the given profile and sets it to the new active profile
     *
     * @param profile
     */
    var activeProfile: IProfile<*>?
        get() = accountHeaderBuilder.currentProfile
        set(profile) {
            profile?.also { setActiveProfile(it, false) }
        }

    /**
     * Set the drawer for the AccountHeader so we can use it for the select
     *
     * @param drawer
     */
    fun setSliderView(sliderView: MaterialDrawerSliderView) {
        accountHeaderBuilder.sliderView = sliderView
    }

    /**
     * set the background for the header via the ImageHolder class
     *
     * @param imageHolder
     */
    fun setHeaderBackground(imageHolder: ImageHolder) {
        imageHolder.applyTo(accountHeaderBuilder.accountHeaderBackground)
    }

    /**
     * Set the background for the Header
     *
     * @param headerBackground
     */
    fun setBackground(headerBackground: Drawable) {
        accountHeaderBuilder.accountHeaderBackground.setImageDrawable(headerBackground)
    }

    /**
     * Set the background for the Header as resource
     *
     * @param headerBackgroundRes
     */
    fun setBackgroundRes(@DrawableRes headerBackgroundRes: Int) {
        accountHeaderBuilder.accountHeaderBackground.setImageResource(headerBackgroundRes)
    }

    /**
     * Toggle the selection list (show or hide it)
     *
     * @param ctx
     */
    fun toggleSelectionList(ctx: Context) {
        accountHeaderBuilder.toggleSelectionList(ctx)
    }


    /**
     * set this to false if you want to hide the first line of the selection box in the header (first line would be the name)
     *
     * @param selectionFirstLineShown
     */
    fun setSelectionFirstLineShown(selectionFirstLineShown: Boolean) {
        accountHeaderBuilder.selectionFirstLineShown = selectionFirstLineShown
        accountHeaderBuilder.updateHeaderAndList()
    }

    /**
     * set this to false if you want to hide the second line of the selection box in the header (second line would be the e-mail)
     *
     * @param selectionSecondLineShown
     */
    fun setSelectionSecondLineShown(selectionSecondLineShown: Boolean) {
        accountHeaderBuilder.selectionSecondLineShown = selectionSecondLineShown
        accountHeaderBuilder.updateHeaderAndList()
    }

    /**
     * set this to define the first line in the selection area if there is no profile
     * note this will block any values from profiles!
     *
     * @param selectionFirstLine
     */
    fun setSelectionFirstLine(selectionFirstLine: String) {
        accountHeaderBuilder.selectionFirstLine = selectionFirstLine
        accountHeaderBuilder.updateHeaderAndList()
    }

    /**
     * set this to define the second line in the selection area if there is no profile
     * note this will block any values from profiles!
     *
     * @param selectionSecondLine
     */
    fun setSelectionSecondLine(selectionSecondLine: String) {
        accountHeaderBuilder.selectionSecondLine = selectionSecondLine
        accountHeaderBuilder.updateHeaderAndList()
    }

    /**
     * Selects the given profile and sets it to the new active profile
     *
     * @param profile
     */
    fun setActiveProfile(profile: IProfile<*>, fireOnProfileChanged: Boolean) {
        val isCurrentSelectedProfile = accountHeaderBuilder.switchProfiles(profile)
        //if the selectionList is shown we should also update the current selected profile in the list
        if (accountHeaderBuilder.sliderView != null && isSelectionListShown) {
            accountHeaderBuilder.sliderView?.setSelection(profile.identifier, false)
        }
        //fire the event if enabled and a listener is set
        if (fireOnProfileChanged && accountHeaderBuilder.onAccountHeaderListener != null) {
            accountHeaderBuilder.onAccountHeaderListener?.onProfileChanged(null, profile, isCurrentSelectedProfile)
        }
    }

    /**
     * Selects a profile by its identifier
     *
     * @param identifier
     */
    @JvmOverloads
    fun setActiveProfile(identifier: Long, fireOnProfileChanged: Boolean = false) {
        accountHeaderBuilder.profiles?.forEach { profile ->
            if (profile.identifier == identifier) {
                setActiveProfile(profile, fireOnProfileChanged)
                return
            }
        }
    }


    /**
     * Helper method to update a profile using it's identifier
     *
     * @param newProfile
     */
    fun updateProfile(newProfile: IProfile<*>) {
        updateProfileByIdentifier(newProfile)
    }

    /**
     * Helper method to update a profile using it's identifier
     *
     * @param newProfile
     */
    @Deprecated("")
    fun updateProfileByIdentifier(newProfile: IProfile<*>) {
        val found = getPositionByIdentifier(newProfile.identifier)
        if (found > -1) {
            accountHeaderBuilder.profiles?.set(found, newProfile)
            accountHeaderBuilder.updateHeaderAndList()
        }
    }


    /**
     * Add new profiles to the existing list of profiles
     *
     * @param profiles
     */
    fun addProfiles(vararg profiles: IProfile<*>) {
        if (accountHeaderBuilder.profiles == null) {
            accountHeaderBuilder.profiles = ArrayList()
        }

        Collections.addAll(accountHeaderBuilder.profiles, *profiles)

        accountHeaderBuilder.updateHeaderAndList()
    }

    /**
     * Add a new profile at a specific position to the list
     *
     * @param profile
     * @param position
     */
    fun addProfile(profile: IProfile<*>, position: Int) {
        if (accountHeaderBuilder.profiles == null) {
            accountHeaderBuilder.profiles = ArrayList()
        }

        accountHeaderBuilder.profiles?.add(position, profile)
        accountHeaderBuilder.updateHeaderAndList()
    }

    /**
     * remove a profile from the given position
     *
     * @param position
     */
    fun removeProfile(position: Int) {
        if (accountHeaderBuilder.profiles != null && accountHeaderBuilder.profiles?.size ?: 0 > position) {
            accountHeaderBuilder.profiles?.removeAt(position)
        }

        accountHeaderBuilder.updateHeaderAndList()
    }

    /**
     * remove the profile with the given identifier
     *
     * @param identifier
     */
    fun removeProfileByIdentifier(identifier: Long) {
        val found = getPositionByIdentifier(identifier)
        if (found > -1) {
            accountHeaderBuilder.profiles?.removeAt(found)
        }

        accountHeaderBuilder.updateHeaderAndList()
    }

    /**
     * try to remove the given profile
     *
     * @param profile
     */
    fun removeProfile(profile: IProfile<*>) {
        removeProfileByIdentifier(profile.identifier)
    }

    /**
     * Clear the header
     */
    fun clear() {
        accountHeaderBuilder.profiles = null

        //calculate the profiles to set
        accountHeaderBuilder.calculateProfiles()

        //process and build the profiles
        accountHeaderBuilder.buildProfiles()
    }

    /**
     * gets the position of a profile by it's identifier
     *
     * @param identifier
     * @return
     */
    private fun getPositionByIdentifier(identifier: Long): Int {
        if (identifier != -1L) {
            accountHeaderBuilder.profiles?.forEachIndexed { index, iProfile ->
                if (iProfile.identifier == identifier) {
                    return index
                }
            }
        }
        return -1
    }

    /**
     * add the values to the bundle for saveInstanceState
     *
     * @param savedInstanceState
     * @return
     */
    fun saveInstanceState(savedInstanceState: Bundle): Bundle {
        savedInstanceState.putInt(BUNDLE_SELECTION_HEADER, accountHeaderBuilder.currentSelection)
        return savedInstanceState
    }


    interface OnAccountHeaderListener {
        /**
         * the event when the profile changes
         *
         * @param view
         * @param profile
         * @return if the event was consumed
         */
        fun onProfileChanged(view: View?, profile: IProfile<*>, current: Boolean): Boolean
    }

    interface OnAccountHeaderItemLongClickListener {
        /**
         * the event when the profile item is longClicked inside the list
         *
         * @param view
         * @param profile
         * @param current
         * @return if the event was consumed
         */
        fun onProfileLongClick(view: View, profile: IProfile<*>, current: Boolean): Boolean
    }

    interface OnAccountHeaderProfileImageListener {
        /**
         * the event when the profile image is clicked
         *
         * @param view
         * @param profile
         * @return if the event was consumed
         */
        fun onProfileImageClick(view: View, profile: IProfile<*>, current: Boolean): Boolean

        /**
         * the event when the profile image is long clicked
         *
         * @param view
         * @param profile
         * @return if the event was consumed
         */
        fun onProfileImageLongClick(view: View, profile: IProfile<*>, current: Boolean): Boolean
    }

    interface OnAccountHeaderSelectionViewClickListener {
        /**
         * the event when the user clicks the selection list under the profile icons
         *
         * @param view
         * @param profile
         * @return if the event was consumed
         */
        fun onClick(view: View, profile: IProfile<*>): Boolean
    }

    companion object {
        const val NAVIGATION_DRAWER_ACCOUNT_ASPECT_RATIO = 9.0 / 16.0

        const val BUNDLE_SELECTION_HEADER = "bundle_selection_header"
    }
}