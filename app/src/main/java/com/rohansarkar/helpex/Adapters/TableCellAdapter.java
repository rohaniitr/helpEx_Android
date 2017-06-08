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

import com.rohansarkar.helpex.CustomData.DataExperiment;
import com.rohansarkar.helpex.CustomData.DataRecord;
import com.rohansarkar.helpex.DatabaseManagers.DatabaseRecordsManager;
import com.rohansarkar.helpex.R;

import java.util.ArrayList;

import Assets.Util;

/**
 * Created by rohan on 23/5/17.
 */
public class TableCellAdapter extends RecyclerView.Adapter<TableCellAdapter.ViewHolder>{
    private ArrayList<String> columnList;
    DataExperiment experimentData;
    ArrayList<DataRecord> experimentRecords;
    private ArrayList<ArrayList<String>> tableData;
    DatabaseRecordsManager recordsManager;
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

    public TableCellAdapter(DataExperiment experimentData, ArrayList<DataRecord> experimentRecords,
                            ArrayList<ArrayList<String>> tableData, ArrayList<String> columnList,
                            DatabaseRecordsManager recordsManager, Context context){
        this.experimentData = experimentData;
        this.experimentRecords = experimentRecords;
        this.tableData = tableData;
        this.columnList = columnList;
        this.recordsManager = recordsManager;
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

    private void saveData(DataRecord dataRecord){
        if (Util.isEmptyRow(dataRecord.record)){
            if(dataRecord.recordId >= 0){
                //Record exist in DB. Remove it.
                recordsManager.deleteRecord(dataRecord.recordId);
                dataRecord.recordId = -1;
            }
        }
        else {
            if(dataRecord.recordId < 0){
                //Default Value. Create new Record.
                dataRecord.recordId = recordsManager.createRecord(dataRecord);
            }
            else {
                //Record already exists. Update it.
                recordsManager.updateRecord(dataRecord);
            }
        }
    }

    private class CellListener implements TextWatcher{
        private int position;

        public void updatePosition(int position){
            this.position = position;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            int rowPos = position/columnList.size();
            tableData.get(rowPos).set(position%columnList.size(), charSequence.toString());
            experimentRecords.get(rowPos).record = Util.getString(tableData.get(rowPos), "~");

            saveData(experimentRecords.get(rowPos));
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }
        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}
