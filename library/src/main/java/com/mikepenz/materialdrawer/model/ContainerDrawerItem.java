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

    private Position mViewPosition = Position.TOP;

    public ContainerDrawerItem withViewPosition(Position position) {
        this.mViewPosition = position;
        return this;
    }

    private boolean mDivider = true;

    public ContainerDrawerItem withDivider(boolean divider) {
        this.mDivider = divider;
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

        //make sure our view is not used in another parent
        if (mView.getParent() != null) {
            ((ViewGroup) mView.getParent()).removeView(mView);
        }

        //make sure the header view is empty
        ((ViewGroup) viewHolder.view).removeAllViews();

        int dividerHeight = 0;
        if (mDivider) {
            dividerHeight = 1;
        }

        View divider = new View(ctx);
        divider.setMinimumHeight(dividerHeight);
        divider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(ctx, R.attr.material_drawer_divider, R.color.material_drawer_divider));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) UIUtils.convertDpToPixel(dividerHeight, ctx));

        //depending on the position we add the view
        if (mViewPosition == Position.TOP) {
            ((ViewGroup) viewHolder.view).addView(mView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.bottomMargin = ctx.getResources().getDimensionPixelSize(R.dimen.material_drawer_padding);
            ((ViewGroup) viewHolder.view).addView(divider, layoutParams);
        } else if (mViewPosition == Position.BOTTOM) {
            layoutParams.topMargin = ctx.getResources().getDimensionPixelSize(R.dimen.material_drawer_padding);
            ((ViewGroup) viewHolder.view).addView(divider, layoutParams);
            ((ViewGroup) viewHolder.view).addView(mView);
        } else {
            ((ViewGroup) viewHolder.view).addView(mView);
        }

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, holder.itemView);
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

        private ViewHolder(View view) {
            super(view);
            this.view = view;
        }
    }
}
