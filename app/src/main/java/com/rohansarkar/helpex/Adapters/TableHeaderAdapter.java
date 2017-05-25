package com.rohansarkar.helpex.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rohansarkar.helpex.R;

import java.util.ArrayList;

/**
 * Created by rohan on 23/5/17.
 */
public class TableHeaderAdapter extends RecyclerView.Adapter<TableHeaderAdapter.ViewHolder>{
    private ArrayList<String> headerValues;
    private Context context;

    String LOG_TAG= this.getClass().getSimpleName();

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView header;
        RelativeLayout elementLayout;

        public ViewHolder(View v) {
            super(v);
            header = (TextView) v.findViewById(R.id.tvTableHeader);
            elementLayout = (RelativeLayout) v.findViewById(R.id.rlHeaderCell);
        }
    }

    public TableHeaderAdapter(ArrayList<String> headerValues, Context context){
        this.headerValues = headerValues;
        this.context= context;
    }

    @Override
    public TableHeaderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_table_header_cell, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.header.setText(headerValues.get(position));
    }

    @Override
    public int getItemCount() {
        return headerValues.size();
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
