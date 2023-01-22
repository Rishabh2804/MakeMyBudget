package com.example.makeMyBudget.authentication

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import com.example.makeMyBudget.authentication.GuestLogin.GUEST_USER_ID
import com.example.makeMyBudget.initialScreens.FrontScreenFragment
import com.example.makeMyBudget.initialScreens.FrontScreenFragmentDirections
import com.example.makeMyBudget.mainScreen.viewModels.UserModel

object Navigate {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userModel: UserModel

    fun action(fragment: Fragment) {
        sharedPreferences =
            fragment.requireActivity().getSharedPreferences("user_auth", Context.MODE_PRIVATE)

        userModel = ViewModelProvider(fragment.requireActivity())[UserModel::class.java]

        val userID = sharedPreferences.getString("user_id", "")!!
        userModel.setUserID(userID)

        val action: NavDirections =
            if (userID == GUEST_USER_ID) {
                actionGuestUser(fragment)
            } else {
                userModel.user?.let { user ->
                    with(sharedPreferences.edit()) {
                        putString("user_id", userID)
                        putString("username", user.username)
                        putString("budget", user.budget)
                    }.apply()

                    actionRegisteredUser(fragment)
                } ?: actionNewUser(fragment)
            }

        NavHostFragment.findNavController(fragment).navigate(action)
    }

    fun actionRegisteredUser(fragment: Fragment): NavDirections {
        return when (fragment) {
            is FrontScreenFragment -> {
                FrontScreenFragmentDirections.actionFrontScreenFragmentToMainScreenFragment()
            }
            is RegisterScreenFragment -> {
                RegisterScreenFragmentDirections.actionRegisterScreenFragmentToMainScreenFragment()
            }
            is LoginScreenFragment -> {
                LoginScreenFragmentDirections.actionLoginScreenFragmentToMainScreenFragment()
            }
            else -> {
                throw Exception("Unknown fragment")
            }
        }
    }

    private fun actionNewUser(fragment: Fragment): NavDirections {
        return when (fragment) {
            is FrontScreenFragment -> {
                FrontScreenFragmentDirections.actionFrontScreenFragmentToUserBudgetDetailsFragment()
            }
            is RegisterScreenFragment -> {
                RegisterScreenFragmentDirections.actionRegisterScreenFragmentToUserBudgetDetailsFragment()
            }
            is LoginScreenFragment -> {
                LoginScreenFragmentDirections.actionLoginScreenFragmentToUserBudgetDetailsFragment()
            }
            else -> {
                throw Exception("Unknown fragment")
            }
        }
    }

    fun actionGuestUser(fragment: Fragment): NavDirections {
        return when (fragment) {
            is FrontScreenFragment -> {
                if (sharedPreferences.getBoolean("allCheck", false))
                    FrontScreenFragmentDirections.actionFrontScreenFragmentToMainScreenFragment()
                else
                    FrontScreenFragmentDirections.actionFrontScreenFragmentToUserBudgetDetailsFragment()
            }
            is LoginScreenFragment -> {
                LoginScreenFragmentDirections.actionLoginScreenFragmentToUserBudgetDetailsFragment()
            }
            else -> {
                throw Exception("Unknown fragment")
            }
        }
    }

}
