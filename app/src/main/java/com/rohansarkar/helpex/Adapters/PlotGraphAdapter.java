package com.rohansarkar.helpex.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.rohansarkar.helpex.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by rohan on 25/5/17.
 */
public class PlotGraphAdapter extends RecyclerView.Adapter<PlotGraphAdapter.ViewHolder>{
    private ArrayList<Pair<String,String>> graphList;
    private ArrayList<ArrayList<String>> xValues;
    private ArrayList<ArrayList<Entry>> yValues;
    private Context context;

    String LOG_TAG= this.getClass().getSimpleName();

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        LineChart lineChart;
        RelativeLayout lineChartLayout;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.tvGraphTitle);
            lineChart = (LineChart) v.findViewById(R.id.lcPlotGraph);
            lineChartLayout = (RelativeLayout) v.findViewById(R.id.rlLineChartLayout);
        }
    }

    public PlotGraphAdapter(ArrayList<ArrayList<Entry>> yValues, ArrayList<ArrayList<String>> xValues,
                            ArrayList<Pair<String,String>> graphList, Context context){
        this.yValues = yValues;
        this.xValues = xValues;
        this.graphList = graphList;
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
        holder.title.setText(graphList.get(position).first + " vs " + graphList.get(position).second);

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage(holder.lineChartLayout);
            }
        });

        // create a dataset and give it a type
        LineDataSet set = new LineDataSet(yValues.get(position), "DataSet " + position);
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

    private void saveImage(View view){
        String imagePath = Environment.getExternalStorageDirectory().toString() + "/" + "helpEx.jpeg";
        //Create bitmap image path.
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        OutputStream fout = null;
        File imageFile = new File(imagePath);

        try {
            fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90,  fout);
            fout.flush();
            fout.close();
            Log.d(LOG_TAG, "Path : " + imagePath);
        }
        catch (Exception e){
            e.printStackTrace();
            Log.d(LOG_TAG, "Exception in saving image.");
        }

    }
}
