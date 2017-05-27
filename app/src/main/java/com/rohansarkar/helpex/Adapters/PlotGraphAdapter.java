package com.rohansarkar.helpex.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.rohansarkar.helpex.R;

import java.util.ArrayList;

/**
 * Created by rohan on 25/5/17.
 */
public class PlotGraphAdapter extends RecyclerView.Adapter<PlotGraphAdapter.ViewHolder>{
    private ArrayList<String> titles;
    private ArrayList<ArrayList<String>> xValues;
    private ArrayList<ArrayList<Entry>> yValues;
    private Context context;

    String LOG_TAG= this.getClass().getSimpleName();

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        LineChart lineChart;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.tvGraphTitle);
            lineChart = (LineChart) v.findViewById(R.id.lcPlotGraph);
        }
    }

    public PlotGraphAdapter(ArrayList<ArrayList<Entry>> yValues, ArrayList<ArrayList<String>> xValues,
                            ArrayList<String> titles, Context context){
        this.yValues = yValues;
        this.xValues = xValues;
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

        // create a dataset and give it a type
        LineDataSet set = new LineDataSet(yValues.get(position), "DataSet " + position);
        set.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        // set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set.setColor(Color.BLACK);
        set.setCircleColor(Color.BLACK);
        set.setLineWidth(1f);
        set.setCircleRadius(3f);
        set.setDrawCircleHole(false);
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
        return yValues.size();
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
