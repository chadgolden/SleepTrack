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

    private DataProcessor() {
        title = "Test Chart";
        labels = new ArrayList<>();
//        labels.add("January");
//        labels.add("February");
//        labels.add("March");
//        labels.add("April");
//        labels.add("May");
//        labels.add("June");
        dataEntries = new ArrayList<>();
//        dataEntries.add(new Entry(4f, 0));
//        dataEntries.add(new Entry(8f, 1));
//        dataEntries.add(new Entry(6f, 2));
//        dataEntries.add(new Entry(12f, 3));
//        dataEntries.add(new Entry(18f, 4));
//        dataEntries.add(new Entry(9f, 5));
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

    public void addEntry(float data) {
        if (bufferIndex < 5) {
            dataEntries.remove(bufferIndex);
            dataEntries.add(bufferIndex, new Entry(data, bufferIndex));
            labels.add(bufferIndex, System.currentTimeMillis() + "");
        } else {
            bufferIndex = 0;
            dataEntries.remove(bufferIndex);
            dataEntries.add(bufferIndex, new Entry(data, bufferIndex));
            labels.add(bufferIndex, System.currentTimeMillis() + "");
        }
        bufferIndex++;
    }

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
