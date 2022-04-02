package com.exarcplus.nsci.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.exarcplus.nsci.MainActivity
import com.exarcplus.nsci.R

class ChatFragment : Fragment() {
    private lateinit var sidemenu_click: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view1 = inflater.inflate(R.layout.fragment_chat, container, false)



        sidemenu_click = view1.findViewById(R.id.icon_sidemenu_chat)

        sidemenu_click.setOnClickListener {
            if (MainActivity.drawerMenuLayout.isDrawerOpen(GravityCompat.END)) {
                MainActivity.drawerMenuLayout.closeDrawer(GravityCompat.END)
            } else {
                MainActivity.drawerMenuLayout.openDrawer(GravityCompat.END)
            }
        }

        return view1

    }
}