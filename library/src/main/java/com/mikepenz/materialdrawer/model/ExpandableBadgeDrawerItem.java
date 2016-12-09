package com.mikepenz.materialdrawer.model;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.TextView;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.R;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.holder.ColorHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.icons.MaterialDrawerFont;
import com.mikepenz.materialdrawer.model.interfaces.ColorfulBadgeable;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import java.util.List;

/**
 * Created by mikepenz on 03.02.15.
 * NOTE: The arrow will just animate (and rotate) on APIs higher than 11 as the ViewCompat will skip this on API 10
 */
public class ExpandableBadgeDrawerItem extends BaseDescribeableDrawerItem<ExpandableBadgeDrawerItem, ExpandableBadgeDrawerItem.ViewHolder>
    implements ColorfulBadgeable<ExpandableBadgeDrawerItem> {

  private Drawer.OnDrawerItemClickListener mOnDrawerItemClickListener;

  protected ColorHolder arrowColor;

  protected int arrowRotationAngleStart = 0;

  protected int arrowRotationAngleEnd = 180;

  protected StringHolder mBadge;
  protected BadgeStyle mBadgeStyle = new BadgeStyle();

  @Override public int getType() {
    return R.id.material_drawer_item_expandable_badge;
  }

  @Override @LayoutRes public int getLayoutRes() {
    return R.layout.material_drawer_item_expandable_badge;
  }

  @Override public void bindView(ExpandableBadgeDrawerItem.ViewHolder viewHolder, List payloads) {
    super.bindView(viewHolder, payloads);

    Context ctx = viewHolder.itemView.getContext();
    //bind the basic view parts
    bindViewHelper(viewHolder);

    //set the text for the badge or hide
    boolean badgeVisible = StringHolder.applyToOrHide(mBadge, viewHolder.badge);
    //style the badge if it is visible
    if (true) {
      mBadgeStyle.style(viewHolder.badge, getTextColorStateList(getColor(ctx), getSelectedTextColor(ctx)));
      viewHolder.badgeContainer.setVisibility(View.VISIBLE);
    } else {
      viewHolder.badgeContainer.setVisibility(View.GONE);
    }

    //define the typeface for our textViews
    if (getTypeface() != null) {
      viewHolder.badge.setTypeface(getTypeface());
    }

    //make sure all animations are stopped
    viewHolder.arrow.setColor(this.arrowColor != null ? this.arrowColor.color(ctx) : getIconColor(ctx));
    viewHolder.arrow.clearAnimation();
    if (!isExpanded()) {
      ViewCompat.setRotation(viewHolder.arrow, this.arrowRotationAngleStart);
    } else {
      ViewCompat.setRotation(viewHolder.arrow, this.arrowRotationAngleEnd);
    }

    //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
    onPostBindView(this, viewHolder.itemView);
  }

  @Override public ExpandableBadgeDrawerItem withOnDrawerItemClickListener(Drawer.OnDrawerItemClickListener onDrawerItemClickListener) {
    mOnDrawerItemClickListener = onDrawerItemClickListener;
    return this;
  }

  @Override public Drawer.OnDrawerItemClickListener getOnDrawerItemClickListener() {
    return mOnArrowDrawerItemClickListener;
  }

  /**
   * our internal onDrawerItemClickListener which will handle the arrow animation
   */
  private Drawer.OnDrawerItemClickListener mOnArrowDrawerItemClickListener = new Drawer.OnDrawerItemClickListener() {
    @Override public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
      if (drawerItem instanceof AbstractDrawerItem && drawerItem.isEnabled()) {
        if (((AbstractDrawerItem) drawerItem).getSubItems() != null) {
          if (((AbstractDrawerItem) drawerItem).isExpanded()) {
            ViewCompat.animate(view.findViewById(R.id.material_drawer_arrow)).rotation(ExpandableBadgeDrawerItem.this.arrowRotationAngleEnd).start();
          } else {
            ViewCompat.animate(view.findViewById(R.id.material_drawer_arrow))
                .rotation(ExpandableBadgeDrawerItem.this.arrowRotationAngleStart)
                .start();
          }
        }
      }

      return mOnDrawerItemClickListener != null && mOnDrawerItemClickListener.onItemClick(view, position, drawerItem);
    }
  };

  @Override public ViewHolderFactory<ViewHolder> getFactory() {
    return new ItemFactory();
  }

  @Override public ExpandableBadgeDrawerItem withBadge(StringHolder badge) {
    this.mBadge = badge;
    return (ExpandableBadgeDrawerItem) this;
  }

  @Override public ExpandableBadgeDrawerItem withBadge(String badge) {
    this.mBadge = new StringHolder(badge);
    return (ExpandableBadgeDrawerItem) this;
  }

  @Override public ExpandableBadgeDrawerItem withBadge(@StringRes int badgeRes) {
    this.mBadge = new StringHolder(badgeRes);
    return (ExpandableBadgeDrawerItem) this;
  }

  @Override public ExpandableBadgeDrawerItem withBadgeStyle(BadgeStyle badgeStyle) {
    this.mBadgeStyle = badgeStyle;
    return (ExpandableBadgeDrawerItem) this;
  }

  public StringHolder getBadge() {
    return mBadge;
  }

  public BadgeStyle getBadgeStyle() {
    return mBadgeStyle;
  }

  public static class ItemFactory implements ViewHolderFactory<ViewHolder> {
    public ViewHolder create(View v) {
      return new ViewHolder(v);
    }
  }

  public static class ViewHolder extends BaseViewHolder {
    public IconicsImageView arrow;
    public View badgeContainer;
    public TextView badge;

    public ViewHolder(View view) {
      super(view);
      badgeContainer = view.findViewById(R.id.material_drawer_badge_container);
      badge = (TextView) view.findViewById(R.id.material_drawer_badge);
      arrow = (IconicsImageView) view.findViewById(R.id.material_drawer_arrow);
      arrow.setIcon(new IconicsDrawable(view.getContext(), MaterialDrawerFont.Icon.mdf_expand_more).sizeDp(16).paddingDp(2).color(Color.BLACK));
    }
  }
}
