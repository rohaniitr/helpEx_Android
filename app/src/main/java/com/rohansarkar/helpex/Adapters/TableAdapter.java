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
    private LayoutInflater inflater;
    private TableHorizontalScrollView.OnScrollListener listener;
    private TableHorizontalScrollView headerScrollView;

    private ArrayList<ArrayList<String>> tableData;
    private ArrayList<String> columnList;

    String LOG_TAG= "TableAdapter Logs";

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView rowNo;
        LinearLayout rowLayout;
        TableHorizontalScrollView scrollView;
        ArrayList<EditText> rowList;

        public ViewHolder(View v) {
            super(v);
            rowNo = (TextView) v.findViewById(R.id.tvTableRowNo);
            scrollView = (TableHorizontalScrollView) v.findViewById(R.id.hsvTableRow);
            rowLayout = (LinearLayout) v.findViewById(R.id.llTableRow);

            rowList = new ArrayList<>();
        }
    }

    public TableAdapter(ArrayList<ArrayList<String>> tableData, ArrayList<String> columnList, Context context,
                        CoordinatorLayout layout, TableHorizontalScrollView.OnScrollListener listener,
                        TableHorizontalScrollView headerScrollView){
        this.tableData = tableData;
        this.columnList = columnList;
        this.layout= layout;
        this.context= context;
        this.listener = listener;
        this.headerScrollView = headerScrollView;

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public TableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_table_row, parent, false);
        ViewHolder vh = new ViewHolder(v);

        if (vh.rowLayout.getChildCount() >= columnList.size())
            return vh;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(320, ViewGroup.LayoutParams.MATCH_PARENT);
        for(int i=0; i<columnList.size(); i++){
            View cellView = inflater.inflate(R.layout.element_table_cell, null, false);
            cellView.setLayoutParams(params);
            vh.rowLayout.addView(cellView);
            vh.rowList.add((EditText) cellView.findViewById(R.id.etTableCell));
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.rowNo.setText((holder.getAdapterPosition()+1) + ".");
        holder.scrollView.setOnScrollListener(listener);

        Random r = new Random();
        for(int i=0; i<columnList.size(); i++) {
            holder.rowList.get(i).setText(tableData.get(position).get(i));
        }

        holder.scrollView.setScrollX(headerScrollView.getScrollX());
//        holder.scrollView.setTranslationX(headerScrollView.getTranslationX());

        Log.d(LOG_TAG, "Pos: " + position + ", TranslationX: " + headerScrollView.getTranslationX() + ", ScrollX: " + headerScrollView.getScrollX());
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
