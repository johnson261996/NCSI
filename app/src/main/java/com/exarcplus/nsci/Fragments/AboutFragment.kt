package com.exarcplus.nsci.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.exarcplus.nsci.MainActivity
import com.exarcplus.nsci.Model.About_List
import com.exarcplus.nsci.R
import com.exarcplus.nsci.Remote.APIService
import com.exarcplus.nsci.Remote.ApiUtils
import com.exarcplus.nsci.SessionManagement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AboutFragment : Fragment() {
    private lateinit var session : SessionManagement
    private lateinit var api:String
    private var TAG ="About"
    private lateinit var mAPIService : APIService
    private lateinit var tv_about : TextView
    private lateinit var sidemenu_click: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
         var view1 = inflater.inflate(R.layout.fragment_about, container, false)
        session = SessionManagement(activity)
        val user = session.getUserDetails()
        val MEMBER_ID = user.get(SessionManagement.KEY_MEMBER_ID)
        val TOKEN_ID = user.get(SessionManagement.KEY_TOKEN)
        api ="/about_ncsi/?member_id=" + MEMBER_ID + "&token=" + TOKEN_ID
        Log.i(TAG,"api-->" + api)
        sidemenu_click = view1.findViewById(R.id.icon_sidemenu_about)
        tv_about = view1.findViewById(R.id.about)
        mAPIService = ApiUtils.getAPIService()

        mAPIService.RetrieveAboutus(api).enqueue(object : Callback<About_List> {
            override fun onResponse(call: Call<About_List>?, response: Response<About_List>?) {
            Log.e(TAG,"Response-->" + response!!.body().result)
                if (response!!.isSuccessful){
                    var about = Html.fromHtml(response!!.body().data.get(0).description.toString())
                    tv_about.setText(about)
                }
            }

            override fun onFailure(call: Call<About_List>?, t: Throwable?) {
                Log.e(TAG,"Response--> unsuccessful")
            }
        })

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
