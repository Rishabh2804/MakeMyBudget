package com.example.makeMyBudget.mainScreen.TransactionLibrary

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.makeMyBudget.mainScreen.viewModels.MainScreenViewModel
import com.example.makemybudget.databinding.FragmentMonthScreenBinding
import com.google.firebase.auth.FirebaseAuth

class MonthScreenFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    companion object {
        private val months = arrayListOf<String>(
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

    private lateinit var binding: FragmentMonthScreenBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: MainScreenViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMonthScreenBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MainScreenViewModel::class.java)
        auth = FirebaseAuth.getInstance()
        sharedPreferences = activity?.getSharedPreferences("user_auth", Context.MODE_PRIVATE)!!

        val monthYear = MonthScreenFragmentArgs.fromBundle(
            requireArguments()
        ).monthYear

        viewModel.setMonthYear(monthYear)

        val month = monthYear % 100
        val year = monthYear / 100

        binding.toolbar.title = "${months[month - 1]} $year"
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val activeIncome = sharedPreferences.getString("income", "0")?.toDouble()
        val totalGains = viewModel.monthlyGains.value
        val totalExpenses = viewModel.monthlyExpenses.value

        binding.amountSpent.text = totalExpenses.toString()
        binding.netBalance.text = totalGains.toString()

        val totalCredit = totalGains?.plus((activeIncome!!))
        val totalBalance = totalCredit?.minus(totalExpenses!!)

        binding.amountSaved.text = totalBalance.toString()

        binding.addTransactionButton.setOnClickListener {
            findNavController().navigate(
                MonthScreenFragmentDirections.actionMonthScreenFragmentToAddOrEditTransactionFragment(
                    0
                )
            )
        }

        return binding.root
    }


    fun showMonthlyTransactions(monthYear: Int) {
        viewModel.monthlyTransactions.observe(viewLifecycleOwner) {
            binding.transactionItems.adapter =
                TransactionListAdapter(it.toMutableList(), this, listener)
        }
    }

    private val listener: (id: Long) -> Unit = {
        findNavController().navigate(
            MonthScreenFragmentDirections.actionMonthScreenFragmentToTransactionDetailFragment(it)
        )
    }
}