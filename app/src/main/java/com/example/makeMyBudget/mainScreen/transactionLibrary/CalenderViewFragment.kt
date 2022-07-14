package com.example.makeMyBudget.mainScreen.transactionLibrary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.makeMyBudget.mainScreen.MainScreenFragmentDirections
import com.example.makeMyBudget.mainScreen.SwipeHandler
import com.example.makeMyBudget.mainScreen.tabs.CalendarViewFragmentArgs
import com.example.makeMyBudget.mainScreen.tabs.CalendarViewFragmentDirections
import com.example.makeMyBudget.mainScreen.viewModels.MainScreenViewModel
import com.example.makeMyBudget.mainScreen.viewModels.TransactionViewModel
import com.example.makemybudget.databinding.FragmentCalenderViewBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class CalenderViewFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentCalenderViewBinding
    private lateinit var viewModel: MainScreenViewModel
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // declaring the binding and viewModel
        binding = FragmentCalenderViewBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MainScreenViewModel::class.java)
        transactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
        // declaring the firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // getting the user id from firebase to set in both viewModels
        viewModel.setUserID(firebaseAuth.currentUser?.uid.toString())
        transactionViewModel.setUserID(firebaseAuth.currentUser?.uid.toString())

        // getting monthYear through arguments of CalenderViewFragment
        var monthYear = CalendarViewFragmentArgs.fromBundle(requireArguments()).monthYear
        // monthYear is of the form YYYYMM in long format
        // hence monthYear /100 will be YYYY(the year) and monthYear % 100 will be MM(the month)
        val month = monthYear % 100
        val year = monthYear / 100
        val day = 1;

        // getting the instance of the calendar
        val calender = Calendar.getInstance()
        calender.set(year, month, day)
        viewModel.setDate(calender.timeInMillis)
        binding.calenderView.selectedDates = mutableListOf(calender)

        binding.calenderView.setOnDayClickListener { eventDay ->
            val calendar2 = eventDay.calendar
            viewModel.setMonthYear(calendar2.get(Calendar.YEAR) * 100 + calendar2.get(Calendar.MONTH))
            viewModel.setDate(calendar2.timeInMillis)
            binding.calenderView.selectedDates = mutableListOf(calendar2)
            binding.calenderView.setHighlightedDays(mutableListOf(calendar2))
        }

//        viewModel.getDates().observe(viewLifecycleOwner, {
//            it?.let {
//                binding.calendarView.markDates(it)
//            }
//        })
//
        viewModel.expenseByDateAndType
            .observe(viewLifecycleOwner) {
                binding.spentAmount.text = it.toString() ?: "0"
            }

        viewModel.incomeByDateAndType.observe(viewLifecycleOwner) {
            binding.earnedAmount.text = it.toString() //?: "0.0"
        }

        viewModel.transactionsByDate.observe(viewLifecycleOwner) { it ->
            val adapter =
                TransactionListAdapter(
                    it.toMutableList(),
                    this,
                    requireContext(),
                    transactionViewModel,
                    listener
                )
            val swipeHandler = object : SwipeHandler() {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    if (direction == ItemTouchHelper.LEFT) {
                        adapter.deleteTransaction(viewHolder.adapterPosition, it.toMutableList())
                    } else if (direction == ItemTouchHelper.RIGHT) {
                        adapter.completeTransaction(viewHolder.adapterPosition, it.toMutableList())
                    }
                }
            }

            binding.dailyTransactions.adapter = adapter
            ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.dailyTransactions)
        }

        val callBackDispatcher = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callBackDispatcher)


//        binding.calendarView.setOnMonthChangeListener(object : OnMonthChangeListener() {
//            override fun onMonthChange(year: Int, month: Int) {
//                monthYear = year * 100 + month
//                markDates(monthYear)
//            }
//        })

        // Inflate the layout for this fragment
        return binding.root
    }


//    private fun markDates(monthYear: Int) {
//        viewModel.getDates(monthYear).observe(viewLifecycleOwner) { it ->
//            it.forEach { date ->
//                val date1= Date(date)
//                binding.calendarView.markDate(DateData(date1.year, date1.month, date1.day))
//            }
//        }
//    }

    fun showData(date: Long) {

    }

    private val listener: (id: Long) -> Unit = {
        findNavController().navigate(
            CalendarViewFragmentDirections.actionCalenderViewFragmentToTransactionDetailFragment(it)
        )
    }
}