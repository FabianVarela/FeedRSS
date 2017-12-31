package com.developer.fabian.feedrss.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.developer.fabian.feedrss.R;
import com.developer.fabian.feedrss.database.FeedDatabase;
import com.developer.fabian.feedrss.database.ScriptDatabase;
import com.developer.fabian.feedrss.entities.Rss;
import com.developer.fabian.feedrss.generics.XmlRequest;
import com.developer.fabian.feedrss.singleton.VolleySingleton;
import com.developer.fabian.feedrss.ui.adapter.FeedAdapter;

public class ListDetailActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String URL_SELECTED = "UrlSelected";
    public static final String FEED_SELECTED = "FeedSelected";
    public static final String IMAGE_SELECTED = "ImageSelected";

    private ListView listView;
    private FeedAdapter adapter;

    private int resource;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail);

        this.listView = findViewById(R.id.listaDetalle);

        String URL_FEED = getIntent().getStringExtra(URL_SELECTED);
        resource = getIntent().getIntExtra(IMAGE_SELECTED, 0);
        name = getIntent().getStringExtra(FEED_SELECTED);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            VolleySingleton.getInstance(this).addToRequestQueue(
                    new XmlRequest<>(URL_FEED, Rss.class, null,
                            new Response.Listener<Rss>() {
                                @Override
                                public void onResponse(Rss response) {
                                    FeedDatabase.getInstance(ListDetailActivity.this).syncEntries(response.getChannel().getItems());
                                    new LoadData().execute();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    String errorMessage = String.format(getString(R.string.errorVolley), error.getMessage());

                                    Log.d(TAG, errorMessage);
                                    Toast.makeText(ListDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            }
                    )
            );
        } else {
            Log.i(TAG, getString(R.string.messageConnection));
            adapter = new FeedAdapter(this,
                    FeedDatabase.getInstance(this).getEntries(),
                    SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                    resource,
                    name);

            listView.setAdapter(adapter);

            Toast.makeText(this, getString(R.string.messageConnection), Toast.LENGTH_SHORT).show();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listener, View view, int position, long id) {
                Cursor cursor = (Cursor) adapter.getItem(position);
                String url = cursor.getString(cursor.getColumnIndex(ScriptDatabase.EnterColumns.URL));

                Intent intent = new Intent(ListDetailActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.URL_EXTRA, url);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public class LoadData extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected Cursor doInBackground(Void... params) {
            return FeedDatabase.getInstance(ListDetailActivity.this).getEntries();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);

            adapter = new FeedAdapter(ListDetailActivity.this,
                    cursor,
                    SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                    resource,
                    name);

            listView.setAdapter(adapter);
        }
    }
}
