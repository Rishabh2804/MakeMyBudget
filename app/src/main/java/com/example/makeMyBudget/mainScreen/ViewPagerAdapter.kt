package com.example.makeMyBudget.mainScreen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.makeMyBudget.mainScreen.tabs.OverviewTabFragment
import com.example.makeMyBudget.mainScreen.tabs.YearMonthTabFragment
import com.example.makeMyBudget.mainScreen.tabs.TransactionsLogTabFragment

class ViewPagerAdapter(fm: FragmentManager, val fragment: Fragment) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                OverviewTabFragment()
            }
            1 -> {
                TransactionsLogTabFragment(fragment)
            }
            else -> {
                YearMonthTabFragment(fragment)
            }
        }
    }
}