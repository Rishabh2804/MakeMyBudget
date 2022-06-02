package com.example.makeMyBudget.initialScreens

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.makeMyBudget.databinding.FragmentFrontScreenBinding
import java.util.*

@Suppress("DEPRECATION")
class FrontScreenFragment : Fragment() {


    private lateinit var binding: FragmentFrontScreenBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedPreferences = activity?.getSharedPreferences("com.example.makeMyBudget", 0)!!
        val isRegistered: Boolean = sharedPreferences.getBoolean("isRegistered", false)
        val isLoggedIn: Boolean = sharedPreferences.getBoolean("isLoggedIn", false)
        val allCheck: Boolean = sharedPreferences.getBoolean("allCheck", false)

        var action: NavDirections = if (isRegistered) {
            if (!isLoggedIn)
                FrontScreenFragmentDirections.actionFrontScreenFragmentToLoginScreenFragment()
            else if (allCheck)
                FrontScreenFragmentDirections.actionFrontScreenFragmentToMainScreenFragment()
            else
                FrontScreenFragmentDirections.actionFrontScreenFragmentToUserBudgetDetailsFragment()

        } else {
            FrontScreenFragmentDirections.actionFrontScreenFragmentToRegisterScreenFragment()
        }

        val handler = Handler()
        handler.postDelayed({
            findNavController().navigate(action)
        }, 2000)
        // Inflate the layout for this fragment
        binding = FragmentFrontScreenBinding.inflate(inflater, container, false)
        action =
            FrontScreenFragmentDirections.actionFrontScreenFragmentToLoginScreenFragment()

        Handler().postDelayed({
            findNavController().navigate(action)
        }, 3000)

        return binding.root
    }


}