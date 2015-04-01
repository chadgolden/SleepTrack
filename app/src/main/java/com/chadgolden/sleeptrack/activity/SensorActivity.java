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


public class SensorActivity extends ActionBarActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private long lastUpdateTimeMillis;

    private TextView moveStatus;
    private ImageView moonImage;

    private Button buttonStats;

    private float previousX;
    private float previousY;
    private float previousZ;

    private static final int DELAY = 1000; // In milliseconds.
    private static final float ALLOWABLE_MOVEMENT = 0.10f;

    private int segmentMovementCount = 0;
    private int segmentDuration = 5000;
    private long lastEntry = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        lastUpdateTimeMillis = System.currentTimeMillis();

        moveStatus = (TextView) findViewById(R.id.textViewMoveStatus);
        moonImage = (ImageView) findViewById(R.id.moonImage);

        buttonStats = (Button) findViewById(R.id.buttonStats);

        buttonStats.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent chartIntent = new Intent(SensorActivity.this, ChartActivity.class);
                startActivity(chartIntent);
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
                if (sensorExteedsAllowableMovement(event.values)) {
                    segmentMovementCount++;
                    if ((System.currentTimeMillis() - lastEntry) > segmentDuration) {
                        lastEntry = System.currentTimeMillis();
                        DataProcessor.getInstance().addEntry(segmentMovementCount);
                        segmentMovementCount = 0;
                    }
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

    private boolean sensorExteedsAllowableMovement(float[] values) {
        if (Math.abs(values[0] - previousX) > ALLOWABLE_MOVEMENT ||
            Math.abs(values[1] - previousY) > ALLOWABLE_MOVEMENT ||
            Math.abs(values[2] - previousZ) > ALLOWABLE_MOVEMENT) {
            return true;
        }
        return false;
    }
}
