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

object FBLogin {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var fragment: Fragment

    fun login(fragment: Fragment, callBackManager: CallbackManager) {
        // fixing the fragment of this file as the previous one.
        this.fragment = fragment

        // login with facebook using the logInWithReadPermissions method.
        LoginManager.getInstance()
            .logInWithReadPermissions(fragment, listOf("public_profile", "email"))

        // callback manager is used to get the result of the login.
        LoginManager.getInstance()
            .registerCallback(
                callBackManager,
                object : FacebookCallback<LoginResult> {
                    // if the user cancels this option for login, login cancelled will be shown
                    override fun onCancel() {
                        Toast.makeText(fragment.activity, "Login Cancelled", Toast.LENGTH_SHORT)
                            .show()
                    }

                    // if the login is unsuccessful, the error is shown.
                    override fun onError(error: FacebookException) {
                        Toast.makeText(
                            fragment.activity, "Login Failed", Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onSuccess(result: LoginResult) {
                        //on success, backend of FB handle works
                        handleFBLogin(result)
                    }

                }
            )
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun handleFBLogin(result: LoginResult) {

        // initializing the firebase auth and the shared preferences.
        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences =
            fragment.requireActivity().getSharedPreferences("user_auth", Context.MODE_PRIVATE)

        // getting credentials from the facebook provider
        val credentials = FacebookAuthProvider.getCredential(result.accessToken.token)
        GlobalScope.launch(Dispatchers.IO) {
            // logging in the user with the credentials.
            val auth = firebaseAuth.signInWithCredential(credentials)

            // if the user is not null, then the user is logged in.
            auth.addOnCompleteListener {
                if (it.isSuccessful) {
                    // if the user is logged in, then the user is saved in the shared preferences.
                    sharedPreferences.edit().putString("user_id", firebaseAuth.currentUser?.uid)
                        .apply()

                    Toast.makeText(
                        fragment.requireActivity(),
                        "Login Successful",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                    sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()

                    Navigate.action(fragment)
                } else {
                    // if the user is not logged in, then the error is shown.
                    Toast.makeText(
                        fragment.requireActivity(),
                        "Login Failed",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
    }


}
