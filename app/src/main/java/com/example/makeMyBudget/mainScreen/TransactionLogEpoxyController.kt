package com.example.makeMyBudget.mainScreen

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.AsyncEpoxyController
import com.example.makeMyBudget.entities.Transaction
import com.example.makeMyBudget.mainScreen.TransactionLibrary.TransactionListAdapter

class TransactionLogEpoxyController(val fragment: Fragment) : AsyncEpoxyController() {
    var transactionLog = mutableListOf<TransactionLogItem>()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {

        transactionLog.forEachIndexed { index, item ->

            val adapter = TransactionListAdapter(item.transactionLog, fragment, listener)
            transactionLog {
                id(index)
                title(item.title)
                adapter(adapter)
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