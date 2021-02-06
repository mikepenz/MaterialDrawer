package com.mikepenz.materialdrawer.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.mikepenz.materialdrawer.util.DrawerImageLoader.IDrawerImageLoader

/**
 * The general management class for the [IDrawerImageLoader] support, to offer support for any image loading library.
 */
open class DrawerImageLoader private constructor(var imageLoader: IDrawerImageLoader?) {

    /**
     * defines if we accept any protocol
     */
    var handleAllProtocols = false

    /**
     * supported protocols
     */
    var handledProtocols = listOf<String?>("http", "https")

    /**
     * The possible tags we currently support.
     */
    enum class Tags {
        PRIMARY_ITEM,
        MINI_ITEM,
        PROFILE,
        PROFILE_DRAWER_ITEM,
        ACCOUNT_HEADER
    }

    /**
     * @param imageView
     * @param uri
     * @param tag
     * @return false if not consumed
     */
    open fun setImage(imageView: ImageView, uri: Uri, tag: String?): Boolean {
        // If we do not handle this protocol we keep the original behavior
        return if (handleAllProtocols || uri.scheme in handledProtocols) {
            imageLoader?.let {
                val placeHolder = it.placeholder(imageView.context, tag)
                it[imageView, uri, placeHolder] = tag
            }
            true
        } else false
    }

    /**
     * Cancel loading for the given [ImageView]
     */
    fun cancelImage(imageView: ImageView) {
        imageLoader?.cancel(imageView)
    }

    interface IDrawerImageLoader {
        /**
         * Start loading the image [uri] for the given [imageView] providing the [placeholder], allowing to identify the location it is gonna be used via the [tag]
         */
        operator fun set(imageView: ImageView, uri: Uri, placeholder: Drawable, tag: String?)

        /**
         * Cancel loading images for the imageView
         */
        fun cancel(imageView: ImageView)

        @Deprecated("Please use the placeholder method with the provided tag instead")
        fun placeholder(ctx: Context): Drawable

        /**
         * Retrieve the placeholder to display, using the [tag] to identify the location it is gonna be used
         */
        fun placeholder(ctx: Context, tag: String?): Drawable
    }

    companion object {

        private var SINGLETON: DrawerImageLoader? = null

        fun init(loaderImpl: IDrawerImageLoader): DrawerImageLoader {
            SINGLETON = DrawerImageLoader(loaderImpl)
            return SINGLETON as DrawerImageLoader
        }

        val instance: DrawerImageLoader
            get() {
                if (SINGLETON == null) {
                    SINGLETON = DrawerImageLoader(object : AbstractDrawerImageLoader() {})
                }
                return SINGLETON as DrawerImageLoader
            }
    }
}
