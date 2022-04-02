package com.exarcplus.nsci.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.exarcplus.nsci.Model.Gallery_Data
import com.exarcplus.nsci.Model.Image
import com.exarcplus.nsci.R
import com.squareup.picasso.Picasso

class GalleryBaseAdapter(context: Context, rowItems: ArrayList<Gallery_Data>):BaseAdapter() {

    private var TAG = "GalleryBaseAdapter"
    var context: Context = context
    var rowItems : ArrayList<Gallery_Data> = rowItems
    private val arraylist: ArrayList<Gallery_Data> = ArrayList()
    private val mInflator: LayoutInflater

    init {
        this.mInflator = LayoutInflater.from(context)
        arraylist.addAll(rowItems)
    }

    private class ViewHolder {
        internal lateinit var imageView1: ImageView
        internal lateinit var totalcount : TextView
        internal lateinit var txttitle : TextView
        internal lateinit var txtlike: TextView
        internal lateinit var txtcomment: TextView
        internal lateinit var txtshare : TextView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: ViewHolder
        val convertView: View?
        convertView = mInflator.inflate(R.layout.gallery_items, null)
        holder = ViewHolder()
        holder.txtlike = convertView.findViewById(R.id.favourite)
        holder.txtcomment = convertView.findViewById(R.id.comment)
        holder.txtshare = convertView.findViewById(R.id.forward)
        holder.totalcount = convertView.findViewById(R.id.count_images)
        holder.imageView1 = convertView.findViewById(R.id.gallery_image)
        holder.txttitle = convertView.findViewById(R.id.txt_title)

        //Log.i(TAG,"image items--> $position --> "+rowItems.get(position).images.get(0).image)
        var images:ArrayList<Image>
        Picasso.with(context).load(rowItems.get(position).images[0].image).placeholder(R.mipmap.launcher)
            .error(R.mipmap.launcher).into(holder.imageView1)
        holder.txttitle.setText(rowItems.get(position).title)
        holder.totalcount.setText(rowItems.get(position).images.size.toString())
        for (i in 0 until 1){
            Log.i(TAG,"Gallery 1-->$i\t" + rowItems.get(1).images.get(i).image)
        }
        /*   try {
        holder.txtlike.setText(rowItems.get(position).)
        holder.txtcomment.setText(rowItems.get(position).comment)
        holder.txtshare.setText(rowItems.get(position).share)
       Log.i(TAG,"Array title:" + rowItems.get(position).title)
       Log.i(TAG,"Array images "+ "position ->" + rowItems.get(position).images.get(position) + "\nid->"+  rowItems.get(position).images.get(position).imageId)
       var itemposition = getItem(position)
       Log.i(TAG, "image position" + itemposition + "\n" + "Total count" + count)
       holder.txttitle.setText(rowItems.get(position).title)
       holder.totalcount.setText(rowItems.get(position).images.size.toString())
       Picasso.with(context).load(rowItems[position].images[position].image).placeholder(R.mipmap.launcher)
           .error(R.mipmap.launcher).into(holder.imageView1)
   }catch (e:IndexOutOfBoundsException){
       Log.i(TAG,"Array index" + e.printStackTrace() +"\t" + rowItems.get(position).images.size)
   }*/
        return convertView
    }

    override fun getItem(position: Int): Gallery_Data {
        return rowItems[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return rowItems.size
    }
}
