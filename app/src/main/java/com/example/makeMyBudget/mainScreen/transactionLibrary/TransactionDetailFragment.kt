package com.example.makeMyBudget.mainScreen.transactionLibrary

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController


import com.example.makeMyBudget.entities.Transaction
import com.example.makeMyBudget.entities.TransactionMode
import com.example.makeMyBudget.mainScreen.viewModels.TransactionViewModel
import com.example.makemybudget.R
import com.example.makemybudget.databinding.FragmentTransactionDetailBinding
import com.google.firebase.auth.FirebaseAuth


class TransactionDetailFragment : Fragment() {

    private lateinit var binding: FragmentTransactionDetailBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: TransactionViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTransactionDetailBinding.inflate(inflater, container, false)
        sharedPreferences =
            requireActivity().getSharedPreferences("transaction_detail", Context.MODE_PRIVATE)

        viewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
        auth = FirebaseAuth.getInstance()

        val user_id = auth.currentUser!!.uid
        viewModel.setUserID(user_id)

        val transaction_id = TransactionDetailFragmentArgs.fromBundle(requireArguments()).transId
        viewModel.setTransId(transaction_id)

        viewModel.transaction.observe(viewLifecycleOwner) {
            setData(it)
        }

        
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.edit -> {
                    findNavController().navigate(
                        TransactionDetailFragmentDirections.actionTransactionDetailFragmentToAddOrEditTransactionFragment(
                            viewModel.transactionID.value!!
                        )
                    )
                    true
                }
                else -> {
                    val dialog = AlertDialog.Builder(requireContext())
                    with(dialog) {
                        setTitle("Delete Transaction")
                        setMessage("Are you sure you want to delete this transaction?")
                        setPositiveButton("Yes") { _, _ ->
                            viewModel.delete(viewModel.transaction.value!!)
                            findNavController().navigate(
                                TransactionDetailFragmentDirections.actionTransactionDetailFragmentToMainScreenFragment()
                            )
                        }
                        setNegativeButton("No") { _, _ ->
                        }
                        show()
                    }
                    viewModel.delete(viewModel.transaction.value!!)
                    true
                }
            }
        }
        return binding.root
    }

    private fun setData(transaction: Transaction) {
        binding.transTitleInput.text = transaction.title
        binding.transDescInput.text = transaction.description
        binding.transAmountInput.text = (transaction.transactionAmount.toString())
        binding.transDateInput.text = transaction.transactionDate.toString()
        if (transaction.isRecurring) {
            binding.isRecurringCheckBox.isChecked = true
            binding.fromDateInput.text = transaction.fromDate.toString()
            binding.toDateInput.text = transaction.toDate.toString()
        } else {
            binding.isRecurringCheckBox.isChecked = false
            binding.fromDateInput.isEnabled = false
            binding.toDateInput.isEnabled = false
        }
        binding.transModeInput.setText(TransactionMode.values().find {
            it.name == transaction.transactionMode.name
        }!!.ordinal)

        binding.radioGroup.check(transaction.transactionType.ordinal)
        binding.radioGroup.isClickable = false;
    }
}