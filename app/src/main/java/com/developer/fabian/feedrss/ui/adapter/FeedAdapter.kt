package com.developer.fabian.feedrss.ui.adapter

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.ImageView
import android.widget.TextView

import com.android.volley.toolbox.NetworkImageView
import com.developer.fabian.feedrss.R
import com.developer.fabian.feedrss.database.ScriptDatabase
import com.developer.fabian.feedrss.singleton.VolleySingleton

class FeedAdapter(context: Context, c: Cursor, flags: Int,
                  private val resource: Int,
                  private val name: String) : CursorAdapter(context, c, flags) {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_layout, null, false)
        val viewHolder = ViewHolder()

        val icon = view.findViewById<ImageView>(R.id.icon)
        val publisher = view.findViewById<TextView>(R.id.publisher)

        viewHolder.title = view.findViewById(R.id.title)
        viewHolder.description = view.findViewById(R.id.description)
        viewHolder.image = view.findViewById(R.id.image)

        icon.setImageResource(resource)
        publisher.text = name

        viewHolder.titleI = cursor.getColumnIndex(ScriptDatabase.EnterColumns.TITLE)
        viewHolder.descriptionI = cursor.getColumnIndex(ScriptDatabase.EnterColumns.DESCRIPTION)
        viewHolder.imageI = cursor.getColumnIndex(ScriptDatabase.EnterColumns.THUMB_URL)

        view.tag = viewHolder

        return view
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val viewHolder = view.tag as ViewHolder

        viewHolder.title!!.text = cursor.getString(viewHolder.titleI)

        val length = cursor.getString(viewHolder.descriptionI).length
        val desc = cursor.getString(viewHolder.descriptionI)

        if (length >= 150)
            viewHolder.description!!.text = context.getString(R.string.description_text, desc.substring(0, 150))
        else
            viewHolder.description!!.text = desc

        val thumbnailUrl = cursor.getString(viewHolder.imageI)
        val imageLoader = VolleySingleton.getInstance(context).imageLoader
        viewHolder.image!!.setImageUrl(thumbnailUrl, imageLoader)
    }

    internal class ViewHolder {
        var title: TextView? = null
        var description: TextView? = null
        var image: NetworkImageView? = null

        var titleI: Int = 0
        var descriptionI: Int = 0
        var imageI: Int = 0
    }
}
