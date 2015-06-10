package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.model.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.util.PressedEffectStateListDrawable;
import com.mikepenz.materialdrawer.util.UIUtils;

/**
 * Created by mikepenz on 03.02.15.
 */
public class SwitchDrawerItem extends BaseDrawerItem<SwitchDrawerItem> {
    private String description;
    private int descriptionRes = -1;

    private boolean switchEnabled = true;

    private boolean checkable = false;
    private boolean checked = false;
    private OnCheckedChangeListener onCheckedChangeListener = null;

    public SwitchDrawerItem withDescription(String description) {
        this.descriptionRes = -1;
        this.description = description;
        return this;
    }

    public SwitchDrawerItem withDescription(int descriptionRes) {
        this.description = null;
        this.descriptionRes = descriptionRes;
        return this;
    }

    public SwitchDrawerItem withChecked(boolean checked) {
        this.checked = checked;
        return this;
    }

    public SwitchDrawerItem withSwitchEnabled(boolean switchEnabled) {
        this.switchEnabled = switchEnabled;
        return this;
    }

    public SwitchDrawerItem withOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
        return this;
    }

    public SwitchDrawerItem withCheckable(boolean checkable) {
        this.checkable = checkable;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.descriptionRes = -1;
        this.description = description;
    }

    public int getDescriptionRes() {
        return descriptionRes;
    }

    public void setDescriptionRes(int descriptionRes) {
        this.description = null;
        this.descriptionRes = descriptionRes;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isSwitchEnabled() {
        return switchEnabled;
    }

    public void setSwitchEnabled(boolean switchEnabled) {
        this.switchEnabled = switchEnabled;
    }

    public OnCheckedChangeListener getOnCheckedChangeListener() {
        return onCheckedChangeListener;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    @Override
    public boolean isCheckable() {
        return checkable;
    }

    @Override
    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
    }

    @Override
    public String getType() {
        return "SWITCH_ITEM";
    }

    @Override
    public int getLayoutRes() {
        return R.layout.material_drawer_item_switch;
    }

    @Override
    public View convertView(LayoutInflater inflater, View convertView, ViewGroup parent) {
        Context ctx = parent.getContext();

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(getLayoutRes(), parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //get the correct color for the background
        int selectedColor = UIUtils.decideColor(ctx, getSelectedColor(), getSelectedColorRes(), R.attr.material_drawer_selected, R.color.material_drawer_selected);
        //get the correct color for the text
        int color;
        if (this.isEnabled()) {
            color = UIUtils.decideColor(ctx, getTextColor(), getTextColorRes(), R.attr.material_drawer_primary_text, R.color.material_drawer_primary_text);
        } else {
            color = UIUtils.decideColor(ctx, getDisabledTextColor(), getDisabledTextColorRes(), R.attr.material_drawer_hint_text, R.color.material_drawer_hint_text);
        }
        int selectedTextColor = UIUtils.decideColor(ctx, getSelectedTextColor(), getSelectedTextColorRes(), R.attr.material_drawer_selected_text, R.color.material_drawer_selected_text);
        //get the correct color for the icon
        int iconColor;
        if (this.isEnabled()) {
            iconColor = UIUtils.decideColor(ctx, getIconColor(), getIconColorRes(), R.attr.material_drawer_primary_icon, R.color.material_drawer_primary_icon);
        } else {
            iconColor = UIUtils.decideColor(ctx, getDisabledIconColor(), getDisabledIconColorRes(), R.attr.material_drawer_hint_text, R.color.material_drawer_hint_text);
        }
        int selectedIconColor = UIUtils.decideColor(ctx, getSelectedIconColor(), getSelectedIconColorRes(), R.attr.material_drawer_selected_text, R.color.material_drawer_selected_text);

        //set the background for the item
        UIUtils.setBackground(viewHolder.view, UIUtils.getDrawerItemBackground(selectedColor));

        //set the text for the name
        if (this.getNameRes() != -1) {
            viewHolder.name.setText(this.getNameRes());
        } else {
            viewHolder.name.setText(this.getName());
        }

        //set the text for the description or hide
        viewHolder.description.setVisibility(View.VISIBLE);
        if (this.getDescriptionRes() != -1) {
            viewHolder.description.setText(this.getDescriptionRes());
        } else if (this.getDescription() != null) {
            viewHolder.description.setText(this.getDescription());
        } else {
            viewHolder.description.setVisibility(View.GONE);
        }

        if (!isCheckable()) {
            viewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (switchEnabled) {
                        viewHolder.switchView.setChecked(!viewHolder.switchView.isChecked());
                    }
                }
            });
        }

        viewHolder.switchView.setChecked(checked);
        viewHolder.switchView.setOnCheckedChangeListener(checkedChangeListener);
        viewHolder.switchView.setEnabled(switchEnabled);

        //set the colors for textViews
        viewHolder.name.setTextColor(UIUtils.getTextColorStateList(color, selectedTextColor));
        viewHolder.description.setTextColor(UIUtils.getTextColorStateList(color, selectedTextColor));

        //define the typeface for our textViews
        if (getTypeface() != null) {
            viewHolder.name.setTypeface(getTypeface());
            viewHolder.description.setTypeface(getTypeface());
        }

        //get the drawables for our icon
        Drawable icon = UIUtils.decideIcon(ctx, getIcon(), getIIcon(), getIconRes(), iconColor, isIconTinted());
        Drawable selectedIcon = UIUtils.decideIcon(ctx, getSelectedIcon(), getIIcon(), getSelectedIconRes(), selectedIconColor, isIconTinted());

        //if we have an icon then we want to set it
        if (icon != null) {
            //if we got a different color for the selectedIcon we need a StateList
            if (selectedIcon != null) {
                viewHolder.icon.setImageDrawable(UIUtils.getIconStateList(icon, selectedIcon));
            } else if (isIconTinted()) {
                viewHolder.icon.setImageDrawable(new PressedEffectStateListDrawable(icon, iconColor, selectedIconColor));
            } else {
                viewHolder.icon.setImageDrawable(icon);
            }
            //make sure we display the icon
            viewHolder.icon.setVisibility(View.VISIBLE);
        } else {
            //hide the icon
            viewHolder.icon.setVisibility(View.GONE);
        }

        return convertView;
    }

    private static class ViewHolder {
        private View view;
        private ImageView icon;
        private TextView name;
        private TextView description;
        private SwitchCompat switchView;

        private ViewHolder(View view) {
            this.view = view;
            this.icon = (ImageView) view.findViewById(R.id.icon);
            this.name = (TextView) view.findViewById(R.id.name);
            this.description = (TextView) view.findViewById(R.id.description);
            this.switchView = (SwitchCompat) view.findViewById(R.id.switchView);
        }
    }

    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            checked = isChecked;

            if (getOnCheckedChangeListener() != null) {
                getOnCheckedChangeListener().onCheckedChanged(SwitchDrawerItem.this, buttonView, isChecked);
            }
        }
    };
}
