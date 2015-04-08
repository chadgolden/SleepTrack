package com.chadgolden.sleeptrack.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chadgolden.sleeptrack.R;
import com.chadgolden.sleeptrack.data.DataProcessor;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.FillFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class SensorActivity extends ActionBarActivity implements SensorEventListener {

    public static int INTERVAL = 500;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private long lastUpdateTimeMillis;

    private TextView moveStatus;
    private ImageView moonImage;

    private Button buttonStats;

    private float previousX;
    private float previousY;
    private float previousZ;

    private static final int DELAY = 10; // In milliseconds.
    private static final float ALLOWABLE_MOVEMENT = 0.04f;

    private int segmentMovementCount = 0;
    private int segmentDuration = 5000;
    private long lastEntry = System.currentTimeMillis();

    private Timer timer;
    private ChartTimer timerTask;

    private LineChart lineChart;
    private LineData lineData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        lineData = new LineData(
                DataProcessor.getInstance().getLabelsAsArrayList(),
                DataProcessor.getInstance().getDataSet()
        );

        lineChart = new LineChart(getApplicationContext());
        lineChart.setData(lineData);
        lineChart.setVisibleYRange(50, YAxis.AxisDependency.LEFT);
        lineChart.setMaxVisibleValueCount(10);
        lineChart.setFillFormatter(new FillFormatter() {
            @Override
            public float getFillLinePosition(LineDataSet lineDataSet, LineData lineData, float v, float v2) {
                return 0;
            }
        });
//        lineChart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setContentView(R.layout.activity_sensor);
//            }
//        });

        timer = new Timer();
        timerTask = new ChartTimer();

        // Set timer schedule to run timer task.
        timer.schedule(timerTask, 0, INTERVAL);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        lastUpdateTimeMillis = System.currentTimeMillis();

        moveStatus = (TextView) findViewById(R.id.textViewMoveStatus);
        moonImage = (ImageView) findViewById(R.id.moonImage);

        buttonStats = (Button) findViewById(R.id.buttonStats);

        buttonStats.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Intent chartIntent = new Intent(SensorActivity.this, ChartActivity.class);
//                startActivity(chartIntent);
                setContentView(lineChart);
            }
        });


        moonImage.bringToFront();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sensor, menu);
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
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        long currentTimeInMillis = System.currentTimeMillis();

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if ((currentTimeInMillis - lastUpdateTimeMillis) >= DELAY) {
                lastUpdateTimeMillis = currentTimeInMillis;
                if (sensorExceedsAllowableMovement(event.values)) {
                    segmentMovementCount++;
//                    if ((System.currentTimeMillis() - lastEntry) > segmentDuration) {
//                        lastEntry = System.currentTimeMillis();
//                        DataProcessor.getInstance().addEntry(segmentMovementCount);
//                        segmentMovementCount = 0;
//                    }
                    moveStatus.setTextColor(Color.GREEN);
                } else {
                    moveStatus.setTextColor(Color.rgb(50,50,50));
                }

            }
        }

        previousX = event.values[0];
        previousY = event.values[1];
        previousZ = event.values[2];
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not interested.
    }

    private boolean sensorExceedsAllowableMovement(float[] values) {
        if (Math.abs(values[0] - previousX) > ALLOWABLE_MOVEMENT ||
            Math.abs(values[1] - previousY) > ALLOWABLE_MOVEMENT ||
            Math.abs(values[2] - previousZ) > ALLOWABLE_MOVEMENT) {
            return true;
        }
        return false;
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
            lineChart.setData(
                    new LineData(
                            DataProcessor.getInstance().getLabelsAsArrayList(),
                            DataProcessor.getInstance().getDataSet()
                    )
            );
            System.err.println(DataProcessor.getInstance().getDataSet().isDrawFilledEnabled());
            lineChart.getLineData().getDataSetByIndex(0).setDrawFilled(true);
            lineChart.getLineData().getDataSetByIndex(0).setFillAlpha(127);

            segmentMovementCount = 0;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lineChart.invalidate();
                }
            });
        }
    }

}
