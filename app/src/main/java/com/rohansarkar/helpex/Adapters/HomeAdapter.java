package com.rohansarkar.helpex.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rohansarkar.helpex.Activities.ExperimentTable;
import com.rohansarkar.helpex.CustomData.DataExperiment;
import com.rohansarkar.helpex.DatabaseManagers.DatabaseEperimentManager;
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
    private DatabaseEperimentManager detailsManager;

    private ArrayList<DataExperiment> experiments;
    private boolean isFavourite;

    String LOG_TAG= "HomeAdapter Logs";

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, subject, dateAndTime;
        ImageView starred, overflowMenu;
        RelativeLayout elementLayout;

        public ViewHolder(View v) {
            super(v);
            title= (TextView) v.findViewById(R.id.tvTitle);
            subject= (TextView) v.findViewById(R.id.tvSubject);
            dateAndTime= (TextView) v.findViewById(R.id.tvDateAndTime);
            starred= (ImageView) v.findViewById(R.id.ivStar);
            overflowMenu = (ImageView) v.findViewById(R.id.ivOverflowMenu);
            elementLayout = (RelativeLayout) v.findViewById(R.id.rlElementHome);
        }
    }

    public HomeAdapter(ArrayList<DataExperiment> experiments, Context context, CoordinatorLayout layout,RecyclerView recyclerView,
                       DatabaseEperimentManager detailsManager, boolean isFavourite){
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.title.setText((position + 1) + ". " + experiments.get(position).title);
        holder.dateAndTime.setText("Created On: " + experiments.get(position).date + "  " + experiments.get(position).time);

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

        holder.overflowMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu overflowPopup = new PopupMenu(context, holder.overflowMenu);
                overflowPopup.getMenuInflater().inflate(R.menu.popup_home_element_experiment, overflowPopup.getMenu());

                overflowPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId() == R.id.popup_delete){
                            //Delete from db.
                            detailsManager.deleteEntry(experiments.get(position).experimentID);
                            showSnackBar("Deleted : " + experiments.get(position).title);

                            //Removed from list.
                            experiments.remove(position);
                            notifyDataSetChanged();
                        }
                        else if(menuItem.getItemId() == R.id.popup_edit){
                            launchNewExperimentDialog(position);
                        }
                        return false;
                    }
                });
                overflowPopup.show();
            }
        });

        View.OnClickListener intentListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ExperimentTable.class);
                i.putExtra("rowId", experiments.get(position).experimentID);
                Log.d(LOG_TAG, experiments.get(position).experimentID + " : experiment ID");
                context.startActivity(i);
            }
        };
        holder.title.setOnClickListener(intentListener);
        holder.subject.setOnClickListener(intentListener);
        holder.dateAndTime.setOnClickListener(intentListener);
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

    private void launchNewExperimentDialog(final int position){
        final Dialog dialog= new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_new_experiment);

        WindowManager.LayoutParams lp= new WindowManager.LayoutParams();
        Window window= dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width= WindowManager.LayoutParams.MATCH_PARENT;
        lp.height= WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        final EditText title = (EditText) dialog.findViewById(R.id.etExperimentTitle);
        final EditText subject = (EditText) dialog.findViewById(R.id.etExperimentSubject);
        Button createNewColumns = (Button) dialog.findViewById(R.id.bNewColumns);
        ImageView back = (ImageView) dialog.findViewById(R.id.ivBack);

        title.setText(experiments.get(position).title);
        subject.setText(experiments.get(position).subject);

        title.requestFocus();
        showKeyboard();

        //Button Listener here.
        View.OnClickListener createNewExperimentListener= new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(title.getText().toString().trim().length()>0){

                    experiments.get(position).title = title.getText().toString();
                    experiments.get(position).subject = subject.getText().toString();

                    //Update experiment to db.
                    detailsManager.updateEntry(experiments.get(position));
                    //Update changes in UI.
                    notifyDataSetChanged();
                    showSnackBar("Updated : " + experiments.get(position).title);
                    dialog.dismiss();
                }
            }
        };
        View.OnClickListener backListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        };

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                hideKeyboard();
            }
        });

        //Buttons here.
        createNewColumns.setOnClickListener(createNewExperimentListener);
        back.setOnClickListener(backListener);
        dialog.show();
    }

    //APIs for Soft Keyboard.
    private void showKeyboard(){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);
    }
}
