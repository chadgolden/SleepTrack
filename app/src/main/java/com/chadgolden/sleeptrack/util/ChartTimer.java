package com.chadgolden.sleeptrack.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

/**
 * Created by chad on 4/6/15.
 */
public class ChartTimer extends TimerTask {

    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        final String time = sdf.format(calendar.getTime());
    }
}
