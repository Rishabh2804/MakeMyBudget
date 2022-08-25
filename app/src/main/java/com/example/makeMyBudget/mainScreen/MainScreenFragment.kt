package com.example.makeMyBudget.mainScreen

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.makeMyBudget.mainScreen.tabs.ViewPagerAdapter
import com.example.makeMyBudget.mainScreen.viewModels.MainScreenViewModel
import com.example.makemybudget.R
import com.example.makemybudget.databinding.FragmentMainScreenBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class MainScreenFragment : Fragment() {

    private lateinit var binding: FragmentMainScreenBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: MainScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        sharedPreferences = activity?.getSharedPreferences("user_auth", 0)!!
        viewModel = ViewModelProvider(this)[MainScreenViewModel::class.java]

        sharedPreferences.edit()
            .putString("pre_screen", "1").apply()

        binding.drawerButton.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        "Hi! ${
            sharedPreferences.getString(
                "username",
                ""
            )
        }".also {
            binding.navigationView.getHeaderView(0).findViewById<TextView>(R.id.app_title).text = it
        }

        binding.navigationView.itemIconTintList = null
        binding.navigationView.setItemIconSize(90)
        binding.navigationView.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.mushroom_item -> {
                    findNavController().navigate(
                        MainScreenFragmentDirections.actionMainScreenFragmentToHelloMushroomsFragment()
                    )
                    true
                }
                R.id.my_details -> {
                    findNavController().navigate(
                        MainScreenFragmentDirections.actionMainScreenFragmentToMyDetailsFragment()
                    )
                    true
                }
                R.id.edit_my_details -> {
                    findNavController().navigate(
                        MainScreenFragmentDirections.actionMainScreenFragmentToEditMyDetailsFragment()
                    )
                    true
                }
                R.id.about_us -> {
                    findNavController().navigate(
                        MainScreenFragmentDirections.actionMainScreenFragmentToAboutUsFragment()
                    )
                    true
                }
                R.id.logout -> {

                    sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
                    sharedPreferences.edit().putBoolean("isRegistered", false).apply()
                    sharedPreferences.edit().putBoolean("allCheck", false).apply()

                    sharedPreferences.edit().putString("tab_position", "0").apply()

                    sharedPreferences.edit().putString("pre_screen", "0").apply()

//                    if (sharedPreferences.getBoolean("isGuest", true)) {
//                        viewModel.deleteUserData()
//                    }

                    sharedPreferences.edit().clear().apply()

                    findNavController().navigate(MainScreenFragmentDirections.actionMainScreenFragmentToLoginScreenFragment())
                    true
                }
                else -> false
            }
        }

        val userID = sharedPreferences.getString("user_id", "")!!
        viewModel.setUserID(userID)

        val choices = arrayOf(
            "This Month",
            //TODO: Yearly Synopsis Coming Soon!!
            // "This Year",
        )

        val choicesAdapter = ArrayAdapter(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_item,
            choices
        )

        binding.detailsMode.textAlignment = View.TEXT_ALIGNMENT_CENTER
        binding.detailsMode.adapter = choicesAdapter

        binding.detailsMode.onItemSelectedListener = object :
            android.widget.AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val calendar = Calendar.getInstance()
                when (position) {
                    0 -> {
                        binding.creditText.text = "Monthly Budget"
                        binding.expenditureText.text = "Expenditure"
                        binding.savingsText.text = "Savings"

                        val month = calendar.get(Calendar.MONTH) + 1
                        val year = calendar.get(Calendar.YEAR)
                        val monthYear = year * 100 + month

                        viewModel.setMonthYear(monthYear)

                        val monthlyBudget = sharedPreferences.getString("budget", "0")?.toDouble()
                        binding.creditAmount.text = monthlyBudget.toString()

                        var monthlyExpenditure = 0.00
                        var monthlySavings = 0.00

                        viewModel.monthlyExpenses.observe(viewLifecycleOwner) {
                            if (it != null) {
                                monthlyExpenditure = it
                            }

                            binding.expenditureAmount.text = monthlyExpenditure.toString()

                            monthlySavings = monthlyBudget?.minus(monthlyExpenditure)!!
                            binding.balanceAmount.text =
                                0.00.coerceAtLeast(monthlySavings).toString()

                            if (monthlyExpenditure > monthlyBudget) {
                                binding.expenditureAmount.error = "You have exceeded your budget"
                                //  binding.savings.setTextColor(resources.getColor(R.color.red))
                            } else {
                                binding.expenditureAmount.error = null
                                //  binding.savings.setTextColor(resources.getColor(R.color.green))
                            }
                        }
                    }

                    /**       TODO: Feature: Yearly Expenditure Synopsis
                     *                    1 -> {
                     *
                     *                        binding.creditText.text = "Total Earnings"
                     *                        binding.expenditureText.text = "Total Expenditure"
                     *                        binding.savingsText.text = "Total Savings"
                     *
                     *                        val year = calendar.get(Calendar.YEAR)
                     *
                     *                        val yearIncome = 0.0
                     *                        // totalMonthlyEarnings(year * 100 + 1, currMonthYear)
                     *
                     *                        binding.creditAmount.text = yearIncome.toString()
                     *
                     *                        var yearlyGains = 0.0
                     *                        var yearlyExpenses = 0.0
                     *                        var yearlyCredit = 0.0
                     *                        var yearlySavings = 0.0
                     *
                     *                        viewModel.getYearlyGains.observe(viewLifecycleOwner) {
                     *                            yearlyGains = it[year]!!
                     *
                     *                            yearlyCredit = yearIncome.plus(yearlyGains)
                     *                            yearlySavings = yearlyCredit.minus(yearlyExpenses)
                     *
                     *                            binding.savingsAmount.text = yearlySavings.toString()
                     *                        }
                     *
                     *                        viewModel.getYearlyExpenses.observe(viewLifecycleOwner) {
                     *                            yearlyExpenses = it[year]!!
                     *                            binding.expenditureAmount.text = yearlyExpenses.toString()
                     *
                     *                            yearlyCredit = yearIncome.plus(yearlyGains)
                     *                            yearlySavings = yearlyCredit.minus(yearlyExpenses)
                     *
                     *                            binding.savingsAmount.text = yearlySavings.toString()
                     *                        }
                     *                    }
                     **/
                }
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {
                // Another interface callback
            }
        }


        binding.viewPager.adapter = ViewPagerAdapter(this)
        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val initialPosition = sharedPreferences.getString("tab_position", "0")?.toInt() ?: 0

        binding.viewPager.currentItem = initialPosition
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
                sharedPreferences.edit()
                    .putString("tab_position", "${tab.position}").apply()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Statistics"
                1 -> tab.text = "Transactions Log"
                2 -> tab.text = "Monthly Timeline"
            }
        }.attach()

        binding.addTransactionButton.setOnClickListener {

            findNavController().navigate(
                MainScreenFragmentDirections.actionMainScreenFragmentToAddOrEditTransactionFragment(
                    0
                )
            )
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                else if (binding.viewPager.currentItem != 0)
                    binding.viewPager.currentItem = 0
                else
                    requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // Inflate the layout for this fragment
        return binding.root
    }
}