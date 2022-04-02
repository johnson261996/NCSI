package com.exarcplus.nsci.Fragments

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.exarcplus.nsci.Adapters.EventTabPageChangeAdapter
import com.exarcplus.nsci.Adapters.ProfileTabPageChangeAdapter
import com.exarcplus.nsci.MainActivity
import com.exarcplus.nsci.R

class EventFragment:Fragment() {

    private lateinit var sidemenu_click: ImageView
    private lateinit var tab_layout: TabLayout
    private lateinit var pager: ViewPager


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view1 = inflater.inflate(R.layout.fragment_events, container, false)
        sidemenu_click = view1.findViewById(R.id.icon_sidemenu_event)
        tab_layout = view1.findViewById(R.id.event_tab_layout)
        pager = view1.findViewById(R.id.event_pager)
        sidemenu_click.setOnClickListener {
            if (MainActivity.drawerMenuLayout.isDrawerOpen(GravityCompat.END)) {
                MainActivity.drawerMenuLayout.closeDrawer(GravityCompat.END)
            } else {
                MainActivity.drawerMenuLayout.openDrawer(GravityCompat.END)
            }
        }
        configureTab()
        return view1
    }

    private fun configureTab() {

        tab_layout.addTab(tab_layout.newTab().setText("UPCOMING"))
        tab_layout.addTab(tab_layout.newTab().setText("COMPLETED"))
        tab_layout.addTab(tab_layout.newTab().setText("ONGOING"))
        createAdapter(tab_layout)
    }

    private fun createAdapter(tab_layout: TabLayout) {
        val adapter =
            EventTabPageChangeAdapter(childFragmentManager, tab_layout.tabCount)
        pager.adapter = adapter
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                Log.e("Tab position:","..." + p0!!.position)
                pager.setCurrentItem(p0!!.position)
            }

        })
    }
}