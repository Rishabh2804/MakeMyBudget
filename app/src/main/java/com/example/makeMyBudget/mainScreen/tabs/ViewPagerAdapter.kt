package com.example.makeMyBudget.mainScreen.tabs

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(val fragment: Fragment) : FragmentStateAdapter(fragment) {

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
                MonthlyTimelineTabFragment(fragment)
            }
        }
    }
}