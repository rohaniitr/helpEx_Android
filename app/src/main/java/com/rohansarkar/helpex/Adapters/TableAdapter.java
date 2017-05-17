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
import android.widget.TextView;
import android.widget.Toast;

import com.rohansarkar.helpex.R;

import java.util.ArrayList;

import Assets.TableHorizontalScrollView;

/**
 * Created by rohan on 17/5/17.
 */
public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder>{
    private Context context;
    private CoordinatorLayout layout;
//    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    LinearLayoutManager layoutManager;
    private TableHorizontalScrollView.OnScrollListener listener;

    private ArrayList<String> tableData;

    String LOG_TAG= "TableAdapter Logs";

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView rowNo;
        RecyclerView recyclerView;
        TableHorizontalScrollView scrollView;

        public ViewHolder(View v) {
            super(v);
            rowNo = (TextView) v.findViewById(R.id.tvTableRowNo);
            scrollView = (TableHorizontalScrollView) v.findViewById(R.id.hsvTableRow);
            Log.d(LOG_TAG, "14");
            recyclerView = (RecyclerView) v.findViewById(R.id.rvExperimentRow);
            Log.d(LOG_TAG, "15");
        }
    }

    public TableAdapter(ArrayList<String> tableData, Context context, CoordinatorLayout layout,
                        TableHorizontalScrollView.OnScrollListener listener){
        this.tableData = tableData;
        this.layout= layout;
        this.context= context;
        this.listener = listener;
    }

    @Override
    public TableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_table_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(LOG_TAG, "11");
        holder.rowNo.setText(tableData.get(position));
        Log.d(LOG_TAG, "12");
        holder.rowNo.setHeight(160);
        Log.d(LOG_TAG, "13");
        holder.scrollView.setOnScrollListener(listener);

        Log.d(LOG_TAG, "1");
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        Log.d(LOG_TAG, "2");
        holder.recyclerView.setLayoutManager(layoutManager);
        Log.d(LOG_TAG, "3");
        setRecyclerView(holder, tableData, position);
        Log.d(LOG_TAG, "4");
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

    private void setRecyclerView(ViewHolder holder, ArrayList<String> list, int position){
        Log.d(LOG_TAG, "5");
        adapter = new TableRowAdapter(list, context, layout, position, listener);
        Log.d(LOG_TAG, "6");
        holder.recyclerView.setAdapter(adapter);
        Log.d(LOG_TAG, "7");
    }

    private void showSnackBar(String message){
        if(layout!= null)
            Snackbar.make(layout, message, Snackbar.LENGTH_SHORT).show();
    }

    private void showToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
