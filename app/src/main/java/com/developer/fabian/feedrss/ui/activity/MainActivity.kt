package com.developer.fabian.feedrss.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast

import com.developer.fabian.feedrss.R
import com.developer.fabian.feedrss.entities.Enter
import com.developer.fabian.feedrss.ui.adapter.MainListAdapter

import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this@MainActivity, ListDetailActivity::class.java)
        val enters = ArrayList<Enter>()

        enters.add(Enter(R.drawable.forbes,
                getString(R.string.forbes),
                getString(R.string.descForbes),
                getString(R.string.urlForbes)))

        val listView = findViewById<ListView>(R.id.lista)
        listView.adapter = object : MainListAdapter(enters, R.layout.list_enter_main, this) {
            override fun onEnter(enter: Any, view: View) {
                if (enter != null) {
                    val textSuperior = view.findViewById<TextView>(R.id.textView_superior)
                    if (textSuperior != null)
                        textSuperior.text = (enter as Enter).textUp

                    val textInferior = view.findViewById<TextView>(R.id.textView_inferior)
                    if (textInferior != null)
                        textInferior.text = (enter as Enter).textDown

                    val image = view.findViewById<ImageView>(R.id.imageView_image)
                    image?.setImageResource((enter as Enter).idImage)
                }
            }
        }

        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val choose = parent.getItemAtPosition(position) as Enter

            val text = String.format(getString(R.string.toastSelect), choose.textUp)
            val toast = Toast.makeText(this@MainActivity, text, Toast.LENGTH_LONG)
            toast.show()

            intent.putExtra(ListDetailActivity.FEED_SELECTED, choose.textUp)
            intent.putExtra(ListDetailActivity.IMAGE_SELECTED, choose.idImage)
            intent.putExtra(ListDetailActivity.URL_SELECTED, choose.urlRSS)

            startActivity(intent)
        }
    }
}
