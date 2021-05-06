package com.veen.homechefrider.fragment.side

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.veen.homechefrider.R
import com.veen.homechefrider.activity.Splash
import com.veen.homechefrider.databinding.FragmentLogoutBinding

class Logout : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_logout, container, false)
        val binding = DataBindingUtil.inflate<FragmentLogoutBinding>(inflater, R.layout.fragment_logout, container, false)

        requireContext().getSharedPreferences("Login", 0).edit().clear().apply()
        requireContext().getSharedPreferences("Token", 0).edit().clear().apply()

        startActivity(Intent(requireContext(), Splash::class.java))
        activity?.finish()

        return binding.root
    }
}