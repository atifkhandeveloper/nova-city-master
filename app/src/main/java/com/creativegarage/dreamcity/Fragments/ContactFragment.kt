package com.creativegarage.dreamcity.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.creativegarage.dreamcity.MyBrowser
import com.creativegarage.dreamcity.R
import com.creativegarage.dreamcity.databinding.FragmentContactBinding


class ContactFragment : Fragment() {

    private lateinit var binding: FragmentContactBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact, container, false)
        loadWebView()
        return binding.root
    }

    private fun loadWebView() {
        binding.webviewContact.webViewClient = MyBrowser()
        binding.webviewContact.settings.loadsImagesAutomatically = true;
        binding.webviewContact.settings.javaScriptEnabled = true;
        binding.webviewContact.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY;
        binding.webviewContact.loadUrl("https://www.google.com/maps/place/Dream+city+kharian/@32.828659,73.8426531,17z/data=!3m1!4b1!4m6!3m5!1s0x391fa915bf928bcd:0x13a002704749508f!8m2!3d32.8286545!4d73.845228!16s%2Fg%2F11kj0b7t6g?hl=en-US");
    }

}