package com.rohansarkar.helpex.Adapters;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rohansarkar.helpex.DatabaseManagers.DatabaseManager;
import com.rohansarkar.helpex.R;

import java.util.ArrayList;

/**
 * Created by rohan on 20/3/17.
 */
public class NewColumnAdapter extends RecyclerView.Adapter<NewColumnAdapter.ViewHolder>{
    private Context context;
    private RelativeLayout layout;
    private RecyclerView recyclerView;

    private ArrayList<String> columnNames;

    String LOG_TAG= "NewColumnAdapter Logs";

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView columnName;
        ImageView remove;
        RelativeLayout elementLayout;

        public ViewHolder(View v) {
            super(v);
            columnName = (TextView) v.findViewById(R.id.tvNewColumn);
            remove = (ImageView) v.findViewById(R.id.ivRemove);
            elementLayout = (RelativeLayout) v.findViewById(R.id.rlNewColumn);
        }
    }

    public NewColumnAdapter(ArrayList<String> columnNames, Context context, RelativeLayout layout,RecyclerView recyclerView){
        this.columnNames= columnNames;
        this.layout= layout;
        this.context= context;
        this.recyclerView= recyclerView;
    }

    @Override
    public NewColumnAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_new_column, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.columnName.setText((position+1) + ". " + columnNames.get(position));
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                columnNames.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return columnNames.size();
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
