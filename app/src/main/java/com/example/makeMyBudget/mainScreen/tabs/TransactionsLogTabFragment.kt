package com.example.makeMyBudget.mainScreen.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.makeMyBudget.mainScreen.TransactionLogEpoxyController
import com.example.makeMyBudget.mainScreen.TransactionLogItem
import com.example.makeMyBudget.mainScreen.viewModels.MainScreenViewModel
import com.example.makeMyBudget.mainScreen.viewModels.TransactionViewModel
import com.example.makemybudget.R
import com.example.makemybudget.databinding.FragmentTransactionsLogTabBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class TransactionsLogTabFragment(val fragment: Fragment) : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentTransactionsLogTabBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var viewModel: MainScreenViewModel
    private lateinit var transactionViewModel: TransactionViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentTransactionsLogTabBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        viewModel = ViewModelProvider(this).get(MainScreenViewModel::class.java)
        transactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)

        viewModel.setUserID(firebaseAuth.currentUser?.uid!!)
        transactionViewModel.setUserID(firebaseAuth.currentUser?.uid!!)

        viewModel
        val transStatus = mutableListOf<TransactionLogItem>(
            TransactionLogItem("Completed", mutableListOf()),
            TransactionLogItem("Pending", mutableListOf()),
            TransactionLogItem("Upcoming", mutableListOf()),
        )

        viewModel.getCompletedTransactions().observe(viewLifecycleOwner) {
            transStatus[0].transactionLog = it.toMutableList()
        }

        viewModel.getPendingTransactions(Date(System.currentTimeMillis()).time)
            .observe(viewLifecycleOwner) {
                transStatus[1].transactionLog = it.toMutableList()
            }

        viewModel.getUpcomingTransactions(Date(System.currentTimeMillis()).time)
            .observe(viewLifecycleOwner) {
                transStatus[2].transactionLog = it.toMutableList()
            }

        val epoxyController =
            TransactionLogEpoxyController(fragment, requireContext(), transactionViewModel)
        epoxyController.transactionLog = transStatus

        binding.transactionsLogRecyclerView.setController(epoxyController)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transactions_log_tab, container, false)
    }

}