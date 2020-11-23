package com.mikepenz.materialdrawer.app.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mikepenz.materialdrawer.app.databinding.FragmentSampleBinding


/**
 * A simple [Fragment] subclass.
 * This is just a demo fragment with a long scrollable view of editTexts. Don't see this as a reference for anything
 */
class DemoFragment : Fragment() {
    private var _binding: FragmentSampleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // don't look at this layout it's just a listView to show how to handle the keyboard
        _binding = FragmentSampleBinding.inflate(inflater, container, false)
        binding.title.text = arguments?.getString(KEY_TITLE)
        return binding.root
    }

    companion object {
        private val KEY_TITLE = "title"
        fun newInstance(title: String): DemoFragment {
            return DemoFragment().apply {
                arguments = Bundle().apply { putString(KEY_TITLE, title) }
            }
        }
    }
}
