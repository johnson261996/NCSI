package com.exarcplus.nsci.Fragments

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import com.exarcplus.nsci.Adapters.EventUpcoming_ListAdapter
import com.exarcplus.nsci.Model.UpcomingEvent_Data
import com.exarcplus.nsci.Model.UpcomingEvent_List
import com.exarcplus.nsci.R
import com.exarcplus.nsci.Remote.APIService
import com.exarcplus.nsci.Remote.ApiUtils
import com.exarcplus.nsci.SessionManagement
import com.exarcplus.nsci.UpcomingEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat

class EventUpcomingFragment : Fragment() {

    private lateinit var session : SessionManagement
    private lateinit var api:String
    private  var TAG = "EventUpcomingFragment"
    private lateinit var listview : ListView
    private lateinit var adapter: EventUpcoming_ListAdapter
    private lateinit var rowItems:ArrayList<UpcomingEvent_Data>
    private lateinit var mAPIService : APIService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view1 = inflater.inflate(R.layout.fragment_eventupcoming, container, false)
        session = SessionManagement(activity)
        val user = session.getUserDetails()
        val MEMBER_ID = user.get(SessionManagement.KEY_MEMBER_ID)
        val TOKEN_ID = user.get(SessionManagement.KEY_TOKEN)
        api ="/mobile/event/upcomming/?member_id="  + MEMBER_ID + "&token=" + TOKEN_ID
        Log.i(TAG,"api-->" + api)
        mAPIService = ApiUtils.getAPIService()
        listview = view1.findViewById(R.id.eventupcoming_listview)
        rowItems = ArrayList()

        mAPIService.RetrieveUpcomingEvent(api).enqueue(object : Callback<UpcomingEvent_List> {
            override fun onResponse(call: Call<UpcomingEvent_List>?, response: Response<UpcomingEvent_List>?) {
                Log.e(TAG,"Response-->" + response!!.body().result)
                if(response!!.isSuccessful()){
                    rowItems = response.body().data as ArrayList<UpcomingEvent_Data>
                    adapter = EventUpcoming_ListAdapter(activity!!, rowItems)
                    listview.setAdapter(adapter)
                    Log.e(TAG,"rowItems -->" + rowItems.size)
                    adapter.notifyDataSetChanged()
                }
                else{
                    Snackbar.make(view1,"Something went wrong", Snackbar.LENGTH_SHORT)
                }
            }

            override fun onFailure(call: Call<UpcomingEvent_List>?, t: Throwable?) {
                Log.e("Member:","unable to retrieve from api=>"+"failed")
            }
        })


        listview.setOnItemClickListener{parent, view, position, id ->
            Toast.makeText(activity, "Clicked item : $position", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, UpcomingEvent::class.java)
            var str = rowItems.get(position).date.split("-")
            var day:Int =  Integer.parseInt(str[2])
            var month:Int = Integer.parseInt(str[1])
            var monthname = DateFormatSymbols().months[month-1]
            var dateformat = SimpleDateFormat("MMM")
            var mon = dateformat.format(dateformat.parse(monthname))
            intent.putExtra("position", position)
            intent.putExtra("Key_title",rowItems.get(position).title)
            intent.putExtra("key_loc", rowItems.get(position).venue.toString())
            intent.putExtra("key_date","" + day + "\n" + mon)
            intent.putExtra("key_des",rowItems.get(position).descriptiup)
            intent.putExtra("key_website",rowItems.get(position).venueUrl)
            intent.putExtra("key_interested",rowItems.get(position).interestedStatus)
            intent.putExtra("key_start",rowItems.get(position).fromTime)
            intent.putExtra("key_end",rowItems.get(position).toTime)
            intent.putExtra("key_image",rowItems.get(position).image)
            intent.putExtra("key_id",rowItems.get(position).id)
            intent.putExtra("key_created",rowItems.get(position).created)
            startActivity(intent)
        }

        return view1


    }
}
