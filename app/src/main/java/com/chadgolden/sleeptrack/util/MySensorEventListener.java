package com.chadgolden.sleeptrack.util;

import android.hardware.SensorEventListener;

/**
 * Created by Chad on 4/19/2015.
 */
public interface MySensorEventListener extends SensorEventListener {

    boolean sensorExceedsAllowableMovement(float[] sensorValues);

}
