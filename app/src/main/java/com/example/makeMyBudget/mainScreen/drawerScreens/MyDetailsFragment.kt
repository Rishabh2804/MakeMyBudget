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
import com.example.makemybudget.databinding.FragmentMyDetailsBinding


class MyDetailsFragment : Fragment() {
    private val screenID = "DRAWER_USER_DETAILS"

    private lateinit var binding: FragmentMyDetailsBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyDetailsBinding.inflate(inflater, container, false)
        sharedPreferences = activity?.getSharedPreferences("user_auth", Context.MODE_PRIVATE)!!

        binding.username.text = sharedPreferences.getString("username", "")
        binding.budget.text = sharedPreferences.getString("budget", "")

        binding.editButton.setOnClickListener {
            findNavController().navigate(
                MyDetailsFragmentDirections.actionMyDetailsFragmentToEditMyDetailsFragment(

                )
            )
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
        return binding.root
    }

}