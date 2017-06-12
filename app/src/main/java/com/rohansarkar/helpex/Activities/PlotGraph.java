package com.rohansarkar.helpex.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;
import com.rohansarkar.helpex.Adapters.PlotGraphAdapter;
import com.rohansarkar.helpex.CustomData.DataExperiment;
import com.rohansarkar.helpex.CustomData.DataGraph;
import com.rohansarkar.helpex.CustomData.DataRecord;
import com.rohansarkar.helpex.DatabaseManagers.DatabaseEperimentManager;
import com.rohansarkar.helpex.DatabaseManagers.DatabaseRecordsManager;
import com.rohansarkar.helpex.R;

import java.util.ArrayList;
import java.util.Collections;

import Assets.Util;

/**
 * Created by rohan on 25/5/17.
 */
public class PlotGraph extends AppCompatActivity implements View.OnClickListener{

    private final String LOG_TAG = getClass().getSimpleName();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager layoutManager;
    private CoordinatorLayout layout;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    ImageView overflowMenu;

    private DatabaseRecordsManager recordsManager;
    private DatabaseEperimentManager detailsManager;

    private ArrayList<ArrayList<String>> tableData;
    private ArrayList<Pair<String,String>> graphList;
    private ArrayList<ArrayList<String>> xValues;
    private ArrayList<ArrayList<Entry>> yValues;
    private DataExperiment experimentDetails;
    private ArrayList<String> columnList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_graph);

        init();
        getData();
        setToolbar();
        structureGraphData();
        setRecyclerView(yValues, xValues, graphList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detailsManager.close();
        recordsManager.close();
    }

    private void structureGraphData(){
        for(int i=0; i<graphList.size(); i++){
            int xPos = columnList.indexOf(graphList.get(i).first);
            int yPos = columnList.indexOf(graphList.get(i).second);
            ArrayList<DataGraph> graphData = new ArrayList<>();

            for(int j=0; j<tableData.size(); j++){
                try{
                    int x = Integer.parseInt(tableData.get(j).get(xPos));
                    float y = Float.parseFloat(tableData.get(j).get(yPos));
                    graphData.add(new DataGraph(tableData.get(j).get(xPos), y));
                }
                catch (Exception e){
                    Log.d(LOG_TAG, "Ignoring Row : " + j + " - (" +
                            tableData.get(j).get(xPos) + ", " + tableData.get(j).get(yPos) + ")");
                }
            }

            //Draw Graph if list has more than one Point.
            if(graphData.size() >=2){
                //Sort as per X-axis.
                Collections.sort(graphData);
                ArrayList<String> xData = new ArrayList<>();
                ArrayList<Entry> yData = new ArrayList<>();

                for(int j=0; j<graphData.size(); j++){
                    Log.d(LOG_TAG, "GraphData [" + j + "] : " + graphData.get(j).x + ", " + graphData.get(j).y);
                    xData.add(graphData.get(j).x);
                    yData.add(new Entry(graphData.get(j).y, Integer.parseInt(graphData.get(j).x)));
                }

                xValues.add(xData);
                yValues.add(yData);
            }
            else {
                //Remove from the graphList to be plotted.
                graphList.remove(i);
            }
        }

        if(xValues.size()<=0 || yValues.size()<=0){
            Toast.makeText(this, "Insufficient data for the Graph(s).", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void getData(){
        //Get details of Graph(s) to o be plotted.
        int size = getIntent().getIntExtra(Util.GRAPH_LIST_SIZE, -1);

        for(int i=0; i<size; i++){
            ArrayList<String> temp = Util.splitString(getIntent().getStringExtra(Util.GRAPH_LIST + i), "~");
            graphList.add(new Pair<String, String>(temp.get(0), temp.get(1)));
        }

        //Get Experiment Details.
        long rowId = getIntent().getLongExtra(Util.EXPERIMENT_ID, -1);
        experimentDetails = detailsManager.getExperimentDetails(rowId);
        columnList = Util.splitString(experimentDetails.columnNames, "~");

        //Get Record Data from Database.
        ArrayList<DataRecord> experimentRecords = recordsManager.getRecords((int) experimentDetails.experimentID);

        for (int i=0; i<experimentRecords.size(); i++){
            ArrayList<String> rowData = Util.splitString(experimentRecords.get(i).record, "~");
            tableData.add(rowData);
        }
    }

    private void init(){
        recyclerView = (RecyclerView) findViewById(R.id.rvPlotGraph);
        overflowMenu = (ImageView) findViewById(R.id.ivOverflowMenu);
        overflowMenu.setOnClickListener(this);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        yValues = new ArrayList<>();
        xValues = new ArrayList<>();
        graphList = new ArrayList<>();
        columnList = new ArrayList<>();
        tableData = new ArrayList<>();

        detailsManager = new DatabaseEperimentManager(this);
        recordsManager = new DatabaseRecordsManager(this);
        detailsManager.open();
        recordsManager.open();
    }

    private void setRecyclerView(ArrayList<ArrayList<Entry>> yValues, ArrayList<ArrayList<String>> xValues,
                                 ArrayList<Pair<String,String>> graphList){
        adapter = new PlotGraphAdapter(yValues, xValues, graphList, experimentDetails, this);
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
        toolbarTitle.setText(experimentDetails.title);
    }

    //Permission UX.
    private String[] permissionString = {"android.permission.WRITE_EXTERNAL_STORAGE"};
    private int PERMISSION_REQUEST_CODE = 3257;

    private boolean checkPerission(){
        return ContextCompat.checkSelfPermission(this, permissionString[0]) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, permissionString, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_REQUEST_CODE){

            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Permission Granted
                showToast("Permission Granted . . .");
            }
            else{
                //Permission not granted.
                if(Build.VERSION.SDK_INT >= Util.MARSHMALLOW){
                   if(ActivityCompat.shouldShowRequestPermissionRationale(this,  permissions[0])){
                       //Explain why you need the permission. Ask for permission again if user agrees.
                       createPermissionAlertBox();
                   }
                }
            }

        }
    }

    private void createPermissionAlertBox(){
        DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(Build.VERSION.SDK_INT >= Util.MARSHMALLOW){
                    requestPermission();
                }
            }
        };

        new AlertDialog.Builder(this).
                setTitle("Permission for saving Graphs.").
                setMessage("Please accept the permission for saving the Graphs.").
                setPositiveButton("Ok", okListener).
                setNegativeButton("Cancel",  null).
                create().
                show();
    }

    private void showToast(String message){
        Toast.makeText(this,  message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivOverflowMenu:
                //Inflate and Show Toolbar popup.
                PopupMenu toolbarMenu = new PopupMenu(this, overflowMenu);
                toolbarMenu.getMenuInflater().inflate(R.menu.popup_plot_graph, toolbarMenu.getMenu());
                toolbarMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId() == R.id.popup_export){
                            showToast("Export");
                        }
                        else if(menuItem.getItemId() == R.id.popup_redraw_graphs){
                            showToast("Redraw");
                        }
                        else if(menuItem.getItemId() == R.id.popup_save_graphs){
                            showToast("Save Graphs");
                        }
                        return false;
                    }
                });
                toolbarMenu.show();
                break;
        }
    }
}