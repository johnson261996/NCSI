package com.exarcplus.nsci

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import com.exarcplus.nsci.Adapters.EditProfileTabPageChangeAdapter
import com.mikhaellopez.circularimageview.CircularImageView
import com.squareup.picasso.Picasso
import id.zelory.compressor.Compressor
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class ProfileEdit: AppCompatActivity() {

    private var TAG:String = "ProfileEdit"
    private val PICK_IMAGE_GALLERY = 2
    private lateinit var selectedImage: Uri
    var UPLOAD_SERVER = "http://www.carryyear.com:3017/mobile/upload_image"
    lateinit var filePath:String
    var response: String = ""
    var response_path: String = ""
    private lateinit var
            filename:String
    private lateinit var toolbar : Toolbar
    private lateinit var image : CircularImageView
    private lateinit var old_dp : String
    companion object {
        @SuppressLint("StaticFieldLeak")
         lateinit var new_dp:String
    }

    private val PERMISSION_ALL = 1
    private lateinit var session : SessionManagement


    private val PERMISSIONS = arrayOf( android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.INTERNET, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.CAMERA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setstatusbarcolorgradient()
        setContentView(R.layout.activity_profile_edit)
        toolbar = findViewById(R.id.eprofile_toolbar)
        image = findViewById(R.id.updateProfile)
        session = SessionManagement(applicationContext)
        var intent:Intent = getIntent()
        old_dp = intent.getStringExtra("key_image")

        //requesting permission
        if (!hasPermissions(this, PERMISSIONS))
        {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
        }
        if(old_dp.isEmpty()){
            Picasso.with(applicationContext).load(R.mipmap.launcher).placeholder(R.mipmap.launcher).error(R.mipmap.launcher)
                .into(image)
        }else {
            Picasso.with(applicationContext).load(old_dp).placeholder(R.mipmap.launcher).error(R.mipmap.launcher)
                .into(image)
        }
        image.setOnClickListener {
            TakePic()
        }
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        configureTab()
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun TakePic() {

        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        selectedImage = data!!.getData()
        filePath = getPath(selectedImage)
        val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)
        image.setImageBitmap(bitmap)
        uploadfile()
    }

    fun uploadfile(){
        var file: File = File(filePath)
        var fileSizeInBytes :Long = file.length()
        var filesizeinKb:Long = fileSizeInBytes / 1024
        var filesizeinMb:Long = filesizeinKb / 1024
        Log.i(TAG,"Before Compressing:->" + filesizeinKb + "KB\n" + "size->" + filesizeinMb + "MB")
        filename = filePath.substring(filePath.lastIndexOf("/")+1)
        val file_extn: String = filePath.substring(filePath.lastIndexOf(".") + 1)
        try {

            var thread = Thread(object:Runnable {
                 override fun run() {
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
        } catch (e: IOException) {
            Log.i("TAG", "image failed " + e.printStackTrace())
        }

    }
    fun hasPermissions(context: Context, permissions:Array<String>):Boolean {
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

    fun POST_Data(file: File): String {
        filename = filePath.substring(filePath.lastIndexOf("/")+1)
        //compressing the image
        var compressedImageFile = Compressor(this).compressToFile(file)
        //calculating the size of the image
        var fileSizeInBytes :Long = compressedImageFile.length()
        var filesizeinKb:Long = fileSizeInBytes / 1024
        var filesizeinMb:Long = filesizeinKb / 1024
        Log.i(TAG,"After Compressing:->" + filesizeinKb + "KB\n" + "size->" + filesizeinMb + "MB")
        val connection: HttpURLConnection
        val outputStream: DataOutputStream
        val inputStream: InputStream
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
        var result: Scanner = Scanner(connection.inputStream)
        response_path = result.nextLine()
        Log.e(TAG, "uploadFile: response path " +response_path)

        Log.i("uploadFile", "HTTP Response is : "
                + serverResponseMessage + ": " + serverResponseCode)
        new_dp = JSONObject(response_path).getString("image")
        session.createNewImageSession(new_dp)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home)
        // Press Back Icon
        {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configureTab() {
        val tab_layout = findViewById<TabLayout>(R.id.eProfile_tab_layout)
        tab_layout.addTab(tab_layout.newTab().setText("PERSONAL"))
        tab_layout.addTab(tab_layout.newTab().setText("BUSINESS"))
        createAdapter(tab_layout)
    }

    private fun createAdapter(tab_layout: TabLayout) {
        val pager = findViewById<ViewPager>(R.id.eprofile_pager)
        val adapter = EditProfileTabPageChangeAdapter(supportFragmentManager, tab_layout.tabCount)
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