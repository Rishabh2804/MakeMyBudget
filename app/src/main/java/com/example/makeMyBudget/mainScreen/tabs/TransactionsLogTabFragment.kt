package com.example.makeMyBudget.mainScreen.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.makeMyBudget.mainScreen.TransactionLogEpoxyController
import com.example.makeMyBudget.mainScreen.TransactionLogItem
import com.example.makeMyBudget.mainScreen.viewModels.MainScreenViewModel
import com.example.makeMyBudget.mainScreen.viewModels.TransactionViewModel
import com.example.makemybudget.databinding.FragmentTransactionsLogTabBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class TransactionsLogTabFragment(val fragment: Fragment) : Fragment() {

    private lateinit var binding: FragmentTransactionsLogTabBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var viewModel: MainScreenViewModel
    private lateinit var transactionViewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTransactionsLogTabBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        viewModel = ViewModelProvider(this).get(MainScreenViewModel::class.java)
        transactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)

        viewModel.setUserID(firebaseAuth.currentUser?.uid!!)
        transactionViewModel.setUserID(firebaseAuth.currentUser?.uid!!)

        val transStatus = mutableListOf(
            TransactionLogItem("Completed", mutableListOf()),
            TransactionLogItem("Pending", mutableListOf()),
            TransactionLogItem("Upcoming", mutableListOf()),
        )

        val epoxyController =
            TransactionLogEpoxyController(fragment, requireContext(), transactionViewModel)
        epoxyController.transactionLog = transStatus

        binding.transactionsLogRecyclerView.setController(epoxyController)

        binding.transactionsLogRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getCompletedTransactions().observe(viewLifecycleOwner) {
            transStatus[0] = TransactionLogItem("Completed", it.toMutableList())
            epoxyController.transactionLog = transStatus
            epoxyController.requestModelBuild()
        }

        viewModel.getPendingTransactions(Date(System.currentTimeMillis()).time)
            .observe(viewLifecycleOwner) {
                transStatus[1] = TransactionLogItem("Pending", it.toMutableList())
                epoxyController.transactionLog = transStatus
                epoxyController.requestModelBuild()
            }

        viewModel.getUpcomingTransactions(Date(System.currentTimeMillis()).time)
            .observe(viewLifecycleOwner) {
                transStatus[2] = TransactionLogItem("Upcoming", it.toMutableList())
                epoxyController.transactionLog = transStatus
                epoxyController.requestModelBuild()
            }


        // Inflate the layout for this fragment
        return binding.root
    }

}