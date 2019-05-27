package com.developer.fabian.feedrss.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

import java.util.ArrayList

abstract class MainListAdapter protected constructor(private val enterList: ArrayList<*>,
                                                     private val idView: Int,
                                                     private val context: Context) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView

        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(idView, null)
        }

        onEnter(enterList[position], view!!)

        return view
    }

    override fun getCount(): Int {
        return enterList.size
    }

    override fun getItem(position: Int): Any {
        return enterList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    abstract fun onEnter(enter: Any, view: View)
}
