package com.exarcplus.nsci.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.exarcplus.nsci.Model.Member_Data
import com.exarcplus.nsci.R
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class CommitteAdapter(context: Context, comItems: ArrayList<Member_Data>):BaseAdapter() {

    private var context: Context = context
    private var comItems : ArrayList<Member_Data> = comItems
    private var arraylist:ArrayList<Member_Data> = ArrayList()

    private val mInflator: LayoutInflater

    init {
        this.mInflator = LayoutInflater.from(context)
        arraylist.addAll(comItems)
    }

    private class ViewHolder {

        internal lateinit var imageView: ImageView
        internal lateinit var txtTitle: TextView
        internal lateinit var txtemail: TextView
        internal lateinit var txtqualification : TextView
        internal lateinit var txtexperience : TextView
    }

    override fun getView(position: Int, convertView: View?, p2: ViewGroup?): View {
        val holder: ViewHolder
        var cv = convertView
        cv = mInflator.inflate(R.layout.committe_list_details, null)
        holder = ViewHolder()
        holder.imageView = cv!!.findViewById(R.id.imageView_com)
        holder.txtTitle = cv!!.findViewById(R.id.name_comm)
        holder.txtemail = cv!!.findViewById(R.id.email_comm)
        holder.txtqualification = cv!!.findViewById(R.id.prof_comm)
        holder.txtexperience = cv!!.findViewById(R.id.exp_comm)
        //putting text in the components
        holder.txtTitle.setText(comItems.get(position).name)
        holder.txtemail.setText(comItems.get(position).email)
        Log.e("CommitteAdapter","rowItems : $position --> "+comItems.get(position).image )
        holder.txtexperience.setText((comItems.get(position).experience) + "years")
        holder.txtqualification.setText((comItems.get(position).qualification))
        if(!comItems.get(position).image.equals("")) {
            Picasso.with(context).load(comItems.get(position).image).placeholder(R.mipmap.launcher).error(R.mipmap.launcher).into(holder.imageView)
        }else {
            holder.imageView.setImageResource(R.mipmap.launcher)
        }
        return cv
    }

    override fun getItem(p0: Int): Member_Data {
        return comItems.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return comItems.size
    }

    fun getFilter(chartext: String) {
        Log.e("Filetr-->","Started-->$context")
        var text = chartext
        text = chartext.toLowerCase(Locale.getDefault())
        comItems.clear()
        if(text.length == 0){
            comItems.addAll(arraylist)
        }
        else{
            for (a in arraylist){
                if(a.name.toLowerCase(Locale.getDefault()).contains(text) ||
                        a.qualification.toLowerCase(Locale.getDefault()).contains(text))
                {
                    comItems.add(a)
                }
            }
        }
        notifyDataSetChanged()
    }


}
