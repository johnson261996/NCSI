package com.exarcplus.nsci

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.content.Intent
import android.graphics.Rect
import android.support.v7.app.AlertDialog
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.exarcplus.nsci.Model.Login_Post
import com.exarcplus.nsci.Remote.APIService
import com.exarcplus.nsci.Remote.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private var TAG ="Login"
    private lateinit var toolbar : Toolbar
    private lateinit var btn_login : Button
    private lateinit var _email : EditText
    private lateinit var paswd : EditText
    private lateinit var tv_forgot_paswd : TextView
    private lateinit var tv_regiser : TextView
    private lateinit var mAPIService : APIService
    private lateinit var session : SessionManagement

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        session = SessionManagement(applicationContext)
        toolbar = findViewById(R.id.toolbar)
        _email = findViewById(R.id.et_loginEmail)
        paswd = findViewById(R.id.et_loginpaswd)
        btn_login = findViewById(R.id.btn_login)
        tv_forgot_paswd = findViewById(R.id.tv_forgotpasswd)
        tv_regiser = findViewById(R.id.tv_register)
        mAPIService = ApiUtils.getAPIService()

        tv_forgot_paswd.setOnClickListener {
            startActivity(Intent(this@LoginActivity,ForgotActivity::class.java))
        }

        tv_regiser.setOnClickListener {
            startActivity(Intent(this@LoginActivity,SignupActivity::class.java))
        }


        //click the login button
        btn_login.setOnClickListener {
            login()
        }
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev!!.getAction() === MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains((ev!!.getRawX() as Float).toInt(), (ev.getRawY() as Float).toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
    private fun login()
    {
        if(!validate()){
            onLoginFailed()
            return
        }else{
            onLoginSucess()
        }
    }

    private fun onLoginSucess() {
        val email_id = _email.text.toString()
        val password = paswd.text.toString()
        mAPIService.saveLoginPost(email_id,password).enqueue(object : Callback<Login_Post>{
            override fun onResponse(call: Call<Login_Post>?, response: Response<Login_Post>?) {
                if(response!!.body().result.equals("success")){
                    Log.e(TAG,"Login post submitted successfully to api" + response.body().result)
                    var token=response.body().token
                    var member=response.body().data.get(0).get_id()
                    var token_id = token
                    var member_id = member
                    Log.i(TAG,"token-->" + token)
                    Log.i(TAG,"member id-->" + member)
                    val builder = AlertDialog.Builder(this@LoginActivity)
                    builder.setTitle("Message")
                    builder.setMessage("Login Success")
                        .setCancelable(false)
                        .setPositiveButton("OK") { dialog, id ->
                            //do things
                            // Creating user login session
                            // For testing i am stroing name, email as follow
                            // Use user real data
                            session.createLoginSession(password, email_id,member_id,token_id)
                            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                            finish()
                        }
                    val alert = builder.create()
                    alert.show()
                }
                else if(response!!.body().result.equals("Invalid password")){

                    val builder = AlertDialog.Builder(this@LoginActivity)
                    builder.setTitle("Message")
                    builder.setMessage("Password is Invalid")
                        .setCancelable(false)
                        .setPositiveButton("OK") { dialog, id ->
                            //do things
                        }
                    val alert = builder.create()
                    alert.show()
                }else if(response!!.body().result.equals("Check your email")){
                    val builder = AlertDialog.Builder(this@LoginActivity)
                    builder.setTitle("Message")
                    builder.setMessage("Email is incorrect")
                        .setCancelable(false)
                        .setPositiveButton("OK") { dialog, id ->
                            //do things

                        }
                    val alert = builder.create()
                    alert.show()
                }else if(response.body().result.equals("pending")){
                    val builder = AlertDialog.Builder(this@LoginActivity)
                    builder.setTitle("Message")
                    builder.setMessage("Wait for the admin's approval")
                        .setCancelable(false)
                        .setPositiveButton("OK") { dialog, id ->
                            //do things

                        }
                    val alert = builder.create()
                    alert.show()
                }

            }

            override fun onFailure(call: Call<Login_Post>?, t: Throwable?) {
                Log.e(TAG,"Unable to submit api")
            }
        })

    }

    private fun onLoginFailed() {
        btn_login!!.setEnabled(true);
    }

    private fun validate():Boolean{
        var valid = true

        val email_id = _email.text.toString()
        val password = paswd.text.toString()

        if (email_id.isEmpty() || !Patterns.EMAIL_ADDRESS!!.matcher(email_id).matches()) {
            _email!!.setError("Enter a valid email address")
            valid = false
        } else {
            _email!!.setError(null)
        }

        if (password.isEmpty()) {
            paswd!!.setError("Enter valid Password")
            valid = false
        } else {
            paswd!!.setError(null)
        }

        return valid
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home)
        // Press Back Icon
        {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
