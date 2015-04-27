package com.chadgolden.sleeptrack.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.chadgolden.sleeptrack.R;
import com.chadgolden.sleeptrack.data.DataProcessor;
import com.chadgolden.sleeptrack.global.GlobalState;
import com.chadgolden.sleeptrack.global.GlobalValues;
import com.chadgolden.sleeptrack.ui.MyAdapter;
import com.chadgolden.sleeptrack.util.MySensorEventListener;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class SettingsActivity extends PreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener, MySensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private LineChart mLineChart;

    private float previousX;
    private float previousY;
    private float previousZ;
    private int segmentMovementCount;
    private long lastUpdateTimeMillis;

    private Timer timer;
    private TimerTask timerTask;

    public static int SETTINGS_INTERVAL = 500;

    public float ALLOWED_MOVEMENT = 0.4f;
//            PreferenceManager.getDefaultSharedPreferences(getBaseContext())
//                    .getFloat("prefDeviceSensitivity", 0.5f);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        addPreferencesFromResource(R.xml.preferences);
        lastUpdateTimeMillis = System.currentTimeMillis();

        setTheme(R.style.LightText);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(
                this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL
        );

        timerTask = new ChartTimer();
        timer = GlobalState.getInstance().newTimer();
        timer.schedule(timerTask, 0, SETTINGS_INTERVAL);

        mLineChart = (LineChart) findViewById(R.id.lineChartSettings);

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        GlobalValues values = GlobalValues.getInstance();
        switch (key) {
            case "prefDeviceSensitivity":
                values.addKeyValuePair(
                        "prefDeviceSensitivity",
                        sharedPreferences.getString("prefDeviceSensitivity", "NULL")
                );
                break;
            case "prefDeviceSensitivity1":
                values.addKeyValuePair(
                        "prefDeviceSensitivity1",
                        sharedPreferences.getString("prefDeviceSensitivity1", "NULL")
                );
                break;
        }

    }

    @Override
    public boolean sensorExceedsAllowableMovement(float[] sensorValues) {
        final String prefString = GlobalValues.getInstance().getValue("prefDeviceSensitivity");
        final float ALLOWED_MOVEMENT = Float.parseFloat(prefString);
        if (Math.abs(sensorValues[0] - previousX) > ALLOWED_MOVEMENT ||
                Math.abs(sensorValues[1] - previousY) > ALLOWED_MOVEMENT ||
                Math.abs(sensorValues[2] - previousZ) > ALLOWED_MOVEMENT) {
            return true;
        }
        return false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        long currentTimeInMillis = System.currentTimeMillis();

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            final String prefTimingString =
                    GlobalValues.getInstance().getValue("prefDeviceSensitivity1");
            final float TIMING_DELAY = Float.parseFloat(prefTimingString);
            if ((currentTimeInMillis - lastUpdateTimeMillis) >= TIMING_DELAY) {
                lastUpdateTimeMillis = currentTimeInMillis;
                if (sensorExceedsAllowableMovement(event.values)) {
                    segmentMovementCount++;
                }
            }
        }
        previousX = event.values[0];
        previousY = event.values[1];
        previousZ = event.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    class ChartTimer extends TimerTask {

        @Override
        public void run() {

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            final String time = sdf.format(calendar.getTime());
            //System.out.println("Timer executed at: " + time);
//            runOnUiThread(
//                    () -> {
//                        DataProcessor.getInstance().addEntry(time, segmentMovementCount);
//                        segmentMovementCount = 0;
//            });
            DataProcessor.getInstance().addEntry(time, segmentMovementCount);
            System.out.println("Entry added: " + time + ", " + segmentMovementCount);
            mLineChart.setData(
                    new LineData(
                            DataProcessor.getInstance().getLabelsAsArrayList(),
                            DataProcessor.getInstance().getDataSet()
                    )
            );
            System.err.println(DataProcessor.getInstance().getDataSet().isDrawFilledEnabled());
            mLineChart.getLineData().getDataSetByIndex(0).setDrawFilled(true);
            mLineChart.getLineData().getDataSetByIndex(0).setFillAlpha(127);
            mLineChart.getLineData().getDataSetByIndex(0).setFillColor(Color.BLUE);
            mLineChart.getAxisLeft().setAxisMaxValue(SETTINGS_INTERVAL/50);
            mLineChart.getAxisLeft().setAxisMinValue(0.0f);
            mLineChart.getAxisRight().setAxisMaxValue(SETTINGS_INTERVAL/50);
            mLineChart.getAxisRight().setAxisMinValue(0.0f);
            mLineChart.getAxisLeft().setDrawGridLines(false);
            mLineChart.getAxisRight().setDrawGridLines(false);
            mLineChart.getXAxis().setDrawGridLines(false);
            mLineChart.getLineData().getDataSetByIndex(0).setDrawCircles(false);
            mLineChart.getLineData().setDrawValues(false);
            setUpChart();
            segmentMovementCount = 0;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLineChart.invalidate();
                }
            });
        }
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
        yAxisLeft.setDrawGridLines(true);
        yAxisLeft.setTextColor(Color.WHITE);
        yAxisLeft.setGridColor(Color.WHITE);

        YAxis yAxisRight = mLineChart.getAxisRight();
        yAxisRight.setDrawGridLines(true);
        yAxisRight.setGridColor(Color.WHITE);
        yAxisRight.setGridColor(Color.TRANSPARENT);

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setDrawGridLines(true);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setGridColor(Color.WHITE);
    }
}
