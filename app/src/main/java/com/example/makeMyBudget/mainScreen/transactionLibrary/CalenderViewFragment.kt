package com.example.makeMyBudget.mainScreen.transactionLibrary

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.applandeo.materialcalendarview.EventDay
import com.example.makeMyBudget.mainScreen.viewModels.MainScreenViewModel
import com.example.makeMyBudget.mainScreen.viewModels.TransactionViewModel
import com.example.makemybudget.R
import com.example.makemybudget.databinding.FragmentCalenderViewBinding

import java.util.*

class CalenderViewFragment : Fragment() {
    private lateinit var binding: FragmentCalenderViewBinding
    private lateinit var viewModel: MainScreenViewModel
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // declaring the binding and viewModel
        binding = FragmentCalenderViewBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[MainScreenViewModel::class.java]
        transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]
        sharedPreferences =
            activity?.getSharedPreferences("user_auth", Context.MODE_PRIVATE)!!
        // declaring the firebaseAuth
        val userID = sharedPreferences.getString("user_id", "")!!

        transactionViewModel.setUserID(userID)
        viewModel.setUserID(userID)


        // getting monthYear through arguments of CalenderViewFragment
        val monthYear = CalenderViewFragmentArgs.fromBundle(requireArguments()).monthYear
        // monthYear is of the form YYYYMM in long format
        // hence monthYear /100 will be YYYY(the year) and monthYear % 100 will be MM(the month)
        viewModel.setMonthYear(monthYear)
        val month = monthYear % 100
        val year = monthYear / 100
        val day = 1

        // getting the instance of the calendar
        val calender = Calendar.getInstance()
        // setting the day, month and year of the calendar
        calender.set(year, month, day)
        // marking the dates
        //markDates(monthYear)

        viewModel.setDate(calender.timeInMillis)
        viewModel.getDates.observe(viewLifecycleOwner)
        { dates ->
            val highlightDays = mutableListOf<EventDay>()
            dates.forEach { date ->
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = date
                highlightDays.add(EventDay(calendar, R.color.category_bills, R.color.category_food))

            }
            binding.calenderView.setEvents(highlightDays)
        }
        binding.calenderView.setOnDayClickListener { eventDay ->
            val calendar = eventDay.calendar
            viewModel.setMonthYear(calendar.get(Calendar.YEAR) * 100 + calendar.get(Calendar.MONTH))
            viewModel.setDate(calendar.timeInMillis)
//            binding.calenderView.selectedDates = mutableListOf(calendar)
//           binding.calenderView.setHighlightedDays(mutableListOf(calendar))
        }



        viewModel.expenseByDateAndType
            .observe(viewLifecycleOwner) {
                binding.spentAmount.text = it?.toString() ?: "0"
            }

        viewModel.incomeByDateAndType
            .observe(viewLifecycleOwner) {
                binding.earnedAmount.text = it?.toString() ?: "0"
            }

        viewModel.transactionsByDate.observe(viewLifecycleOwner) {
            val adapter =
                TransactionListAdapter(
                    it.toMutableList(),
                    this,
                    requireContext(),
                    transactionViewModel,
                    listener
                )

//            val swipeHandler = object : SwipeHandler() {
//                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                    if (direction == ItemTouchHelper.LEFT) {
//                        adapter.deleteTransaction(
//                            viewHolder.absoluteAdapterPosition,
//                            it.toMutableList()
//                        )
//                        setDefaultSwipeDirs(0)
//                    } else if (direction == ItemTouchHelper.RIGHT) {
//                        adapter.completeTransaction(
//                            viewHolder.absoluteAdapterPosition,
//                            it.toMutableList()
//                        )
//                    }
//                }
//            }
//            ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.dailyTransactions)

            binding.dailyTransactions.adapter = adapter
            binding.dailyTransactions.layoutManager =
                androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // Inflate the layout for this fragment
        return binding.root
    }

    private val listener: (id: Long) -> Unit = {
        findNavController().navigate(
            CalenderViewFragmentDirections.actionCalenderViewFragmentToTransactionDetailFragment(
                it,
                0
            )
        )
    }

}