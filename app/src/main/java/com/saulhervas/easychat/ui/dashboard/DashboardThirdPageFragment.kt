package com.saulhervas.easychat.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.saulhervas.easychat.R
import com.saulhervas.easychat.databinding.FragmentDashboardThirdPageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardThirdPageFragment : Fragment() {

    private lateinit var binding: FragmentDashboardThirdPageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardThirdPageBinding.inflate(inflater, container, false)
        setOnClickListener()
        return binding.root
    }

    private fun setOnClickListener() {
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardThirdPageFragment_to_dashboardFourthPageFragment)
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}