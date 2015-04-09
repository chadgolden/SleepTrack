package com.chadgolden.sleeptrack.data;

import android.graphics.Color;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chad on 4/1/15.
 */
public class DataProcessor {

    private static DataProcessor instance;

    private CircularFifoQueue<Entry> dataEntries;
    private CircularFifoQueue<String> labels;

   // private ArrayList<Entry> dataEntries;
    //private ArrayList<String> labels;
    private String title;

    private int bufferIndex = 0;
    private int bufferSize = 10;
    private int numberOfEntries = 0;

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
        dataEntries = new CircularFifoQueue<>(bufferSize);
        labels = new CircularFifoQueue<>(bufferSize);
        for (int i = 0; i < bufferSize; i++) {
            dataEntries.add(new Entry(0f, i));
            labels.add("--:--:--");
        }
    }

    public LineDataSet getDataSet() {
        LineDataSet dataSet = new LineDataSet(getDataEntriesAsArrayList(), title);
        dataSet.setDrawCircles(true);
        dataSet.setLineWidth(2.0f);
        dataSet.setColor(Color.BLACK);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.rgb(0,255,127));
        dataSet.setDrawCubic(true);
        dataSet.setFillAlpha(Color.alpha(127));
        return dataSet;
    }

    public void addEntry(String time, float data) {
        labels.add(time);
        dataEntries.add(new Entry(data, numberOfEntries++));
        updateYValues();
    }


    public static synchronized DataProcessor getInstance() {
        if (instance == null) {
            instance = new DataProcessor();
        }
        return instance;
    }

    public ArrayList<Entry> getDataEntriesAsArrayList() {
        return new ArrayList<>(dataEntries);
    }

    public ArrayList<String> getLabelsAsArrayList() {
        return new ArrayList<>(labels);
    }

    private void updateYValues() {
        for (int i = 0; i < dataEntries.size(); i++) {
            dataEntries.get(i).setXIndex(i);
        }
    }
}
