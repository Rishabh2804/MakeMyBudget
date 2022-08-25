package com.example.makeMyBudget.mainScreen.transactionLibrary

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.makeMyBudget.entities.Transaction
import com.example.makeMyBudget.mainScreen.viewModels.TransactionViewModel
import com.example.makemybudget.R
import com.example.makemybudget.databinding.FragmentTransactionDetailBinding
import java.text.SimpleDateFormat
import java.util.*

class TransactionDetailFragment : Fragment() {

    private lateinit var binding: FragmentTransactionDetailBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: TransactionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTransactionDetailBinding.inflate(inflater, container, false)
        sharedPreferences =
            requireActivity().getSharedPreferences("user_auth", Context.MODE_PRIVATE)

        viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        val userID = sharedPreferences.getString("user_id", "")!!

        viewModel.setUserID(userID)

        val transactionId = TransactionDetailFragmentArgs.fromBundle(requireArguments()).transId

        viewModel.setTransId(transactionId)

        viewModel.transaction.observe(viewLifecycleOwner) {
            if (it != null)
                setData(it)
        }

        binding.alreadyCompleted.isEnabled = false

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.edit -> {
                    findNavController().navigate(
                        TransactionDetailFragmentDirections.actionTransactionDetailFragmentToAddOrEditTransactionFragment(
                            viewModel.transactionID.value!!,
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
                            Toast.makeText(
                                requireContext(),
                                "Transaction deleted successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                            navigate()
                        }
                        setNegativeButton("No") { _, _ ->
                        }
                    }
                    dialog.create().show()
                    true
                }
            }
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigate()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
        return binding.root
    }

    private fun navigate() {
        val preScreen = sharedPreferences.getString("pre_screen", "0")?.toInt() ?: 0

        sharedPreferences.edit()
            .putString("pre_screen", "0").apply()

        when (preScreen) {
            0 -> findNavController().navigateUp()
            1 -> findNavController().navigate(TransactionDetailFragmentDirections.actionTransactionDetailFragmentToMainScreenFragment())
        }
    }

    private fun setData(transaction: Transaction) {
        binding.transTitleInput.text = transaction.title
        binding.transDescInput.text = transaction.description ?: ""
        binding.transAmountInput.text = (transaction.transactionAmount.toString())
        binding.transDateInput.text =
            (SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(transaction.transactionDate))

        binding.transactionCategory.text = transaction.transactionCategory.name
        binding.transModeInput.text = transaction.transactionMode.name
        binding.incomeButton.isChecked = transaction.transactionType.ordinal == 1
        binding.incomeButton.isEnabled = false
        binding.expenseButton.isChecked = transaction.transactionType.ordinal == 0
        binding.expenseButton.isEnabled = false
    }
}