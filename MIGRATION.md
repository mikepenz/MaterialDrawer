###Upgrade Notes
#### < v4.0.0
* now most methods require you to have an identifier set for the DrawerItems
* it is higly recommended that you use the identifier
* some packages changed. re-import classes which are not found
* changed the `ListView` to a `RecyclerView`
* no more setters on the items. use the `with*` methods
* `withCheckable()` was renamed to `withSelectable()`
* most values of the DrawerItem's are now wrapped in `Holder` classes
* the `OnDrawerItemClickListener`was simplified to `onItemClick(View view, int position, IDrawerItem drawerItem)`
* the `IDrawerItem` interface was modified to now reflect the format required for a `RecyclerView`
* the position specific methods are now absolut and will also contain the count of the Header items (use identifier based methods)
* `*ByIdentifier` was removed from the methods to force their usage
* `addItem(IDrawerItem, pos)` was renamed to `addItemAtPosition`