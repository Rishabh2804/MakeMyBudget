package com.example.makeMyBudget.mainScreen

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.AsyncEpoxyController
import com.example.makeMyBudget.entities.Transaction
import com.example.makeMyBudget.mainScreen.transactionLibrary.TransactionListAdapter
import com.example.makeMyBudget.mainScreen.viewModels.TransactionViewModel

class TransactionLogEpoxyController(
    val fragment: Fragment,
    val context: Context,
    val viewModel: TransactionViewModel
) : AsyncEpoxyController() {
    var transactionLog = mutableListOf<TransactionLogItem>()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {

        transactionLog.forEachIndexed { index, item ->

            val adapter =
                TransactionListAdapter(item.transactionLog, fragment, context, viewModel, listener)

            val swipeHandler = object : SwipeHandler() {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    if (direction == ItemTouchHelper.LEFT) {
                        adapter.deleteTransaction(viewHolder.adapterPosition)
                    } else if (direction == ItemTouchHelper.RIGHT) {
                        adapter.completeTransaction(viewHolder.adapterPosition)
                    }
                }
            }

            val itemTouchHelper = ItemTouchHelper(swipeHandler)

            transactionLog {
                id(index)
                title(item.title)
                adapter(adapter)
                itemTouchHelper(itemTouchHelper)
            }


        }
    }

    private val listener: (id: Long) -> Unit = {
        fragment.findNavController().navigate(
            MainScreenFragmentDirections.actionMainScreenFragmentToTransactionDetailFragment(it)
        )
    }

}

data class TransactionLogItem(
    val title: String = "",
    var transactionLog: MutableList<Transaction> = mutableListOf(),

    )