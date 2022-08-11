package com.example.makeMyBudget.mainScreen.drawerScreens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.makemybudget.databinding.FragmentHelloMushroomsBinding

class HelloMushroomsFragment : Fragment() {

    lateinit var binding: FragmentHelloMushroomsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHelloMushroomsBinding.inflate(inflater, container, false)
        binding.backToMainscreen.setOnClickListener {
            findNavController().navigate(
                HelloMushroomsFragmentDirections.actionHelloMushroomsFragmentToMainScreenFragment(0)
            )
        }
        // Inflate the layout for this fragment
        return binding.root
    }


}