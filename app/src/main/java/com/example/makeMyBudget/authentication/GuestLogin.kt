package com.example.makeMyBudget.authentication

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment

object GuestLogin {

    private lateinit var sharedPreferences: SharedPreferences
    const val GUEST_USER_ID = "abcdefghijklmnopqrstuvwxyz12"

    fun login(fragment: Fragment) {
        sharedPreferences =
            fragment.requireActivity().getSharedPreferences("user_auth", Context.MODE_PRIVATE)

        sharedPreferences.edit().putString("user_id", GUEST_USER_ID).apply()

        sharedPreferences.edit().putBoolean("isGuest", true).apply()
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()

        Navigate.action(fragment)
    }


}