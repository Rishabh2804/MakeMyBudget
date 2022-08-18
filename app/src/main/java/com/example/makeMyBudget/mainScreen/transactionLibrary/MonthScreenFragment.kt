package com.example.makeMyBudget.mainScreen.transactionLibrary

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.makeMyBudget.mainScreen.viewModels.MainScreenViewModel
import com.example.makeMyBudget.mainScreen.viewModels.TransactionViewModel
import com.example.makemybudget.R
import com.example.makemybudget.databinding.FragmentMonthScreenBinding

class MonthScreenFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private lateinit var binding: FragmentMonthScreenBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: MainScreenViewModel
    private lateinit var transactionViewModel: TransactionViewModel

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMonthScreenBinding.inflate(inflater, container, false)

        //initialising both the viewModels, firebase auth and shared preferences
        transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]
        viewModel = ViewModelProvider(this)[MainScreenViewModel::class.java]
        sharedPreferences = activity?.getSharedPreferences("user_auth", Context.MODE_PRIVATE)!!

        //getting the monthYear from required arguments
        val monthYear = MonthScreenFragmentArgs.fromBundle(
            requireArguments()
        ).monthYear

        val userID = sharedPreferences.getString("user_id", "")!!

        //setting the user id  of both the viewModels
        transactionViewModel.setUserID(userID)
        viewModel.setUserID(userID)

        //setting the monthYear to the viewModel
        viewModel.setMonthYear(monthYear)

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.calenderView -> {
                    findNavController().navigate(
                        MonthScreenFragmentDirections.actionMonthScreenFragmentToCalenderViewFragment(
                            monthYear
                        )
                    )
                }
            }
            true
        }

        //monthYear is pf the format "YYYYMM" so we need to split it to get the month and year
        val month = monthYear % 100
        val year = monthYear / 100

        // title being the month and year
        binding.toolbar.title = "${months[month - 1]} $year"

        //if the user clicks on the arrow back button, he will be redirected to the previous screen
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }


        //getting the budget and income from the shared preferences
        val monthlyBudget = sharedPreferences.getString("budget", "0")!!.toDouble()

        //variables to store the monthly synopsis
        binding.monthBudget.text = monthlyBudget.toString()

//        var monthlyGains = 0.00
        var monthlyExpenses = 0.00

        //observing in the livedata returned by the viewModel to calculate the expenses and gains of the month
        viewModel.monthlyExpenses.observe(viewLifecycleOwner) {
            if (it != null) {
                monthlyExpenses = it
            }

            binding.amountSpent.text = monthlyExpenses.toString()
            binding.amountSaved.text =
                0.00.coerceAtLeast(monthlyBudget.minus(monthlyExpenses)).toString()

            binding.progressBar.progress =
                (100 * monthlyExpenses / monthlyBudget).coerceAtMost(100.0).toInt()

            if (monthlyExpenses >= monthlyBudget) {
                binding.amountSpent.error = "You have exceeded your budget"
                //  binding.savings.setTextColor(resources.getColor(R.color.red))
            } else {
                binding.amountSpent.error = null
                //  binding.savings.setTextColor(resources.getColor(R.color.green))
            }
        }

        //creating the adapter for the recycler view
        //the adapter will show the transactions of the month
        viewModel.monthlyTransactions.observe(viewLifecycleOwner) {
            val adapter = TransactionListAdapter(
                it.toMutableList(),
                this,
                requireContext(),
                transactionViewModel,
                listener,
            )
            //setting the adapter to the recycler view
            binding.transactionItems.adapter = adapter
            //setting the layout manager to the recycler view
            binding.transactionItems.layoutManager = LinearLayoutManager(requireContext())
            val swipeHandler = object : SwipeHandler() {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    //if user swipes left, delete the transaction
                    if (direction == ItemTouchHelper.LEFT) {
                        adapter.deleteTransaction(
                            viewHolder.absoluteAdapterPosition,
                            it.toMutableList()
                        )
                    }
                    //if user swipes right, complete the transaction
                    else if (direction == ItemTouchHelper.RIGHT) {
                        adapter.completeTransaction(
                            viewHolder.absoluteAdapterPosition,
                            it.toMutableList()
                        )
                    }
                }
            }

            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(binding.transactionItems)
        }

        //if the user clicks on the add transaction button, he will be redirected to the add transaction screen
        binding.addTransactionButton.setOnClickListener {
            findNavController().navigate(
                MonthScreenFragmentDirections.actionMonthScreenFragmentToAddOrEditTransactionFragment(
                    0
                )
            )
        }

        //if user presses back button, he will be redirected to the previous screen
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)

        return binding.root
    }

    private val listener: (id: Long) -> Unit = {
        //to direct to the transaction detail fragment
        findNavController().navigate(
            MonthScreenFragmentDirections.actionMonthScreenFragmentToTransactionDetailFragment(
                it,
                0
            )
        )
    }

    companion object {
        //array of months
        private val months = arrayListOf(
            "JANUARY",
            "FEBRUARY",
            "MARCH",
            "APRIL",
            "MAY",
            "JUNE",
            "JULY",
            "AUGUST",
            "SEPTEMBER",
            "OCTOBER",
            "NOVEMBER",
            "DECEMBER"
        )
    }
}