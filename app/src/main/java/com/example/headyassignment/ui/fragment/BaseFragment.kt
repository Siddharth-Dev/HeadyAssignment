package com.example.headyassignment.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.headyassignment.ui.MainViewModel

open class BaseFragment : Fragment() {

    protected var homeViewModel: MainViewModel? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            homeViewModel = ViewModelProviders.of(it)[MainViewModel::class.java]
        }
    }

    protected fun navigate(actionId: Int, arguments: Bundle?) {
        findNavController().navigate(actionId, arguments)
    }
}