package com.chadgolden.sleeptrack.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chadgolden.sleeptrack.R;
import com.chadgolden.sleeptrack.data.DataProcessor;
import com.chadgolden.sleeptrack.data.SleepSession;
import com.chadgolden.sleeptrack.global.GlobalState;
import com.chadgolden.sleeptrack.io.InternalStorageIO;
import com.chadgolden.sleeptrack.util.MySensorEventListener;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class SensorActivity extends ActionBarActivity implements MySensorEventListener {

    public static int INTERVAL = 1000*10;
    public static int MAX_Y_VALUE = 100;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private long lastUpdateTimeMillis;

    private TextView moveStatus;
    private ImageView moonImage;

    private Button buttonStats;
    private Button buttonStart;

    private float previousX;
    private float previousY;
    private float previousZ;

    public static final int DELAY = 5; // In milliseconds.
    public static final float ALLOWABLE_MOVEMENT = 0.04f;

    private int segmentMovementCount = 0;
    private int segmentDuration = 5000;
    private long lastEntry = System.currentTimeMillis();

    private SleepSession sleepSession;
    private Timer timer;
    private ChartTimer timerTask;
    private SleepRecorder sleepRecorder;

    private LineChart lineChart;
    private LineData lineData;

    private boolean timerRunning;

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

//        lineChart.setFillFormatter(new FillFormatter() {
//            @Override
//            public float getFillLinePosition(LineDataSet lineDataSet, LineData lineData, float v, float v2) {
//                return 0;
//            }
//        });
//        lineChart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setContentView(R.layout.activity_sensor);
//            }
//        });

        timerRunning = false;

        //timer = GlobalState.getInstance().getTimer();
        timerTask = new ChartTimer();

        // Set timer schedule to run timer task.
        //timer.schedule(timerTask, 0, INTERVAL);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        lastUpdateTimeMillis = System.currentTimeMillis();

        moveStatus = (TextView) findViewById(R.id.textViewMoveStatus);
        moonImage = (ImageView) findViewById(R.id.moonImage);

        buttonStats = (Button) findViewById(R.id.buttonStats);
        buttonStart = (Button) findViewById(R.id.buttonStart);

        buttonStart.setText((timerRunning) ? "Stop" : "Start");

        buttonStats.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
//                Intent chartIntent = new Intent(SensorActivity.this, ChartActivity.class);
//                startActivity(chartIntent);
                setContentView(lineChart);
                System.out.println("Stats button clicked.");
            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!timerRunning) {
                    buttonStart.setText("Stop");
                    mSensorManager.registerListener(
                            SensorActivity.this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL
                    );
                    timer = GlobalState.getInstance().newTimer();
                    timer.schedule(timerTask, 0, INTERVAL);
                } else if (timerRunning) {
                    buttonStart.setText("Start");
                    if (timerTask.cancel()) {
                        System.out.println("Timer Task canceled.");
                    }
                    timer.cancel();
                    mSensorManager.unregisterListener(SensorActivity.this, mAccelerometer);
                }
                System.out.println("Start/Stop button clicked.");

            }

        });

        buttonStart.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonStart.getText().equals("Start")) {
                    buttonStart.setText("Finish");
                    sleepSession = new SleepSession();
                    mSensorManager.registerListener(
                            SensorActivity.this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL
                    );
                    timer = GlobalState.getInstance().newTimer();
                    sleepRecorder = new SleepRecorder(sleepSession);
                    timer.schedule(sleepRecorder, 0, INTERVAL);
                } else if (buttonStart.getText().equals("Finish")) {
                    sleepRecorder.cancel();
                    timer.cancel();
                    mSensorManager.unregisterListener(SensorActivity.this, mAccelerometer);
                    System.out.println("Sleep tracking session completed.");
                    AlertDialog.Builder builder = new AlertDialog.Builder(SensorActivity.this);
                    builder
                        .setTitle("Save Session")
                        .setMessage("Would you like to save your session?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FileOutputStream stream = null;
                                try {
                                    stream = getApplicationContext()
                                            .openFileOutput(
                                                    sleepSession.getBeginSleepSession().getTime() +
                                                    ".stf", // SleepTrack format
                                                    Context.MODE_PRIVATE);

                                    InternalStorageIO io = new InternalStorageIO(
                                            sleepSession,
                                            getApplicationContext()
                                    );
                                    stream.write(io.encode().getBytes());
                                    String[] list = fileList();

                                    stream.flush();
                                    stream.close();

                                    Toast.makeText(getBaseContext(),"Saved Successfully.",
                                            Toast.LENGTH_SHORT).show();

                                } catch (Exception e) {

                                } finally {
                                    if (stream != null) {
                                        stream = null;
                                    }
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
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

    public boolean sensorExceedsAllowableMovement(float[] values) {
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
            lineChart.getAxisLeft().setAxisMaxValue(INTERVAL/50);
            lineChart.getAxisLeft().setAxisMinValue(0.0f);
            lineChart.getAxisRight().setAxisMaxValue(INTERVAL/50);
            lineChart.getAxisRight().setAxisMinValue(0.0f);
            lineChart.getAxisLeft().setDrawGridLines(false);
            lineChart.getAxisRight().setDrawGridLines(false);
            lineChart.getXAxis().setDrawGridLines(false);
            lineChart.getLineData().getDataSetByIndex(0).setDrawCircles(false);
            lineChart.getLineData().setDrawValues(false);
            segmentMovementCount = 0;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lineChart.invalidate();
                }
            });
        }
    }

    class SleepRecorder extends TimerTask {

        SleepSession sleepSession;

        public SleepRecorder(SleepSession sleepSession) {
            this.sleepSession = sleepSession;
        }

        @Override
        public void run() {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            final String time = sdf.format(calendar.getTime());
            sleepSession.addEntry(time, segmentMovementCount);
            segmentMovementCount = 0;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            setContentView(R.layout.activity_sensor);
            System.out.println("Back button clicked.");
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
