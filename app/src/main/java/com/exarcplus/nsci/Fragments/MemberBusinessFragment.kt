package com.exarcplus.nsci.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.exarcplus.nsci.MemberDetail
import com.exarcplus.nsci.R
import com.exarcplus.nsci.Remote.ApiUtils
import com.pixplicity.fontview.FontTextView

class MemberBusinessFragment : Fragment() {

    private lateinit var tv_email: FontTextView
    private lateinit var tv_mobile: FontTextView
    private lateinit var tv_exp: FontTextView
    private lateinit var tv_addr: FontTextView
    private lateinit var tv_name: FontTextView
    private lateinit var data: Array<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view1 = inflater.inflate(R.layout.fragment_mem_business, container, false)

        tv_name = view1.findViewById(R.id.name_business)
        tv_email = view1.findViewById(R.id.email_business)
        tv_mobile = view1.findViewById(R.id.mobile_business)
        tv_exp = view1.findViewById(R.id.exp_business)
        tv_addr = view1.findViewById(R.id.addr_business)
        data = MemberDetail.instance.getData()
        tv_email.setText(data.get(0))
        tv_mobile.setText(data.get(1))
        tv_exp.setText(data.get(3))
        tv_addr.setText(data.get(4))
        tv_name.setText(data.get(5))
        return view1
    }

}
