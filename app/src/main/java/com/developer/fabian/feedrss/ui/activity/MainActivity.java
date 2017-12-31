package com.developer.fabian.feedrss.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.fabian.feedrss.R;
import com.developer.fabian.feedrss.entities.Enter;
import com.developer.fabian.feedrss.ui.adapter.MainListAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(MainActivity.this, ListDetailActivity.class);
        ArrayList<Enter> enters = new ArrayList<>();

        enters.add(new Enter(R.drawable.forbes,
                getString(R.string.forbes),
                getString(R.string.descForbes),
                getString(R.string.urlForbes)));

        ListView listView = findViewById(R.id.lista);
        listView.setAdapter(new MainListAdapter(enters, R.layout.list_enter_main, this) {
            @Override
            public void onEnter(Object enter, View view) {
                if (enter != null) {
                    TextView textSuperior = view.findViewById(R.id.textView_superior);
                    if (textSuperior != null)
                        textSuperior.setText(((Enter) enter).getTextUp());

                    TextView textInferior = view.findViewById(R.id.textView_inferior);
                    if (textInferior != null)
                        textInferior.setText(((Enter) enter).getTextDown());

                    ImageView image = view.findViewById(R.id.imageView_image);
                    if (image != null)
                        image.setImageResource(((Enter) enter).getIdImage());
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Enter choose = (Enter) parent.getItemAtPosition(position);

                CharSequence text = String.format(getString(R.string.toastSelect), choose.getTextUp());
                Toast toast = Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG);
                toast.show();

                intent.putExtra(ListDetailActivity.FEED_SELECTED, choose.getTextUp());
                intent.putExtra(ListDetailActivity.IMAGE_SELECTED, choose.getIdImage());
                intent.putExtra(ListDetailActivity.URL_SELECTED, choose.getUrlRSS());

                startActivity(intent);
            }
        });
    }
}
