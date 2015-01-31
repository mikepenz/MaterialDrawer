#MaterialDrawer  [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.mikepenz.materialdrawer/library/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.mikepenz.materialdrawer/library)

The **MaterialDrawer** library aims to provide a quick and easy Activity to create and implement a drawer layout in your application.


It's compatible down to API Level 14
The **AboutLibraries** library allows you to easily create an **used open source libraries** fragment/activity within your app. As an extra feature you can also add an **about this app** section. 

Here's a quick overview of functions it include:
- **easy integration**
- quick and simple api
- modify the colors on the go
- **uses the AppCompat support library**
- comes with a basetheme which helps if you want an activity with a colored statusbar
- the navigationdrawer is also und the statusbar

#Screenshots
![Image](https://raw.githubusercontent.com/mikepenz/MaterialDrawer/master/DEV/screenshots/screenshot1_small.png)
![Image](https://raw.githubusercontent.com/mikepenz/MaterialDrawer/master/DEV/screenshots/screenshot2_small.png)


##Include in your project
###Using Maven
The MaterialDrawer Library is pushed to [Maven Central](http://search.maven.org/#search|ga|1|g%3A%22com.mikepenz.materialdrawer%22), so you just need to add the following dependency to your `build.gradle`.

```javascript
compile('com.mikepenz.materialdrawer:library:0.1.0@aar') {
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
```java
 extends BaseActivity
```
And implement following methods
```java
@Override
    public ArrayList<NavDrawerItem> getNavDrawerItems() {
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<NavDrawerItem>();
        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem("Home"));
        // SPACER
        navDrawerItems.add(new NavDrawerItem(NavDrawerItem.SPACER));
        // Settings
        navDrawerItems.add(new NavDrawerItem("Settings", FontAwesome.Icon.faw_cog, NavDrawerItem.SECONDARY));
        return navDrawerItems;
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    public void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new SampleFragment("Home");
                break;
            case 2:
                fragment = new SampleFragment("Settings");
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
            // update selected item and title, then close the drawer
            getDrawerListView().setItemChecked(position, true);
            getDrawerListView().setSelection(position);
            setTitle(navMenuTitles[position]);
            getDrawerLayout().closeDrawer(getSlider());
        }
    }
```

###AndroidManifest.xml
```xml
  android:theme="@style/AppTheme"
```

###styles.xml (OPTIONAL)
Overwrite following colors to create a quick custom style for your application
```
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
    <color name="material_drawer_list">#303030</color>
    <color name="material_drawer_list_secondary">#424242</color>
```

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
