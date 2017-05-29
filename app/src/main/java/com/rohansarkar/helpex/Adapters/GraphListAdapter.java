package com.rohansarkar.helpex.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rohansarkar.helpex.R;

import java.util.ArrayList;

/**
 * Created by rohan on 29/5/17.
 */
public class GraphListAdapter extends RecyclerView.Adapter<GraphListAdapter.ViewHolder>{
    private ArrayList<Pair<String,  String>> graphList;
    private LinearLayout recyclerViewLayout;
    private TextView emptyLayout;
    private Context context;

    String LOG_TAG= this.getClass().getSimpleName();

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView graphColumns;
        ImageView cross;

        public ViewHolder(View v) {
            super(v);
            graphColumns = (TextView) v.findViewById(R.id.tvGraphColumns);
            cross = (ImageView) v.findViewById(R.id.ivCross);
        }
    }

    public GraphListAdapter(ArrayList<Pair<String,  String>> graphList, LinearLayout recyclerViewLayout, TextView emptyLayout,
                            Context context){
        this.graphList = graphList;
        this.recyclerViewLayout = recyclerViewLayout;
        this.emptyLayout = emptyLayout;
        this.context= context;
    }

    @Override
    public GraphListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_graph_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.graphColumns.setText((position + 1) + ". " + graphList.get(position).first + " vs " + graphList.get(position).second);

        holder.cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                graphList.remove(position);
                notifyDataSetChanged();

                if(graphList.size() <= 0){
                    recyclerViewLayout.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                }
            }
        });
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
