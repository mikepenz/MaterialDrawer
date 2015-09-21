package com.mikepenz.materialdrawer.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialdrawer.util.PressedEffectStateListDrawable;
import com.mikepenz.materialize.util.UIUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by mikepenz on 13.07.15.
 */
public class ImageHolder {
    private Uri mUri;
    private Drawable mIcon;
    private Bitmap mBitmap;
    private int mIconRes = -1;
    private IIcon mIIcon;

    public ImageHolder(String url) {
        this.mUri = Uri.parse(url);
    }

    public ImageHolder(Uri uri) {
        this.mUri = uri;
    }

    public ImageHolder(Drawable icon) {
        this.mIcon = icon;
    }

    public ImageHolder(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    public ImageHolder(@DrawableRes int iconRes) {
        this.mIconRes = iconRes;
    }

    public ImageHolder(IIcon iicon) {
        this.mIIcon = iicon;
    }

    public Uri getUri() {
        return mUri;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public int getIconRes() {
        return mIconRes;
    }

    public IIcon getIIcon() {
        return mIIcon;
    }


    /**
     * sets an existing image to the imageView
     *
     * @param imageView
     * @return true if an image was set
     */
    public boolean applyTo(ImageView imageView) {
        return applyTo(imageView, null);
    }

    /**
     * sets an existing image to the imageView
     *
     * @param imageView
     * @param tag       used to identify imageViews and define different placeholders
     * @return true if an image was set
     */
    public boolean applyTo(ImageView imageView, String tag) {
        if (mUri != null) {
            if ("http".equals(mUri.getScheme()) || "https".equals(mUri.getScheme())) {
                DrawerImageLoader.getInstance().setImage(imageView, mUri, tag);
            } else {
                imageView.setImageURI(mUri);
            }
        } else if (mIcon != null) {
            imageView.setImageDrawable(mIcon);
        } else if (mBitmap != null) {
            imageView.setImageBitmap(mBitmap);
        } else if (mIconRes != -1) {
            imageView.setImageResource(mIconRes);
        } else if (mIIcon != null) {
            imageView.setImageDrawable(new IconicsDrawable(imageView.getContext(), mIIcon).actionBar());
        } else {
            imageView.setImageBitmap(null);
            return false;
        }
        return true;
    }

    /**
     * this only handles Drawables
     *
     * @param ctx
     * @param iconColor
     * @param tint
     * @return
     */
    public Drawable decideIcon(Context ctx, int iconColor, boolean tint, int paddingDp) {
        Drawable icon = mIcon;

        if (mIIcon != null) {
            icon = new IconicsDrawable(ctx, mIIcon).color(iconColor).sizeDp(24).paddingDp(paddingDp);
        } else if (mIconRes != -1) {
            icon = UIUtils.getCompatDrawable(ctx, mIconRes);
        } else if (mUri != null) {
            try {
                InputStream inputStream = ctx.getContentResolver().openInputStream(mUri);
                icon = Drawable.createFromStream(inputStream, mUri.toString());
            } catch (FileNotFoundException e) {
                //no need to handle this
            }
        }

        //if we got an icon AND we have auto tinting enabled AND it is no IIcon, tint it ;)
        if (icon != null && tint && mIIcon == null) {
            icon = icon.mutate();
            icon.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
        }

        return icon;
    }

    /**
     * a small static helper to set the image from the imageHolder nullSave to the imageView
     *
     * @param imageHolder
     * @param imageView
     * @return true if an image was set
     */
    public static boolean applyTo(ImageHolder imageHolder, ImageView imageView) {
        return applyTo(imageHolder, imageView, null);
    }

    /**
     * a small static helper to set the image from the imageHolder nullSave to the imageView
     *
     * @param imageHolder
     * @param imageView
     * @param tag         used to identify imageViews and define different placeholders
     * @return true if an image was set
     */
    public static boolean applyTo(ImageHolder imageHolder, ImageView imageView, String tag) {
        if (imageHolder != null && imageView != null) {
            return imageHolder.applyTo(imageView, tag);
        }
        return false;
    }

    /**
     * a small static helper to set the image from the imageHolder nullSave to the imageView and hide the view if no image was set
     *
     * @param imageHolder
     * @param imageView
     */
    public static void applyToOrSetInvisible(ImageHolder imageHolder, ImageView imageView) {
        applyToOrSetInvisible(imageHolder, imageView, null);
    }

    /**
     * a small static helper to set the image from the imageHolder nullSave to the imageView and hide the view if no image was set
     *
     * @param imageHolder
     * @param imageView
     * @param tag         used to identify imageViews and define different placeholders
     */
    public static void applyToOrSetInvisible(ImageHolder imageHolder, ImageView imageView, String tag) {
        boolean imageSet = applyTo(imageHolder, imageView, tag);
        if (imageView != null) {
            if (imageSet) {
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.INVISIBLE);
            }
        }
    }


    /**
     * a small static helper to set the image from the imageHolder nullSave to the imageView and hide the view if no image was set
     *
     * @param imageHolder
     * @param imageView
     */
    public static void applyToOrSetGone(ImageHolder imageHolder, ImageView imageView) {
        applyToOrSetGone(imageHolder, imageView, null);
    }

    /**
     * a small static helper to set the image from the imageHolder nullSave to the imageView and hide the view if no image was set
     *
     * @param imageHolder
     * @param imageView
     * @param tag         used to identify imageViews and define different placeholders
     */
    public static void applyToOrSetGone(ImageHolder imageHolder, ImageView imageView, String tag) {
        boolean imageSet = applyTo(imageHolder, imageView, tag);
        if (imageView != null) {
            if (imageSet) {
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * a small static helper which catches nulls for us
     *
     * @param imageHolder
     * @param ctx
     * @param iconColor
     * @param tint
     * @return
     */
    public static Drawable decideIcon(ImageHolder imageHolder, Context ctx, int iconColor, boolean tint, int paddingDp) {
        if (imageHolder == null) {
            return null;
        } else {
            return imageHolder.decideIcon(ctx, iconColor, tint, paddingDp);
        }
    }

    /**
     * decides which icon to apply or hide this view
     *
     * @param imageHolder
     * @param imageView
     * @param iconColor
     * @param tint
     * @param paddingDp
     */
    public static void applyDecidedIconOrSetGone(ImageHolder imageHolder, ImageView imageView, int iconColor, boolean tint, int paddingDp) {
        if (imageHolder != null && imageView != null) {
            Drawable drawable = ImageHolder.decideIcon(imageHolder, imageView.getContext(), iconColor, tint, paddingDp);
            if (drawable != null) {
                imageView.setImageDrawable(drawable);
                imageView.setVisibility(View.VISIBLE);
            } else if (imageHolder.getBitmap() != null) {
                imageView.setImageBitmap(imageHolder.getBitmap());
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
        } else if (imageView != null) {
            imageView.setVisibility(View.GONE);
        }
    }

    /**
     * a small static helper to set a multi state drawable on a view
     *
     * @param icon
     * @param iconColor
     * @param selectedIcon
     * @param selectedIconColor
     * @param tinted
     * @param imageView
     */
    public static void applyMultiIconTo(Drawable icon, int iconColor, Drawable selectedIcon, int selectedIconColor, boolean tinted, ImageView imageView) {
        //if we have an icon then we want to set it
        if (icon != null) {
            //if we got a different color for the selectedIcon we need a StateList
            if (selectedIcon != null) {
                imageView.setImageDrawable(DrawerUIUtils.getIconStateList(icon, selectedIcon));
            } else if (tinted) {
                imageView.setImageDrawable(new PressedEffectStateListDrawable(icon, iconColor, selectedIconColor));
            } else {
                imageView.setImageDrawable(icon);
            }
            //make sure we display the icon
            imageView.setVisibility(View.VISIBLE);
        } else {
            //hide the icon
            imageView.setVisibility(View.GONE);
        }
    }
}
