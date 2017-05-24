package com.rohansarkar.helpex.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rohansarkar.helpex.R;

import java.util.ArrayList;

/**
 * Created by rohan on 24/5/17.
 */
public class TableRowNoAdapter extends RecyclerView.Adapter<TableRowNoAdapter.ViewHolder>{
    private int size;
    private Context context;

    String LOG_TAG= this.getClass().getSimpleName();

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView rowNo;
        LinearLayout elementLayout;

        public ViewHolder(View v) {
            super(v);
            rowNo = (TextView) v.findViewById(R.id.tvTableRowNo);
            elementLayout = (LinearLayout) v.findViewById(R.id.llTableRowNo);
        }
    }

    public TableRowNoAdapter(int size, Context context){
        this.size = size;
        this.context= context;
    }

    @Override
    public TableRowNoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_table_row_no, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.rowNo.setText((position+1) + ".");
    }

    @Override
    public int getItemCount() {
        return size;
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
