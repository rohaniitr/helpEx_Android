package com.rohansarkar.helpex.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.rohansarkar.helpex.R;

import java.util.ArrayList;

/**
 * Created by rohan on 25/5/17.
 */
public class PlotGraphAdapter extends RecyclerView.Adapter<PlotGraphAdapter.ViewHolder>{
    private ArrayList<String> titles;
    private ArrayList<LineGraphSeries<DataPoint>> series;
    private Context context;

    String LOG_TAG= this.getClass().getSimpleName();

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        GraphView graphView;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.tvGraphTitle);
            graphView = (GraphView) v.findViewById(R.id.gvPlotGraph);
        }
    }

    public PlotGraphAdapter(ArrayList<LineGraphSeries<DataPoint>> series, ArrayList<String> titles, Context context){
        this.series = series;
        this.titles = titles;
        this.context= context;
    }

    @Override
    public PlotGraphAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_plot_graph, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.title.setText(titles.get(position));
        holder.graphView.removeAllSeries();
        holder.graphView.addSeries(series.get(position));
    }

    @Override
    public int getItemCount() {
        return series.size();
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
