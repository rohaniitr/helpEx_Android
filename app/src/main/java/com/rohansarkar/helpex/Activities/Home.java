package com.rohansarkar.helpex.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rohansarkar.helpex.Adapters.HomeAdapter;
import com.rohansarkar.helpex.Adapters.NewColumnAdapter;
import com.rohansarkar.helpex.CustomData.DataExperiment;
import com.rohansarkar.helpex.CustomData.DataRecord;
import com.rohansarkar.helpex.DatabaseManagers.DatabaseExperimentManager;
import com.rohansarkar.helpex.DatabaseManagers.DatabaseRecordsManager;
import com.rohansarkar.helpex.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import Assets.Util;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;


public class Home extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private CoordinatorLayout layout;
    private Toolbar toolbar;
    private ImageView overflowMenu;
    private ImageView favourites;

    private ArrayList<DataExperiment> experiments;
    private ArrayList<DataExperiment> favouriteExperiments;
    private DatabaseExperimentManager detailsManager;
    private DatabaseRecordsManager recordsManager;
    boolean isFavourite;

    private String LOG_TAG = getClass().getSimpleName();
    private int IMPORT_FROM_CSV = 343;
    private int IMPORT_FROM_EXCEL = 534;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        setRecyclerView(experiments);
        setToolbar();
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detailsManager.close();
        recordsManager.close();
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
        isFavourite = false;

        detailsManager = new DatabaseExperimentManager(this);
        recordsManager = new DatabaseRecordsManager(this);
        detailsManager.open();
        recordsManager.open();
    }

    private void setRecyclerView(ArrayList<DataExperiment> dataExperiments){
        adapter = new HomeAdapter(dataExperiments, this, layout, recyclerView, detailsManager, recordsManager, isFavourite);
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
                toolbarMenu.getMenuInflater().inflate(R.menu.popup_home, toolbarMenu.getMenu());
                toolbarMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.popup_add_experiment){
                            launchNewExperimentDialog();
                        }
                        else if (menuItem.getItemId() == R.id.popup_import_data){
                            if (Util.isExplorerPresent(overflowMenu.getContext())){
                                showImportOptions();
                            }
                            else {
                                showToast("No file explorer detected. \nPlease install a file explorer to import data.");
                            }
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

        //Add listeners here.
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
        final Button creatNewExperiment = (Button) dialog.findViewById(R.id.bNewExperiment);
        final Button createNewExperiment= (Button) dialog.findViewById(R.id.bNewExperiment);
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
                    createNewExperiment(title, subject, null);
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

    //Get file for import.
    private void getFile(int importOptions){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");
        startActivityForResult(intent, importOptions);
    }

    //Import Data from Excel File.
    private void importDataFromExcel(Uri xlsPath){
        if(!xlsPath.getPath().contains(".xls")){
            //Wrong File type.
            showToast("Error in reading file. \nPlease make sure you are importing .xls file only.");
        }

        try{
            String root = Environment.getExternalStorageDirectory().toString();
            String fileName = xlsPath.getLastPathSegment().split(".xls")[0];

            ArrayList<String> columnList = new ArrayList<>();
            ArrayList<ArrayList<String>> tableData = new ArrayList<>();
            File inputFile = new File(xlsPath.getPath());
            Workbook workbook = Workbook.getWorkbook(inputFile);
            showToast("Importing data from " + xlsPath.getLastPathSegment());

            //Should make it dynamic.
            Sheet sheet = workbook.getSheet(0);

            for (int j=0; j<sheet.getColumns(); j++){
                columnList.add(sheet.getCell(j,0).getContents());
            }

            for (int i=0;  i<sheet.getRows(); i++){
                ArrayList<String> rowData = new ArrayList<>();
                for (int j=0; j<sheet.getColumns(); j++){
                    Cell cell = sheet.getCell(j,i);
                    rowData.add(cell.getContents());
                }

                //Verify Column Structure in .xls file
                if(columnList.size() >= rowData.size()){
                    tableData.add(rowData);
                }
                else {
                    Log.d(LOG_TAG, "Row No : " + i + ",  columnList.size : " + columnList.size() +
                            ", rowData.size : " + rowData.size());
                    showToast("Data in " + xlsPath.getLastPathSegment() + " is not properly organised.");
                    return;
                }
            }

            //Create Experiment.
            createNewExperiment(fileName, "", Util.getString(columnList, "~"));
            saveTableData(tableData, experiments.get(experiments.size()-1));
            showToast("Successfully imported " + xlsPath.getLastPathSegment());
        }
        catch (Exception e){
            e.printStackTrace();
            showToast("Error in reading file. \nPlease make sure you are importing .xls file only.");
        }
    }

    //Import Data from CSV File.
    private void importDataFromCSV(Uri csvPath){
        if(!csvPath.getPath().contains(".csv")){
            //Wrong File type.
            showToast("Error in reading file. \nPlease make sure you are importing .csv file only.");
        }

        InputStream inputStream = null;
        try{
            String root = Environment.getExternalStorageDirectory().toString();
            String fileName = csvPath.getLastPathSegment().split(".csv")[0];

            ArrayList<String> columnList = new ArrayList<>();
            ArrayList<ArrayList<String>> tableData = new ArrayList<>();
            inputStream = new FileInputStream(csvPath.getPath());
            BufferedReader csvReader = new BufferedReader(new InputStreamReader(inputStream));
            showToast("Importing data from " + csvPath.getLastPathSegment());

            String csvLine;
            //Get column list
            if((csvLine = csvReader.readLine()) != null){
                columnList = Util.splitString(csvLine, ",");
            }

            //get Table Data
            while ((csvLine = csvReader.readLine()) != null){
                tableData.add(Util.splitString(csvLine, ","));

                //Verify column structure in .csv file
                if(columnList.size() != tableData.get(tableData.size()-1).size()){
                    showToast("Data in " + csvPath.getLastPathSegment() + " is not properly organised.");
                    return;
                }
            }

            //Create Experiment.
            createNewExperiment(fileName, "", Util.getString(columnList, "~"));
            saveTableData(tableData, experiments.get(experiments.size() - 1));
            inputStream.close();
            showToast("Successfully imported " + csvPath.getLastPathSegment());
        }
        catch (Exception e){
            e.printStackTrace();
            showToast("Error in reading file. \nPlease make sure you are importing .csv file only.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == IMPORT_FROM_EXCEL){
                importDataFromExcel(data.getData());
            }
            else if (requestCode == IMPORT_FROM_CSV){
                importDataFromCSV(data.getData());
            }
        }
    }

    private void createNewExperiment(String title, String subject, String columnString){
        DataExperiment data = new DataExperiment();
        data.title = title;
        data.starType = Util.StarType.NOT_STARRED;
        data.date = "12/03/2017";
        data.time = "12:07 PM";
        data.subject = subject;

        if(columnString != null)
            data.columnNames = columnString;

        data.experimentID = detailsManager.createEntry(data);

        experiments.add(data);
        adapter.notifyItemInserted(experiments.size()-1);
        recyclerView.smoothScrollToPosition(experiments.size()-1);
    }

    private void showImportOptions(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.icon_export_grey);
        builder.setTitle("Import from : ");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
        adapter.add("Excel File (.xls)");
        adapter.add("CSV File (.csv)");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        getFile(IMPORT_FROM_EXCEL);
                        break;
                    case 1:
                        getFile(IMPORT_FROM_CSV);
                        break;
                }
            }
        });
        builder.show();
    }

    private void saveTableData(ArrayList<ArrayList<String>> tableData, DataExperiment experimentDetails){
        for(int i=0;  i<tableData.size(); i++){
            recordsManager.createRecord(new DataRecord(experimentDetails.experimentID,
                    Util.getString(tableData.get(i), "~")));
        }
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
