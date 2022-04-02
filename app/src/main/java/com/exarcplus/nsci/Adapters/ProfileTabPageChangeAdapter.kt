package com.exarcplus.nsci.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.exarcplus.nsci.Fragments.ProfileBusinessFragment
import com.exarcplus.nsci.Fragments.ProfilePersonalFragment

class ProfileTabPageChangeAdapter(fragmentManager: FragmentManager?, private var tabCount: Int) : FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment? {
        when(position){
            0 -> return ProfilePersonalFragment()
            1 -> return ProfileBusinessFragment()
            else -> return null
        }
    }


    override fun getCount(): Int {
        return tabCount
    }

}
