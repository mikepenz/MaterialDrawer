package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.model.utils.ViewHolderFactory;
import com.mikepenz.materialize.util.UIUtils;

/**
 * Created by mikepenz on 03.02.15.
 */
public class ContainerDrawerItem extends AbstractDrawerItem<ContainerDrawerItem> {
    private View mView;

    public ContainerDrawerItem withView(View view) {
        this.mView = view;
        return this;
    }

    public View getView() {
        return mView;
    }

    public enum Position {
        TOP,
        BOTTOM,
        NONE;
    }

    public Position mViewPosition = Position.TOP;

    public ContainerDrawerItem withViewPosition(Position position) {
        this.mViewPosition = position;
        return this;
    }

    public Position getViewPosition() {
        return mViewPosition;
    }

    @Override
    public String getType() {
        return "CONTAINER_ITEM";
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.material_drawer_item_container;
    }

    @Override
    public void bindView(RecyclerView.ViewHolder holder) {
        Context ctx = holder.itemView.getContext();

        //get our viewHolder
        ViewHolder viewHolder = (ViewHolder) holder;

        //set the identifier from the drawerItem here. It can be used to run tests
        holder.itemView.setId(getIdentifier());

        //define how the divider should look like
        viewHolder.view.setEnabled(false);

        //set the color for the divider
        viewHolder.divider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_divider, R.color.material_drawer_divider));
        viewHolder.divider.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHolder.divider.getLayoutParams();

        //make sure our view is not used in another parent
        if (mView.getParent() != null) {
            ((ViewGroup) mView.getParent()).removeView(mView);
        }

        //make sure the header view is empty
        ((ViewGroup) viewHolder.view).removeAllViews();

        //depending on the position we add the view
        if (mViewPosition == Position.TOP) {
            ((ViewGroup) viewHolder.view).addView(mView, 0);
            layoutParams.bottomMargin = ctx.getResources().getDimensionPixelSize(R.dimen.material_drawer_padding);
        } else if (mViewPosition == Position.BOTTOM) {
            ((ViewGroup) viewHolder.view).addView(mView);
            layoutParams.topMargin = ctx.getResources().getDimensionPixelSize(R.dimen.material_drawer_padding);
        } else {
            viewHolder.divider.setVisibility(View.GONE);
        }

        viewHolder.divider.setLayoutParams(layoutParams);
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
        private View divider;

        private ViewHolder(View view) {
            super(view);
            this.view = view;
            this.divider = view.findViewById(R.id.divider);
        }
    }
}
