package com.example.makeMyBudget.mainScreen.transactionLibrary

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.makeMyBudget.entities.*
import com.example.makeMyBudget.mainScreen.viewModels.TransactionViewModel
import com.example.makemybudget.databinding.FragmentAddOrEditTransactionBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*


class AddOrEditTransactionFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentAddOrEditTransactionBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: TransactionViewModel
    private val screenNo = AddOrEditTransactionFragmentArgs.fromBundle(requireArguments()).screenNo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentAddOrEditTransactionBinding.inflate(inflater, container, false)

        sharedPreferences = activity?.getSharedPreferences("user_auth", Context.MODE_PRIVATE)!!


        binding.transDateInput.transformIntoDatePicker(requireContext(), "dd-MM-yyyy")
        binding.toDateInput.transformIntoDatePicker(requireContext(), "dd-MM-yyyy")
        binding.fromDateInput.transformIntoDatePicker(requireContext(), "dd-MM-yyyy")

        binding.fromDateInput.isVisible = false
        binding.toDateInput.isVisible = false

        binding.isRecurringCheckBox.setOnCheckedChangeListener { _, it ->

            binding.toDateInput.isVisible = it
            binding.fromDateInput.isVisible = it

        }

        val modeArray: MutableList<String> = mutableListOf()
        TransactionMode.values().forEach {
            modeArray.add(it.name)
        }
        val adapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            modeArray
        )
        binding.transModeInput.adapter = adapter
        binding.transModeInput.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}
                override fun onNothingSelected(p0: AdapterView<*>?) {}

            }
        val categoryArray: MutableList<String> = mutableListOf()
        TransactionCategory.values().forEach {
            categoryArray.add(it.name)
        }
        val adapter2 = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            categoryArray
        )
        binding.tagsSpinner.adapter = adapter2
        binding.tagsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        viewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)

        val userID = sharedPreferences.getString("user_id", "")!!
        val transID = AddOrEditTransactionFragmentArgs.fromBundle(requireArguments()).transId

        //setting the user id and trans id of viewModel after getting it through firebase
        viewModel.setUserID(userID)
        viewModel.setTransId(transID)

        if (transID == 0L) {
            binding.toolbar.title = "Add new Transaction"
        } else binding.toolbar.title = "Edit Transaction"
        viewModel.transaction.observe(viewLifecycleOwner) {
            if (transID != 0L) {
                setData(it)
            }
        }
        binding.cancelButton.setOnClickListener {

            val dialog = AlertDialog.Builder(requireContext())
            with(dialog) {
                setTitle("Cancel Transaction")
                setMessage("Are you sure you want to cancel this transaction?")
                setPositiveButton("Yes") { _, _ ->
                    findNavController().navigate(
                        AddOrEditTransactionFragmentDirections.actionAddOrEditTransactionFragmentToMainScreenFragment(
                            screenNo
                        )
                    )
                }
            }
            dialog.create().show()
        }

        binding.saveButton.setOnClickListener {
            saveData()
        }

        return binding.root
    }

    private fun saveData() {
        val name = binding.transTitleInput.text.toString()
        val desc = binding.transDescInput.text.toString()
        val amount = binding.transAmountInput.text.toString().toDouble()
        val date: Date = SimpleDateFormat(
            "dd-MM-yyyy",
            Locale.getDefault()
        ).parse(binding.transDateInput.text.toString())!!
        val isRecurring = binding.isRecurringCheckBox.isChecked
        val fromDate: Date
        val toDate: Date
        if (isRecurring) {
            fromDate = SimpleDateFormat(
                "dd-MM-yyyy",
                Locale.getDefault()
            ).parse(binding.fromDateInput.text.toString())!!
            toDate = SimpleDateFormat(
                "dd-MM-yyyy",
                Locale.getDefault()
            ).parse(binding.toDateInput.text.toString())!!
        } else {
            fromDate = SimpleDateFormat(
                "dd-MM-yyyy",
                Locale.getDefault()
            ).parse(binding.transDateInput.text.toString())!!
            toDate = SimpleDateFormat(
                "dd-MM-yyyy",
                Locale.getDefault()
            ).parse(binding.transDateInput.text.toString())!!
        }
        val mode: TransactionMode =
            TransactionMode.values()[binding.transModeInput.selectedItemPosition]
        val category: TransactionCategory =
            TransactionCategory.values()[binding.tagsSpinner.selectedItemPosition]
        val month = binding.transDateInput.text.toString().substring(3, 5).toInt()
        val year = binding.transDateInput.text.toString().substring(6).toInt()
        val monthYear = year * 100 + month
//        Log.d("monthyear", "$monthYear $month $year")
        val typeIndex = if (binding.incomeButton.isChecked) 1 else 0
        val type: TransactionType = TransactionType.values()[typeIndex]
        var status: TransactionStatus = TransactionStatus.PENDING
        if (binding.alreadyCompleted.isChecked) {
            status = TransactionStatus.COMPLETED
        }
        val transaction = Transaction(
            viewModel.userID.value!!,
            viewModel.transactionID.value!!,
            name,
            desc,
            amount,
            date,
            isRecurring,
            fromDate,
            toDate,
            month,
            year,
            monthYear,
            type,
            category,
            mode,
            status
        )
        viewModel.insertOrUpdate(transaction)
        Toast.makeText(requireContext(), "Transaction added/updated", Toast.LENGTH_SHORT).show()
        findNavController().navigate(
            AddOrEditTransactionFragmentDirections.actionAddOrEditTransactionFragmentToMainScreenFragment(
                screenNo
            )
        )
    }

    private fun setData(transaction: Transaction) {
        binding.transTitleInput.setText(transaction.title)
        binding.transDescInput.setText(transaction.description)
        binding.transAmountInput.setText(transaction.transactionAmount.toString())
        binding.transDateInput.setText(SimpleDateFormat("dd-MM-yyyy").format(transaction.transactionDate))
        if (transaction.isRecurring) {
            binding.isRecurringCheckBox.isChecked = true
            binding.fromDateInput.setText(transaction.fromDate.toString())
            binding.toDateInput.setText(transaction.toDate.toString())
        } else {
            binding.isRecurringCheckBox.isChecked = false
            binding.fromDateInput.isEnabled = false
            binding.toDateInput.isEnabled = false
        }
        binding.transModeInput.setSelection(TransactionMode.values().find {
            it.name == transaction.transactionMode.name
        }!!.ordinal)
        binding.tagsSpinner.setSelection(TransactionCategory.values().find {
            it.name == transaction.transactionCategory.name
        }!!.ordinal)
        binding.incomeButton.isChecked = transaction.transactionType.ordinal == 1
        binding.expenseButton.isChecked = transaction.transactionType.ordinal == 0

    }

    private fun EditText.transformIntoDatePicker(context: Context, format: String) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val sdf = SimpleDateFormat(format, Locale.UK)
                setText(sdf.format(myCalendar.time))
            }

        setOnClickListener {
            DatePickerDialog(
                context, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).run {

                show()
            }
        }
    }
}