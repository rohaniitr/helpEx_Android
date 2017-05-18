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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    private TableHorizontalScrollView.OnScrollListener listener;

    private ArrayList<String> tableData;
    ArrayList<ArrayList<View>> cells;

    String LOG_TAG= "TableAdapter Logs";

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView rowNo;
        LinearLayout rowLayout;
        TableHorizontalScrollView scrollView;

        public ViewHolder(View v) {
            super(v);
            rowNo = (TextView) v.findViewById(R.id.tvTableRowNo);
            scrollView = (TableHorizontalScrollView) v.findViewById(R.id.hsvTableRow);
            rowLayout = (LinearLayout) v.findViewById(R.id.llTableRow);
        }
    }

    public TableAdapter(ArrayList<String> tableData, Context context, CoordinatorLayout layout,
                        TableHorizontalScrollView.OnScrollListener listener){
        this.tableData = tableData;
        this.layout= layout;
        this.context= context;
        this.listener = listener;

        if(cells == null) {
            cells = new ArrayList<>();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(320,200);

            for (int i = 0; i < tableData.size(); i++) {
                ArrayList<View> rows = new ArrayList<>();

                for (int j = 0; j < 6; j++) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View cellView = inflater.inflate(R.layout.element_table_cell, null, false);
                    cellView.setLayoutParams(params);
                    rows.add(cellView);
                }
                cells.add(rows);
            }
        }
    }

    @Override
    public TableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_table_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        holder.rowNo.setText(tableData.get(holder.getPosition()));
        holder.rowNo.setHeight(160);
        holder.scrollView.setOnScrollListener(listener);

        if(holder.rowLayout != null)
            holder.rowLayout.removeAllViews();

        Log.d(LOG_TAG, "Position: " + holder.getPosition() + ", " + holder.rowLayout.getChildCount());
        for(int i=0; i<6; i++)
            holder.rowLayout.addView(cells.get(holder.getPosition()).get(i));
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
