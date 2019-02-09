package com.mikepenz.materialdrawer.model.interfaces

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.annotation.DrawableRes
import com.mikepenz.fastadapter.IIdentifyable
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.holder.StringHolder

/**
 * Created by mikepenz on 03.02.15.
 */
interface IProfile<T> : IIdentifyable {

    val name: StringHolder?

    val email: StringHolder?

    val icon: ImageHolder?

    var isSelectable: Boolean

    fun withName(name: CharSequence?): T

    fun withEmail(email: String?): T

    fun withIcon(icon: Drawable?): T

    fun withIcon(bitmap: Bitmap): T

    fun withIcon(@DrawableRes iconRes: Int): T

    fun withIcon(url: String): T

    fun withIcon(uri: Uri): T

    fun withIcon(icon: IIcon): T
}
