package com.exarcplus.nsci.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.exarcplus.nsci.R
import com.exarcplus.nsci.Model.Member_Data
import com.mikhaellopez.circularimageview.CircularImageView
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList


class ListBaseAdapter(context: Context, rowItem: ArrayList<Member_Data>) : BaseAdapter() {


    var context: Context = context
    var rowItems : ArrayList<Member_Data> = rowItem
    private val arraylist: ArrayList<Member_Data> = ArrayList<Member_Data>()

    private val mInflator: LayoutInflater

    init {
        this.mInflator = LayoutInflater.from(context)
        arraylist.addAll(rowItems)
    }

    private class ViewHolder {

        internal lateinit var imageView: CircularImageView

        internal lateinit var txtTitle: TextView
        internal lateinit var txtDesc: TextView
    }



    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView1: View?, p2: ViewGroup?): View {
        val holder: ViewHolder
        val convertView: View?
        convertView = mInflator.inflate(R.layout.member_list_items, null)
        holder = ViewHolder()
        val member: Member_Data = getItem(position)

        holder.txtDesc = convertView.findViewById(R.id.textView2)
        holder.txtTitle = convertView.findViewById(R.id.textView1)
        holder.imageView = convertView.findViewById(R.id.imageView1)

        holder.txtDesc.setText(member.name)
        holder.txtTitle.setText(member.email)

        Log.e("ListBaseAdapter","rowItems : $position --> "+rowItems.get(position).image )

        if(!rowItems.get(position).image.equals("")) {
            Picasso.with(context).load(member.image).placeholder(R.mipmap.launcher).error(R.mipmap.launcher).into(holder.imageView)
        }else {
            holder.imageView.setImageResource(R.mipmap.launcher)
        }
        return convertView

    }

    override fun getCount(): Int {
           return rowItems.size    }

    override fun getItem(position: Int): Member_Data {
        return rowItems.get(position)
    }
    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

     fun getFilter(chartext: String) {
        Log.e("Filetr-->","Started-->$context")
        var text = chartext
         text = chartext.toLowerCase(Locale.getDefault())
         rowItems.clear()
         if(text.length == 0){
             rowItems.addAll(arraylist)
         }
         else{
             for (a in arraylist){
                 if(a.name.toLowerCase(Locale.getDefault()).contains(text))
                 {
                    rowItems.add(a)
                 }
             }
         }
         notifyDataSetChanged()
    }

}


