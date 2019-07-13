package com.mikepenz.materialdrawer.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView

/**
 * Created by mikepenz on 24.03.15.
 */
open class DrawerImageLoader private constructor(var imageLoader: IDrawerImageLoader?) {

    private var mHandleAllProtocols = false
    private var mHandledProtocols = listOf<String?>("http", "https")

    enum class Tags {
        PROFILE,
        PROFILE_DRAWER_ITEM,
        ACCOUNT_HEADER
    }

    /**
     * Makes this DrawerImageLoader handle all Uri protocols
     */
    fun withHandleAllProtocols(handleAllProtocols: Boolean): DrawerImageLoader {
        this.mHandleAllProtocols = handleAllProtocols
        return this
    }

    /**
     * @param protocols The Uri protocols which this DrawerImageLoader will handle
     */
    fun withProtocols(vararg protocols: String): DrawerImageLoader {
        this.mHandledProtocols = protocols.toList()
        return this
    }

    /**
     * @param imageView
     * @param uri
     * @param tag
     * @return false if not consumed
     */
    open fun setImage(imageView: ImageView, uri: Uri, tag: String?): Boolean {
        // If we do not handle this protocol we keep the original behavior
        return if (mHandleAllProtocols || uri.scheme in mHandledProtocols) {
            imageLoader?.let {
                val placeHolder = it.placeholder(imageView.context, tag)
                it[imageView, uri, placeHolder] = tag
            }
            true
        } else false
    }

    fun cancelImage(imageView: ImageView) {
        imageLoader?.cancel(imageView)
    }

    interface IDrawerImageLoader {
        @Deprecated("")
        operator fun set(imageView: ImageView, uri: Uri, placeholder: Drawable)

        operator fun set(imageView: ImageView, uri: Uri, placeholder: Drawable, tag: String?)

        fun cancel(imageView: ImageView)

        fun placeholder(ctx: Context): Drawable

        /**
         * @param ctx
         * @param tag current possible tags: "profile", "profileDrawerItem", "accountHeader"
         * @return
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
                    SINGLETON = DrawerImageLoader(object : AbstractDrawerImageLoader() {

                    })
                }
                return SINGLETON as DrawerImageLoader
            }
    }
}
