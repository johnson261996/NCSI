package com.exarcplus.nsci.Fragments


import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import com.exarcplus.nsci.Adapters.GalleryBaseAdapter
import com.exarcplus.nsci.GalleryImages_Activity
import com.exarcplus.nsci.MainActivity
import com.exarcplus.nsci.Model.Gallery_Data
import com.exarcplus.nsci.Model.Gallery_List
import com.exarcplus.nsci.Model.Image
import com.exarcplus.nsci.R
import com.exarcplus.nsci.Remote.APIService
import com.exarcplus.nsci.Remote.ApiUtils
import com.exarcplus.nsci.SessionManagement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class GalleryFragment() :Fragment(), Serializable {

    private lateinit var session : SessionManagement
    private lateinit var api:String
    private  var TAG = "Gallery"
    private lateinit var sidemenu_click: ImageView
    private lateinit var listview : ListView
    private lateinit var mAPIService : APIService
    private lateinit var rowItems:ArrayList<Gallery_Data>
    private lateinit var adapter: GalleryBaseAdapter



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view1 = inflater.inflate(R.layout.fragment_gallery, container, false)
        session = SessionManagement(activity)
        val user = session.getUserDetails()
        val MEMBER_ID = user.get(SessionManagement.KEY_MEMBER_ID)
        val TOKEN_ID = user.get(SessionManagement.KEY_TOKEN)
        api ="/mobile/gallery_list/?member_id="  + MEMBER_ID + "&token=" + TOKEN_ID
        Log.i(TAG,"api-->" + api)
        sidemenu_click = view1.findViewById(R.id.icon_sidemenu_gallery)
        mAPIService = ApiUtils.getAPIService()

        sidemenu_click.setOnClickListener {
            if (MainActivity.drawerMenuLayout.isDrawerOpen(GravityCompat.END)) {
                MainActivity.drawerMenuLayout.closeDrawer(GravityCompat.END)
            } else {
                MainActivity.drawerMenuLayout.openDrawer(GravityCompat.END)
            }
        }
        listview = view1.findViewById(R.id.Gallery_listview)
        rowItems = ArrayList()

        mAPIService.RetrieveGallery(api).enqueue(object : Callback<Gallery_List>{
            override fun onResponse(call: Call<Gallery_List>?, response: Response<Gallery_List>?) {
                Log.e(TAG, "Response-->" + response!!.body())
                if (response!!.isSuccessful()) {
                    rowItems = response.body().data as ArrayList<Gallery_Data>

                    Log.e(TAG, "rowItems -->" + rowItems.size)
                    Toast.makeText(context, "Toatl images :" + response.body().data.get(0).images.size, Toast.LENGTH_SHORT).show()
                    adapter = GalleryBaseAdapter(activity!!, rowItems)
                    listview.setAdapter(adapter)
                    adapter.notifyDataSetChanged()
                } else {
                    Log.e(TAG, "Something went wrong" + "failed")
                }
            }
            override fun onFailure(call: Call<Gallery_List>?, t: Throwable?) {
                Log.e(TAG, "Unable to retrieve api")
            }
        })

        listview.setOnItemClickListener{parent, view, position, id ->

            Toast.makeText(activity, "Clicked item : $position", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, GalleryImages_Activity::class.java)
            var images:ArrayList<String> = ArrayList()
            Log.i(TAG,"Gallery size:$position" + rowItems.get(position).images.size)
            for (i in 0 until rowItems.get(position).images.size)
            {
                images.add(rowItems.get(position).images.get(i).image)
                Log.i(TAG,"Gallery images:$position-->" + rowItems.get(position).images.get(i).image)
            }
            intent.putExtra("key_images",images)
            startActivity(intent)
        }

        return view1

    }

}