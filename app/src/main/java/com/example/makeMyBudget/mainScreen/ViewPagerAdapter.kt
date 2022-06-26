package com.example.makeMyBudget.mainScreen

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.makeMyBudget.mainScreen.tabs.OverviewTabFragment
import com.example.makeMyBudget.mainScreen.tabs.YearMonthTabFragment
import com.example.makeMyBudget.mainScreen.tabs.TransactionsLogTabFragment

class ViewPagerAdapter( val fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
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