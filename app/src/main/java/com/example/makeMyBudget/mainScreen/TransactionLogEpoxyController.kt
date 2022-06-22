package com.example.makeMyBudget.mainScreen

import androidx.fragment.app.Fragment
import com.airbnb.epoxy.AsyncEpoxyController
import com.example.makeMyBudget.entities.Transaction

class TransactionLogEpoxyController(val fragment: Fragment) : AsyncEpoxyController() {
    var transactionLog = mutableListOf<TransactionLogItem>()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        transactionLog.forEachIndexed { index, item ->
            
        }
    }


}

data class TransactionLogItem(
    val title: String = "",
    val transactionLog: MutableList<Transaction> = mutableListOf(),


    )