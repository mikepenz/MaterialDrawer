### Developer:
Since there is MaterialDrawer in Support Library, could you give a couple of points why to use your library instead of from Support Library one?

### Author:
Well, that really depends on the use case of the drawer in this project, and which functionalities + which customizations are needed. 
The MaterialDrawer was created long before Google came up with their drawer in the design support libraries. 

Basically, the one of Google is definitely great and I recommend it to being used in all cases where it is enough. I am a fan of keeping
things as simple as possible, as it will have the great effect on the user experience. So if this project only needs a very minimal drawer
implementation, with just `Main` items, no `profile` functionality, and no great flexibility (no custom items, no custom styles, ... no advanced API for doing stuff programmatically) then you should definitely consider using the one from the design library. 

If you have a more advanced use case, like using the profile switcher, the profile page, custom items like different sizes, or checkboxes, etc then you might want to stay with my library. 
