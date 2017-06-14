package com.rohansarkar.helpex.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.rohansarkar.helpex.CustomData.DataExperiment;
import com.rohansarkar.helpex.R;

import java.io.File;
import java.util.ArrayList;

import Assets.Util;

/**
 * Created by rohan on 25/5/17.
 */
public class PlotGraphAdapter extends RecyclerView.Adapter<PlotGraphAdapter.ViewHolder>{
    private ArrayList<Pair<String,String>> graphList;
    private ArrayList<ArrayList<String>> xValues;
    private ArrayList<ArrayList<Entry>> yValues;
    private DataExperiment experimentDetails;
    private RecyclerView recyclerView;
    private Context context;

    String LOG_TAG= this.getClass().getSimpleName();

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        LineChart lineChart;
        ImageView saveGraph;
        RelativeLayout lineChartLayout;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.tvGraphTitle);
            lineChart = (LineChart) v.findViewById(R.id.lcPlotGraph);
            lineChartLayout = (RelativeLayout) v.findViewById(R.id.rlLineChartLayout);
            saveGraph = (ImageView) v.findViewById(R.id.ivSaveGraph);
        }
    }

    public PlotGraphAdapter(ArrayList<ArrayList<Entry>> yValues, ArrayList<ArrayList<String>> xValues,
                            ArrayList<Pair<String,String>> graphList, DataExperiment experimentDetails,
                            RecyclerView recyclerView, Context context){
        this.yValues = yValues;
        this.xValues = xValues;
        this.graphList = graphList;
        this.experimentDetails = experimentDetails;
        this.recyclerView = recyclerView;
        this.context= context;
    }

    @Override
    public PlotGraphAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_plot_graph, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d(LOG_TAG, "Position : " + position);
        holder.title.setText(graphList.get(position).first + " vs " + graphList.get(position).second);

        holder.saveGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean status = Util.saveImage(holder.lineChartLayout, experimentDetails.title,
                        graphList.get(position).first + " vs " + graphList.get(position).second, context);;

                if(status){
                    showToast("Graph saved to " + context.getString(R.string.app_name) + File.separator +
                    experimentDetails.title.replace(" ","") + File.separator +
                    graphList.get(position).first + "vs" + graphList.get(position).second + ".jpg");
                }
                else {
                    showToast("Unable to save Graph.");
                }
            }
        });

        // create a dataset and give it a type
        LineDataSet set = new LineDataSet(yValues.get(position), graphList.get(position).first
                + " vs " + graphList.get(position).second);
        set.setFillAlpha(110);
        set.setColor(Color.BLACK);
        set.setCircleColor(Color.BLACK);
        set.setLineWidth(1f);
        set.setCircleRadius(3f);
        set.setDrawCircleHole(true);
        set.setValueTextSize(9f);
        set.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xValues.get(position), dataSets);

        // set data
        holder.lineChart.setData(data);
    }

    @Override
    public int getItemCount() {
        return graphList.size();
    }

    /*
    **Logic Implementation.
    * */

     /*
    **Database Interface.
    * */

    /*
    **Display Assets..
    * */

    private void showToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
