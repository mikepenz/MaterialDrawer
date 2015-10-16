# How do I create a Multi-pane layout with MaterialDrawer for tablets?

![Example of Multi-pane layout](http://developer.android.com/images/training/app-navigation-multiple-sizes-multipane-good.png "Multi-pane layout")

This can be achieved with the `.buildView` and `.getSlider` methods of `DrawerBuilder` and  `Drawer` respectively.

## Sample code
First, in your activity XML, wrap your content in a placeholder FrameLayout, called `container` in this example. 

Do the same thing with another FrameLayout (called `nav_tablet`) for the MaterialDrawer. We'll replace it with the `Drawer` at runtime.

```xml

<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <!-- Horizontal Linear Layout to display the Drawer on the left,
     and the app's contents on the right.-->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="horizontal">

        <FrameLayout
            android:id="@+id/nav_tablet"
            android:layout_width="@dimen/navigation_menu_width"
            android:layout_height="match_parent" />

        <!-- A layout with the contents of the app and the toolbar -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical">

            <include layout="@layout/toolbar" />

            <!-- FrameLayout with the real contents of the app,
             like a Fragment replaced programmatically.-->
            <FrameLayout
                android:id="@+id/container"
                android:layout_width="match_parent"
                android:layout_height="match_parent" />
                
        </LinearLayout>
    </LinearLayout>
</RelativeLayout>

```

Then in your Activity code build the Drawer in two different ways, depending on if the device is a Tablet or not.

```java
private Drawer drawer;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("Home");

    DrawerBuilder drawerBuilder = new DrawerBuilder()
            .withActivity(this)
            .withToolbar(toolbar)
            .addDrawerItems(item1);
            
    if (tabletSize) {
        //on Tablets
        this.drawer = drawerBuilder.buildView();
        ((ViewGroup) findViewById(R.id.nav_tablet)).addView(drawer.getSlider());
    } else {
        //on Smartphones
        this.drawer = drawerBuilder.build();
    }  
    //...and so on
}
    
```
