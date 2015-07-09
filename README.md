#MaterialDrawer  [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.mikepenz/materialdrawer/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.mikepenz/materialdrawer) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-MaterialDrawer-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1526)

[![Join the chat at https://gitter.im/mikepenz/MaterialDrawer](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/mikepenz/MaterialDrawer?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

The **MaterialDrawer** library aims to provide the easiest possible implementation of a navigation drawer for your application. It provides a great amount of out of the box customizations and also includes an easy to use header which can be used as **AccountSwitcher**.


- **the easiest possible integration**
 - integrate in less then **5 minutes**
- includes an **AccountSwitcher**
 - Easy to use
 - No additional setup
 - Many additional usecases possible
 - Compact style included
- quick and simple api
- follows the **Google Material Design Guidelines**
- comes with various themes which help to get your own themes clean
- modify the colors on the go
- **uses the AppCompat support library**
- compatible down to **API Level 10**
- **supports multiple drawers**
- comes with multiple default drawer items
- **badge** support
- define custom drawer items
- tested and **stable**
- many many options how to display the drawer
 - Translucent StatusBar
 - Display Above the Toolbar
 - Display Under the Toolbar


#Preview
##Demo
You can try it out here [Google Play](https://play.google.com/store/apps/details?id=com.mikepenz.unsplash) (wall:splash an open source application which uses this drawer implementation). Or you try the [Sample Application](https://play.google.com/store/apps/details?id=com.mikepenz.materialdrawer.app)

##Screenshots
![Image](https://raw.githubusercontent.com/mikepenz/MaterialDrawer/master/DEV/screenshots/screenshot1_small.png)
![Image](https://raw.githubusercontent.com/mikepenz/MaterialDrawer/master/DEV/screenshots/screenshot2_small.png)

![Image](https://raw.githubusercontent.com/mikepenz/MaterialDrawer/master/DEV/screenshots/screenshot3_small.png)
![Image](https://raw.githubusercontent.com/mikepenz/MaterialDrawer/master/DEV/screenshots/screenshot4_small.png)


#Include in your project
##Using Maven
The MaterialDrawer Library is pushed to [Maven Central](http://search.maven.org/#search|ga|1|g%3A%22com.mikepenz%22), so you just need to add the following dependency to your `build.gradle`.

```javascript
compile('com.mikepenz:materialdrawer:3.1.2@aar') {
	transitive = true
}
```

##How to use
Here's a quick overview what you have to do within your application.
You can find a detailed description of all methods in the [WIKI](https://github.com/mikepenz/MaterialDrawer/wiki).


###Upgrade Notes
#### < v3.0.0
* There is no more `Drawer.Result`. To create a drawer use the new `DrawerBuilder` instead of the `Drawer`. The result is now a `Drawer`.
* There is no more `AccountHeader.Result`. To create a drawer use the new `AccountHeaderBuilder` instead of the `AccountHeader`. The result is now a `AccountHeader`.
* `new Drawer()` --> `new DrawerBuilder()`, `Drawer.Result` --> `Drawer`, `new AccountHeader()` --> `new AccountHeaderBuilder()`, `AccountHeader.Result` --> `AccountHeader`
* The `onDrawerItemClick` listener now returns a `boolean` defining if the event was consumed

#### < v2.9.8
Changed maven group. You can get all updates via the new one `com.mikepenz:materialdrawer:x.y.z@aar`

#### < v2.9.5
Tinting is now off by DEFAULT again. Enable it for your items with `withIconTintingEnabled(true)` if needed. This release now also tint's the normal state of the icon.

#### < v2.9.0
v2.9.0 now uses the latest com.android.support:appcompat version 22.1.1. Please update if you use an older version. It is now also required to set the theme within your layout for toolbars. (Especially for the DarkToolbar theme)
```java
<android.support.v7.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        android:background="?attr/colorPrimary"
        android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"
        app:popupTheme="@style/ThemeOverlay.AppCompat.Light"
        android:elevation="4dp"/>
```

#### < v2.8.0
v2.8.0 now uses the latest com.android.support:appcompat version 22.1.0. Please update if you use an older version.

###Minimal SetUp

####Code:
It's (theoretically) a one-liner :D. This will create an empty drawer.
```java
new DrawerBuilder().withActivity(this).build();
```

###Activity with Toolbar
####Code:
```java
Drawer result = new DrawerBuilder()
    .withActivity(this)
    .withToolbar(toolbar)
    .addDrawerItems(
	    new PrimaryDrawerItem().withName(R.string.drawer_item_home),
	    new DividerDrawerItem(),
	    new SecondaryDrawerItem().withName(R.string.drawer_item_settings)
    )
    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
    @Override
    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
    	// do something with the clicked item :D
    }
    })
    .build();

//use the result object to get different views of the drawer or modify it's data
//some sample calls
result.setSelectionByIdentifier(1);
result.openDrawer();
result.closeDrawer();
result.isDrawerOpen();
result.addItem(..);
..

```

###Drawer with AccountSwitcher
####Code:
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
Drawer result = new DrawerBuilder()
    .withActivity(this)
    .withToolbar(toolbar)
    .withAccountHeader(headerResult)
    .addDrawerItems(
	    new PrimaryDrawerItem().withName(R.string.drawer_item_home),
	    new DividerDrawerItem(),
	    new SecondaryDrawerItem().withName(R.string.drawer_item_settings)
    )
    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
    @Override
    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
    	// do something with the clicked item :D
    }
    })
    .build();

```

###Activity with ActionBar
####Code:
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

###Activity with Multiple Drawers
####Code:
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

###Load images via url
The MaterialDrawer supports fetching images from URLs and setting them for the Profile icons. As the MaterialDrawer does not contain an ImageLoading library
the dev can choose his own implementation (Picasso, Glide, ...). This has to be done, before the first image should be loaded via URL. (Should be done in the Application, but any other spot before loading the first image is working too)
####Code:
```java
//SAMPLE using [PICASSO](https://github.com/square/picasso)
//initialize and create the image loader logic
DrawerImageLoader.init(new DrawerImageLoader.IDrawerImageLoader() {
    @Override
    public void set(ImageView imageView, Uri uri, Drawable placeholder) {
        Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
    }

    @Override
    public void cancel(ImageView imageView) {
        Picasso.with(imageView.getContext()).cancelRequest(imageView);
    }

    @Override
    public Drawable placeholder(Context ctx) {
        return null;
    }
});
```


###Switching between Back-Arrow or Hamburger-Icon
If you use the included ActionBarDrawerToggle you can switch between back-arrow or hamburger-icon
with the following code snippet. (Please note that the order of these lines matter)
####Code - Show the back arrow:
```java
result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
getSupportActionBar().setDisplayHomeAsUpEnabled(true);
```
####Code - Show the hamburger icon:
```java
getSupportActionBar().setDisplayHomeAsUpEnabled(false);
result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
```


###AndroidManifest.xml
Use one of the provided themes. They all use the AppCompat theme as parent and define the color values for the drawer.

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
  

###Style the drawer
#### Use of a none MaterialDrawer.* style
If you don't use one of the provided styles you have to add the style values to your style. Here's a simple sample. 
This is the same as the Custom style just with a parent like `parent="Theme.AppCompat.Light.DarkActionBar"`
#### Custom style - styles.xml
Create your custom style and use one of the provided themes as parent. If you don't need a custom theme see the next section, how you can set the colors just by overwriting the original colors.

```xml
    <style name="CustomTheme" parent="MaterialDrawerTheme">
        <!-- ...and here we setting appcompat’s color theming attrs -->
        <item name="colorPrimary">@color/material_drawer_primary</item>
        <item name="colorPrimaryDark">@color/material_drawer_primary_dark</item>
        <item name="colorAccent">@color/material_drawer_accent</item>

        <!-- MaterialDrawer specific values -->
        <item name="material_drawer_background">@color/material_drawer_background</item>
        <item name="material_drawer_icons">@color/material_drawer_icons</item>
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

### Custom colors - colors.xml
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
    <color name="material_drawer_icons">#FFF</color>
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
    <color name="material_drawer_dark_icons">#000</color>
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

##FAQ
#####How can i create a drawer without a default selection
```java
//just use this with the Drawer
.withSelectedItem(-1)
```

#####How can i use this with espresso
```java
androidTestCompile ('com.android.support.test.espresso:espresso-contrib:2.0') {
//this library uses the newest app compat v22 but the espresso contrib still v21. 
//you have to specifically exclude the older verisions of the contrib library or
// there will be some conflicts
    exclude module: 'support-annotations'
    exclude module: 'support-v4'
    exclude module: 'recyclerview-v7'
}
```

#####I have problems with the SoftKeyboard. How can i fix this?
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

#####Can I lock the Drawer
As the MaterialDrawer will just create a normal DrawerLayout (with some magic around it) everything a normal
DrawerLayout can do is also available in the MaterialDrawer. 
```java
//get the DrawerLayout from the Drawer
DrawerLayout drawerLayout = result.getDrawerLayout();
//do whatever you want with the Drawer. Like locking it. 
drawerLayout.setDrawerLockMode(int lockMode); //or (int lockMode, int edgeGravity)
```


##Apps using the MaterialDrawer
(feel free to send me new projects)

* [wall:splash](https://play.google.com/store/apps/details?id=com.mikepenz.unsplash)

* [GitSkarios](https://play.google.com/store/apps/details?id=com.alorma.github)
* [Academic Schedule](https://play.google.com/store/apps/details?id=com.auebcsschedule.ppt)
* [Strength](https://play.google.com/store/apps/details?id=com.e13engineering.strength)
* [Sprit Club](https://play.google.com/store/apps/details?id=at.idev.spritpreise)
* [FitHub](https://play.google.com/store/apps/details?id=com.gabilheri.fithub)
* [StickyNotes](https://play.google.com/store/apps/details?id=com.jsvmsoft.stickynotes)
* [Smartphone Italia](https://play.google.com/store/apps/details?id=rebus.smartphone.italia)
* [MLManager](https://github.com/javiersantos/MLManager)
* [Hold'Em Poker Manager](https://play.google.com/store/apps/details?id=pt.massena.holdemtracker.free)



##Articles about the MaterialDrawer
* [java-help.ru](http://java-help.ru/material-navigationdrawer/)



#Credits

- Mirosław Stanek - [GitHub](https://github.com/frogermcs)
	- For his InstaMaterial concept and the idea of inflating the drawerLayout [InstaMaterial Concept](http://frogermcs.github.io/InstaMaterial-concept-part-7-navigation-drawer/)

- Lunae Luman - [Behance](https://www.behance.net/gallery/18526001/Material-Wallpaper) for the Header Image

#Developed By

* Mike Penz - http://mikepenz.com - <mikepenz@gmail.com>


#License

    Copyright 2015 Mike Penz

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
