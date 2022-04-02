package com.exarcplus.nsci.Adapters
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.exarcplus.nsci.Fragments.MemberBusinessFragment
import com.exarcplus.nsci.Fragments.MemberPersonalFragment

class MemberTabPageChangeAdapter(fm:FragmentManager,private var tabcount : Int) : FragmentPagerAdapter(fm){
    override fun getItem(position: Int): Fragment? {
        when(position){
            0 -> return MemberPersonalFragment()
            1 -> return MemberBusinessFragment()
            else -> return  null
        }
    }

    override fun getCount(): Int {
        return tabcount
    }


}