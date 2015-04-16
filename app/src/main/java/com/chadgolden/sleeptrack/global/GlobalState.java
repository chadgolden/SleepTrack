package com.chadgolden.sleeptrack.global;

import com.chadgolden.sleeptrack.activity.SensorActivity;

import java.util.Timer;

/**
 * Created by Chad on 4/15/2015.
 */
public class GlobalState {

    private static GlobalState instance;

    private Timer timer;
    private SensorActivity sensorActivity;

    private GlobalState() {
        timer = new Timer();
        sensorActivity = new SensorActivity();
    }

    public static GlobalState getInstance() {
        if (instance == null) {
            instance = new GlobalState();
        }
        return instance;
    }

    public Timer getTimer() {
        return timer;
    }

    public Timer newTimer() {
        timer = null;
        return timer = new Timer();
    }

    public SensorActivity getSensorActivity() {
        return sensorActivity;
    }



}
