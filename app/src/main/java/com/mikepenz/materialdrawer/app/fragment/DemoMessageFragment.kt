package com.mikepenz.materialdrawer.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mikepenz.materialdrawer.app.R
import com.mikepenz.materialdrawer.app.databinding.FragmentMessageSampleBinding

class DemoMessageFragment : Fragment() {

    private var _binding: FragmentMessageSampleBinding? = null
    private val binding get() = _binding!!

    private var message: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        message = arguments?.let { DemoMessageFragmentArgs.fromBundle(it).message }
        _binding = FragmentMessageSampleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.messageTextView.text = "$message"
        binding.btnFragmentHome.setOnClickListener { navigateTo(R.id.fragmentHome) }
        binding.btnFragment1.setOnClickListener { navigateTo(R.id.messageFragment1) }
        binding.btnFragment2.setOnClickListener { navigateTo(R.id.messageFragment2) }
        binding.btnFragment3.setOnClickListener { navigateTo(R.id.messageFragment3) }
        binding.btnPopup.setOnClickListener { navigateTo(R.id.action_global_fragmentHome) }
    }

    private fun navigateTo(destination: Int) {
        findNavController().navigate(destination)
    }
}