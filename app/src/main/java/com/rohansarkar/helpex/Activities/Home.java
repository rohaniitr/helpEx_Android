package com.rohansarkar.helpex.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rohansarkar.helpex.Adapters.HomeAdapter;
import com.rohansarkar.helpex.Adapters.NewColumnAdapter;
import com.rohansarkar.helpex.CustomData.DataExperiment;
import com.rohansarkar.helpex.DatabaseManagers.DatabaseEperimentManager;
import com.rohansarkar.helpex.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import Assets.Util;


public class Home extends AppCompatActivity implements View.OnClickListener{

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    CoordinatorLayout layout;
    Toolbar toolbar;
    ImageView overflowMenu;
    ImageView favourites;

    ArrayList<DataExperiment> experiments;
    ArrayList<DataExperiment> favouriteExperiments;
    DatabaseEperimentManager detailsManager;
    boolean isFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        setRecyclerView(experiments);
        setToolbar();
    }

    @Override
    protected void onPause() {
        super.onPause();
        detailsManager.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        detailsManager.open();
        getData();
    }

    private void getData(){
        experiments = detailsManager.getExperimentDetails();
        setRecyclerView(experiments);
    }

    /*
    **Initializations.
    * */

    private void init(){
        recyclerView = (RecyclerView) findViewById(R.id.rvHome);
        layout= (CoordinatorLayout) findViewById(R.id.clHome);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        experiments= new ArrayList<>();
        favouriteExperiments = new ArrayList<>();
        detailsManager = new DatabaseEperimentManager(this);
        isFavourite = false;
    }

    private void setRecyclerView(ArrayList<DataExperiment> dataExperiments){
        adapter = new HomeAdapter(dataExperiments, this, layout, recyclerView, detailsManager, isFavourite);
        recyclerView.setAdapter(adapter);
    }

    private void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.tbHome);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(Color.parseColor("#dddddd"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        overflowMenu = (ImageView) findViewById(R.id.ivOverflowMenu);
        favourites = (ImageView) findViewById(R.id.ivFavourites);
        overflowMenu.setOnClickListener(this);
        favourites.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.ivOverflowMenu:
                //Inflate and Show Toolbar popup.
                PopupMenu toolbarMenu = new PopupMenu(this, overflowMenu);
                toolbarMenu.getMenuInflater().inflate(R.menu.popup_home_toolbar, toolbarMenu.getMenu());
                toolbarMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId() == R.id.popup_add_experiment){
                            launchNewExperimentDialog();
                        }
                        return false;
                    }
                });
                toolbarMenu.show();
                break;

            case R.id.ivFavourites:
                isFavourite = !isFavourite;

                if(isFavourite){
                    //Recreate favouriteExperiment with filter.
                    favouriteExperiments.clear();
                    for(int i=0; i<experiments.size(); i++){
                        if(experiments.get(i).starType == Util.StarType.STARRED)
                            favouriteExperiments.add(experiments.get(i));
                    }

                    setRecyclerView(favouriteExperiments);
                }
                else{

                    experiments= detailsManager.getExperimentDetails();
                    setRecyclerView(experiments);
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchNewExperimentDialog(){
        final Dialog dialog= new Dialog(this);
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

        title.requestFocus();
        showKeyboard();

        //Button Listener here.
        View.OnClickListener createNewExperimentListener= new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(title.getText().toString().trim().length()>0){

                    DataExperiment data = new DataExperiment();
                    data.title = title.getText().toString();
                    data.starType = Util.StarType.NOT_STARRED;
                    data.subject = subject.getText().toString();
                    data.columnNames = "";

                    //Get current Date & Time.
                    DateFormat dfDate = new SimpleDateFormat("yyyy/mm/dd");
                    DateFormat dfTime = new SimpleDateFormat("hh:mm aaa");
                    data.date=dfDate.format(Calendar.getInstance().getTime());
                    data.time = dfTime.format(Calendar.getInstance().getTime());

                    //Add new experiment to db.
                    data.experimentID = detailsManager.createEntry(data);

                    //Add new experiment to the list.
                    experiments.add(data);
                    setRecyclerView(experiments);
                    //Scroll to the newly added experiment.
                    recyclerView.scrollToPosition(experiments.size()-1);

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

    private void setRecyclerView(Dialog dialog, RecyclerView dialogRecyclelrView, RecyclerView.Adapter dialogAdapter,
                                 ArrayList<String> columnNames, RelativeLayout dialogLayout){
        dialogAdapter = new NewColumnAdapter(columnNames, dialog.getContext(), dialogLayout, recyclerView);
        dialogRecyclelrView.setAdapter(dialogAdapter);
    }

    private void launchNewColumnDialog(final String title, final String subject){
        final Dialog dialog= new Dialog(this);
//        dialog.getWindow().getAttributes().windowAnimations= R.style.CustomLeftDialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_new_columns);

        WindowManager.LayoutParams lp= new WindowManager.LayoutParams();
        Window window= dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width= WindowManager.LayoutParams.MATCH_PARENT;
        lp.height= WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        final ArrayList<String> columnNames = new ArrayList<>();
        columnNames.add("x");

        final EditText columnName = (EditText) dialog.findViewById(R.id.etColumnName);
        final RelativeLayout dialogLayout= (RelativeLayout) dialog.findViewById(R.id.rlNewColumn);
        Button addColumn = (Button) dialog.findViewById(R.id.bAddColumn);
        Button createNewExperiment= (Button) dialog.findViewById(R.id.bNewExperiment);
        ImageView back = (ImageView) dialog.findViewById(R.id.ivBack);

        final RecyclerView dialogRecylerView= (RecyclerView) dialog.findViewById(R.id.rvNewExperiment);
        LinearLayoutManager dialogLayoutManager = new LinearLayoutManager(dialog.getContext());
        dialogRecylerView.setLayoutManager(dialogLayoutManager);
        final RecyclerView.Adapter detailsAdapter = null;
        setRecyclerView(dialog, dialogRecylerView, detailsAdapter, columnNames, dialogLayout);


        //Button Listener here.
        View.OnClickListener addColumnListener= new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(columnName.getText().toString().trim().length()>0){
                    columnNames.add(columnName.getText().toString());
                    setRecyclerView(dialog, dialogRecylerView, detailsAdapter, columnNames, dialogLayout);
                }
            }
        };

        View.OnClickListener createNewExperimentListener= new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(title.trim().length()>0){
                    DataExperiment data = new DataExperiment();
                    data.title = title;
                    data.starType = Util.StarType.NOT_STARRED;
                    data.date = "12/03/2017";
                    data.time = "12:07 PM";

                    if(subject.trim().length()>0)
                        data.subject = subject;
                    else
                        data.subject = "";

                    data.experimentID = detailsManager.createEntry(data);

                    experiments.add(data);
                    setRecyclerView(experiments);

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

        //Buttons here.
        addColumn.setOnClickListener(addColumnListener);
        createNewExperiment.setOnClickListener(createNewExperimentListener);
        back.setOnClickListener(backListener);
        dialog.show();
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //APIs for Soft Keyboard.
    private void showKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);
    }
}
