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
import com.example.makeMyBudget.authentication.Navigate
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

        val isLoggedIn: Boolean = sharedPreferences.getBoolean("isLoggedIn", false)

        //to hold the front screen for 5 seconds
        val handler = Handler()
        handler.postDelayed({
            if(isLoggedIn){
                Navigate.action(this)
            }
            else{
                findNavController().navigate(FrontScreenFragmentDirections.actionFrontScreenFragmentToLoginScreenFragment())
            }
        }, 500)

        return binding.root
    }


}