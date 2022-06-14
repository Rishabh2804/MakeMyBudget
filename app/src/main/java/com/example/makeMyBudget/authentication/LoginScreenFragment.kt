package com.example.makeMyBudget.authentication

import android.content.Context
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


class LoginScreenFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentLoginScreenBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var allCheck: Boolean = false
    private val SIGN_IN_CODE = 12345

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedPreferences = activity?.getSharedPreferences("user_auth", Context.MODE_PRIVATE)!!
        val isRegistered: Boolean = sharedPreferences.getBoolean("isRegistered", false)
        val isLoggedIn: Boolean = sharedPreferences.getBoolean("isLoggedIn", false)
        allCheck = sharedPreferences.getBoolean("allCheck", false)

        var action: NavDirections

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Inflate the layout for this fragment
        binding = FragmentLoginScreenBinding.inflate(inflater, container, false)

        // Configure Google Sign In
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        // Build a GoogleSignInClient with the options specified by googleSignInOptions.
        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)

        // Set the click listeners
        binding.customRegisterButton.setBackgroundColor(resources.getColor(R.color.white))
        binding.customRegisterButton.setTextColor(resources.getColor(R.color.black))
        binding.googleLoginButton.setOnClickListener {
            action =
                LoginScreenFragmentDirections.actionLoginScreenFragmentToGoogleLoginFragment()
            findNavController().navigate(action)
        }
        binding.fbLoginButton.setOnClickListener {
            action =
                LoginScreenFragmentDirections.actionLoginScreenFragmentToFacebookLoginFragment()
            findNavController().navigate(action)
        }
        binding.customRegisterButton.setOnClickListener {
            action =
                LoginScreenFragmentDirections.actionLoginScreenFragmentToRegisterScreenFragment()
            findNavController().navigate(action)
        }

        var stayLoggedIn = false
        binding.stayLoggedIn.setOnClickListener {
            stayLoggedIn = binding.stayLoggedIn.isChecked
        }

        binding.customLoginButton.setOnClickListener {
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
                                Toast.makeText(
                                    context,
                                    "Welcome to MakeMyBudget",
                                    Toast.LENGTH_SHORT
                                ).show()
                                action()

                            } else {
                                Toast.makeText(
                                    context,
                                    "Invalid username or password",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                            }
                        }
                }
            }

        }


        return binding.root
    }

    private fun action() {
        var action =LoginScreenFragmentDirections.actionLoginScreenFragmentToMainScreenFragment()
        if (!allCheck)
            action =LoginScreenFragmentDirections.actionLoginScreenFragmentToUserBudgetDetailsFragment()

        findNavController().navigate(action)
    }
}