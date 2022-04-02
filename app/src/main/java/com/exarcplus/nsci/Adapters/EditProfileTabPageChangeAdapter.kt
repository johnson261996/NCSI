package com.exarcplus.nsci.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.exarcplus.nsci.Fragments.EditBusinessFragment
import com.exarcplus.nsci.Fragments.EditPersonalFragment


class EditProfileTabPageChangeAdapter(fm: FragmentManager?,private var tabCount: Int) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        when(position){
            0 -> return EditPersonalFragment()
            1 -> return EditBusinessFragment()
            else -> return  null
        }
    }

    override fun getCount(): Int {
        return tabCount
    }
}
