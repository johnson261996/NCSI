package com.exarcplus.nsci

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.util.Patterns
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.*
import com.exarcplus.nsci.Model.Committe_List
import com.exarcplus.nsci.Model.Register_Post
import com.exarcplus.nsci.Remote.APIService
import com.exarcplus.nsci.Remote.ApiUtils
import com.mikhaellopez.circularimageview.CircularImageView
import id.zelory.compressor.Compressor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class SignupActivity : AppCompatActivity() {

    private val TAG = "Register"
    private var committee = ""
    private val MY_CAMERA_REQUEST_CODE = 100
    private val PICK_IMAGE_CAMERA = 1
    private val PICK_IMAGE_GALLERY = 2
    private var ImageStore:String = ""


    private lateinit var toolbar : Toolbar
    private lateinit var et_username: EditText
    private lateinit var et_email: EditText
    private lateinit var et_mobile: EditText
    private lateinit var et_quali: EditText
    private lateinit var et_addr: EditText
    private lateinit var et_exp: EditText
    private lateinit var btn_submit: Button
    private lateinit var tv_login : TextView
    private lateinit var imageview:CircularImageView
    private lateinit var et_passwd:EditText
    private lateinit var et_confirm_passwd: EditText
    private lateinit var et_committe:Spinner

    private lateinit var mAPIService : APIService
    private lateinit var selectedImage: Uri 
    var response: String = ""
    var response_path: String = ""
    var UPLOAD_SERVER = "http://www.carryyear.com:3017/mobile/upload_image"

    private lateinit var filename:String
    lateinit var filePath:String
    private val PERMISSION_ALL = 1
    private val PERMISSIONS = arrayOf( android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.INTERNET, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.CAMERA)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        toolbar = findViewById(R.id.toolbar2)
        et_username = findViewById(R.id.et_registername)
        et_email = findViewById(R.id.et_registerEmail)
        et_mobile = findViewById(R.id.et_mobileno)
        et_quali = findViewById(R.id.et_qualification)
        et_addr = findViewById(R.id.et_addr)
        et_exp = findViewById(R.id.et_exp)
        btn_submit = findViewById(R.id.btn_submit)
        tv_login = findViewById(R.id.tv_login)
        et_passwd =findViewById(R.id.et_password)
        et_confirm_passwd = findViewById(R.id.et_cnpassword)
        et_committe = findViewById(R.id.spinner1)
        imageview = findViewById(R.id.profile_image)
        mAPIService = ApiUtils.getAPIService()

        //requesting permission
        if (!hasPermissions(this, PERMISSIONS))
        {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
        }

        imageview.setOnClickListener {
        TakePic()
        }

        mAPIService.RetrieveCommitte().enqueue(object :Callback<Committe_List>{


            override fun onResponse(call: Call<Committe_List>?, response: Response<Committe_List>?) {
                Log.e(TAG,"Response=>" + response!!.body().result)
                var items: ArrayList<String> = ArrayList()
                for (i in 0 until +response.body().data.size){
                    Log.e(TAG,"Committe spinner items $i --->"+response.body().data.get(i).name)
                   items.add(response.body().data.get(i).name)
                }

                //Adapter for spinner
                et_committe.adapter = ArrayAdapter(this@SignupActivity, android.R.layout.simple_spinner_dropdown_item, items)

                et_committe.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        committee = response.body().data.get(position).id
                    }
                }
            }

            override fun onFailure(call: Call<Committe_List>?, t: Throwable?) {
                Log.e(TAG,"Response=>"+"failed")            }
        })


        tv_login.setOnClickListener {
            startActivity(Intent(this@SignupActivity,LoginActivity::class.java))
        }
        //make an arrow to go back
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        //check validation and buttonclick
        btn_submit.setOnClickListener {
            register()
        }
    }

     fun hasPermissions(context:Context, permissions:Array<String>):Boolean {
         if (context != null && permissions != null)
         {
             for (permission in permissions)
             {
                 if (ActivityCompat.checkSelfPermission(context, permission) !== PackageManager.PERMISSION_GRANTED)
                 {
                     return false
                 }
             }
         }
         return true
     }

   @TargetApi(Build.VERSION_CODES.M)
    private fun TakePic() {
        //giving permission dynamically
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA),MY_CAMERA_REQUEST_CODE)
        }
       val options = arrayOf<CharSequence>("Take Photo", "Choose From Gallery", "Cancel")
       val builder = AlertDialog.Builder(this)
       builder.setTitle("Select Item")
       builder.setItems(options) { dialog, item ->
           if (options[item].equals("Take Photo")) {
               dialog.dismiss()
               val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
               startActivityForResult(intent, PICK_IMAGE_CAMERA)
           } else if (options[item].equals("Choose From Gallery")) {
               dialog.dismiss()
               val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
               intent.setType("image/*")
               intent.setAction(Intent.ACTION_GET_CONTENT)
               startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY)
           } else if (options[item].equals("Cancel")) {
               dialog.dismiss()
           }
       }
       builder.show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == PICK_IMAGE_CAMERA)
            {
                if (resultCode != RESULT_OK)
                {
                    return
                }
                val photo:Bitmap = data!!.getExtras().get("data") as Bitmap
                imageview.setImageBitmap(photo)

                val tempUri = getImageUri(getApplicationContext(), photo)
                filePath = getPath(tempUri)
                Log.i(TAG,"Camera photo path-->" + filePath)
                uploadfile()
            }
            else if (resultCode != RESULT_OK)
            {
                return
            }
            else if (requestCode == PICK_IMAGE_GALLERY && resultCode == RESULT_OK)
            {

                selectedImage = data!!.getData()
                filePath = getPath(selectedImage)
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)
                imageview.setImageBitmap(bitmap)
                uploadfile()

            }
    }
     //getting uri of the camera
    fun getImageUri(inContext:Context, inImage:Bitmap):Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Image" + 1, null)
        return Uri.parse(path)
    }

    fun uploadfile(){
        var file:File = File(filePath)
        var fileSizeInBytes :Long = file.length()
        var filesizeinKb:Long = fileSizeInBytes / 1024
        var filesizeinMb:Long = filesizeinKb / 1024
        Log.i(TAG,"Before Compressing:->" + filesizeinKb + "KB\n" + "size->" + filesizeinMb + "MB")
        filename = filePath.substring(filePath.lastIndexOf("/")+1)
        val file_extn: String = filePath.substring(filePath.lastIndexOf(".") + 1)
        try {

            var thread = Thread(object:Runnable {
                public override fun run() {
                    try {

                        response = POST_Data(file)
                        if (response.contains("success"))
                        {
                            Log.e("Image upload-->","Result-->" + response)
                        }
                    }catch (e: IOException){
                        response = "Image was not uploaded!"
                        Log.e(TAG,"Response-->" + response)
                    }
                }


            })
            thread.start()

            Log.e(TAG,"File path--> $filePath\n" + "File name-->" + filename + "\nFile extension-->" + file_extn)
        } catch (e:IOException) {
            Log.i("TAG", "image failed " + e.printStackTrace())
        }

    }

     fun POST_Data(file:File): String {
         filename = filePath.substring(filePath.lastIndexOf("/")+1)
         //compressing the image
         var compressedImageFile = Compressor(this).compressToFile(file)
         //calculating the size of the image
         var fileSizeInBytes :Long = compressedImageFile.length()
         var filesizeinKb:Long = fileSizeInBytes / 1024
         var filesizeinMb:Long = filesizeinKb / 1024
         Log.i(TAG,"After Compressing:->" + filesizeinKb + "KB\n" + "size->" + filesizeinMb + "MB")
         val connection:HttpURLConnection
         val outputStream:DataOutputStream
         val inputStream:InputStream
         val boundary = "*****" + java.lang.Long.toString(System.currentTimeMillis()) + "*****"
         var bytesRead:Int
         var bytesAvailable:Int
         var bufferSize:Int
         val buffer:ByteArray
         val maxBufferSize:Int = 1 * 1024 * 1024
         val fileInputStream = FileInputStream(compressedImageFile)
         val url = URL(UPLOAD_SERVER)
         connection = url.openConnection() as HttpURLConnection
         connection.setDoInput(true)
         connection.setDoOutput(true)
         connection.setUseCaches(false)
         connection.setRequestMethod("POST")
         connection.setRequestProperty("Connection", "Keep-Alive")
         connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0")
         connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary)
         outputStream = DataOutputStream(connection.getOutputStream())
         outputStream.writeBytes("--" + boundary + "\r\n")
         outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "image" + "\"; filename=\""  +filename + "\"" + "\r\n")
         outputStream.writeBytes("Content-Type: image/jpeg" + "\r\n")
         outputStream.writeBytes("Content-Transfer-Encoding: binary" + "\r\n")
         outputStream.writeBytes("\r\n")
         bytesAvailable = fileInputStream.available()
         bufferSize = Math.min(bytesAvailable, maxBufferSize)
         buffer = ByteArray(bufferSize)
         bytesRead = fileInputStream.read(buffer, 0, bufferSize)
         while (bytesRead > 0)
         {
             outputStream.write(buffer, 0, bufferSize)
             bytesAvailable = fileInputStream.available()
             bufferSize = Math.min(bytesAvailable, maxBufferSize)
             bytesRead = fileInputStream.read(buffer, 0, bufferSize)
         }
         outputStream.writeBytes("\r\n")
         outputStream.writeBytes("--" + boundary + "--" + "\r\n")
         inputStream = connection.getInputStream()
         val serverResponseCode = connection.getResponseCode()
         var serverResponseMessage: String = connection.responseMessage
         var result:Scanner = Scanner(connection.inputStream)
         response_path = result.nextLine()
         Log.e(TAG, "uploadFile: response path " +response_path)

         Log.i("uploadFile", "HTTP Response is : "
                 + serverResponseMessage + ": " + serverResponseCode)
         ImageStore = JSONObject(response_path).getString("image")

         fileInputStream.close()
         outputStream.flush()
         outputStream.close()
         return serverResponseCode.toString()

     }

    private fun getPath(selectedImage: Uri): String {
        var projection= arrayOf(MediaStore.MediaColumns.DATA)
        var cursor: Cursor = managedQuery(selectedImage, projection, null, null, null);
        var column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        cursor.moveToFirst();
        var imagePath = cursor.getString(column_index)

        return imagePath
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

    private fun register() {

        if(!validate()){
            onregisterFailed()
            return
        }else{
            onregisterSucess()
        }
    }

    private fun onregisterSucess() {
        val name = et_username.getText().toString().trim()
        val mobile_no = et_mobile.text.toString().trim()
        val email = et_email.text.toString().trim()
        val qualification = et_quali.text.toString().trim()
        val experience = et_exp.text.toString().trim()
        val address = et_addr.text.toString().trim()
        val password = et_passwd.text.toString().trim()
        val real_password = et_confirm_passwd.text.toString().trim()
        val longitude = "10"
        val latitude = "20"
        val image = ImageStore
        Log.i(TAG,"Image stored in " + ImageStore)

        mAPIService.saveRegisterPost(name,committee,qualification,email,image,password,real_password,address,latitude,longitude,experience,mobile_no)
            .enqueue(object: Callback<Register_Post> {

                override fun onResponse(call: Call<Register_Post>?, response: Response<Register_Post>?) {
                    if(response!!.body().result.equals("success")){
                        Toast.makeText(this@SignupActivity,"post successfully submitted to API",Toast.LENGTH_SHORT).show()
                        Log.i(TAG, "post successfully submitted to API." + response!!.body().result)
                        val builder = AlertDialog.Builder(this@SignupActivity)
                        builder.setTitle("Message")
                        builder.setMessage("Signup Success")
                            .setCancelable(false)
                            .setPositiveButton("OK") { dialog, id ->
                                //do things
                                startActivity(Intent(this@SignupActivity,LoginActivity::class.java))
                                finish()
                            }
                        val alert = builder.create()
                        alert.show()
                    }else if(response!!.body().result.equals("already register")){
                        val builder = AlertDialog.Builder(this@SignupActivity)
                        builder.setTitle("Message")
                        builder.setMessage("Email already registered")
                            .setCancelable(false)
                            .setPositiveButton("OK") { dialog, id ->
                                //do things

                            }
                        val alert = builder.create()
                        alert.show()
                    }

                }

                override fun onFailure(call: Call<Register_Post>?, t: Throwable?) {
                    val builder = AlertDialog.Builder(this@SignupActivity)
                    builder.setTitle("try again")
                    builder.setMessage("Something not right")
                        .setCancelable(false)
                        .setPositiveButton("OK") { dialog, id ->
                            //do things

                        }
                    val alert = builder.create()
                    alert.show()
                    Toast.makeText(this@SignupActivity,"Unable to submit to API",Toast.LENGTH_SHORT).show()
                }
            })

    }

    private fun onregisterFailed() {
        btn_submit.setEnabled(true)
        val builder = AlertDialog.Builder(this@SignupActivity)
        builder.setTitle("Message")
        builder.setMessage("Signup failed")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, id ->
                //do things

            }
        val alert = builder.create()
        alert.show()
    }

    private fun validate(): Boolean {
        var valid:Boolean = true
        val name = et_username.text.toString()
        val email_id = et_email.text.toString()
        val qualification = et_quali.text.toString()
        val experience = et_exp.text.toString()
        val mobileno = et_mobile.text.toString()
        val address = et_addr.text.toString()
        val password = et_passwd.text.toString()
        val re_password = et_confirm_passwd.text.toString()


        if(name.isEmpty()){
            et_username.setError("Enter your name")
            valid = false
        }else{
            et_username.setError(null)
        }

        if(qualification.isEmpty()){
            et_quali.setError("Enter your Qualification")
        }else{
            et_quali.setError(null)
        }

        if(experience.isEmpty()){
            et_exp.setError("Enter your Experience")
            valid = false
        }else{
            et_exp.setError(null)
        }

        if(address.isEmpty()){
            et_addr.setError("Enter your Address")
            valid = false
        }else{
            et_addr.setError(null)
        }

        if (email_id.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email_id).matches()) {
            et_email.setError("Enter a valid email address")
            valid = false
        } else {
            et_email.setError(null)
        }


        if(mobileno.isEmpty() || mobileno.length!=10){
            et_mobile.setError("Enter valid phone number")
            valid = false
        }else{
            et_mobile.setError(null)
        }

        if(password.isEmpty() || password.length<4 ||password.length>10){
            et_passwd.setError("Password should be between 4 and 10")
            valid = false
        }else{
            et_passwd.setError(null)
        }

        if(re_password.isEmpty() || !re_password.equals(password)){
            et_confirm_passwd.setError("Password is not matching")
            valid = false
        }else{
            et_confirm_passwd.setError(null)
            valid = true
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



