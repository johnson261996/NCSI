package com.exarcplus.nsci.Fragments

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatButton
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.exarcplus.nsci.*
import com.exarcplus.nsci.Model.Committe_List
import com.exarcplus.nsci.Model.Profile_List
import com.exarcplus.nsci.Model.Update_Post
import com.exarcplus.nsci.Remote.APIService
import com.exarcplus.nsci.Remote.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class EditPersonalFragment : Fragment() {
    private lateinit var api:String
    private lateinit var api2:String
    private var TAG:String = "Profile"
    private lateinit var committee_name:String

    private lateinit var username: EditText
    private lateinit var email:EditText
    private lateinit var mobile_no:EditText
    private lateinit var qualification:EditText
    private lateinit var experience:EditText
    private lateinit var Residential_addr:EditText
    private lateinit var committe:Spinner
    private lateinit var update_btn:AppCompatButton
    private lateinit var session :SessionManagement
    private lateinit var mAPIService : APIService



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view1 = inflater.inflate(R.layout.fragment_editpersonprofile, container, false)
        username = view1.findViewById(R.id.et_epusername)
        email = view1.findViewById(R.id.et_epemail)
        mobile_no = view1.findViewById(R.id.et_epmobileno)
        qualification = view1.findViewById(R.id.et_epqualification)
        experience = view1.findViewById(R.id.et_epexp)
        Residential_addr = view1.findViewById(R.id.et_epaddr)
        committe = view1.findViewById(R.id.eProfile_spinner)
        update_btn = view1.findViewById(R.id.btn_update)
        session = SessionManagement(context)
        mAPIService = ApiUtils.getAPIService()
        session = SessionManagement(activity)
        val user = session.getUserDetails()
        val MEMBER_ID = user.get(SessionManagement.KEY_MEMBER_ID)
        val TOKEN_ID = user.get(SessionManagement.KEY_TOKEN)
        api ="/mobile/update_profile/?member_id=" + MEMBER_ID + "&token=" + TOKEN_ID
        api2 = "/mobile/profile/?member_id=" + MEMBER_ID + "&token=" + TOKEN_ID
        Log.i(TAG,"api-->" + api)
        Log.i(TAG,"api-->" + api2)

        mAPIService.RetrieveProfile(api2).enqueue(object : Callback<Profile_List> {
            override fun onResponse(call: Call<Profile_List>?, response: Response<Profile_List>?) {
                Log.e(TAG,"Response-->" + response!!.body().result)
                if(response!!.isSuccessful()){
                    Snackbar.make(view1,"Sucessfully retrieved the Profile data", Snackbar.LENGTH_SHORT).show()
                    username.setText(response.body().data.get(0).name)
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

        mAPIService.RetrieveCommitte().enqueue(object :Callback<Committe_List>{

            override fun onResponse(call: Call<Committe_List>?, response: Response<Committe_List>?) {
                Log.e(TAG,"Response=>" + response!!.body().result)
                var items: ArrayList<String> = ArrayList()
                for (i in 0 until response.body().data.size){
                    Log.e(TAG,"Committe spinner items $i --->"+response.body().data.get(i).name)
                    items.add(response.body().data.get(i).name)
                }

                //Adapter for spinner
                committe.adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item, items)

                committe.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        committee_name = response.body().data.get(position).id
                    }
                }
            }

            override fun onFailure(call: Call<Committe_List>?, t: Throwable?) {
                Log.e(TAG,"Response=>"+"failed")            }
        })

        update_btn.setOnClickListener {
            update_profile(api)
        }


        return view1
    }

    private fun update_profile(api:String) {
        session = SessionManagement(activity)
        val user = session.getUserDetails()
        val name = username.getText().toString().trim()
        val mobile = mobile_no.text.toString().trim()
        val email_id = email.text.toString().trim()
        val quali = qualification.text.toString().trim()
        val experien = experience.text.toString().trim()
        val address = Residential_addr.text.toString().trim()
        var image:String? = user.get(SessionManagement.KEY_NEW_IMAGE)
        Log.i(TAG,"Before image path->" + image)
        if(image!!.isEmpty()){
            image = user.get(SessionManagement.KEY_Old_IMAGE)
        }
        Log.i(TAG,"After image path->" + image)

             if(validate(name,mobile,email_id,quali,experien,address)){
                 ProfileUpdateSuccess(api,name,quali,experien,address,image,email_id,mobile)
             }
    }

    private fun ProfileUpdateSuccess( api:String,
                                      name: String,
                                      qualification: String,
                                      experience: String,
                                      address: String,
                                      image:String?,
                                      email:String,
                                      mobile: String) {
        mAPIService.UpdateProfile(api,name,committee_name,qualification,email,image,address,experience,mobile)
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
                        // startActivity(Intent(activity,MainActivity::class.java))
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

    private fun validate(
        name: String,
        mobile: String,
        email_id: String,
        quali: String,
        experien: String,
        address: String
    ): Boolean {
        var valid = true

        if(name.isEmpty()){
            username.setError("Enter username")
            valid = false
        }else{
            username.setError(null)
        }


        if(experien.isEmpty()){
            experience.setError("Enter experience")
            valid = false
        }else{
            experience.setError(null)
        }

        if(address.isEmpty()){
            Residential_addr.setError("Enter address")
            valid = false
        }else{
            Residential_addr.setError(null)
        }


        if(quali.isEmpty()){
            qualification.setError("Enter qualification")
            valid = false
        }else{
            qualification.setError(null)
        }


        if (email_id.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email_id).matches()) {
            email.setError("Enter a valid email address")
            valid = false
        } else {
            email.setError(null)
        }


        if(mobile.isEmpty() || mobile.length!=10){
            mobile_no.setError("Enter valid phone number")
            valid = false
        }else{
            mobile_no.setError(null)
        }
        return  valid
    }


}
