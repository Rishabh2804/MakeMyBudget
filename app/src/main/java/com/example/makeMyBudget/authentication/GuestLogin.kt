package com.example.makeMyBudget.authentication

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment

class GuestLogin {

    companion object {
        private lateinit var sharedPreferences: SharedPreferences

        fun login(fragment: Fragment) {
            sharedPreferences =
                fragment.requireActivity().getSharedPreferences("user_auth", Context.MODE_PRIVATE)

            sharedPreferences.edit().putString("user_id", "abcdefghijklmnopqrstuvwxyz12").apply()

            sharedPreferences.edit().putBoolean("isGuest", true).apply()
            sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()

            Navigate.action(fragment)

        }
    }

}