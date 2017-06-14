package com.rohansarkar.helpex.CustomData;

/**
 * Created by rohan on 29/5/17.
 */
public class DataSelectColumn {
    public boolean isSelected;
    public String text;

    public DataSelectColumn(){
        isSelected = false;
        text = "";
    }
    public DataSelectColumn(String text){
        isSelected = false;
        this.text = text;
    }
    public DataSelectColumn(String text, boolean isSelected){
        this.isSelected = isSelected;
        this.text = text;
    }


}
