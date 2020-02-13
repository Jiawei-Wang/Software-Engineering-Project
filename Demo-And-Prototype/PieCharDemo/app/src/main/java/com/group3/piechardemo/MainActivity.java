package com.group3.piechardemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private PieChart mPieChart;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        initData();

    }
    private void initView(){
        mPieChart = findViewById(R.id.mPieChart);

        mPieChart.setUsePercentValues(true);

        mPieChart.getDescription().setEnabled(false);

        mPieChart.setExtraOffsets(5, 10, 5, 5);

        mPieChart.setDragDecelerationFrictionCoef(0.95f);


        mPieChart.setCenterText(generateCenterSpannableText());



        mPieChart.setDrawHoleEnabled(true);

        mPieChart.setHoleColor(Color.WHITE);

        mPieChart.setTransparentCircleColor(Color.WHITE);

        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setHoleRadius(58f);

        mPieChart.setTransparentCircleRadius(61f);

        mPieChart.setDrawCenterText(true);

        mPieChart.setRotationAngle(0);


        mPieChart.setRotationEnabled(true);;

        mPieChart.setHighlightPerTapEnabled(true);


    }
    private void initData() {

//data

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        entries.add(new PieEntry(40, "Group1"));

        entries.add(new PieEntry(20, "Group2"));

        entries.add(new PieEntry(30, "Group3"));

        entries.add(new PieEntry(10, "Group4"));



        setData(entries);

        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);


        Legend l = mPieChart.getLegend();

        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);

        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

        l.setOrientation(Legend.LegendOrientation.VERTICAL);

        l.setDrawInside(false);

        l.setXEntrySpace(0f);

        l.setYEntrySpace(0f);

        l.setYOffset(0f);


        mPieChart.setDrawEntryLabels(true);

        mPieChart.setEntryLabelColor(Color.WHITE);

        mPieChart.setEntryLabelTextSize(12f);

    }

    private SpannableString generateCenterSpannableText() {


        SpannableString s = new SpannableString("se\n\nse");

        s.setSpan(new RelativeSizeSpan(1.7f), 0, 6, 0);



        return s;

    }
    private void setData(ArrayList<PieEntry> entries) {

        PieDataSet dataSet = new PieDataSet(entries, "Group3");

        dataSet.setSliceSpace(3f);

        dataSet.setSelectionShift(5f);

//color

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)

            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)

            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)

            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)

            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)

            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);

        data.setValueFormatter(new PercentFormatter());

        data.setValueTextSize(11f);

        data.setValueTextColor(Color.WHITE);

        mPieChart.setData(data);

        mPieChart.highlightValues(null);



    }



}
