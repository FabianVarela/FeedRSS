package com.developer.fabian.feedrss.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.developer.fabian.feedrss.R;
import com.developer.fabian.feedrss.database.ScriptDatabase;
import com.developer.fabian.feedrss.singleton.VolleySingleton;

public class FeedAdapter extends CursorAdapter {

    static class ViewHolder {
        TextView title;
        TextView description;
        NetworkImageView image;

        int titleI;
        int descriptionI;
        int imageI;
    }

    private int resource;
    private String name;

    public FeedAdapter(Context context, Cursor c, int flags, int resource, String name) {
        super(context, c, flags);

        this.resource = resource;
        this.name = name;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_layout, null, false);
        ViewHolder viewHolder = new ViewHolder();

        ImageView icon = view.findViewById(R.id.icon);
        TextView publisher = view.findViewById(R.id.publisher);

        viewHolder.title = view.findViewById(R.id.title);
        viewHolder.description = view.findViewById(R.id.description);
        viewHolder.image = view.findViewById(R.id.image);

        icon.setImageResource(resource);
        publisher.setText(name);

        viewHolder.titleI = cursor.getColumnIndex(ScriptDatabase.EnterColumns.TITLE);
        viewHolder.descriptionI = cursor.getColumnIndex(ScriptDatabase.EnterColumns.DESCRIPTION);
        viewHolder.imageI = cursor.getColumnIndex(ScriptDatabase.EnterColumns.THUMB_URL);

        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.title.setText(cursor.getString(viewHolder.titleI));

        int length = cursor.getString(viewHolder.descriptionI).length();
        String desc = cursor.getString(viewHolder.descriptionI);

        if (length >= 150)
            viewHolder.description.setText(context.getString(R.string.description_text, desc.substring(0, 150)));
        else
            viewHolder.description.setText(desc);

        String thumbnailUrl = cursor.getString(viewHolder.imageI);
        ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
        viewHolder.image.setImageUrl(thumbnailUrl, imageLoader);
    }
}
