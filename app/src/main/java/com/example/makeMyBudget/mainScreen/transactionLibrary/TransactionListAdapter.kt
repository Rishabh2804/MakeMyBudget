package com.example.makeMyBudget.mainScreen.transactionLibrary

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.runtime.compositionLocalOf
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

import com.example.makeMyBudget.entities.Transaction
import com.example.makeMyBudget.entities.TransactionStatus
import com.example.makeMyBudget.entities.TransactionType
import com.example.makeMyBudget.mainScreen.viewModels.TransactionViewModel
import com.example.makemybudget.R

class TransactionListAdapter(
    val transactionList: MutableList<Transaction>,
    val fragment: Fragment,
    val context: Context,
    private val viewModel: TransactionViewModel,
    private var listener: (Long) -> Unit
) : RecyclerView.Adapter<TransactionListAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.transaction_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindview(transactionList[position])
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    inner class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        init {
            val viewDetails: TextView = view.findViewById(R.id.profitOrLoss)
            viewDetails.setOnClickListener {
                listener.invoke(transactionList[adapterPosition].trans_id)
            }
        }

        val layout: ConstraintLayout = view.findViewById(R.id.item)
        val title: TextView = view.findViewById(R.id.year)
        val mode: TextView = view.findViewById(R.id.month_transaction)
        val amount: TextView = view.findViewById(R.id.amount)
        val date: TextView = view.findViewById(R.id.GainOrLoss)
        private val typeHere: View = view.findViewById(R.id.expenseOrIncome)
        val image: ImageView = view.findViewById(R.id.categorized_image)

        fun bindview(transaction: Transaction) {
            val isUpcoming = transaction.transactionDate.time > System.currentTimeMillis()

            if (transaction.transactionStatus == TransactionStatus.COMPLETED)
                layout.setBackgroundColor(statusColor[0])
            else if (isUpcoming)
                layout.setBackgroundColor(statusColor[1])
            else if (transaction.transactionStatus == TransactionStatus.PENDING)
                layout.setBackgroundColor(statusColor[2])

            title.text = transaction.title
            mode.text = transaction.transactionMode.name
            amount.text = transaction.transactionAmount.toString()
            date.text = transaction.transactionDate.toString()

            val type = transaction.transactionType
            if (type == TransactionType.INCOME)
                typeHere.setBackgroundColor(typeColor[0])
            else
                typeHere.setBackgroundColor(typeColor[1])

            image.setImageDrawable(
                ContextCompat.getDrawable(
                    image.context,
                    images[transaction.transactionCategory.ordinal]
                )
            )
        }
    }

    fun deleteTransaction(position: Int) {
        val builder = AlertDialog.Builder(context)
            .setTitle("Delete Transaction")
            .setMessage("Are you sure you want to delete this transaction?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.setTransId(transactionList[position].trans_id)
                viewModel.delete(viewModel.transaction.value!!)
                transactionList.removeAt(position)
                notifyItemRemoved(position)
            }
            .setNegativeButton("No") { _, _ ->
                // do nothing; 
                // just close the alert dialog
            }

        builder.create().show()
    }

    fun completeTransaction(position: Int) {
        viewModel.setTransId(transactionList[position].trans_id)
        viewModel.complete()
        val transaction = transactionList[position]
        transaction.transactionStatus = TransactionStatus.COMPLETED
        transactionList[position] = transaction
        notifyItemChanged(position)
    }

    val typeColor = arrayListOf(
        R.color.type_income,
        R.color.type_expense
    )

    val modeColor = arrayListOf(
        R.color.mode_cash,
        R.color.mode_credit_card,
        R.color.mode_debit_card
    )

    val statusColor = arrayListOf(
        R.color.status_completed,
        R.color.status_upcoming,
        R.color.status_pending,
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