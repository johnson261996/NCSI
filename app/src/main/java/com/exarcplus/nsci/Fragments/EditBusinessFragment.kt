package com.exarcplus.nsci.Fragments

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatButton
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.exarcplus.nsci.LoginActivity
import com.exarcplus.nsci.Model.Profile_List
import com.exarcplus.nsci.Model.Update_Post
import com.exarcplus.nsci.R
import com.exarcplus.nsci.Remote.APIService
import com.exarcplus.nsci.Remote.ApiUtils
import com.exarcplus.nsci.SessionManagement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditBusinessFragment : Fragment() {
    private lateinit var session : SessionManagement
    private lateinit var api:String
    private lateinit var api2:String
    private var TAG:String = "Profile"
    private lateinit var email: EditText
    private lateinit var mobile_no: EditText
    private lateinit var qualification: EditText
    private lateinit var experience: EditText
    private lateinit var Residential_addr: EditText
    private lateinit var update_btn: AppCompatButton
    private lateinit var mAPIService : APIService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view1 = inflater.inflate(R.layout.fragment_editbusinessprofile, container, false)
        email = view1.findViewById(R.id.et_epemail)
        mobile_no = view1.findViewById(R.id.et_epmobileno)
        qualification = view1.findViewById(R.id.et_epqualification)
        experience = view1.findViewById(R.id.et_epexp)
        Residential_addr = view1.findViewById(R.id.et_epaddr)
        update_btn = view1.findViewById(R.id.btn_update)
        mAPIService = ApiUtils.getAPIService()
        session = SessionManagement(activity)
        val user = session.getUserDetails()
        val MEMBER_ID = user.get(SessionManagement.KEY_MEMBER_ID)
        val TOKEN_ID = user.get(SessionManagement.KEY_TOKEN)
        api ="/mobile/update_profile/?member_id="  + MEMBER_ID + "&token=" + TOKEN_ID
        api2 = "/mobile/profile/?member_id=" + MEMBER_ID + "&token=" + TOKEN_ID
        Log.i(TAG,"api-->" + api)
        mAPIService.RetrieveProfile(api2).enqueue(object : Callback<Profile_List> {
            override fun onResponse(call: Call<Profile_List>?, response: Response<Profile_List>?) {
                Log.e(TAG,"Response-->" + response!!.body().result)
                if(response.isSuccessful()){
                    Snackbar.make(view1,"Sucessfully retrieved the Profile data", Snackbar.LENGTH_SHORT).show()
                    email.setText(response.body().data.get(0).email)
                    mobile_no.setText(response.body().data.get(0).mobileNo)
                    experience.setText(response.body().data.get(0).experience)
                    Residential_addr.setText(response.body().data.get(0).address)
                    qualification.setText(response.body().data.get(0).qualification)
                }
                else{
                    Snackbar.make(view1,"Something went wrong", Snackbar.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Profile_List>?, t: Throwable?) {
                Log.e(TAG,"Response-->fail" )
            }
        })

        update_btn.setOnClickListener {
            val name = ""
            val mobile_no = mobile_no.text.toString().trim()
            val email = email.text.toString().trim()
            val qualification = qualification.text.toString().trim()
            val experience = experience.text.toString().trim()
            val address = Residential_addr.text.toString().trim()
            val image = ""
            val committee_name = ""

            mAPIService.UpdateProfile(api,name,committee_name,qualification,email,image,address,experience,mobile_no)
                .enqueue(object: Callback<Update_Post> {
                    override fun onResponse(call: Call<Update_Post>?, response: Response<Update_Post>?) {
                        if(response!!.body().result.equals("success")){
                            //Toast.makeText(activity,"post successfully submitted to API",Toast.LENGTH_SHORT).show()
                            Log.i(TAG, "post successfully submitted to API." + response!!.body().result)
                            val snackBar = Snackbar.make(getActivity()!!.findViewById(android.R.id.content),
                                "Profile Updated Successfully", Snackbar.LENGTH_LONG)
                            update_btn.isClickable = false
                            update_btn.setBackgroundColor(resources.getColor(R.color.light_gray))
                            snackBar.show()
                        }else {
                            val snackBar = Snackbar.make(getActivity()!!.findViewById(android.R.id.content),
                                "Please try again", Snackbar.LENGTH_LONG)
                            snackBar.show()
                        }
                    }

                    override fun onFailure(call: Call<Update_Post>?, t: Throwable?) {
                        val snackBar = Snackbar.make(getActivity()!!.findViewById(android.R.id.content),
                            "unable to update Profile", Snackbar.LENGTH_LONG)
                        snackBar.show()
                    }
                })
        }

        return view1
    }

}
