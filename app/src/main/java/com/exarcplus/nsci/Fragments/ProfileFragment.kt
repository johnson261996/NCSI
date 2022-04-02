package com.exarcplus.nsci.Fragments

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.exarcplus.nsci.*
import com.exarcplus.nsci.Adapters.ProfileTabPageChangeAdapter
import com.exarcplus.nsci.Model.Profile_List
import com.exarcplus.nsci.Remote.APIService
import com.exarcplus.nsci.Remote.ApiUtils
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {
    private lateinit var session : SessionManagement
    private lateinit var api:String
    private  var TAG:String = "Profile"
    private lateinit var sidemenu_click: ImageView
    private lateinit var tab_layout: TabLayout
    private lateinit var pager: ViewPager
    private lateinit var profile_edit : TextView
    private lateinit var name: TextView
    private lateinit var qualification:TextView
    private lateinit var imageview:ImageView
    private lateinit var mAPIService : APIService
    private var store_dp:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view1 =inflater.inflate(R.layout.fragment_profile, container, false)
        session = SessionManagement(activity)
        val user = session.getUserDetails()
        val MEMBER_ID = user.get(SessionManagement.KEY_MEMBER_ID)
        val TOKEN_ID = user.get(SessionManagement.KEY_TOKEN)
        api ="/mobile/profile/?member_id=" + MEMBER_ID + "&token=" + TOKEN_ID
        Log.i(TAG,"api-->" + api)
        sidemenu_click = view1.findViewById(R.id.icon_sidemenu_profile)
         tab_layout = view1.findViewById(R.id.Profile_tab_layout)
        pager = view1.findViewById<ViewPager>(R.id.profile_pager)
        profile_edit = view1.findViewById(R.id.profile_edit)
        name = view1.findViewById(R.id.Profile_member_name)
        qualification = view1.findViewById(R.id.profile_specicalist)
        imageview = view1.findViewById(R.id.Profile_image_dp)
        mAPIService = ApiUtils.getAPIService()


        mAPIService.RetrieveProfile(api).enqueue(object : Callback<Profile_List> {
            override fun onResponse(call: Call<Profile_List>?, response: Response<Profile_List>?) {
                Log.e(TAG,"Response-->" + response!!.body().result)
                if(response!!.isSuccessful()){
                    Snackbar.make(view1,"Sucessfully retrieved the Profile data", Snackbar.LENGTH_SHORT).show()
                     name.setText(response.body().data.get(0).name)
                     qualification.setText(response.body().data.get(0).qualification)
                    store_dp = response.body().data.get(0).image
                    session.createOldImageSession(store_dp)
                    Picasso.with(context).load(response.body().data.get(0).image).placeholder(R.mipmap.launcher).error(R.mipmap.launcher).into(imageview)

                }
                else{
                    Snackbar.make(view1,"Something went wrong", Snackbar.LENGTH_SHORT)
                }
            }

            override fun onFailure(call: Call<Profile_List>?, t: Throwable?) {
                Log.e(TAG,"Response-->fail" )
            }
        })

        profile_edit.setOnClickListener {
            var intent = Intent(activity,ProfileEdit::class.java)
            intent.putExtra("key_image",store_dp)
            startActivity(intent)
            //startActivity(Intent(activity,ProfileEdit::class.java))
        }

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

        tab_layout.addTab(tab_layout.newTab().setText("PERSONAL"))
        tab_layout.addTab(tab_layout.newTab().setText("BUSINESS"))
        createAdapter(tab_layout)
    }

    private fun createAdapter(tab_layout: TabLayout) {
        val adapter =
                ProfileTabPageChangeAdapter(childFragmentManager, tab_layout.tabCount)
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
