package com.chadgolden.sleeptrack.data;

import android.content.Context;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

/**
 * Created by Chad on 4/6/2015.
 */
public class LineChartWrapper {

    public static LineChartWrapper instance;

    private LineChart lineChart;

    private LineChartWrapper(Context context) {
        lineChart = new LineChart(context);
        lineChart.setDescription("");
        lineChart.setDrawGridBackground(false);
        lineChart.setData(new LineData());
        lineChart.invalidate();
    }

    public static synchronized LineChartWrapper getInstance(Context context) {
        if (instance == null) {
            instance = new LineChartWrapper(context);
        }
        return instance;
    }

    public void addEntry(String xValue, float yValue) {
        LineData data = lineChart.getLineData();
        if (data != null) {
            LineDataSet dataSet = data.getDataSetByIndex(0);
            if (dataSet == null) {
                dataSet = createDataSet();
                data.addDataSet(dataSet);
            }
        }

    }

    public void addDataSet() {

    }

    public void removeDataSet() {

    }

    public LineDataSet createDataSet() {
        return null;
    }



}
