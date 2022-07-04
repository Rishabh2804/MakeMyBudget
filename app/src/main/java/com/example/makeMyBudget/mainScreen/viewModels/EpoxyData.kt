package com.example.makeMyBudget.mainScreen.viewModels

import android.content.SharedPreferences
import com.example.makeMyBudget.daoS.MonthDetail
import com.example.makeMyBudget.mainScreen.transactionLibrary.MonthCardDetail
import com.example.makeMyBudget.mainScreen.tabs.YearMonthTabFragment.Companion.months

data class EpoxyData(
    val year: Int,
    val mutableList: MutableList<MonthCardDetail>,
)

fun convertIntoEpoxyData(
    map: Map<Int, List<MonthDetail>>,
    sharedPreferences: SharedPreferences
): MutableList<EpoxyData> {
    val epoxyDataList = mutableListOf<EpoxyData>()
    for (key in map.keys) {
        val epoxyData = EpoxyData(key, mutableListOf())
        for (monthDetail in map[key]!!) {
            val income = sharedPreferences.getString("income", "0")!!.toDouble()
            var expense = 0.0
            var profit = 0.0
            expense += monthDetail.expense
            profit += monthDetail.gain
            val amount: Double = income + profit - expense
            val month: String = months[monthDetail.month - 1]
        }
        epoxyDataList.add(epoxyData)
    }
    return epoxyDataList
}

