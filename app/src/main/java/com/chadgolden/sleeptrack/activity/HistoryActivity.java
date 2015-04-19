package com.chadgolden.sleeptrack.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chadgolden.sleeptrack.R;
import com.chadgolden.sleeptrack.ui.ContentItem;
import com.chadgolden.sleeptrack.ui.MyAdapter;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.Date;

public class HistoryActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Utils.init(getResources());

        ArrayList<ContentItem> contentItems = initContentItems();
        MyAdapter myAdapter = new MyAdapter(this, contentItems);

        ListView listView = (ListView)findViewById(R.id.listViewRecordFiles);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private ArrayList<ContentItem> initContentItems() {
        ArrayList<ContentItem> contentItems = new ArrayList<>();
        ArrayList<String> files = new ArrayList<>();

        for (String file : fileList()) {
            if (file.contains(".stf")) {
                String fileWithoutFormat = file.substring(0, file.length() - 4);
                long fileTimeInMillis = Long.parseLong(fileWithoutFormat);
                contentItems.add(new ContentItem(new Date(fileTimeInMillis).toString(), "<empty>"));
            }
        }
        return contentItems;
    }
}
