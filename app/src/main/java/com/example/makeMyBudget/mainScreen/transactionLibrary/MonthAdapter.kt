package com.example.makeMyBudget.mainScreen.transactionLibrary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.makeMyBudget.mainScreen.MainScreenFragmentDirections
import com.example.makemybudget.R
import com.example.makemybudget.databinding.EpoxyMonthsBinding

class MonthAdapter(val MonthCardList: MutableList<MonthCardDetail>, val fragment: Fragment) :
    RecyclerView.Adapter<MonthAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EpoxyMonthsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(MonthCardList[position])
    }

    override fun getItemCount(): Int {
        return MonthCardList.size
    }

    inner class ViewHolder(val binding: EpoxyMonthsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.detailsInside.setOnClickListener {
                fragment.findNavController().navigate(
                    MainScreenFragmentDirections.actionMainScreenFragmentToMonthScreenFragment(
                        MonthCardList[absoluteAdapterPosition].monthYear

                    )
                )
            }
        }

        fun bindView(monthCardDetail: MonthCardDetail) {
            binding.apply {
                monthTransaction.text = monthCardDetail.month
            }
        }
    }

    val typeColor = arrayListOf(
        R.color.type_income,
        R.color.type_expense
    )
}