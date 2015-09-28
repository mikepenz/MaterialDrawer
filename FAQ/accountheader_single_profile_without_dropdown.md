#How can i use the AccountHeader with just one profile and disable the dropdown?

This can be simply achieved by adding  `.withSelectionListEnabledForSingleProfile(false)` to the
`Builder` of the `AccountHeader`. This will then disable the dropdown and hide the arrow from the
header.

##Sample code

```java
.withSelectionListEnabledForSingleProfile(false)
```

##Links
https://github.com/mikepenz/MaterialDrawer/issues/615
https://github.com/mikepenz/MaterialDrawer/issues/454
https://github.com/mikepenz/MaterialDrawer/issues/443
https://github.com/mikepenz/MaterialDrawer/issues/350