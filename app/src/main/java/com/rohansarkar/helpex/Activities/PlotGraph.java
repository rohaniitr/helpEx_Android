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
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.rohansarkar.helpex.Adapters.PlotGraphAdapter;
import com.rohansarkar.helpex.CustomData.DataExperiment;
import com.rohansarkar.helpex.DatabaseManagers.DatabaseManager;
import com.rohansarkar.helpex.R;

import java.util.ArrayList;
import java.util.Random;

import Assets.SmartRecyclerView;
import Assets.Util;

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
    private DatabaseManager detailsManager;

    private ArrayList<Pair<String,String>> graphList;
    private ArrayList<ArrayList<String>> xValues;
    private ArrayList<ArrayList<Entry>> yValues;
    DataExperiment experimentData;
    ArrayList<String> columnList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_graph);

        init();
        getData();
        setToolbar();
        setRecyclerView(yValues, xValues, graphList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detailsManager.close();
    }

    private void getData(){
        int size = getIntent().getIntExtra(Util.GRAPH_LIST_SIZE, -1);

        for(int i=0; i<size; i++){
            ArrayList<String> temp = Util.splitString(getIntent().getStringExtra(Util.GRAPH_LIST + i), "~");
            graphList.add(new Pair<String, String>(temp.get(0), temp.get(1)));
        }

        long rowId = getIntent().getLongExtra(Util.EXPERIMENT_ID, -1);
        experimentData = detailsManager.getExperimentDetails(rowId);
        columnList = Util.splitString(experimentData.columnNames, "~");

        Random r = new Random();

        ArrayList<String> xData = new ArrayList<>();
        ArrayList<Entry> yData = new ArrayList<>();
        for(int i=0; i<100; i++) {
            xData.add(i+"");
            yData.add(new Entry((float)Math.sin(i), i));
        }
        xValues.add(xData);
        yValues.add(yData);

        ArrayList<String> x = new ArrayList<>();
        for(int i=0; i<20; i++){
            x.add(i+"");
        }

        for(int i=0; i<5; i++){
            ArrayList<Entry> y = new ArrayList<>();
            int val = 10;
            for(int j=0; j<20; j++){
                y.add(new Entry(r.nextInt(val) + r.nextFloat(), j));
            }
            yValues.add(y);
            xValues.add(x);
            val = val*10;
        }
    }

    private void init(){
        recyclerView = (RecyclerView) findViewById(R.id.rvPlotGraph);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        yValues = new ArrayList<>();
        xValues = new ArrayList<>();
        graphList = new ArrayList<>();
        columnList = new ArrayList<>();

        detailsManager = new DatabaseManager(this);
        detailsManager.open();
    }

    private void setRecyclerView(ArrayList<ArrayList<Entry>> yValues, ArrayList<ArrayList<String>> xValues,
                                 ArrayList<Pair<String,String>> graphList){
        adapter = new PlotGraphAdapter(yValues, xValues, graphList, this);
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
        toolbarTitle.setText(experimentData.title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}