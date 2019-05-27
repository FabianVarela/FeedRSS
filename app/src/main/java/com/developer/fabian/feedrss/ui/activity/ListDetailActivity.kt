package com.developer.fabian.feedrss.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.Toast

import com.android.volley.Response
import com.developer.fabian.feedrss.R
import com.developer.fabian.feedrss.database.FeedDatabase
import com.developer.fabian.feedrss.database.ScriptDatabase
import com.developer.fabian.feedrss.entities.Rss
import com.developer.fabian.feedrss.generics.XmlRequest
import com.developer.fabian.feedrss.singleton.VolleySingleton
import com.developer.fabian.feedrss.ui.adapter.FeedAdapter

class ListDetailActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName

        const val URL_SELECTED = "UrlSelected"
        const val FEED_SELECTED = "FeedSelected"
        const val IMAGE_SELECTED = "ImageSelected"
    }

    private lateinit var listView: ListView
    private lateinit var adapter: FeedAdapter

    private var resource: Int = 0
    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_detail)

        this.listView = findViewById(R.id.listaDetalle)

        val urlFeed = intent.getStringExtra(URL_SELECTED)
        resource = intent.getIntExtra(IMAGE_SELECTED, 0)
        name = intent.getStringExtra(FEED_SELECTED)

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            VolleySingleton.getInstance(this).addToRequestQueue(
                    XmlRequest(urlFeed, Rss::class.java, null,
                            Response.Listener { response ->
                                FeedDatabase.getInstance(this@ListDetailActivity).syncEntries(response.channel!!.items!!)
                                LoadData().execute()
                            },
                            Response.ErrorListener { error ->
                                val errorMessage = String.format(getString(R.string.errorVolley), error.message)

                                Log.d(TAG, errorMessage)
                                Toast.makeText(this@ListDetailActivity, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                    )
            )
        } else {
            Log.i(TAG, getString(R.string.messageConnection))
            adapter = FeedAdapter(this,
                    FeedDatabase.getInstance(this).getEntries(),
                    SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                    resource,
                    name!!)

            listView.adapter = adapter

            Toast.makeText(this, getString(R.string.messageConnection), Toast.LENGTH_SHORT).show()
        }

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val cursor = adapter.getItem(position) as Cursor
            val url = cursor.getString(cursor.getColumnIndex(ScriptDatabase.EnterColumns.URL))

            val intent = Intent(this@ListDetailActivity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.URL_EXTRA, url)
            startActivity(intent)
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class LoadData : AsyncTask<Void, Void, Cursor>() {
        override fun doInBackground(vararg params: Void): Cursor {
            return FeedDatabase.getInstance(this@ListDetailActivity).getEntries()
        }

        override fun onPostExecute(cursor: Cursor) {
            super.onPostExecute(cursor)

            adapter = FeedAdapter(this@ListDetailActivity,
                    cursor,
                    SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
                    resource,
                    name!!)

            listView.adapter = adapter
        }
    }
}
