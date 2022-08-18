package com.example.makeMyBudget.mainScreen.drawerScreens

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.makemybudget.databinding.FragmentEditMyDetailsBinding

class EditMyDetailsFragment : Fragment() {

    private lateinit var binding: FragmentEditMyDetailsBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //initialising binding and shared preferences
        binding = FragmentEditMyDetailsBinding.inflate(inflater, container, false)
        sharedPreferences = activity?.getSharedPreferences("user_auth", Context.MODE_PRIVATE)!!

        binding.username.setText(sharedPreferences.getString("username", ""))
        binding.budget.setText(sharedPreferences.getString("budget", ""))

        //if the user clicks the button save
        binding.saveButton.setOnClickListener {

            //if there is nothing written in username column
            if (binding.username.text.isEmpty()) {
                //the warning should be visible about it being empty
                binding.warningUsername.visibility =
                    View.VISIBLE
            }

            //if there is nothing written in budget column
            else if (binding.budget.text.isEmpty()) {
                //the warning should be visible about it being empty
                binding.warningBudget.visibility = View.VISIBLE
            } else {
                //both the warnings should no more be visible, as both the fields are filled.
                binding.warningUsername.visibility = View.GONE
                binding.warningBudget.visibility = View.GONE

                //feeding the data of username and budget in the shared preferences
                val editor = sharedPreferences.edit()
                editor.putString("username", binding.username.text.toString())
                editor.putString("budget", binding.budget.text.toString())

                //after all changes, editor is applied to make a permanent change in shared preferences
                editor.apply()

                //Then the user is directed to the main screen fragment
                findNavController().navigateUp()
            }

            binding.cancelButton.setOnClickListener{
                findNavController().navigateUp()
            }
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
        return binding.root
    }
}