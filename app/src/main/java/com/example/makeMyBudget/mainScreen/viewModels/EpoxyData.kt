package com.example.makeMyBudget.mainScreen.viewModels

import com.example.makeMyBudget.daoS.MonthDetail
import com.example.makeMyBudget.mainScreen.transactionLibrary.MonthCardDetail
import com.example.makeMyBudget.mainScreen.tabs.MonthlyTimelineTabFragment.Companion.months

data class EpoxyData(val year: Int, var mutableList: MutableList<MonthCardDetail>)

fun convertIntoEpoxyData(
    map: Map<Int, List<MonthDetail>>,
//    sharedPreferences: SharedPreferences
): MutableList<EpoxyData> {
    val epoxyDataList = mutableListOf<EpoxyData>()
    for (key in map.keys) {
        val epoxyData = EpoxyData(key, mutableListOf())
        for (monthDetail in map[key]!!) {
            var expense = 0.0
            var profit = 0.0
            expense += monthDetail.expense
            profit += monthDetail.gain
            val amount: Double = profit - expense
            val month: String = months[monthDetail.month - 1]
            val monthCardDetail =
                MonthCardDetail(month, amount, expense, monthDetail.monthYear, profit)
            epoxyData.mutableList.add(monthCardDetail)
        }
        epoxyDataList.add(epoxyData)
    }
    return epoxyDataList
}