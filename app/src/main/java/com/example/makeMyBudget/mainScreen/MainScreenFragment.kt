package com.example.makeMyBudget.mainScreen

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.makeMyBudget.mainScreen.viewModels.MainScreenViewModel
import com.example.makemybudget.R
import com.example.makemybudget.databinding.FragmentMainScreenBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*
import kotlin.collections.HashMap

class MainScreenFragment : Fragment() {

    private lateinit var binding: FragmentMainScreenBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: MainScreenViewModel
    private lateinit var incomeRegister: HashMap<Int, Double>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        sharedPreferences = activity?.getSharedPreferences("user_auth", 0)!!
        viewModel = ViewModelProvider(this).get(MainScreenViewModel::class.java)

        binding.drawerButton.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.app_title).text =
            "Hi! " + sharedPreferences.getString("username", "")
        binding.navigationView.setNavigationItemSelectedListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)

            when (it.itemId) {
                R.id.my_details -> {

                }
                R.id.about_us -> {
                    //TODO: About us page
                }

                R.id.logout -> {
                    sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
                    // TODO: Clear all database data if user logged in as a guest
//                    findNavController().navigate(R.id.action_mainScreenFragment_to_loginFragment)
                }

            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        val initialPosition = MainScreenFragmentArgs.fromBundle(requireArguments()).screenNumber

        binding.viewPager.adapter = ViewPagerAdapter(this)
        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        binding.viewPager.currentItem = initialPosition
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

        val userID = sharedPreferences.getString("user_id", "")!!
        viewModel.setUserID(userID)

        val activeMonthlyIncome = sharedPreferences.getString("income", "0")?.toDouble()
        val activeYearlyPackage = activeMonthlyIncome?.times(12)

        val choices = arrayOf(
            "All Time",
            "Yearly",
        )

        val choicesAdapter = ArrayAdapter(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_item,
            choices
        )

        binding.spinner.adapter = choicesAdapter

        //incomeRegister = sharedPreferences.getHashMap("income_register")

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
                        val totalMonthlyEarnings = 0.0
                        // allTimeMonthlyEarnings()
                        var totalGains = 0.0
                        var totalExpenses = 0.0
                        var totalCredit = 0.0
                        var totalSavings = 0.0

                        viewModel.allTimeGains.observe(viewLifecycleOwner) {
                            totalGains = it ?: 0.0
                            totalCredit = totalMonthlyEarnings.plus(totalGains)
                            totalSavings = totalCredit.minus(totalExpenses)

                            binding.credit.text = totalCredit.toString()
                            binding.expenditure.text = totalExpenses.toString()
                            binding.savings.text = totalSavings.toString()
                        }

                        viewModel.allTimeExpense.observe(viewLifecycleOwner) {
                            totalExpenses = it ?: 0.0
                            totalCredit = totalMonthlyEarnings.plus(totalGains)
                            totalSavings = totalCredit.minus(totalExpenses)

                            binding.credit.text = totalCredit.toString()
                            binding.expenditure.text = totalExpenses.toString()
                            binding.savings.text = totalSavings.toString()
                        }
                    }
                    1 -> {
                        val calendar = Calendar.getInstance()

                        val year = calendar.get(Calendar.YEAR)

                        val yearIncome = 0.0
                        // totalMonthlyEarnings(year * 100 + 1, currMonthYear)

                        binding.credit.text = yearIncome.toString()

                        var yearlyGains = 0.0
                        var yearlyExpenses = 0.0
                        var yearlyCredit = 0.0
                        var yearlySavings = 0.0

                        viewModel.getYearlyGains.observe(viewLifecycleOwner) {
                            yearlyGains = it[year]!!

                            yearlyCredit = yearIncome.plus(yearlyGains)
                            yearlySavings = yearlyCredit.minus(yearlyExpenses)

                            binding.savings.text = yearlySavings.toString()
                        }

                        viewModel.getYearlyExpenses.observe(viewLifecycleOwner) {
                            yearlyExpenses = it[year]!!
                            binding.expenditure.text = yearlyExpenses.toString()

                            yearlyCredit = yearIncome.plus(yearlyGains)
                            yearlySavings = yearlyCredit.minus(yearlyExpenses)

                            binding.savings.text = yearlySavings.toString()
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {
                // Another interface callback
            }
        }

        binding.floatingActionButton2.setOnClickListener {
            findNavController().navigate(
                MainScreenFragmentDirections.actionMainScreenFragmentToAddOrEditTransactionFragment(
                    0,
                    0
                )
            )
        }

        // Inflate the layout for this fragment
        return binding.root
    }

//    fun allTimeMonthlyEarnings(): Double {
//        var totalEarnings = 0.0
//        var earningTillNow = 0.0
//        var lastMonth = -1
//
//        for (KeyValuePair in incomeRegister) {
//            val currMonth = KeyValuePair.key % 100
//            if (earningTillNow != 0.0) {
//                val months = currMonth - lastMonth
//                totalEarnings += earningTillNow * months
//            }
//
//            earningTillNow = KeyValuePair.value
//            lastMonth = currMonth
//        }
//
//        return totalEarnings
//    }
//
//    fun totalMonthlyEarnings(startMonth: Int, endMonth: Int): Double {
//        var totalEarnings = 0.0
//
//        for (i in startMonth..endMonth)
//            totalEarnings += incomeRegister[i]!!
//
//        return totalEarnings
//    }

}