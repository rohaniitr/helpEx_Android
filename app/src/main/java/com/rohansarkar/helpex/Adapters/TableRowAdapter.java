package com.rohansarkar.helpex.Adapters;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rohansarkar.helpex.R;

import java.util.ArrayList;

import Assets.TableHorizontalScrollView;

/**
 * Created by rohan on 17/5/17.
 */
public class TableRowAdapter extends RecyclerView.Adapter<TableRowAdapter.ViewHolder>{
    private Context context;
    private CoordinatorLayout layout;
    private RecyclerView recyclerView;
    private TableHorizontalScrollView.OnScrollListener listener;

    private ArrayList<String> tableData;
    private int  rowNo;

    String LOG_TAG= "TableAdapter Logs";

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView headerCell;
        EditText cell;
        RelativeLayout cellLayout, headerLayout;

        public ViewHolder(View v) {
            super(v);
            cell = (EditText) v.findViewById(R.id.etTableCell);
            headerCell = (TextView) v.findViewById(R.id.tvTableHeader);
            cellLayout = (RelativeLayout) v.findViewById(R.id.rlTableCell);
            headerLayout = (RelativeLayout) v.findViewById(R.id.rlHeader);
        }
    }

    public TableRowAdapter(ArrayList<String> tableData, Context context, CoordinatorLayout layout,
                        int rowNo, TableHorizontalScrollView.OnScrollListener listener){
        this.tableData = tableData;
        this.layout= layout;
        this.context= context;
        this.rowNo = rowNo;
        this.listener = listener;
    }

    @Override
    public TableRowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_table_cell, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(rowNo > 0){
            //Normal Cells
            holder.cellLayout.setVisibility(View.GONE);
            holder.headerCell.setText("Header");
        }
        else if(rowNo == 0){
            //Header Cell
            holder.headerLayout.setVisibility(View.GONE);
            holder.cell.setText("Value");
        }
    }

    @Override
    public int getItemCount() {
//        return tableData.size();
        return 8;
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
