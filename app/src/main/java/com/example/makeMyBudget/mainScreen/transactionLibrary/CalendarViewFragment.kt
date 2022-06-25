package com.example.makeMyBudget.mainScreen.transactionLibrary

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.makeMyBudget.entities.TransactionType
import com.example.makeMyBudget.mainScreen.MainScreenFragmentDirections
import com.example.makeMyBudget.mainScreen.SwipeHandler
import com.example.makeMyBudget.mainScreen.viewModels.MainScreenViewModel
import com.example.makeMyBudget.mainScreen.viewModels.TransactionViewModel
import com.example.makemybudget.R
import com.example.makemybudget.databinding.FragmentCalendarViewBinding
import com.google.firebase.auth.FirebaseAuth
import sun.bob.mcalendarview.listeners.OnDateClickListener
import sun.bob.mcalendarview.listeners.OnMonthChangeListener
import sun.bob.mcalendarview.vo.DateData
import java.util.*

class CalendarViewFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentCalendarViewBinding
    private lateinit var viewModel: MainScreenViewModel
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCalendarViewBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MainScreenViewModel::class.java)
        transactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
        firebaseAuth = FirebaseAuth.getInstance()

        viewModel.setUserID(firebaseAuth.currentUser?.uid.toString())
        transactionViewModel.setUserID(firebaseAuth.currentUser?.uid.toString())

        var monthYear = CalendarViewFragmentArgs.fromBundle(arguments!!).monthYear
        val month = monthYear % 100
        val year = monthYear / 100
        val day = 1;

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        markDates(monthYear)

        binding.calendarView.setOnDateClickListener(object : OnDateClickListener() {
            override fun onDateClick(view: View?, date: DateData?) {
                val calendar = Calendar.getInstance()
                calendar.set(date!!.year, date.month, date.day)
                showData(calendar.timeInMillis)
            }
        })

        binding.calendarView.setOnMonthChangeListener(object : OnMonthChangeListener() {
            override fun onMonthChange(year: Int, month: Int) {
                monthYear = year * 100 + month
                markDates(monthYear)
            }
        })

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar_view, container, false)
    }


    private fun markDates(monthYear: Int) {
        viewModel.getDates(monthYear).observe(viewLifecycleOwner) { it ->
            it.forEach { i ->
                val date1 = Date(i)
                binding.calendarView.markDate(DateData(date1.year, date1.month, date1.day))
            }
        }
    }

    fun showData(date: Long) {

        viewModel.getAmountByDateAndType(date, TransactionType.EXPENSE)
            .observe(viewLifecycleOwner) {
                binding.spentAmount.text = it.toString()
            }

        viewModel.getAmountByDateAndType(date, TransactionType.INCOME).observe(viewLifecycleOwner) {
            binding.earnedAmount.text = it.toString()
        }

        viewModel.getTransactionsByDate(date).observe(viewLifecycleOwner) { it ->
            val adapter =
                TransactionListAdapter(it.toMutableList(), this,requireContext(), transactionViewModel, listener)
            val swipeHandler = object : SwipeHandler() {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    if (direction == ItemTouchHelper.LEFT) {
                        adapter.deleteTransaction(viewHolder.adapterPosition)
                    } else if (direction == ItemTouchHelper.RIGHT) {
                        adapter.completeTransaction(viewHolder.adapterPosition)
                    }
                }
            }

            binding.dailyTransactions.adapter = adapter
            ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.dailyTransactions)

        }
    }

    private val listener: (id: Long) -> Unit = {
        findNavController().navigate(
            MainScreenFragmentDirections.actionMainScreenFragmentToTransactionDetailFragment(it)
        )
    }
}