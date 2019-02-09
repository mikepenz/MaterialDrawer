package com.mikepenz.materialdrawer.app.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.app.R
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.SectionDrawerItem


/**
 * A simple [Fragment] subclass.
 * This is just a demo fragment with a long scrollable view of editTexts. Don't see this as a reference for anything
 */
class DrawerFragment : Fragment() {

    private var result: Drawer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // don't look at this layout it's just a listView to show how to handle the keyboard
        val view = inflater.inflate(R.layout.fragment_simple_sample, container, false)

        result = DrawerBuilder()
                .withActivity(activity!!)
                .withRootView(view.findViewById<View>(R.id.rootView) as ViewGroup)
                .withDisplayBelowStatusBar(false)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(
                        PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                        PrimaryDrawerItem().withName(R.string.drawer_item_free_play).withIcon(FontAwesome.Icon.faw_gamepad),
                        PrimaryDrawerItem().withName(R.string.drawer_item_custom).withIcon(FontAwesome.Icon.faw_eye),
                        SectionDrawerItem().withName(R.string.drawer_item_section_header),
                        SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog),
                        SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_question).withEnabled(false),
                        SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github),
                        SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_bullhorn)
                )
                .buildForFragment()

        val textView = view.findViewById<View>(R.id.title) as TextView
        textView.text = arguments?.getString(KEY_TITLE)

        result?.apply {
            drawerLayout.fitsSystemWindows = false
            slider.fitsSystemWindows = false
        }

        return view
    }

    override fun onSaveInstanceState(_outState: Bundle) {
        //add the values which need to be saved from the drawer to the bundle
        super.onSaveInstanceState(result?.saveInstanceState(_outState) ?: _outState)
    }

    companion object {
        private const val KEY_TITLE = "title"

        fun newInstance(title: String): DrawerFragment {
            return DrawerFragment().apply {
                arguments = Bundle().apply { putString(KEY_TITLE, title) }
            }
        }
    }
}
