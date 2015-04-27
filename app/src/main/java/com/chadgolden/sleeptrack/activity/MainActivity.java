package com.chadgolden.sleeptrack.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chadgolden.sleeptrack.R;
import com.chadgolden.sleeptrack.global.GlobalState;
import com.chadgolden.sleeptrack.global.GlobalValues;
import com.github.mikephil.charting.utils.Utils;
import com.chadgolden.sleeptrack.ui.MyAdapter;
import com.chadgolden.sleeptrack.ui.ContentItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private Button button;
    private Button webButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        // Get user application preferences so that we can easily make them accessible by
        // other application modules.
        SharedPreferences prefDeviceSensitivity = PreferenceManager.getDefaultSharedPreferences(this);

        // Set some settings variables for other applications to access.
        GlobalValues values = GlobalValues.getInstance();
        values.addKeyValuePair("prefDeviceSensitivity",
                prefDeviceSensitivity.getString("prefDeviceSensitivity", "NULL"));
        values.addKeyValuePair("prefDeviceSensitivity1",
                prefDeviceSensitivity.getString("prefDeviceSensitivity1", "NULL"));

        Utils.init(getResources());

        ArrayList<ContentItem> contentItems = new ArrayList<>();
        contentItems.add(new ContentItem("Record Your Sleep", "Click here and begin sleeping!"));
        contentItems.add(new ContentItem("View Your Sleep Patterns",
                "Click here to view your past nights."));
        contentItems.add(new ContentItem("Options",
                "Change settings or calibrate your movement sensor."));

        MyAdapter adapter = new MyAdapter(this, contentItems);

        ListView listView = (ListView)findViewById(R.id.listViewOptions);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);

        button = (Button) findViewById(R.id.buttonBegin);
        webButton = (Button) findViewById(R.id.buttonWeb);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(
                        MainActivity.this, GlobalState.getInstance().getSensorActivity().getClass()
                );
                startActivity(intent);
            }
        });

        webButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Web Button Pressed");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(this, SensorActivity.class);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
    }

}
