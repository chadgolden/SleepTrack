package com.chadgolden.sleeptrack.io;

import android.content.Context;

import com.chadgolden.sleeptrack.data.SleepSession;

import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Chad on 4/18/2015.
 */
public class InternalStorageIO {

    private Date beginSleepTime;
    private List<String> times;
    private List<Integer> numberOfMovements;
    private Context context;

    public InternalStorageIO(Date beginSleepTime, List<String> times,
                             List<Integer> numberOfMovements, Context context) {
        this.beginSleepTime = beginSleepTime;
        this.times = times;
        this.numberOfMovements = numberOfMovements;
        this.context = context;
    }

    public InternalStorageIO(SleepSession sleepSession, Context context) {
        this.beginSleepTime = sleepSession.getBeginSleepSession();
        this.times = sleepSession.getTimes();
        this.numberOfMovements = sleepSession.getNumberOfMovements();
        this.context = context;
    }

    public void write() {
        FileOutputStream fileOutputStream;
        try {
            File file = new File(context.getFilesDir(), "ST_" + beginSleepTime.toString());
            fileOutputStream = new FileOutputStream(file);

        } catch (IOException e) {
            // IO exception code.
        }
    }

    /**
     * Encode future file contents into CSV style.
     * @return
     */
    public String encode() {
        StringBuilder retVal = new StringBuilder();
        for (int i = 0; i < times.size(); i++) {
            retVal.append(times.get(i) + "," + numberOfMovements.get(i) + "\n");
        }
        return retVal.toString();
    }

    public void printContextFilesDirectory() {

    }

}
