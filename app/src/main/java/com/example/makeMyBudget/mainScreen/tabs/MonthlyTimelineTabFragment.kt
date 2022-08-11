package com.example.makeMyBudget.mainScreen.tabs

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.makeMyBudget.mainScreen.transactionLibrary.YearEpoxyController
import com.example.makeMyBudget.mainScreen.viewModels.MainScreenViewModel
import com.example.makeMyBudget.mainScreen.viewModels.convertIntoEpoxyData
import com.example.makemybudget.databinding.FragmentMonthlyTimelineTabBinding

class MonthlyTimelineTabFragment(val fragment: Fragment) : Fragment() {

    private lateinit var binding: FragmentMonthlyTimelineTabBinding
    private lateinit var viewModel: MainScreenViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMonthlyTimelineTabBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[MainScreenViewModel::class.java]
        sharedPreferences = activity?.getSharedPreferences("user_auth", Context.MODE_PRIVATE)!!

        val userID = sharedPreferences.getString("user_id", "")!!

        //setting the user id of viewModel after getting it through firebase
        viewModel.setUserID(userID)

        val yearEpoxyController = YearEpoxyController(fragment)
        //yearEpoxyController.transactYears= list
        binding.epoxy.setController(yearEpoxyController)
        binding.epoxy.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        viewModel.epoxyDataList.observe(viewLifecycleOwner) {
            val list = convertIntoEpoxyData(it)
            yearEpoxyController.transactYears = list
            yearEpoxyController.requestModelBuild()
        }
        return binding.root
    }

    companion object {
        val months = arrayListOf(
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
}