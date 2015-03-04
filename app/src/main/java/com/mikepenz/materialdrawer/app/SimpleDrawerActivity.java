package com.mikepenz.materialdrawer.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.model.interfaces.Tagable;

public class SimpleDrawerActivity extends ActionBarActivity {

    private AccountHeader.Result headerResult = null;
    private Drawer.Result result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        IProfile profile = new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile));
        IProfile profile2 = new ProfileDrawerItem().withName("Max Muster").withEmail("max.mustermann@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile2));
        IProfile profile3 = new ProfileDrawerItem().withName("Felix House").withEmail("felix.house@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile3));
        IProfile profile4 = new ProfileDrawerItem().withName("Mr. X").withEmail("mister.x.super@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile4));
        IProfile profile5 = new ProfileDrawerItem().withName("Batman").withEmail("batman@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile5));

        headerResult = new AccountHeader()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        profile,
                        profile2,
                        profile3,
                        profile4,
                        profile5,
                        //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
                        new ProfileSettingDrawerItem().withEmail("Add Account").withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_add).actionBarSize().paddingDp(5)).withIdentifier(1),
                        new ProfileSettingDrawerItem().withEmail("Manage Account").withIcon(GoogleMaterial.Icon.gmd_settings)
                )
                .withOnAccountHeaderClickListener(new AccountHeader.OnAccountHeaderClickListener() {
                    @Override
                    public void onProfileClick(View view, IProfile profile) {
                        Toast.makeText(SimpleDrawerActivity.this, profile.getName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSelectionClick(IProfile currentProfile) {
                        if (!TextUtils.isEmpty(currentProfile.getName())) {
                            Toast.makeText(SimpleDrawerActivity.this, currentProfile.getName(), Toast.LENGTH_SHORT).show();
                        }

                        if (currentProfile instanceof IDrawerItem && ((IDrawerItem) currentProfile).getIdentifier() == 1) {

                            IProfile newProfile = new ProfileDrawerItem().withName("Batman").withEmail("batman@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile5));
                            if (headerResult.getProfiles() != null) {
                                //we know that there are 2 setting elements. set the new profile above them ;)
                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
                            } else {
                                headerResult.addProfiles(newProfile);
                            }
                        }
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        result = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_action_bar).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_multi_drawer).withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(2).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_non_translucent_status_drawer).withIcon(FontAwesome.Icon.faw_eye).withIdentifier(3).withCheckable(false),
                        new SectionDrawerItem().withName(R.string.drawer_item_section_header),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog).withIdentifier(5).withTextColor(Color.RED),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_question).setEnabled(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github).withIdentifier(4).withCheckable(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(GoogleMaterial.Icon.gmd_format_color_fill).withTag("Bullhorn")
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem

                        if (drawerItem != null) {
                            if (drawerItem instanceof Nameable) {
                                Toast.makeText(SimpleDrawerActivity.this, SimpleDrawerActivity.this.getString(((Nameable) drawerItem).getNameRes()), Toast.LENGTH_SHORT).show();
                            }

                            if (drawerItem.getIdentifier() == 1) {
                                Intent intent = new Intent(SimpleDrawerActivity.this, ActionBarDrawerActivity.class);
                                SimpleDrawerActivity.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 2) {
                                Intent intent = new Intent(SimpleDrawerActivity.this, MultiDrawerActivity.class);
                                SimpleDrawerActivity.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 3) {
                                Intent intent = new Intent(SimpleDrawerActivity.this, SimpleNonTranslucentDrawerActivity.class);
                                SimpleDrawerActivity.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 4) {
                                new Libs.Builder().withFields(R.string.class.getFields()).withActivityTheme(R.style.MaterialDrawerTheme_ActionBar).start(SimpleDrawerActivity.this);
                            }

                            if (drawerItem instanceof Tagable && drawerItem.getTag() != null) {
                                String tag = (String) drawerItem.getTag();
                                Toast.makeText(SimpleDrawerActivity.this, "Tag set on item:" + tag, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        result.setSelectionByIdentifier(5);

        /*
        new Drawer()
                //set the activity so we can inflate layouts automatically
                .withActivity(this)
                //set the toolbar to use with the drawer. will allow special stuff like ActionBarDrawerToggle
                .withToolbar(toolbar)
                //set the layout for the drawer manually. normally handled by the library
                .withDrawerLayout(VIEW|RES)
                //set the gravity for the drawer DEFAULT: START
                .withDrawerGravity(Gravity.END)
                //set this if you use the translucent statusBar feature DEFAULT: true
                .withTranslucentStatusBar(true)
                //set this to disable the ActionBarDrawerToggle, or pass a custom ActionBarDrawerToggle DEFAULT: true
                .withActionBarDrawerToggle(BOOLEAN|ActionBarDrawerToggle)
                //set the header for the drawer
                .withHeader(VIEW|RES)
                //set this to enable the onClick callback for the header
                .withHeaderClickable(true)
                //set this to disable the divider after the header DEFAULT: true
                .withHeaderDivider(false)
                //set the footer for the drawer
                .withFooter(VIEW|RES)
                //set this to enable the onClick callback for the footer
                .withFooterClickable(true)
                //set this to disable the divider before the footer DEFAULT: true
                .withFooterDivider(false)
                //set the sticky footer for the drawer (this one is always visible)
                .withStickyFooter(VIEW|RES)
                //set this if you want a onClick event as soon as you call .build() for the initial set DEFAULT: false
                .withFireOnInitialOnClick(true)
                //set the initial selected item. this is the position of the item. NOT the identifier
                .withSelectedItem(0)
                //set this to pass a custom ListView to the drawer. normally handled by the library
                .withListView(VIEW)
                //set this to pass a custom BaseDrawerAdapter to the drawer. normally handled by the library
                .withAdapter(BaseDrawerAdapter)
                //set one of this parameters to set the items for the drawer. not required if you pass your own adapter or even your own listView
                .withDrawerItems().addDrawerItems()
                //set this to disable the auto-close of the drawer after onClick DEFAULT: true
                .withCloseOnClick(false)
                //set this to modify the delay to close the drawer. this is a "hack" to prevent lag after onClick DEFAULT: 150 / DISABLE: -1
                .withDelayOnDrawerClose(-1)
                //set one of these methods to set listeners for the drawer
                .withOnDrawerListener().withOnDrawerItemClickListener().withOnDrawerItemLongClickListener().withOnDrawerItemSelectedListener()
                //set this method if you got a savedInstance (find more details in the sample application)
                .withSavedInstance()
                //set the width of the drawer FROM RES/DP/PX (just use one)
                .withDrawerWidthRes(R.dimen.material_drawer_width)
                .withDrawerWidthDp(240)
                .withDrawerWidthPx(1000)
                //set this if you use an actionBar and want also a translucent statusBar (really rare scenario)
                .withTranslucentActionBarCompatibility(true)
                //use one of those methods to finalize the drawer and to build it. append to add a second drawer to an existing drawer
                .build()
                .append(Drawer.Result)
         */
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

}
