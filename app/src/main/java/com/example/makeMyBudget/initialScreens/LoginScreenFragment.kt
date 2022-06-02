package com.example.makeMyBudget.initialScreens

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
import com.example.makeMyBudget.R
import com.example.makeMyBudget.databinding.FragmentLoginScreenBinding
import com.google.firebase.auth.FirebaseAuth


class LoginScreenFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentLoginScreenBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedPreferences = activity?.getSharedPreferences("com.example.makeMyBudget", 0)!!
        val isRegistered: Boolean = sharedPreferences.getBoolean("isRegistered", false)
        val isLoggedIn: Boolean = sharedPreferences.getBoolean("isLoggedIn", false)
        val allCheck: Boolean = sharedPreferences.getBoolean("allCheck", false)

        var action: NavDirections

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Inflate the layout for this fragment
        binding = FragmentLoginScreenBinding.inflate(inflater, container, false)

        // Set the click listeners
        binding.customRegisterButton.setBackgroundColor(resources.getColor(R.color.white))
        binding.customRegisterButton.setTextColor(resources.getColor(R.color.black))
        binding.customRegisterButton.setOnClickListener {
            val action =
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
}