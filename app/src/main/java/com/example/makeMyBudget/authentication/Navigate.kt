package com.example.makeMyBudget.authentication

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import com.example.makeMyBudget.initialScreens.FrontScreenFragment
import com.example.makeMyBudget.initialScreens.FrontScreenFragmentDirections
import com.example.makeMyBudget.mainScreen.viewModels.UserModel

class Navigate {
    companion object {
        private lateinit var sharedPreferences: SharedPreferences
        private lateinit var userModel: UserModel

        fun action(fragment: Fragment) {
            sharedPreferences =
                fragment.requireActivity().getSharedPreferences("user_auth", Context.MODE_PRIVATE)

            userModel = ViewModelProvider(fragment.requireActivity())[UserModel::class.java]
            userModel.setUserID(sharedPreferences.getString("user_id", "not_found")!!)

            val handler = Handler()
            handler.postDelayed({
                //navigating to Login Screen from Front screen

                userModel.user.observe(fragment.viewLifecycleOwner) {
                    val action: NavDirections? = if (it != null) {
                        with(sharedPreferences) {
                            edit().putString("user_id", it.user_id).apply()
                            edit().putString("username", it.username).apply()
                            edit().putString("budget", it.budget.toString()).apply()
                        }

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
                        when (fragment) {
                            is FrontScreenFragment -> {
                                FrontScreenFragmentDirections.actionFrontScreenFragmentToUserBudgetDetailsFragment()
                            }
                            is LoginScreenFragment -> {
                                LoginScreenFragmentDirections.actionLoginScreenFragmentToUserBudgetDetailsFragment()
                            }
                            is RegisterScreenFragment -> {
                                RegisterScreenFragmentDirections.actionRegisterScreenFragmentToUserBudgetDetailsFragment()
                            }
                            else -> null
                        }
                    }

                    NavHostFragment.findNavController(fragment).navigate(action!!)
                }
            }, 1000)
        }
    }
}