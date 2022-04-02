package com.exarcplus.nsci

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.support.design.widget.NavigationView
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import com.exarcplus.nsci.Fragments.*

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var drawerMenuLayout: DrawerLayout
    }
    internal lateinit var navigationView: NavigationView
    private lateinit var session : SessionManagement
    private lateinit var btn_logout : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStatusBarGradiant(this@MainActivity)

        setContentView(R.layout.activity_main)
        btn_logout = findViewById(R.id.logout)
        session = SessionManagement(applicationContext)
     //   Toast.makeText(applicationContext, "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show()

        navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        var menu:Menu = navigationView.menu
        menu.findItem(R.id.chat).setVisible(false)
        drawerMenuLayout = findViewById<View>(R.id.drawerMenuLayout) as DrawerLayout

        val fragment = HomeFragment()
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameContainer, fragment)
        fragmentTransaction.commit()

        navigationView.setCheckedItem(R.id.home)
        navigationView.setNavigationItemSelectedListener(this)

        btn_logout.setOnClickListener {
            session.logoutUser()
            startActivity(Intent(this@MainActivity,LoginActivity::class.java))
            finish()
        }
    }

    private fun setStatusBarGradiant(activity: MainActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            val background = activity.resources.getDrawable(R.drawable.gradient)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = activity.resources.getColor(android.R.color.transparent)
            window.navigationBarColor = activity.resources.getColor(android.R.color.transparent)
            window.setBackgroundDrawable(background)
        }
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (item.itemId == R.id.home) run {

            val fragment = HomeFragment()
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameContainer, fragment)
            fragmentTransaction.commit()
            navigationView.setCheckedItem(R.id.home)

        } else if(item.itemId == R.id.comitte){
            val fragment = ComitteFragment()
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameContainer, fragment)
            fragmentTransaction.commit()
            navigationView.setCheckedItem(R.id.comitte)

        } else if(item.itemId == R.id.events){
            val fragment = EventFragment()
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameContainer, fragment)
            fragmentTransaction.commit()
            navigationView.setCheckedItem(R.id.events)

        } else if(item.itemId == R.id.gallery){
            val fragment = GalleryFragment()
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameContainer, fragment)
            fragmentTransaction.commit()
            navigationView.setCheckedItem(R.id.gallery)

        } else if(item.itemId == R.id.chat){
            val fragment = ChatFragment()
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameContainer, fragment)
            fragmentTransaction.commit()
            navigationView.setCheckedItem(R.id.chat)

        } else if(item.itemId == R.id.profile) {
            val fragment = ProfileFragment()
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameContainer, fragment)
            fragmentTransaction.commit()
            navigationView.setCheckedItem(R.id.profile)

        }else if(item.itemId == R.id.feedback) {
            val fragment = FeedbackFragment()
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameContainer, fragment)
            fragmentTransaction.commit()
            navigationView.setCheckedItem(R.id.feedback)

        }else if(item.itemId == R.id.thought) {
            val fragment = ThoughtFragment()
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameContainer, fragment)
            fragmentTransaction.commit()
            navigationView.setCheckedItem(R.id.thought)

        }else if(item.itemId == R.id.about) {
            val fragment = AboutFragment()
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameContainer, fragment)
            fragmentTransaction.commit()
            navigationView.setCheckedItem(R.id.about)
        }

        drawerMenuLayout.closeDrawer(GravityCompat.END)
        navigationView.setCheckedItem(id)

        return true
    }



}
