package com.mikepenz.materialdrawer.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView

/**
 * Created by mikepenz on 24.03.15.
 */
class DrawerImageLoader private constructor(var imageLoader: IDrawerImageLoader?) {
    private var mHandleAllUris = false

    enum class Tags {
        PROFILE,
        PROFILE_DRAWER_ITEM,
        ACCOUNT_HEADER
    }

    fun withHandleAllUris(handleAllUris: Boolean): DrawerImageLoader {
        this.mHandleAllUris = handleAllUris
        return this
    }

    /**
     * @param imageView
     * @param uri
     * @param tag
     * @return false if not consumed
     */
    fun setImage(imageView: ImageView, uri: Uri, tag: String?): Boolean {
        //if we do not handle all uris and are not http or https we keep the original behavior
        if (mHandleAllUris || "http" == uri.scheme || "https" == uri.scheme) {
            imageLoader?.let {
                val placeHolder = it.placeholder(imageView.context, tag)
                it[imageView, uri, placeHolder] = tag
            }
            return true
        }
        return false
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
