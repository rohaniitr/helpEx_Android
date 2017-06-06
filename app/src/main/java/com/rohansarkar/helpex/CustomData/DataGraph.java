package com.rohansarkar.helpex.CustomData;

/**
 * Created by rohan on 6/6/17.
 */
public class DataGraph implements Comparable<DataGraph>{
    public String x;
    public float y;

    public DataGraph(String x, float y){
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(DataGraph dataGraph) {
        return Double.valueOf(x).compareTo(Double.valueOf(dataGraph.x));
    }
}
