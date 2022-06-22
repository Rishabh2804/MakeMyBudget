package com.example.makeMyBudget.mainScreen.TransactionLibrary

import com.airbnb.epoxy.AsyncEpoxyController

class MonthsEpoxyController() : AsyncEpoxyController() {
    var transactYears = mutableListOf<EpoxyData>()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        transactYears.forEachIndexed { index, epoxyData ->
            if(epoxyData.toDisplay) {

            }
        }
    }
}