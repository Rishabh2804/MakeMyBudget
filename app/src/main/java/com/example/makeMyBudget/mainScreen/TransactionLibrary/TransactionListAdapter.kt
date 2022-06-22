package com.example.makeMyBudget.mainScreen.TransactionLibrary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.example.makeMyBudget.entities.Transaction
import com.example.makeMyBudget.entities.TransactionStatus
import com.example.makeMyBudget.entities.TransactionType
import com.example.makemybudget.R

class TransactionListAdapter(private val listener: (Long) -> Unit) :
    ListAdapter<Transaction, TransactionListAdapter.holder>(diffView()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.transaction_item, parent, false)
        return holder(view)
    }

    override fun onBindViewHolder(holder: holder, position: Int) {
        holder.bindview(getItem(position))

    }

    inner class holder(val view: View) : RecyclerView.ViewHolder(view) {
        init {
            val viewDetails: TextView = view.findViewById(R.id.profitOrLoss)
            viewDetails.setOnClickListener {
                listener.invoke(getItem(adapterPosition).trans_id)
            }
        }

        val layout: ConstraintLayout = view.findViewById(R.id.item)
        val title: TextView = view.findViewById(R.id.transaction_title)
        val mode: TextView = view.findViewById(R.id.month_transaction)
        val amount: TextView = view.findViewById(R.id.amount)
        val date: TextView = view.findViewById(R.id.GainOrLoss)
        val typeHere: View = view.findViewById(R.id.expenseOrIncome)
        val image: ImageView = view.findViewById(R.id.categorized_image)

        fun bindview(transaction: Transaction) {
            when (transaction.transactionStatus) {
                TransactionStatus.UPCOMING ->
                    layout.setBackgroundColor(status_color[0])
                TransactionStatus.MISSED ->
                    layout.setBackgroundColor(status_color[1])
                else -> layout.setBackgroundColor(status_color[2])
            }

            title.text = transaction.title

            mode.text = transaction.transactionMode.name

            amount.text = transaction.transactionAmount.toString()

            date.text = transaction.transactionDate.toString()

            val type = transaction.transactionType
            if (type == TransactionType.INCOME)
                typeHere.setBackgroundColor(type_color[0])
            else
                typeHere.setBackgroundColor(type_color[1])

            image.setImageDrawable(
                ContextCompat.getDrawable(
                    image.context,
                    images[transaction.transactionCategory.ordinal]
                )
            )
        }
    }

    class diffView : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.trans_id == newItem.trans_id
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }

    }

    val type_color = arrayListOf(
        R.color.type_income,
        R.color.type_expense
    )

    val mode_color = arrayListOf(
        R.color.mode_cash,
        R.color.mode_credit_card,
        R.color.mode_debit_card
    )

    val status_color = arrayListOf(
        R.color.status_upcoming,
        R.color.status_missed,
        R.color.status_completed
    )

    companion object {
        val images = mutableListOf(
            R.drawable.food_category,
            R.drawable.clothing,
            R.drawable.entertainment_category,
            R.drawable.transport,
            R.drawable.health,
            R.drawable.education,
            R.drawable.bill,
            R.drawable.others
        )
    }
}