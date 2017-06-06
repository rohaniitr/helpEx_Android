package com.rohansarkar.helpex.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rohansarkar.helpex.R;

import java.util.ArrayList;

/**
 * Created by rohan on 23/5/17.
 */
public class TableCellAdapter extends RecyclerView.Adapter<TableCellAdapter.ViewHolder>{
    private ArrayList<String> columnList;
    private ArrayList<ArrayList<String>> tableData;
    private Context context;

    String LOG_TAG= this.getClass().getSimpleName();

    public class ViewHolder extends RecyclerView.ViewHolder{
        EditText cell;
        RelativeLayout elementLayout;
        CellListener cellListener;

        public ViewHolder(View v, CellListener cellListener) {
            super(v);
            cell = (EditText) v.findViewById(R.id.etTableCell);
            elementLayout = (RelativeLayout) v.findViewById(R.id.rlTableCell);

            this.cellListener = cellListener;
            cell.addTextChangedListener(cellListener);
        }
    }

    public TableCellAdapter(ArrayList<ArrayList<String>> tableData, ArrayList<String> columnList, Context context){
        this.tableData = tableData;
        this.columnList = columnList;
        this.context= context;
    }

    @Override
    public TableCellAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_table_cell, parent, false);
        ViewHolder vh = new ViewHolder(v, new CellListener());
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.cellListener.updatePosition(position);
        holder.cell.setText(tableData.get(position/columnList.size()).get(position%columnList.size()));
    }

    @Override
    public int getItemCount() {
        return (tableData.size()*columnList.size());
    }

    /*
    **Display Assets..
    * */

    private void showToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private class CellListener implements TextWatcher{
        private int position;

        public void updatePosition(int position){
            this.position = position;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            tableData.get(position/columnList.size()).set(position%columnList.size(), charSequence.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }
        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}
