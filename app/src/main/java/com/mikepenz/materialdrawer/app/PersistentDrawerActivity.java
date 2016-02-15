package com.mikepenz.materialdrawer.app;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.mikepenz.crossfader.Crossfader;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.app.utils.CrossfadeWrapper;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialize.util.UIUtils;

public class PersistentDrawerActivity extends AppCompatActivity {
    private static final int PROFILE_SETTING = 1;

    //save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private MiniDrawer miniResult = null;
    private Crossfader crossFader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persistent_drawer);

        //Remove line to test RTL support
        // getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        //example how to implement a persistentDrawer as shown in the google material design guidelines
        //https://material-design.storage.googleapis.com/publish/material_v_4/material_ext_publish/0Bx4BSt6jniD7YVdKQlF3TEo2S3M/patterns_navdrawer_behavior_persistent2.png
        //https://www.google.com/design/spec/patterns/navigation-drawer.html#navigation-drawer-behavior

        // Handle Toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.drawer_item_persistent_compact_header);

        // Create a few sample profile
        final IProfile profile = new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(R.drawable.profile);
        final IProfile profile2 = new ProfileDrawerItem().withName("Max Muster").withEmail("max.mustermann@gmail.com").withIcon(R.drawable.profile2);
        final IProfile profile3 = new ProfileDrawerItem().withName("Felix House").withEmail("felix.house@gmail.com").withIcon(R.drawable.profile3);
        final IProfile profile4 = new ProfileDrawerItem().withName("Mr. X").withEmail("mister.x.super@gmail.com").withIcon(R.drawable.profile4);
        final IProfile profile5 = new ProfileDrawerItem().withName("Batman").withEmail("batman@gmail.com").withIcon(R.drawable.profile5);

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(true)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(new ColorDrawable(Color.parseColor("#FDFDFD")))
                .withHeightPx(UIUtils.getActionBarHeight(this))
                .withAccountHeader(R.layout.material_drawer_compact_persistent_header)
                .withTextColor(Color.BLACK)
                .addProfiles(
                        profile,
                        profile2,
                        profile3,
                        profile4,
                        profile5
                )
                .withSavedInstance(savedInstanceState)
                .build();

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_free_play).withIcon(FontAwesome.Icon.faw_gamepad),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_custom).withIcon(FontAwesome.Icon.faw_eye),
                        new SectionDrawerItem().withName(R.string.drawer_item_section_header),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_question).withEnabled(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_bullhorn)
                )
                .withGenerateMiniDrawer(true)
                .withSavedInstance(savedInstanceState)
                .buildView();

        // create the MiniDrawer and define the drawer and header to be used (it will automatically use the items from them)
        miniResult = result.getMiniDrawer().withIncludeSecondaryDrawerItems(true);

        //set the back arrow in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        //get the widths in px for the first and second panel
        int firstWidth = (int) com.mikepenz.crossfader.util.UIUtils.convertDpToPixel(300, this);
        int secondWidth = (int) com.mikepenz.crossfader.util.UIUtils.convertDpToPixel(72, this);

        //create and build our crossfader (see the MiniDrawer is also builded in here, as the build method returns the view to be used in the crossfader)
        crossFader = new Crossfader()
                .withContent(findViewById(R.id.crossfade_content))
                .withFirst(result.getSlider(), firstWidth)
                .withSecond(miniResult.build(this), secondWidth)
                .withSavedInstance(savedInstanceState)
                .build();

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        miniResult.withCrossFader(new CrossfadeWrapper(crossFader));

        //define and create the arrow ;)
        ImageView toggle = (ImageView) headerResult.getView().findViewById(R.id.material_drawer_account_header_toggle);
        //for RTL you would have to define the other arrow
        toggle.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_chevron_left).sizeDp(16).color(Color.BLACK));
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crossFader.crossFade();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        //add the values which need to be saved from the crossFader to the bundle
        outState = crossFader.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle the click on the back arrow click
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}
