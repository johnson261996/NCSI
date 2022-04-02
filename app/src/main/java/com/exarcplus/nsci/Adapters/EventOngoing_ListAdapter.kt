package com.exarcplus.nsci.Adapters

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.exarcplus.nsci.Model.OngoingEvent_Data
import com.exarcplus.nsci.R
import com.squareup.picasso.Picasso
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat

class EventOngoing_ListAdapter(c:Context,rowitems: ArrayList<OngoingEvent_Data>) : BaseAdapter(){

    var context: Context = c
    var rowItems : ArrayList<OngoingEvent_Data> = rowitems
    private val arraylist: ArrayList<OngoingEvent_Data> = ArrayList<OngoingEvent_Data>()

    private val mInflator: LayoutInflater

    init {
        this.mInflator = LayoutInflater.from(context)
        arraylist.addAll(rowItems)
    }

    private class ViewHolder {

        internal lateinit var imageView: ImageView

        internal lateinit var txteventname: TextView
        internal lateinit var txtschedulefrom: TextView
        internal lateinit var txtscheduleto: TextView
        internal lateinit var txtlocation: TextView
        internal lateinit var txtday: TextView
        internal lateinit var txtinterested: TextView
    }



    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder:ViewHolder
        val convertView: View?
        convertView = mInflator.inflate(R.layout.eventongoing_listitems, null)
        holder = ViewHolder()
        holder.txteventname = convertView.findViewById(R.id.name_event)
        holder.txtschedulefrom = convertView.findViewById(R.id.event_schedulefrom)
        holder.txtscheduleto = convertView.findViewById(R.id.event_scheduleto)
        holder.txtlocation = convertView.findViewById(R.id.event_location)
        holder.txtday = convertView.findViewById(R.id.day_event)
        holder.txtinterested = convertView.findViewById(R.id.event_interested)
        holder.imageView = convertView.findViewById(R.id.event_image)

        var str = rowItems.get(position).date.split("-")
        var day:Int =  Integer.parseInt(str[2])
        var month:Int = Integer.parseInt(str[1])
        var monthname = DateFormatSymbols().months[month-1]
        var dateformat = SimpleDateFormat("MMM")
        var mon = dateformat.format(dateformat.parse(monthname))

        holder.txteventname.setText(rowItems.get(position).title)
        holder.txtschedulefrom.setText(rowItems.get(position).fromTime)
        holder.txtscheduleto.setText(rowItems.get(position).toTime)
        holder.txtday.setText("" + day + "\n" + mon)
        holder.txtlocation.setText(Html.fromHtml(rowItems.get(position).venue))
        holder.txtinterested.setText((rowItems.get(position).interestedUsers.size.toString()))

        if(!rowItems.get(position).image.equals("")) {
            Picasso.with(context).load(rowItems.get(position).image).placeholder(R.mipmap.launcher).error(R.mipmap.launcher).into(holder.imageView)
        }else {
            holder.imageView.setImageResource(R.mipmap.launcher)
        }

        return convertView;
    }

    override fun getItem(position: Int): OngoingEvent_Data {
        return rowItems.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return rowItems.size
    }
}