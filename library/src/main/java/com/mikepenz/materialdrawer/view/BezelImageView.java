/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * The original is from Google you can find here:
 * https://github.com/google/iosched/blob/master/android/src/main/java/com/google/samples/apps/iosched/ui/widget/BezelImageView.java
 *
 * Modified and improved with additional functionality by Mike Penz
 */

package com.mikepenz.materialdrawer.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;


/**
 * An {@link android.widget.ImageView} that draws its contents inside a mask and draws a border
 * drawable on top. This is useful for applying a beveled look to image contents, but is also
 * flexible enough for use with other desired aesthetics.
 */
public class BezelImageView extends ImageView {
    private Paint mBlackPaint;
    private Paint mMaskedPaint;

    private Rect mBounds;
    private RectF mBoundsF;

    private Drawable mMaskDrawable;
    private boolean mDrawCircularShadow = true;

    private ColorMatrixColorFilter mDesaturateColorFilter;

    private int mSelectorAlpha = 150;
    private int mSelectorColor;
    private ColorFilter mSelectorFilter;

    private boolean mCacheValid = false;
    private Bitmap mCacheBitmap;
    private int mCachedWidth;
    private int mCachedHeight;

    private boolean isPressed = false;
    private boolean isSelected;

    public BezelImageView(Context context) {
        this(context, null);
    }

    public BezelImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezelImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Attribute initialization
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BezelImageView, defStyle, R.style.BezelImageView);

        mMaskDrawable = a.getDrawable(R.styleable.BezelImageView_biv_maskDrawable);
        if (mMaskDrawable != null) {
            mMaskDrawable.setCallback(this);
        }

        mDrawCircularShadow = a.getBoolean(R.styleable.BezelImageView_biv_drawCircularShadow, true);

        mSelectorColor = a.getColor(R.styleable.BezelImageView_biv_selectorOnPress, 0);

        a.recycle();

        // Other initialization
        mBlackPaint = new Paint();
        mBlackPaint.setColor(0xff000000);

        mMaskedPaint = new Paint();
        mMaskedPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // Always want a cache allocated.
        mCacheBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);

        // Create a desaturate color filter for pressed state.
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        mDesaturateColorFilter = new ColorMatrixColorFilter(cm);

        //create a selectorFilter if we already have a color
        if (mSelectorColor != 0) {
            this.mSelectorFilter = new PorterDuffColorFilter(Color.argb(mSelectorAlpha, Color.red(mSelectorColor), Color.green(mSelectorColor), Color.blue(mSelectorColor)), PorterDuff.Mode.SRC_ATOP);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int old_w, int old_h) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mDrawCircularShadow) {
                setOutlineProvider(new CustomOutline(w, h));
            }
        }
    }

    @TargetApi(21)
    private class CustomOutline extends ViewOutlineProvider {

        int width;
        int height;

        CustomOutline(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, width, height);
        }
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        final boolean changed = super.setFrame(l, t, r, b);
        mBounds = new Rect(0, 0, r - l, b - t);
        mBoundsF = new RectF(mBounds);

        if (mMaskDrawable != null) {
            mMaskDrawable.setBounds(mBounds);
        }

        if (changed) {
            mCacheValid = false;
        }

        return changed;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBounds == null) {
            return;
        }

        int width = mBounds.width();
        int height = mBounds.height();

        if (width == 0 || height == 0) {
            return;
        }

        if (!mCacheValid || width != mCachedWidth || height != mCachedHeight || isSelected != isPressed) {
            // Need to redraw the cache
            if (width == mCachedWidth && height == mCachedHeight) {
                // Have a correct-sized bitmap cache already allocated. Just erase it.
                mCacheBitmap.eraseColor(0);
            } else {
                // Allocate a new bitmap with the correct dimensions.
                mCacheBitmap.recycle();
                //noinspection AndroidLintDrawAllocation
                mCacheBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                mCachedWidth = width;
                mCachedHeight = height;
            }

            Canvas cacheCanvas = new Canvas(mCacheBitmap);
            if (mMaskDrawable != null) {
                int sc = cacheCanvas.save();
                mMaskDrawable.draw(cacheCanvas);
                if (isSelected) {
                    if (mSelectorFilter != null) {
                        mMaskedPaint.setColorFilter(mSelectorFilter);
                    } else {
                        mMaskedPaint.setColorFilter(mDesaturateColorFilter);

                    }
                } else {
                    mMaskedPaint.setColorFilter(null);
                }
                cacheCanvas.saveLayer(mBoundsF, mMaskedPaint,
                        Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG);
                super.onDraw(cacheCanvas);
                cacheCanvas.restoreToCount(sc);
            } else if (isSelected) {
                int sc = cacheCanvas.save();
                cacheCanvas.drawRect(0, 0, mCachedWidth, mCachedHeight, mBlackPaint);
                if (mSelectorFilter != null) {
                    mMaskedPaint.setColorFilter(mSelectorFilter);
                } else {
                    mMaskedPaint.setColorFilter(mDesaturateColorFilter);
                }
                cacheCanvas.saveLayer(mBoundsF, mMaskedPaint,
                        Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG);
                super.onDraw(cacheCanvas);
                cacheCanvas.restoreToCount(sc);
            } else {
                super.onDraw(cacheCanvas);
            }
        }

        // Draw from cache
        canvas.drawBitmap(mCacheBitmap, mBounds.left, mBounds.top, null);

        //remember the previous press state
        isPressed = isPressed();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // Check for clickable state and do nothing if disabled
        if (!this.isClickable()) {
            this.isSelected = false;
            return super.onTouchEvent(event);
        }

        // Set selected state based on Motion Event
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.isSelected = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_SCROLL:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
                this.isSelected = false;
                break;
        }

        // Redraw image and return super type
        this.invalidate();
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mMaskDrawable != null && mMaskDrawable.isStateful()) {
            mMaskDrawable.setState(getDrawableState());
        }
        if (isDuplicateParentStateEnabled()) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public void invalidateDrawable(Drawable who) {
        if (who == mMaskDrawable) {
            invalidate();
        } else {
            super.invalidateDrawable(who);
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return who == mMaskDrawable || super.verifyDrawable(who);
    }


    /**
     * Sets the color of the selector to be draw over the
     * CircularImageView. Be sure to provide some opacity.
     *
     * @param selectorColor The color (including alpha) to set for the selector overlay.
     */
    public void setSelectorColor(int selectorColor) {
        this.mSelectorColor = selectorColor;
        this.mSelectorFilter = new PorterDuffColorFilter(Color.argb(mSelectorAlpha, Color.red(mSelectorColor), Color.green(mSelectorColor), Color.blue(mSelectorColor)), PorterDuff.Mode.SRC_ATOP);
        this.invalidate();
    }


    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
    }

    @Override
    public void setImageURI(Uri uri) {
        if ("http".equals(uri.getScheme()) || "https".equals(uri.getScheme())) {
            DrawerImageLoader.getInstance().setImage(this, uri, null);
        } else {
            super.setImageURI(uri);
        }
    }

    private ColorMatrixColorFilter mTempDesaturateColorFilter;
    private ColorFilter mTempSelectorFilter;

    public void disableTouchFeedback(boolean disable) {
        if (disable) {
            mTempDesaturateColorFilter = this.mDesaturateColorFilter;
            mTempSelectorFilter = this.mSelectorFilter;
            this.mSelectorFilter = null;
            this.mDesaturateColorFilter = null;
        } else {
            if (mTempDesaturateColorFilter != null) {
                this.mDesaturateColorFilter = mTempDesaturateColorFilter;
            }
            if (mTempSelectorFilter != null) {
                this.mSelectorFilter = mTempSelectorFilter;
            }
        }
    }
}
