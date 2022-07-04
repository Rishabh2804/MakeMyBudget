package com.example.makeMyBudget.authentication

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*

class FBLogin {

    companion object {

        private lateinit var firebaseAuth: FirebaseAuth
        private lateinit var sharedPreferences: SharedPreferences

        private lateinit var fragment: Fragment

        fun login(fragment: Fragment, callBackManager: CallbackManager) {
            this.fragment = fragment

            LoginManager.getInstance()
                .logInWithReadPermissions(fragment, listOf("public_profile", "email"))

            LoginManager.getInstance()
                .registerCallback(
                    callBackManager,
                    object : FacebookCallback<LoginResult> {
                        override fun onCancel() {
                            Toast.makeText(fragment.requireActivity(), "Login Cancelled", Toast.LENGTH_SHORT)
                                .show()
                        }

                        override fun onError(error: FacebookException) {
                            Toast.makeText(
                                fragment.requireActivity(), "Login Failed", Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onSuccess(result: LoginResult) {
                            handleFBLogin(result)
                        }

                    }
                )
        }

        @OptIn(DelicateCoroutinesApi::class)
        private fun handleFBLogin(result: LoginResult) {

            firebaseAuth = FirebaseAuth.getInstance()
            sharedPreferences =
                fragment.requireActivity()!!.getSharedPreferences("user_auth", Context.MODE_PRIVATE)


            val credentials = FacebookAuthProvider.getCredential(result.accessToken.token)
            GlobalScope.launch(Dispatchers.IO) {
                val auth = firebaseAuth.signInWithCredential(credentials)


                auth.addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(fragment.requireActivity(), "Login Successful", Toast.LENGTH_SHORT)
                            .show()
                        val user = auth.result?.user
                    } else {
                        Toast.makeText(fragment.requireActivity(), "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }

                withContext(Dispatchers.Main) {
                    sharedPreferences.edit().putBoolean("isRegistered", true).apply()
                    Navigate.action(fragment)
                }
            }
        }


    }
}