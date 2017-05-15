package com.rohansarkar.helpex.DatabaseManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rohansarkar.helpex.CustomData.DataExperiment;
import com.rohansarkar.helpex.CustomData.DataRecord;

import java.util.ArrayList;

import Assets.Util;

/**
 * Created by rohan on 16/3/17.
 */
public class DatabaseManager {

    String LOG_TAG= "DatabaseManager Logs";
    public static final String KEY_EXPERIMENT_ID = "experiment_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_SUBJECT = "subject";
    public static final String KEY_COLUMN_NAMES = "column_names";
    public static final String KEY_DATE= "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_STAR_TYPE = "star_type";

    private static final String DATABASE_NAME = "helpEx";
    private static final String TABLE_EXPERIMENT_DETAILS = "ExperimentDetails";
    private static final int DATABASE_VERSION = 1;

    private DbHelper ourHelper, ourHelperRecords;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase, ourDbRecords;

    private static class DbHelper extends SQLiteOpenHelper {

        private Util.DatabaseType databaseType;

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_EXPERIMENT_DETAILS + " (" +
                            KEY_EXPERIMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            KEY_TITLE + " TEXT NOT NULL, " +
                            KEY_SUBJECT + " TEXT NOT NULL, " +
                            KEY_COLUMN_NAMES + " TEXT NOT NULL, " +
                            KEY_STAR_TYPE + " INTEGER NOT NULL, " +
                            KEY_DATE + " TEXT NOT NULL, " +
                            KEY_TIME + " TEXT NOT NULL);"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPERIMENT_DETAILS);
            onCreate(db);
        }
    }

    public DatabaseManager(Context c){
        ourContext = c;
    }
    public DatabaseManager open() throws SQLException {

        ourHelper = new DbHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();

        return this;
    }
    public void close(){
        ourHelper.close();
    }

    public long createEntry(DataExperiment data) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_TITLE, data.title);
        cv.put(KEY_SUBJECT, data.subject);
        cv.put(KEY_COLUMN_NAMES, data.columnNames);
        cv.put(KEY_STAR_TYPE, data.starType.getInt());
        cv.put(KEY_DATE, data.date);
        cv.put(KEY_TIME, data.time);

        Log.d("DatabaseManager", "Created Entry.");
        return ourDatabase.insert(TABLE_EXPERIMENT_DETAILS, null, cv);
    }

    public ArrayList<DataExperiment> getExperimentDetails(){
        String[] columns = new String[]{KEY_EXPERIMENT_ID, KEY_TITLE, KEY_SUBJECT, KEY_COLUMN_NAMES, KEY_STAR_TYPE, KEY_DATE, KEY_TIME};
        Cursor cursor = ourDatabase.query(TABLE_EXPERIMENT_DETAILS, columns, null, null, null, null, null);

        ArrayList<DataExperiment> experimentDetails= new ArrayList<>();
        int iRowId = cursor.getColumnIndex(KEY_EXPERIMENT_ID);
        int iTitle = cursor.getColumnIndex(KEY_TITLE);
        int iSubject = cursor.getColumnIndex(KEY_SUBJECT);
        int iColumnNames = cursor.getColumnIndex(KEY_COLUMN_NAMES);
        int iStarType = cursor.getColumnIndex(KEY_STAR_TYPE);
        int iDate = cursor.getColumnIndex(KEY_DATE);
        int iTime = cursor.getColumnIndex(KEY_TIME);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            DataExperiment data = new DataExperiment(cursor.getLong(iRowId), cursor.getString(iTitle), cursor.getString(iSubject),
                    cursor.getString(iColumnNames), cursor.getString(iDate), cursor.getString(iTime), cursor.getInt(iStarType));
            experimentDetails.add(data);
            Log.d(LOG_TAG, "Entry Id: " + cursor.getInt(iRowId) + " : " + cursor.getString(iTitle));
        }

        Log.d(LOG_TAG, "Size: " + experimentDetails.size());
        cursor.close();
        return experimentDetails;
    }

    public DataExperiment getExperimentDetails(long rowId){
        String[] columns = new String[]{KEY_EXPERIMENT_ID, KEY_TITLE, KEY_SUBJECT, KEY_COLUMN_NAMES, KEY_STAR_TYPE,
                KEY_DATE, KEY_TIME};
        Log.d(LOG_TAG, "SELECT * FROM " + TABLE_EXPERIMENT_DETAILS +
                " WHERE " + KEY_EXPERIMENT_ID + " = " + rowId);
        Cursor cursor = ourDatabase.rawQuery("SELECT * FROM " + TABLE_EXPERIMENT_DETAILS +
                " WHERE " + KEY_EXPERIMENT_ID + " = ?", new String[]{rowId + ""});

        DataExperiment experimentDetail = null;
        int iRowId = cursor.getColumnIndex(KEY_EXPERIMENT_ID);
        int iTitle = cursor.getColumnIndex(KEY_TITLE);
        int iSubject = cursor.getColumnIndex(KEY_SUBJECT);
        int iColumnNames = cursor.getColumnIndex(KEY_COLUMN_NAMES);
        int iStarType = cursor.getColumnIndex(KEY_STAR_TYPE);
        int iDate = cursor.getColumnIndex(KEY_DATE);
        int iTime = cursor.getColumnIndex(KEY_TIME);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            DataExperiment data = new DataExperiment(cursor.getLong(iRowId), cursor.getString(iTitle), cursor.getString(iSubject),
                    cursor.getString(iColumnNames), cursor.getString(iDate), cursor.getString(iTime), cursor.getInt(iStarType));
            experimentDetail = data;
            Log.d(LOG_TAG, "Entry Id: " + cursor.getInt(iRowId) + " : " + cursor.getString(iTitle));
        }

        cursor.close();
        return experimentDetail;
    }

    private boolean isEmpty(){
        String[] columns = new String[]{KEY_EXPERIMENT_ID};
        Cursor c = ourDatabase.query(TABLE_EXPERIMENT_DETAILS, columns, null, null, null, null, null);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            return false;
        }
        return true;
    }

    public void deleteEntry(long row_key) throws SQLException{
        ourDatabase.delete(TABLE_EXPERIMENT_DETAILS, KEY_EXPERIMENT_ID + "=" + row_key, null);
    }

    public long updateEntry(DataExperiment data) {
        try {
            ContentValues cvUpdate = new ContentValues();
            cvUpdate.put(KEY_TITLE, data.title);
            cvUpdate.put(KEY_SUBJECT, data.subject);
            cvUpdate.put(KEY_COLUMN_NAMES, data.columnNames);
            cvUpdate.put(KEY_STAR_TYPE, data.starType.getInt());
            cvUpdate.put(KEY_DATE, data.date);
            cvUpdate.put(KEY_TIME, data.time);
            Log.d(LOG_TAG, "Updates");
            return ourDatabase.update(TABLE_EXPERIMENT_DETAILS, cvUpdate, KEY_EXPERIMENT_ID + "=" + data.experimentID, null);
        }
        catch (Exception e){
            Log.d(LOG_TAG, "Error encountered");
        }
        return 0;
    }

    public void deleteAll(){
        ourDatabase.execSQL("delete from "+ TABLE_EXPERIMENT_DETAILS);
        ourDatabase.execSQL("vacuum");
    }
}