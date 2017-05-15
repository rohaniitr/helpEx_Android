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
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import com.rohansarkar.helpex.Adapters.NewColumnAdapter;
import com.rohansarkar.helpex.CustomData.DataExperiment;
import com.rohansarkar.helpex.DatabaseManagers.DatabaseManager;
import com.rohansarkar.helpex.R;

import java.util.ArrayList;

/**
 * Created by rohan on 11/5/17.
 */
public class ExperimentTable extends AppCompatActivity implements View.OnClickListener{

    private static final String LOG_TAG = "ExperimentTable";
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    CoordinatorLayout layout;
    Toolbar toolbar;
    ImageView overflowMenu;
    TextView toolbarTitle;
    TextView emptyLayout;

    DatabaseManager detailsManager;
    DataExperiment experimentData;
    ArrayList<String> columnList;
    long rowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_table);

        init();
        getData();
        setToolbar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detailsManager.close();
    }

    private void getData(){
        rowId = getIntent().getLongExtra("rowId", -1);
        experimentData = detailsManager.getExperimentDetails(rowId);
        columnList = getColumnList(experimentData.columnNames);
//        experiments = detailsManager.getExperimentDetails();
//        setRecyclerView(experiments);
    }

    /*
    **Initializations.
    * */

    private void init(){
        recyclerView = (RecyclerView) findViewById(R.id.rvExperimentTable);
        layout= (CoordinatorLayout) findViewById(R.id.clExperimentTable);
        emptyLayout = (TextView) findViewById(R.id.tvEmptyTable);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        columnList = new ArrayList<>();

        emptyLayout.setOnClickListener(this);

        detailsManager = new DatabaseManager(this);
        detailsManager.open();
    }

    private void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.tbExperimentTable);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(Color.parseColor("#dddddd"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        overflowMenu = (ImageView) findViewById(R.id.ivOverflowMenu);
        overflowMenu.setOnClickListener(this);

        toolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);
        toolbarTitle.setText(experimentData.title);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.ivOverflowMenu:
                //Inflate and Show Toolbar popup.
                PopupMenu toolbarMenu = new PopupMenu(this, overflowMenu);
                toolbarMenu.getMenuInflater().inflate(R.menu.popup_experiment_table, toolbarMenu.getMenu());
                toolbarMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId() == R.id.popup_add_columns){
                            launchNewColumnDialog();
                        }
                        else if(menuItem.getItemId() == R.id.popup_plot_graph){
                            showToast("Plot Graph");
                        }
                        else if(menuItem.getItemId() == R.id.popup_export_details){
                            showToast("Export Details");
                        }
                        return false;
                    }
                });
                toolbarMenu.show();
                break;

            case R.id.tvEmptyTable:
                launchNewColumnDialog();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setRecyclerView(Dialog dialog, RecyclerView dialogRecyclelrView, RecyclerView.Adapter dialogAdapter,
                                 ArrayList<String> columnNames, RelativeLayout dialogLayout){
        dialogAdapter = new NewColumnAdapter(columnNames, dialog.getContext(), dialogLayout, recyclerView);
        dialogRecyclelrView.setAdapter(dialogAdapter);
    }

    private void launchNewColumnDialog(){
        final Dialog dialog= new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_new_columns);

        WindowManager.LayoutParams lp= new WindowManager.LayoutParams();
        Window window= dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width= WindowManager.LayoutParams.MATCH_PARENT;
        lp.height= WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        final ArrayList<String> columnNames = new ArrayList<>();
        for(int i=0; i<columnList.size(); i++){
            columnNames.add(columnList.get(i));
        }

        final EditText columnName = (EditText) dialog.findViewById(R.id.etColumnName);
        final RelativeLayout dialogLayout= (RelativeLayout) dialog.findViewById(R.id.rlNewColumn);
        Button addColumn = (Button) dialog.findViewById(R.id.bAddColumn);
        Button createNewExperiment= (Button) dialog.findViewById(R.id.bNewExperiment);
        ImageView back = (ImageView) dialog.findViewById(R.id.ivBack);

        columnName.requestFocus();
        showKeyboard();

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
                    dialogRecylerView.scrollToPosition(columnNames.size()-1);
                    columnName.setText("");
                }
            }
        };

        View.OnClickListener createNewExperimentListener= new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateColumns(columnNames);
                dialog.dismiss();
            }
        };

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                hideKeyboard();
            }
        });

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

    //Database APIs
    private void updateColumns(ArrayList<String> columnNames){
        experimentData.columnNames = getColumnString(columnNames);
        columnList = columnNames;
        Log.d(LOG_TAG, "columnNames: " + experimentData.columnNames + "columnNames.size : " + columnNames.size());
        detailsManager.updateEntry(experimentData);
    }

    //String Slicing for Columns
    private ArrayList<String> getColumnList(String columnNames){
        ArrayList<String> columns = new ArrayList<>();
        String[] columnArray = columnNames.split("~");

        for(int i=0; i<columnArray.length; i++){
            columns.add(columnArray[i]);
        }
        return columns;
    }
    private String getColumnString(ArrayList<String> columnNames){
        String columnString = "";

        if(columnNames.size()==1){
            columnString = columnNames.get(0);
        }
        else if(columnNames.size() > 1){
            columnString = columnNames.get(0);
            for(int i=1; i<columnNames.size(); i++){
                columnString += "~" + columnNames.get(i);
            }
        }

        return columnString;
    }
}
