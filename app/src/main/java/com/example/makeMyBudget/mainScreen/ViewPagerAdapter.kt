package com.example.makeMyBudget.mainScreen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.makeMyBudget.mainScreen.tabs.OverviewTabFragment
import com.example.makeMyBudget.mainScreen.tabs.TransactionTabFragment
import com.example.makeMyBudget.mainScreen.tabs.TransactionsLogTabFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                OverviewTabFragment()
            }
            1 -> {
                TransactionsLogTabFragment()
            }
            else -> {
                TransactionTabFragment()
            }
        }
    }





}