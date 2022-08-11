package com.example.makeMyBudget.mainScreen.transactionLibrary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.makeMyBudget.mainScreen.MainScreenFragmentDirections
import com.example.makemybudget.R

class MonthAdapter(val MonthCardList: MutableList<MonthCardDetail>, val fragment: Fragment) :
    RecyclerView.Adapter<MonthAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.epoxy_months, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(MonthCardList[position])
    }

    override fun getItemCount(): Int {
        return MonthCardList.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val view: CardView = view.findViewById(R.id.card_view)
        val layout: ConstraintLayout = view.findViewById(R.id.month_card)
        val monthName: TextView = view.findViewById(R.id.month_transaction)
        val monthlyBudget: TextView = view.findViewById(R.id.month_budget)
        val earnings: TextView = view.findViewById(R.id.month_expense)
        val savings: TextView = view.findViewById(R.id.month_savings)
        private val viewDetails: TextView = view.findViewById(R.id.detailsInside)

        init {
            viewDetails.setOnClickListener {
                fragment.findNavController().navigate(
                    MainScreenFragmentDirections.actionMainScreenFragmentToMonthScreenFragment(
                        MonthCardList[absoluteAdapterPosition].monthYear

                    )
                )
            }
        }

        fun bindView(monthCardDetail: MonthCardDetail) {
            monthName.text = monthCardDetail.month
            monthlyBudget.text = if (monthCardDetail.amount < 0) {
                "Loss"
            } else {
                "Gain"
            }
            earnings.text = monthCardDetail.earnings.toString()
            savings.text = monthCardDetail.amount.toString()

        }
    }

    val typeColor = arrayListOf(
        R.color.type_income,
        R.color.type_expense
    )
}