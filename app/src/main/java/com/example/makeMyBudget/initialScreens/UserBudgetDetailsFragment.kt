package com.example.makeMyBudget.initialScreens

import android.content.Context
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.compose.NavHost
import androidx.navigation.fragment.findNavController
import com.example.makeMyBudget.mainScreen.MainScreenFragment.Companion.saveHashMap
import com.example.makemybudget.databinding.FragmentUserBudgetDetailsBinding

class UserBudgetDetailsFragment : Fragment() {
    private lateinit var binding: FragmentUserBudgetDetailsBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBudgetDetailsBinding.inflate(inflater, container, false)

        sharedPreferences = activity?.getSharedPreferences("user_auth", Context.MODE_PRIVATE)!!

        val incomeRegister = HashMap<Int, Double>()

        binding.saveButton.setOnClickListener {
            if (binding.username.text.isEmpty()) {
                binding.warningUsername.visibility = View.VISIBLE
            } else if (binding.budget.text.isEmpty()) {
                binding.warningBudget.visibility = View.VISIBLE
            } else {
                binding.warningUsername.visibility = View.GONE
                binding.warningBudget.visibility = View.GONE
                val editor = sharedPreferences.edit()
                editor.putString("username", binding.username.text.toString())
                editor.putString("budget", binding.budget.text.toString())
                if (binding.income.text.isEmpty()) {
                    editor.putString("income", "0")
                } else {
                    val calendar = Calendar.getInstance()
                    val year = calendar.get(Calendar.YEAR)
                    val currMonthYear = year * 100 + calendar.get(Calendar.MONTH)

                    editor.putString("income", binding.income.text.toString())
                    incomeRegister[currMonthYear] = binding.income.text.toString().toDouble()
                }

                sharedPreferences.saveHashMap("income_register", incomeRegister)

                editor.putBoolean("allCheck", true)
                editor.apply()

                findNavController().navigate(UserBudgetDetailsFragmentDirections.actionUserBudgetDetailsFragmentToMainScreenFragment())
            }
        }

        return binding.root
    }

}