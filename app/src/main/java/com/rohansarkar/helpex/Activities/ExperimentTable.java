package com.rohansarkar.helpex.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rohansarkar.helpex.Adapters.NewColumnAdapter;
import com.rohansarkar.helpex.Adapters.TableAdapter;
import com.rohansarkar.helpex.Adapters.TableHeaderAdapter;
import com.rohansarkar.helpex.Adapters.TableRowNoAdapter;
import com.rohansarkar.helpex.CustomData.DataExperiment;
import com.rohansarkar.helpex.DatabaseManagers.DatabaseManager;
import com.rohansarkar.helpex.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import Assets.SmartRecyclerView;
import Assets.SmartScrollView;

/**
 * Created by rohan on 11/5/17.
 */
public class ExperimentTable extends AppCompatActivity implements View.OnClickListener{

    private static final String LOG_TAG = "ExperimentTable";
    SmartRecyclerView recyclerView, rowRecyclerView;
    RecyclerView headerRecyclerView;
    RecyclerView.Adapter adapter, headerAdapter, rowAdapter;
    LinearLayoutManager layoutManager, headerLayoutManager, rowLayoutManager;
    RecyclerView.OnScrollListener tableScrollListener, rowNoScrollListener;
    CoordinatorLayout layout;
    Toolbar toolbar;
    ImageView overflowMenu;
    TextView toolbarTitle;
    RelativeLayout emptyLayout;   //If NumberOfColumns <= 0
    RelativeLayout tableLayout;   //If NumberOfColumns > 0
    ViewGroup headerViewGroup;

    DatabaseManager detailsManager;
    DataExperiment experimentData;              //Data about Experiment selected.
    ArrayList<String> columnList;               //Contains Column List.
    ArrayList<String> prevColumnList;           //Contains Just Previous Column List. Required while shifting Columns.
    ArrayList<ArrayList<String>> tableData;     //Contains 2D Table Data.
    long rowId;                                 //Row ID of the Selected Experiment.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_table);

        init();
        getData();
        setLayoutVisibility();
        setToolbar();
        setHeaderRecyclerView(columnList);
        setRecyclerView(tableData, columnList);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        detailsManager.close();
    }

    private void getData(){
        //Get Row ID of Experiment.
        rowId = getIntent().getLongExtra("rowId", -1);
        //Get Details of the Experiment.
        experimentData = detailsManager.getExperimentDetails(rowId);
        columnList = getColumnList(experimentData.columnNames);
        Log.d(LOG_TAG, "ColumnList Size : " + columnList.size());

        //Get Table Data for  the Experiment.
        for(int i=0; i<30; i++) {
            ArrayList<String> rowData = new ArrayList<>();
            for(int j=0; j<10; j++)
                rowData.add(i + j + "");
            tableData.add(rowData);
        }
//        experiments = detailsManager.getExperimentDetails();
//        setRecyclerView(experiments);
    }

    private void setRecyclerView(ArrayList<ArrayList<String>> tableData, ArrayList<String> columnList){
        adapter = new TableAdapter(tableData, columnList, this, layout);
        recyclerView.computedWidth = getRecyclerWidth();
        recyclerView.setAdapter(adapter);

        rowAdapter = new TableRowNoAdapter(tableData.size(), this);
        rowRecyclerView.computedWidth = (int) getResources().getDimension(R.dimen.custom_table_serial_number_width);
        rowRecyclerView.setAdapter(rowAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            private int mLastY;

            @Override
            public boolean onInterceptTouchEvent(@NonNull final RecyclerView rv, @NonNull final
            MotionEvent e) {
                final Boolean ret = rv.getScrollState() != RecyclerView.SCROLL_STATE_IDLE;
                if (!ret) {
                    onTouchEvent(rv, e);
                }
                return Boolean.FALSE;
            }

            @Override
            public void onTouchEvent(@NonNull final RecyclerView rv, @NonNull final MotionEvent e) {

                final int action;
                if ((action = e.getAction()) == MotionEvent.ACTION_DOWN && rowRecyclerView
                        .getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                    mLastY = rv.getScrollY();
                    rv.addOnScrollListener(rowNoScrollListener);
                }
                else {
                    if (action == MotionEvent.ACTION_UP && rv.getScrollY() == mLastY) {
                        rv.removeOnScrollListener(rowNoScrollListener);
                    }
                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(final boolean disallowIntercept) {
            }
        });

        rowRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            private int mLastY;

            @Override
            public boolean onInterceptTouchEvent(@NonNull final RecyclerView rv, @NonNull final
            MotionEvent e) {

                final Boolean ret = rv.getScrollState() != RecyclerView.SCROLL_STATE_IDLE;
                if (!ret) {
                    onTouchEvent(rv, e);
                }
                return Boolean.FALSE;
            }

            @Override
            public void onTouchEvent(@NonNull final RecyclerView rv, @NonNull final MotionEvent e) {

                final int action;
                if ((action = e.getAction()) == MotionEvent.ACTION_DOWN && recyclerView
                        .getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                    mLastY = rv.getScrollY();
                    rv.addOnScrollListener(tableScrollListener);
                }
                else {
                    if (action == MotionEvent.ACTION_UP && rv.getScrollY() == mLastY) {
                        rv.removeOnScrollListener(tableScrollListener);
                    }
                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(final boolean disallowIntercept) {
            }
        });
    }

    private void setHeaderRecyclerView(ArrayList<String> columnList){
        headerAdapter = new TableHeaderAdapter(columnList, this);
        headerRecyclerView.setAdapter(headerAdapter);
    }

    /*
    **Initializations.
    * */

    private void init(){
        recyclerView = (SmartRecyclerView) findViewById(R.id.rvTable);
        rowRecyclerView = (SmartRecyclerView) findViewById(R.id.rvTableNo);
        layout= (CoordinatorLayout) findViewById(R.id.clExperimentTable);
        emptyLayout = (RelativeLayout) findViewById(R.id.rlEmptyLayout);
        tableLayout = (RelativeLayout) findViewById(R.id.rlTableLayout);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        rowLayoutManager = new LinearLayoutManager(this);
        rowLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rowRecyclerView.setLayoutManager(rowLayoutManager);
        rowRecyclerView.setHasFixedSize(true);

        columnList = new ArrayList<>();
        prevColumnList = new ArrayList<>();
        tableData = new ArrayList<>();

        emptyLayout.setOnClickListener(this);

        detailsManager = new DatabaseManager(this);
        detailsManager.open();

        //Table Header
        headerViewGroup = (ViewGroup) findViewById(R.id.vTableHeader);
        headerRecyclerView = (RecyclerView) headerViewGroup.findViewById(R.id.rvTableRow);
        headerLayoutManager = new LinearLayoutManager(this);
        headerLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        headerRecyclerView.setLayoutManager(headerLayoutManager);
        headerRecyclerView.setHasFixedSize(true);

        //Table Scroll Listener.
        tableScrollListener = new SmartScrollView() {
            @Override
            public void onScrolled(@NonNull final RecyclerView recyclerView, final int dx, final int dy) {
                super.onScrolled(recyclerView, dx, dy);
                recyclerView.scrollBy(dx, dy);
            }
        };
        rowNoScrollListener = new SmartScrollView() {
            @Override
            public void onScrolled(@NonNull final RecyclerView recyclerView, final int dx, final int dy) {
                super.onScrolled(recyclerView, dx, dy);
                rowRecyclerView.scrollBy(dx, dy);
            }
        };
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

            case R.id.rlEmptyLayout:
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
        prevColumnList = columnList;
        columnList = columnNames;
        Log.d(LOG_TAG, "columnNames: " + experimentData.columnNames + "columnNames.size : " + columnNames.size());
        detailsManager.updateEntry(experimentData);
        //Show Table if Columns present in  table.
        setLayoutVisibility();
        //Update the table.
        refreshTable();
    }

    //Updates the Table.
    private void refreshTable(){
        ArrayList<ArrayList<String>> tempTableData = new ArrayList<>(tableData.size());

        for(int i=0; i<columnList.size(); i++){
            if(tempTableData.isEmpty()) {
                //First Element of Each Row.
                if (prevColumnList.contains(columnList.get(i))) {
                    //Move the Column Data.
                    //Get position of Column in Prev Table Arrangement.
                    int columnPos = prevColumnList.indexOf(columnList.get(i));
                    ArrayList<String> data = null;
                    //Copy Prev Column Data to new Position.
                    for (int j = 0; j < tableData.size(); j++) {
                        data = new ArrayList<>();
                        data.add(tableData.get(j).get(columnPos));
                        tempTableData.add(data);
                    }
                } else {
                    //Initialise Empty Values.
                    ArrayList<String> data = null;
                    for (int j = 0; j < tableData.size(); j++) {
                        data = new ArrayList<>();
                        data.add("");
                        tempTableData.add(data);
                    }
                }
            }
            else {
                //For  Rest of elements.
                if (prevColumnList.contains(columnList.get(i))) {
                    int columnPos = prevColumnList.indexOf(columnList.get(i));
                    //Copy Prev Values to new Position.
                    for (int j = 0; j < tableData.size(); j++)
                        tempTableData.get(j).add(tableData.get(j).get(columnPos));
                }
                else {
                    //Assign Empty Values
                    for (int j = 0; j < tableData.size(); j++)
                        tempTableData.get(j).add("");
                }
            }
        }

        //Free Memory & Update UI.
        tableData.clear();
        tableData = tempTableData;
        setRecyclerView(tableData, columnList);
        setHeaderRecyclerView(columnList);

        //Update Database for Table  Data.
    }

    //String Slicing for Columns
    private ArrayList<String> getColumnList(String columnNames){
        ArrayList<String> columns = new ArrayList<>();

        //Empty Columns.
        if(columnNames.trim().length() <=0)
            return columns;

        //Slicing ColumnList String.
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

    //If Table has Columns -> Show Table.
    //Else show  options for adding Columns.
    private void setLayoutVisibility(){
        if(columnList.size()>0){
            tableLayout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }
        else{
            tableLayout.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        }
    }

    private int getRecyclerWidth(){
        float sp = (getResources().getDimension(R.dimen.custom_table_cell_width) * columnList.size());
        return (int)sp;
    }

    //EVENT Class.
    public static class Event {

        private final int x;
        private final int y;
        private final HorizontalScrollView view;

        public Event(HorizontalScrollView view, int x, int y) {
            this.x = x;
            this.y = y;
            this.view = view;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public HorizontalScrollView getView() {
            return view;
        }
    }
}
