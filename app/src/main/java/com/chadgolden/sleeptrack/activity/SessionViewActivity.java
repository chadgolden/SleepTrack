package com.chadgolden.sleeptrack.activity;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.chadgolden.sleeptrack.R;
import com.chadgolden.sleeptrack.data.SleepSession;
import com.chadgolden.sleeptrack.io.InternalStorageIO;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;

import java.util.ArrayList;
import java.util.Date;

public class SessionViewActivity extends ActionBarActivity implements OnChartValueSelectedListener,
        OnChartGestureListener {

    private LineChart mLineChart;
    private SleepSession sleepSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_session_view);

        mLineChart = (LineChart)findViewById(R.id.chart1);
        setUpChart();
        mLineChart.setOnChartGestureListener(this);
        mLineChart.setOnChartGestureListener(this);

        String fileName = getIntent().getStringExtra("FILE_NAME");
        setTitle(new Date(Long.parseLong(fileName.substring(0, fileName.length() - 4))).toString());

        InternalStorageIO io = null;

        try {
            io = new InternalStorageIO(fileName, openFileInput(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        sleepSession = io.getSleepSession();

        setData();
        animate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_session_view_activitiy, menu);
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
    public void onValueSelected(Entry entry, int i, Highlight highlight) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onChartLongPressed(MotionEvent motionEvent) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent motionEvent) {
        animate();
    }

    @Override
    public void onChartSingleTapped(MotionEvent motionEvent) {

    }

    @Override
    public void onChartFling(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {

    }

    private void setUpChart() {
        mLineChart.setDescription(""); // Blank
        mLineChart.setHighlightEnabled(false);
        mLineChart.setTouchEnabled(true);
        mLineChart.setScaleEnabled(true);
        mLineChart.setDragEnabled(true);
        mLineChart.setPinchZoom(true);
        mLineChart.setBackgroundColor(Color.rgb(0, 0, 16));
        mLineChart.setGridBackgroundColor(Color.rgb(0, 0, 16));
        mLineChart.getAxisRight().setEnabled(false);

        YAxis yAxisLeft = mLineChart.getAxisLeft();
        yAxisLeft.setTextColor(Color.WHITE);
        yAxisLeft.setGridColor(Color.WHITE);

        YAxis yAxisRight = mLineChart.getAxisRight();
        yAxisRight.setGridColor(Color.WHITE);
        yAxisRight.setGridColor(Color.TRANSPARENT);

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setGridColor(Color.WHITE);

    }

    private void setData() {
        ArrayList<String> xValuesTimes = new ArrayList<>(sleepSession.getTimes());
        ArrayList<Integer> yValuesMovements = new ArrayList<>(sleepSession.getNumberOfMovements());
        ArrayList<Entry> entries = new ArrayList<>(xValuesTimes.size());

        for (int i = 0; i < xValuesTimes.size(); i++) {
            float value = (float) yValuesMovements.get(i);
            entries.add(new Entry(value, i));
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "Movements Over Time");
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillAlpha(127);
        lineDataSet.setFillColor(Color.BLUE);
        lineDataSet.setColor(Color.BLACK);
        lineDataSet.setLineWidth(1.0f);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawCubic(true);
        lineDataSet.setDrawValues(false);

        ArrayList<LineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(lineDataSet);

        LineData data = new LineData(xValuesTimes, lineDataSets);

        mLineChart.setData(data);
    }

    private void animate() {
        mLineChart.animateY(1500);
    }
}
