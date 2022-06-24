package com.mikepenz.materialdrawer.app

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.multidex.MultiDexApplication
import coil.dispose
import coil.load
import com.google.android.material.color.DynamicColors
import com.mikepenz.iconics.Iconics
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.utils.backgroundColorRes
import com.mikepenz.iconics.utils.sizeDp
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader
import com.mikepenz.materialdrawer.util.getPlaceHolder

/**
 * Created by mikepenz on 27.03.15.
 */
class CustomApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        Iconics.init(this)

        DynamicColors.applyToActivitiesIfAvailable(this)

        //initialize and create the image loader logic
        /*
        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable) {
                Picasso.get().load(uri).placeholder(placeholder).into(imageView)
            }

            override fun cancel(imageView: ImageView) {
                Picasso.get().cancelRequest(imageView)
            }
        })
        */

        //initialize and create the image loader logic
        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable, tag: String?) {
                imageView.load(uri) {
                    allowHardware(false)
                    placeholder(placeholder)
                }
            }

            override fun cancel(imageView: ImageView) {
                imageView.dispose()
            }

            override fun placeholder(ctx: Context, tag: String?): Drawable {
                //define different placeholders for different imageView targets
                //default tags are accessible via the DrawerImageLoader.Tags
                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                return when (tag) {
                    DrawerImageLoader.Tags.PROFILE.name -> getPlaceHolder(ctx)
                    DrawerImageLoader.Tags.ACCOUNT_HEADER.name -> IconicsDrawable(ctx, " ").apply { backgroundColorRes = R.color.colorPrimary; sizeDp = 56 }
                    "customUrlItem" -> IconicsDrawable(ctx, " ").apply { backgroundColorRes = R.color.colorAccent; sizeDp = 56 }
                    //we use the default one for
                    //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()
                    else -> super.placeholder(ctx, tag)
                }
            }
        })
    }
}

