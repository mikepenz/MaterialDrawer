package com.mikepenz.materialdrawer.app.drawerItems;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.app.R;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.BaseDescribeableDrawerItem;
import com.mikepenz.materialdrawer.model.BaseViewHolder;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.List;

public class CheckboxDrawerItem extends BaseDescribeableDrawerItem<CheckboxDrawerItem, CheckboxDrawerItem.ViewHolder> {


    private boolean switchEnabled = true;

    private boolean checked = false;
    private OnCheckedChangeListener onCheckedChangeListener = null;


    public boolean isChecked() {
        return checked;
    }

    public boolean isSwitchEnabled() {
        return switchEnabled;
    }

    public OnCheckedChangeListener getOnCheckedChangeListener() {
        return onCheckedChangeListener;
    }

    @Override
    public int getType() {
        return R.layout.material_drawer_item_checkbox;
    }


    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_checkbox;
    }

    @Override
    public void bindView(final CheckboxDrawerItem.ViewHolder viewHolder, List payloads) {
        //bind the basic view parts
        bindViewHelper(viewHolder);

        //handle the switch
        viewHolder.checkBox.setOnCheckedChangeListener(null);
        viewHolder.checkBox.setChecked(checked);
        viewHolder.checkBox.setOnCheckedChangeListener(checkedChangeListener);
        viewHolder.checkBox.setEnabled(switchEnabled);

        //add a onDrawerItemClickListener here to be able to check / uncheck if the drawerItem can't be selected
        withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                if (!isSelectable()) {
                    checked = !checked;
                    viewHolder.checkBox.setChecked(checked);
                }

                return false;
            }
        });

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, viewHolder.itemView);
    }

    @Override
    public ViewHolderFactory<ViewHolder> getFactory() {
        return new ItemFactory();
    }

    public class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    public static class ViewHolder extends BaseViewHolder {
        private CheckBox checkBox;

        private ViewHolder(View view) {
            super(view);
            this.checkBox = (CheckBox) view.findViewById(R.id.material_drawer_checkbox);
        }
    }

    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isEnabled()) {
                checked = isChecked;
                if (getOnCheckedChangeListener() != null) {
                    getOnCheckedChangeListener().onCheckedChanged(CheckboxDrawerItem.this, buttonView, isChecked);
                }
            } else {
                buttonView.setOnCheckedChangeListener(null);
                buttonView.setChecked(!isChecked);
                buttonView.setOnCheckedChangeListener(checkedChangeListener);
            }
        }
    };
}