package com.exarcplus.nsci.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.widget.AppCompatTextView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.exarcplus.nsci.MainActivity
import com.exarcplus.nsci.Model.Thought_Data
import com.exarcplus.nsci.Model.Thought_List
import com.exarcplus.nsci.R
import com.exarcplus.nsci.Remote.APIService
import com.exarcplus.nsci.Remote.ApiUtils
import com.exarcplus.nsci.SessionManagement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ThoughtFragment : Fragment() {

    private lateinit var session : SessionManagement
    private lateinit var api:String
    private lateinit var sidemenu_click: ImageView
    private lateinit var tv_thought : AppCompatTextView
    private var TAG ="ThoughtFragment"
    private lateinit var mAPIService : APIService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view1 = inflater.inflate(R.layout.fragment_thought, container, false)
        sidemenu_click = view1.findViewById(R.id.icon_sidemenu_thou)
        tv_thought = view1.findViewById(R.id.quote)
        mAPIService = ApiUtils.getAPIService()
        session = SessionManagement(activity)
        val user = session.getUserDetails()
        val MEMBER_ID = user.get(SessionManagement.KEY_MEMBER_ID)
        val TOKEN_ID = user.get(SessionManagement.KEY_TOKEN)
        api ="/mobile/thought_of_the_day/?member_id="  + MEMBER_ID + "&token=" + TOKEN_ID
        Log.i(TAG,"api-->" + api)

        mAPIService.RetrieveThoght(api).enqueue(object : Callback<Thought_List>{
           override fun onResponse(call: Call<Thought_List>?, response: Response<Thought_List>?) {
               Log.e(TAG, "Response-->" + response!!.body().result)
               if (response!!.isSuccessful()) {
                   Log.i(TAG,"quote" + response.body().data.get(0).quotes )
                    tv_thought.setText("\n \" "+ response.body().data.get(0).name + "\"\n")
               }
           }

           override fun onFailure(call: Call<Thought_List>?, t: Throwable?) {
               Log.e(TAG, "Response-->fail")
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
