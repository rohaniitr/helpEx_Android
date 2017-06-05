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
 * Created by rohan on 17/3/17.
 */
public class DatabaseRecordsManager {

    String LOG_TAG= this.getClass().getSimpleName();
    public static final String KEY_EXPERIMENT_ID = "experiment_id";
    public static final String KEY_RECORD_ID = "record_id";
    public static final String KEY_RECORD = "record";

    private static final String DATABASE_NAME = "helpEx_records";
    private static final String TABLE_EXPERIMENT_RECORDS = "ExperimentRecords";
    private static final int DATABASE_VERSION = 1;

    private DbHelper ourHelper, ourHelperRecords;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase, ourDbRecords;

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_EXPERIMENT_RECORDS + " (" +
                            KEY_RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            KEY_EXPERIMENT_ID + " INTEGER NOT NULL, " +
                            KEY_RECORD + " TEXT NOT NULL);"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPERIMENT_RECORDS);
            onCreate(db);
        }
    }

    public DatabaseRecordsManager(Context c){
        ourContext = c;
    }
    public DatabaseRecordsManager open() throws SQLException {

        ourHelper = new DbHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();

        return this;
    }
    public void close(){
        ourHelper.close();
    }

    public long createRecord(DataRecord data) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_EXPERIMENT_ID, data.experimentID);
        cv.put(KEY_RECORD, data.record);

        Log.d(LOG_TAG, "Created Entry.");
        return ourDatabase.insert(TABLE_EXPERIMENT_RECORDS, null, cv);
    }

    public ArrayList<DataRecord> getRecords(int experimentID){
        String[] columns = new String[]{KEY_RECORD_ID, KEY_EXPERIMENT_ID, KEY_RECORD};
        String query = "SELECT * FROM " + TABLE_EXPERIMENT_RECORDS + " WHERE " + KEY_EXPERIMENT_ID + " = " + experimentID;
        Cursor c = ourDatabase.rawQuery(query, null);

        ArrayList<DataRecord> experimentRecords= new ArrayList<>();
        int iRecordId = c.getColumnIndex(KEY_RECORD_ID);
        int IExperimentId = c.getColumnIndex(KEY_EXPERIMENT_ID);
        int iRecord = c.getColumnIndex(KEY_RECORD);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            DataRecord data = new DataRecord(c.getInt(iRecordId), c.getInt(IExperimentId), c.getString(iRecord));
            experimentRecords.add(data);
            Log.d(LOG_TAG, "Record Id: " + c.getInt(iRecordId) + " : " + c.getString(iRecord));
        }

        return experimentRecords;
    }

    public int updateRecord(DataRecord data){
        try {
            ContentValues cvUpdate = new ContentValues();
            cvUpdate.put(KEY_EXPERIMENT_ID, data.experimentID);
            cvUpdate.put(KEY_RECORD, data.record);
            Log.d(LOG_TAG, "Updating : " + data.recordId);
            return ourDatabase.update(TABLE_EXPERIMENT_RECORDS, cvUpdate, KEY_RECORD_ID + "=" + data.recordId, null);
        }
        catch (Exception e){
            Log.d(LOG_TAG, "Update for recordId : " +  data.recordId + " failed.");
        }
        return -1;
    }

    public void deleteRecord(long recordId){
        ourDatabase.delete(TABLE_EXPERIMENT_RECORDS, KEY_RECORD_ID + "=" + recordId, null);
        Log.d(LOG_TAG, "Deleted : " + recordId);
    }
}