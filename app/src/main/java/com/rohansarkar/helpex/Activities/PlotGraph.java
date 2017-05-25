package com.rohansarkar.helpex.Activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.rohansarkar.helpex.Adapters.PlotGraphAdapter;
import com.rohansarkar.helpex.Adapters.TableAdapter;
import com.rohansarkar.helpex.R;

import java.util.ArrayList;
import java.util.Random;

import Assets.SmartRecyclerView;

/**
 * Created by rohan on 25/5/17.
 */
public class PlotGraph extends AppCompatActivity{

    private final String LOG_TAG = getClass().getSimpleName();
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    LinearLayoutManager layoutManager;
    CoordinatorLayout layout;
    Toolbar toolbar;
    TextView toolbarTitle;

    private ArrayList<String> titles;
    private ArrayList<LineGraphSeries<DataPoint>> series;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_graph);

        init();
        getData();
        setToolbar();
        setRecyclerView(series, titles);
    }

    private void getData(){
        Random r = new Random();

        LineGraphSeries<DataPoint> data1 = new LineGraphSeries<>();
        for(int i=0; i<15; i++)
            data1.appendData(new DataPoint(i, Math.sin(i)),  true, 15);
        series.add(data1);

        LineGraphSeries<DataPoint> data2 = new LineGraphSeries<>();
        for(int i=0; i<15; i++)
            data2.appendData(new DataPoint(i, r.nextInt(1000) + (i*i)),  true, 15);
        series.add(data2);

        LineGraphSeries<DataPoint> data3 = new LineGraphSeries<>();
        for(int i=0; i<15; i++)
            data3.appendData(new DataPoint(i, r.nextInt(1000) +  (i*i*i) + (i*i)),  true, 15);
        series.add(data3);

        LineGraphSeries<DataPoint> data4 = new LineGraphSeries<>();
        for(int i=0; i<15; i++)
            data4.appendData(new DataPoint(i, r.nextInt(1000)),  true, 15);
        series.add(data4);

        LineGraphSeries<DataPoint> data5 = new LineGraphSeries<>();
        for(int i=0; i<15; i++)
            data5.appendData(new DataPoint(i, r.nextInt(100)),  true, 15);
        series.add(data5);

        titles.add("X vs Theta");
        titles.add("Alpha vs Gamma");
        titles.add("Beta vs Y");
        titles.add("Y vs Alpha");
        titles.add("sin(Theta) vs Gamma");
    }

    private void init(){
        recyclerView = (RecyclerView) findViewById(R.id.rvPlotGraph);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        series = new ArrayList<>();
        titles = new ArrayList<>();
    }

    private void setRecyclerView(ArrayList<LineGraphSeries<DataPoint>> series, ArrayList<String> titles){
        adapter = new PlotGraphAdapter(series, titles, this);
        recyclerView.setAdapter(adapter);
    }

    private void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.tbPlotGraph);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(Color.parseColor("#dddddd"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);
        toolbarTitle.setText("Malus Law");
    }
}