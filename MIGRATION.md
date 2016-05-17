###Upgrade Notes

#### v5.2.0 -> v5.2.1
* the `SecondaryDrawerItem` is now a subclass of the `PrimaryDrawerItem` (extends `PrimaryDrawerItem`). If you have an `if` which checks for the type with `instanceOf` make sure you check for the `SecondaryDrawerItem` first. (`secondaryDrawerItem instanceOf PrimaryDrawerItem == true`)

#### v5.1.6 -> 5.1.8
* if you use the `FastAdapter` please check out the release notes of v1.4.0 (https://github.com/mikepenz/FastAdapter/releases/tag/v1.4.0)

#### v5.0.0 -> 5.0.5
* the `expanding` functionality is now handled by the `FastAdapter` so the toggling code is no longer needed. See the following diff for the change (just the `DrawerActivity`) https://github.com/mikepenz/MaterialDrawer/commit/88e9bdf8cccaac5aaf567ac6ffe682aeccba4f29

#### v4.6.0 -> v5.0.0
* the identifier was changed from `int` to `long` as the internal adapter (FastAdapter) uses `long` to identify items (as the `Adapter` does)
* v5.0.0 no longer sets the `FULL_SCREEN` flag to get the drawer below the `StatusBar` it now uses the `fitsSystemWindows` everywhere. This should improve compatiblity with a lot of things like the `CoordinatorLayout` and should also improve compatiblity with future Android updates
* removed the following methods:
 * DrawerUIUtils.getScreenWidth -> moved to UIUtils from the `Materialize` library
 * DrawerBuilder.withTranslucentStatusBarProgrammatically -> no longer necessary as we now depend on the `fitsSystemWindows` flag
 * `StatusBarColor` can now be set via the `Drawer.getDrawerLayout().setStatusBarBackgroundColor(color)`
 * DrawerBuilder.keyboardSupportEnabled -> `KeyboardUtil` should no  longer be necessary
* `StatusBar` on **API < 21** is no longer colored, because of the changed way how we display the `Drawer` under the `StatusBar`
* `DrawerItems` changed. Please take a look at the `CustomDrawerItems` from the sample or the default ones, to add the changes to your `CustomDrawerItems`
* ...

#### v4.5.9 -> v4.6.0
* it is now possible to let the `Drawer` manage the `MiniDrawer`. Enable this via `withGenerateMiniDrawer(true)`. Afterwards remove the `MiniDrawer` calls inside the listeners, those are now done within the `Drawer`. You can get the `MiniDrawer` result object via `Drawer.getMiniDrawer();`

#### v4.3.7 -> v4.4.3
* added new method `withHeaderPadding` to the drawer and `withPaddingBelowHeader` to the header to control the padding separately from the `divider`which can be controlled via `withHeaderDivider`

#### v4.3.7
* depends on the latest `v23.1.0` **support libraries**. Those also require you to have `compileSDKVersion 23`

#### v4.2.0 -> v4.3.0
* new `placeholder(Context ctx, String tag)` to the `IDrawerImageLoader` interface
* new `AbstractDrawerImageLoader` to simplify the `DrawerImageLoader` usage. See the new implementation in the `CustomApplication`
* to keep the old behavior just change from `new DrawerImageLoader.IDrawerImageLoader() {` to `new AbstractDrawerImageLoader() {` for the `DrawerImageLoader.init`
* add new `tag` to the placeholder, to be able to define different placeholders for different targets

#### v4.2.0
* no more need to define an identifier for the items, they get one automatically. if you do not have logics which require you to do so, you are safe to forget about the identifier now.

#### v4.0.2 -> v4.0.7
* renamed `setDivider()` to `withDivider`
* remove `setTypeface()` use `withTypeface()` instead

#### v4.0.0 -> v4.0.2
* `getCurrentSelection()` will now return the `identifier` of the current selection or `null`
* `getCurrentSelectedPosition()` was added
* renamed all `*Footer*` methods to `*StickyFooter*` to prevent confusion

#### < v4.0.0

#####Common changes
* depends on the latest `v23` **support libraries**. Those also require you to have `compileSDKVersion 23`
* change the `onItemClick` listener to `onItemClick(View view, int i, IDrawerItem iDrawerItem)`
* modify the import of the `AccountHeader` and `AccountHeaderBuilder` to
```gradle
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
```
* the `identifier` should now be set for the `DrawerItems` as it is used now as default for all update/modify/.. actions
* rename `withCheckable()` to `withSelectable()`
* rename `set*` methods of the `DrawerItems` to `with*` methods as those were renamed
* rename all methods like `setSelection`, `setFooterSelection`, `removeItem`, ... to `*ByPosition` (added the **ByPosition**)
* rename all methods like `setSelectionByIdentifier`, `setFooterSelectionByIdentifier`, ... to `setSelection`, `setFooterSelection` (removed the **ByIdentifier**)
* change `updateName`, `updateIcon`, `updateBadge` those methods take now an `identifier` and the specific `Holder` object
* all `get*` methods of the `DrawerItems` will now return a `Holder` object for the specific type, making it easier to work with types like `String`, `StringRes`, `Color`, `ColorRes`, `ColorInt`, ..

#####Android-Iconics (icon font)
* the MaterialDrawer now only includes the `core` of the Android-Iconics project
 * add the fonts you use https://github.com/mikepenz/Android-Iconics#2-choose-your-desired-fonts
* pre MaterialDrawer v4.0.0 following fonts were included
```gradle
compile 'com.mikepenz:google-material-typeface:1.2.0.1@aar' //Google Material Design Icons
compile 'com.mikepenz:fontawesome-typeface:4.4.0.1@aar' //FontAwesome **NOTE:** the packagename changed for this font
```

#####Advanced usage changes
* changed the `ListView` to a `RecyclerView`
 * rename methods with `*ListView*` to `*RecyclerView*`
* the `IDrawerItem` interface was extended to better reflect a `RecyclerView` and to improve performance
 * added an `AbstractDrawerItem` to implement some common methods
 * see the [SectionDrawerItem](https://github.com/mikepenz/MaterialDrawer/blob/feature/refactoring/library/src/main/java/com/mikepenz/materialdrawer/model/SectionDrawerItem.java) for an easy example
