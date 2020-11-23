package com.mikepenz.materialdrawer.app.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.materialdrawer.app.R
import com.mikepenz.materialdrawer.app.databinding.FragmentSimpleSampleBinding
import com.mikepenz.materialdrawer.iconics.withIcon
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.withIdentifier
import com.mikepenz.materialdrawer.model.interfaces.withName


/**
 * A simple [Fragment] subclass.
 * This is just a demo fragment with a long scrollable view of editTexts. Don't see this as a reference for anything
 */
class SecondDrawerFragment : Fragment() {
    private var _binding: FragmentSimpleSampleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // don't look at this layout it's just a listView to show how to handle the keyboard
        _binding = FragmentSimpleSampleBinding.inflate(inflater, container, false)
        binding.title.text = arguments?.getString(KEY_TITLE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.slider.apply {
            itemAdapter.add(
                    PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                    PrimaryDrawerItem().withName(R.string.drawer_item_free_play).withIcon(FontAwesome.Icon.faw_gamepad),
                    PrimaryDrawerItem().withName(R.string.drawer_item_custom).withIcon(FontAwesome.Icon.faw_eye)
            )
            setSavedInstance(savedInstanceState)
            setSelection(1)
        }
    }

    override fun onSaveInstanceState(_outState: Bundle) {
        var outState = _outState
        //add the values which need to be saved from the drawer to the bundle
        outState = binding.slider.saveInstanceState(outState)
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
}