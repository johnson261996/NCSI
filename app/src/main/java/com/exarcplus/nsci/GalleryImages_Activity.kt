package com.exarcplus.nsci

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import com.exarcplus.nsci.Adapters.GalleryImageAdapter
import com.exarcplus.nsci.Model.Gallery_Data
import com.exarcplus.nsci.Model.Image

class GalleryImages_Activity:AppCompatActivity() {

    private lateinit var list_view:ListView
    private lateinit var adapter: GalleryImageAdapter
    private lateinit var image_list : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        list_view = findViewById(R.id.activity_Gallery_listview)
        var intent :Intent = getIntent()
        image_list = intent.getStringArrayListExtra("key_images")
        adapter = GalleryImageAdapter(this@GalleryImages_Activity,image_list)
        list_view.setAdapter(adapter)
        adapter.notifyDataSetChanged()




    }
}