package com.rohansarkar.helpex.Adapters;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rohansarkar.helpex.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Random;

import Assets.TableHorizontalScrollView;

/**
 * Created by rohan on 17/5/17.
 */
public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder>{
    private Context context;
    private CoordinatorLayout layout;

    private ArrayList<ArrayList<String>> tableData;
    private ArrayList<String> columnList;

    String LOG_TAG= "TableAdapter Logs";

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView rowNo;
        LinearLayout rowLayout;
        RecyclerView rowRecyclerView;

        public ViewHolder(View v) {
            super(v);
            rowNo = (TextView) v.findViewById(R.id.tvTableRowNo);
            rowRecyclerView = (RecyclerView) v.findViewById(R.id.rvTableRow);
            rowLayout = (LinearLayout) v.findViewById(R.id.llTableRow);
        }
    }

    public TableAdapter(ArrayList<ArrayList<String>> tableData, ArrayList<String> columnList, Context context,
                        CoordinatorLayout layout){
        this.tableData = tableData;
        this.columnList = columnList;
        this.layout= layout;
        this.context= context;
    }

    @Override
    public TableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_table_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.rowNo.setText((position + 1) + ".");

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.rowRecyclerView.setLayoutManager(layoutManager);
        holder.rowRecyclerView.setHasFixedSize(true);

        TableRowAdapter rowAdapter = new TableRowAdapter(tableData.get(position), context);
        holder.rowRecyclerView.setAdapter(rowAdapter);
    }

    @Override
    public int getItemCount() {
        return tableData.size();
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

    private void showSnackBar(String message){
        if(layout!= null)
            Snackbar.make(layout, message, Snackbar.LENGTH_SHORT).show();
    }

    private void showToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
