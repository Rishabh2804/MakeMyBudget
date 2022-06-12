package com.example.makeMyBudget.authentication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.makeMyBudget.R
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FacebookLoginFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var callBackManager: CallbackManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Initalize the shared preferences
        sharedPreferences = activity?.getSharedPreferences("fb_login", Context.MODE_PRIVATE)!!

        firebaseAuth = FirebaseAuth.getInstance()

        callBackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile", "email"))

        LoginManager.getInstance()
            .registerCallback(
                callBackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onCancel() {
                        Toast.makeText(activity, "Login Cancelled", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(error: FacebookException) {
                        Toast.makeText(activity, "Login Failed", Toast.LENGTH_SHORT).show()
                    }

                    override fun onSuccess(result: LoginResult) {
                        handleFBLogin(result)
                    }


                }
            )
        return inflater.inflate(R.layout.fragment_facebook_login, container, false)
    }

    private fun handleFBLogin(result: LoginResult) {
        val credentials = FacebookAuthProvider.getCredential(result.accessToken.token)
        GlobalScope.launch(Dispatchers.IO) {
            val auth = firebaseAuth.signInWithCredential(credentials)
            val allCheck: Boolean = sharedPreferences.getBoolean("allCheck", false)
            auth.addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = auth.result?.user
                } else {
                    Toast.makeText(activity, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
            withContext(Dispatchers.Main) {
                sharedPreferences.edit().putBoolean("isRegistered", true).apply()
                val action: NavDirections = if (allCheck)
                    LoginScreenFragmentDirections.actionLoginScreenFragmentToUserBudgetDetailsFragment()
                else
                    LoginScreenFragmentDirections.actionLoginScreenFragmentToMainScreenFragment()

                findNavController().navigate(action)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}