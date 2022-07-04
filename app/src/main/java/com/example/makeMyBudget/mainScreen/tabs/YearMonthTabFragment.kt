package com.example.makeMyBudget.mainScreen.tabs

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import com.example.makeMyBudget.mainScreen.transactionLibrary.YearEpoxyController
import com.example.makeMyBudget.mainScreen.viewModels.MainScreenViewModel
import com.example.makeMyBudget.mainScreen.viewModels.convertIntoEpoxyData
import com.example.makemybudget.databinding.FragmentYearMonthTabBinding
import com.google.firebase.auth.FirebaseAuth
import kotlin.collections.ArrayList

class YearMonthTabFragment(val fragment: Fragment) : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val register: String = "https://www.codechef.com/login?destination=/"
        val user: String = "https://www.codechef.com/users/"
    }

    private lateinit var years: ArrayList<Spinner>
    private lateinit var binding: FragmentYearMonthTabBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var viewModel: MainScreenViewModel

    companion object {
        val months = arrayListOf<String>(
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
            "DECEMBER",
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentYearMonthTabBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        viewModel = ViewModelProvider(this).get(MainScreenViewModel::class.java)
        sharedPreferences = activity?.getSharedPreferences("user_auth", 0)!!
        viewModel.setUserID(firebaseAuth.currentUser!!.uid)

        val yearEpoxyController = YearEpoxyController(fragment)
        //yearEpoxyController.transactYears= list
        binding.epoxy.setController(yearEpoxyController)
        binding.epoxy.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        viewModel.epoxyDataList.observe(viewLifecycleOwner) {
            val list = convertIntoEpoxyData(it, sharedPreferences)
            yearEpoxyController.transactYears = list
            yearEpoxyController.requestModelBuild()
        }

        // Inflate the layout for this fragment
        return binding.root
    }


}