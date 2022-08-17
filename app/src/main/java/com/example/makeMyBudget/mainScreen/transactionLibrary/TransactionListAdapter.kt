package com.example.makeMyBudget.mainScreen.transactionLibrary

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.makeMyBudget.entities.Transaction
import com.example.makeMyBudget.entities.TransactionStatus
import com.example.makeMyBudget.entities.TransactionType
import com.example.makeMyBudget.mainScreen.viewModels.TransactionViewModel
import com.example.makemybudget.R
import pl.droidsonroids.gif.GifImageView
import java.text.SimpleDateFormat
import java.util.*

class TransactionListAdapter(
    var transactionList: MutableList<Transaction>,
    val fragment: Fragment,
    val context: Context,
    private val viewModel: TransactionViewModel,
    private var listener: (Long) -> Unit,
) : RecyclerView.Adapter<TransactionListAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.transaction_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindView(transactionList[position])
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    inner class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        init {
            val viewDetails: TextView = view.findViewById(R.id.view_details)
            viewDetails.setOnClickListener {
                listener.invoke(transactionList[absoluteAdapterPosition].trans_id)
            }
        }

        val layout: ConstraintLayout = view.findViewById(R.id.trans_item)
        val title: TextView = view.findViewById(R.id.year)
        val mode: TextView = view.findViewById(R.id.trans_mode)
        val amount: TextView = view.findViewById(R.id.amount)
        val date: TextView = view.findViewById(R.id.trans_date)
        private val typeHere: View = view.findViewById(R.id.expenseOrIncome)
        val image: ImageView = view.findViewById(R.id.categorized_image)

        fun bindView(transaction: Transaction) {
            val isUpcoming = transaction.transactionDate > Date()
            if (transaction.transactionStatus == TransactionStatus.COMPLETED)
                layout.setBackgroundResource(statusColor[0])
            else if (isUpcoming)
                layout.setBackgroundResource(statusColor[1])
            else if (transaction.transactionStatus == TransactionStatus.PENDING)
                layout.setBackgroundResource(statusColor[2])

            title.text = transaction.title
            mode.text = transaction.transactionMode.name
            amount.text = transaction.transactionAmount.toString()
            date.text =
                SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(transaction.transactionDate)

            val type = transaction.transactionType
            if (type == TransactionType.INCOME)
                typeHere.setBackgroundResource(typeColor[0])
            else
                typeHere.setBackgroundResource(typeColor[1])

            image.setImageDrawable(
                ContextCompat.getDrawable(
                    image.context,
                    images[transaction.transactionCategory.ordinal]
                )
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteTransaction(position: Int, transactionList2: MutableList<Transaction>) {
        val layout =
            LayoutInflater.from(context).inflate(R.layout.fragment_transaction_completed, null)
        "Transaction Deleted :)".also {
            layout.findViewById<TextView>(R.id.text_transaction).text = it
        }
        layout.findViewById<GifImageView>(R.id.gif_complete)
            .setBackgroundResource(R.drawable.deleted_transaction)
        val builder = AlertDialog.Builder(context)
            .setTitle("Delete Transaction")
            .setMessage("Are you sure you want to delete this transaction?")
            .setCancelable(true)
            .setPositiveButton("YES") { _, _ ->
                viewModel.delete(transactionList2[position])
                notifyDataSetChanged()
                val dialog = AlertDialog.Builder(context)
                    .setView(layout)
                    .setNegativeButton("OK") { _, _ ->

                    }
                dialog.create().show()
            }
            .setNegativeButton("NO") { _, _ ->

            }
        builder.create().show()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun completeTransaction(position: Int, transactionList2: MutableList<Transaction>) {
        val layout =
            LayoutInflater.from(context).inflate(R.layout.fragment_transaction_completed, null)
        "Transaction Completed :)".also {
            layout.findViewById<TextView>(R.id.text_transaction).text = it
        }
        layout.findViewById<GifImageView>(R.id.gif_complete)
            .setBackgroundResource(R.drawable.transaction_completed)
        val builder = AlertDialog.Builder(context)
            .setTitle("Complete Transaction")
            .setMessage("Mark this transaction as completed?")
            .setCancelable(true)
            .setPositiveButton("YES") { _, _ ->
                viewModel.complete(transactionList2[position])
                notifyDataSetChanged()
                val dialog = AlertDialog.Builder(context)
                    .setView(layout)
                    .setNegativeButton("OK") { _, _ ->

                    }
                dialog.create().show()
            }
            .setNegativeButton("NO") { _, _ ->

            }
        builder.create().show()
    }

    val typeColor = arrayListOf(
        R.color.type_expense,
        R.color.type_income,
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