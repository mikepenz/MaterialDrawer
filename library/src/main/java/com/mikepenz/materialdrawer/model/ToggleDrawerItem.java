package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.model.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.util.PressedEffectStateListDrawable;
import com.mikepenz.materialdrawer.util.UIUtils;

/**
 * Created by mikepenz on 03.02.15.
 */
public class ToggleDrawerItem extends BaseDrawerItem<ToggleDrawerItem> {
    private String description;
    private int descriptionRes = -1;

    private boolean toggleEnabled = true;

    private boolean checked = false;
    private OnCheckedChangeListener onCheckedChangeListener = null;

    public ToggleDrawerItem withDescription(String description) {
        this.description = description;
        return this;
    }

    public ToggleDrawerItem withDescription(int descriptionRes) {
        this.descriptionRes = descriptionRes;
        return this;
    }

    public ToggleDrawerItem withChecked(boolean checked) {
        this.checked = checked;
        return this;
    }

    public ToggleDrawerItem withToggleEnabled(boolean toggleEnabled) {
        this.toggleEnabled = toggleEnabled;
        return this;
    }

    public ToggleDrawerItem withOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDescriptionRes() {
        return descriptionRes;
    }

    public void setDescriptionRes(int descriptionRes) {
        this.descriptionRes = descriptionRes;
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
    public String getType() {
        return "TOGGLE_ITEM";
    }

    @Override
    public int getLayoutRes() {
        return R.layout.material_drawer_item_toggle;
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

        int selected_color = getSelectedColor();
        if (selected_color == 0 && getSelectedColorRes() != -1) {
            selected_color = ctx.getResources().getColor(getSelectedColorRes());
        } else if (selected_color == 0) {
            selected_color = UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_selected, R.color.material_drawer_selected);
        }
        UIUtils.setBackground(viewHolder.view, UIUtils.getDrawerItemBackground(selected_color));

        if (this.getNameRes() != -1) {
            viewHolder.name.setText(this.getNameRes());
        } else {
            viewHolder.name.setText(this.getName());
        }

        viewHolder.description.setVisibility(View.VISIBLE);
        if (this.getDescriptionRes() != -1) {
            viewHolder.description.setText(this.getDescriptionRes());
        } else if (this.getDescription() != null) {
            viewHolder.description.setText(this.getDescription());
        } else {
            viewHolder.description.setVisibility(View.GONE);
        }


        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleEnabled) {
                    viewHolder.toggle.setChecked(!viewHolder.toggle.isChecked());
                }
            }
        });
        viewHolder.toggle.setChecked(checked);
        viewHolder.toggle.setOnCheckedChangeListener(checkedChangeListener);
        viewHolder.toggle.setEnabled(toggleEnabled);

        //get the correct color for the text
        int color;
        int selected_text = getSelectedTextColor();
        if (selected_text == 0 && getSelectedTextColorRes() != -1) {
            selected_text = ctx.getResources().getColor(getSelectedTextColorRes());
        } else if (selected_text == 0) {
            selected_text = UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_selected_text, R.color.material_drawer_selected_text);
        }
        if (this.isEnabled()) {
            color = getTextColor();
            if (color == 0 && getTextColorRes() != -1) {
                color = ctx.getResources().getColor(getTextColorRes());
            } else if (color == 0) {
                color = UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_primary_text, R.color.material_drawer_primary_text);
            }
        } else {
            color = getDisabledTextColor();
            if (color == 0 && getDisabledTextColorRes() != -1) {
                color = ctx.getResources().getColor(getDisabledTextColorRes());
            } else if (color == 0) {
                color = UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_hint_text, R.color.material_drawer_hint_text);
            }
        }

        //get the correct color for the icon
        int iconColor;
        int selected_icon = getSelectedIconColor();
        if (selected_icon == 0 && getSelectedIconColorRes() != -1) {
            selected_icon = ctx.getResources().getColor(getSelectedIconColorRes());
        } else if (selected_icon == 0) {
            selected_icon = UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_selected_text, R.color.material_drawer_selected_text);
        }
        if (this.isEnabled()) {
            iconColor = getIconColor();
            if (iconColor == 0 && getIconColorRes() != -1) {
                iconColor = ctx.getResources().getColor(getIconColorRes());
            } else if (iconColor == 0) {
                iconColor = UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_primary_icon, R.color.material_drawer_primary_icon);
            }
        } else {
            iconColor = getDisabledIconColor();
            if (iconColor == 0 && getDisabledIconColorRes() != -1) {
                iconColor = ctx.getResources().getColor(getDisabledIconColorRes());
            } else if (iconColor == 0) {
                iconColor = UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_hint_text, R.color.material_drawer_hint_text);
            }
        }

        viewHolder.name.setTextColor(UIUtils.getTextColor(color, selected_text));
        viewHolder.description.setTextColor(UIUtils.getTextColor(color, selected_text));

        if (getTypeface() != null) {
            viewHolder.name.setTypeface(getTypeface());
            viewHolder.description.setTypeface(getTypeface());
        }

        Drawable icon = null;
        Drawable selectedIcon = null;
        if (this.getIcon() != null) {
            icon = this.getIcon();

            if (this.getSelectedIcon() != null) {
                selectedIcon = this.getSelectedIcon();
            } else if (this.isSelectedIconTinted()) {
                icon = new PressedEffectStateListDrawable(icon, selected_icon);
            }
        } else if (this.getIIcon() != null) {
            icon = new IconicsDrawable(ctx, this.getIIcon()).color(iconColor).actionBarSize().paddingDp(1);
            selectedIcon = new IconicsDrawable(ctx, this.getIIcon()).color(selected_icon).actionBarSize().paddingDp(1);
        } else if (this.getIconRes() > -1) {
            icon = UIUtils.getCompatDrawable(ctx, getIconRes());

            if (this.getSelectedIconRes() > -1) {
                selectedIcon = UIUtils.getCompatDrawable(ctx, getSelectedIconRes());
            } else if (this.isSelectedIconTinted()) {
                icon = new PressedEffectStateListDrawable(icon, selected_icon);
            }
        }

        if (icon != null) {
            if (selectedIcon != null) {
                viewHolder.icon.setImageDrawable(UIUtils.getIconColor(icon, selectedIcon));
            } else {
                viewHolder.icon.setImageDrawable(icon);
            }

            viewHolder.icon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.icon.setVisibility(View.GONE);
        }

        return convertView;
    }

    private static class ViewHolder {
        private View view;
        private ImageView icon;
        private TextView name;
        private TextView description;
        private ToggleButton toggle;

        private ViewHolder(View view) {
            this.view = view;
            this.icon = (ImageView) view.findViewById(R.id.icon);
            this.name = (TextView) view.findViewById(R.id.name);
            this.description = (TextView) view.findViewById(R.id.description);
            this.toggle = (ToggleButton) view.findViewById(R.id.toggle);
        }
    }

    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (getOnCheckedChangeListener() != null) {
                getOnCheckedChangeListener().onCheckedChanged(ToggleDrawerItem.this, buttonView, isChecked);
            }
        }
    };
}
