package com.example.makeMyBudget.TransactionLibrary

import com.airbnb.epoxy.AsyncEpoxyController

class EpoxyController() : AsyncEpoxyController() {
    var transactYears = mutableListOf<EpoxyData>()
        set(value) {
            field = value
            requestModelBuild()
        }


    override fun buildModels() {
        transactYears.forEachIndexed { index, epoxyData ->
            if(epoxyData.toDisplay) {
//are vai jaise tumne left wali screen oe click kiya, wahan to ek hi mahina select karoge na, to bas uss mahina ka calendar aa jayega
                //somry vro

            }
        }
    }
}