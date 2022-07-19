package com.example.makeMyBudget.initialScreens

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.makemybudget.databinding.FragmentFrontScreenBinding


@Suppress("DEPRECATION")
class FrontScreenFragment : Fragment() {

    private lateinit var binding: FragmentFrontScreenBinding
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //initialising shared preferences and binding
        sharedPreferences = activity?.getSharedPreferences("user_auth", Context.MODE_PRIVATE)!!
        binding = FragmentFrontScreenBinding.inflate(inflater, container, false)

        //getting the variables isRegistered and isLoggedIn from shared preferences
        val isRegistered: Boolean = sharedPreferences.getBoolean("isRegistered", false)
        val isLoggedIn: Boolean = sharedPreferences.getBoolean("isLoggedIn", false)
        val allCheck: Boolean = sharedPreferences.getBoolean("allCheck", false)
        var action: NavDirections =
            FrontScreenFragmentDirections.actionFrontScreenFragmentToLoginScreenFragment()

        //if the user is registered and logged in, then navigate to the home screen
        if (isRegistered && isLoggedIn && allCheck) {
            action = FrontScreenFragmentDirections.actionFrontScreenFragmentToMainScreenFragment(0)
        }

        //if not all check, direct to the user budget detail fragment
        if (isRegistered && isLoggedIn && !allCheck) {
            action =
                FrontScreenFragmentDirections.actionFrontScreenFragmentToUserBudgetDetailsFragment()
        }

        //to hold the front screen for 5 seconds
        val handler = Handler()
        handler.postDelayed({
            //navigating to Login Screen from Front screen
            findNavController().navigate(action)
        }, 5000)
        return binding.root
    }


}