package com.exarcplus.nsci.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import com.exarcplus.nsci.Fragments.*

class EventTabPageChangeAdapter(childFragmentManager: FragmentManager,private var tabCount: Int) :  FragmentPagerAdapter(childFragmentManager) {
    override fun getItem(position: Int): Fragment? {
        when(position){
            0 -> return EventUpcomingFragment()
            1 -> return EventCompletedFragment()
            2 -> return EventOngoingFragment()
            else -> return null
        }
    }

    override fun getCount(): Int {
        return tabCount
    }
}
