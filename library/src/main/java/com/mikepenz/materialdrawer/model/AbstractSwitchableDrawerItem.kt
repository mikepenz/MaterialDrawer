package com.mikepenz.materialdrawer.model

import android.view.View
import android.widget.CompoundButton
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.SwitchCompat
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.R
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

/**
 * Created by mikepenz on 03.02.15.
 */
abstract class AbstractSwitchableDrawerItem<Item : AbstractSwitchableDrawerItem<Item>> : BaseDescribeableDrawerItem<Item, AbstractSwitchableDrawerItem.ViewHolder>() {

    var isSwitchEnabled = true
    var isChecked = false
    var onCheckedChangeListener: OnCheckedChangeListener? = null

    override val type: Int
        get() = R.id.material_drawer_item_primary_switch

    override val layoutRes: Int
        @LayoutRes
        get() = R.layout.material_drawer_item_switch

    private val checkedChangeListener = object : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton, ic: Boolean) {
            if (isEnabled) {
                isChecked = ic
                onCheckedChangeListener?.onCheckedChanged(this@AbstractSwitchableDrawerItem, buttonView, ic)
            } else {
                buttonView.setOnCheckedChangeListener(null)
                buttonView.isChecked = !ic
                buttonView.setOnCheckedChangeListener(this)
            }
        }
    }

    fun withChecked(checked: Boolean): Item {
        this.isChecked = checked
        return this as Item
    }

    fun withSwitchEnabled(switchEnabled: Boolean): Item {
        this.isSwitchEnabled = switchEnabled
        return this as Item
    }

    fun withOnCheckedChangeListener(onCheckedChangeListener: OnCheckedChangeListener): Item {
        this.onCheckedChangeListener = onCheckedChangeListener
        return this as Item
    }

    fun withCheckable(checkable: Boolean): Item {
        return withSelectable(checkable)
    }

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)

        //bind the basic view parts
        bindViewHelper(holder)

        //handle the switch
        holder.switchView.setOnCheckedChangeListener(null)
        holder.switchView.isChecked = isChecked
        holder.switchView.setOnCheckedChangeListener(checkedChangeListener)
        holder.switchView.isEnabled = isSwitchEnabled

        //add a onDrawerItemClickListener here to be able to check / uncheck if the drawerItem can't be selected
        withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
            override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*>): Boolean {
                if (!isSelectable) {
                    isChecked = !isChecked
                    holder.switchView.isChecked = isChecked
                }

                return false
            }
        })

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, holder.itemView)
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    open class ViewHolder internal constructor(view: View) : BaseViewHolder(view) {
        internal val switchView: SwitchCompat = view.findViewById<SwitchCompat>(R.id.material_drawer_switch)
    }
}
