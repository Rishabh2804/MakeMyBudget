package com.example.makeMyBudget.mainScreen.TransactionLibrary

import androidx.fragment.app.Fragment
import com.airbnb.epoxy.AsyncEpoxyController

class YearEpoxyController(val fragment: Fragment) : AsyncEpoxyController() {
    var transactYears = mutableListOf<EpoxyData>()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        transactYears.forEachIndexed { index, epoxyData ->
            val monthAdapter = MonthAdapter(epoxyData.mutableList, fragment)


        }
    }
}