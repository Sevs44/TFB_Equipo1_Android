package com.saulhervas.easychat.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.saulhervas.easychat.R
import com.saulhervas.easychat.databinding.FragmentDashboardFourthPageBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DashboardFourthPageFragment : Fragment() {

    private lateinit var binding: FragmentDashboardFourthPageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardFourthPageBinding.inflate(inflater, container, false)
        setOnClickListener()
        return binding.root
    }

    private fun setOnClickListener() {
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFourthPageFragment_to_dashboardFifthPageFragment)
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}