package com.example.makeMyBudget.authentication

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.makeMyBudget.initialScreens.FrontScreenFragment
import com.example.makeMyBudget.initialScreens.FrontScreenFragmentDirections
import com.example.makeMyBudget.mainScreen.viewModels.UserModel

class Navigate {
    companion object {
        private lateinit var sharedPreferences: SharedPreferences
        private lateinit var viewModel: UserModel

        fun action(fragment: Fragment) {
            sharedPreferences =
                fragment.requireActivity().getSharedPreferences("user_auth", Context.MODE_PRIVATE)

            registerUser(fragment)
        }

        fun specialAction(fragment: Fragment) {
            registerUser(fragment)
        }

        fun registerUser(fragment: Fragment) {
            viewModel = ViewModelProvider(fragment.requireActivity())[UserModel::class.java]
            viewModel.setUserID(sharedPreferences.getString("user_id", "")!!)


            val handler = Handler()
            handler.postDelayed({
                //navigating to Login Screen from Front screen

                viewModel.user.observe(fragment.viewLifecycleOwner) {
                    val action: NavDirections? = if (it != null) {
                        sharedPreferences.edit().putString("user_id", it.user_id).apply()
                        sharedPreferences.edit().putString("username", it.name).apply()
                        sharedPreferences.edit().putString("budget", it.budget.toString()).apply()

                        when (fragment) {
                            is FrontScreenFragment -> {
                                FrontScreenFragmentDirections.actionFrontScreenFragmentToMainScreenFragment()
                            }
                            is LoginScreenFragment -> {
                                LoginScreenFragmentDirections.actionLoginScreenFragmentToMainScreenFragment()
                            }
                            is RegisterScreenFragment -> {
                                RegisterScreenFragmentDirections.actionRegisterScreenFragmentToMainScreenFragment()
                            }
                            else -> null
                        }
                    } else {
                        if (fragment is LoginScreenFragment) {
                            LoginScreenFragmentDirections.actionLoginScreenFragmentToUserBudgetDetailsFragment()
                        } else {
                            RegisterScreenFragmentDirections.actionRegisterScreenFragmentToUserBudgetDetailsFragment()
                        }
                    }

                    NavHostFragment.findNavController(fragment).navigate(action!!)
                }
            }, 1000)
        }
    }
}