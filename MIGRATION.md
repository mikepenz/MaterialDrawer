###Upgrade Notes
#### < v4.0.0

#####Common changes
* change the `onItemClick` listener to `onItemClick(View view, int i, IDrawerItem iDrawerItem)`
* modify the import of the `AccountHeader` and `AccountHeaderBuilder` to
 * `import com.mikepenz.materialdrawer.AccountHeader`
 * `import com.mikepenz.materialdrawer.AccountHeaderBuilder`
* the `identifier` should now be set for the `DrawerItems` as it is used now as default for all update/modify/.. actions
* rename `withCheckable()` to `withSelectable()`
* rename `set*` methods of the `DrawerItems` to `with*` methods as those were renamed
* rename all methods like `setSelection`, `setFooterSelection`, `removeItem`, ... to `*ByPosition` (added the **ByPosition**)
* rename all methods like `setSelectionByIdentifier`, `setFooterSelectionByIdentifier`, ... to `setSelection`, `setFooterSelection` (removed the **ByIdentifier**)
* change `updateName`, `updateIcon`, `updateBadge` those methods take now an `identifier` and the specific `Holder` object

#####Android-Iconics (icon font)
* the MaterialDrawer now only includes the `core` of the Android-Iconics project
 * add the fonts you use https://github.com/mikepenz/Android-Iconics#2-choose-your-desired-fonts
* pre MaterialDrawer v4.0.0 following fonts were included
 * `compile 'com.mikepenz:google-material-typeface:1.2.0@aar'`
 * `compile 'com.mikepenz:fontawesome-typeface:4.4.0@aar'` **NOTE:** the packagename changed for this font

#####Advanced usage changes
* changed the `ListView` to a `RecyclerView`
 * rename methods with `*ListView*` to `*RecyclerView*`
* the `IDrawerItem` interface was extended to better reflect a `RecyclerView` and to improve performance
 * added an `AbstractDrawerItem` to implement some common methods
 * see the [SectionDrawerItem](https://github.com/mikepenz/MaterialDrawer/blob/feature/refactoring/library/src/main/java/com/mikepenz/materialdrawer/model/SectionDrawerItem.java) for an easy example