package com.exarcplus.nsci.Adapters

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.exarcplus.nsci.GalleryImages_Activity
import com.exarcplus.nsci.Model.Gallery_Data
import android.view.LayoutInflater
import android.widget.ImageView
import com.exarcplus.nsci.Model.Image
import com.exarcplus.nsci.R
import com.squareup.picasso.Picasso


class GalleryImageAdapter(activity: GalleryImages_Activity, rowitems: ArrayList<String>):BaseAdapter() {

    private var TAG = "GalleryImageAdapter"
    private var context:Context = activity
    private var galleryitems:ArrayList<String> = rowitems
    private val mInflator: LayoutInflater

    init {
        this.mInflator = LayoutInflater.from(context)
    }

    private class ViewHolder {
        internal lateinit var imageView1: ImageView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val holder: ViewHolder
        val convertView: View?
        convertView = mInflator.inflate(R.layout.activity_gallery_items, null)
        holder = ViewHolder()
        holder.imageView1 = convertView!!.findViewById(R.id.gallery_images)
        Log.i(TAG,"Gallery position:$position-->" + galleryitems[position])
        Picasso.with(context).load(galleryitems[position]).placeholder(R.mipmap.launcher).error(R.mipmap.launcher).into(holder.imageView1)
        return convertView
    }

    override fun getItem(position: Int): String {
        return galleryitems[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return galleryitems.size
    }
}
