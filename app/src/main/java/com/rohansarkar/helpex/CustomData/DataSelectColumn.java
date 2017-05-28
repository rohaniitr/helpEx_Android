package com.rohansarkar.helpex.CustomData;

/**
 * Created by rohan on 29/5/17.
 */
public class DataSelectColumn {
    public boolean isSelected;
    public String columnName;

    public DataSelectColumn(){
        isSelected = false;
        columnName = "";
    }
    public DataSelectColumn(String columnName){
        isSelected = false;
        this.columnName = columnName;
    }
    public DataSelectColumn(String columnName, boolean isSelected){
        this.isSelected = isSelected;
        this.columnName = columnName;
    }


}
