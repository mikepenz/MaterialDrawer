package com.mikepenz.materialdrawer.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mikepenz.materialdrawer.app.R
import kotlinx.android.synthetic.main.fragment_message_sample.*

class DemoMessageFragment : Fragment() {

    private var message: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        message = arguments?.let { DemoMessageFragmentArgs.fromBundle(it).message }

        return inflater.inflate(R.layout.fragment_message_sample, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageTextView.text = "$message"

        btnFragmentHome.setOnClickListener { navigateTo(R.id.fragmentHome) }
        btnFragment1.setOnClickListener { navigateTo(R.id.messageFragment1) }
        btnFragment2.setOnClickListener { navigateTo(R.id.messageFragment2) }
        btnFragment3.setOnClickListener { navigateTo(R.id.messageFragment3) }
        btnPopup.setOnClickListener { navigateTo(R.id.action_global_fragmentHome) }
    }

    private fun navigateTo(destination: Int) {
        findNavController().navigate(destination)
    }
}