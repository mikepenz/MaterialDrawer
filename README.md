#MaterialDrawer  [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.mikepenz.materialdrawer/library/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.mikepenz.materialdrawer/library) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-MaterialDrawer-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1526)

The **MaterialDrawer** library aims to provide a quick and easy Activity to create and implement a drawer layout in your application.

Here's a quick overview of functions it include:
- **easy integration**
- quick and simple api
- modify the colors on the go
- **uses the AppCompat support library**
- comes with a basetheme which helps if you want an activity with a colored statusbar
- the navigationdrawer is also under the statusbar
- compatible down to API Level 14

#Screenshots
![Image](https://raw.githubusercontent.com/mikepenz/MaterialDrawer/master/DEV/screenshots/screenshot1_small.png)
![Image](https://raw.githubusercontent.com/mikepenz/MaterialDrawer/master/DEV/screenshots/screenshot2_small.png)


##Include in your project
###Using Maven
The MaterialDrawer Library is pushed to [Maven Central](http://search.maven.org/#search|ga|1|g%3A%22com.mikepenz.materialdrawer%22), so you just need to add the following dependency to your `build.gradle`.

```javascript
compile('com.mikepenz.materialdrawer:library:0.9.1@aar') {
	transitive = true
}
```

##Upcoming
- Simplify usage
  - eliminate the requirement to set the fragment on-your-own (see the "How to use" instructions)
- Some cleanup
- Improved options for the drawer style itself

##How to use
Here's a quick overview what you have to do within your application.

####Activity
#####Code:
It's (theoretically) a one-liner :D
```java
new Drawer()
.withActivity(this)
.withToolbar(toolbar)
.withActionBarDrawerToggle(true)
.addDrawerItems(
	new PrimaryDrawerItem().withName(R.string.drawer_item_home),
	new PrimaryDrawerItem().withName(R.string.drawer_item_free_play),
	new PrimaryDrawerItem().withName(R.string.drawer_item_custom),
	new SpacerDrawerItem(),
	new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog),
	new SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_question).setEnabled(false),
	new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github),
	new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_bullhorn)
)
.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
@Override
public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
	// do something with the clicked item :D
}
})
.build();
```

###AndroidManifest.xml (OPTIONAL)
You can use the provided theme, this will allow the drawer to display under the statusbar
```xml
  android:theme="@style/AppTheme"
```

###styles.xml (OPTIONAL)
Overwrite following colors to create a quick custom style for your application
```xml
    <!-- Material SAMPLE DARK colors -->
    <color name="material_drawer_primary">#9C27B0</color>
    <color name="material_drawer_primary_dark">#7B1FA2</color>
    <color name="material_drawer_primary_light">#E1BEE7</color>
    <color name="material_drawer_accent">#00BCD4</color>
    <color name="material_drawer_background">#303030</color>
    <!-- Material SAMPLE DARK text / items colors -->
    <color name="material_drawer_icons">#000</color>
    <color name="material_drawer_primary_text">#FFF</color>
    <color name="material_drawer_secondary_text">#DEDEDE</color>
    <color name="material_drawer_hint_text">#ABABAB</color>
    <color name="material_drawer_divider">#555555</color>
    <!-- Material SAMPLE DARK drawer colors -->
    <color name="material_drawer_selected">#E8E8E8</color>
    <color name="material_drawer_selected_text">#2196F3</color>
```

#Credits

- Mirosław Stanek - [GitHub](https://github.com/frogermcs)
	- For his InstaMaterial concept and the idea of inflating the drawerLayout [InstaMaterial Concept](http://frogermcs.github.io/InstaMaterial-concept-part-7-navigation-drawer/)


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
