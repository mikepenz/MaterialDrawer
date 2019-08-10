package com.mikepenz.materialdrawer.model

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.shape.ShapeAppearanceModel
import com.mikepenz.fastadapter.IParentItem
import com.mikepenz.fastadapter.ISubItem
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.holder.ColorHolder
import com.mikepenz.materialdrawer.holder.applyColor
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.OnPostBindViewListener
import com.mikepenz.materialdrawer.model.interfaces.Selectable
import com.mikepenz.materialdrawer.model.interfaces.Tagable
import com.mikepenz.materialdrawer.model.interfaces.Typefaceable
import com.mikepenz.materialdrawer.util.DrawerUIUtils

/**
 * Created by mikepenz on 14.07.15.
 */
abstract class AbstractDrawerItem<T, VH : RecyclerView.ViewHolder> : IDrawerItem<VH>, Selectable<T>, Tagable<T>, Typefaceable<T> {
    // the identifier for this item
    override var identifier: Long = -1

    // the tag for this item
    override var tag: Any? = null
    // defines if this item is enabled
    override var isEnabled = true

    // defines if the item is selected
    override var isSelected = false
    // defines if this item is selectable
    override var isSelectable = true
    // defines if the item's background' change should be animated when it is (de)selected
    var isSelectedBackgroundAnimated = true
    // defines the content descripton of items
    var contentDescription: String? = null

    var selectedColor: ColorHolder? = null
    var textColor: ColorHolder? = null
    var selectedTextColor: ColorHolder? = null
    var disabledTextColor: ColorHolder? = null
    override var typeface: Typeface? = null
    var colorStateList: Pair<Int, ColorStateList>? = null

    open var onDrawerItemClickListener: Drawer.OnDrawerItemClickListener? = null

    var onPostBindViewListener: OnPostBindViewListener? = null
        protected set

    // the parent of this item
    override var parent: IParentItem<*>? = null
    // the subItems to expand for this item
    private var mSubItems: MutableList<ISubItem<*>> = mutableListOf()
    override var subItems: MutableList<ISubItem<*>>
        get() = mSubItems
        set(subItems) {
            this.mSubItems = subItems

            subItems.let {
                for (subItem in it) {
                    subItem.parent = this
                }
            }
        }

    //if the this item is currently expanded
    override var isExpanded = false

    /**
     * overwrite this method and return true if the item should auto expand on click, false if you want to disable this
     *
     * @return true if this item should auto expand in the adapter
     */
    override val isAutoExpanding: Boolean
        get() = true

    fun withIdentifier(identifier: Long): T {
        this.identifier = identifier
        return this as T
    }

    fun withTag(`object`: Any): T {
        this.tag = `object`
        return this as T
    }

    fun withEnabled(enabled: Boolean): T {
        this.isEnabled = enabled
        return this as T
    }

    fun withSelected(selected: Boolean): T {
        this.isSelected = selected
        return this as T
    }

    open fun withSelectable(selectable: Boolean): T {
        this.isSelectable = selectable
        return this as T
    }

    open fun withContentDescription(contentDescription: String?): T {
        this.contentDescription = contentDescription
        return this as T
    }

    fun withSelectedColor(@ColorInt selectedColor: Int): T {
        this.selectedColor = ColorHolder.fromColor(selectedColor)
        return this as T
    }

    fun withSelectedColorRes(@ColorRes selectedColorRes: Int): T {
        this.selectedColor = ColorHolder.fromColorRes(selectedColorRes)
        return this as T
    }

    fun withTextColor(@ColorInt textColor: Int): T {
        this.textColor = ColorHolder.fromColor(textColor)
        return this as T
    }

    fun withTextColorRes(@ColorRes textColorRes: Int): T {
        this.textColor = ColorHolder.fromColorRes(textColorRes)
        return this as T
    }

    fun withSelectedTextColor(@ColorInt selectedTextColor: Int): T {
        this.selectedTextColor = ColorHolder.fromColor(selectedTextColor)
        return this as T
    }

    fun withSelectedTextColorRes(@ColorRes selectedColorRes: Int): T {
        this.selectedTextColor = ColorHolder.fromColorRes(selectedColorRes)
        return this as T
    }

    fun withDisabledTextColor(@ColorInt disabledTextColor: Int): T {
        this.disabledTextColor = ColorHolder.fromColor(disabledTextColor)
        return this as T
    }

    fun withDisabledTextColorRes(@ColorRes disabledTextColorRes: Int): T {
        this.disabledTextColor = ColorHolder.fromColorRes(disabledTextColorRes)
        return this as T
    }

    /**
     * allows to set the typeface being useable for the item implementation
     */
    override fun withTypeface(typeface: Typeface?): T {
        this.typeface = typeface
        return this as T
    }

    /**
     * set if this item is selectable
     *
     * @param selectedBackgroundAnimated true if this item's background should fade when it is (de) selected
     * @return
     */
    fun withSelectedBackgroundAnimated(selectedBackgroundAnimated: Boolean): T {
        this.isSelectedBackgroundAnimated = selectedBackgroundAnimated
        return this as T
    }

    /**
     * this listener is called when an item is clicked in the drawer.
     * WARNING: don't overwrite this in the Switch / Toggle drawerItems if you want the toggle / switch to be selected
     * if the item is clicked and the item is not selectable.
     *
     * @param onDrawerItemClickListener
     * @return
     */
    open fun withOnDrawerItemClickListener(onDrawerItemClickListener: Drawer.OnDrawerItemClickListener): T {
        this.onDrawerItemClickListener = onDrawerItemClickListener
        return this as T
    }

    /**
     * add this listener and hook in if you want to modify a drawerItems view without creating a custom drawer item
     *
     * @param onPostBindViewListener
     * @return
     */
    fun withPostOnBindViewListener(onPostBindViewListener: OnPostBindViewListener): T {
        this.onPostBindViewListener = onPostBindViewListener
        return this as T
    }

    /**
     * is called after bindView to allow some post creation setps
     *
     * @param drawerItem the drawerItem which is bound to the view
     * @param view       the currently view which will be bound
     */
    protected fun onPostBindView(drawerItem: IDrawerItem<*>, view: View) {
        onPostBindViewListener?.onBindView(drawerItem, view)
    }

    fun withParent(parent: IParentItem<*>): T {
        this.parent = parent
        return this as T
    }

    fun withSubItems(subItems: MutableList<ISubItem<*>>): T {
        mSubItems = subItems
        return this as T
    }

    /**
     * an array of subItems
     * **WARNING** Make sure the subItems provided already have identifiers
     *
     * @param subItems
     * @return
     */
    fun <SubType : ISubItem<*>> setSubItems(vararg subItems: SubType) {
        for (subItem in subItems) {
            subItem.parent = this
        }

        mSubItems.clear()
        mSubItems.addAll(subItems)
    }

    fun withSubItems(vararg subItems: ISubItem<*>): T {
        setSubItems(*subItems)
        return this as T
    }

    fun withSetExpanded(expanded: Boolean): T {
        isExpanded = expanded
        return this as T
    }

    /**
     * generates a view by the defined LayoutRes
     *
     * @param ctx
     * @return
     */
    override fun generateView(ctx: Context): View {
        val viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(layoutRes, null, false))
        bindView(viewHolder, mutableListOf())
        return viewHolder.itemView
    }

    /**
     * generates a view by the defined LayoutRes and pass the LayoutParams from the parent
     *
     * @param ctx
     * @param parent
     * @return
     */
    override fun generateView(ctx: Context, parent: ViewGroup): View {
        val viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(layoutRes, parent, false))
        bindView(viewHolder, mutableListOf())
        return viewHolder.itemView
    }

    @CallSuper
    override fun bindView(holder: VH, payloads: MutableList<Any>) {
        contentDescription?.let {
            holder.itemView.contentDescription = it
        }
        holder.itemView.setTag(R.id.material_drawer_item, this)
    }

    /**
     * called when the view is unbound
     *
     * @param holder
     */
    override fun unbindView(holder: VH) {
        holder.itemView.clearAnimation()
    }

    /**
     * View got attached to the window
     *
     * @param holder
     */
    override fun attachToWindow(holder: VH) {

    }

    /**
     * View got detached from the window
     *
     * @param holder
     */
    override fun detachFromWindow(holder: VH) {

    }

    /**
     * is called when the ViewHolder is in a transient state. return true if you want to reuse
     * that view anyways
     *
     * @param holder the viewHolder for the view which failed to recycle
     * @return true if we want to recycle anyways (false - it get's destroyed)
     */
    override fun failedToRecycle(holder: VH): Boolean {
        return false
    }

    /**
     * This method returns the ViewHolder for our item, using the provided View.
     *
     * @param parent
     * @return the ViewHolder for this Item
     */
    override fun getViewHolder(parent: ViewGroup): VH {
        return getViewHolder(LayoutInflater.from(parent.context).inflate(layoutRes, parent, false))
    }

    /**
     * This method returns the ViewHolder for our item, using the provided View.
     *
     * @param v
     * @return the ViewHolder for this Item
     */
    abstract fun getViewHolder(v: View): VH

    /**
     * If this item equals to the given identifier
     *
     * @param id
     * @return
     */
    override fun equals(id: Long): Boolean {
        return id == identifier
    }

    override fun equals(id: Int): Boolean {
        return id.toLong() == identifier
    }

    /**
     * If this item equals to the given object
     *
     * @param other
     * @return
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as AbstractDrawerItem<*, *>?
        return identifier == that?.identifier
    }

    /**
     * the hashCode implementation
     *
     * @return
     */
    override fun hashCode(): Int {
        return java.lang.Long.valueOf(identifier).hashCode()
    }

    /**
     * helper method to decide for the correct color
     *
     * @param ctx
     * @return
     */
    protected fun getSelectedColor(ctx: Context): Int {
        return if (DrawerUIUtils.getBooleanStyleable(ctx, R.styleable.MaterialDrawer_material_drawer_legacy_style, false)) {
            selectedColor.applyColor(ctx, R.attr.material_drawer_selected_legacy, R.color.material_drawer_selected_legacy)
        } else {
            selectedColor.applyColor(ctx, R.attr.material_drawer_selected, R.color.material_drawer_selected)
        }
    }

    /**
     * helper method to decide for the correct color
     *
     * @param ctx
     * @return
     */
    protected open fun getColor(ctx: Context): Int {
        return if (isEnabled) {
            textColor.applyColor(ctx, R.attr.material_drawer_primary_text, R.color.material_drawer_primary_text)
        } else {
            disabledTextColor.applyColor(ctx, R.attr.material_drawer_hint_text, R.color.material_drawer_hint_text)
        }
    }

    /**
     * helper method to decide for the correct color
     *
     * @param ctx
     * @return
     */
    protected fun getSelectedTextColor(ctx: Context): Int {
        return selectedTextColor.applyColor(ctx, R.attr.material_drawer_selected_text, R.color.material_drawer_selected_text)
    }

    /**
     * helper method to decide for the ShapeAppearanceModel used for the drawable's corners
     *
     * @param ctx
     * @return
     */
    protected fun getShapeAppearanceModel(ctx: Context): ShapeAppearanceModel {
        val cornerRadius = ctx.resources.getDimensionPixelSize(R.dimen.material_drawer_item_corner_radius)
        val shapeAppearanceModel = ShapeAppearanceModel()
        shapeAppearanceModel.setCornerRadius(cornerRadius.toFloat())
        return shapeAppearanceModel
    }

    /**
     * helper to get the ColorStateList for the text and remembering it so we do not have to recreate it all the time
     *
     * @param color
     * @param selectedTextColor
     * @return
     */
    protected fun getTextColorStateList(@ColorInt color: Int, @ColorInt selectedTextColor: Int): ColorStateList? {
        if (colorStateList == null || color + selectedTextColor != colorStateList?.first) {
            colorStateList = Pair(color + selectedTextColor, DrawerUIUtils.getTextColorStateList(color, selectedTextColor))
        }
        return colorStateList?.second
    }
}
