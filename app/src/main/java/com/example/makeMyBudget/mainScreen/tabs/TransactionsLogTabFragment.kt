package com.example.makeMyBudget.mainScreen.tabs

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.makeMyBudget.mainScreen.transactionLibrary.TransactionLogEpoxyController
import com.example.makeMyBudget.mainScreen.transactionLibrary.TransactionLogItem
import com.example.makeMyBudget.mainScreen.viewModels.MainScreenViewModel
import com.example.makeMyBudget.mainScreen.viewModels.TransactionViewModel
import com.example.makemybudget.databinding.FragmentTransactionsLogTabBinding

import java.util.*

class TransactionsLogTabFragment(val fragment: Fragment) : Fragment() {

    private lateinit var binding: FragmentTransactionsLogTabBinding
    private lateinit var viewModel: MainScreenViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var transactionViewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // initialize the binding, firebase auth and both the viewModels
        binding = FragmentTransactionsLogTabBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[MainScreenViewModel::class.java]
        sharedPreferences = requireActivity().getSharedPreferences("user_auth", Context.MODE_PRIVATE)
        transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        val userID = sharedPreferences.getString("user_id", "")!!

        //setting the user id of viewModel after getting it through firebase
        viewModel.setUserID(userID)

        transactionViewModel.setUserID(userID)

        val transStatus = mutableListOf(
            TransactionLogItem("Completed", mutableListOf()),
            TransactionLogItem("Pending", mutableListOf()),
            TransactionLogItem("Upcoming", mutableListOf()),
        )

        val epoxyController =
            TransactionLogEpoxyController(fragment, requireContext(), transactionViewModel)
        epoxyController.transactionLog = transStatus

        binding.transactionsLogRecyclerView.adapter = epoxyController
        binding.transactionsLogRecyclerView.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(requireContext())

        viewModel.getCompletedTransactions().observe(viewLifecycleOwner) {
            transStatus[0] = TransactionLogItem("Completed", it.toMutableList())
            epoxyController.transactionLog = transStatus
            binding.transactionsLogRecyclerView.adapter = epoxyController
        }

        viewModel.getPendingTransactions(Date(System.currentTimeMillis()).time)
            .observe(viewLifecycleOwner) {
                transStatus[1] = TransactionLogItem("Pending", it.toMutableList())
                epoxyController.transactionLog = transStatus
                binding.transactionsLogRecyclerView.adapter = epoxyController
            }

        viewModel.getUpcomingTransactions(Date(System.currentTimeMillis()).time)
            .observe(viewLifecycleOwner) {
                transStatus[2] = TransactionLogItem("Upcoming", it.toMutableList())
                epoxyController.transactionLog = transStatus
                binding.transactionsLogRecyclerView.adapter = epoxyController
            }

        // Inflate the layout for this fragment
        return binding.root
    }

}