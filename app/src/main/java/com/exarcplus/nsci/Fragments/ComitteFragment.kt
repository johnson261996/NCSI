package com.exarcplus.nsci.Fragments

import android.app.Activity
import android.content.ContentValues.TAG
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import com.exarcplus.nsci.Adapters.CommitteAdapter
import com.exarcplus.nsci.LoginActivity
import com.exarcplus.nsci.MainActivity
import com.exarcplus.nsci.Model.Member_Data
import com.exarcplus.nsci.Model.Member_List
import com.exarcplus.nsci.R
import com.exarcplus.nsci.Remote.APIService
import com.exarcplus.nsci.Remote.ApiUtils
import com.exarcplus.nsci.SessionManagement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ComitteFragment:Fragment() {
    private lateinit var session : SessionManagement
    private lateinit var api:String
    private lateinit var sidemenu_click: ImageView
    private lateinit var listview : ListView
    private lateinit var editText: EditText
    private lateinit var comItems:ArrayList<Member_Data>
    private lateinit var adapter: CommitteAdapter
    private lateinit var mAPIService : APIService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view1 = inflater.inflate(R.layout.fragment_comitte, container, false)
        session = SessionManagement(activity)
        val user = session.getUserDetails()
        val MEMBER_ID = user.get(SessionManagement.KEY_MEMBER_ID)
        val TOKEN_ID = user.get(SessionManagement.KEY_TOKEN)
        api ="/mobile/member_list?member_id="  + MEMBER_ID + "&token=" + TOKEN_ID
        Log.i(TAG,"api-->" + api)
        sidemenu_click = view1.findViewById(R.id.icon_sidemenu_com)
        mAPIService = ApiUtils.getAPIService()

        sidemenu_click.setOnClickListener {
            if (MainActivity.drawerMenuLayout.isDrawerOpen(GravityCompat.END)) {
                MainActivity.drawerMenuLayout.closeDrawer(GravityCompat.END)
            } else {
                MainActivity.drawerMenuLayout.openDrawer(GravityCompat.END)
            }
        }
        comItems = ArrayList()
        listview = view1.findViewById(R.id.listview_committe)
        editText = view1.findViewById(R.id.search_com)

        mAPIService.RetrieveMember(api).enqueue(object : Callback<Member_List> {
            override fun onResponse(call: Call<Member_List>?, response: Response<Member_List>?) {
                Log.e(TAG,"Response-->" + response!!.body().result)
                if(response!!.isSuccessful()){
                    comItems = response.body().data as ArrayList<Member_Data>

                    adapter = CommitteAdapter(activity!!, comItems)
                    listview.setAdapter(adapter)
                    Log.e(TAG,"rowItems -->" + comItems.size)
                    adapter.notifyDataSetChanged()
                }
                else{
                    Snackbar.make(view1,"Something went wrong", Snackbar.LENGTH_SHORT)
                }
            }

            override fun onFailure(call: Call<Member_List>?, t: Throwable?) {
                Log.e("Member:","unable to retrieve from api=>"+"failed")
            }
        })



        //hide keyboard when touch outside the edittext
        editText.setOnFocusChangeListener(object :View.OnFocusChangeListener{
            override fun onFocusChange(p0: View?, hasFocus: Boolean) {
                if(!hasFocus){
                    hidekeyboard(view1)
                }
            }
        })


        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(editable: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.e("SearchUrl():", "--->$editable")
                var text = editText.text.toString().toLowerCase(Locale.getDefault())
                adapter.getFilter(text)
            }

        })

        return view1

    }

    private fun hidekeyboard(view:View) {
        val inputMethodManager = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }

}