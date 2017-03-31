package com.mikepenz.materialdrawer.model;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.List;

/**
 * Created by mikepenz on 03.02.15.
 */
public class AbstractToggleableDrawerItem<Item extends AbstractToggleableDrawerItem> extends BaseDescribeableDrawerItem<Item, AbstractToggleableDrawerItem.ViewHolder> {
    private boolean toggleEnabled = true;

    private boolean checked = false;
    private OnCheckedChangeListener onCheckedChangeListener = null;

    public Item withChecked(boolean checked) {
        this.checked = checked;
        return (Item) this;
    }

    public Item withToggleEnabled(boolean toggleEnabled) {
        this.toggleEnabled = toggleEnabled;
        return (Item) this;
    }

    public Item withOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
        return (Item) this;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isToggleEnabled() {
        return toggleEnabled;
    }

    public void setToggleEnabled(boolean toggleEnabled) {
        this.toggleEnabled = toggleEnabled;
    }

    public OnCheckedChangeListener getOnCheckedChangeListener() {
        return onCheckedChangeListener;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    @Override
    public int getType() {
        return R.id.material_drawer_item_primary_toggle;
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_toggle;
    }

    @Override
    public void bindView(final ViewHolder viewHolder, List payloads) {
        super.bindView(viewHolder, payloads);

        //bind the basic view parts
        bindViewHelper(viewHolder);

        //handle the toggle
        viewHolder.toggle.setOnCheckedChangeListener(null);
        viewHolder.toggle.setChecked(checked);
        viewHolder.toggle.setOnCheckedChangeListener(checkedChangeListener);
        viewHolder.toggle.setEnabled(toggleEnabled);

        //add a onDrawerItemClickListener here to be able to check / uncheck if the drawerItem can't be selected
        withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                if (!isSelectable()) {
                    checked = !checked;
                    viewHolder.toggle.setChecked(checked);
                }

                return false;
            }
        });

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, viewHolder.itemView);
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    public static class ViewHolder extends BaseViewHolder {
        private ToggleButton toggle;

        private ViewHolder(View view) {
            super(view);
            this.toggle = (ToggleButton) view.findViewById(R.id.material_drawer_toggle);
        }
    }

    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isEnabled()) {
                checked = isChecked;
                if (getOnCheckedChangeListener() != null) {
                    getOnCheckedChangeListener().onCheckedChanged(AbstractToggleableDrawerItem.this, buttonView, isChecked);
                }
            } else {
                buttonView.setOnCheckedChangeListener(null);
                buttonView.setChecked(!isChecked);
                buttonView.setOnCheckedChangeListener(checkedChangeListener);
            }
        }
    };
}
