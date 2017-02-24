#MaterialDrawer  [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.mikepenz/materialdrawer/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.mikepenz/materialdrawer) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-MaterialDrawer-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1526) [![Join the chat at https://gitter.im/mikepenz/MaterialDrawer](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/mikepenz/MaterialDrawer?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

![MaterialDrawer the flexible, easy to use, all in one drawer library for your android project.](https://raw.githubusercontent.com/mikepenz/MaterialDrawer/develop/DEV/github/banner.png)

> Does your application **contain a Drawer**? Do you want to have it **up and running in less than 5 minutes**? Do you want your drawer to follow the **Android Design Guidelines**?
Do you have **profiles**? Do you need **flexibility**? Is Google's navigation Drawer of the **design support** not enough for you? Do you want a **simple and easy** to understand api?

If any (or all) of these questions seem familiar, the **MaterialDrawer** is the perfect library for you.

**Never** waste your time again.
It provides you with the easiest possible implementation of a navigation drawer for your application.
There  is a Header with profiles (**AccountHeader**), a **MiniDrawer** for Tablets (like Gmail), provide
**custom DrawerItems**, **custom colors**, **custom themes**, ... **No limits** for customizations.

###A quick overview what's in
- **the easiest possible integration**
- integrate in less than **5 minutes**
- compatible down to **API Level 14**
- includes an **AccountSwitcher**
- quick and simple api
- follows the **Google Material Design Guidelines**
- use **vector** (.svg) icons and **icon fonts** via the [Android-Iconics](https://github.com/mikepenz/Android-Iconics) integration
 - **Google Material Design Icons**, Google **Material Community** Design Icons, FontAwesome and more
- comes with various **themes** which help to get your own themes clean
- modify the colors on the go
- **uses the AppCompat support library**
- comes with multiple default drawer items
- based on a **RecyclerView**
- **RTL** support
- Gmail like **MiniDrawer**
- expandable items
- **badge** support
- define custom drawer items
- tested and **stable**
- sticky footer or headers
- **absolutely NO limits**

> If you upgrade from < 5.0.0 follow the [MIGRATION GUIDE](https://github.com/mikepenz/MaterialDrawer/blob/develop/MIGRATION.md)

#Preview
##Demo
You can try it out here [Google Play](https://play.google.com/store/apps/details?id=com.mikepenz.unsplash) (wall:splash an open source application which uses this drawer implementation). Or you try the [Sample Application](https://play.google.com/store/apps/details?id=com.mikepenz.materialdrawer.app)

##Screenshots
![Image](https://raw.githubusercontent.com/mikepenz/MaterialDrawer/develop/DEV/github/screenshots1.jpg)
![Image](https://raw.githubusercontent.com/mikepenz/MaterialDrawer/develop/DEV/github/screenshots2.jpg)

#WIKI / FAQ
You can find some frequently asked questions and other resources in the [WIKI / FAQ](FAQ.md) site.

#Setup
##1. Provide the gradle dependency

```gradle
compile('com.mikepenz:materialdrawer:5.8.2@aar') {
	transitive = true
}
```

##2. Add your drawer
```java
new DrawerBuilder().withActivity(this).build();
```

Great. Your drawer is now ready to use.


#Additional Setup
##Add items and adding some functionality

```java
//if you want to update the items at a later time it is recommended to keep it in a variable
PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_home);
SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_settings);

//create the drawer and remember the `Drawer` result object
Drawer result = new DrawerBuilder()
    .withActivity(this)
    .withToolbar(toolbar)
    .addDrawerItems(
	    item1,
	    new DividerDrawerItem(),
	    item2,
	    new SecondaryDrawerItem().withName(R.string.drawer_item_settings)
    )
    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
    	    // do something with the clicked item :D
        }
    })
    .build();
```

##Selecting an item
```java
//set the selection to the item with the identifier 1
result.setSelection(1);
//set the selection to the item with the identifier 2
result.setSelection(item2);
//set the selection and also fire the `onItemClick`-listener
result.setSelection(1, true);
```

By default, when a drawer item is clicked, it becomes the new selected item. If this isn't the expected behavior,
you can disable it for this item using `withSelectable(false)`:
```java
new SecondaryDrawerItem().withName(R.string.drawer_item_dialog).withSelectable(false)
```

##Modify items or the drawer

```java
//modify an item of the drawer
item1.withName("A new name for this drawerItem").withBadge("19").withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700));
//notify the drawer about the updated element. it will take care about everything else
result.updateItem(item1);

//to update only the name, badge, icon you can also use one of the quick methods
result.updateName(1, "A new name");

//the result object also allows you to add new items, remove items, add footer, sticky footer, ..
result.addItem(new DividerDrawerItem());
result.addStickyFooterItem(new PrimaryDrawerItem().withName("StickyFooterItem"));

//remove items with an identifier
result.removeItem(2);

//open / close the drawer
result.openDrawer();
result.closeDrawer();

//get the reference to the `DrawerLayout` itself
result.getDrawerLayout();
```

##Add profiles and an AccountHeader
```java
// Create the AccountHeader
AccountHeader headerResult = new AccountHeaderBuilder()
	.withActivity(this)
    .withHeaderBackground(R.drawable.header)
	.addProfiles(
		new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile))
	)
    .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
		@Override
		public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
		    return false;
		}
	})
	.build();

//Now create your drawer and pass the AccountHeader.Result
new DrawerBuilder()
    .withAccountHeader(headerResult)
    //additional Drawer setup as shown above
    ...
    .build();

```

##Use the included icon font
The MaterialDrawer comes with the `core` of the [Android-Iconics](https://github.com/mikepenz/Android-Iconics) library. This allows you to create your `DrawerItems` with an icon from any font.

Choose the fonts you need. [Available Fonts](https://github.com/mikepenz/Android-Iconics#2-choose-your-desired-fonts)
**build.gradle**
```gradle
compile 'com.mikepenz:google-material-typeface:x.y.z@aar' //Google Material Icons
compile 'com.mikepenz:fontawesome-typeface:x.y.z@aar'     //FontAwesome
```

**java**
```java
//now you can simply use any icon of the Google Material Icons font
new PrimaryDrawerItem().withIcon(GoogleMaterial.Icon.gmd_wb_sunny)
//Or an icon from FontAwesome
new SecondaryDrawerItem().withIcon(FontAwesome.Icon.faw_github)
```

#Advanced Setup
##Activity with ActionBar
###Code:
```java
new DrawerBuilder()
	.withActivity(this)
	.withTranslucentStatusBar(false)
    .withActionBarDrawerToggle(false)
	.addDrawerItems(
		//pass your items here
	)
	.build();
```

##Activity with Multiple Drawers
###Code:
```java
Drawer result = new DrawerBuilder()
	.withActivity(this)
	.withToolbar(toolbar)
	.addDrawerItems(
		//pass your items here
	)
	.build();

new DrawerBuilder()
	.withActivity(this)
    .addDrawerItems(
    	//pass your items here
    )
    .withDrawerGravity(Gravity.END)
    .append(result);
```

##Load images via url
The MaterialDrawer supports fetching images from URLs and setting them for the Profile icons. As the MaterialDrawer does not contain an ImageLoading library
the dev can choose his own implementation (Picasso, Glide, ...). This has to be done, before the first image should be loaded via URL. (Should be done in the Application, but any other spot before loading the first image is working too)
* SAMPLE using [PICASSO](https://github.com/square/picasso)
* [SAMPLE](https://github.com/mikepenz/MaterialDrawer/blob/develop/app/src/main/java/com/mikepenz/materialdrawer/app/CustomApplication.java) using [GLIDE](https://github.com/bumptech/glide)

###Code:
```java
//initialize and create the image loader logic
DrawerImageLoader.init(new AbstractDrawerImageLoader() {
    @Override
    public void set(ImageView imageView, Uri uri, Drawable placeholder) {
        Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
    }

    @Override
    public void cancel(ImageView imageView) {
        Picasso.with(imageView.getContext()).cancelRequest(imageView);
    }

    /*
    @Override
    public Drawable placeholder(Context ctx) {
        return super.placeholder(ctx);
    }

    @Override
    public Drawable placeholder(Context ctx, String tag) {
        return super.placeholder(ctx, tag);
    }
    */
});
```

An implementation with [GLIDE](https://github.com/mikepenz/MaterialDrawer/blob/develop/app/src/main/java/com/mikepenz/materialdrawer/app/CustomApplication.java#L42) can be found in the sample application


##Switching between Back-Arrow or Hamburger-Icon
If you use the included ActionBarDrawerToggle you can switch between back-arrow or hamburger-icon
with the following code snippet. (Please note that the order of these lines matter)
###Code - Show the back arrow:
```java
result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
getSupportActionBar().setDisplayHomeAsUpEnabled(true);
```
###Code - Show the hamburger icon:
```java
getSupportActionBar().setDisplayHomeAsUpEnabled(false);
result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
```


##AndroidManifest.xml
The **MaterialDrawer** requires an `AppCompat` theme or a derivative theme like the `MaterialDrawerTheme`s as base. It is highly recommended to use one of the provided themes. They all use the `AppCompat` theme as parent and define the color values for the drawer.

**NOTE:** The theme states ActionBar and not NoActionBar like the Appcompat style

- **MaterialDrawerTheme** (extends Theme.AppCompat.NoActionBar)
- **MaterialDrawerTheme.TranslucentStatus**
- **MaterialDrawerTheme.ActionBar** (extends Theme.AppCompat)
- **MaterialDrawerTheme.ActionBar.TranslucentStatus**
- **MaterialDrawerTheme.Light** (extends Theme.AppCompat.Light.NoActionBar)
- **MaterialDrawerTheme.Light.TranslucentStatus**
- **MaterialDrawerTheme.Light.ActionBar** (extends Theme.AppCompat.Light)
- **MaterialDrawerTheme.Light.ActionBar.TranslucentStatus**
- **MaterialDrawerTheme.Light.DarkToolbar** (extends Theme.AppCompat.DarkActionBar) (disabled the ActionBar)
- **MaterialDrawerTheme.Light.DarkToolbar.TranslucentStatus**
- **MaterialDrawerTheme.Light.DarkToolbar.ActionBar** (extends Theme.AppCompat.DarkActionBar)
- **MaterialDrawerTheme.Light.DarkToolbar.ActionBar.TranslucentStatus**
  

##Style the drawer
### Use of a none MaterialDrawer.* style
If you don't use one of the provided styles you have to add the style values to your style. Here's a simple sample. 
This is the same as the Custom style just with a parent like `parent="Theme.AppCompat.Light.DarkActionBar"`
### Custom style - styles.xml
Create your custom style and use one of the provided themes as parent. If you don't need a custom theme see the next section, how you can set the colors just by overwriting the original colors.

```xml
    <style name="CustomTheme" parent="MaterialDrawerTheme">
        <!-- ...and here we setting appcompat’s color theming attrs -->
        <item name="colorPrimary">@color/material_drawer_primary</item>
        <item name="colorPrimaryDark">@color/material_drawer_primary_dark</item>
        <item name="colorAccent">@color/material_drawer_accent</item>

        <!-- MaterialDrawer specific values -->
        <item name="material_drawer_background">@color/material_drawer_background</item>
        <item name="material_drawer_primary_text">@color/material_drawer_primary_text</item>
        <item name="material_drawer_primary_icon">@color/material_drawer_primary_icon</item>
        <item name="material_drawer_secondary_text">@color/material_drawer_secondary_text</item>
        <item name="material_drawer_hint_text">@color/material_drawer_hint_text</item>
        <item name="material_drawer_divider">@color/material_drawer_divider</item>
        <item name="material_drawer_selected">@color/material_drawer_selected</item>
        <item name="material_drawer_selected_text">@color/material_drawer_selected_text</item>
        <item name="material_drawer_header_selection_text">@color/material_drawer_header_selection_text</item>
    </style>
```

## Custom colors - colors.xml
No need to create a custom theme. Just set these colors (or some of them) and you have your own style.
```xml
	<!-- Material DEFAULT colors -->
    <color name="material_drawer_primary">#2196F3</color>
    <color name="material_drawer_primary_dark">#1976D2</color>
    <color name="material_drawer_primary_light">#BBDEFB</color>
    <color name="material_drawer_accent">#FF4081</color>
    
    <!-- OVERWRITE THESE COLORS FOR A LIGHT THEME -->
    <!-- MaterialDrawer DEFAULT colors -->
    <color name="material_drawer_background">#F9F9F9</color>
    <!-- Material DEFAULT text / items colors -->
    <color name="material_drawer_primary_text">#DE000000</color>
    <color name="material_drawer_primary_icon">#8A000000</color>
    <color name="material_drawer_secondary_text">#8A000000</color>
    <color name="material_drawer_hint_text">#42000000</color>
    <color name="material_drawer_divider">#1F000000</color>
    <!-- Material DEFAULT drawer colors -->
    <color name="material_drawer_selected">#E8E8E8</color>
    <color name="material_drawer_selected_text">#2196F3</color>
    <color name="material_drawer_header_selection_text">#FFF</color>
    
    <!-- OVERWRITE THESE COLORS FOR A DARK THEME -->
    <!-- MaterialDrawer DEFAULT DARK colors -->
    <color name="material_drawer_dark_background">#303030</color>
    <!-- MaterialDrawer DEFAULT DARK text / items colors -->
    <color name="material_drawer_dark_primary_text">#DEFFFFFF</color>
    <color name="material_drawer_dark_primary_icon">#8AFFFFFF</color>
    <color name="material_drawer_dark_secondary_text">#8AFFFFFF</color>
    <color name="material_drawer_dark_hint_text">#42FFFFFF</color>
    <color name="material_drawer_dark_divider">#1FFFFFFF</color>
    <!-- MaterialDrawer DEFAULT DARK drawer colors -->
    <color name="material_drawer_dark_selected">#202020</color>
    <color name="material_drawer_dark_selected_text">@color/material_drawer_primary</color>
    <color name="material_drawer_dark_header_selection_text">#FFF</color>
```

#FAQ
###How can i create a drawer without a default selection
```java
//just use this with the Drawer
.withSelectedItem(-1)
```

###I have problems with the SoftKeyboard. How can i fix this?
The MaterialDrawer will display your activity as FullScreen. Starting with API 19
the `adjustResize` works different then. This is default Android behavior. 
This is a big issue for a lot of devs so i've created a helper which "fixes" this issue. 
(It is recommend to just enable it for activities / fragments which need it)
```java
.keyboardSupportEnabled(activity, enabled)
```
A additional workaround is to disable the translucent StatusBar (This will break the
drawer to be displayed under the StatusBar). `.withTranslucentStatusBar(false)`

You can read about this here: https://github.com/mikepenz/MaterialDrawer/issues/95, https://github.com/mikepenz/MaterialDrawer/issues/183, https://github.com/mikepenz/MaterialDrawer/issues/196

###Can I lock the Drawer
As the MaterialDrawer will just create a normal DrawerLayout (with some magic around it) everything a normal
DrawerLayout can do is also available in the MaterialDrawer. 
```java
//get the DrawerLayout from the Drawer
DrawerLayout drawerLayout = result.getDrawerLayout();
//do whatever you want with the Drawer. Like locking it. 
drawerLayout.setDrawerLockMode(int lockMode); //or (int lockMode, int edgeGravity)
```

###Can I use my own DrawerLayout implementation
MaterialDrawer allows you to use a compatible implementation of MaterialDrawer.
Please note that the provided layout must follow the same structure as the `MaterialDrawer` internal one.

Start by copying the [`material_drawer.xml`](https://github.com/mikepenz/MaterialDrawer/blob/develop/library/src/main/res/layout/material_drawer.xml)
file inside your project, and replace `android.support.v4.widget.DrawerLayout` with the fully qualified name of your class
(`com.yourapp.com.ui.CustomDrawerLayout` for example). Please note that your class must extend the original DrawerLayout.

You'll then be able to use this custom class:
```java
builder.withDrawerLayout(R.layout.material_drawer);
```

#Apps using the MaterialDrawer
(feel free to send me new projects)

* [wall:splash](https://play.google.com/store/apps/details?id=com.mikepenz.unsplash)
* [GitSkarios](https://play.google.com/store/apps/details?id=com.alorma.github)
* [Screener](https://play.google.com/store/apps/details?id=de.toastcode.screener)
* [Academic Schedule](https://play.google.com/store/apps/details?id=com.auebcsschedule.ppt)
* [Strength](https://play.google.com/store/apps/details?id=com.e13engineering.strength)
* [Sprit Club](https://play.google.com/store/apps/details?id=at.idev.spritpreise)
* [FitHub](https://play.google.com/store/apps/details?id=com.gabilheri.fithub)
* [StickyNotes](https://play.google.com/store/apps/details?id=com.jsvmsoft.stickynotes)
* [Smartphone Italia](https://play.google.com/store/apps/details?id=rebus.smartphone.italia)
* [MLManager](https://github.com/javiersantos/MLManager)
* [Hold'Em Poker Manager](https://play.google.com/store/apps/details?id=pt.massena.holdemtracker.free)
* [Fimpl](https://play.google.com/store/apps/details?id=com.danielZET.fimpl)
* [+UEA](https://play.google.com/store/apps/details?id=br.edu.uea.app)
* [PixCell8](https://play.google.com/store/apps/details?id=com.pixcell8.prod)
* [TS3 Viewer for TeamSpeak 3](https://play.google.com/store/apps/details?id=com.game_state.ts3viewer)
* [Teacher Gradebook](https://play.google.com/store/apps/details?id=com.apolosoft.cuadernoprofesor)
* [Tabe3 News Reader](https://play.google.com/store/apps/details?id=com.tabe3.news)
* [Facepunch Droid](https://play.google.com/store/apps/details?id=com.apps.anker.facepunchdroid)
* [World Tourist Attractions](https://play.google.com/store/apps/details?id=indian.fig.whatsaround)
* [HipCar](https://play.google.com/store/apps/details?id=com.hipcar.android)
* [AS Sales Management](https://play.google.com/store/apps/details?id=com.armsoft.mtrade)
* [Sporza Voetbal](http://play.google.com/store/apps/details?id=be.vrt.mobile.android.sporza.voetbal)
* [Atmosphere](https://play.google.com/store/apps/details?id=com.peakpocketstudios.atmosphere)
* [Slidechat](https://play.google.com/store/apps/details?id=com.taddu.adfree.slidechat)
* [Fitness Challenge](https://play.google.com/store/apps/details?id=com.isidroid.fitchallenge)
* [European Capital of Culture - Pafos2017 official app](https://play.google.com/store/apps/details?id=com.trackandtalk.pafos17)
* [I'm Reading Quran - Kur'an Okuyorum](https://play.google.com/store/apps/details?id=com.homemade.kuranokuma)
* [Makota Money Manager](https://play.google.com/store/apps/details?id=be.jatra.makota)
* [Companion for Band](https://github.com/adithya321/Companion-for-Band)
* [Chisme for Sensu](https://play.google.com/store/apps/details?id=com.antonionicolaspina.sensu)
* [Recipedia](https://play.google.com/store/apps/details?id=com.md.recipedia)
* [Right Сourse - ruble course](https://play.google.com/store/apps/details?id=com.currency.work.currencychecker)
* [Gameru](https://play.google.com/store/apps/details?id=net.gameru)

#Articles about the MaterialDrawer
* [java-help.ru](http://java-help.ru/material-navigationdrawer/)


#Credits

- Mirosław Stanek - [GitHub](https://github.com/frogermcs)
	- For his InstaMaterial concept and the idea of inflating the drawerLayout [InstaMaterial Concept](http://frogermcs.github.io/InstaMaterial-concept-part-7-navigation-drawer/)

- Lunae Luman - [Behance](https://www.behance.net/gallery/18526001/Material-Wallpaper) for the Header Image

#Developed By

* Mike Penz 
 * [mikepenz.com](http://mikepenz.com) - <mikepenz@gmail.com>
 * [paypal.me/mikepenz](http://paypal.me/mikepenz)


#License

    Copyright 2016 Mike Penz

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
