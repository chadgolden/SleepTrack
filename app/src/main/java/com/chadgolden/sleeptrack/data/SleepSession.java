package com.chadgolden.sleeptrack.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Chad on 4/18/2015.
 */
public class SleepSession {

    private Date beginSleepSession;
    private List<String> times;
    private List<Integer> numberOfMovements;

    public SleepSession() {
        beginSleepSession = new Date();
        times = new ArrayList<>();
        numberOfMovements = new ArrayList<>();
    }

    public SleepSession(Date beginSleepSession, List<String> times,
                        List<Integer> numberOfMovements) {
        this.beginSleepSession = beginSleepSession;
        this.times = times;
        this.numberOfMovements = numberOfMovements;
    }

    public void addEntry(String time, int movementCount) {
        times.add(time);
        numberOfMovements.add(movementCount);
    }

    public Date getBeginSleepSession() {
        return beginSleepSession;
    }

    public List<String> getTimes() {
        return times;
    }

    public List<Integer> getNumberOfMovements() {
        return numberOfMovements;
    }

}
