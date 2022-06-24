package com.example.makeMyBudget.mainScreen.TransactionLibrary

import com.example.makeMyBudget.entities.Transaction

data class EpoxyData(
    val year: Int,
    val mutableList: MutableList<MonthCardDetail>,

    )

