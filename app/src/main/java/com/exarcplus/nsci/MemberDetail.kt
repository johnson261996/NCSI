package com.exarcplus.nsci

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.exarcplus.nsci.Adapters.MemberTabPageChangeAdapter
import com.mikhaellopez.circularimageview.CircularImageView
import com.squareup.picasso.Picasso

class MemberDetail : AppCompatActivity() {


    private lateinit var toolbar : Toolbar
    private lateinit var member_name : TextView
    private lateinit var member_phone : ImageView
    private lateinit var member_message : ImageView
    private lateinit var member_pic : CircularImageView
    private lateinit var member_qual : TextView
    companion object {
         lateinit var instance: MemberDetail
    }
    private lateinit var data:Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setstatusbarcolorgradient()
        setContentView(R.layout.activity_member_detail)
        instance = this
        toolbar = findViewById(R.id.member_toolbar)
        member_name = findViewById(R.id.member_name)
        member_phone = findViewById(R.id.image_phone)
        member_message = findViewById(R.id.image_message)
        member_pic = findViewById(R.id.image_dp)
        member_qual = findViewById(R.id.memver_specicalist)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        var intent : Intent = getIntent()
        var name : String = intent.getStringExtra("key_name")
        var image: String = intent.getStringExtra("key_image")
        var mobile: String = intent.getStringExtra("key_mobile")
        var qual: String = intent.getStringExtra("key_qual")
        var exp : String = intent.getStringExtra("key_exp")
        var email:String = intent.getStringExtra("key_email")
        var addr:String = intent.getStringExtra("key_addr")
        data = arrayOf(email,mobile,qual,exp,addr,name)
        member_name.setText(name)
        member_qual.setText(qual)
        Picasso.with(application).load(image).placeholder(R.mipmap.launcher).error(R.mipmap.launcher).into(member_pic)

        member_phone.setOnClickListener {
            var intent:Intent = Intent(Intent.ACTION_DIAL,Uri.fromParts("tel",mobile,null))
            startActivity(intent)
        }

        member_message.setOnClickListener {

            var smsUri: Uri = Uri.parse("sms:"+mobile)
            var intent:Intent = Intent(Intent.ACTION_VIEW)
            intent.setData(smsUri)
            intent.putExtra("sms_body","text message")
            startActivity(intent)
        }

        configureTab()
    }

    fun getActivityInstance():MemberDetail {
        return instance
    }
    fun getData():Array<String> {
        return this.data
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home)
        // Press Back Icon
        {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configureTab() {
        val tab_layout = findViewById<TabLayout>(R.id.tab_layout)
        tab_layout.addTab(tab_layout.newTab().setText("PERSONAL"))
        tab_layout.addTab(tab_layout.newTab().setText("BUSINESS"))
        createAdapter(tab_layout)
    }

    private fun createAdapter(tab_layout: TabLayout) {
        val pager = findViewById<ViewPager>(R.id.pager)
        val adapter =
            MemberTabPageChangeAdapter(supportFragmentManager, tab_layout.tabCount)
        pager.adapter = adapter
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                Log.e("Tab position:","..." + p0!!.position)
                pager.setCurrentItem(p0!!.position)
            }

        })
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
