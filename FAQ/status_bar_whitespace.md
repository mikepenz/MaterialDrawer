### Q: Why is there whitespace where the status bar should be while my theme is set as fullscreen ?
### A: 
You also need to add 
```java
'drawerBuilder.withFullScreen(true)'
```
when building your drawer.

Please refer to the following activity code within the samples:
[FullScreenDrawerActivity.java](https://github.com/mikepenz/MaterialDrawer/blob/develop/app/src/main/java/com/mikepenz/materialdrawer/app/FullscreenDrawerActivity.java)
