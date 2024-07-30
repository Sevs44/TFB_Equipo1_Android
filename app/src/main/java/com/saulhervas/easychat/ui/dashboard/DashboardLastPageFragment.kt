package com.saulhervas.easychat.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.saulhervas.easychat.R
import com.saulhervas.easychat.databinding.FragmentDashboardLastPageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardLastPageFragment : Fragment() {

    private lateinit var binding: FragmentDashboardLastPageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardLastPageBinding.inflate(inflater, container, false)
        setOnClickListener()
        return binding.root
    }

    private fun setOnClickListener() {
        binding.btnStart.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardLastPageFragment_to_userLogin)
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}