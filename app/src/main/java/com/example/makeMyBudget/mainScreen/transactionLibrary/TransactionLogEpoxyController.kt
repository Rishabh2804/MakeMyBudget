package com.example.makeMyBudget.mainScreen.transactionLibrary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.makeMyBudget.entities.Transaction
import com.example.makeMyBudget.mainScreen.MainScreenFragmentDirections
import com.example.makeMyBudget.mainScreen.viewModels.TransactionViewModel
import com.example.makemybudget.R
import com.example.makemybudget.databinding.TransactionLogItemBinding

class TransactionLogEpoxyController(
    val fragment: Fragment,
    val context: Context,
    val viewModel: TransactionViewModel
) : RecyclerView.Adapter<TransactionLogEpoxyController.Holder>() {
    var transactionLog = mutableListOf<TransactionLogItem>()

    inner class Holder(val binding: TransactionLogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: TransactionLogItem) {

            val adapter =
                TransactionListAdapter(item.transactionLog, fragment, context, viewModel, listener)

            binding.apply {

                heading.text = item.title
                recyclerView.adapter = adapter
                recyclerView.layoutManager =
                    androidx.recyclerview.widget.LinearLayoutManager(recyclerView.context)

                recyclerView.visibility = if (item.title == "Completed") {
                    this.expandCollapse.setImageResource(R.drawable.ic_baseline_collapse_up)
                    View.VISIBLE
                } else {
                    this.expandCollapse.setImageResource(R.drawable.ic_baseline_expand_down)
                    View.GONE
                }

                cardView.setOnClickListener {
                    if (recyclerView.visibility == View.GONE) {
                        recyclerView.visibility = View.VISIBLE
                        this.expandCollapse.setImageResource(R.drawable.ic_baseline_collapse_up)
                    } else {
                        recyclerView.visibility = View.GONE
                        this.expandCollapse.setImageResource(R.drawable.ic_baseline_expand_down)
                    }
                }
            }
        }
    }

    private val listener: (id: Long) -> Unit = {
        fragment.findNavController().navigate(
            MainScreenFragmentDirections.actionMainScreenFragmentToTransactionDetailFragment(it, 1)
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            TransactionLogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindView(transactionLog[position])
    }

    override fun getItemCount(): Int {
        return transactionLog.size
    }

}

data class TransactionLogItem(
    val title: String = "",
    var transactionLog: MutableList<Transaction> = mutableListOf(),

    )