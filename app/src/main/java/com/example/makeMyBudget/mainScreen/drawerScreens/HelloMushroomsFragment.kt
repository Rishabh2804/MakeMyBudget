package com.example.makeMyBudget.mainScreen.drawerScreens

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.makemybudget.databinding.FragmentHelloMushroomsBinding

class HelloMushroomsFragment : Fragment() {

    lateinit var binding: FragmentHelloMushroomsBinding
    lateinit var sharedPreferences : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHelloMushroomsBinding.inflate(inflater, container, false)
        sharedPreferences = activity?.getSharedPreferences("user_auth", Context.MODE_PRIVATE)!!

        binding.userName.text = sharedPreferences.getString("username", "")

        binding.backToMainscreen.setOnClickListener {
            findNavController().navigate(
                HelloMushroomsFragmentDirections.actionHelloMushroomsFragmentToMainScreenFragment(0)
            )
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(
                    MyDetailsFragmentDirections.actionMyDetailsFragmentToMainScreenFragment(
                        0
                    )
                )
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)

        return binding.root
    }


}