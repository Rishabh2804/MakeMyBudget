package com.example.makeMyBudget.mainScreen

import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.makeMyBudget.mainScreen.viewModels.MainScreenViewModel
import com.example.makemybudget.R
import com.example.makemybudget.databinding.FragmentMainScreenBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.HashMap


class MainScreenFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        fun SharedPreferences.saveHashMap(key: String, obj: HashMap<Int, Double>) {
            val editor = this.edit()
            val gson = Gson()
            val json = gson.toJson(obj)
            editor.putString(key, json)
            editor.apply()
        }

        fun SharedPreferences.getHashMap(key: String): HashMap<Int, Double> {
            val gson = Gson()
            val json = this.getString(key, "")
            val type = object : TypeToken<HashMap<Int, Int>>() {}.type
            return gson.fromJson(json, type)
        }
    }

    private lateinit var binding: FragmentMainScreenBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: MainScreenViewModel
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var incomeRegister: HashMap<Int, Double>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        sharedPreferences = activity?.getSharedPreferences("user_auth", 0)!!
        viewModel = ViewModelProvider(this).get(MainScreenViewModel::class.java)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.viewPager.adapter = ViewPagerAdapter(this)

        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Statistics"
                1 -> tab.text = "Recent Transaction"
                2 -> tab.text = "Calendar"
            }
        }.attach()
        val userId = firebaseAuth.currentUser?.uid!!
        viewModel.setUserID(userId)

        val activeMonthlyIncome = sharedPreferences.getString("income", "0")?.toDouble()
        val activeYearlyPackage = activeMonthlyIncome?.times(12)

        val choices = arrayOf(
            "All Time",
            "Yearly",
        )

        val choicesAdapter = ArrayAdapter(
            activity!!.baseContext,
            android.R.layout.simple_spinner_item,
            choices
        )

        binding.spinner.adapter = choicesAdapter


        incomeRegister = sharedPreferences.getHashMap("income_register")

        binding.spinner.onItemSelectedListener = object :
            android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        val totalMonthlyEarnings = allTimeMonthlyEarnings()
                        val totalGains = viewModel.allTimeGains.value
                        val totalExpenses = viewModel.allTimeExpense.value


                        val totalCredit = totalMonthlyEarnings.plus(totalGains!!)
                        val totalSavings = totalCredit.minus(totalExpenses!!)

                        binding.credit.text = totalCredit.toString()
                        binding.expenditure.text = totalExpenses.toString()
                        binding.savings.text = totalSavings.toString()
                    }
                    1 -> {
                        val calendar = Calendar.getInstance()

                        val year = calendar.get(Calendar.YEAR)
                        val currMonthYear = year * 100 + calendar.get(Calendar.MONTH)

                        val yearIncome =
                            totalMonthlyEarnings(year * 100 + 1, currMonthYear)
                        val yearlyGains = viewModel.getYearlyGains(year).value
                        val yearlyExpenses = viewModel.getYearlyExpenses(year).value

                        val yearlyCredit = yearIncome.plus(yearlyGains!!)
                        val yearlySavings = yearlyCredit.minus(yearlyExpenses!!)

                        binding.credit.text = yearIncome.toString()
                        binding.expenditure.text = yearlyExpenses.toString()
                        binding.savings.text = yearlySavings.toString()
                    }
                }
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {
                // Another interface callback
            }
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_screen, container, false)
    }

    fun allTimeMonthlyEarnings(): Double {
        var totalEarnings = 0.0
        var earningTillNow = 0.0
        var lastMonth = -1

        for (KeyValuePair in incomeRegister) {
            val currMonth = KeyValuePair.key % 100
            if (earningTillNow != 0.0) {
                val months = currMonth - lastMonth
                totalEarnings += earningTillNow * months
            }

            earningTillNow = KeyValuePair.value
            lastMonth = currMonth
        }

        return totalEarnings
    }

    fun totalMonthlyEarnings(startMonth: Int, endMonth: Int): Double {
        var totalEarnings = 0.0

        for (i in startMonth..endMonth)
            totalEarnings += incomeRegister[i]!!

        return totalEarnings
    }

}