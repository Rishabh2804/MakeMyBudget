package com.example.makeMyBudget

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import com.example.makeMyBudget.databinding.FragmentAddOrEditTransactionBinding
import com.example.makeMyBudget.databinding.FragmentTransactionTabBinding
import kotlin.collections.ArrayList

class TransactionTabFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val register: String = "https://www.codechef.com/login?destination=/"
        val user: String = "https://www.codechef.com/users/"
    }

    private lateinit var years : ArrayList<Spinner>
    private lateinit var binding: FragmentTransactionTabBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val months = arrayListOf<String>(
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        years = ArrayList()
        binding = FragmentTransactionTabBinding.inflate(inflater, container, false)


        binding.floatingActionButton.setOnClickListener{
            val newYear : Spinner = Spinner(requireContext())
            years.add(newYear)

        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction_tab, container, false)
    }


}