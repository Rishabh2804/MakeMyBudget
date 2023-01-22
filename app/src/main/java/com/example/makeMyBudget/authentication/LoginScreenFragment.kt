package com.example.makeMyBudget.authentication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.makemybudget.R
import com.example.makemybudget.databinding.FragmentLoginScreenBinding
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth

class LoginScreenFragment : Fragment() {


    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentLoginScreenBinding
    private lateinit var action: NavDirections
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var callBackManager: CallbackManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        sharedPreferences = activity?.getSharedPreferences("user_auth", Context.MODE_PRIVATE)!!

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Inflate the layout for this fragment
        binding = FragmentLoginScreenBinding.inflate(inflater, container, false)

        // Set the click listeners
        binding.createAccount.setBackgroundColor(resources.getColor(R.color.white))
        binding.createAccount.setTextColor(resources.getColor(R.color.black))
        binding.googleLoginButton.setOnClickListener {
            GoogleLogin.login(this)
        }

        binding.fbLoginButton.setOnClickListener {
            callBackManager = CallbackManager.Factory.create()
            FBLogin.login(this, callBackManager)
        }

        binding.customLoginButton.setOnClickListener {
            handleCustomLogin()
        }

        binding.guestLogin.setOnClickListener {
            GuestLogin.login(this)
        }

        binding.createAccount.setOnClickListener {
            action =
                LoginScreenFragmentDirections.actionLoginScreenFragmentToRegisterScreenFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun handleCustomLogin() {
        val email = binding.userName.text.toString()
        val password = binding.passWord.text.toString()

        if (email.isEmpty() or password.isEmpty()) {
            Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT)
                .show()
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(context, "Please enter valid email", Toast.LENGTH_SHORT).show()
            } else {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            sharedPreferences.edit()
                                .putString("user_id", firebaseAuth.currentUser?.uid).apply()

                            sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()

                            Toast.makeText(
                                context,
                                "Welcome to MakeMyBudget",
                                Toast.LENGTH_SHORT
                            ).show()

                            Navigate.action(this)
                        } else {
                            Toast.makeText(
                                context,
                                "Invalid username or password",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SIGN_IN_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                GoogleLogin.firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Toast.makeText(context, "Google sign in failed ☹", Toast.LENGTH_SHORT)
                    .show()
            }
            super.onActivityResult(requestCode, resultCode, data)
        } else
            callBackManager.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private const val SIGN_IN_CODE = 12345
        const val LOGIN_FRAGMENT_TAG = "login_fragment_tag"
    }

}