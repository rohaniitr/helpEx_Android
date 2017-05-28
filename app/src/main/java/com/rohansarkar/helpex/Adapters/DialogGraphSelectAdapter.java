package com.rohansarkar.helpex.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rohansarkar.helpex.CustomData.DataSelectColumn;
import com.rohansarkar.helpex.R;

import java.util.ArrayList;

/**
 * Created by rohan on 28/5/17.
 */
public class DialogGraphSelectAdapter extends RecyclerView.Adapter<DialogGraphSelectAdapter.ViewHolder>{
    private ArrayList<DataSelectColumn> columnName;
    private Context context;

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

    public DialogGraphSelectAdapter(ArrayList<DataSelectColumn> columnName, Context context){
        this.columnName = columnName;
        this.context= context;
    }

    @Override
    public DialogGraphSelectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_plot_graph_column, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.columnName.setText(columnName.get(position).columnName);

        if(columnName.get(position).isSelected){
            holder.tick.setVisibility(View.VISIBLE);
        }
        else {
            holder.tick.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return columnName.size();
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
