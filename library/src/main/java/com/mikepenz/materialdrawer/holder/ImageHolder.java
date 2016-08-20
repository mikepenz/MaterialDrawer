package com.mikepenz.materialdrawer.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by mikepenz on 13.07.15.
 */

public class ImageHolder extends com.mikepenz.materialize.holder.ImageHolder {
    private IIcon mIIcon;

    public ImageHolder(String url) {
        super(url);
    }

    public ImageHolder(Uri uri) {
        super(uri);
    }

    public ImageHolder(Drawable icon) {
        super(icon);
    }

    public ImageHolder(Bitmap bitmap) {
        super(bitmap);
    }

    public ImageHolder(@DrawableRes int iconRes) {
        super(iconRes);
    }

    public ImageHolder(IIcon iicon) {
        super((Bitmap) null);
        this.mIIcon = iicon;
    }

    public IIcon getIIcon() {
        return mIIcon;
    }

    public void setIIcon(IIcon mIIcon) {
        this.mIIcon = mIIcon;
    }

    /**
     * sets an existing image to the imageView
     *
     * @param imageView
     * @param tag       used to identify imageViews and define different placeholders
     * @return true if an image was set
     */
    @Override
    public boolean applyTo(ImageView imageView, String tag) {
        if (getUri() != null) {
            boolean consumed = DrawerImageLoader.getInstance().setImage(imageView, getUri(), tag);
            if (!consumed) {
                imageView.setImageURI(getUri());
            }
        } else if (getIcon() != null) {
            imageView.setImageDrawable(getIcon());
        } else if (getBitmap() != null) {
            imageView.setImageBitmap(getBitmap());
        } else if (getIconRes() != -1) {
            imageView.setImageResource(getIconRes());
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
        Drawable icon = getIcon();

        if (mIIcon != null) {
            icon = new IconicsDrawable(ctx, mIIcon).color(iconColor).sizeDp(24).paddingDp(paddingDp);
        } else if (getIconRes() != -1) {
            icon = ContextCompat.getDrawable(ctx, getIconRes());
        } else if (getUri() != null) {
            try {
                InputStream inputStream = ctx.getContentResolver().openInputStream(getUri());
                icon = Drawable.createFromStream(inputStream, getUri().toString());
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

}
