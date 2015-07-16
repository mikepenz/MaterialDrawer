package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.holder.ColorHolder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.utils.ViewHolderFactory;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;

/**
 * Created by mikepenz on 03.02.15.
 */
public class SwitchDrawerItem extends BaseDrawerItem<SwitchDrawerItem> {
    private StringHolder description;
    private ColorHolder descriptionTextColor;

    private boolean switchEnabled = true;

    private boolean checked = false;
    private OnCheckedChangeListener onCheckedChangeListener = null;

    public SwitchDrawerItem withDescription(String description) {
        this.description = new StringHolder(description);
        return this;
    }

    public SwitchDrawerItem withDescription(@StringRes int descriptionRes) {
        this.description = new StringHolder(descriptionRes);
        return this;
    }

    public SwitchDrawerItem withDescriptionTextColor(@ColorInt int color) {
        this.descriptionTextColor = ColorHolder.fromColor(color);
        return this;
    }

    public SwitchDrawerItem withDescriptionTextColorRes(@ColorRes int colorRes) {
        this.descriptionTextColor = ColorHolder.fromColorRes(colorRes);
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
        return withSelectable(checkable);
    }

    public StringHolder getDescription() {
        return description;
    }

    public ColorHolder getDescriptionTextColor() {
        return descriptionTextColor;
    }

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
    public String getType() {
        return "SWITCH_ITEM";
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_switch;
    }

    @Override
    public void bindView(RecyclerView.ViewHolder holder) {
        Context ctx = holder.itemView.getContext();

        //get our viewHolder
        final ViewHolder viewHolder = (ViewHolder) holder;

        //set the identifier from the drawerItem here. It can be used to run tests
        viewHolder.itemView.setId(getIdentifier());

        //set the item selected if it is
        viewHolder.itemView.setSelected(isSelected());

        //get the correct color for the background
        int selectedColor = getSelectedColor(ctx);
        //get the correct color for the text
        int color = getColor(ctx);
        int selectedTextColor = getSelectedTextColor(ctx);
        //get the correct color for the icon
        int iconColor = getIconColor(ctx);
        int selectedIconColor = getSelectedIconColor(ctx);

        //set the background for the item
        UIUtils.setBackground(viewHolder.view, DrawerUIUtils.getSelectableBackground(ctx, selectedColor));

        //set the text for the name
        StringHolder.applyTo(this.getName(), viewHolder.name);
        //set the text for the description or hide
        StringHolder.applyToOrHide(this.getDescription(), viewHolder.description);

        if (!isSelectable()) {
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
        viewHolder.name.setTextColor(getTextColorStateList(color, selectedTextColor));
        //set the description text color
        ColorHolder.applyToOr(getDescriptionTextColor(), viewHolder.description, getTextColorStateList(color, selectedTextColor));

        //define the typeface for our textViews
        if (getTypeface() != null) {
            viewHolder.name.setTypeface(getTypeface());
            viewHolder.description.setTypeface(getTypeface());
        }

        //get the drawables for our icon and set it
        Drawable icon = ImageHolder.decideIcon(getIcon(), ctx, iconColor, isIconTinted(), 1);
        Drawable selectedIcon = ImageHolder.decideIcon(getSelectedIcon(), ctx, selectedIconColor, isIconTinted(), 1);
        ImageHolder.applyMultiIconTo(icon, iconColor, selectedIcon, selectedIconColor, isIconTinted(), viewHolder.icon);

        //fix padding issues
        viewHolder.view.setPadding((int) UIUtils.convertDpToPixel(16, ctx), 0, (int) UIUtils.convertDpToPixel(16, ctx), 0);
    }

    @Override
    public ViewHolderFactory getFactory() {
        return new ItemFactory();
    }

    public static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder factory(View v) {
            return new ViewHolder(v);
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private ImageView icon;
        private TextView name;
        private TextView description;
        private SwitchCompat switchView;

        private ViewHolder(View view) {
            super(view);
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
