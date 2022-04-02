package com.exarcplus.nsci.Fragments

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.exarcplus.nsci.LoginActivity
import com.exarcplus.nsci.Model.Profile_List
import com.exarcplus.nsci.R
import com.exarcplus.nsci.Remote.APIService
import com.exarcplus.nsci.Remote.ApiUtils
import com.exarcplus.nsci.SessionManagement
import com.pixplicity.fontview.FontTextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfilePersonalFragment:Fragment() {
    private lateinit var session : SessionManagement
    private lateinit var api:String
    private  var TAG:String = "Profile Personal"
    private lateinit var tv_email:FontTextView
    private lateinit var tv_mobile: FontTextView
    private lateinit var tv_qual:FontTextView
    private lateinit var tv_exp:FontTextView
    private lateinit var tv_addr:FontTextView
    private lateinit var imageview: ImageView

    private lateinit var mAPIService : APIService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view1 = inflater.inflate(R.layout.fragment_profile_personal, container, false)
        session = SessionManagement(activity)
        val user = session.getUserDetails()
        val MEMBER_ID = user.get(SessionManagement.KEY_MEMBER_ID)
        val TOKEN_ID = user.get(SessionManagement.KEY_TOKEN)
        api ="/mobile/profile/?member_id="  + MEMBER_ID + "&token=" + TOKEN_ID
        Log.i(TAG,"api-->" + api)
        tv_email = view1.findViewById(R.id.email)
        tv_mobile = view1.findViewById(R.id.mobile)
        tv_qual = view1.findViewById(R.id.qualification)
        tv_exp = view1.findViewById(R.id.exp)
        tv_addr = view1.findViewById(R.id.addr)

        mAPIService = ApiUtils.getAPIService()


        mAPIService.RetrieveProfile(api).enqueue(object : Callback<Profile_List> {
            override fun onResponse(call: Call<Profile_List>?, response: Response<Profile_List>?) {
                Log.e(TAG,"Response-->" + response!!.body().result)
                if(response!!.isSuccessful()){
                    Snackbar.make(view1,"Sucessfully retrieved the Profile data", Snackbar.LENGTH_SHORT).show()
                    tv_email.setText(response.body().data.get(0).email)
                    tv_mobile.setText(response.body().data.get(0).mobileNo)
                    tv_qual.setText(response.body().data.get(0).qualification)
                    tv_exp.setText(response.body().data.get(0).experience)
                    tv_addr.setText(response.body().data.get(0).address)

                }
                else{
                    Snackbar.make(view1,"Something went wrong",Snackbar.LENGTH_SHORT)
                }
            }

            override fun onFailure(call: Call<Profile_List>?, t: Throwable?) {
                Log.e(TAG,"Response-->fail" )
            }
        })

        return view1


    }

}