package com.exarcplus.nsci

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.view.WindowManager
import android.os.Build



class AccountType : AppCompatActivity() {
    private lateinit var btn_signup : Button
    private lateinit var btn_signin : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setstatusbarcolorgradient()

        setContentView(R.layout.account_type)
        btn_signup = findViewById(R.id.btn_signup)
        btn_signin = findViewById(R.id.btn_signin)
        btn_signup.setOnClickListener {
            startActivity(Intent(this@AccountType,SignupActivity::class.java))
        }
        btn_signin.setOnClickListener {
            startActivity(Intent(this@AccountType,LoginActivity::class.java))
        }
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
