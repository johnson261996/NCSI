package com.exarcplus.nsci

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log

class LauncherScreen : AppCompatActivity() {

    private var TAG = "Session"

    private lateinit var  session: SessionManagement
    private lateinit var i : Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.launcherscreen)

        session = SessionManagement(applicationContext)
        var pref : SharedPreferences = getSharedPreferences("com.exarcplus.nsci", Context.MODE_PRIVATE)

         val handler = Handler()
         val r: Runnable = Runnable {

             if(session.isLoggedIn){
                 startActivity(Intent(this@LauncherScreen,MainActivity::class.java))
                 Log.e("LauncherScreen","session ==> true")
                 finish()
             }else{
                 if(pref.getBoolean("firstrun",true)) {
                     Log.e("LauncherScreen", "session ==> false")
                     pref.edit().putBoolean("firstrun", false).commit()
                     startActivity(Intent(this@LauncherScreen, AccountType::class.java))
                     finish()
                 }
                 else
                 {
                     startActivity(Intent(this@LauncherScreen, LoginActivity::class.java))
                     finish()
                 }
             }



        }
        handler.postDelayed(r, 1000);




    }



}
