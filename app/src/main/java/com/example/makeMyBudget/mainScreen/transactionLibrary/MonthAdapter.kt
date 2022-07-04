package com.example.makeMyBudget.mainScreen.transactionLibrary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
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
        val view: CardView = view.findViewById(R.id.id_card_view)
        val layout: ConstraintLayout = view.findViewById(R.id.month_card)
        val monthName: TextView = view.findViewById(R.id.trans_mode)
        val gainOrLoss: TextView = view.findViewById(R.id.trans_date)
        val profit: TextView = view.findViewById(R.id.view_details)
        val balance: TextView = view.findViewById(R.id.net_balance)
        private val viewDetails: TextView = view.findViewById(R.id.net_balance_amount)

        init {
            viewDetails.setOnClickListener {
                fragment.findNavController().navigate(
                    MainScreenFragmentDirections.actionMainScreenFragmentToMonthScreenFragment(
                        MonthCardList[adapterPosition].monthYear
                    )
                )
            }
        }

        fun bindView(monthCardDetail: MonthCardDetail) {
            monthName.text = monthCardDetail.month
            gainOrLoss.text = if (monthCardDetail.amount < 0) {
                "Loss"
            } else {
                "Gain"
            }

            balance.text = monthCardDetail.amount.toString()

        }
    }

    val typeColor = arrayListOf(
        R.color.type_income,
        R.color.type_expense
    )

}