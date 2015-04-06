package com.chadgolden.sleeptrack.data;

import android.graphics.Color;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chad on 4/1/15.
 */
public class DataProcessor {

    private static DataProcessor instance;

    private ArrayList<Entry> dataEntries;
    private ArrayList<String> labels;
    private String title;

    private int bufferIndex = 0;
    private int bufferSize = 8;

    private DataProcessor() {
//        title = "Test Chart";
//        labels = new ArrayList<>();
//        labels.add("--:--");
//        labels.add("--:--");
//        labels.add("--:--");
//        labels.add("--:--");
//        labels.add("--:--");
//        //labels.add("June");
//        dataEntries = new ArrayList<>();
//        dataEntries.add(new Entry(0f, 0));
//        dataEntries.add(new Entry(0f, 1));
//        dataEntries.add(new Entry(0f, 2));
//        dataEntries.add(new Entry(0f, 3));
//        dataEntries.add(new Entry(0f, 4));
//        dataEntries.add(new Entry(9f, 5));
        init();
    }

    private void init() {
        title = "Test Chart";
        labels = new ArrayList<>();
        dataEntries = new ArrayList<>();
        for (int i = 0; i < bufferSize; i++) {
            labels.add("--:--:--");
            dataEntries.add(new Entry(0, i));
        }
    }

    private DataProcessor(ArrayList<Entry> dataEntries, String title) {
        this.dataEntries = dataEntries;
        this.title = title;
    }

    public LineDataSet getDataSet() {
        LineDataSet dataSet = new LineDataSet(dataEntries, title);
        dataSet.setDrawCircles(true);
        dataSet.setLineWidth(2.0f);
        dataSet.setColor(Color.BLACK);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.CYAN);
        dataSet.setDrawCubic(true);
        dataSet.setFillAlpha(Color.alpha(127));
        return dataSet;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public void addEntry(String time, float data) {
        if (bufferIndex < bufferSize) {
            dataEntries.remove(bufferIndex);
            dataEntries.add(bufferIndex, new Entry(data, bufferIndex));
            labels.remove(bufferIndex);
            labels.add(bufferIndex, time);
        } else {
            bufferIndex = 0;
            dataEntries.remove(bufferIndex);
            dataEntries.add(bufferIndex, new Entry(data, bufferIndex));
            labels.remove(bufferIndex);
            labels.add(bufferIndex, time);
        }
        bufferIndex++;
    }

//    public void addEntry(String time, float data) {
//        addEntry(data);
//        addLabel(time);
//    }

    public void addLabel(String label) {
        if (bufferIndex < 5) {
            labels.add(bufferIndex, label);
        } else {
            bufferIndex = 0;
            labels.add(bufferIndex, label);
        }
    }

    public static synchronized DataProcessor getInstance() {
        if (instance == null) {
            instance = new DataProcessor();
        }
        return instance;
    }
}
