### Q: How to open the drawer from an instrumental test written with Espresso?
### A: 

First, you need a add `espresso-contrib` to your project. It has the needed `DrawerActions` class.

`androidTestCompile 'com.android.support.test.espresso:espresso-contrib:2.2.2'`

Then, you need to open the drawer with his `openDrawer()` method and the drawer layout ID. The generated one is `R.id.material_drawer_layout`

`onView(withId(R.id.material_drawer_layout)).perform(DrawerActions.open());`
