package com.exarcplus.nsci.Fragments

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.exarcplus.nsci.MemberDetail
import com.exarcplus.nsci.Model.Member_Data
import com.exarcplus.nsci.R
import com.exarcplus.nsci.Remote.APIService
import com.exarcplus.nsci.Remote.ApiUtils
import com.pixplicity.fontview.FontTextView


class MemberPersonalFragment : Fragment() {

    private  var TAG = "Personal"
    private lateinit var tv_email: FontTextView
    private lateinit var tv_mobile: FontTextView
    private lateinit var tv_qual: FontTextView
    private lateinit var tv_exp: FontTextView
    private lateinit var tv_addr: FontTextView
    private lateinit var data: Array<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view1 = inflater.inflate(R.layout.fragment_mem_personal, container, false)

        tv_email = view1.findViewById(R.id.email_personal)
        tv_mobile = view1.findViewById(R.id.mobile_personal)
        tv_qual = view1.findViewById(R.id.qual_personal)
        tv_exp = view1.findViewById(R.id.exp_personal)
        tv_addr = view1.findViewById(R.id.addr_personal)
        data = MemberDetail.instance.getData()
        tv_email.setText(data.get(0))
        tv_mobile.setText(data.get(1))
        tv_qual.setText(data.get(2))
        tv_exp.setText(data.get(3))
        tv_addr.setText(data.get(4))
        return view1


    }
}
