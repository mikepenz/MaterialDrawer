#MaterialDrawer  [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.mikepenz.materialdrawer/library/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.mikepenz.materialdrawer/library) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-MaterialDrawer-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1526)

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
You can try it out here [Google Play](https://play.google.com/store/apps/details?id=com.mikepenz.unsplash) (wall:splash an open source application which uses this drawer implementation)

##Screenshots
![Image](https://raw.githubusercontent.com/mikepenz/MaterialDrawer/master/DEV/screenshots/screenshot1_small.png)
![Image](https://raw.githubusercontent.com/mikepenz/MaterialDrawer/master/DEV/screenshots/screenshot2_small.png)

![Image](https://raw.githubusercontent.com/mikepenz/MaterialDrawer/master/DEV/screenshots/screenshot3_small.png)
![Image](https://raw.githubusercontent.com/mikepenz/MaterialDrawer/master/DEV/screenshots/screenshot4_small.png)


#Include in your project
##Using Maven
The MaterialDrawer Library is pushed to [Maven Central](http://search.maven.org/#search|ga|1|g%3A%22com.mikepenz.materialdrawer%22), so you just need to add the following dependency to your `build.gradle`.

```javascript
compile('com.mikepenz.materialdrawer:library:2.0.7@aar') {
	transitive = true
}
```

##How to use
Here's a quick overview what you have to do within your application.
You can find a detailed description of all methods in the [WIKI](https://github.com/mikepenz/MaterialDrawer/wiki).

###Minimal SetUp

####Code:
It's (theoretically) a one-liner :D. This will create an empty drawer.
```java
new Drawer().withActivity(this).build()
```

###Activity with Toolbar
####Code:
```java
Drawer.Result result = new Drawer()
    .withActivity(this)
    .withToolbar(toolbar)
    .addDrawerItems(
	    new PrimaryDrawerItem().withName(R.string.drawer_item_home),
	    new DividerDrawerItem(),
	    new SecondaryDrawerItem().withName(R.string.drawer_item_settings)
    )
    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
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
headerResult = new AccountHeader()
	.withActivity(this)
    .withHeaderBackground(R.drawable.header)
	.addProfiles(
		new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile))
	)
    .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
		@Override
		public void onProfileChanged(View view, IProfile profile) { 
		}
	})
	.build();
                
//Now create your drawer and pass the AccountHeader.Result
Drawer.Result result = new Drawer()
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
    	// do something with the clicked item :D
    }
    })
    .build();

```

###Activity with ActionBar
####Code:
```java
new Drawer()
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
Drawer.Result result = new Drawer()
	.withActivity(this)
	.withToolbar(toolbar)
	.addDrawerItems(
		//pass your items here
	)
	.build();

new Drawer()
	.withActivity(this)
    .addDrawerItems(
    	//pass your items here
    )
    .withDrawerGravity(Gravity.END)
    .append(result);
```

###AndroidManifest.xml
Use one of the provided themes. They all use the AppCompat theme as parent and define the color values for the drawer.

**NOTE:** The theme states ActionBar and not NoActionBar like the Appcompat style

- MaterialDrawerTheme
- MaterialDrawerTheme.TranslucentStatus
- MaterialDrawerTheme.ActionBar
- MaterialDrawerTheme.ActionBar.TranslucentStatus
- MaterialDrawerTheme.Light
- MaterialDrawerTheme.Light.TranslucentStatus
- MaterialDrawerTheme.Light.ActionBar
- MaterialDrawerTheme.Light.ActionBar.TranslucentStatus
- MaterialDrawerTheme.Light.DarkToolbar
- MaterialDrawerTheme.Light.DarkToolbar.TranslucentStatus
- MaterialDrawerTheme.Light.DarkToolbar.ActionBar
- MaterialDrawerTheme.Light.DarkToolbar.ActionBar.TranslucentStatus
  

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
        <item name="material_drawer_window_background">@color/material_drawer_window_background</item>
        <item name="material_drawer_background">@color/material_drawer_background</item>
        <item name="material_drawer_icons">@color/material_drawer_icons</item>
        <item name="material_drawer_primary_text">@color/material_drawer_primary_text</item>
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
    <color name="material_drawer_window_background">#F9F9F9</color>
    <color name="material_drawer_background">#F9F9F9</color>
    <!-- Material DEFAULT text / items colors -->
    <color name="material_drawer_icons">#FFF</color>
    <color name="material_drawer_primary_text">#212121</color>
    <color name="material_drawer_secondary_text">#727272</color>
    <color name="material_drawer_hint_text">#B8B8B8</color>
    <color name="material_drawer_divider">#B6B6B6</color>
    <!-- Material DEFAULT drawer colors -->
    <color name="material_drawer_selected">#E8E8E8</color>
    <color name="material_drawer_selected_text">#2196F3</color>
    <color name="material_drawer_header_selection_text">#FFF</color>
    
    <!-- OVERWRITE THESE COLORS FOR A DARK THEME -->
    <!-- MaterialDrawer DEFAULT DARK colors -->
    <color name="material_drawer_dark_window_background">#303030</color>
    <color name="material_drawer_dark_background">#303030</color>
    <!-- MaterialDrawer DEFAULT DARK text / items colors -->
    <color name="material_drawer_dark_icons">#000</color>
    <color name="material_drawer_dark_primary_text">#FFF</color>
    <color name="material_drawer_dark_secondary_text">#DEDEDE</color>
    <color name="material_drawer_dark_hint_text">#ABABAB</color>
    <color name="material_drawer_dark_divider">#555555</color>
    <!-- MaterialDrawer DEFAULT DARK drawer colors -->
    <color name="material_drawer_dark_selected">#202020</color>
    <color name="material_drawer_dark_selected_text">@color/material_drawer_primary</color>
    <color name="material_drawer_dark_header_selection_text">#FFF</color>
```

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
