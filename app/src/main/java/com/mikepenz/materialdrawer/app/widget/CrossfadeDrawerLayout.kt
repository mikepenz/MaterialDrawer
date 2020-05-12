package com.mikepenz.materialdrawer.app.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.mikepenz.crossfadedrawerlayout.ApplyTransformationListener
import com.mikepenz.crossfadedrawerlayout.animation.ResizeWidthAnimation
import com.mikepenz.materialdrawer.app.R
import com.mikepenz.materialdrawer.app.utils.convertDpToPixel

/**
 * Created by mikepenz on 20.10.15.
 */
open class CrossfadeDrawerLayout : DrawerLayout {
    var drawerOpened = false
        private set

    private var touchDown = -1f
    private var prevTouch = -1f

    //remember the previous width to optimize performance
    private var prevWidth = -1

    var crossfadeListener: ((containerView: View?, currentSlidePercentage: Float, slideOffset: Int) -> Unit)? = null

    var minWidthPx = 0
    var maxWidthPx = 0

    lateinit var container: ConstraintLayout
        private set
    lateinit var smallView: ViewGroup
        private set
    lateinit var largeView: ViewGroup
        private set

    var isCrossfaded = false
        private set
    var sliderOnRight = false

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init(context)
    }

    private fun init(ctx: Context) {
        super.addDrawerListener(innerDrawerListener)
        //define default valuse for min and max
        minWidthPx = convertDpToPixel(72f, ctx).toInt()
        maxWidthPx = convertDpToPixel(200f, ctx).toInt()
    }

    override fun addView(child: View, index: Int) {
        super.addView(wrapSliderContent(child, index), index)
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams?) {
        super.addView(wrapSliderContent(child, index), index, params)
    }

    /**
     * this will wrap the view which is added to the slider into another layout so we can then overlap the small and large view
     *
     * @param child
     * @param index
     * @return
     */
    private fun wrapSliderContent(child: View, index: Int): View? {
        if (child.id == R.id.crossFadeSlider) {
            container = child as ConstraintLayout
            largeView = child.findViewById(R.id.crossFadeLargeView)
            largeView.alpha = 0f
            largeView.visibility = View.GONE
            smallView = child.findViewById(R.id.crossFadeSmallView)
            return container
        }
        return child
    }

    private var innerDrawerListener = object : DrawerListener {
        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            drawerOpened = slideOffset == 1f
        }

        override fun onDrawerOpened(drawerView: View) {
            drawerOpened = true
        }

        override fun onDrawerClosed(drawerView: View) {
            val lp = drawerView.layoutParams as MarginLayoutParams
            lp.width = minWidthPx
            drawerView.layoutParams = lp
            //revert alpha :D
            smallView.alpha = 1f
            smallView.bringToFront()
            largeView.alpha = 0f
            drawerOpened = false
        }

        override fun onDrawerStateChanged(newState: Int) {}
    }

    override fun dispatchTouchEvent(motionEvent: MotionEvent): Boolean {
        if (drawerOpened) {
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                touchDown = motionEvent.x
                prevTouch = motionEvent.x
                return super.dispatchTouchEvent(motionEvent)
            } else if (motionEvent.action == MotionEvent.ACTION_UP) {
                val click = touchDown == prevTouch
                touchDown = -1f
                prevTouch = -1f
                val lp = container.layoutParams as MarginLayoutParams
                val percentage = calculatePercentage(lp.width)
                if (percentage > 50) {
                    fadeUp(DEFAULT_ANIMATION)
                } else {
                    fadeDown(DEFAULT_ANIMATION)
                }
                return if (click) {
                    super.dispatchTouchEvent(motionEvent)
                } else {
                    true
                }
            } else if (motionEvent.action == MotionEvent.ACTION_MOVE && touchDown != -1f) {
                val lp = container.layoutParams as MarginLayoutParams
                //the current drawer width
                var diff = motionEvent.x - touchDown
                if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL || sliderOnRight) {
                    diff *= -1
                }
                if (diff == 0f) {
                    //no difference nothing to do
                    //return super.dispatchTouchEvent(motionEvent);
                } else if (diff > 0 && lp.width <= maxWidthPx && lp.width + diff < maxWidthPx && lp.width >= minWidthPx) {
                    lp.width = (lp.width + diff).toInt()
                    container.layoutParams = lp
                    touchDown = motionEvent.x
                    overlapViews(lp.width)
                } else if (diff < 0 && lp.width >= minWidthPx && lp.width + diff > minWidthPx) {
                    lp.width = (lp.width + diff).toInt()
                    container.layoutParams = lp
                    touchDown = motionEvent.x
                    overlapViews(lp.width)
                } else if (lp.width < minWidthPx) {
                    lp.width = minWidthPx
                    container.layoutParams = lp
                    drawerOpened = false
                    touchDown = -1f
                    overlapViews(minWidthPx)
                } else if (lp.width + diff < minWidthPx) { //return super.dispatchTouchEvent(motionEvent);
                }
                //return true;
            }
        }
        return super.dispatchTouchEvent(motionEvent)
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        try {
            return super.onTouchEvent(motionEvent)
        } catch (ex: RuntimeException) {
            ex.printStackTrace()
        }
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        try {
            return super.onInterceptTouchEvent(ev)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }
        return false
    }

    override fun openDrawer(gravity: Int) {
        drawerOpened = true
        super.openDrawer(gravity)
    }

    override fun openDrawer(drawerView: View) {
        drawerOpened = true
        super.openDrawer(drawerView)
    }

    override fun openDrawer(drawerView: View, animate: Boolean) {
        drawerOpened = true
        super.openDrawer(drawerView, animate)
    }

    override fun openDrawer(gravity: Int, animate: Boolean) {
        drawerOpened = true
        super.openDrawer(gravity, animate)
    }

    override fun closeDrawer(drawerView: View) {
        drawerOpened = false
        container.clearAnimation()
        super.closeDrawer(drawerView)
    }

    override fun closeDrawer(drawerView: View, animate: Boolean) {
        drawerOpened = false
        container.clearAnimation()
        super.closeDrawer(drawerView, animate)
    }

    override fun closeDrawer(gravity: Int) {
        drawerOpened = false
        container.clearAnimation()
        super.closeDrawer(gravity)
    }

    override fun closeDrawer(gravity: Int, animate: Boolean) {
        drawerOpened = false
        container.clearAnimation()
        super.closeDrawer(gravity, animate)
    }

    /**
     * crossfade the small to the large view (with default animation time)
     */
    @JvmOverloads
    fun crossfade(duration: Int = DEFAULT_ANIMATION) {
        if (isCrossfaded) {
            fadeDown(duration)
        } else {
            fadeUp(duration)
        }
    }

    /**
     * animate to the large view
     *
     * @param duration
     */
    fun fadeUp(duration: Int) { //animate up
        container.clearAnimation()
        val anim = ResizeWidthAnimation(container, maxWidthPx, ApplyTransformationListener { width -> overlapViews(width) })
        anim.duration = duration.toLong()
        container.startAnimation(anim)
    }

    /**
     * animate to the small view
     *
     * @param duration
     */
    fun fadeDown(duration: Int) { //fade down
        container.clearAnimation()
        val anim = ResizeWidthAnimation(container, minWidthPx, ApplyTransformationListener { width -> overlapViews(width) })
        anim.duration = duration.toLong()
        container.startAnimation(anim)
    }

    /**
     * calculate the percentage to how many percent the slide is already visible
     *
     * @param width
     * @return
     */
    private fun calculatePercentage(width: Int): Float {
        val absolute = maxWidthPx - minWidthPx
        val current = width - minWidthPx
        val percentage = 100.0f * current / absolute
        //we can assume that we are crossfaded if the percentage is > 90
        isCrossfaded = percentage > 90
        return percentage
    }

    /**
     * overlap the views and provide the crossfade effect
     *
     * @param width
     */
    private fun overlapViews(width: Int) {
        if (width == prevWidth) {
            return
        }
        //remember this width so it is't processed twice
        prevWidth = width
        val percentage = calculatePercentage(width)
        val alpha = percentage / 100
        smallView.alpha = 1f
        smallView.isClickable = false
        largeView.bringToFront()
        largeView.alpha = alpha
        largeView.isClickable = true
        largeView.visibility = if (alpha > 0.01f) View.VISIBLE else View.GONE
        //notify the crossfadeListener
        crossfadeListener?.invoke(container, calculatePercentage(width), width)
    }

    companion object {
        private const val DEFAULT_ANIMATION = 200
    }
}