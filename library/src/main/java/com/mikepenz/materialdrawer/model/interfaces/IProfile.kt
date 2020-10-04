package com.mikepenz.materialdrawer.model.interfaces

import androidx.annotation.StringRes
import com.mikepenz.fastadapter.IIdentifyable
import com.mikepenz.materialdrawer.holder.StringHolder

/**
 * Defines a general [IProfile] to be displayed in the [com.mikepenz.materialdrawer.widget.MaterialDrawerSliderView] with the [com.mikepenz.materialdrawer.widget.AccountHeaderView]
 */
interface IProfile : IIdentifyable, Nameable, Iconable, Selectable, Tagable, Describable

@Deprecated("Please consider to replace with the actual property setter")
fun <T : IProfile> T.withEmail(@StringRes emailRes: Int): T {
    this.description = StringHolder(emailRes)
    return this
}

@Deprecated("Please consider to replace with the actual property setter")
fun <T : IProfile> T.withEmail(email: String?): T {
    this.description = StringHolder(email)
    return this
}
