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

package com.mikepenz.materialdrawer.view

import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewOutlineProvider
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import com.mikepenz.materialdrawer.R


/**
 * An [android.widget.ImageView] that draws its contents inside a mask and draws a border
 * drawable on top. This is useful for applying a beveled look to image contents, but is also
 * flexible enough for use with other desired aesthetics.
 */
open class BezelImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : AppCompatImageView(context, attrs, defStyle) {
    private val mBlackPaint: Paint
    private val mMaskedPaint: Paint

    private var mBounds: Rect? = null
    private var mBoundsF: RectF? = null

    private val mMaskDrawable: Drawable?
    private var mDrawCircularShadow = true

    private var mDesaturateColorFilter: ColorMatrixColorFilter? = null

    private val mSelectorAlpha = 150
    private var mSelectorColor: Int = 0
    private var mSelectorFilter: ColorFilter? = null

    private var mCacheValid = false
    private var mCacheBitmap: Bitmap
    private var mCachedWidth: Int = 0
    private var mCachedHeight: Int = 0

    private var mIsPressed = false
    private var mIsSelected: Boolean = false

    private var mTempDesaturateColorFilter: ColorMatrixColorFilter? = null
    private var mTempSelectorFilter: ColorFilter? = null

    init {

        // Attribute initialization
        val a = context.obtainStyledAttributes(attrs, R.styleable.BezelImageView, defStyle, R.style.BezelImageView)

        mMaskDrawable = a.getDrawable(R.styleable.BezelImageView_materialDrawerMaskDrawable)
        if (mMaskDrawable != null) {
            mMaskDrawable.callback = this
        }

        mDrawCircularShadow = a.getBoolean(R.styleable.BezelImageView_materialDrawerDrawCircularShadow, true)

        mSelectorColor = a.getColor(R.styleable.BezelImageView_materialDrawerSelectorOnPress, 0)

        a.recycle()

        // Other initialization
        mBlackPaint = Paint()
        mBlackPaint.color = -0x1000000

        mMaskedPaint = Paint()
        mMaskedPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        // Always want a cache allocated.
        mCacheBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

        // Create a desaturate color filter for pressed state.
        val cm = ColorMatrix()
        cm.setSaturation(0f)
        mDesaturateColorFilter = ColorMatrixColorFilter(cm)

        //create a selectorFilter if we already have a color
        if (mSelectorColor != 0) {
            this.mSelectorFilter = PorterDuffColorFilter(Color.argb(mSelectorAlpha, Color.red(mSelectorColor), Color.green(mSelectorColor), Color.blue(mSelectorColor)), PorterDuff.Mode.SRC_ATOP)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, old_w: Int, old_h: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mDrawCircularShadow) {
                outlineProvider = CustomOutline(w, h)
            }
        }
    }

    @TargetApi(21)
    private inner class CustomOutline internal constructor(internal var width: Int, internal var height: Int) : ViewOutlineProvider() {

        override fun getOutline(view: View, outline: Outline) {
            outline.setOval(0, 0, width, height)
        }
    }

    override fun setFrame(l: Int, t: Int, r: Int, b: Int): Boolean {
        val changed = super.setFrame(l, t, r, b)
        mBounds = Rect(0, 0, r - l, b - t).also {
            mBoundsF = RectF(it)

            if (mMaskDrawable != null) {
                mMaskDrawable.bounds = it
            }
        }

        if (changed) {
            mCacheValid = false
        }

        return changed
    }

    override fun onDraw(canvas: Canvas) {
        val bounds = mBounds ?: return

        val width = bounds.width()
        val height = bounds.height()

        if (width == 0 || height == 0) {
            return
        }

        if (!mCacheValid || width != mCachedWidth || height != mCachedHeight || mIsSelected != mIsPressed) {
            // Need to redraw the cache
            if (width == mCachedWidth && height == mCachedHeight) {
                // Have a correct-sized bitmap cache already allocated. Just erase it.
                mCacheBitmap.eraseColor(0)
            } else {
                // Allocate a new bitmap with the correct dimensions.
                mCacheBitmap.recycle()

                mCacheBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                mCachedWidth = width
                mCachedHeight = height
            }

            val cacheCanvas = Canvas(mCacheBitmap)

            when {
                mMaskDrawable != null -> {
                    val sc = cacheCanvas.save()
                    mMaskDrawable.draw(cacheCanvas)
                    if (mIsSelected) {
                        if (mSelectorFilter != null) {
                            mMaskedPaint.colorFilter = mSelectorFilter
                        } else {
                            mMaskedPaint.colorFilter = mDesaturateColorFilter
                        }
                    } else {
                        mMaskedPaint.colorFilter = null
                    }
                    cacheCanvas.saveLayer(mBoundsF, mMaskedPaint, Canvas.ALL_SAVE_FLAG)
                    super.onDraw(cacheCanvas)
                    cacheCanvas.restoreToCount(sc)
                }
                mIsSelected -> {
                    val sc = cacheCanvas.save()
                    cacheCanvas.drawRect(0f, 0f, mCachedWidth.toFloat(), mCachedHeight.toFloat(), mBlackPaint)
                    if (mSelectorFilter != null) {
                        mMaskedPaint.colorFilter = mSelectorFilter
                    } else {
                        mMaskedPaint.colorFilter = mDesaturateColorFilter
                    }
                    cacheCanvas.saveLayer(mBoundsF, mMaskedPaint, Canvas.ALL_SAVE_FLAG)
                    super.onDraw(cacheCanvas)
                    cacheCanvas.restoreToCount(sc)
                }
                else -> super.onDraw(cacheCanvas)
            }
        }

        // Draw from cache
        canvas.drawBitmap(mCacheBitmap, bounds.left.toFloat(), bounds.top.toFloat(), null)

        //remember the previous press state
        mIsPressed = isPressed
    }


    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        // Check for clickable state and do nothing if disabled
        if (!this.isClickable) {
            this.mIsSelected = false
            return super.onTouchEvent(event)
        }

        // Set selected state based on Motion Event
        when (event.action) {
            MotionEvent.ACTION_DOWN -> this.mIsSelected = true
            MotionEvent.ACTION_UP, MotionEvent.ACTION_SCROLL, MotionEvent.ACTION_OUTSIDE, MotionEvent.ACTION_CANCEL -> this.mIsSelected = false
        }

        // Redraw image and return super type
        this.invalidate()
        return super.dispatchTouchEvent(event)
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        if (mMaskDrawable != null && mMaskDrawable.isStateful) {
            mMaskDrawable.state = drawableState
        }
        if (isDuplicateParentStateEnabled) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun invalidateDrawable(who: Drawable) {
        if (who === mMaskDrawable) {
            invalidate()
        } else {
            super.invalidateDrawable(who)
        }
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return who === mMaskDrawable || super.verifyDrawable(who)
    }


    /**
     * Sets the color of the selector to be draw over the
     * CircularImageView. Be sure to provide some opacity.
     *
     * @param selectorColor The color (including alpha) to set for the selector overlay.
     */
    fun setSelectorColor(selectorColor: Int) {
        this.mSelectorColor = selectorColor
        this.mSelectorFilter = PorterDuffColorFilter(Color.argb(mSelectorAlpha, Color.red(mSelectorColor), Color.green(mSelectorColor), Color.blue(mSelectorColor)), PorterDuff.Mode.SRC_ATOP)
        this.invalidate()
    }

    fun disableTouchFeedback(disable: Boolean) {
        if (disable) {
            mTempDesaturateColorFilter = this.mDesaturateColorFilter
            mTempSelectorFilter = this.mSelectorFilter
            this.mSelectorFilter = null
            this.mDesaturateColorFilter = null
        } else {
            if (mTempDesaturateColorFilter != null) {
                this.mDesaturateColorFilter = mTempDesaturateColorFilter
            }
            if (mTempSelectorFilter != null) {
                this.mSelectorFilter = mTempSelectorFilter
            }
        }
    }
}
