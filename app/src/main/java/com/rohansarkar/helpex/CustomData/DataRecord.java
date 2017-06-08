package com.rohansarkar.helpex.CustomData;

/**
 * Created by rohan on 17/3/17.
 */
public class DataRecord {
    public long experimentID;
    public long recordId;
    public String record;

    public DataRecord(long recordId, long experimentID, String record){
        this.recordId = recordId;
        this.experimentID = experimentID;
        this.record = record;
    }

    public DataRecord(long experimentID, String record){
        this.experimentID = experimentID;
        this.record = record;
        this.recordId = recordId;
    }

    public DataRecord() {
        this.recordId = recordId;
    }
}