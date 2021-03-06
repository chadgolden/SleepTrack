package com.chadgolden.sleeptrack.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.chadgolden.sleeptrack.R;
import com.chadgolden.sleeptrack.data.DataProcessor;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ChartActivity extends ActionBarActivity {

    private DataProcessor dataProcessor;

    private LineChart chart;
    private LineData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        dataProcessor = DataProcessor.getInstance();
        chart = new LineChart(this.getApplicationContext());
        //data = dataProcessor.getLineData();
        data = new LineData(dataProcessor.getLabelsAsArrayList(), dataProcessor.getDataSet());
        chart.setData(data);
        chart.invalidate();

        setContentView(chart);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chart, menu);
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

    public void updateChart()
    {
        DataProcessor dataProcessor = DataProcessor.getInstance();
        data = new LineData(dataProcessor.getLabelsAsArrayList(),dataProcessor.getDataSet());
        data.setDrawValues(false);
        System.err.println(data.getDataSetByIndex(0).isDrawFilledEnabled());
        // Must be done on UI Thread.
        runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                chart.invalidate();
            }
        });
    }
}
