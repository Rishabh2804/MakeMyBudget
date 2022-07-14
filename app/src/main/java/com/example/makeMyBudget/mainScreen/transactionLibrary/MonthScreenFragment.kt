package com.example.makeMyBudget.mainScreen.transactionLibrary

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.makeMyBudget.mainScreen.SwipeHandler
import com.example.makeMyBudget.mainScreen.viewModels.MainScreenViewModel
import com.example.makeMyBudget.mainScreen.viewModels.TransactionViewModel
import com.example.makemybudget.R
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
    private lateinit var transactionViewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMonthScreenBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MainScreenViewModel::class.java)
        transactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
        auth = FirebaseAuth.getInstance()
        sharedPreferences = activity?.getSharedPreferences("user_auth", Context.MODE_PRIVATE)!!


        val monthYear = MonthScreenFragmentArgs.fromBundle(
            requireArguments()
        ).monthYear

        transactionViewModel.setUserID(auth.currentUser?.uid!!)
        viewModel.setUserID(auth.currentUser?.uid!!)

        viewModel.setMonthYear(monthYear)

        val month = monthYear % 100
        val year = monthYear / 100

        binding.toolbar.title = "${months[month - 1]} $year"
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

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

        val activeIncome = sharedPreferences.getString("income", "0")?.toDouble()
        val totalGains = viewModel.monthlyGains.value
        val totalExpenses = viewModel.monthlyExpenses.value

        val totalCredit = totalGains?.plus((activeIncome!!))
        val totalBalance = totalCredit?.minus(totalExpenses!!)

        val progressRatio = totalBalance?.div(totalCredit)
        binding.progressBar.progress = progressRatio!!.toInt()

        binding.amountSpent.text = totalExpenses.toString()
        binding.netBalanceAmount.text = totalBalance.toString()

        binding.amountSaved.text = totalBalance.toString()

        binding.addTransactionButton.setOnClickListener {
            findNavController().navigate(
                MonthScreenFragmentDirections.actionMonthScreenFragmentToAddOrEditTransactionFragment(
                    0
                )
            )
        }

        showMonthlyTransactions(monthYear)

        return binding.root
    }

    fun showMonthlyTransactions(monthYear: Int) {
        viewModel.monthlyTransactions.observe(viewLifecycleOwner) {
            val adapter =
                TransactionListAdapter(
                    it.toMutableList(),
                    this,
                    requireContext(),
                    transactionViewModel,
                    listener
                )

            binding.transactionItems.adapter = adapter
            binding.transactionItems.layoutManager = LinearLayoutManager(requireContext())

            val swipeHandler = object : SwipeHandler() {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    if (direction == ItemTouchHelper.LEFT) {
                        adapter.deleteTransaction(viewHolder.adapterPosition, it.toMutableList())
                    } else if (direction == ItemTouchHelper.RIGHT) {
                        adapter.completeTransaction(viewHolder.adapterPosition, it.toMutableList())
                    }
                }
            }

            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(binding.transactionItems)
        }
    }


    private val listener: (id: Long) -> Unit = {
        findNavController().navigate(
            MonthScreenFragmentDirections.actionMonthScreenFragmentToTransactionDetailFragment(it)
        )
    }

}