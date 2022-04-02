package com.exarcplus.nsci

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.Toolbar
import android.text.Html
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.exarcplus.nsci.Model.InterestedUser
import com.exarcplus.nsci.Remote.APIService
import com.exarcplus.nsci.Remote.ApiUtils
import com.pixplicity.fontview.FontTextView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UpcomingEvent : AppCompatActivity() {

    private lateinit var session : SessionManagement
    private lateinit var api:String
    private var TAG="UpcomingEvent"
    private lateinit var image:ImageView
    private lateinit var title:TextView
    private lateinit var from_time:TextView
    private lateinit var to_time:TextView
    private lateinit var location:TextView
    private lateinit var date: FontTextView
    private lateinit var description: TextView
    private lateinit var contact_person:TextView
    private lateinit var website:TextView
    private lateinit var intested:TextView
    private lateinit var btn_iterested:Button
    private lateinit var mAPIService:APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setstatusbarcolorgradient()
        setContentView(R.layout.activity_upcoming_event)
        val toolbar : Toolbar = findViewById(R.id.event_toolbar)
        image = findViewById(R.id.event_image)
        title = findViewById(R.id.name_event)
        from_time = findViewById(R.id.event_schedulefrom)
        to_time = findViewById(R.id.event_scheduleto)
        location = findViewById(R.id.event_location)
        date = findViewById(R.id.day_event)
        description = findViewById(R.id.event_desc)
        contact_person = findViewById(R.id.contact)
        website = findViewById(R.id.event_webpage)
        intested = findViewById(R.id.event_interested)
        btn_iterested = findViewById(R.id.btn_intersted)
        mAPIService = ApiUtils.getAPIService()
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)


        session = SessionManagement(this)
        val user = session.getUserDetails()
        val MEMBER_ID = user.get(SessionManagement.KEY_MEMBER_ID)
        val TOKEN_ID = user.get(SessionManagement.KEY_TOKEN)
        api ="/mobile/update_interested_users/" + "5c5979d42f7a8f624afd12a0/?member_id="  + MEMBER_ID + "&token=" + TOKEN_ID
        Log.i(TAG,"api-->" + api)

        var intent : Intent = getIntent()
        var _title : String = intent.getStringExtra("Key_title")
        var _image: String = intent.getStringExtra("key_image")
        var _from_time: String = intent.getStringExtra("key_start")
        var _to_time: String = intent.getStringExtra("key_end")
        var _location : String = intent.getStringExtra("key_loc")
        var _date:String = intent.getStringExtra("key_date")
        var _description:String = intent.getStringExtra("key_des")
        var _contact_person: String = intent.getStringExtra("key_created")
        var _website: String = intent.getStringExtra("key_website")
        var _intested : String = intent.getStringExtra("key_interested")
        var venue = Html.fromHtml(_location)
        var desc = Html.fromHtml(_description)
        title.setText(_title)
        from_time.setText(_from_time)
        to_time.setText(_to_time)
        location.setText(venue)
        date.setText(_date)
        description.setText(desc)
        contact_person.setText(_contact_person)
        website.setText(_website)
        intested.setText(_intested)
        Picasso.with(application).load(_image).placeholder(R.mipmap.launcher).error(R.mipmap.launcher).into(image)
        btn_iterested.setOnClickListener {
            mAPIService.RetrieveInterestedUser(api).enqueue(object : Callback<InterestedUser> {

                override fun onResponse(call: Call<InterestedUser>?, response: Response<InterestedUser>?) {
                    Log.i(TAG,"response->"+response!!.body().result )
                    if (response!!.body().result.equals("success")) {
                        if (_intested.equals("0", ignoreCase = true)) {
                            _intested = "1"
                            btn_iterested.isClickable = false
                            btn_iterested.setBackgroundColor(resources.getColor(R.color.light_gray))
                        }

                    } else {
                        val snackBar = Snackbar.make(
                            findViewById(android.R.id.content),
                            "User already interested", Snackbar.LENGTH_LONG
                        ).show()
                        btn_iterested.isClickable = false
                    }

                }

                override fun onFailure(call: Call<InterestedUser>?, t: Throwable?) {
                    Log.i(TAG, "api failed")
                }
            })
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home)
        // Press Back Icon
        {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setstatusbarcolorgradient() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = getWindow()
            val background = getResources().getDrawable(R.drawable.gradient)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent))
            window.setNavigationBarColor(getResources().getColor(android.R.color.transparent))
            window.setBackgroundDrawable(background)
        }
    }
}
