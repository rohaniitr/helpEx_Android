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

import com.rohansarkar.helpex.CustomData.DataExperiment;
import com.rohansarkar.helpex.DatabaseManagers.DatabaseManager;
import com.rohansarkar.helpex.R;

import java.util.ArrayList;

import Assets.Util;

/**
 * Created by rohan on 14/3/17.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{
    private Context context;
    private CoordinatorLayout layout;
    private RecyclerView recyclerView;
    private DatabaseManager detailsManager;

    private ArrayList<DataExperiment> experiments;
    private boolean isFavourite;

    String LOG_TAG= "HomeAdapter Logs";

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, subject, dateAndTime;
        ImageView starred;
        RelativeLayout elementLayout;

        public ViewHolder(View v) {
            super(v);
            title= (TextView) v.findViewById(R.id.tvTitle);
            subject= (TextView) v.findViewById(R.id.tvSubject);
            dateAndTime= (TextView) v.findViewById(R.id.tvDateAndTime);
            starred= (ImageView) v.findViewById(R.id.ivNewExperiment);
            elementLayout = (RelativeLayout) v.findViewById(R.id.rlElementHome);
        }
    }

    public HomeAdapter(ArrayList<DataExperiment> experiments, Context context, CoordinatorLayout layout,RecyclerView recyclerView,
                       DatabaseManager detailsManager, boolean isFavourite){
        this.experiments= experiments;
        this.layout= layout;
        this.context= context;
        this.recyclerView= recyclerView;
        this.detailsManager = detailsManager;
        this.isFavourite = isFavourite;
    }

    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_home, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.title.setText((position + 1) + ". " + experiments.get(position).title);
        holder.dateAndTime.setText("Created On: " + experiments.get(position).date + "  " + experiments.get(position).time + " PM");

        if(experiments.get(position).subject.equals(""))
            holder.subject.setVisibility(View.GONE);
        else {
            holder.subject.setText("Subject: " + experiments.get(position).subject);
            holder.subject.setVisibility(View.VISIBLE);
        }

        if(experiments.get(position).starType == Util.StarType.STARRED) {
            holder.starred.setImageResource(R.drawable.star_solid);
            holder.elementLayout.setVisibility(View.VISIBLE);
        }
        else if(experiments.get(position).starType == Util.StarType.NOT_STARRED) {
            holder.starred.setImageResource(R.drawable.star_hollow);
            holder.elementLayout.setVisibility(View.VISIBLE);
        }
        else if(experiments.get(position).starType == Util.StarType.HIDDEN) {
            holder.elementLayout.setVisibility(View.GONE);
        }

        holder.starred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Customize StarType as per user selection.
                if (experiments.get(position).starType == Util.StarType.STARRED) {
                    experiments.get(position).starType = Util.StarType.NOT_STARRED;
                } else if (experiments.get(position).starType == Util.StarType.NOT_STARRED) {
                    experiments.get(position).starType = Util.StarType.STARRED;
                }

                //Update DB
                detailsManager.updateEntry(experiments.get(position));

                //Remove. Since NOT_STARRED should not be in favourite list.
                if(isFavourite){
                    experiments.remove(position);
                }

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return experiments.size();
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
