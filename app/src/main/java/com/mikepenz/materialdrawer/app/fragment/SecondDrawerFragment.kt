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


/**
 * A simple [Fragment] subclass.
 * This is just a demo fragment with a long scrollable view of editTexts. Don't see this as a reference for anything
 */
class SecondDrawerFragment : Fragment() {

    private var result: Drawer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // don't look at this layout it's just a listView to show how to handle the keyboard
        val view = inflater.inflate(R.layout.fragment_simple_sample, container, false)

        result = DrawerBuilder()
                .withActivity(activity!!)
                .withRootView(view.findViewById<ViewGroup>(R.id.rootView))
                .withDisplayBelowStatusBar(false)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(
                        PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                        PrimaryDrawerItem().withName(R.string.drawer_item_free_play).withIcon(FontAwesome.Icon.faw_gamepad),
                        PrimaryDrawerItem().withName(R.string.drawer_item_custom).withIcon(FontAwesome.Icon.faw_eye)
                )
                .buildForFragment()

        val textView = view.findViewById<TextView>(R.id.title)
        textView.text = arguments!!.getString(KEY_TITLE)

        result?.apply {
            drawerLayout.fitsSystemWindows = false
            (slider as View).fitsSystemWindows = false
        }

        return view
    }

    override fun onSaveInstanceState(_outState: Bundle) {
        var outState = _outState
        //add the values which need to be saved from the drawer to the bundle
        outState = result?.saveInstanceState(outState) ?: outState
        super.onSaveInstanceState(outState)
    }

    companion object {
        private val KEY_TITLE = "title"

        fun newInstance(title: String): SecondDrawerFragment {
            return SecondDrawerFragment().apply {
                arguments = Bundle().apply { putString(KEY_TITLE, title) }
            }
        }
    }
}// Required empty public constructor
