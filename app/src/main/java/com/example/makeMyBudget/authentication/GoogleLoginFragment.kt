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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Suppress("DEPRECATION")
class GoogleLoginFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var allCheck: Boolean = false
    private val SIGN_IN_CODE = 12345

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedPreferences =
            activity?.getSharedPreferences("user_auth", Context.MODE_PRIVATE)!!
        allCheck = sharedPreferences.getBoolean("allCheck", false)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Configure Google Sign In
        val googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build()

        // Build a GoogleSignInClient with the options specified by googleSignInOptions.
        googleSignInClient =
            GoogleSignIn.getClient(requireContext(), googleSignInOptions)

        val intent = googleSignInClient.signInIntent

        startActivityForResult(intent, SIGN_IN_CODE)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_google_login, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SIGN_IN_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Toast.makeText(context, "Google sign in failed ☹", Toast.LENGTH_SHORT)
                    .show()
            }
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        GlobalScope.launch(Dispatchers.IO) {
            val auth = firebaseAuth.signInWithCredential(credentials)
            val user = auth.result.user
            withContext(Dispatchers.Main) {
                if (user != null) {
                    sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                    sharedPreferences.edit().putBoolean("isRegistered", true).apply()
                    action()
                }
            }
        }
    }

    private fun action() {
        val action: NavDirections = if (allCheck)
            LoginScreenFragmentDirections.actionLoginScreenFragmentToUserBudgetDetailsFragment()
        else
            LoginScreenFragmentDirections.actionLoginScreenFragmentToMainScreenFragment()

        findNavController().navigate(action)
    }
}
