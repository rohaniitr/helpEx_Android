package com.rohansarkar.helpex.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rohansarkar.helpex.CustomData.DataSelectColumn;
import com.rohansarkar.helpex.R;

import java.util.ArrayList;

/**
 * Created by rohan on 28/5/17.
 */
public class GraphSelectAdapter extends RecyclerView.Adapter<GraphSelectAdapter.ViewHolder>{
    private ArrayList<DataSelectColumn> columnNames;
    private ArrayList<Pair<String,String>> graphList;
    private RecyclerView.Adapter graphListAdapter;
    private LinearLayout graphRecyclerViewLayout;
    private TextView emptyLayout;
    private Context context;
    //Position of columns for X & Y axis.
    private int xPos;
    private int yPos;

    String LOG_TAG= this.getClass().getSimpleName();

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView columnName;
        ImageView tick;

        public ViewHolder(View v) {
            super(v);
            columnName = (TextView) v.findViewById(R.id.tvColumnName);
            tick = (ImageView) v.findViewById(R.id.ivTick);
        }
    }

    public GraphSelectAdapter(ArrayList<DataSelectColumn> columnNames, ArrayList<Pair<String,String>> graphList,
                              RecyclerView.Adapter graphListAdapter, LinearLayout graphRecyclerViewLayout,
                              TextView emptyLayout, Context context){
        this.columnNames = columnNames;
        this.graphList = graphList;
        this.graphListAdapter = graphListAdapter;
        this.graphRecyclerViewLayout = graphRecyclerViewLayout;
        this.emptyLayout = emptyLayout;
        this.context= context;
        xPos = -1;
        yPos = -1;
    }

    @Override
    public GraphSelectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_plot_graph_column, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.columnName.setText(columnNames.get(position).columnName);

        if(columnNames.get(position).isSelected){
            holder.tick.setVisibility(View.VISIBLE);
        }
        else {
            holder.tick.setVisibility(View.GONE);
        }

        holder.columnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                columnNames.get(position).isSelected = !columnNames.get(position).isSelected;

                if(xPos == -1){
                    //First element added. (X-axis)
                    xPos = position;
                }
                else if(xPos != position){
                    //Second element added. (Y-axis)
                    //Add Graph detail to graphList & Update UI.
                    graphList.add(new Pair<String, String>(columnNames.get(xPos).columnName, columnNames.get(position).columnName));
                    graphListAdapter.notifyItemInserted(columnNames.size()-1);
                    showToast("Added : " + graphList.get(graphList.size()-1).first + " vs " + graphList.get(graphList.size()-1).second);
                    //Assign Normal state to recyclerView elements.
                    columnNames.get(xPos).isSelected = false;
                    columnNames.get(position).isSelected = false;
                    xPos = -1;

                    //Hide emptyLayout & display recyclerView
                    emptyLayout.setVisibility(View.GONE);
                    graphRecyclerViewLayout.setVisibility(View.VISIBLE);
                }
                else{
                    //If selected column is selected again.
                    xPos = -1;
                }

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return columnNames.size();
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
