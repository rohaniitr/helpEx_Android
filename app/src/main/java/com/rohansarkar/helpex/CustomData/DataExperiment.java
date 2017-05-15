package com.rohansarkar.helpex.CustomData;

import Assets.Util;

/**
 * Created by rohan on 14/3/17.
 */
public class DataExperiment {

    public long experimentID;
    public String title;
    public String subject;
    public String columnNames;
    public String date;
    public String time;
    public Util.StarType starType;

    public DataExperiment(long experimentID, String title, String subject, String columnNames, String date, String time, int starType){
        this.experimentID = experimentID;
        this.title = title;
        this.subject = subject;
        this.columnNames = columnNames;
        this.starType = Util.StarType.values()[starType];
        this.date = date;
        this.time = time;
    }

    public DataExperiment(String title, String subject, String columnNames, String date, String time, int starType){
        this.title = title;
        this.subject = subject;
        this.columnNames = columnNames;
        this.starType = Util.StarType.values()[starType];
        this.date = date;
        this.time = time;
    }

    public DataExperiment(){

    }
}
