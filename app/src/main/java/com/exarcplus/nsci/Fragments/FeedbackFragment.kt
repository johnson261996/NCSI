package com.exarcplus.nsci.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.exarcplus.nsci.MainActivity
import com.exarcplus.nsci.Model.Feedback_Post
import com.exarcplus.nsci.R
import com.exarcplus.nsci.Remote.APIService
import com.exarcplus.nsci.Remote.ApiUtils
import com.exarcplus.nsci.SessionManagement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FeedbackFragment : Fragment() {

    private lateinit var session : SessionManagement
    private lateinit var api:String
    private var TAG ="Feedback"
    private lateinit var mAPIService : APIService
    private lateinit var sidemenu_click: ImageView
    private lateinit var subject : EditText
    private lateinit var message : EditText
    private lateinit var btn_submit : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view1 = inflater.inflate(R.layout.fragment_feedback, container, false)
        session = SessionManagement(activity)
        val user = session.getUserDetails()
        val MEMBER_ID = user.get(SessionManagement.KEY_MEMBER_ID)
        val TOKEN_ID = user.get(SessionManagement.KEY_TOKEN)
        api ="/mobile/feedback/?member_id="  + MEMBER_ID + "&token=" + TOKEN_ID
        Log.i(TAG,"api-->" + api)
        sidemenu_click = view1.findViewById(R.id.icon_sidemenu_feed)
        subject = view1.findViewById(R.id.et_feedsubject)
        message = view1.findViewById(R.id.et_feedmessage)
        btn_submit = view1.findViewById(R.id.btn_feedsubmit)
        mAPIService = ApiUtils.getAPIService()

        btn_submit.setOnClickListener {
            val sub = subject.text.toString()
            val mesg = message.text.toString()

            if(validate())
            {
                mAPIService.saveFeedbackPost(api,sub,mesg).enqueue(object : Callback<Feedback_Post> {
                    override fun onResponse(call: Call<Feedback_Post>?, response: Response<Feedback_Post>?) {
                        Log.e(TAG,"Feedback post submitted successfully to api" + response!!.body().result)
                        val builder = AlertDialog.Builder(view1.context)
                        builder.setTitle("Message")
                        builder.setMessage("Feedback submitted Successfully")
                            .setCancelable(false)
                            .setPositiveButton("OK") { dialog, id ->
                                btn_submit.setEnabled(false)
                                btn_submit.setBackgroundColor(resources.getColor(R.color.light_gray))
                                subject.setText("")
                                message.setText("")
                            }
                        val alert = builder.create()
                        alert.show()
                    }

                    override fun onFailure(call: Call<Feedback_Post>?, t: Throwable?) {
                        Log.e(TAG,"Unable to submit api")
                        Toast.makeText(activity,"Unable to submit feedback",Toast.LENGTH_SHORT).show()
                    }
                })

            }

        }
        sidemenu_click.setOnClickListener {
            if (MainActivity.drawerMenuLayout.isDrawerOpen(GravityCompat.END)) {
                MainActivity.drawerMenuLayout.closeDrawer(GravityCompat.END)
            } else {
                MainActivity.drawerMenuLayout.openDrawer(GravityCompat.END)
            }
        }
        return view1
    }

    private fun validate():Boolean{
        var valid = true
        val sub = subject.text.toString()
        val mesg = message.text.toString()

        if (sub.isEmpty()) {
            subject!!.setError("Enter a Subject")
            valid = false
        } else {
            subject!!.setError(null)
        }

        if (mesg.isEmpty()) {
            message!!.setError("Enter Message")
            valid = false
        } else {
            message!!.setError(null)
        }

        return valid
    }



}
